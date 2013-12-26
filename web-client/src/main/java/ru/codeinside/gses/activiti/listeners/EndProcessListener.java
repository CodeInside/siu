/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.listeners;

import com.google.common.base.Objects;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.pvm.delegate.ExecutionListenerExecution;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.BidStatus;
import ru.codeinside.adm.database.DefinitionStatus;
import ru.codeinside.gses.activiti.Activiti;
import ru.codeinside.gses.activiti.ReceiptEnsurance;
import ru.codeinside.gses.beans.ActivitiBean;
import ru.codeinside.gses.manager.ManagerService;
import ru.codeinside.gses.webui.Flash;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

final public class EndProcessListener implements ExecutionListener {

  final private Logger logger = Logger.getLogger(getClass().getName());
  final private boolean endEvent;
  final private ReceiptEnsurance receiptEnsurance;

  public EndProcessListener(boolean endEvent, ReceiptEnsurance receiptEnsurance) {
    this.endEvent = endEvent;
    this.receiptEnsurance = receiptEnsurance;
  }

  @Override
  public void notify(DelegateExecution execution) throws Exception {

    final String executionId = execution.getId();
    final boolean forcedDelete = detectForcedDelete(execution);

    AdminServiceProvider.get()
      .createLog(Flash.getActor(), "execution", executionId, endEvent ? "end" : "complete", forcedDelete ? "forced" : null,
                 true);

    final Bid bid = getBid(executionId);
    if (bid == null) {
      logger.info("Завершение исполнения " + executionId + " вне заявки.");
      return;
    }

    bid.setStatus(BidStatus.Executed);
    bid.setDateFinished(new Date());
    bid.setComment(forcedDelete ? "Удалена оператором" : "");
    Activiti.getEm().merge(bid);

    if (DefinitionStatus.PathToArchive.equals(bid.getProcedureProcessDefinition().getStatus())) {
      String processDefinitionId = bid.getProcedureProcessDefinition().getProcessDefinitionId();
      if (Flash.flash().getProcessEngine().getRuntimeService().createProcessInstanceQuery().processDefinitionId(processDefinitionId).count() <= 1) {
        ManagerService.get().updateProcessDefinationStatus(processDefinitionId, DefinitionStatus.Archive, 1);
      }
    }

    // Завершение имеет смысл лишь кода мы обнаружили заявку, когда executionId==processInstanceId,
    // в других случаях может завершаться дочерний процесс.
    // endEvent==true  - обработчик блока endEvent.
    // endEvent==false - обработчик завершения процесса.
    if (!endEvent) {
      if (receiptEnsurance != null) {
        receiptEnsurance.completeReceipt(execution);
      }
      AdminServiceProvider.get().createLog(Flash.getActor(), "Bid", bid.getId().toString(), "complete", null, true);
    }
  }

  private boolean detectForcedDelete(final DelegateExecution execution) {
    if (!(execution instanceof ExecutionListenerExecution)) {
      return false;
    }
    return Objects.equal(ActivitiBean.FORCED_DELETE, ((ExecutionListenerExecution) execution).getDeleteReason());
  }

  private Bid getBid(final String executionId) {
    final List<Bid> bids = Activiti.getEm()
      .createQuery("select b from Bid b where b.processInstanceId = :processInstanceId", Bid.class)
      .setParameter("processInstanceId", executionId).getResultList();
    if (bids.isEmpty()) {
      return null;
    }
    return bids.get(0);
  }
}
