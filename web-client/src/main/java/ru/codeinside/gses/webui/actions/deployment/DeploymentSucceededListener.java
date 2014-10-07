/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.actions.deployment;

import com.google.common.collect.Lists;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import ru.codeinside.adm.ui.LazyLoadingContainer2;
import ru.codeinside.gses.beans.ActivitiBean;
import ru.codeinside.gses.manager.ManagerService;
import ru.codeinside.gses.manager.SandboxEngine;
import ru.codeinside.gses.service.Functions;
import ru.codeinside.gses.service.PF;

import javax.ejb.EJBException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeploymentSucceededListener implements com.vaadin.ui.Upload.SucceededListener {

  private static final long serialVersionUID = 8870696618119836644L;
  private final DeploymentUploadReceiver receiver;
  private final String procedureId;
  private final String processDefId;
  private List<LazyLoadingContainer2> loadingContainers = Lists.newArrayList();

  public void addLoadingContainer(LazyLoadingContainer2 loadingContainer) {
    this.loadingContainers.add(loadingContainer);
  }

  public DeploymentSucceededListener(DeploymentUploadReceiver receiver, String procedureId, String processDefId) {
    this.receiver = receiver;
    this.procedureId = procedureId;
    this.processDefId = processDefId;
  }

  @Override
  public void uploadSucceeded(SucceededEvent event) {
    try {
      validateDeployment();
    } catch (EJBException e) {
      Throwable th = e.getCause() != null ? e.getCause() : e;
      String message = th.getMessage() != null ? th.getMessage() : th.getClass().toString();
      showMessage(event, "Ошибка в маршруте", message, Notification.TYPE_ERROR_MESSAGE);
      return;
    } catch (ActivitiException e) {
      showMessage(event, "Ошибка в маршруте", e.getMessage(), Notification.TYPE_ERROR_MESSAGE);
      return;
    }

    Deployment deploy = ActivitiBean.get().deploy(receiver.fileName, receiver.getBytes());

    String user = event.getUpload().getApplication().getUser().toString();
    ProcessDefinition pd = ActivitiBean.get().getProcessDefinitionByDeployment(deploy.getId(), user);
    ManagerService.get().createProcessDefination(procedureId, pd, user, processDefId);

    event.getUpload().setData(null);
    for (LazyLoadingContainer2 loadingContainer : loadingContainers) {
      loadingContainer.fireItemSetChange();
    }
    showMessage(event,
      "Новая версия",
      "Загружен описатель маршрута №" + pd.getDeploymentId() + ", с ключём " + pd.getKey(),
      Notification.TYPE_HUMANIZED_MESSAGE);
  }

  private void validateDeployment() {
    Boolean isOk = !Functions.withEngine(new PF<Boolean>() {
      private static final long serialVersionUID = 1L;

      public Boolean apply(ProcessEngine realEngine) {
        ProcessEngine sandbox = SandboxEngine.create();
        try {
          ProcessDefinition processDefinition = createProcessDefinition(sandbox);
          if (processDefinition == null) {
            throw new ActivitiException("Маршрут не загружен. Проверьте суффикс имени файла");
          }
          RepositoryServiceImpl impl = (RepositoryServiceImpl) sandbox.getRepositoryService();
          ProcessDefinitionEntity pdEntity = (ProcessDefinitionEntity) impl.getDeployedProcessDefinition(processDefinition.getId());
          for (ActivityImpl ac : pdEntity.getActivities()) {
            if (ac.getActivityBehavior() instanceof UserTaskActivityBehavior) {
              UserTaskActivityBehavior utab = (UserTaskActivityBehavior) ac.getActivityBehavior();
              Set<Expression> candidateUsers = utab.getTaskDefinition().getCandidateUserIdExpressions();
              Set<Expression> candidateGroups = utab.getTaskDefinition().getCandidateGroupIdExpressions();
              if (candidateUsers.isEmpty() && candidateGroups.isEmpty()) {
                throw new ActivitiException("В " + utab.getTaskDefinition().getKey() + " не указаны кандидаты исполнения");
              }
            }
          }
          return ManagerService.get().existProcessDefinitionWithKeyOtherProcedure(procedureId, processDefinition.getKey());
        } finally {
          try {
            sandbox.close();
          } catch (Throwable e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "cleanup", e);
          }
        }
      }

      private ProcessDefinition createProcessDefinition(ProcessEngine sandbox) {
        InputStream is = new ByteArrayInputStream(receiver.getBytes());
        Deployment tempDeployment = sandbox.getRepositoryService().createDeployment().name(receiver.fileName)
          .addInputStream(receiver.fileName, is).deploy();
        ProcessDefinition tempProcessDefinition = sandbox.getRepositoryService().createProcessDefinitionQuery()
          .deploymentId(tempDeployment.getId()).singleResult();
        return tempProcessDefinition;
      }
    });
    if (!isOk) {
      throw new ActivitiException("'Key' маршрута задействован в другой процедуре");
    }
  }

  private void showMessage(SucceededEvent event, String title, String message, int type) {
    Window window = currentWindow(event);
    if (window != null) {
      window.showNotification(title, message, type);
    }
  }

  private Window currentWindow(SucceededEvent event) {
    return event.getUpload().getWindow();
  }

}