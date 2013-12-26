/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vaadin.ui.Form;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.form.FormPropertyHandler;
import org.activiti.engine.impl.form.FormPropertyImpl;
import org.junit.Test;
import ru.codeinside.gses.webui.components.TaskFormHandlerShowUi;
import ru.codeinside.gses.webui.components.TempForm;

import java.util.List;
import java.util.Map;

public class TaskFormHandlerShowUiTest {
    @Test
    public void test(){
        final Form form = new Form();
        List<FormProperty > formProperties = Lists.newArrayList();
        formProperties.add(fp("login", "Логин"));
        formProperties.add(fp("+Block", "Человек"));
            formProperties.add(fp("userName", "Имя"));
            formProperties.add(fp("age", "Возраст"));
            formProperties.add(fp("+Gruop", "Группа"));
                formProperties.add(fp("class", "Класс"));
                formProperties.add(fp("litter","Буква"));
            formProperties.add(fp("-Gruop","Группа"));
            formProperties.add(fp("sex","Пол"));
        formProperties.add(fp("-Block","Человек"));
        formProperties.add(fp("def","Дополнительная инфа"));
        Map<String, String> values = Maps.newHashMap();
        values.put("login", "login0");
        values.put("userName_1", "userName_1");
        values.put("class_1_1", "class_1_1");
        values.put("class_1_2", "class_1_2");
        values.put("litter_1_2", "litter_1_2");
        values.put("userName_2", "userName_2");
        TempForm baseForm = TaskFormHandlerShowUi.createBaseForm("", "", form, formProperties, "", values);

        baseForm.fillForm(form);

        System.out.println("-----");
        for (Object o : form.getItemPropertyIds()){
            System.out.println(o + "= " +  form.getItemProperty(o).toString());
        }
        System.out.println("-----");
    }

    private FormPropertyImpl fp(String id, String name) {
        FormPropertyHandler formPropertyHandler = new FormPropertyHandler();
        formPropertyHandler.setId(id);
        formPropertyHandler.setName(name);
        return new FormPropertyImpl(formPropertyHandler);
    }
}

