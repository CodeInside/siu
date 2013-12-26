/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.beans;

import org.activiti.engine.delegate.VariableScope;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.activiti.ftarchive.AttachmentFFT;

/**
 * Сложное значение переменной. Значеним может быть идентификатор вложения.
 */
final public class ComplexValue {

  final Object value;
  final String attachmentId;

  ComplexValue(Object value, String attachmentId) {
    this.value = value;
    this.attachmentId = attachmentId;
  }

  public boolean isAttachmentId() {
    return attachmentId != null;
  }

  public Object getValue() {
    return value;
  }

  public String getAttachmentId() {
    return attachmentId;
  }

  static ComplexValue get(VariableScope variables, String name) {
    Object value = null;
    String attachmentId = null;

    if (variables != null && name != null && variables.hasVariable(name)) {
      value = variables.getVariable(name);
      attachmentId = StringUtils.trimToNull(AttachmentFFT.getAttachmentIdByValue(value));
      if (attachmentId != null) {
        value = null;
      }
    }

    return new ComplexValue(value, attachmentId);
  }

}
