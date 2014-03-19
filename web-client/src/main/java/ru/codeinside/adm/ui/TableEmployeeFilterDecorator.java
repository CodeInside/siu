/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.terminal.Resource;
import com.vaadin.ui.DateField;
import org.tepi.filtertable.FilterDecorator;
import org.tepi.filtertable.numberfilter.NumberFilterPopupConfig;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TableEmployeeFilterDecorator implements FilterDecorator, Serializable {

  final SimpleDateFormat shortFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
  final SimpleDateFormat longFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");

  public String getEnumFilterDisplayName(Object propertyId, Object value) {
    return null;
  }

  public Resource getEnumFilterIcon(Object propertyId, Object value) {
    return null;
  }

  public String getBooleanFilterDisplayName(Object propertyId, boolean value) {
    return value ? "Да" : "Нет";
  }

  public Resource getBooleanFilterIcon(Object propertyId, boolean value) {
    return null;
  }

  public String getFromCaption() {
    return "С";
  }

  public String getToCaption() {
    return "По";
  }

  public String getSetCaption() {
    return "Задать";
  }

  public String getClearCaption() {
    return "Сбросить";
  }

  public boolean isTextFilterImmediate(Object propertyId) {
    return true;
  }

  public int getTextChangeTimeout(Object propertyId) {
    return 500;
  }

  public String getAllItemsVisibleString() {
    return "Все";
  }

  public NumberFilterPopupConfig getNumberFilterPopupConfig() {
    return null;
  }

  public boolean usePopupForNumericProperty(Object propertyId) {
    return false;
  }

  public int getDateFieldResolution(Object propertyId) {
    return isLogDate(propertyId) ? DateField.RESOLUTION_MSEC : DateField.RESOLUTION_SEC;
  }

  private boolean isLogDate(Object propertyId) {
    return "logDate".equals(propertyId);
  }

  public DateFormat getDateFormat(Object propertyId) {
    return isLogDate(propertyId) ? longFormat : shortFormat;
  }

}
