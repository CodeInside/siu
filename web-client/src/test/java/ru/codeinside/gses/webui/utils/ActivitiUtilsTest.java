/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static ru.codeinside.gses.webui.utils.ActivitiUtils.makeAdaptedActivitiName;

import org.junit.Test;

public class ActivitiUtilsTest {

	@Test
	public void testMakeAdaptedActivitiName() {		
		assertThat(makeAdaptedActivitiName("some.txt"), is("some.txt"));
		assertThat(makeAdaptedActivitiName("some.xml"), is("some.bpmn20.xml"));
		assertThat(makeAdaptedActivitiName("some.bpmn"), is("some.bpmn20.xml"));
		assertThat(makeAdaptedActivitiName("some.xml.zip"), is("some.xml.zip"));
	}
}
