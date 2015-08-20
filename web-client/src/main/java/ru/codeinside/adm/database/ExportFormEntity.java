/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2015, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.adm.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "export_form")
@SequenceGenerator(name = "id_seq", sequenceName = "export_form_id_seq", allocationSize = 1)
public class ExportFormEntity {

  @Id
  @GeneratedValue(generator = "id_seq")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Employee employee;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false)
  private Date date;

  @Column(nullable = false)
  private byte[] json;

  @Column(nullable = false)
  private byte[] pkcs7;

  @Column(nullable = false)
  private String procedure;

  @Column(nullable = false)
  private String task;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Bid bid;

  public ExportFormEntity() { }

  public Long getId() {
    return id;
  }

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public byte[] getJson() {
    return json;
  }

  public void setJson(byte[] json) {
    this.json = json;
  }

  public byte[] getPkcs7() {
    return pkcs7;
  }

  public void setPkcs7(byte[] pkcs7) {
    this.pkcs7 = pkcs7;
  }

  public String getProcedure() {
    return procedure;
  }

  public void setProcedure(String procedure) {
    this.procedure = procedure;
  }

  public String getTask() {
    return task;
  }

  public void setTask(String task) {
    this.task = task;
  }

  public Bid getBid() {
    return bid;
  }

  public void setBid(Bid bid) {
    this.bid = bid;
  }
}
