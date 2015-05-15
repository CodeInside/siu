package ru.codeinside.gses.webui.form;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.InfoSystemService;
import ru.codeinside.gses.beans.ActivitiExchangeContext;
import ru.codeinside.gses.service.F2;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.wizard.ResultTransition;
import ru.codeinside.gses.webui.wizard.TransitionAction;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientRequest;

import java.util.List;

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
    InfoSystemService service = validateAndGetService(serviceName);
    Client client = AdminServiceProvider.get().getClientRefByNameAndVersion(service.getName(), service.getSversion()).getRef();
    dataAccumulator.setClient(client);

    ClientRequest request = Fn.withEngine(new GetClientRequest(), Flash.login(), dataAccumulator);
    dataAccumulator.setClientRequest(request);

    return new ResultTransition(request.appData);
  }

  private InfoSystemService validateAndGetService(String serviceName) {
    List<InfoSystemService> services = AdminServiceProvider.get().getInfoSystemServiceBySName(serviceName);
    if (services == null || services.isEmpty()) {
      throw new IllegalStateException("Нет модуля потребителя СМЭВ с именем '" + serviceName + "'");
    }
    return getServiceWithMaxVersion(services);
  }

  private InfoSystemService getServiceWithMaxVersion(List<InfoSystemService> services) {
    InfoSystemService curService = null;
    for (InfoSystemService s : services) {
      if (curService == null) {
        curService = s;
      }
      if (s.getSversion().compareTo(curService.getSversion()) >= 0) {
        curService = s;
      }
    }
    return curService;
  }

  final private static class GetClientRequest implements F2<ClientRequest, String, DataAccumulator> {
    @Override
    public ClientRequest apply(ProcessEngine engine, String login, DataAccumulator dataAccumulator) {

      CommandExecutor commandExecutor = ((ServiceImpl) engine.getFormService()).getCommandExecutor();
      DelegateExecution execution = (DelegateExecution) commandExecutor.execute(new GetExecutionCmd(dataAccumulator.getTaskId()));

      return dataAccumulator.getClient().createClientRequest(new ActivitiExchangeContext(execution));
    }
  }

  final private static class GetExecutionCmd implements Command {
    private final String taskId;

    GetExecutionCmd(String taskId) {
      this.taskId = taskId;
    }

    @Override
    public Object execute(CommandContext commandContext) {
      commandContext.getVariableInstanceManager().findVariableInstancesByExecutionId("");
      String processInstanceId = AdminServiceProvider.get().getBidByTask(taskId).getProcessInstanceId();

      return Context.getCommandContext()
          .getExecutionManager()
          .findExecutionById(processInstanceId);
    }
  }
}
