/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api.impl;

import ru.codeinside.gws.api.ClientLog;
import ru.codeinside.gws.api.LogService;
import ru.codeinside.gws.api.ServerLog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

final public class LogServiceFileImpl implements LogService {

  final static Logger LOGGER = Logger.getLogger(LogServiceFileImpl.class.getName());

  final Set<String> enabledServers = new HashSet<String>();
  boolean serverLogEnabled = false;
  boolean logErrors = false;
  String logStatus = null;

  @Override
  public void setServerLogEnabled(boolean enabled) {
    synchronized (enabledServers) {
      serverLogEnabled = enabled;
      writeConfig();
    }
  }

  @Override
  public void setServerLogErrorsEnabled(boolean enabled) {
    synchronized (enabledServers) {
      logErrors = enabled;
      writeConfig();
    }
  }

  @Override
  public void setServerLogStatus(String status) {
    synchronized (enabledServers) {
      logStatus = status;
      writeConfig();
    }
  }

  @Override
  public boolean isServerLogEnabled() {
    synchronized (enabledServers) {
      return serverLogEnabled;
    }
  }

  @Override
  public boolean isServerLogErrorsEnabled() {
    synchronized (enabledServers) {
      return logErrors;
    }
  }

  @Override
  public String getServerLogStatus() {
    synchronized (enabledServers) {
      return logStatus;
    }
  }

  @Override
  public void setServerLogEnabled(String componentName, boolean enabled) {
    synchronized (enabledServers) {
      if (enabled) {
        enabledServers.add(componentName);
      } else {
        enabledServers.remove(componentName);
      }
      writeConfig();
    }
  }

  @Override
  public boolean isServerLogEnabled(String componentName) {
    synchronized (enabledServers) {
      return enabledServers.contains(componentName);
    }
  }

  @Override
  public String getPathInfo() {
    return LogSettings.getPath(false);
  }


  @Override
  public ClientLog createClientLog(long bid, String componentName, String processInstanceId,
                                   boolean isLogEnabled, boolean logErrors, String status) {
    return new FileClientLog(bid, componentName, processInstanceId, isLogEnabled, logErrors, status);
  }

  @Override
  public ServerLog createServerLog(String componentName) {
    boolean logEnabled;
    synchronized (enabledServers) {
      logEnabled = serverLogEnabled && enabledServers.contains(componentName);
      if (!logEnabled && !logErrors) {
        return null;
      }
    }
    return new FileServerLog(componentName, logEnabled, logErrors, logStatus);
  }


  File getCfgRoot() {
    String instanceRoot = System.getProperty("com.sun.aas.instanceRoot");
    final File cfgRoot;
    if (instanceRoot != null) {
      File asRoot = new File(instanceRoot);
      cfgRoot = new File(asRoot, "config");
    } else {
      // core scope !!!
      cfgRoot = new File("target");
    }
    return cfgRoot;
  }

  File getConfigFile() {
    return new File(getCfgRoot(), "smev-log.conf");
  }

  File getOldConfigFile() {
    return new File(getCfgRoot(), "smev-log-old.conf");
  }

  //TODO избавиться от старых конфигов
  void readConfig() throws IOException {
    File cfg = getConfigFile();
    if (cfg.exists()) {
      BufferedReader br = new BufferedReader(new FileReader(cfg));
      File cfgOld = getOldConfigFile();
        try {
          if (cfgOld.exists()) {
            String line = br.readLine();
            if (line != null) {
              line = line.trim();
              serverLogEnabled = Boolean.parseBoolean(line);
              logErrors = Boolean.parseBoolean((line = br.readLine()) == null ? null : line.trim());
              logStatus = (line = br.readLine()) == null ? null : line.trim();
              while (null != (line = br.readLine())) {
                line = line.trim();
                if (!line.isEmpty()) {
                  enabledServers.add(line);
                }
              }
            }
          } else {
            String line = br.readLine();
            if (line != null) {
              line = line.trim();
              serverLogEnabled = Boolean.parseBoolean(line);
              while (null != (line = br.readLine())) {
                line = line.trim();
                if (!line.isEmpty()) {
                  enabledServers.add(line);
                }
              }
            }
            cfg.renameTo(new File(cfg.getParent(), "smev-log-old.conf"));
          }
        } finally {
          Files.close(br);
        }
      }
  }


  void writeConfig() {
    File cfg = getConfigFile();
    try {
      BufferedWriter bf = new BufferedWriter(new FileWriter(cfg));
      try {
        bf.write(Boolean.toString(serverLogEnabled));
        bf.newLine();
        bf.write(Boolean.toString(logErrors));
        bf.newLine();
        bf.write(logStatus == null ? "" : logStatus);
        bf.newLine();
        for (String line : enabledServers) {
          bf.write(line);
          bf.newLine();
        }
        getOldConfigFile().createNewFile();
      } finally {
        Files.close(bf);
      }
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, "config writing failure", e);
    }
  }

}
