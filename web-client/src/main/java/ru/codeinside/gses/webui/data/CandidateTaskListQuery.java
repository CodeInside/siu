/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.data;

import com.google.common.base.Objects;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.ProcedureType;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.components.TasksQueryFilter;

import java.util.Date;
import java.util.List;

import static ru.codeinside.gses.service.DeclarantService.VAR_PROCEDURE_ID;
import static ru.codeinside.gses.service.DeclarantService.VAR_PROCEDURE_TYPE_NAME;
import static ru.codeinside.gses.service.DeclarantService.VAR_REQUESTER_LOGIN;
import static ru.codeinside.gses.service.DeclarantService.VAR_SERVICE_ID;

public class CandidateTaskListQuery extends AbstractLazyLoadingQuery<Task> implements TasksQueryFilter {

  private static final long serialVersionUID = 1L;

  private final String user;

  private String processInstanceId;
  private String serviceId;
  private ProcedureType type;
  private String taskKey;
  private String procedureId;
  private String declarantTypeName;
  private String declarantTypeValue;
  private String requester;
  private Date fromDate;
  private Date toDate;
  private boolean overdue;

  public CandidateTaskListQuery(ItemBuilder<Task> itemBuilder, String user) {
    super(itemBuilder);
    this.user = user;
  }

  List<Task> items(int start, int count) {
    TaskQuery query = createCandidateTaskQuery();
    query.orderByTaskPriority().desc();
    if ("name".equals(orderBy)) {
      query.orderByTaskName();
    } else if ("id".equals(orderBy)) {
      query.orderByTaskId();
    } else {
      query.orderByDueDate();
    }
    return listPage(query, start, count);
  }

  public Task singleResult(String id) {
    return createCandidateTaskQuery().taskDefinitionKey(id).singleResult();
  }

  public int size() {
    return (int) createCandidateTaskQuery().count();
  }

  private TaskQuery createCandidateTaskQuery() {
    final TaskQuery query = Flash.flash().getProcessEngine().getTaskService().createTaskQuery()
      .taskUnassigned()
      .taskCandidateUser(user);
    if (processInstanceId != null) {
      query.processInstanceId(processInstanceId);
    }
    if (type != null) {
      query.processVariableValueEquals(VAR_PROCEDURE_TYPE_NAME, Integer.toString(type.ordinal()));
    }
    if (!StringUtils.isEmpty(serviceId)) {
      query.processVariableValueEquals(VAR_SERVICE_ID, serviceId);
    }
    if (taskKey != null && !taskKey.isEmpty()) {
      query.taskDefinitionKey(taskKey);
    }
    if (procedureId != null && !procedureId.isEmpty()) {
      query.processVariableValueEquals(VAR_PROCEDURE_ID, procedureId);
    }
    if (declarantTypeName != null && declarantTypeValue != null) {
      query.processVariableValueEquals(declarantTypeName, declarantTypeValue);
    }
    if (requester != null && !requester.isEmpty()) {
      query.processVariableValueEquals(VAR_REQUESTER_LOGIN, requester);
    }
    if (fromDate != null) {
      query.taskCreatedAfter(DateUtils.addSeconds(fromDate, -1));
    }
    if (toDate != null) {
      query.taskCreatedBefore(DateUtils.addSeconds(toDate, 1));
    }
    if (overdue) {
      ((TaskQueryImpl2) query).setOverdue(true);
    }
    return query;
  }

  public void setFromDate(Date date) {
    this.fromDate = date;
  }

  public void setToDate(Date date) {
    this.toDate = date;
  }

  public void setBidId(String bidId) {
    Bid bid = AdminServiceProvider.get().getBid(bidId);
    processInstanceId = bid != null ? bid.getProcessInstanceId() : null;
  }

  public void setRequester(String login) {
    this.requester = login;
  }

  public void setProcedureType(String procedureTypeName) {
    if (Objects.equal("Административная процедура", procedureTypeName)) {
      type = ProcedureType.Administrative;
    } else if (Objects.equal("Межведомственная процедура", procedureTypeName)) {
      type = ProcedureType.Interdepartmental;
    } else {
      type = null;
    }
  }

  public void setTaskKey(String taskKey) {
    this.taskKey = taskKey;
  }

  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

  public void setProcedureId(String id) {
    this.procedureId = id;
  }

  public void setDeclarantTypeName(String name) {
    this.declarantTypeName = name;
  }

  public void setDeclarantTypeValue(String value) {
    this.declarantTypeValue = value;
  }

  @Override
  public void setOverdue(boolean value) {
    this.overdue = value;
  }

  @Override
  public void setControlledOrgGroups(List groups) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setControlledEmpGroups(List groups) {
    throw new UnsupportedOperationException();
  }
}