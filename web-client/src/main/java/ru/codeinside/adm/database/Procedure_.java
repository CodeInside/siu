/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Procedure.class)
final public class Procedure_ {
	public static volatile SingularAttribute<Procedure, Long> id;
	public static volatile SingularAttribute<Procedure, ProcedureType> type;
	public static volatile SingularAttribute<Procedure, Service> service;
	public static volatile SingularAttribute<Procedure, String> name;
	public static volatile SetAttribute<Procedure, ProcedureProcessDefinition> processDefinitions;

}
