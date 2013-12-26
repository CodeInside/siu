/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive.validators;

import com.vaadin.data.validator.AbstractStringValidator;
import org.apache.commons.lang.StringUtils;

public class FilteredLongValidator extends AbstractStringValidator {

  final String regex;

  public FilteredLongValidator(String regex, String errorMessage) {
    super(errorMessage);
    this.regex = regex;
  }

  @Override
  protected boolean isValidString(String value) {
    try {
      toLong(value);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public Long toLong(String value) throws NumberFormatException {
    if (value != null) {
      value = StringUtils.trimToNull(value.replaceAll(regex, ""));
    }
    if (value == null) {
      return null;
    }
    return Long.parseLong(value);
  }
}
