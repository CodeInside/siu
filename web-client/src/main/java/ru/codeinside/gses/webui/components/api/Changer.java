/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components.api;

import java.io.Serializable;

import com.vaadin.ui.Component;

/**
 * Общий интерфейс для замены одного компонента другим
 */
public interface Changer extends Serializable {

	void set(Component newComponent, String name);

	void change(Component newComponent);

	void back();

	void clear();
}
