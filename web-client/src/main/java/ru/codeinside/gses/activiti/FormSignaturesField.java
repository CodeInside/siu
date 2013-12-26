/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import ru.codeinside.gses.vaadin.customfield.CustomField;

final public class FormSignaturesField extends CustomField {

  private static final long serialVersionUID = 1L;

  public FormSignaturesField(String text, Signatures signatures) {
    ReadOnly readOnly = new ReadOnly(text);
    setCompositionRoot(readOnly);
    setValue(signatures);
  }

  @Override
  public Class<?> getType() {
    return Signatures.class;
  }
}
