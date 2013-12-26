/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.migrations;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.gses.liquibase.impl.DbConfig;
import ru.codeinside.gses.liquibase.impl.LegacyMigrationService;
import ru.codeinside.gses.liquibase.impl.Migration;
import ru.codeinside.gses.liquibase.impl.MigrationServiceImpl;
import ru.codeinside.gses.webui.osgi.Renovation;
import ru.codeinside.log.Log;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;
import java.io.File;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.LinkedList;
import java.util.Map;

/**
 * Можно запустить вручную, например:
 * <pre></pre>mvn test -Dtest=ValidateSchemaTest -Darquillian.launch=xeodon</pre>
 */
@RunWith(Arquillian.class)
public class ValidateSchemaTest extends Assert {

  @Deployment
  public static JavaArchive createDeployment() {
    return ShrinkWrap.create(JavaArchive.class)
      .addPackage(Employee.class.getPackage())
      .addPackage(Log.class.getPackage())
      .addClass(BaseBean.class)
      .addAsResource("META-INF/persistence.xml");
    //.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
  }

  static void assertMigration() throws Exception {
    final UserTransaction tx = InitialContext.doLookup("UserTransaction");
    tx.begin();
    final DataSource tmp = InitialContext.doLookup(Databases.TMP);
    final DbConfig tmpDb = new DbConfig(tmp);
    File target = new File("target");
    LinkedList<String> bad = new LinkedList<String>();
    for (final Map.Entry<String, String> entry : Databases.VERSIONS.entrySet()) {
      final String dbName = entry.getKey();
      final String version = entry.getValue();
      final String simpleName = dbName.substring(dbName.indexOf('/') + 1);
      final String resource = "migrations/" + simpleName + "/migration.xml";
      final Migration assistance = new LegacyMigrationService().create(resource, ValidateSchemaTest.class.getClassLoader());
      final DataSource ds = InitialContext.doLookup(dbName);
      final DbConfig db = new DbConfig(ds);
      final PrintStream ps = new PrintStream(new File(target, "validation_" + simpleName + ".diff"));
      if (!assistance.validate(version, db, tmpDb, ps)) {
        bad.add(simpleName);
      }
      ps.close();
    }
    if (bad.isEmpty()) {
      tx.commit();
    } else {
      tx.rollback();
    }
    assertTrue("Ошибки в миграции " + bad + ", детали в файлах target/validation_{dbName}.diff", bad.isEmpty());
  }


  @Test
  public void validateAll() throws Exception {
    Renovation renovation = new Renovation();
    renovation.validateResources();
    renovation.validatePersistence();
    GenerateSchema.generateToFiles();

    final String profile = System.getProperty("arquillian.launch");
    if (profile == null) {
      System.out.println();
      System.out.println();
      System.out.println("----------------------------------------------------");
      System.out.println("Профиль по умолчанию пока не поддерживает PostgreSQL");
      System.out.println("----------------------------------------------------");
      System.out.println();
      System.out.println();
    } else {
      CleanSchema.clean();
      renovation.renovate(new MigrationServiceImpl());

      CleanSchema.clean();
      GenerateSchema.generate(false);
      createViewForSecurityRealm();
      assertMigration();
    }
  }

  private void createViewForSecurityRealm() {
    try {
      final DataSource admin = InitialContext.doLookup(Databases.ADMIN);
      Connection connection = admin.getConnection();
      PreparedStatement st = connection.prepareStatement("create view active_employee as select * from employee where locked = false ");
      st.executeUpdate();
      st.close();
      connection.close();
    } catch (Exception e) {
      e.printStackTrace();
      fail("Can't create view for security realm " + e.getMessage());

    }
  }
}
