/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.form.docx;

import org.codehaus.jackson.map.ObjectMapper;
import ru.codeinside.gses.form.FormConverter;
import ru.codeinside.gses.form.FormData;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

final public class ConverterProxy implements FormConverter {

  final Logger logger = Logger.getLogger(getClass().getName());
  final ThreadGroup group = new ThreadGroup("docx");
  final String OK = "ok" + System.getProperty("line.separator");
  final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
    @Override
    public Thread newThread(Runnable runnable) {
      Thread thread = new Thread(group, runnable, "docxProxy", 256 * 1024);
      thread.setDaemon(true);
      thread.setPriority(Thread.NORM_PRIORITY - 1);
      return thread;
    }
  });

  RemoteService remoteService;

  public ConverterProxy() {

    executorService.scheduleWithFixedDelay(new Runnable() {
      @Override
      public void run() {
        ping();
      }
    }, 30, 30, TimeUnit.SECONDS);

    executorService.schedule(new Runnable() {
      @Override
      public void run() {
        refreshService();
      }
    }, 5, TimeUnit.SECONDS);

  }

  public void close() {
    closeService();
    executorService.shutdownNow();
  }

  @Override
  public void createForm(FormData data) {
    if (data == null) {
      return;
    }

    final byte[] request = createRequest(data);

    Future<?> future = executorService.submit(new Runnable() {
      @Override
      public void run() {
        process(request);
      }
    });

    try {
      future.get(30, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("interrupted");
    } catch (ExecutionException e) {
      throw new RuntimeException(e.getCause());
    } catch (TimeoutException e) {
      throw new RuntimeException("executionTimeOut");
    }
  }

  void process(byte[] request) {
    refreshService();

    long startAt = System.currentTimeMillis();
    StringBuilder response = new StringBuilder();

    try {
      OutputStream out = remoteService.getOut();
      BufferedReader in = remoteService.getIn();

      out.write(request);
      out.write('\n');
      out.flush();

      for (; ; ) {
        if (!in.ready()) {
          long now = System.currentTimeMillis();
          if (now - startAt > 25000L) {
            closeService();
            throw new RuntimeException("responseTimeOut");
          }
          try {
            Thread.sleep(100);
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("interrupted");
          }
          continue;
        }
        int c = in.read();
        if (c == -1) {
          closeService();
          throw new RuntimeException("brokenPipe");
        }
        response.append((char) c);
        if (OK.equals(response.toString())) {
          break;
        }
      }
    } catch (IOException e) {
      closeService();
      throw new RuntimeException(e);
    }
  }

  byte[] createRequest(FormData data) {
    ObjectMapper objectMapper = new ObjectMapper();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      objectMapper.writeValue(bos, data);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return bos.toByteArray();
  }

  void refreshService() {
    if (remoteService != null) {
      if (!remoteService.isAlive()) {
        closeService();
      }
    }
    if (remoteService == null) {
      logger.fine("open service");
      remoteService = new RemoteService();
    }
  }

  void closeService() {
    if (remoteService != null) {
      logger.fine("close service");
      remoteService.close();
      remoteService = null;
    }
  }

  void ping() {
    refreshService();

    OutputStream out = remoteService.getOut();
    try {
      out.write('\n');
      out.flush();
    } catch (IOException e) {
      logger.log(Level.INFO, "io error", e);
      closeService();
    }
  }
}
