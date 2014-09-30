/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.jta;

import org.activiti.engine.impl.variable.EntityManagerSession;

import javax.persistence.EntityManager;

/**
 * Сессия для EntityManager внутри JTA.
 */
final class JtaEntityManagerSession implements EntityManagerSession {

  private final EntityManager entityManager;

  public JtaEntityManagerSession(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public EntityManager getEntityManager() {
    return entityManager;
  }

  @Override
  public void flush() {
    entityManager.flush();
  }

  @Override
  public void close() {
    entityManager.flush();
    entityManager.clear();
  }
}
