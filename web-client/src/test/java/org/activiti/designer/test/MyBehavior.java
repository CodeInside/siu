/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package org.activiti.designer.test;

import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

public class MyBehavior implements ActivityBehavior {

	@Override
	public void execute(ActivityExecution execution) throws Exception {
		final PvmTransition transition;
		if ("x".equals(execution.getVariable("x"))) {
			transition = execution.getActivity().findOutgoingTransition("ok");
		} else {
			System.out.println("Transitions: " + execution.getActivity().getOutgoingTransitions());
			transition = execution.getActivity().findOutgoingTransition("fail");
		}
		execution.take(transition);
	}

}
