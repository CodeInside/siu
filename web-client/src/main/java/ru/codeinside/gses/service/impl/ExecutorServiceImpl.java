/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.service.impl;

import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.task.TaskDefinition;
import ru.codeinside.adm.database.Procedure;
import ru.codeinside.adm.database.ProcedureProcessDefinition;
import ru.codeinside.gses.service.ExecutorService;
import ru.codeinside.gses.webui.Flash;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;

import static javax.ejb.TransactionManagementType.CONTAINER;

@TransactionManagement(CONTAINER)
@Stateless
public class ExecutorServiceImpl implements ExecutorService {
    @PersistenceContext(unitName = "myPU")
    EntityManager em;

    @Override
    public String getProcedureNameByDefinitionId(String processDefinitionId) {
        ProcedureProcessDefinition procedureProcessDefinition = em.find(ProcedureProcessDefinition.class, processDefinitionId);
        try {
            String processName = procedureProcessDefinition.getProcedure().getName();
            return processName;
        } catch (NullPointerException e) {
            System.out.println("Can`t get procedure on process definition");
        }
        return null;
    }

    @Override
    public Map<String, TaskDefinition> selectTasksByProcedureId(long procedureId) {
        return getTaskDefinitions(procedureId);
    }

    @Override
    public int countTasksByProcedureId(long procedureId) {
        return getTaskDefinitions(procedureId).size();
    }

    private Map<String, TaskDefinition> getTaskDefinitions(long procedureId) {
        Procedure procedure = em.find(Procedure.class, procedureId);
        ProcedureProcessDefinition ppd = (ProcedureProcessDefinition) em.createQuery("select p from procedure_process_definition p where p.procedure=:proc").
                setParameter("proc", procedure).getResultList().get(0);
        final RepositoryServiceImpl repositoryService = (RepositoryServiceImpl) Flash.flash().getProcessEngine().getRepositoryService();
        ProcessDefinitionEntity def = (ProcessDefinitionEntity) repositoryService.getDeployedProcessDefinition(ppd.getProcessDefinitionId());
        return def.getTaskDefinitions();
    }

}
