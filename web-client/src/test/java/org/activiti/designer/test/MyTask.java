/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package org.activiti.designer.test;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

public class MyTask implements JavaDelegate {

	public Expression surname;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.out.println(surname.getExpressionText() + " = " + surname.getValue(execution));
		System.out.println("Process: " + execution.getVariableNames());
		System.out.println(" Locals: " + execution.getVariableNamesLocal());
		execution.setVariable("x", "xValue");
		execution.setVariableLocal("x1", "x1Value");

		System.out.println("Process: " + execution.getVariableNames());
		System.out.println(" Locals: " + execution.getVariableNamesLocal());

	}

}
