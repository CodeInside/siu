/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm;


import ru.codeinside.adm.database.ExternalGlue;
import ru.codeinside.adm.database.HttpLog;
import ru.codeinside.adm.database.OepLog;
import ru.codeinside.adm.database.SoapPacket;
import ru.codeinside.gses.webui.osgi.LogCustomizer;

import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;

@TransactionManagement
@TransactionAttribute
@Singleton
@Stateless
@DependsOn("BaseBean")
public class LogConverter {
  @PersistenceContext(unitName = "myPU")
  public EntityManager em;

  private String dirPath;

  public OepLog getOepLog(EntityManager em, String marker) {
    List<OepLog> list = em.createQuery("select l from OepLog l where l.marker = :marker").setParameter("marker", marker).getResultList();
    final OepLog result;
    if (list.isEmpty()) {
      result = new OepLog();
      result.setDate(new Date());
      result.setMarker(marker);
    } else {
      result = list.get(0);
    }
    return result;
  }

  public static boolean isEmpty(String str) {
    return str == null || str.length() == 0;
  }

  @TransactionAttribute(REQUIRES_NEW)
  public void logToBd() {
    try {
      String pathInfo = getDirPath();
      if (isEmpty(pathInfo)) {
        return;
      }
      File[] files = new File(pathInfo).listFiles();
      if (files == null) {
        return;
      }
      for (File f : files) {
        if (f == null) {
          continue;
        }
        OepLog log = getOepLog(em, f.getName());
        if (f.listFiles() != null) {
          for (File logFile : f.listFiles()) {
            if (logFile == null) {
              continue;
            }
            String name = logFile.getName();
            if (name.startsWith("log-http")) {
              int lastIndex = name.lastIndexOf("-");
              int index = name.indexOf("-", "log-http".length());

              log.setClient(Boolean.valueOf(name.substring(lastIndex + 1)));

              String httpString = readFileAsString(logFile);
              if (Boolean.valueOf(name.substring(index + 1, lastIndex))) {
                log.setSendHttp(new HttpLog(httpString));
              } else {
                log.setReceiveHttp(new HttpLog(httpString));
              }
            }
            if (name.startsWith("log-Error")) {
              log.setError(readFileAsString(logFile));
            }
            if (name.startsWith("log-Date")) {
              log.setLogDate(readFileAsString(logFile));
            }
            if (name.startsWith("log-ProcessInstanceId")) {
              String processInstanceId = readFileAsString(logFile);
              List<Long> l = em.createQuery("select b.id from Bid b where b.processInstanceId=:processInstanceId", Long.class).setParameter("processInstanceId", processInstanceId).getResultList();
              if (!l.isEmpty()) {
                log.setBidId(l.get(0).toString());
              }
            }
            if (name.startsWith("log-ClientRequest")) {
              log.setSendPacket(getPacket(logFile));
              setInfoSystem(log, log.getSendPacket().getSender());
            }
            if (name.startsWith("log-ClientResponse")) {
              log.setReceivePacket(getPacket(logFile));
              setInfoSystem(log, log.getReceivePacket().getSender());
            }
            if (name.startsWith("log-ServerRequest")) {
              log.setSendPacket(getPacket(logFile));
              setInfoSystem(log, log.getSendPacket().getRecipient());
            }
            if (name.startsWith("log-ServerResponse")) {
              log.setReceivePacket(getPacket(logFile));
              setInfoSystem(log, log.getReceivePacket().getRecipient());
            }
            if (isEmpty(log.getBidId())) {
              ExternalGlue glue = getExternalGlue(log);
              if (glue != null) {
                log.setBidId(glue.getBidId());
              }
            }
            persist(log);
            deleteFile(logFile);
          }
        }
        deleteFile(f);
      }
    } catch (Throwable th) {
      th.printStackTrace();
    }
  }

  private void deleteFile(File file) {
    if (canDelete()) {
      file.delete(); //обработать откат транзакции
    }
  }

  private void persist(OepLog log) {
    if (em != null) {
      em.persist(log);
    }
  }

  private boolean canDelete() {
    return em != null;
  }

  public String getDirPath() {
    if (dirPath == null) {
      dirPath = LogCustomizer.getLogger().getPathInfo();
    }
    return dirPath;
  }

  public void setDirPath(String path) {
    dirPath = path;
  }

  private ExternalGlue getExternalGlue(OepLog log) {
    ExternalGlue externalGlue = getExternalGlue(log.getReceivePacket());
    if (externalGlue != null) {
      return externalGlue;
    }
    return getExternalGlue(log.getSendPacket());
  }

  private ExternalGlue getExternalGlue(SoapPacket soapPacket) {
    if (em == null) {
      return null;
    }
    if (soapPacket != null && !isEmpty(soapPacket.getOriginRequestIdRef())) {
      List resultList = em.createQuery("select g from ExternalGlue g where g.requestIdRef = :requestIdRef").setParameter("requestIdRef", soapPacket.getOriginRequestIdRef()).getResultList();
      if (!resultList.isEmpty()) {
        return (ExternalGlue) resultList.get(0);
      }
    }
    return null;
  }

  private static void setInfoSystem(OepLog log, String sender) {
    if (isEmpty(log.getInfoSystem()) && !isEmpty(sender)) {
      log.setInfoSystem(sender);
    }
  }

  private static String readFileAsString(File file) {
    try {
      StringBuffer fileData = new StringBuffer();
      BufferedReader reader = new BufferedReader(new FileReader(file));
      char[] buf = new char[1024];
      int numRead = 0;
      while ((numRead = reader.read(buf)) != -1) {
        String readData = String.valueOf(buf, 0, numRead);
        fileData.append(readData);
      }
      reader.close();
      return fileData.toString();
    } catch (IOException e) {
      return null;
    }
  }

  private static SoapPacket getPacket(File logFile) {
    String splitter = "!!;";

    String packetString = readFileAsString(logFile);
    String[] values = packetString.split(splitter);

    int index = 0;
    SoapPacket result = new SoapPacket();
    result.setSender(getValue(values, index++));
    result.setRecipient(getValue(values, index++));
    result.setOriginator(getValue(values, index++));
    result.setService(getValue(values, index++));
    result.setTypeCode(getValue(values, index++));
    result.setStatus(getValue(values, index++));
    result.setDate(getValue(values, index++));
    result.setRequestIdRef(getValue(values, index++));
    result.setOriginRequestIdRef(getValue(values, index++));
    result.setServiceCode(getValue(values, index++));
    result.setCaseNumber(getValue(values, index++));
    result.setExchangeType(getValue(values, index++));

    return result;
  }

  private static String getValue(String[] values, int index) {
    if (values.length <= index) {
      return "";
    }
    return values[index];
  }

}
