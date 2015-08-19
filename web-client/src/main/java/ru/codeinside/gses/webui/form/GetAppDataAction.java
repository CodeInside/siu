package ru.codeinside.gses.webui.form;

import com.google.common.collect.Maps;
import com.vaadin.ui.Form;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.VariableScope;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.osgi.framework.ServiceReference;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.activiti.forms.Signatures;
import ru.codeinside.gses.activiti.forms.SubmitFormCmd;
import ru.codeinside.gses.activiti.forms.SubmitFormDataCmd;
import ru.codeinside.gses.beans.ActivitiExchangeContext;
import ru.codeinside.gses.beans.StartFormExchangeContext;
import ru.codeinside.gses.service.F3;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.form.api.FieldValuesSource;
import ru.codeinside.gses.webui.osgi.Activator;
import ru.codeinside.gses.webui.wizard.ResultTransition;
import ru.codeinside.gses.webui.wizard.TransitionAction;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.CryptoProvider;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.XmlNormalizer;
import ru.codeinside.gws.api.XmlSignatureInjector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAppDataAction implements TransitionAction {

  private final String serviceName;
  private final DataAccumulator dataAccumulator;

  GetAppDataAction(final DataAccumulator dataAccumulator) {
    this.serviceName = dataAccumulator.getServiceName();
    this.dataAccumulator = dataAccumulator;
  }

  @Override
  public ResultTransition doIt() throws IllegalStateException {
    try {
      final ServiceReference reference = ProtocolUtils.getServiceReference(serviceName, Client.class);
      if (reference == null) {
        throw new IllegalStateException("Не удалось получить ссылку на сервис " + dataAccumulator.getServiceName());
      }

      final Client client = ProtocolUtils.getService(reference, Client.class);
      if (client == null) {
        throw new IllegalStateException("Клиент " + dataAccumulator.getServiceName() + " недоступен");
      }

      final ClientRequest request;
      try {
        request = Fn.withEngine(new GetClientRequest(), Flash.login(), dataAccumulator, client);
        dataAccumulator.setClientRequest(request);
      } finally {
        Activator.getContext().ungetService(reference);
      }

      final ServiceReference normalizerReference = Activator.getContext().getServiceReference(XmlNormalizer.class.getName());
      final ServiceReference cryptoReference = Activator.getContext().getServiceReference(CryptoProvider.class.getName());
      final ServiceReference injectorReference = Activator.getContext().getServiceReference(XmlSignatureInjector.class.getName());
      byte[] signedInfoBytes = null;
      try {
        XmlNormalizer normalizer = (XmlNormalizer) Activator.getContext().getService(normalizerReference);
        if (normalizer == null) {
          throw new IllegalStateException("Сервис нормализации не доступен.");
        }
        CryptoProvider cryptoProvider = (CryptoProvider) Activator.getContext().getService(cryptoReference);
        if (cryptoProvider == null) {
          throw new IllegalStateException("Сервис криптографии не доступен.");
        }
        XmlSignatureInjector injector = (XmlSignatureInjector) Activator.getContext().getService(injectorReference);
        if (injector == null) {
          throw new IllegalStateException("Сервис внедрения подписи не доступен.");
        }
        boolean isSignatureLast = dataAccumulator.getPropertyTree().isAppDataSignatureBlockLast();
        signedInfoBytes = injector.prepareAppData(request, isSignatureLast, normalizer, cryptoProvider);
      } finally {
        Activator.getContext().ungetService(normalizerReference);
        Activator.getContext().ungetService(cryptoReference);
        Activator.getContext().ungetService(injectorReference);
      }
      return new ResultTransition(new SignData(signedInfoBytes, request.enclosures));
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalStateException("Ошибка получения подготовительных данных: " + e.getMessage(), e);
    }
  }

  final static class GetClientRequest implements F3<ClientRequest, String, DataAccumulator, Client> {
    @Override
    public ClientRequest apply(ProcessEngine engine, String login, DataAccumulator dataAccumulator, Client client) {
      CommandExecutor commandExecutor = ((ServiceImpl) engine.getFormService()).getCommandExecutor();
      List<Form> forms = dataAccumulator.getForms();
      if (dataAccumulator.getTaskId() != null && forms.size() > 0) {
        Map<String, Object> properties = ((FieldValuesSource)forms.get(0)).getFieldValues();
        commandExecutor.execute(
            new SubmitFormCmd(FormID.byTaskId(dataAccumulator.getTaskId()), properties, null, false, dataAccumulator));
      }
      return (ClientRequest) commandExecutor.execute(new GetClientRequestCmd(dataAccumulator, client));
    }
  }

  final private static class GetClientRequestCmd implements Command {
    private final DataAccumulator dataAccumulator;
    private final Client client;

    GetClientRequestCmd(DataAccumulator dataAccumulator, Client client) {
      this.dataAccumulator = dataAccumulator;
      this.client = client;
    }

    @Override
    public Object execute(CommandContext commandContext) {
      ExchangeContext context;
      String taskId = dataAccumulator.getTaskId();

      if (taskId != null) {
        final String processInstanceId = AdminServiceProvider.get().getBidByTask(taskId).getProcessInstanceId();

        DelegateExecution execution = Context.getCommandContext()
            .getExecutionManager()
            .findExecutionById(processInstanceId);
        context = new ActivitiExchangeContext(execution);
      } else {
        VariableScope variableScope = new StartEventVariableScope();

        Map<SignatureType, Signatures> signatures = null;
        if (dataAccumulator.getSignatures() != null) {
          signatures = new HashMap<SignatureType, Signatures>();
          signatures.put(SignatureType.FIELDS, dataAccumulator.getSignatures());
        }

        new SubmitFormDataCmd(
            dataAccumulator.getPropertyTree(),
            variableScope,
            getFieldValues(),
            signatures,
            new StartEventAttachmentConverter(dataAccumulator),
            dataAccumulator).execute(commandContext);

        context = new StartFormExchangeContext(variableScope, dataAccumulator);
        dataAccumulator.setTempContext(context);
      }

      ProtocolUtils.writeInfoSystemsToContext(dataAccumulator.getServiceName(), context);
      ClientRequest request = client.createClientRequest(context);
      ProtocolUtils.fillRequestPacket(request, dataAccumulator.getServiceName());

      return request;
    }

    private Map<String, Object> getFieldValues() {
      List<Form> forms = dataAccumulator.getForms();
      if (forms.size() > 0) {
        return ((FieldValuesSource)forms.get(0)).getFieldValues();
      }
      return Maps.newHashMap();
    }
  }
}
