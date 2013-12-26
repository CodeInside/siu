/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ProcedureProcessDefinition.class)
final public class ProcedureProcessDefinition_ {

	public static volatile SingularAttribute<ProcedureProcessDefinition, String> processDefinitionId;
	public static volatile SingularAttribute<ProcedureProcessDefinition, Procedure> procedure;
	public static volatile SingularAttribute<ProcedureProcessDefinition, DefinitionStatus> status;
}
