/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm;


import org.activiti.engine.ActivitiException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.Task;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.TaskDates;
import ru.codeinside.gses.API;
import ru.codeinside.gses.activiti.mail.SmtpConfig;
import ru.codeinside.gses.activiti.mail.SmtpConfigReader;

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
      int priority = 50;
      if (td != null) {
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
      Bid bid = em.createQuery("select b from Bid b where b.processInstanceId = :pid", Bid.class)
        .setParameter("pid", task.getProcessInstanceId())
        .getSingleResult();
      if (bid.getMaxDate() != null && currentDate.after(bid.getMaxDate())) {
        overdueBids.add(bid);
        priority += 20;
      } else if (bid.getRestDate() != null && currentDate.after(bid.getRestDate())) {
        endingBids.add(bid);
        priority += 10;
      }
      task.setPriority(priority);
      processEngine.getTaskService().saveTask(task);
    }
    String address = AdminServiceProvider.get().getSystemProperty(API.EMAIL_FOR_EXECUTION_DATES);
    if (address == null || address.isEmpty()) {
      return;
    }
    if (overdueTasks.isEmpty() && overdueBids.isEmpty() && endingTasks.isEmpty() && endingBids.isEmpty() && inactionTasks.isEmpty()) {
      return;
    }
    Email email = new SimpleEmail();
    try {
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
      email.addTo(address, "user");

      SmtpConfig credential = SmtpConfigReader.readSmtpConnectionParams();

      String host = credential.getHost();
      if (host == null) {
        throw new ActivitiException("Could not send email: no SMTP host is configured");
      }
      email.setHostName(host);

      int port = credential.getPort();
      email.setSmtpPort(port);

      email.setTLS(credential.getUseTLS());

      String user = credential.getUserName();
      String password = credential.getPassword();
      if (user != null && password != null) {
        email.setAuthentication(user, password);
      }
      email.setFrom("oeptest@mail.ru", "oeptest");
      email.setCharset("utf-8");
      email.send();
    } catch (EmailException e) {
      e.printStackTrace();
    }

  }
}
