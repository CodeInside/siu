/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;

import ru.codeinside.log.Logger;

import java.io.Serializable;

@Entity
@EntityListeners(Logger.class)
public class InfoSystem implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private String code;

  private String name;

  private String comment;

  private boolean source;

  private boolean main;

  protected InfoSystem() {

  }

  public InfoSystem(String code, String name) {
    this.code = code;
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public boolean isSource() {
    return source;
  }

  public void setSource(boolean source) {
    this.source = source;
  }

  public boolean isMain() {
    return main;
  }

  public void setMain(boolean main) {
    this.main = main;
  }

  @Override
  public String toString() {
    return code != null ? code : "";
  }
}
