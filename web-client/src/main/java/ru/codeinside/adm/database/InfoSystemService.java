/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import ru.codeinside.log.Logger;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.List;

@Entity
@EntityListeners(Logger.class)
@SequenceGenerator(name = "info_system_service_seq", sequenceName = "info_system_service_seq")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"sname", "sversion"}))
public class InfoSystemService implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(generator = "info_system_service_seq")
  private Long id;

  private String address;
  @Column(nullable = false)
  private String revision;

  @Column(nullable = false)
  private String sname;

  @Column(nullable = false)
  private String sversion;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private boolean available;

  @Column(name = "logenabled", nullable = false)
  private boolean logEnabled;

  @ManyToOne
  private InfoSystem infoSystem;

  @ManyToOne
  private InfoSystem source;

  // всего лишь для каскаднго удаления ServiceUnavailable
  @OneToMany(mappedBy = "infoSystemService", cascade = CascadeType.REMOVE)
  private List<ServiceUnavailable> serviceUnavailable;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getRevision() {
    return revision;
  }

  public void setRevision(String revision) {
    this.revision = revision;
  }

  public String getSname() {
    return sname;
  }

  public void setSname(String sname) {
    this.sname = sname;
  }

  public String getSversion() {
    return sversion;
  }

  public void setSversion(String sversion) {
    this.sversion = sversion;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean available) {
    this.available = available;
  }

  public InfoSystem getInfoSystem() {
    return infoSystem;
  }

  public void setInfoSystem(InfoSystem infoSystem) {
    this.infoSystem = infoSystem;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public boolean isLogEnabled() {
    return logEnabled;
  }

  public void setLogEnabled(boolean logEnabled) {
    this.logEnabled = logEnabled;
  }

  public InfoSystem getSource() {
    return source;
  }

  public void setSource(InfoSystem source) {
    this.source = source;
  }
}
