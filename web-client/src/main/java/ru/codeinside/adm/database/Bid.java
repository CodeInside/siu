/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import ru.codeinside.log.Logger;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@EntityListeners(Logger.class)
@SequenceGenerator(name = "bid_seq", sequenceName = "bid_seq")
public class Bid {

  @Id
  @GeneratedValue(generator = "bid_seq")
  private Long id;

  @ManyToOne
  private Procedure procedure;

  @Column(nullable = false)
  private String processInstanceId;

  @Temporal(TemporalType.TIMESTAMP)
  private Date dateCreated = new Date();

  @Temporal(TemporalType.TIMESTAMP)
  private Date dateFinished;

  @Column(nullable = false)
  private String declarant;

  @Column(nullable = false)
  private String version;

  private String comment;

  @ManyToOne
  private ProcedureProcessDefinition procedureProcessDefinition;

  @Column(nullable = false)
  private BidStatus status;

  @ElementCollection
  private Set<String> currentSteps;

  @ManyToOne
  private Employee employee;

  @Column(nullable = false)
  private String tag;

  @ManyToOne(optional = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "gid")
  private ExternalGlue glue;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "rest_date")
  private Date restDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "max_date")
  private Date maxDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "default_rest_date")
  private Date defaultRestDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "default_max_date")
  private Date defaultMaxDate;

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  public Procedure getProcedure() {
    return procedure;
  }

  public void setProcedure(Procedure procedure) {
    this.procedure = procedure;
  }

  public String getProcessInstanceId() {
    return processInstanceId;
  }

  public void setProcessInstanceId(String processInstanceId) {
    this.processInstanceId = processInstanceId;
  }

  public String getDeclarant() {
    return declarant;
  }

  public void setDeclarant(String declarant) {
    this.declarant = declarant;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public ProcedureProcessDefinition getProcedureProcessDefinition() {
    return procedureProcessDefinition;
  }

  public void setProcedureProcessDefinition(ProcedureProcessDefinition procedureProcessDefinition) {
    this.procedureProcessDefinition = procedureProcessDefinition;
  }

  public BidStatus getStatus() {
    return status;
  }

  public void setStatus(BidStatus status) {
    this.status = status;
  }

  public Set<String> getCurrentSteps() {
    if (currentSteps == null) {
      currentSteps = new LinkedHashSet<String>();
    }
    return currentSteps;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getDateFinished() {
    return dateFinished;
  }

  public void setDateFinished(Date dateFinished) {
    this.dateFinished = dateFinished;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public ExternalGlue getGlue() {
    return glue;
  }

  public void setGlue(ExternalGlue glue) {
    this.glue = glue;
  }

  public Date getRestDate() {
    return restDate;
  }

  public void setRestDate(Date restDate) {
    this.restDate = restDate;
  }

  public Date getMaxDate() {
    return maxDate;
  }

  public void setMaxDate(Date maxDate) {
    this.maxDate = maxDate;
  }

  public Date getDefaultRestDate() {
    return defaultRestDate;
  }

  public void setDefaultRestDate(Date defaultRestDate) {
    this.defaultRestDate = defaultRestDate;
  }

  public Date getDefaultMaxDate() {
    return defaultMaxDate;
  }

  public void setDefaultMaxDate(Date defaultMaxDate) {
    this.defaultMaxDate = defaultMaxDate;
  }
}
