package ru.codeinside.gses.webui.form;

import com.vaadin.ui.Form;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.osgi.framework.ServiceReference;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.ExternalGlue;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.activiti.forms.SubmitFormCmd;
import ru.codeinside.gses.beans.ActivitiReceiptContext;
import ru.codeinside.gses.service.F3;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.form.api.FieldValuesSource;
import ru.codeinside.gses.webui.osgi.Activator;
import ru.codeinside.gses.webui.wizard.ResultTransition;
import ru.codeinside.gses.webui.wizard.TransitionAction;
import ru.codeinside.gws.api.CryptoProvider;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.Server;
import ru.codeinside.gws.api.ServerResponse;
import ru.codeinside.gws.api.XmlNormalizer;
import ru.codeinside.gws.api.XmlSignatureInjector;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class GetRequestAppDataAction implements TransitionAction {
  private final DataAccumulator dataAccumulator;

  GetRequestAppDataAction(DataAccumulator dataAccumulator) {
    this.dataAccumulator = dataAccumulator;
  }

  @Override
  public ResultTransition doIt() throws IllegalStateException {
    try {
      if (dataAccumulator.getTaskId() == null) {
        throw new IllegalStateException("Отсутствует taskId");
      }

      final String serviceName = ProtocolUtils.getServerName(dataAccumulator.getTaskId());
      final ServiceReference reference = ProtocolUtils.getServiceReference(serviceName, Server.class);
      if (reference == null) {
        throw new IllegalStateException("Не удалось получить ссылку на сервис " + serviceName);
      }

      final Server server = ProtocolUtils.getService(reference, Server.class);
      if (server == null) {
        throw new IllegalStateException("Поставщик " + serviceName + " недоступен");
      }

      ServerResponse response;
      try {
        response = Fn.withEngine(new GetServerResponse(), Flash.login(), dataAccumulator, server);
        dataAccumulator.setServerResponse(response);
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
        signedInfoBytes = injector.prepareAppData(response, isSignatureLast, normalizer, cryptoProvider);
      } finally {
        Activator.getContext().ungetService(normalizerReference);
        Activator.getContext().ungetService(cryptoReference);
        Activator.getContext().ungetService(injectorReference);
      }
      return new ResultTransition(new SignData(signedInfoBytes, response.attachmens));
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalStateException("Ошибка получения подготовительных данных: " + e.getMessage(), e);
    }
  }

  final static class GetServerResponse implements F3<ServerResponse, String, DataAccumulator, Server> {
    @Override
    public ServerResponse apply(ProcessEngine engine, String login, DataAccumulator dataAccumulator, Server server) {
      CommandExecutor commandExecutor = ((ServiceImpl) engine.getFormService()).getCommandExecutor();
      List<Form> forms = dataAccumulator.getForms();
      if (forms.size() > 0) {
        Map<String, Object> properties = ((FieldValuesSource)forms.get(0)).getFieldValues();
        commandExecutor.execute(
            new SubmitFormCmd(FormID.byTaskId(dataAccumulator.getTaskId()), properties, null, false, dataAccumulator));
      }
      return commandExecutor.execute(new GetServerResponseCmd(dataAccumulator, server));
    }
  }

  final private static class GetServerResponseCmd implements Command<ServerResponse> {
    private final DataAccumulator dataAccumulator;
    private final Server server;

    GetServerResponseCmd(DataAccumulator dataAccumulator, Server server) {
      this.dataAccumulator = dataAccumulator;
      this.server = server;
    }

    @Override
    public ServerResponse execute(CommandContext commandContext) {
      ActivitiReceiptContext context;

      String taskId = dataAccumulator.getTaskId();
      String requestType = dataAccumulator.getRequestType();

      if (taskId != null) {
        Bid bid = AdminServiceProvider.get().getBidByTask(taskId);
        final String processInstanceId = bid.getProcessInstanceId();

        DelegateExecution execution = Context.getCommandContext()
            .getExecutionManager()
            .findExecutionById(processInstanceId);
        context = new ActivitiReceiptContext(execution, bid.getId());
      } else {
        throw new IllegalStateException("Task is null");
      }

      ServerResponse response;
      String responseMessage = dataAccumulator.getResponseMessage();
      if ("result".equals(requestType)) {
        if (responseMessage == null || responseMessage.isEmpty()) {
          responseMessage = "Исполнено";
        }
        response = server.processResult(responseMessage, context);
      } else if ("status".equals(requestType)) {
        if (responseMessage == null || responseMessage.isEmpty()) {
          responseMessage = "Статус";
        }
        response = server.processStatus(responseMessage, context);
      } else {
        throw new IllegalStateException("Неправильный тип запроса");
      }

      dataAccumulator.setUsedEnclosures(context.getUsedEnclosures());

      fillResponsePacket(response.packet);

      return response;
    }

    private void fillResponsePacket(Packet packet) {
      Bid bid = AdminServiceProvider.get().getBidByTask(dataAccumulator.getTaskId());

      ExternalGlue glue = bid.getGlue();

      ru.codeinside.adm.database.InfoSystem sender;
      ru.codeinside.adm.database.InfoSystem originator;
      ru.codeinside.adm.database.InfoSystem recipient;
      if (glue != null &&
          (sender = glue.getSender()) != null &&
          (originator = glue.getOrigin()) != null &&
          (recipient = glue.getRecipient()) != null) {
        packet.sender = new InfoSystem(recipient.getCode(), recipient.getName());
        packet.recipient = new InfoSystem(sender.getCode(), sender.getName());
        packet.originator = new InfoSystem(originator.getCode(), originator.getName());
      } else {
        throw new IllegalStateException("Нет связи с внешней услугой");
      }

      packet.originRequestIdRef = glue.getRequestIdRef();
      packet.requestIdRef = packet.originRequestIdRef;

      if (packet.date == null) {
        packet.date = new Date();
      }
    }
  }

}
