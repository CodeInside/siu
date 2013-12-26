/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.jta;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandInterceptor;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.logging.Level;
import java.util.logging.Logger;

final public class JtaTransactionInterceptor extends CommandInterceptor {

  private final Logger logger = Logger.getLogger(getClass().getName());
  private final TransactionManager transactionManager;
  private final boolean requiresNew;

  final static String STATUSES[] = {
    "ACTIVE",
    "MARKED_ROLLBACK",
    "PREPARED",
    "COMMITTED",
    "ROLLEDBACK",
    "UNKNOWN",
    "NO_TRANSACTION",
    "PREPARING",
    "COMMITTING",
    "ROLLING_BACK"
  };

  public JtaTransactionInterceptor(final TransactionManager transactionManager, final boolean requiresNew) {
    this.transactionManager = transactionManager;
    this.requiresNew = requiresNew;
  }

  public <T> T execute(Command<T> command) {
    Transaction oldTx = null;
    try {
      boolean existing = isExisting();
      boolean isNew = !existing || requiresNew;
      if (existing && requiresNew) {
        oldTx = doSuspend();
      }
      if (isNew) {
        doBegin();
      }
      T result;
      final int priority = Thread.currentThread().getPriority();
      try {
        // есть глючные сценарии с бесконечными циклами - дадим шанс остальным
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        result = next.execute(command);
      } catch (RuntimeException ex) {
        doRollback(isNew);
        throw ex;
      } catch (Error err) {
        doRollback(isNew);
        throw err;
      } catch (Exception ex) {
        doRollback(isNew);
        throw new UndeclaredThrowableException(ex, "TransactionCallback threw undeclared checked exception");
      } finally {
        Thread.currentThread().setPriority(priority);
      }
      if (isNew) {
        doCommit();
      }
      return result;
    } finally {
      doResume(oldTx);
    }
  }

  private void doBegin() {
    try {
      transactionManager.begin();
    } catch (NotSupportedException e) {
      throw new TransactionException("Unable to begin transaction", e);
    } catch (SystemException e) {
      throw new TransactionException("Unable to begin transaction", e);
    }
  }

  private boolean isExisting() {
    try {
      return transactionManager.getStatus() != Status.STATUS_NO_TRANSACTION;
    } catch (SystemException e) {
      throw new TransactionException("Unable to retrieve transaction status", e);
    }
  }


  private Transaction doSuspend() {
    try {
      return transactionManager.suspend();
    } catch (SystemException e) {
      throw new TransactionException("Unable to suspend transaction", e);
    }
  }

  private void doResume(final Transaction tx) {
    if (tx != null) {
      try {
        final int status = tx.getStatus();
        if (status != Status.STATUS_ROLLEDBACK) {
          if (status != Status.STATUS_ACTIVE) {
            logger.info("Статус транзакции перед присоединением: " + STATUSES[status]);
          }
          transactionManager.resume(tx);
        }
      } catch (SystemException e) {
        throw new TransactionException("Unable to resume transaction", e);
      } catch (InvalidTransactionException e) {
        throw new TransactionException("Unable to resume transaction", e);
      }
    }
  }

  private void doCommit() {
    try {
      transactionManager.commit();
    } catch (HeuristicMixedException e) {
      throw new TransactionException("Unable to commit transaction", e);
    } catch (HeuristicRollbackException e) {
      throw new TransactionException("Unable to commit transaction", e);
    } catch (RollbackException e) {
      throw new TransactionException("Unable to commit transaction", e);
    } catch (SystemException e) {
      throw new TransactionException("Unable to commit transaction", e);
    } catch (RuntimeException e) {
      doRollback(true);
      throw e;
    } catch (Error e) {
      doRollback(true);
      throw e;
    }
  }

  private void doRollback(boolean isNew) {
    try {
      if (isNew) {
        transactionManager.rollback();
      } else {
        transactionManager.setRollbackOnly();
      }
    } catch (SystemException e) {
      logger.log(Level.FINE, "Error when rolling back transaction", e);
    }
  }

  private static class TransactionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private TransactionException(String s, Throwable throwable) {
      super(s, throwable);
    }
  }
}
