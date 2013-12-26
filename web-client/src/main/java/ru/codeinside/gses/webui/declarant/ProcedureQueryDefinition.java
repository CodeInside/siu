/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.declarant;

import ru.codeinside.adm.database.ProcedureType;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryDefinition;

final public class ProcedureQueryDefinition extends LazyQueryDefinition {

	private static final long serialVersionUID = 1L;

	final ProcedureType type;

	long serviceId = -1;

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	public ProcedureQueryDefinition(ProcedureType type) {
		super(false, 10);
		this.type = type;
		addProperty("id", String.class, null, true, true);
		addProperty("name", String.class, null, true, true);
	}
}
