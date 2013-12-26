/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

final class NBlock implements BlockNode {

  final String id;
  final PropertyNode[] nodes;
  final int min;
  final int max;
  final String underline;
  final String tip;
  final NullAction nullAction;


  NBlock(String id, PropertyNode[] nodes, int min, int max, String underline, String tip, NullAction nullAction) {
    this.id = id;
    this.nodes = nodes;
    this.min = min;
    this.max = max;
    this.underline = underline;
    this.tip = tip;
    this.nullAction = nullAction;
  }

  @Override
  public PropertyNode[] getNodes() {
    return nodes;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public PropertyType getPropertyType() {
    return PropertyType.BLOCK;
  }

  @Override
  public int getMinimum() {
    return min;
  }

  @Override
  public int getMaximum() {
    return max;
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
    return true;
  }

  @Override
  public boolean isWritable() {
    return true;
  }
}
