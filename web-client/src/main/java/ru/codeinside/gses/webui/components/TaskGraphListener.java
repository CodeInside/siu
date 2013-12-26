/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import org.activiti.engine.task.Task;
import ru.codeinside.gses.webui.components.api.Changer;
import ru.codeinside.gses.webui.executor.ExecutorFactory;

final public class TaskGraphListener implements Button.ClickListener {
  private static final long serialVersionUID = 1L;

  private final Changer changer;
  private final Task task;

  public TaskGraphListener(Changer changer, Task task) {
    this.changer = changer;
    this.task = task;
  }

  @Override
  public void buttonClick(ClickEvent event) {
    ExecutorFactory.showTask(changer, task);
  }
}