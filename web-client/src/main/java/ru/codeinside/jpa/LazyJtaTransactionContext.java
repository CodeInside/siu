/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.jpa;

import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ленивая транзакция для JPA Container.
 */
final public class LazyJtaTransactionContext {

  final private UserTransaction userTransaction;
  final private EntityManager entityManager;

  boolean active;

  public LazyJtaTransactionContext(UserTransaction userTransaction, EntityManager entityManager) {
    this.userTransaction = userTransaction;
    this.entityManager = entityManager;
  }

  public EntityManager getEntityManager() {
    if (!active) {
      try {
        if (userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION) {
          userTransaction.begin();
          active = true;
        }
      } catch (NotSupportedException e) {
        logger().log(Level.WARNING, "failure", e);
        throw new RuntimeException(e);
      } catch (SystemException e) {
        logger().log(Level.WARNING, "failure", e);
        throw new RuntimeException(e);
      }
    }
    return entityManager;
  }

  public void close(boolean success) {
    if (active) {
      active = false;
      try {
        if (success) {
          userTransaction.commit();
        } else {
          userTransaction.rollback();
        }
      } catch (RollbackException e) {
        logger().log(Level.WARNING, "rollback", e);
      } catch (HeuristicMixedException e) {
        logger().log(Level.WARNING, "heuristic failure", e);
      } catch (HeuristicRollbackException e) {
        logger().log(Level.WARNING, "heuristic rollback", e);
      } catch (SystemException e) {
        logger().log(Level.WARNING, "failure", e);
      }
    }
  }

  private Logger logger() {
    return Logger.getLogger(getClass().getName());
  }

}
