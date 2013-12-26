/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.listeners;

import org.activiti.engine.impl.bpmn.behavior.MailActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ScopeImpl;
import org.activiti.engine.impl.util.xml.Element;

import ru.codeinside.gses.activiti.behavior.MailBehavior;

/**
 * 
 * Нужен для внедренния переопределенного behavior для MailTask
 * 
 */
public class MailBpmnParseListener extends AbstractBpmnParseListener {
	public void parseServiceTask(Element serviceTaskElement, ScopeImpl scope, ActivityImpl activity) {
		if (activity.getActivityBehavior() instanceof MailActivityBehavior) {
				activity.setActivityBehavior(new MailBehavior((MailActivityBehavior)activity.getActivityBehavior()));
		}
	}
}
