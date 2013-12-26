/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.vaadin;

import com.vaadin.data.Property;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.TextField;
import ru.codeinside.gses.vaadin.client.VNumericField;

@ClientWidget(VNumericField.class)
public class NumericField extends TextField {
	private static final long serialVersionUID = 3119804051599796474L;

	public NumericField() {
		super();
	}

	public NumericField(Property dataSource) {
		super(dataSource);
	}

	public NumericField(String caption, Property dataSource) {
		super(caption, dataSource);
	}

	public NumericField(String caption, String value) {
		super(caption, value);
	}

	public NumericField(String caption) {
		super(caption);
	}
}
