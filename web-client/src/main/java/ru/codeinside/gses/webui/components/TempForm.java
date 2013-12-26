/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;

import java.util.LinkedList;
import java.util.Map;

public class TempForm {

    private Map<Object,Field> fields = Maps.newHashMap();
    private LinkedList<Object> fieldOrder= Lists.newLinkedList();

    public void fillForm(Form form){
        for(Object o : fieldOrder){
            form.addField(o, fields.get(o));
        }
    }

    public void copy(TempForm tempForm ){
        for(Object o : tempForm.fieldOrder){
            add(o, tempForm.fields.get(o));
        }
    }

    public void add(Object o, Field f){
        fieldOrder.addLast(o);
        fields.put(o, f);
    }

    public boolean hasFields(){
        return !fieldOrder.isEmpty();
    }
}
