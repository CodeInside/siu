/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui;

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

public final class DelegateCloseHandler implements TabSheet.CloseHandler {
	private static final long serialVersionUID = 1L;

	public void onTabClose(TabSheet tabsheet, Component c) {
		// Вызываем ДО отсоединения
		if (c instanceof TabSheet.CloseHandler) {
			((TabSheet.CloseHandler) c).onTabClose(tabsheet, c);
		}
		tabsheet.removeComponent(c);
	}
}