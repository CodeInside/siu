/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class AuditValue {

  /**
   * HistoryId
   */
  @Id
  protected long hid;

  @Column(updatable = false, length = 64)
  protected String login;

  @Column(nullable = false, updatable = false)
  protected boolean attachment;

  @Column(updatable = false)
  protected byte[] cert;

  @Column(updatable = false)
  protected byte[] sign;

  @Transient
  protected Object detail;

  // Только для JPA
  protected AuditValue() {

  }

  public AuditValue(final String login, final long hid, final Object detail) {
    this.hid = hid;
    this.detail = detail;
    this.login = login;
    this.attachment = false;
  }

  public void setSignature(final byte[] cert, final byte[] sign, boolean attachment) {
    this.cert = cert;
    this.sign = sign;
    this.attachment = attachment;
  }

  public long getHid() {
    return hid;
  }

  public boolean isAttachment() {
    return attachment;
  }

  public String getLogin() {
    return login;
  }

  public Object getDetail() {
    return detail;
  }

  public byte[] getCert() {
    return cert;
  }

  public byte[] getSign() {
    return sign;
  }
}
