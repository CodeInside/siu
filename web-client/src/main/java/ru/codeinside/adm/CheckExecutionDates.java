/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm;


import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.TaskDates;
import ru.codeinside.gses.API;

import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
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
  public Email checkDates(ProcessEngine processEngine) throws EmailException {
    Set<Task> overdueTasks = new HashSet<Task>();
    Set<Bid> overdueBids = new HashSet<Bid>();
    Set<Task> endingTasks = new HashSet<Task>();
    Set<Bid> endingBids = new HashSet<Bid>();
    Set<Task> inactionTasks = new HashSet<Task>();

    check(processEngine, overdueTasks, overdueBids, endingTasks, endingBids, inactionTasks);

    String emailTo = get(API.EMAIL_TO);
    String receiverName = get(API.RECEIVER_NAME);
    String hostName = get(API.HOST);
    String port = get(API.PORT);
    String senderLogin = get(API.SENDER_LOGIN);
    String password = get(API.PASSWORD);
    String emailFrom = get(API.EMAIL_FROM);
    String senderName = get(API.SENDER_NAME);
    if (emailTo.isEmpty() || receiverName.isEmpty() || hostName.isEmpty() || port.isEmpty() || senderLogin.isEmpty()
      || password.isEmpty() || emailFrom.isEmpty() || senderName.isEmpty()) {
      return null;
    }
    if (overdueTasks.isEmpty() && overdueBids.isEmpty() && endingTasks.isEmpty() && endingBids.isEmpty() && inactionTasks.isEmpty()) {
      return null;
    }
    Email email = new SimpleEmail();
    email.setSubject("Сроки исполнения заявок и этапов");
    StringBuilder msg = new StringBuilder();
    if (!overdueTasks.isEmpty()) {
      msg.append("Этапы, у которых превышен максимальный срок исполнения: \n");
      for (Task task : overdueTasks) {
        msg.append(task.getId()).append(" - ").append(task.getName()).append("\n");
      }
    }
    if (!overdueBids.isEmpty()) {
      msg.append("Заявки, у которых превышен максимальный срок исполнения: \n");
      for (Bid bid : overdueBids) {
        msg.append(bid.getId()).append(" - ").append(bid.getProcedure().getName()).append("\n");
      }
    }
    if (!endingTasks.isEmpty()) {
      msg.append("Этапы, срок исполнения приближается к максимальному: \n");
      for (Task task : endingTasks) {
        msg.append(task.getId()).append(" - ").append(task.getName()).append("\n");
      }
    }
    if (!endingBids.isEmpty()) {
      msg.append("Заявки, срок исполнения приближается к максимальному: \n");
      for (Bid bid : endingBids) {
        msg.append(bid.getId()).append(" - ").append(bid.getProcedure().getName()).append("\n");
      }
    }
    if (!inactionTasks.isEmpty()) {
      msg.append("Этапы, которые находятся в бездействии: \n");
      for (Task task : inactionTasks) {
        msg.append(task.getId()).append(" - ").append(task.getName()).append("\n");
      }
    }
    email.setMsg(msg.toString());
    email.addTo(emailTo, receiverName);
    email.setHostName(hostName);
    email.setSmtpPort(Integer.parseInt(port));
    email.setTLS(AdminServiceProvider.getBoolProperty(API.TLS));
    email.setAuthentication(senderLogin, password);
    email.setFrom(emailFrom, senderName);
    email.setCharset("utf-8");
    return email;
  }

  private String get(String property) {
    return StringUtils.trimToEmpty(AdminServiceProvider.get().getSystemProperty(property));
  }

  private void check(
    ProcessEngine processEngine,
    Set<Task> overdueTasks,
    Set<Bid> overdueBids,
    Set<Task> endingTasks,
    Set<Bid> endingBids,
    Set<Task> inactionTasks
  ) {
    List<Task> list = processEngine.getTaskService().createTaskQuery().list();
    Date currentDate = new Date();
    for (Task task : list) {
      List<TaskDates> listTd = em.createQuery("select td from TaskDates td where td.id = :id", TaskDates.class)
        .setParameter("id", task.getId())
        .getResultList();
      int priority = 50;
      if (listTd != null && !listTd.isEmpty()) {
        TaskDates td = listTd.get(0);
        if (task.getAssignee() == null) {
          if (td.getAssignDate() != null && currentDate.after(td.getAssignDate())) {
            inactionTasks.add(task);
            priority = 60;
          }
        } else {
          if (td.getMaxDate() != null && currentDate.after(td.getMaxDate())) {
            overdueTasks.add(task);
            priority = 70;
          } else if (td.getRestDate() != null && currentDate.after(td.getRestDate())) {
            endingTasks.add(task);
            priority = 60;
          }
        }
      }
      List<Bid> bids = em.createQuery("select b from Bid b where b.processInstanceId = :pid", Bid.class)
        .setParameter("pid", task.getProcessInstanceId())
        .getResultList();
      if (listTd != null && !listTd.isEmpty()) {
        Bid bid = bids.get(0);
        if (bid.getMaxDate() != null && currentDate.after(bid.getMaxDate())) {
          overdueBids.add(bid);
          priority += 20;
        } else if (bid.getRestDate() != null && currentDate.after(bid.getRestDate())) {
          endingBids.add(bid);
          priority += 10;
        }
      }
      task.setPriority(priority);
      processEngine.getTaskService().saveTask(task);
    }
  }

  @TransactionAttribute(REQUIRES_NEW)
  public void createLog(Exception e) {
    logger.log(Level.WARNING, "email exception", e);
    AdminServiceProvider.get().createLog(null, "ExecutionDates", "email", "send", e.getMessage(), true);
  }
}
