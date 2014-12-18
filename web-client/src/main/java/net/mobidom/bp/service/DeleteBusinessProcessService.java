package net.mobidom.bp.service;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.DelegateExecution;

import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.BidStatus;
import ru.codeinside.gses.activiti.Activiti;
import ru.codeinside.gses.beans.ActivitiBean;

@Named("deleteBusinessProcessService")
@Singleton
public class DeleteBusinessProcessService {

  static Logger log = Logger.getLogger(DeleteBusinessProcessService.class.getName());

  @Inject
  Instance<ProcessEngine> processEngine;

  public void execute(DelegateExecution execution) {
    String bid = (String) execution.getVariable("bid");
    log.info("need to cancel bid: " + bid);

    List<Bid> bidList = Activiti.getEm().createQuery("SELECT b FROM Bid b WHERE b.id = :bid and b.status = :status", Bid.class)
        .setParameter("bid", Long.valueOf(bid)).setParameter("status", BidStatus.New).getResultList();

    if (bidList == null || bidList.isEmpty()) {
      throw new RuntimeException("can't find request for id = " + bid);
    }

    Bid bidInstance = bidList.get(0);
    log.info("bid " + bid + " status " + bidInstance.getStatus() + " - " + bidInstance.getStatus().getName());
    String pid = bidInstance.getProcessInstanceId();
    log.info("need to cancel pid: " + pid);

    processEngine.get().getRuntimeService().deleteProcessInstance(pid, null);
//    ActivitiBean.get().deleteProcessInstance(pid);
    log.info("deleted by pid = " + pid);

  }

}
