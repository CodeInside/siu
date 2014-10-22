/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import org.eclipse.persistence.annotations.CascadeOnDelete;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "smev_task")
@SequenceGenerator(name = "smev_task_seq", sequenceName = "smev_task_seq")
public class SmevTask {

  @Id
  @GeneratedValue(generator = "smev_task_seq")
  Long id;

  int revision;

  @Column(name = "process_instance_id", length = 64, nullable = false, updatable = false)
  String processInstanceId;

  @Column(name = "execution_id", length = 64, nullable = false, updatable = false)
  String executionId;

  @Column(name = "task_id", nullable = false, updatable = false)
  String taskId;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "date_created")
  Date dateCreated = new Date();

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "last_change")
  Date lastChange;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "request_type")
  SmevRequestType requestType;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "response_type")
  SmevResponseType responseType;

  @ManyToOne
  Employee employee;

  @Column(name = "error_max_count")
  int errorMaxCount;

  @Column(name = "error_count")
  int errorCount;

  @Column(name = "error_delay")
  int errorDelay;

  @Column(name = "ping_max_count")
  int pingMaxCount;

  @Column(name = "ping_count")
  int pingCount;

  @Column(name = "ping_delay")
  int pingDelay;

  @Column(name = "failure", columnDefinition = "text")
  String failure;

  @Column(name = "origin_id")
  String originId;

  @Column(name = "request_id")
  String requestId;

  @ElementCollection(fetch = FetchType.LAZY)
  @CascadeOnDelete
  private Set<String> groups;

  @ManyToOne
  private Bid bid;

  @Column(name = "need_user_reaction")
  private boolean needUserReaction = false;

  /**
   * Стретегия потребителя.
   */
  @Enumerated(EnumType.ORDINAL)
  @Column(nullable = false)
  SmevTaskStrategy strategy;

  /**
   * Имя компонента OSGI с реализацией потребителя СМЭВ
   */
  @Column(nullable = false)
  String consumer;

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  public String getProcessInstanceId() {
    return processInstanceId;
  }

  public void setProcessInstanceId(String processInstanceId) {
    this.processInstanceId = processInstanceId;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getLastChange() {
    return lastChange;
  }

  public void setLastChange(Date lastChange) {
    this.lastChange = lastChange;
  }

  public int getRevision() {
    return revision;
  }

  public void setRevision(int revision) {
    this.revision = revision;
  }

  public String getExecutionId() {
    return executionId;
  }

  public void setExecutionId(String executionId) {
    this.executionId = executionId;
  }

  public int getErrorMaxCount() {
    return errorMaxCount;
  }

  public void setErrorMaxCount(int errorMaxCount) {
    this.errorMaxCount = Math.max(0, errorMaxCount);
  }

  public int getErrorCount() {
    return errorCount;
  }

  public void setErrorCount(int errorCount) {
    this.errorCount = errorCount;
  }

  public int getErrorDelay() {
    return errorDelay;
  }

  public void setErrorDelay(int errorDelay) {
    this.errorDelay = Math.max(10, errorDelay);
  }

  public int getPingMaxCount() {
    return pingMaxCount;
  }

  public void setPingMaxCount(int pingMaxCount) {
    this.pingMaxCount = Math.max(0, pingMaxCount);
  }

  public int getPingCount() {
    return pingCount;
  }

  public void setPingCount(int pingCount) {
    this.pingCount = pingCount;
  }

  public int getPingDelay() {
    return pingDelay;
  }

  public void setPingDelay(int pingDelay) {
    this.pingDelay = Math.max(10, pingDelay);
  }

  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  public String getFailure() {
    return failure;
  }

  public void setFailure(String failure) {
    this.failure = failure;
  }

  public Set<String> getGroups() {
    return groups;
  }

  public void setGroups(Set<String> groups) {
    this.groups = groups;
  }

  public SmevTaskStrategy getStrategy() {
    return strategy;
  }

  public void setStrategy(SmevTaskStrategy strategy) {
    this.strategy = strategy;
  }

  public String getConsumer() {
    return consumer;
  }

  public void setConsumer(String consumer) {
    this.consumer = consumer;
  }

  public SmevRequestType getRequestType() {
    return requestType;
  }

  public void setRequestType(SmevRequestType requestType) {
    this.requestType = requestType;
  }

  public SmevResponseType getResponseType() {
    return responseType;
  }

  public void setResponseType(SmevResponseType responseType) {
    this.responseType = responseType;
  }

  public String getOriginId() {
    return originId;
  }

  public void setOriginId(String originId) {
    this.originId = originId;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public Bid getBid() {
    return bid;
  }

  public void setBid(Bid bid) {
    this.bid = bid;
  }

  public boolean isNeedUserReaction() {
    return needUserReaction;
  }

  public void setNeedUserReaction(boolean needUserReaction) {
    this.needUserReaction = needUserReaction;
  }

  public void registerFailure(String failure) {
    this.failure = failure;
    errorCount++;
  }

  public boolean canProcess() {
    return pingCount < pingMaxCount && errorCount < errorMaxCount;
  }

  public boolean needHumanReaction() {
    return pingCount >= pingMaxCount ||
      errorCount >= errorMaxCount ||
      responseType == SmevResponseType.INVALID;
  }

}
