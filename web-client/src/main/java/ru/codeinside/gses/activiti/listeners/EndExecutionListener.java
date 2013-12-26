/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.listeners;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

import com.google.common.base.Objects;

public class EndExecutionListener implements ExecutionListener {

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		System.out.println(" end process " + execution.getProcessInstanceId());
		if (Objects.equal(ExecutionListener.EVENTNAME_END, execution.getEventName()) && execution instanceof ExecutionEntity) {
			System.out.println("reason: " + ((ExecutionEntity) execution).getDeleteReason());
		}
	}
}
