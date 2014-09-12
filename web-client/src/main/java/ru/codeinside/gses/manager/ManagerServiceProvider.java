/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.manager;

import static ru.codeinside.gses.manager.ManagerService.instance;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Singleton
@Startup
@Lock(LockType.READ)
@ApplicationScoped
public class ManagerServiceProvider {

	@Inject
	ManagerService managerService;

	@PostConstruct
	void initialize() {
		synchronized (ManagerService.class) {
			if (instance == null) {
				instance = managerService;
			}
		}
	}

	@PreDestroy
	void shutdown() {
		synchronized (ManagerService.class) {
			if (instance == managerService) {
				instance = null;
			}
		}
	}

}
