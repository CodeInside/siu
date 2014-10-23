package ru.codeinside.gses.beans;

import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.DelegateExecution;

import ru.codeinside.adm.AdminService;
import ru.codeinside.gws.api.Enclosure;

@Named("requestPreparationService")
@Singleton
public class RequestPreparationService {

  static Logger log = Logger.getLogger(RequestPreparationService.class.getName());

  @Inject
  AdminService adminService;

  @Inject
  Instance<ProcessEngine> processEngine;

  public void init() {
    log.info("======================\n" + "RequestPreparationService inited\n" + "======================");
  }

  void logVars(Map<String, Object> vars) {
    for (Entry<String, Object> e : vars.entrySet()) {
      log.fine(String.format("%s = %s[class = %s]", e.getKey(), e.getValue(), e.getValue().getClass()));
    }

  }

  public void prepareRequest(DelegateExecution execution) {

    log.info("======================\n" + "prepareRequest invoked\n" + "======================");

    log.fine("variables");
    logVars(execution.getVariables());

    log.fine("local variables");
    logVars(execution.getVariablesLocal());

    log.fine("=======");

    log.fine("aeCtx variables");
    ActivitiExchangeContext aeCtx = new ActivitiExchangeContext(execution);
    Enclosure enclosure = aeCtx.getEnclosure("attachment");
    log.fine("enclosure = " + enclosure);

    log.fine("=======");

  }

}
