/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.data;

import java.io.Serializable;

import ru.codeinside.gses.lazyquerycontainer.Query;
import ru.codeinside.gses.lazyquerycontainer.QueryDefinition;
import ru.codeinside.gses.lazyquerycontainer.QueryFactory;

public class AssigneeTaskBeanQueryFactory implements QueryFactory, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public void setQueryDefinition(QueryDefinition queryDefinition) {
	}

	@Override
	public Query constructQuery(Object[] sortPropertyIds, boolean[] sortStates) {
		final String orderBy;
		final boolean asc;
		if (sortPropertyIds != null && sortPropertyIds.length > 0) {
			orderBy = (String) sortPropertyIds[0];
			asc = sortStates[0];
		} else {
			orderBy = "";
			asc = true;
		}
		return new AssigneeTaskBeanQuery(orderBy, asc);
	}

}
