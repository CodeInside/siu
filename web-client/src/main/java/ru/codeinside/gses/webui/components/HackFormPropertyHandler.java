/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.form.FormPropertyHandler;

public class HackFormPropertyHandler extends FormPropertyHandler implements FormProperty {

    private final String defaultValue;

    public HackFormPropertyHandler(FormPropertyHandler base, String defaultValue){
        setId(base.getId());
        setName(base.getName());
        setType(base.getType());
        this.defaultValue = defaultValue;
        setReadable(base.isReadable());
        setWritable(base.isWritable());
        setRequired(base.isRequired());
    }

    @Override
    public String getValue() {
        return defaultValue;
    }
}
