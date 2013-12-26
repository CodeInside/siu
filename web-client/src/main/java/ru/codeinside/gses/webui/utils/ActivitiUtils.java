/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.utils;

public class ActivitiUtils {
	
	public static String makeAdaptedActivitiName(final String name) {
		if (name.endsWith("bpmn20.xml")) {
			return name;
		}
		if (name.endsWith(".xml")) {
			return name.substring(0, name.length() - 3) + "bpmn20.xml";
		}
		if (name.endsWith(".bpmn")) {
			return name.substring(0, name.length() - 4) + "bpmn20.xml";
		}
		return name;
	}
}
