/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.vaadin.client;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.vaadin.terminal.gwt.client.ui.VTextField;

public class VNumericField extends VTextField {
	public VNumericField() {
		super();
		addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent e) {
				if (e.getCharCode() == KeyCodes.KEY_BACKSPACE
						|| e.getCharCode() == KeyCodes.KEY_DELETE
						|| e.getCharCode() == KeyCodes.KEY_END
						|| e.getCharCode() == KeyCodes.KEY_ENTER
						|| e.getCharCode() == KeyCodes.KEY_ESCAPE
						|| e.getCharCode() == KeyCodes.KEY_HOME
						|| e.getCharCode() == KeyCodes.KEY_LEFT
						|| e.getCharCode() == KeyCodes.KEY_PAGEDOWN
						|| e.getCharCode() == KeyCodes.KEY_PAGEUP
						|| e.getCharCode() == KeyCodes.KEY_RIGHT
						|| e.getCharCode() == KeyCodes.KEY_TAB
						|| e.isAnyModifierKeyDown())
					return;
				if (!Character.isDigit(e.getCharCode()))
					e.preventDefault();
			}
		});

		addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				try {
					if (event.getNativeKeyCode() == KeyCodes.KEY_UP) {
						int value = Integer.parseInt(getValue());

						value++;

						setValue(String.valueOf(value));

						event.preventDefault();
					} else if (event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
						int value = Integer.parseInt(getValue());

						value--;

						setValue(String.valueOf(value));

						event.preventDefault();
					}
				} catch (Exception e) {
					setValue("0");
				}
			}
		});
	}

	@SuppressWarnings("unused")
	private String getValueToOperate(String value) {
		return value.length() > 1 ? value.substring(value.length() - 2) : value
				.substring(value.length() - 1);
	}

	@SuppressWarnings("unused")
	private String getLiteralValue(String value) {
		return value.length() > 1 ? value.substring(0, value.length() - 2)
				: value.substring(0, value.length() - 1);
	}
}
