/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.api.definitions;

import org.activiti.engine.impl.form.FormTypes;

import java.util.Map;

public abstract class VariableTypes extends FormTypes {
  abstract public Map<String, VariableType> getTypes();
}
