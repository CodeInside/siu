package ru.codeinside.gses.webui.form;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.beans.ActivitiExchangeContext;
import ru.codeinside.gses.service.F2;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.osgi.Activator;
import ru.codeinside.gses.webui.wizard.ResultTransition;
import ru.codeinside.gses.webui.wizard.TransitionAction;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientRequest;

public class GetAppDataAction implements TransitionAction {

  private final String serviceName;
  private final DataAccumulator dataAccumulator;

  GetAppDataAction(String serviceName, DataAccumulator dataAccumulator) {
    this.serviceName = serviceName;
    this.dataAccumulator = dataAccumulator;
  }

  /**
   * Выполнить действие перехода.
   */
  @Override
  public ResultTransition doIt() throws IllegalStateException {

    ServiceReference[] references = new ServiceReference[0];
    String filter = "(&(component.name=" + serviceName + "))";
    try {
      references = Activator.getContext().getAllServiceReferences(Client.class.getName(), filter);
    } catch (InvalidSyntaxException e) {
      e.printStackTrace();
    }

    if (references.length != 1) {
      throw new IllegalStateException("Клиент " + serviceName + " не найден");
    }

    ServiceReference ref = references[0];
    final Client client = (Client) Activator.getContext().getService(ref);
    Activator.getContext().ungetService(ref);
    dataAccumulator.setClient(client);

    final ClientRequest request = Fn.withEngine(new GetClientRequest(), Flash.login(), dataAccumulator);
    dataAccumulator.setClientRequest(request);

    return new ResultTransition(request.appData);
  }

  final private static class GetClientRequest implements F2<ClientRequest, String, DataAccumulator> {
    @Override
    public ClientRequest apply(ProcessEngine engine, String login, DataAccumulator dataAccumulator) {
      CommandExecutor commandExecutor = ((ServiceImpl) engine.getFormService()).getCommandExecutor();
      return (ClientRequest) commandExecutor.execute(new GetClientRequestCmd(dataAccumulator.getTaskId(), dataAccumulator.getClient()));
    }
  }

  final private static class GetClientRequestCmd implements Command {
    private final String taskId;
    private final Client client;

    GetClientRequestCmd(String taskId, Client client) {
      this.taskId = taskId;
      this.client = client;
    }

    @Override
    public Object execute(CommandContext commandContext) {
      final String processInstanceId = AdminServiceProvider.get().getBidByTask(taskId).getProcessInstanceId();

      DelegateExecution execution = Context.getCommandContext()
          .getExecutionManager()
          .findExecutionById(processInstanceId);

      return client.createClientRequest(new ActivitiExchangeContext(execution));
    }
  }
}
