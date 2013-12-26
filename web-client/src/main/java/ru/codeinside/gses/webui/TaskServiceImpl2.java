/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui;

import org.activiti.engine.impl.TaskServiceImpl;
import org.activiti.engine.task.TaskQuery;
import ru.codeinside.gses.webui.data.TaskQueryImpl2;

public class TaskServiceImpl2 extends TaskServiceImpl {
  @Override
  public TaskQuery createTaskQuery() {
    return new TaskQueryImpl2(commandExecutor);
  }

}
