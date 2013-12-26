/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.manager;


import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.beans.DirectoryBeanProvider;
import ru.codeinside.gses.service.impl.DeclarantServiceImpl;
import ru.codeinside.gses.webui.DeclarantTypeChanged;
import ru.codeinside.gses.webui.Flash;

public class AddTupleButtonListener implements Button.ClickListener {

    private static final long serialVersionUID = 11323321L;

    final Form createFieldForm;
    final FilterTable directoryTable;
    final TextField keyField;
    final TextField valField;
    final Table dirMapTable;

    public AddTupleButtonListener(Form createFieldForm, FilterTable directoryTable, TextField keyField, TextField valField, Table dirMapTable) {
        this.createFieldForm = createFieldForm;
        this.directoryTable = directoryTable;
        this.keyField = keyField;
        this.valField = valField;
        this.dirMapTable = dirMapTable;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        try {
            createFieldForm.commit();

            Object rowId = directoryTable.getValue();
            final String dirName = (String) directoryTable.getContainerProperty(rowId, "name").getValue();

            final String key = keyField.getValue().toString();
            final String value = valField.getValue().toString();

            DirectoryBeanProvider.get().add(dirName, key, value);
            AdminServiceProvider.get().createLog(Flash.getActor(), "Directory value", dirName, "add",
                                               "key => ".concat(key).concat("value =>").concat(value), true);
            keyField.setValue("");
            valField.setValue("");
            ManagerWorkplace.reloadMap(dirName, dirMapTable);
            if(DeclarantServiceImpl.DECLARANT_TYPES.equals(dirName)){
                Flash.fire(new DeclarantTypeChanged(this));
            }
        } catch (Exception e) {
            //
        }
    }
}
