/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.history;

final public class VariableSignature {

  final ExecutionId executionId;
  final String variable;
  final byte[] cert;
  final byte[] sign;
  final boolean file;

  public VariableSignature(ExecutionId executionId, String variable, byte[] cert, byte[] sign, boolean file) {
    this.executionId = executionId;
    this.variable = variable;
    this.cert = cert;
    this.sign = sign;
    this.file = file;
  }

  @Override
  public String toString() {
    return "{" +
      "executionId=" + executionId +
      ", variable='" + variable + '\'' +
      '}';
  }
}
