/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.data;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.*;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.actions.ItemBuilder;
import ru.codeinside.gses.webui.components.TasksQueryFilter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ru.codeinside.gses.service.impl.DeclarantServiceImpl.*;

public class ControlledTasksQuery extends AbstractLazyLoadingQuery<Task> implements TasksQueryFilter {
  private static final long serialVersionUID = 1L;
  private List<String> controlledGroups;
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
  private List<String> orgGroups;
  private List<String> empGroups;
  private boolean superSupervisor;

  public ControlledTasksQuery(ItemBuilder<Task> itemBuilder, Employee emp){
    super(itemBuilder);
    superSupervisor = emp.getRoles().contains(Role.SuperSupervisor);
    controlledGroups = new ArrayList<String>(emp.getOrganizationGroups().size() + emp.getEmployeeGroups().size());
    for(Group group : emp.getOrganizationGroups()){
      controlledGroups.add(group.getName());
    }
    for(Group group : emp.getEmployeeGroups()){
      controlledGroups.add(group.getName());
    }
  }

  List<Task> items(int start, int count){
    TaskQuery query = createTaskQuery();
    query.orderByTaskPriority().desc();
    if ("name".equals(orderBy)) {
      query.orderByTaskName();
    } else if ("id".equals(orderBy)) {
      query.orderByTaskId();
    } else {
      query.orderByDueDate();
    }
    return  listPage(query, start, count);
  }

  private TaskQuery createTaskQuery() {
    TaskQuery query = Flash.flash().getProcessEngine().getTaskService().createTaskQuery();
    ((TaskQueryImpl2)query).setIgnoreAssignee(false);
    List<String> resultGroups;
    if (superSupervisor) {
      if(orgGroups == null && empGroups !=null){
        resultGroups= empGroups;
      } else if(empGroups == null && orgGroups != null){
        resultGroups = orgGroups;
      } else if(empGroups != null && orgGroups != null){
        orgGroups.addAll(empGroups);
        resultGroups = orgGroups;
      } else {
        resultGroups = Lists.newArrayListWithExpectedSize(0);
      }
    } else {
      resultGroups = Lists.newArrayListWithExpectedSize(controlledGroups.size());
      resultGroups.addAll(controlledGroups);
      if(orgGroups == null && empGroups !=null){
        resultGroups.retainAll(empGroups);
      } else if(empGroups == null && orgGroups != null){
        resultGroups.retainAll(orgGroups);
      } else if(empGroups != null && orgGroups != null){
        orgGroups.addAll(empGroups);
        resultGroups.retainAll(orgGroups);
      }
    }
    if(!resultGroups.isEmpty()){
      query.taskCandidateGroupIn(resultGroups);
    }
    if(processInstanceId != null){
      query.processInstanceId(processInstanceId);
    }
    if(type != null){
      query.processVariableValueEquals(VAR_PROCEDURE_TYPE_NAME,  Integer.toString(type.ordinal()));
    }
    if(!StringUtils.isEmpty(serviceId)){
      query.processVariableValueEquals(VAR_SERVICE_ID, serviceId);
    }
    if(taskKey !=null && !taskKey.isEmpty()){
      query.taskDefinitionKey(taskKey);
    }
    if(procedureId != null && !procedureId.isEmpty()){
      query.processVariableValueEquals(VAR_PROCEDURE_ID, procedureId);
    }
    if(declarantTypeName != null && declarantTypeValue != null){
      query.processVariableValueEquals(declarantTypeName, declarantTypeValue);
    }
    if(requester != null && !requester.isEmpty()){
      query.processVariableValueEquals(VAR_REQUESTER_LOGIN, requester);
    }
    if(fromDate != null){
      query.taskCreatedAfter(DateUtils.addSeconds(fromDate, -1));
    }
    if(toDate != null){
      query.taskCreatedBefore(DateUtils.addSeconds(toDate, 1));
    }
    if(overdue){
      query.taskMinPriority(70);
    }
    return query;
  }

  @Override
  Task singleResult(String id) {
    return createTaskQuery().taskDefinitionKey(id).singleResult();
  }

  @Override
  public int size() {
    return (int)createTaskQuery().count();
  }

  @Override
  public void setFromDate(Date fromDate) {
    this.fromDate = fromDate;
  }

  @Override
  public void setToDate(Date toDate) {
    this.toDate = toDate;
  }

  @Override
  public void setRequester(String requester) {
    this.requester = requester;
  }

  @Override
  public void setBidId(String bidId) {
    Bid bid = AdminServiceProvider.get().getBid(bidId);
    processInstanceId = bid!= null ? bid.getProcessInstanceId() : null;
  }

  @Override
  public void setProcedureType(String procedureType) {
    if(Objects.equal("Административная процедура", procedureType)){
      type = ProcedureType.Administrative;
    }else if(Objects.equal("Межведомственная процедура", procedureType)){
      type = ProcedureType.Interdepartmental;
    }else{
      type = null;
    }
  }

  @Override
  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

  @Override
  public void setProcedureId(String procedureId) {
    this.procedureId = procedureId;
  }

  @Override
  public void setTaskKey(String taskKey) {
    this.taskKey = taskKey;
  }

  @Override
  public void setDeclarantTypeName(String declarantTypeName) {
    this.declarantTypeName = declarantTypeName;
  }

  @Override
  public void setDeclarantTypeValue(String declarantTypeValue) {
    this.declarantTypeValue = declarantTypeValue;
  }

  @Override
  public void setOverdue(boolean value) {
    this.overdue = value;
  }

  @Override
  public void setControlledOrgGroups(List groups) {
    orgGroups = groups;
  }

  @Override
  public void setControlledEmpGroups(List groups) {
    empGroups = groups;
  }

}
