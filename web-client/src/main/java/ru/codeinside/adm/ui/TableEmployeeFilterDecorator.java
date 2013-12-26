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

    public String getEnumFilterDisplayName(Object propertyId, Object value) {
        return null;
    }

    public Resource getEnumFilterIcon(Object propertyId, Object value) {
        return null;
    }

    public String getBooleanFilterDisplayName(Object propertyId, boolean value) {
        return null;
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
        // use default caption
        return null;
    }

    public String getClearCaption() {
        // use default caption
        return null;
    }

    public boolean isTextFilterImmediate(Object propertyId) {
        // use text change events for all the text fields
        return true;
    }

    public int getTextChangeTimeout(Object propertyId) {
        // use the same timeout for all the text fields
        return 500;
    }

    public String getAllItemsVisibleString() {
        return "Все";
    }

    public NumberFilterPopupConfig getNumberFilterPopupConfig() {
        // work with default config
        return null;
    }

    public boolean usePopupForNumericProperty(Object propertyId) {
        return false;
    }

    public int getDateFieldResolution(Object propertyId) {
        return DateField.RESOLUTION_SEC;
    }

    public DateFormat getDateFormat(Object propertyId) {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    }

}
