/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.vaadin;

import java.util.Map;

import com.vaadin.ui.Field;
import com.vaadin.ui.Layout;

public interface FieldConstructor {
	Field createField(String name, String value, Layout layout, boolean writable, boolean required);

	void setPattern(String patternText);

	void setMap(Map<String, String> values);
}
