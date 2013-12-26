/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package org.activiti.designer.test;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.test.ActivitiRule;

/**
 * Упрощенные правила тестирования без Spring
 */
public class SimpleActivitiRule extends ActivitiRule {

	protected void initializeProcessEngine() {
		ProcessEngineConfiguration cfg = ProcessEngineConfiguration
				.createStandaloneInMemProcessEngineConfiguration();
		cfg.setDatabaseSchemaUpdate("true");
		processEngine = cfg.buildProcessEngine();
	}

}