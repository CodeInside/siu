/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.manager;

import com.vaadin.ui.*;
import org.tepi.filtertable.FilterTable;

import java.io.Serializable;

public class DirectoryPanel implements Serializable {

    static Component createDirectoryPanel() {
        HorizontalSplitPanel horSplit = new HorizontalSplitPanel();
        horSplit.setSizeFull();
        horSplit.setMargin(true);

        Panel panel00 = new Panel();
        Panel panel01 = new Panel();

        Panel panel10 = new Panel();

        horSplit.setFirstComponent(panel00);

        VerticalLayout vl = new VerticalLayout();
        horSplit.setSecondComponent(vl);

        vl.addComponent(panel01);
        vl.addComponent(panel10);

        vl.setSpacing(true);

        horSplit.setWidth("100%");
        vl.setHeight("100%");

        panel00.setHeight("100%");
        panel00.setWidth("100%");

        panel01.setWidth("100%");
        panel01.setHeight("100%");
        panel10.setHeight("100%");
        horSplit.setSplitPosition(35);
        vl.setExpandRatio(panel01, 0.25f);
        vl.setExpandRatio(panel10, 0.75f);

        final Table dirMapTable = ManagerWorkplace.createDirectoryMapTable();
        final FilterTable directoryTable = ManagerWorkplace.createDirectoryTable();
        dirMapTable.setVisible(false);

        final Form createFieldForm = new Form();
        createFieldForm.setCaption("Добавление значения в справочник");

        final TextField keyField = new TextField("Ключ");
        keyField.setRequired(true);
        keyField.setMaxLength(254);
        createFieldForm.addField("key", keyField);

        final TextField valField = new TextField("Значение");
        valField.setRequired(true);
        valField.setMaxLength(1022);
        createFieldForm.addField("val", valField);
        createFieldForm.setVisible(false);

        Button addButton = new Button("Сохранить", new AddTupleButtonListener(createFieldForm, directoryTable, keyField, valField, dirMapTable));
        createFieldForm.addField("submit", addButton);

        directoryTable.addListener(new DirectoryTableChangeListener(createFieldForm,  directoryTable, dirMapTable));

        ManagerWorkplace.buildContainer(directoryTable, createFieldForm, dirMapTable);
        directoryTable.setColumnHeaders(new String[]{"Название", "", ""});

        final Form createDirectory = new Form();
        createDirectory.setCaption("Создание справочника");
        final TextField field = new TextField("Название");
        field.setRequired(true);
        field.setMaxLength(255);
        field.setRequiredError("Введите название справочника");
        createDirectory.addField("name", field);
        Button createButton = new Button("Сохранить", new CreateDirectoryButtonListener( field, createDirectory, directoryTable));
        createDirectory.addField("submit", createButton);

        Panel loadPanel = new Panel();
        loadPanel.setCaption("Импорт справочников");

        UploadDirectory events = new UploadDirectory(directoryTable, dirMapTable);

        Upload c = new Upload("", events);
        c.addListener(events);

        c.setButtonCaption("Загрузить");
        loadPanel.addComponent(c);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(loadPanel);
        verticalLayout.addComponent(createDirectory);
        verticalLayout.addComponent(directoryTable);

        panel00.addComponent(verticalLayout);

        panel01.addComponent(createFieldForm);

        panel10.addComponent(dirMapTable);

        return horSplit;
    }
}