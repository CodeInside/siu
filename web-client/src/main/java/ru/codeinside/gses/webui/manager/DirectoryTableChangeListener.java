/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.manager;


import com.google.common.base.Objects;
import com.vaadin.data.Property;
import com.vaadin.ui.Form;
import com.vaadin.ui.Table;
import org.apache.commons.lang.StringUtils;
import org.tepi.filtertable.FilterTable;

public class DirectoryTableChangeListener implements Property.ValueChangeListener{

    final Form createFieldForm;
    final FilterTable directoryTable;
    final Table dirMapTable;

    public DirectoryTableChangeListener(Form createFieldForm, FilterTable directoryTable, Table dirMapTable) {
        this.createFieldForm = createFieldForm;
        this.directoryTable = directoryTable;
        this.dirMapTable = dirMapTable;
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        Object value = event.getProperty().getValue();
        final String dirName = Objects.firstNonNull(value, "").toString();
        createFieldForm.setCaption("Добавление значения в справочник " + dirName);
        directoryTable.requestRepaint();
        dirMapTable.setVisible(true);
        createFieldForm.setVisible(StringUtils.isNotEmpty(dirName));
        ManagerWorkplace.reloadMap(dirName, dirMapTable);
    }
}
