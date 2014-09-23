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
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;

@TransactionManagement
@TransactionAttribute
@Singleton
@DependsOn("BaseBean")
@Lock(LockType.READ)
public class CheckExecutionDates {

  @PersistenceContext(unitName = "myPU")
  EntityManager em;

  @Inject
  AdminService adminService;

  final Logger logger = Logger.getLogger(getClass().getName());

  @TransactionAttribute(REQUIRES_NEW)
  public void createLog(Exception e) {
    logger.log(Level.WARNING, "email exception", e);
    adminService.createLog(null, "ExecutionDates", "email", "send", e.getMessage(), true);
  }

  @TransactionAttribute(REQUIRES_NEW)
  public Email checkDates(ProcessEngine processEngine) throws EmailException {
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

    List<TaskDates> overdueTasks = new ArrayList<TaskDates>();
    List<Bid> overdueBids = new ArrayList<Bid>();
    List<TaskDates> endingTasks = new ArrayList<TaskDates>();
    List<Bid> endingBids = new ArrayList<Bid>();
    List<TaskDates> inactionTasks = new ArrayList<TaskDates>();

    Date currentDate = check(overdueTasks, overdueBids, endingTasks, endingBids, inactionTasks);

    Email email = new SimpleEmail();
    email.setSubject("[siu.oep-penza.ru] Сроки исполнения заявок и этапов на " + new SimpleDateFormat("yyyy.MM.dd HH:mm").format(currentDate));

    StringBuilder msg = new StringBuilder();
    msg.append("Добрый день!\n" +
      "Сообщаем Вам список заявок из Системы Исполнения Услуг [siu.oep-penza.ru] за ")
      .append(new SimpleDateFormat("yyyy.MM.dd").format(currentDate))
      .append(", нуждающихся в обработке:\n\n");

    int n = 1;
    n = addTaskList("Этапы, у которых превышен максимальный срок исполнения", n, processEngine, overdueTasks, msg);
    n = addBidList("Заявки, у которых превышен максимальный срок исполнения", n, overdueBids, msg);
    n = addTaskList("Этапы, срок исполнения которых приближается к максимальному", n, processEngine, endingTasks, msg);
    n = addBidList("Заявки, срок исполнения которых приближается к максимальному", n, endingBids, msg);
    n = addTaskList("Этапы, которые находятся в бездействии:", n, processEngine, inactionTasks, msg);

    msg.append("Обращаем Ваше внимание, данное письмо сформировано автоматически и не требует ответа!\n" +
      "Дополнительные консультации по вопросам обработки заявок " +
      "можно получить по телефону техподдержки – тел. 8(8412)23-11-25 (доб. 45, 46, 47)");

    if (n == 1) {
      return null;
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

  private int addBidList(String title, int n, List<Bid> list, StringBuilder msg) {
    if (list.isEmpty()) {
      return n;
    }
    msg.append(n)
      .append(')')
      .append(title)
      .append(":\n");
    for (Bid bid : list) {
      msg.append('\t')
        .append(bid.getId())
        .append(" - ")
        .append(bid.getProcedure().getName())
        .append("\n");
    }
    msg.append("\n");
    return n + 1;
  }

  // в TaskDates нет даты исполнения этапа, требуется проверка существования этапа.
  private int addTaskList(String title, int n, ProcessEngine processEngine, List<TaskDates> list, StringBuilder msg) {
    if (list.isEmpty()) {
      return n;
    }
    StringBuilder tasks = new StringBuilder();
    for (TaskDates taskDates : list) {
      Task task = processEngine.getTaskService().createTaskQuery().taskId(taskDates.getId()).singleResult();
      if (task != null) { // если нет этапа - он уже исполнен
        tasks.append('\t')
          .append("\"")
          .append(task.getName())
          .append("\" по заявке №")
          .append(taskDates.getBid().getId())
          .append("\n");
      }
    }
    if (tasks.length() == 0) {
      return n;
    }
    msg.append(n)
      .append(')')
      .append(title)
      .append(":\n");
    msg.append(tasks);
    msg.append("\n");
    return n + 1;
  }

  private String get(String property) {
    return StringUtils.trimToEmpty(AdminServiceProvider.get().getSystemProperty(property));
  }

  private Date check(
    List<TaskDates> overdueTasks,
    List<Bid> overdueBids,
    List<TaskDates> endingTasks,
    List<Bid> endingBids,
    List<TaskDates> inactionTasks
  ) {
    Date currentDate = new Date();
    List<TaskDates> datesList = em.createQuery("select t from TaskDates t where " +
        "t.bid.dateFinished is null and " +
        "t.maxDate is not null and (" +
        " (t.maxDate <= :now or t.restDate <= :now) or" +
        " (t.assignDate is null and t.inactionDate <= :now)" +
        ")",
      TaskDates.class)
      .setParameter("now", currentDate)
      .getResultList();

    for (TaskDates dates : datesList) {
      List<TaskDates> sink;
      if (dates.getMaxDate().compareTo(currentDate) <= 0) {
        sink = overdueTasks;
      } else if (dates.getRestDate().compareTo(currentDate) <= 0) {
        sink = endingTasks;
      } else {
        sink = inactionTasks;
      }
      sink.add(dates);
    }
    List<Bid> bidList = em.createQuery("select b from Bid b where " +
        "b.dateFinished is null and (" +
        " b.maxDate <= :now or" +
        " b.restDate <= :now" +
        ")",
      Bid.class)
      .setParameter("now", currentDate)
      .getResultList();

    for (Bid bid : bidList) {
      List<Bid> sink;
      if (bid.getMaxDate().compareTo(currentDate) <= 0) {
        sink = overdueBids;
      } else {
        sink = endingBids;
      }
      sink.add(bid);
    }
    return currentDate;
  }

}
