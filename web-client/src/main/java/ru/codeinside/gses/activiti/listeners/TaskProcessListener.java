/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.listeners;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.TaskDates;
import ru.codeinside.gses.activiti.Activiti;
import ru.codeinside.gses.activiti.forms.CustomTaskFormHandler;
import ru.codeinside.gses.webui.Flash;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

public class TaskProcessListener implements TaskListener {
  @Override
  public void notify(final DelegateTask execution) {
    final Event event = Event.valueOf(execution);
    Bid firstBid = null;
    EntityManager em = Activiti.getEm();
    List<Bid> resultList = em
      .createQuery("select e from Bid e where e.processInstanceId = :processInstanceId", Bid.class)
      .setParameter("processInstanceId", execution.getProcessInstanceId()).getResultList();
    for (final Bid bid : resultList) {
      if (firstBid == null) {
        firstBid = bid;
      }
      if (event == Event.Create) {
        bid.getCurrentSteps().add(execution.getId());
        TaskDates task = new TaskDates();
        task.setId(execution.getId());
        task.setBid(bid);
        task.setStartDate(execution.getCreateTime());
        ((CustomTaskFormHandler) ((TaskEntity) execution).getTaskDefinition().getTaskFormHandler()).setInactionDate(task);
        em.persist(task);
      } else if (event == Event.Complete) {
        bid.getCurrentSteps().remove(execution.getId());
      }
    }

    String info = null;
    String action = null;
    if (event == Event.Complete) {
      if (firstBid != null && firstBid.getProcedure() != null) {
        info = "procedureId: " + firstBid.getProcedure().getId();
      }
      action = "complete";
    } else if (event == Event.Assignment) {
      TaskDates task = em.find(TaskDates.class, execution.getId());
      if (task.getAssignDate() == null) {
        task.setAssignDate(new Date());
        ((CustomTaskFormHandler) ((TaskEntity) execution).getTaskDefinition().getTaskFormHandler()).setExecutionDate(task);
        em.persist(task);
      }
      info = "assigned: " + execution.getAssignee();
      if (firstBid != null && firstBid.getProcedure() != null) {
        info += ", procedureId: " + firstBid.getProcedure().getId();
      }
      action = "assign";
    }
    if (event == Event.Assignment || event == Event.Complete) {
      AdminServiceProvider.get().createLog(Flash.getActor(), "task", execution.getId(), action, info, true);
    }
  }
}