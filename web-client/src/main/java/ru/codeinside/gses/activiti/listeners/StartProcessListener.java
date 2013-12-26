/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.listeners;

import com.google.common.base.Objects;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.webui.Flash;

import javax.persistence.NoResultException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartProcessListener implements ExecutionListener {
    final static Logger logger = Logger.getLogger(StartProcessListener.class.getName());

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		if (Objects.equal(ExecutionListener.EVENTNAME_START, execution.getEventName())
				&& execution instanceof ExecutionEntity) {
			try {
                //todo событие в логе появляется два раза
                AdminServiceProvider.get().createLog(Flash.getActor(),
                                                     "execution",
                                                     execution.getId(),
                                                     "start",
                                                     execution.getEventName(), true);
			} catch (NoResultException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
			} catch (Exception e) {
                logger.log(Level.WARNING, e.getMessage(), e);
			}
		}
	}

}