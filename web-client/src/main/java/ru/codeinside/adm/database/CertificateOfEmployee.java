/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "certificate_of_employee")
@SequenceGenerator(name = "certificate_of_employee_", sequenceName = "certificate_of_employee_seq", initialValue = 1, allocationSize = 10)
public class CertificateOfEmployee implements Serializable {

  private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "certificate_of_employee_", strategy = GenerationType.SEQUENCE)
  Long id;
    @Version
    @Column(nullable = false)
  Timestamp timeOfChange;
    @Lob
    @Column(nullable = false)
  byte[] x509;


  public Long getId() {
    return id;
  }

  public void setId(Long _) {
    id = _;
  }


  public Timestamp getTimeOfChange() {
    return timeOfChange;
  }

  public void setTimeOfChange(Timestamp _) {
    timeOfChange = _;
  }


  public byte[] getX509() {
    return x509;
  }

  public void setX509(byte[] _) {
    x509 = _;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CertificateOfEmployee that = (CertificateOfEmployee) o;
    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    return true;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}
