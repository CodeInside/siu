/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.migrations;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.codeinside.gses.liquibase.impl.DbConfig;
import ru.codeinside.gses.liquibase.impl.LegacyMigrationService;
import ru.codeinside.gses.liquibase.impl.Migration;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Запускать вручную, например:
 * <pre></pre>mvn test -Dtest=CleanSchema -Darquillian.launch=xeodon</pre>
 */
@RunWith(Arquillian.class)
public class CleanSchema extends Assert {

  static void clean() throws Exception {
    final UserTransaction tx = InitialContext.doLookup("UserTransaction");
    final Migration assistance = new LegacyMigrationService().create();
    tx.begin();

    try {
      final DataSource ds = InitialContext.doLookup(Databases.ADMIN);
      assistance.drop(new DbConfig(ds));
      Connection connection = ds.getConnection();
      PreparedStatement st = connection.prepareStatement("CREATE SCHEMA activiti; CREATE SCHEMA domain1; CREATE SCHEMA log;CREATE SCHEMA timer;");
      st.executeUpdate();
      st.close();
      connection.close();
      tx.commit();
    } finally {
      if (tx.getStatus() == Status.STATUS_ACTIVE) {
        tx.rollback();
      }
    }
  }

  @Test
  public void cleanAll() throws Exception {
    clean();
  }


}
