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

@Named("deleteAllBusinessProcessesService")
@Singleton
public class DeleteAllBusinessProcessesService {

  static Logger log = Logger.getLogger(DeleteAllBusinessProcessesService.class.getName());

  @Inject
  Instance<ProcessEngine> processEngine;

  public void execute(DelegateExecution execution) {

    List<Bid> bidList = Activiti.getEm().createQuery("SELECT b FROM Bid b WHERE b.status = :status1 or b.status = :status2", Bid.class)
        .setParameter("status1", BidStatus.New).setParameter("status2", BidStatus.Execute).getResultList();

    if (bidList == null || bidList.isEmpty()) {
      return;
    }

    for (Bid bid : bidList) {

      log.info("bid " + bid.getId() + " status " + bid.getStatus());
      String pid = bid.getProcessInstanceId();
      if (execution.getProcessInstanceId() == pid) {
        continue;
      }

      log.info("need to cancel pid: " + pid);
      try {
        processEngine.get().getRuntimeService().deleteProcessInstance(pid, null);
        log.info("deleted by pid = " + pid);
      } catch (Exception e) {
        log.info("can't delete pid " + pid);
      }
    }
  }
}
