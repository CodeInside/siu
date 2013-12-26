/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.service;

import org.activiti.engine.ProcessEngine;
import ru.codeinside.adm.database.*;
import ru.codeinside.gses.activiti.ActivitiFormProperties;

import javax.annotation.security.RolesAllowed;
import javax.ejb.TransactionAttribute;
import java.util.List;

import static javax.ejb.TransactionAttributeType.REQUIRED;

@TransactionAttribute(REQUIRED)
@RolesAllowed("Declarant")
public interface DeclarantService {

  int activeServicesCount(ProcedureType type);

  int activeProceduresCount(ProcedureType type, long serviceId);

  int declarantProceduresCount(ProcedureType type, long serviceId);

  List<Service> selectActiveServices(ProcedureType type, int start, int count);

  List<Procedure> selectActiveProcedures(ProcedureType type, long serviceId, int start, int count);

  List<Procedure> selectDeclarantProcedures(ProcedureType type, long serviceId, int start, int count);

  ProcedureProcessDefinition selectActive(long procedureId);

  String createProcess(ProcessEngine engine, ProcedureProcessDefinition def, ActivitiFormProperties properties, String login);

  String createProcess(ProcessEngine engine, String processDefinitionId, ActivitiFormProperties properties, String login);

  String getBidIdByProcessDefinitionId(String procDefId);

  String declareProcess(ProcessEngine processEngine, String processDefinitionId, ActivitiFormProperties properties);

  ExternalGlue declareProcess(String requestIdRef, String name, ProcessEngine engine, String processDefinitionId, ActivitiFormProperties properties, String login);

  Bid createDeclarantBid(String processDefinitionId, String declarant);
}
