/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import com.vaadin.ui.Component;
import ru.codeinside.gses.webui.components.api.Changer;

import java.io.Serializable;

public class ShowDiagramComponentParameterObject implements Serializable {
    public Changer changer;
    public String processDefinitionId;
    public String executionId;
    public String height;
    public String width;
    public String windowHeader;
    public String caption;
}