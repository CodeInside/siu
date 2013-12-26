/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToOne;
import javax.persistence.Version;

import ru.codeinside.log.Logger;

@Entity
public class AuditSnapshot {

  @EmbeddedId
  protected AuditId id;

  @OneToOne(optional = false)
  protected AuditValue value;

  @Version
  @Column(nullable = false)
  protected int version;

  protected AuditSnapshot() {

  }

  public AuditSnapshot(AuditId id, AuditValue value) {
    this.id = id;
    this.value = value;
  }

  public AuditId getId() {
    return id;
  }

  public AuditValue getValue() {
    return value;
  }

  public void setValue(AuditValue value) {
    this.value = value;
  }

}
