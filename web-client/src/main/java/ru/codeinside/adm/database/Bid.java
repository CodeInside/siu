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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

  //TODO добавить проставление даты в листнер завершения процесса
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

  public void setCurrentSteps(Set<String> currentStep) {
    this.currentSteps = currentStep;
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
}
