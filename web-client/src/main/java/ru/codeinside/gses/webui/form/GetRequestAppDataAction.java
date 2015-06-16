package ru.codeinside.gses.webui.form;

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
import ru.codeinside.gses.beans.ActivitiReceiptContext;
import ru.codeinside.gses.service.F3;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.osgi.Activator;
import ru.codeinside.gses.webui.wizard.ResultTransition;
import ru.codeinside.gses.webui.wizard.TransitionAction;
import ru.codeinside.gws.api.ReceiptContext;
import ru.codeinside.gws.api.Server;
import ru.codeinside.gws.api.ServerResponse;

public class GetRequestAppDataAction implements TransitionAction {
  private final DataAccumulator dataAccumulator;

  GetRequestAppDataAction(DataAccumulator dataAccumulator) {
    this.dataAccumulator = dataAccumulator;
  }

  @Override
  public ResultTransition doIt() throws IllegalStateException {
    try {
      final String serviceName = ProtocolUtils.getServerName(dataAccumulator.getTaskId());
      final ServiceReference reference = ProtocolUtils.getServiceReference(serviceName, Server.class);
      final Server server = ProtocolUtils.getService(reference, Server.class);

      ServerResponse response;
      try {
        response = Fn.withEngine(new GetServerResponse(), Flash.login(), dataAccumulator, server);
      } finally {
        Activator.getContext().ungetService(reference);
      }
      dataAccumulator.setServerResponse(response);

      if (!dataAccumulator.isNeedOv()) {
        //чтобы были ссылки
        dataAccumulator.setSoapMessage(null);
//      dataAccumulator.setResponseId(0L);
      }

      return new ResultTransition(response.appData);

    } catch (Exception e) {
      throw new IllegalStateException("Ошибка получения подготовительных данных: " + e.getMessage(), e);
    }
  }

  final static class GetServerResponse implements F3<ServerResponse, String, DataAccumulator, Server> {
    @Override
    public ServerResponse apply(ProcessEngine engine, String login, DataAccumulator dataAccumulator, Server server) {
      CommandExecutor commandExecutor = ((ServiceImpl) engine.getFormService()).getCommandExecutor();
      return (ServerResponse) commandExecutor.execute(new GetServerResponseCmd(
          dataAccumulator.getTaskId(),
          server,
          dataAccumulator.getRequestType()
      ));
    }
  }

  final private static class GetServerResponseCmd implements Command {
    private final String taskId;
    private final Server service;
    private final String requestType;

    GetServerResponseCmd(String taskId, Server service, String requestType) {
      this.taskId = taskId;
      this.service = service;
      this.requestType = requestType;
    }

    @Override
    public Object execute(CommandContext commandContext) {
      ReceiptContext context;

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
      if ("result".equals(requestType)) {
        response = service.processResult("resultMessage", context);
      } else if ("status".equals(requestType)) {
         response = service.processStatus("", context);//TODO status message прописывать в маршруте?
      } else {
        throw new IllegalStateException("Неправильный тип запроса");
      }

      return response;
    }
  }

}
