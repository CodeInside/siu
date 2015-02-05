/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.behavior;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.impl.bpmn.behavior.MailActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.TaskActivityBehavior;
import org.activiti.engine.impl.bpmn.helper.ErrorPropagation;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

/**
 *
 * Переопределяет поведение mail task.
 *
 */
public class MailBehavior extends TaskActivityBehavior  {
	private MailActivityBehavior activityBehavior;
	public MailBehavior(MailActivityBehavior activityBehavior) {
		this.activityBehavior = activityBehavior;
	}

  public void execute(ActivityExecution execution) throws Exception {
    try {
      this.activityBehavior.execute(execution);
    } catch (Exception err) {
      ErrorPropagation.propagateError(new BpmnError("mail-send-error", err.getMessage()), execution);

      String message;
      Throwable e = err.getCause();
      if (e != null)
        message = e.getMessage();
      else
        message = err.getMessage();
      execution.setVariable("mail-send-error", message);

      err.printStackTrace();
    }
  }
}
