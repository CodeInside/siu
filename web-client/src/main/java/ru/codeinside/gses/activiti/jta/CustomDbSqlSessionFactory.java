/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.jta;

import org.activiti.engine.impl.db.DbSqlSessionFactory;
import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;

final public class CustomDbSqlSessionFactory implements SessionFactory {

  private final DbSqlSessionFactory factory;

  public CustomDbSqlSessionFactory(final DbSqlSessionFactory factory) {
    this.factory = factory;
  }

  @Override
  public Class<?> getSessionType() {
    return factory.getSessionType();
  }

  @Override
  public Session openSession() {
    return new CustomDbSqlSession(factory);
  }

}
