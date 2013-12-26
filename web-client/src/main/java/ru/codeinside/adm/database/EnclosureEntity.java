/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import ru.codeinside.log.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@EntityListeners(Logger.class)
@Table(name = "enclosure")
public class EnclosureEntity {

  @Id
  private String id;
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private ServiceResponseEntity response;
  @Column(nullable = false)
  private String var;
  private String fileName;
  private String code;
  private String number;
  private String zipPath;

  /**
   * Для JPA движка
   */
  protected EnclosureEntity() {

  }

  public EnclosureEntity(final ServiceResponseEntity response, final String id, final String var,
                         final String fileName, final String code, final String number, String zipPath) {
    this.id = id;
    this.response = response;
    this.var = var;
    this.fileName = fileName;
    this.code = code;
    this.number = number;
    this.zipPath = zipPath;
  }

  public String getId() {
    return id;
  }

  public String getVar() {
    return var;
  }

  public String getFileName() {
    return fileName;
  }

  public String getCode() {
    return code;
  }

  public String getNumber() {
    return number;
  }

  public String getZipPath() {
    return zipPath;
  }

  @Override
  public String toString() {
    return "{"
      + "id='" + id + '\''
      + ", var='" + var + '\''
      + ", fileName='" + fileName + '\''
      + ", code='" + code + '\''
      + ", number='" + number + '\''
      + ", zipPath='" + zipPath + '\''
      + '}';
  }
}
