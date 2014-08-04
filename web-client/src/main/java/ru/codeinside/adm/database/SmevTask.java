/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import ru.codeinside.gws.api.Packet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@SequenceGenerator(name = "smev_task_seq", sequenceName = "smev_task_seq")
public class SmevTask {

  @Id
  @GeneratedValue(generator = "smev_task_seq")
  private Long id;

  private int revision;

  @Column(nullable = false, name = "process_instance_id")
  private String processInstanceId;

  @Column(name = "execution_id")
  private String executionId;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "date_created")
  private Date dateCreated = new Date();

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "last_change")
  private Date lastChange;

  @Column(name = "request_status")
  private Packet.Status requestStatus;

  @Column(name = "response_status")
  private Packet.Status responseStatus;

  @ManyToOne
  private Employee employee;

  @Column(name = "error_max_count")
  private int errorMaxCount;

  @Column(name = "error_count")
  private int errorCount;

  @Column(name = "error_delay")
  private int errorDelay;

  @Column(name = "ping_max_count")
  private int pingMaxCount;

  @Column(name = "ping_count")
  private int pingCount;

  @Column(name = "ping_delay")
  private int pingDelay;

  @Column(name = "retries_count")
  private int retriesCount;

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

  public Packet.Status getRequestStatus() {
    return requestStatus;
  }

  public void setRequestStatus(Packet.Status requestStatus) {
    this.requestStatus = requestStatus;
  }

  public Packet.Status getResponseStatus() {
    return responseStatus;
  }

  public void setResponseStatus(Packet.Status responseStatus) {
    this.responseStatus = responseStatus;
  }

  public int getErrorMaxCount() {
    return errorMaxCount;
  }

  public void setErrorMaxCount(int errorMaxCount) {
    this.errorMaxCount = errorMaxCount;
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
    this.errorDelay = errorDelay;
  }

  public int getPingMaxCount() {
    return pingMaxCount;
  }

  public void setPingMaxCount(int pingMaxCount) {
    this.pingMaxCount = pingMaxCount;
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
    this.pingDelay = pingDelay;
  }

  public int getRetriesCount() {
    return retriesCount;
  }

  public void setRetriesCount(int retriesCount) {
    this.retriesCount = retriesCount;
  }
}
