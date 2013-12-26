/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.jta;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.db.DbSqlSessionFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomDbSqlSession extends DbSqlSession {
  public CustomDbSqlSession(DbSqlSessionFactory dbSqlSessionFactory) {
    super(dbSqlSessionFactory);
  }

  @Override
  public void dbSchemaDrop() {
    final Connection connection = sqlSession.getConnection();
    try {
      final Statement st = connection.createStatement();
      st.executeUpdate("DROP SEQUENCE activiti_seq");
      st.close();
    } catch (Exception e) {
      throw new ActivitiException("customize fail", e);
    }
    super.dbSchemaDrop();
  }

  @Override
  protected void dbSchemaCreateEngine() {
    super.dbSchemaCreateEngine();
    final Connection connection = sqlSession.getConnection();
    try {
      final String lastId;
      {
        final Statement st = connection.createStatement();
        final ResultSet rs = st.executeQuery("select value_ from act_ge_property where name_= 'next.dbid'");
        rs.next();
        lastId = rs.getString(1);
        rs.close();
        st.close();
      }
      {
        final Statement st = connection.createStatement();
        st.executeUpdate("CREATE SEQUENCE activiti_seq START " + lastId);
        st.close();
      }
    } catch (Exception e) {
      throw new ActivitiException("customize fail", e);
    }
  }

  public String getNextId() {
    final Connection connection = sqlSession.getConnection();
    try {
      final Statement statement = connection.createStatement();
      final ResultSet rs = statement.executeQuery("select nextval('activiti_seq')");
      rs.next();
      final String id = Long.toString(rs.getLong(1));
      rs.close();
      statement.close();
      return id;
    } catch (SQLException e) {
      throw new ActivitiException("nextId fail", e);
    }
  }

}
