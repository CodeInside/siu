/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.manager;

import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.TextField;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.beans.DirectoryBeanProvider;
import ru.codeinside.gses.service.impl.DeclarantServiceImpl;
import ru.codeinside.gses.webui.DeclarantTypeChanged;
import ru.codeinside.gses.webui.Flash;

public class CreateDirectoryButtonListener implements Button.ClickListener{

    private static final long serialVersionUID = 1132332423432431L;

    final TextField field;
    final Form createDirectory;
    final FilterTable directoryTable;

    public CreateDirectoryButtonListener(TextField field, Form createDirectory, FilterTable directoryTable) {
        this.field = field;
        this.createDirectory = createDirectory;
        this.directoryTable = directoryTable;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        try {
            String dirName = field.getValue().toString().trim();
            if (dirName.equals("")) {
                return;
            }
            createDirectory.commit();
            DirectoryBeanProvider.get().create(dirName);
            AdminServiceProvider.get().createLog(Flash.getActor(), "Directory", dirName, "create", "Manual create",
                                                 true);
            ManagerWorkplace.refreshDirectoryTable(directoryTable);
            if(DeclarantServiceImpl.DECLARANT_TYPES.equals(dirName)){
                Flash.fire(new DeclarantTypeChanged(this));
            }
        } catch (Exception e) {
            // Ignored, we'll let the Form handle the errors
        }
    }
}
