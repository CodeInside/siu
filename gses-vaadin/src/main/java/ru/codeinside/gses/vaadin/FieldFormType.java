/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.vaadin;

import com.vaadin.ui.Form;

public interface FieldFormType {

	String getFromType();

	String getFieldValue(String formPropertyId, Form form);

	String convertModelValueToFormValue(Object modelValue);

	Object convertFormValueToModelValue(String propertyValue);

	boolean usePattern();
	
	boolean useMap();
	
	FieldConstructor createConstructorOfField();
}