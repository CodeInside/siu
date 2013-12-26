/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.vaadin.beans;

import java.util.List;

public interface GsesBeanService {

	public void register(GsesBean gsesBean);

	public void unregister(GsesBean gsesBean);

	public List<GsesBean> getGsesBeans();

	public void addListener(GsesBeanListener listener);

	public void removeListener(GsesBeanListener listener);

}
