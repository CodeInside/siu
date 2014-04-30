/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import org.activiti.engine.test.ActivitiRule;
import org.eclipse.persistence.config.PersistenceUnitProperties;

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
    props.put("eclipselink.ddl-generation", "create-tables"); // "create-or-extend-tables"
    props.put("eclipselink.ddl-generation.output-mode", "database");
    emf = Persistence.createEntityManagerFactory("myPU", props);
    emf.createEntityManager().close();

    processEngine = new CustomStandaloneProcessEngineConfiguration() {
      {
        jdbcUrl = url;
        jpaEntityManagerFactory = emf;
        jpaHandleTransaction = true;
        databaseSchemaUpdate = "true";

      }
    }.buildProcessEngine();
  }

}