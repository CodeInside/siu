/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import org.activiti.engine.impl.form.TaskFormDataImpl;

public class CustomTaskFormData extends TaskFormDataImpl implements PropertyTreeProvider, CloneTreeProvider {

  PropertyTree propertyTree;
  CloneTree cloneTree;

  public PropertyTree getPropertyTree() {
    return propertyTree;
  }

  public void setPropertyTree(PropertyTree propertyTree) {
    this.propertyTree = propertyTree;
  }

  public void setCloneTree(CloneTree cloneTree) {
    this.cloneTree = cloneTree;
  }

  @Override
  public CloneTree getCloneTree() {
    return cloneTree;
  }
}
