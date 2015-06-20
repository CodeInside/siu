package ru.codeinside.gses.webui.form;

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
import ru.codeinside.gses.activiti.forms.SubmitFormDataCmd;
import ru.codeinside.gses.beans.ActivitiExchangeContext;
import ru.codeinside.gses.beans.StartFormExchangeContext;
import ru.codeinside.gses.service.F3;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.osgi.Activator;
import ru.codeinside.gses.webui.wizard.ResultTransition;
import ru.codeinside.gses.webui.wizard.TransitionAction;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ExchangeContext;

import java.util.HashMap;
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
      final Client client = ProtocolUtils.getService(reference, Client.class);

      final ClientRequest request;
      try {
        request = Fn.withEngine(new GetClientRequest(), Flash.login(), dataAccumulator, client);
      } finally {
        Activator.getContext().ungetService(reference);
      }

      dataAccumulator.setClientRequest(request);

      if (!dataAccumulator.isNeedOv()) {
        //чтобы были ссылки
        dataAccumulator.setSoapMessage(null);
        dataAccumulator.setRequestId(0L);
      }

      return new ResultTransition(request.appData);

    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalStateException("Ошибка получения подготовительных данных: " + e.getMessage(), e);
    }
  }

  final static class GetClientRequest implements F3<ClientRequest, String, DataAccumulator, Client> {
    @Override
    public ClientRequest apply(ProcessEngine engine, String login, DataAccumulator dataAccumulator, Client client) {
      CommandExecutor commandExecutor = ((ServiceImpl) engine.getFormService()).getCommandExecutor();
      return (ClientRequest) commandExecutor.execute(new GetClientRequestCmd(
          dataAccumulator,
          client
      ));
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
        new SubmitFormDataCmd(
            dataAccumulator.getPropertyTree(),
            variableScope,
            getFieldValues(),
            null, //Signatures. Предполаем, что signature и dataflow вместе не используются
            new StartEventAttachmentConverter()).execute(commandContext);

        context = new StartFormExchangeContext(variableScope, dataAccumulator);
      }

      ClientRequest request = client.createClientRequest(context);
      ProtocolUtils.fillRequestPacket(request, dataAccumulator.getServiceName());
      
      return request;
    }

    private Map<String, Object> getFieldValues() {
      Map<String, Object> properties = new HashMap<String, Object>();
      for (FormField field : dataAccumulator.getFormFields()) {
        properties.put(field.getPropId(), field.getValue());
      }
      return properties;
    }
  }
}
