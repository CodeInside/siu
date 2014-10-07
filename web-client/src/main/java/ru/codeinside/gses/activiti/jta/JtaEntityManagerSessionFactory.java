/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.jta;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.variable.EntityManagerSession;

import javax.persistence.EntityManager;

/**
 * Создатель сессии EntityManger внтури JTA.
 */
final class JtaEntityManagerSessionFactory implements SessionFactory {

  final private EntityManager entityManager;


  public JtaEntityManagerSessionFactory(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Class<?> getSessionType() {
    return EntityManagerSession.class;
  }

  @Override
  public Session openSession() {
    return new JtaEntityManagerSession(entityManager);
  }

}
