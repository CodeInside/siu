/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.vaadin.beans.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.codeinside.gses.vaadin.beans.GsesBean;
import ru.codeinside.gses.vaadin.beans.GsesBeanListener;
import ru.codeinside.gses.vaadin.beans.GsesBeanService;

public class GsesBeanServiceImpl implements GsesBeanService {

	private List<GsesBean> listGsesBean = new ArrayList<GsesBean>();

	private ArrayList<GsesBeanListener> listeners = new ArrayList<GsesBeanListener>();

	@Override
	public void register(GsesBean gsesBean) {
		listGsesBean.add(gsesBean);
		for (GsesBeanListener listener : (ArrayList<GsesBeanListener>) listeners.clone()) {
			listener.gsesBeanRegistered(this, gsesBean);
		}
	}

	@Override
	public void unregister(GsesBean gsesBean) {
		listGsesBean.remove(gsesBean);
		for (GsesBeanListener listener : (ArrayList<GsesBeanListener>) listeners.clone()) {
			listener.gsesBeanRegistered(this, gsesBean);
		}
	}

	@Override
	public List<GsesBean> getGsesBeans() {
		return Collections.unmodifiableList(listGsesBean);
	}

	@Override
	public void addListener(GsesBeanListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(GsesBeanListener listener) {
		listeners.remove(listener);
	}

}
