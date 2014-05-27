/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm;


import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.Task;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.TaskDates;

import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;

@TransactionManagement
@TransactionAttribute
@Singleton
@Stateless
@DependsOn("BaseBean")
public class CheckExecutionDates {

  @PersistenceContext(unitName = "myPU")
  EntityManager em;

  final Logger logger = Logger.getLogger(getClass().getName());

  @TransactionAttribute(REQUIRES_NEW)
  public void checkDates(ProcessEngine processEngine) {
    List<Task> list = processEngine.getTaskService().createTaskQuery().list();
    List<Task> overdueTasks = new ArrayList<Task>();
    List<Bid> overdueBids = new ArrayList<Bid>();
    List<Task> endingTasks = new ArrayList<Task>();
    List<Bid> endingBids = new ArrayList<Bid>();
    List<Task> inactionTasks = new ArrayList<Task>();
    Date currentDate = new Date();
    for (Task task : list) {
      TaskDates td = em.createQuery("select td from TaskDates td where td.id = :id", TaskDates.class)
        .setParameter("id", task.getId())
        .getSingleResult();
      if (task.getAssignee() == null) {
        if (currentDate.before(td.getAssignDate())) {
          inactionTasks.add(task);
        }
      } else {
        if (currentDate.before(td.getMaxDate())) {
          overdueTasks.add(task);
        } else if (currentDate.before(td.getRestDate())) {
          endingTasks.add(task);
        }
      }
      Bid bid = em.createQuery("select b from Bid b where b.processInstanceId = :pid", Bid.class)
        .setParameter("pid", task.getProcessInstanceId())
        .getSingleResult();
      if (currentDate.before(bid.getMaxDate())) {
        overdueBids.add(bid);
      } else if (currentDate.before(bid.getRestDate())) {
        endingBids.add(bid);
      }
    }
  }
}
