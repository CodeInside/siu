/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.supervisor;

import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import ru.codeinside.gses.webui.components.TaskGraph;

import javax.ejb.EJBException;

final public class DiagramPanel extends Panel {

  public DiagramPanel(final String definitionId, final String executionId) {
    setSizeFull();
    getContent().setSizeUndefined();
    TaskGraph taskGraph = null;
    try {
      taskGraph = new TaskGraph(definitionId, executionId);
    } catch (EJBException e) {
      // уже журналировано!
    }
    if (taskGraph != null && taskGraph.hasBlocks) {
      addComponent(taskGraph);
    } else if (executionId != null) {
      addComponent(new Label("Ветвь " + executionId + " маршрута " + definitionId + " уже исполнена"));
    } else {
      addComponent(new Label("Маршрут " + definitionId + " не найден"));
    }
  }
}
