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
import ru.codeinside.gses.beans.StartFormExchangeContext;
import ru.codeinside.gses.service.F2;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.osgi.Activator;
import ru.codeinside.gses.webui.wizard.ResultTransition;
import ru.codeinside.gses.webui.wizard.TransitionAction;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ExchangeContext;

import java.util.List;

public class GetAppDataAction implements TransitionAction {

  private final String serviceName;
  private final DataAccumulator dataAccumulator;

  GetAppDataAction(final DataAccumulator dataAccumulator) {
    this.serviceName = dataAccumulator.getServiceName();
    this.dataAccumulator = dataAccumulator;
  }

  /**
   * Выполнить действие перехода.
   */
  @Override
  public ResultTransition doIt() throws IllegalStateException {
    final ServiceReference reference = getServiceReference(serviceName);
    final Client client = getClient(reference);
    dataAccumulator.setClient(client);

    final ClientRequest request;
    try {
      request = Fn.withEngine(new GetClientRequest(), Flash.login(), dataAccumulator);
    } finally {
      Activator.getContext().ungetService(reference);
    }
    dataAccumulator.setClientRequest(request);

    return new ResultTransition(request.appData);
  }

  static ServiceReference getServiceReference(String serviceName) {
    ServiceReference[] references;
    String filter = "(&(component.name=" + serviceName + "))";
    try {
      references = Activator.getContext().getAllServiceReferences(Client.class.getName(), filter);
    } catch (InvalidSyntaxException e) {
      throw new IllegalStateException("Не удаётся получить ссылку на сервис " + serviceName);
    }

    ServiceReference reference = null;
    if (references.length == 1) {
      reference = references[0];
    } else if (references.length > 1) { // Если есть несколько клиентов с таким именем, берём тот, у которого выше версия
      for (ServiceReference comparedReference : references) {
        if (reference == null) {
          reference = comparedReference;
        }

        int comparedReferenceId = Integer.valueOf(comparedReference.getProperty("service.id").toString());
        int referenceId = Integer.valueOf(reference.getProperty("service.id").toString());

        if (comparedReferenceId > referenceId) {
          reference = comparedReference;
        }
      }
    } else if (references.length < 1) {
      throw new IllegalStateException("Клиент " + serviceName + " не найден");
    }

    return reference;
  }

  static Client getClient(ServiceReference reference) {
    return  (Client) Activator.getContext().getService(reference);
  }

  final static class GetClientRequest implements F2<ClientRequest, String, DataAccumulator> {
    @Override
    public ClientRequest apply(ProcessEngine engine, String login, DataAccumulator dataAccumulator) {
      CommandExecutor commandExecutor = ((ServiceImpl) engine.getFormService()).getCommandExecutor();
      return (ClientRequest) commandExecutor.execute(new GetClientRequestCmd(
          dataAccumulator.getTaskId(),
          dataAccumulator.getClient(),
          dataAccumulator.getFormFields()
      ));
    }
  }

  final private static class GetClientRequestCmd implements Command {
    private final String taskId;
    private final Client client;
    private final List<FormField> formFields;

    GetClientRequestCmd(String taskId, Client client, List<FormField> formFields) {
      this.taskId = taskId;
      this.client = client;
      this.formFields = formFields;
    }

    @Override
    public Object execute(CommandContext commandContext) {
      ExchangeContext context;

      if (taskId != null) {
        final String processInstanceId = AdminServiceProvider.get().getBidByTask(taskId).getProcessInstanceId();

        DelegateExecution execution = Context.getCommandContext()
            .getExecutionManager()
            .findExecutionById(processInstanceId);
        context = new ActivitiExchangeContext(execution);
      } else {
        context = new StartFormExchangeContext();
        setContextVariables(context);
      }

      return client.createClientRequest(context);
    }

    //TODO записать данные в контекст
    private void setContextVariables(ExchangeContext context) {
      for (FormField field : formFields) {
        context.setVariable(field.getPropId(), field.getValue());
      }
    }
  }
}
