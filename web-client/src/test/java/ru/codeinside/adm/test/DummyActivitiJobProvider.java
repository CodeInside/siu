/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.test;

import org.activiti.engine.impl.jobexecutor.JobExecutor;
import ru.codeinside.gses.webui.ActivitiJobProvider;

class DummyActivitiJobProvider implements ActivitiJobProvider {

  @Override
  public void startNow() {
  }

  @Override
  public JobExecutor createJobExecutor() {
    return null;
  }
}
