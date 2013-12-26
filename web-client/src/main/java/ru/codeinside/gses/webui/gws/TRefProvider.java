/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.gws;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import ru.codeinside.gses.webui.osgi.TRefRegistryImpl;

@Singleton
@Startup
@ApplicationScoped
public class TRefProvider {

	@Inject
	ServiceRefRegistry serviceRegistry;
	
	
	transient static ServiceRefRegistry instance;

	@PostConstruct
	void initialize() {
		synchronized (TRefRegistryImpl.class) {
			if (instance == null) {
				instance = serviceRegistry;
			}			
		}		
	}
	
	@PreDestroy
	void shutdown() {
		synchronized (TRefRegistryImpl.class) {
			if (instance == serviceRegistry) {
				instance = null;
			}			
		}
	}

	public static ServiceRefRegistry get() {
		if (instance == null) {
			throw new IllegalStateException("Сервис не зарегистрирован!");
		}		
		return instance;
	}
}
