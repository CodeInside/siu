/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.jta;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * Источник данных, привязанный к EntityManager в JTA.
 */
final class JtaEntityManagerDataSource implements DataSource {

  final private EntityManager entityManager;

  public JtaEntityManagerDataSource(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Connection getConnection() throws SQLException {
    Connection connection = entityManager.unwrap(Connection.class);
    if (connection == null) {
      throw new SQLException("Отсутсвтует активная транзакция");
    }
    return connection;
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getLoginTimeout() throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    throw new UnsupportedOperationException();
  }
}
