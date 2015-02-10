/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.behavior;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.impl.bpmn.behavior.MailActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.TaskActivityBehavior;
import org.activiti.engine.impl.bpmn.helper.ErrorPropagation;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.API;

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
      ProcessEngineConfiguration processEngineConfiguration = Context.getProcessEngineConfiguration();

      processEngineConfiguration.setMailServerUsername(AdminServiceProvider.get().getSystemProperty(API.MT_SENDER_LOGIN));
      processEngineConfiguration.setMailServerDefaultFrom(AdminServiceProvider.get().getSystemProperty(API.MT_DEFAULT_FROM));
      processEngineConfiguration.setMailServerPassword(AdminServiceProvider.get().getSystemProperty(API.MT_PASSWORD));
      processEngineConfiguration.setMailServerHost(AdminServiceProvider.get().getSystemProperty(API.MT_HOST));
      processEngineConfiguration.setMailServerPort(Integer.valueOf(AdminServiceProvider.get().getSystemProperty(API.MT_PORT)));
      processEngineConfiguration.setMailServerUseTLS(Boolean.valueOf(AdminServiceProvider.get().getSystemProperty(API.MT_TLS)));

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
