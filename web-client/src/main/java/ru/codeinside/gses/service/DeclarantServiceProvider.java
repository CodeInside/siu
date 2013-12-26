/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import ru.codeinside.gses.service.impl.DeclarantServiceImpl;

@Singleton
@Startup
@ApplicationScoped
public class DeclarantServiceProvider {

	@Inject
	DeclarantService declarantService;
	
	transient static DeclarantService instance;

	@PostConstruct
	void initialize() {
		synchronized (DeclarantServiceImpl.class) {
			if (instance == null) {
				instance = declarantService;
			}			
		}		
	}
	
	@PreDestroy
	void shutdown() {
		synchronized (DeclarantServiceImpl.class) {
			if (instance == declarantService) {
				instance = null;
			}			
		}
	}

	public static DeclarantService get() {
		if (instance == null) {
			throw new IllegalStateException("Сервис не зарегистрирован!");
		}		
		return instance;
	}
}
