/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.jta;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

/**
 * Создатель JTA транзакций для iBatis.
 */
final class JtaTransactionFactory implements TransactionFactory {

  @Override
  public void setProperties(Properties props) {

  }

  @Override
  public Transaction newTransaction(Connection conn) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Transaction newTransaction(final DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
    return new JtaTransaction(dataSource);
  }

}
