/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.listeners;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.webui.Flash;

import java.util.logging.Level;
import java.util.logging.Logger;

final class StartProcessListener implements ExecutionListener {

  @Override
  public void notify(DelegateExecution execution) throws Exception {
    try {
      AdminServiceProvider.get().createLog(Flash.getActor(), "execution", execution.getId(), execution.getEventName(), null, true);
    } catch (Exception e) {
      Logger.getLogger(getClass().getName()).log(Level.WARNING, "log fail", e);
    }
  }

}