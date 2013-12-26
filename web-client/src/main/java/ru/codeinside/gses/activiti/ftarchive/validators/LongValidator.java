/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive.validators;

import com.vaadin.data.validator.AbstractStringValidator;


public class LongValidator extends AbstractStringValidator {

  private static final long serialVersionUID = 8306001395582004472L;

  public LongValidator(String errorMessage) {
    super(errorMessage);
  }

  @Override
  protected boolean isValidString(String value) {
    try {
        Long.parseLong(value);
        return true;
    } catch (Exception e) {
        return false;
    }
  }  
}
