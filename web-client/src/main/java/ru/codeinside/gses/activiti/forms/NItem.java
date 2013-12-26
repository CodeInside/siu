/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

final class NItem implements PropertyNode {

  final String id;
  final String underline;
  final String tip;
  final NullAction nullAction;
  final boolean readable;
  final boolean writable;


  NItem(final String id, String underline, String tip, NullAction nullAction, boolean readable, boolean writable) {
    this.id = id;
    this.underline = underline;
    this.tip = tip;
    this.nullAction = nullAction;
    this.readable = readable;
    this.writable = writable;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public PropertyType getPropertyType() {
    return PropertyType.ITEM;
  }

  @Override
  public String getUnderline() {
    return underline;
  }

  @Override
  public String getTip() {
    return tip;
  }

  @Override
  public NullAction getNullAction() {
    return nullAction;
  }

  @Override
  public boolean isReadable() {
    return readable;
  }

  @Override
  public boolean isWritable() {
    return writable;
  }
}
