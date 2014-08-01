/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm;

import org.activiti.engine.ProcessEngine;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

final class CheckExecutionThread implements Runnable {

  private final ProcessEngine processEngine;
  private final CheckExecutionDates checkExecutionDates;

  CheckExecutionThread(ProcessEngine processEngine, CheckExecutionDates checkExecutionDates) {
    this.processEngine = processEngine;
    this.checkExecutionDates = checkExecutionDates;
  }

  @Override
  public void run() {
    try {
      Email email = checkExecutionDates.checkDates(processEngine);
      if (email != null) {
        email.send();
      }
    } catch (EmailException e) {
      checkExecutionDates.createLog(e);
    }
  }
}
