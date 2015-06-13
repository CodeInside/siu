package ru.codeinside.gses.webui.form;

import org.activiti.engine.delegate.BpmnError;
import org.osgi.framework.ServiceReference;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.ExternalGlue;
import ru.codeinside.gses.webui.wizard.ResultTransition;
import ru.codeinside.gses.webui.wizard.TransitionAction;
import ru.codeinside.gws.api.Server;

public class GetRequestAppDataAction implements TransitionAction {
  private final DataAccumulator dataAccumulator;

  GetRequestAppDataAction(DataAccumulator dataAccumulator) {
    this.dataAccumulator = dataAccumulator;
  }

  @Override
  public ResultTransition doIt() throws IllegalStateException {
    String serviceName = getServiceName();

    final ServiceReference reference = FormSeqUtils.getServiceReference(serviceName, Server.class);
    final Server service = FormSeqUtils.getService(reference, Server.class);

    if ("result".equals(dataAccumulator.getRequestType())) {
      // TODO получить ServerResponse (service.processResult())
    } else if ("status".equals(dataAccumulator.getRequestType())) {
      // TODO получить ServerResponse (service.processStatus())
    } else {
      throw new IllegalStateException("Неправильный тип запроса");
    }


    throw new IllegalStateException("FIN");
  }

  private String getServiceName() {
    Bid bid;
    if (dataAccumulator.getTaskId() != null) {
      bid = AdminServiceProvider.get().getBidByTask(dataAccumulator.getTaskId());
    } else {
      throw new IllegalStateException("Task id is null");
    }

    ExternalGlue glue = bid.getGlue();

    if (glue == null) {
      throw new BpmnError("Нет связи с внешней услугой");
    }

    return glue.getName();
  }




}
