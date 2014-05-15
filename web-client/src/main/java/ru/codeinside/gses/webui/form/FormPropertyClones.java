/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import org.activiti.engine.form.FormProperty;
import ru.codeinside.gses.activiti.history.VariableSnapshot;

import java.util.List;
import java.util.Map;

public class FormPropertyClones {
  public List<FormProperty> properties;
  Map<String, VariableSnapshot> snapshots;
}
