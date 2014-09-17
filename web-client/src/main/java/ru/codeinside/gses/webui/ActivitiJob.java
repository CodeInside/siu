/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.cmd.ExecuteJobsCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.jobexecutor.JobExecutor;
import org.activiti.engine.impl.jobexecutor.JobExecutorContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.DependsOn;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Lock(LockType.READ)
@DependsOn("BaseBean")
@TransactionManagement(TransactionManagementType.BEAN)
public class ActivitiJob implements ActivitiJobProvider {

  final Logger logger = Logger.getLogger(getClass().getName());
  final AtomicInteger total = new AtomicInteger(0);

  volatile JobExecutor jobExecutor;
  volatile ExecutorService executorService;


  /**
   * Код внутри Activiti использует suspend() и resume(tx) для того чтобы открепить транзакцию от потока
   * а потом снова прикрепить, поэтому UserTransaction не подойдёт!
   */
  @Resource
  TransactionManager transactionManager;

  final class Executor extends JobExecutor {

    Executor() {
      // Сколько "забирать" задач из базы за один запрос
      setMaxJobsPerAcquisition(Runtime.getRuntime().availableProcessors());

      // Время бездействия когда нет задач
      setWaitTimeInMillis(9 * 1000);
    }

    @Override
    protected void startExecutingJobs() {
      logger.info("Запуск потока выборки задач");
      startJobAcquisitionThread();
    }

    @Override
    protected void stopExecutingJobs() {
      if (jobAcquisitionThread != null) {
        logger.info("Остановка потока выборки задач");
        stopJobAcquisitionThread();
      }
    }

    @Override
    protected void executeJobs(List<String> jobIds) {
      for (String jobId : jobIds) {
        scheduleJob(jobId);
      }
    }
  }

  void scheduleJob(String jobId) {
    if (executorService == null) {
      throw new IllegalStateException("Нет исполнителя для " + jobId);
    }
    executorService.submit(new SingleJobExecution(jobId));
  }

  void executeJob(final String jobId, final int num) {
    final JobExecutorContext jobExecutorContext = new JobExecutorContext();
    Context.setJobExecutorContext(jobExecutorContext);
    final List<String> currentProcessorJobQueue = jobExecutorContext.getCurrentProcessorJobQueue();
    currentProcessorJobQueue.add(jobId);
    try {
      transactionManager.begin();
      boolean doCommit = false;
      try {
        final CommandExecutor commandExecutor = jobExecutor.getCommandExecutor();
        while (!currentProcessorJobQueue.isEmpty()) {
          final String jobToExecute = currentProcessorJobQueue.remove(0);
          commandExecutor.execute(new ExecuteJobsCmd(jobToExecute));
        }
        doCommit = true;
      } catch (ActivitiException e) {
        // Стек уже журналирован в commandExecutor!
        logger.log(Level.SEVERE, "Сбой исполнения задачи #" + num + "/" + jobId);
      } catch (Exception e) {
        logger.log(Level.SEVERE, "Сбой исполнения задачи #" + num + "/" + jobId, e);
      }
      if (doCommit) {
        transactionManager.commit();
        // Запускаем лишь при успешном коммите
        for (final String id : currentProcessorJobQueue) {
          scheduleJob(id);
        }
      } else {
        transactionManager.rollback();
      }
    } catch (NotSupportedException e) {
      logger.log(Level.SEVERE, "Транзакции не поддерживаются", e);
    } catch (SystemException e) {
      logger.log(Level.SEVERE, "Системная ошибка", e);
    } catch (HeuristicRollbackException e) {
      logger.log(Level.SEVERE, "Ошибка отката", e);
    } catch (RollbackException e) {
      logger.log(Level.SEVERE, "Ошибка отката", e);
    } catch (HeuristicMixedException e) {
      logger.log(Level.SEVERE, "Смешанная ошибка", e);
    } finally {
      Context.removeJobExecutorContext();
    }
  }

  @PostConstruct
  void afterConstruct() {
    if (executorService == null) {
      logger.info("Создание исполнителя фоновых задач");
      executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactory() {
        final AtomicInteger serialNumber = new AtomicInteger();

        /**
         * Ставим имя и низкий приоритет на всякий случай.
         */
        @Override
        public Thread newThread(final Runnable r) {
          final Thread thread = new Thread(r, "Activiti-" + serialNumber.incrementAndGet());
          thread.setPriority(Thread.MIN_PRIORITY);
          return thread;
        }
      });
    }
  }

  @PreDestroy
  void beforeDestroy() {
    if (jobExecutor != null) {
      jobExecutor.shutdown();
      jobExecutor = null;
    }
    if (executorService != null) {
      logger.info("Остановка исполнителя фоновых задач");
      executorService.shutdownNow();
      executorService = null;
    }
  }

  @Override
  public void startNow() {
    if (jobExecutor != null) {
      jobExecutor.start();
    } else {
      logger.info("Нет исполнителя для запуска");
    }
  }

  /**
   * Предполагается что лишь Engine будет создавать исполнителя.
   */
  public JobExecutor createJobExecutor() {
    if (jobExecutor != null) {
      jobExecutor.shutdown();
    }
    jobExecutor = new Executor();
    return jobExecutor;
  }

  final class SingleJobExecution implements Runnable {
    private final String jobId;
    private final int num;

    public SingleJobExecution(String jobId) {
      this.jobId = jobId;
      num = total.incrementAndGet();
    }

    @Override
    public void run() {
      if (jobExecutor == null) {
        logger.severe("Нет исполнителя для задачи #" + num + "/" + jobId);
      } else {
        try {
          executeJob(jobId, num);
        } catch (Exception e) {
          logger.log(Level.SEVERE, "Ошибка исполнения задачи #" + num + "/" + jobId, e);
        }
      }
    }
  }
}
