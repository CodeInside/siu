/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;

@Entity
@Table(name = "http_log")
@SequenceGenerator(name = "http_log_seq", sequenceName = "http_log_seq")
public class HttpLog {

  // для JPA движка
  protected HttpLog() {

  }

  public HttpLog(byte[] data) {
    this.data = data;
  }

  @Id
  @GeneratedValue(generator = "http_log_seq")
  private Long id;

  private byte[] data;

  public byte[] getData() {
    return data;
  }

  public Long getId() {
    return id;
  }

}
