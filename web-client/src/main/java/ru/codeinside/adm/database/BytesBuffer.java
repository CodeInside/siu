/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @since 1.0.8
 */
@Entity
@Cacheable(false)
@Table(name = "bytes_buffer")
public class BytesBuffer {

  @Id
  @GeneratedValue(generator = "bytes_buffer_seq")
  @SequenceGenerator(name = "bytes_buffer_seq", sequenceName = "bytes_buffer_seq")
  private Integer id;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  private byte[] bytes;


  protected BytesBuffer() {

  }

  public BytesBuffer(byte[] bytes) {
    this.bytes = bytes;
  }

  public Integer getId() {
    return id;
  }

  public byte[] getBytes() {
    return bytes;
  }
}
