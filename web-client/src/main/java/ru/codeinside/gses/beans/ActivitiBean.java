/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.beans;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.identity.Group;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.service.Functions;
import ru.codeinside.gses.service.PF;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.utils.ActivitiUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ActivitiBean {

    public static final String FORCED_DELETE = "forced delete";

    private static ActivitiBean activitiBean = new ActivitiBean();

    public static ActivitiBean get() {
        return activitiBean;
    }

    public ProcessDefinition getProcessDefinitionByDeployment(final String deploymentId, String user) {
        return Functions.withRepository(user, new Function<RepositoryService, ProcessDefinition>() {
            public ProcessDefinition apply(RepositoryService srv) {
                return srv.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
            }
        });
    }

    public ProcessDefinition getProcessDefinition(final String processDefinitionId, String user) {
        return Functions.withRepository(user, new Function<RepositoryService, ProcessDefinition>() {
            public ProcessDefinition apply(RepositoryService srv) {
                return srv.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
            }
        });
    }

    public Deployment deploy(final String fileName, final byte[] bytes) {
        return Functions.withRepository(new Function<RepositoryService, Deployment>() {
            public Deployment apply(RepositoryService repositoryService) {
                InputStream is = new ByteArrayInputStream(bytes);
                String adaptedName = ActivitiUtils.makeAdaptedActivitiName(fileName);
                Deployment deploy = repositoryService.createDeployment().name(adaptedName)
                        .addInputStream(adaptedName, is).deploy();
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return deploy;
            }

        });
    }

    public boolean start(final String processDefinitionId) {
        return Functions.withEngine(new PF<Boolean>() {
            private static final long serialVersionUID = 1L;

            public Boolean apply(ProcessEngine engine) {
                StartFormData startFormData = engine.getFormService().getStartFormData(processDefinitionId);

                if (!hasStartFormData(startFormData)) {
                    engine.getRepositoryService().createProcessDefinitionQuery()
                            .processDefinitionId(processDefinitionId).singleResult();
                    return engine.getRuntimeService().startProcessInstanceById(processDefinitionId) != null;

                }
                return false;
            }
        });

    }

    /**
     * Назначить задачу taskId на исполнителя toUserLogin. Форисровать переназначение если forceAssign == true.
     */
    public String claim(final String taskId, final String toUserLogin, final String byUser, final boolean forceAssign) {
        return Functions.withEngine(new PF<String>() {
            private static final long serialVersionUID = 1L;

            public String apply(final ProcessEngine engine) {
                final TaskService taskService = engine.getTaskService();
                final Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
                if (task == null) {
                    return "Задача уже исполнена!";
                }
                try {
                    checkCandidates(engine, toUserLogin, task);
                } catch (Exception e) {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "claim fail", e);
                    return e.getMessage();
                }
                if (forceAssign) {
                    taskService.setAssignee(task.getId(), toUserLogin);
                } else {
                    try {
                        taskService.claim(task.getId(), toUserLogin);
                    } catch (ActivitiTaskAlreadyClaimedException e) {
                        return "Задача уже назначена исполнителю!";
                    } catch (RuntimeException e) {
                        Logger.getLogger(getClass().getName()).log(Level.INFO, "claim fail", e);
                        return e.getMessage();
                    }
                }
                // TODO: либо обработать событие TaskListener.EVENTNAME_ASSIGNMENT
                AdminServiceProvider.get().saveBidAssignment(engine, task.getProcessInstanceId(), toUserLogin);
                return "";
            }
        });
    }

    private void checkCandidates(ProcessEngine engine, String user, Task task) throws Exception {
        if (user.equals(task.getAssignee())) {
            return;
        }
        TaskService taskService = engine.getTaskService();

        boolean canClaim = taskService.createTaskQuery().taskCandidateUser(user).taskId(task.getId())
                .count() == 1;

        List<String> candidateGroups = Lists.newArrayList();
        for (Group g : engine.getIdentityService().createGroupQuery().groupMember(user).list()) {
            candidateGroups.add(g.getId());
        }

        boolean canClaimByGroup = taskService.createTaskQuery().taskCandidateGroupIn(candidateGroups)
                .taskId(task.getId()).count() == 1;

        if (!canClaim && !canClaimByGroup) {
            throw new Exception("У Вас нет прав доступа к этому этапу");
        }
    }


    private boolean hasStartFormData(StartFormData startFormData) {
        return (startFormData != null) && //
                (((startFormData.getFormProperties() != null) && //
                        (startFormData.getFormProperties().size() > 0)) || (startFormData.getFormKey() != null));
    }

    public String deleteProcessInstance(final String taskId) {
        return Functions.withEngine(new PF<String>() {
            private static final long serialVersionUID = 1L;

            public String apply(final ProcessEngine engine) {
                engine.getIdentityService().setAuthenticatedUserId(Flash.login());
                Task task = engine.getTaskService().createTaskQuery().taskId(taskId).singleResult();

                engine.getRuntimeService().deleteProcessInstance(task.getProcessInstanceId(), FORCED_DELETE);
                return null;
            }
        });
    }
}
