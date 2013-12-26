/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.migrations;

import liquibase.exception.LiquibaseException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.codeinside.gses.activiti.CustomStandaloneProcessEngineConfiguration;
import ru.codeinside.gses.liquibase.impl.DbConfig;
import ru.codeinside.gses.liquibase.impl.LegacyMigrationService;
import ru.codeinside.gses.liquibase.impl.Migration;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import javax.sql.XADataSource;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.activiti.engine.ProcessEngineConfiguration.HISTORY_FULL;

/**
 * Запускать вручную, например:
 * <pre></pre>mvn test -Dtest=GenerateSchema -Darquillian.launch=xeodon</pre>
 */
@RunWith(Arquillian.class)
public class GenerateSchema extends Assert {

  /**
   * Внимание! Генерация не атомарна, так как JTA НЕ используется!
   */
  static void generate(boolean drop) throws NamingException, LiquibaseException, IOException, SQLException {
    final Migration assistance = new LegacyMigrationService().create();
    for (final String entry : Databases.UNITS) {
      final DataSource ds = InitialContext.doLookup(Databases.ADMIN);
      if (drop) {
        assistance.drop(new DbConfig(ds.unwrap(XADataSource.class)));
      }
      final Map<String, String> props = new LinkedHashMap<String, String>();
      props.put("eclipselink.ddl-generation", "create-tables");
      props.put("eclipselink.ddl-generation.output-mode", "database");
      props.put("eclipselink.logging.level", "SEVERE");
      EntityManagerFactory emf = Persistence.createEntityManagerFactory(entry, props);
      emf.createEntityManager().close();
      emf.close();
    }
    {
      final DataSource ds = InitialContext.doLookup(Databases.ADMIN);
      if (drop) {
        assistance.drop(new DbConfig(ds.unwrap(XADataSource.class)));
      }
      StandaloneProcessEngineConfiguration cfg = new CustomStandaloneProcessEngineConfiguration();
      cfg.setHistory(HISTORY_FULL);
      cfg.setDatabaseSchemaUpdate("true");
      cfg.setDataSource(ds);
      ProcessEngine engine = cfg.buildProcessEngine();
      engine.close();
    }
  }

  static void generateToFiles() throws Exception {
    String appDir = "target";
    for (final String entry : Databases.UNITS) {
      String createName = "createDDL_" + entry + ".jdbc";
      String dropName = "dropDDL_" + entry + ".jdbc";
      final Map<String, String> props = new LinkedHashMap<String, String>();
      props.put("eclipselink.ddl-generation", "create-tables");
      props.put("eclipselink.ddl-generation.output-mode", "sql-script");
      props.put("eclipselink.logging.level", "SEVERE");
      props.put("eclipselink.application-location", appDir);
      props.put("eclipselink.create-ddl-jdbc-file-name", createName);
      props.put("eclipselink.drop-ddl-jdbc-file-name", dropName);
      EntityManagerFactory emf = Persistence.createEntityManagerFactory(entry, props);
      emf.createEntityManager().close();
      emf.close();
    }
  }


  static void createDiff0() throws Exception {
    final Migration assistance = new LegacyMigrationService().create();
    final DataSource tmp = InitialContext.doLookup(Databases.TMP);
    final DbConfig emptyDb = new DbConfig(tmp.unwrap(XADataSource.class));
    assistance.drop(emptyDb);
    File target = new File("target");
    for (final String dbName : Databases.ALL) {
      final DataSource ds = InitialContext.doLookup(dbName);
      final DbConfig db = new DbConfig(ds.unwrap(XADataSource.class));
      final PrintStream ps = new PrintStream(new File(target, "migration_" + dbName.substring(dbName.indexOf('/') + 1) + ".diff"));
      boolean ok = assistance.validate(emptyDb, db, ps);
      ps.close();
      assertFalse(ok);
    }
  }

  @Test
  public void generateAll() throws Exception {
    generate(false);
    createDiff0();
  }
}
