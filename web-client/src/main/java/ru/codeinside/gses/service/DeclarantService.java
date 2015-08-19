/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.service;

import org.activiti.engine.ProcessEngine;
import ru.codeinside.adm.database.Procedure;
import ru.codeinside.adm.database.ProcedureProcessDefinition;
import ru.codeinside.adm.database.ProcedureType;
import ru.codeinside.adm.database.Service;
import ru.codeinside.adm.database.SmevChain;
import ru.codeinside.gses.activiti.forms.Signatures;
import ru.codeinside.gses.webui.form.SignatureType;

import java.util.List;
import java.util.Map;

public interface DeclarantService {

  public final String DECLARANT_TYPES = "DeclarantTypes";
  public final String VAR_SERVICE_ID = "serviceId";
  public final String VAR_PROCEDURE_TYPE_NAME = "procedureTypeName";
  public final String VAR_PROCEDURE_ID = "procedureId";
  public final String VAR_REQUESTER_LOGIN = "declarantLogin";


  int activeServicesCount(ProcedureType type);

  int activeProceduresCount(ProcedureType type, long serviceId);

  List<Service> selectActiveServices(ProcedureType type, int start, int count);

  List<Procedure> selectActiveProcedures(ProcedureType type, long serviceId, int start, int count);

  List<Procedure> selectDeclarantProcedures(ProcedureType type, long serviceId, int start, int count);

  ProcedureProcessDefinition selectActive(long procedureId);

  BidID declare(ProcessEngine engine, String processDefinitionId,
                Map<String, Object> properties, Map<SignatureType, Signatures> signatures, String declarer);

  BidID smevDeclare(SmevChain smevChain, String componentName, ProcessEngine engine, String processDefinitionId,
                    Map<String, Object> properties, String declarer, String tag);

  List<String> getBids(long gid);

  long getGlueIdByRequestIdRef(String requestIdRef);
}
