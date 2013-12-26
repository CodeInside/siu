/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class AuditId implements Serializable {
  private static final long serialVersionUID = 1L;

  protected long eid;

  @Column(nullable = false, length = 255)
  protected String name;


  public AuditId() {

  }

  public AuditId(long eid, String name) {
    this.eid = eid;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public long getEid() {
    return eid;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AuditId auditId = (AuditId) o;
    if (eid != auditId.eid) return false;
    if (name != null ? !name.equals(auditId.name) : auditId.name != null) return false;
    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (eid ^ (eid >>> 32));
    result = 31 * result + (name != null ? name.hashCode() : 0);
    return result;
  }
}
