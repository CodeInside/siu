/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.form.StartFormHandler;
import org.activiti.engine.impl.form.TaskFormHandler;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.test.ActivitiRule;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import ru.codeinside.gses.activiti.forms.api.definitions.FormDefinitionProvider;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyTree;
import ru.codeinside.gses.activiti.forms.types.VariableTypes;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.spi.PersistenceUnitTransactionType;
import java.util.HashMap;
import java.util.Map;

final public class InMemoryEngineRule extends ActivitiRule {

  public EntityManagerFactory emf;

  protected void initializeProcessEngine() {
    final String url = "jdbc:h2:mem:" + System.currentTimeMillis() + ";MODE=PostgreSQL;MVCC=true";
    Map props = new HashMap();
    props.put(PersistenceUnitProperties.TRANSACTION_TYPE, PersistenceUnitTransactionType.RESOURCE_LOCAL.name());
    props.put(PersistenceUnitProperties.JDBC_DRIVER, "org.h2.Driver");
    props.put(PersistenceUnitProperties.JDBC_URL, url);
    props.put(PersistenceUnitProperties.JDBC_USER, "sa");
    props.put(PersistenceUnitProperties.JDBC_PASSWORD, "");
    props.put(PersistenceUnitProperties.JDBC_READ_CONNECTIONS_MIN, "1");
    props.put(PersistenceUnitProperties.JDBC_WRITE_CONNECTIONS_MIN, "1");
    props.put("eclipselink.ddl-generation", "drop-and-create-tables"); // "create-tables" | "create-or-extend-tables"
    props.put("eclipselink.ddl-generation.output-mode", "database");
    props.put("eclipselink.logging.level", "SEVERE");
    emf = Persistence.createEntityManagerFactory("myPU", props);
    emf.createEntityManager().close();

    processEngine = new CustomStandaloneProcessEngineConfiguration() {
      {
        jdbcUrl = url;
        jpaEntityManagerFactory = emf;
        jpaHandleTransaction = true;
        databaseSchemaUpdate = "true";
        formTypes = new VariableTypes();
      }
    }.buildProcessEngine();
  }

  public <T> T executeCommand(Command<T> command) {
    return ((ServiceImpl) getFormService()).getCommandExecutor().execute(command);
  }

  public PropertyTree getStartFormDefinition(final String processDefinitionId) {
    return executeCommand(new Command<PropertyTree>() {
      @Override
      public PropertyTree execute(CommandContext commandContext) {
        StartFormHandler startFormHandler = Context.getProcessEngineConfiguration().getDeploymentCache()
          .findDeployedProcessDefinitionById(processDefinitionId).getStartFormHandler();
        return ((FormDefinitionProvider) startFormHandler).getPropertyTree();
      }
    });
  }

  public PropertyTree getFormDefinition(final String taskId) {
    return executeCommand(new Command<PropertyTree>() {
      @Override
      public PropertyTree execute(CommandContext commandContext) {
        TaskFormHandler taskFormHandler = commandContext.getTaskManager().findTaskById(taskId)
          .getTaskDefinition().getTaskFormHandler();
        return ((FormDefinitionProvider) taskFormHandler).getPropertyTree();
      }
    });
  }

}