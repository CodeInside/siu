/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.declarant;

import java.io.Serializable;

import ru.codeinside.gses.lazyquerycontainer.Query;
import ru.codeinside.gses.lazyquerycontainer.QueryDefinition;
import ru.codeinside.gses.lazyquerycontainer.QueryFactory;

final public class ServiceQueryFactory implements QueryFactory, Serializable {

	private static final long serialVersionUID = 1L;
	private ServiceQueryDefinition def;

    private boolean showActive;

    public ServiceQueryFactory(boolean showActive) {
        this.showActive = showActive;
    }


    @Override
	public void setQueryDefinition(QueryDefinition queryDefinition) {
		def = (ServiceQueryDefinition) queryDefinition;
	}

	@Override
	public Query constructQuery(Object[] sortPropertyIds, boolean[] sortStates) {
		return new ServiceQuery(def.type, showActive);
	}

}
