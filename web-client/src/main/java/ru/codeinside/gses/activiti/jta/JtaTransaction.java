/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.jta;

import org.apache.ibatis.transaction.Transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * JTA транзакция для iBatis.
 */
final class JtaTransaction implements Transaction {

  private final DataSource dataSource;

  public JtaTransaction(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }

  @Override
  public void commit() throws SQLException {

  }

  @Override
  public void rollback() throws SQLException {

  }

  @Override
  public void close() throws SQLException {

  }
}
