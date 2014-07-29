/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm;


import commons.Streams;
import org.codehaus.jackson.map.ObjectMapper;
import ru.codeinside.adm.database.ExternalGlue;
import ru.codeinside.adm.database.HttpLog;
import ru.codeinside.adm.database.SmevLog;
import ru.codeinside.adm.database.SoapPacket;
import ru.codeinside.gses.API;
import ru.codeinside.gses.webui.osgi.LogCustomizer;
import ru.codeinside.gws.log.format.Metadata;
import ru.codeinside.gws.log.format.Pack;

import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;

@TransactionManagement
@TransactionAttribute
@Singleton
@DependsOn("BaseBean")
public class LogConverter {

  @PersistenceContext(unitName = "myPU")
  EntityManager em;

  final Logger logger = Logger.getLogger(getClass().getName());
  String dirPath;
  Storage storage;

  // test support
  public interface Storage {
    String getLazyDirPath();

    SmevLog findLogEntry(String marker);

    void store(SmevLog smevLog);
  }


  public void setStorage(Storage storage) {
    this.storage = storage;
  }

  @TransactionAttribute(REQUIRES_NEW)
  public boolean logToBd() {
    if (storage == null) {
      storage = new Storage() {
        @Override
        public String getLazyDirPath() {
          return LogCustomizer.getStoragePath();
        }

        @Override
        public SmevLog findLogEntry(String marker) {
          List<SmevLog> rs = em.
            createQuery("select l from SmevLog l where l.marker = :marker", SmevLog.class)
            .setMaxResults(1)
            .setParameter("marker", marker)
            .getResultList();
          return rs.isEmpty() ? null : rs.get(0);
        }

        @Override
        public void store(SmevLog smevLog) {
          em.persist(smevLog);
        }
      };
    }

    String pathInfo = getDirPath();
    if (isEmpty(pathInfo)) {
      return false;
    }

    final File logPackage = listLowDirs(new File(pathInfo));
    if (logPackage == null) {
      return false;
    }
    try {
      SmevLog log = getOepLog(logPackage.getName());
      File[] logFiles = logPackage.listFiles();
      if (logFiles != null) {
        for (File logFile : logFiles) {
          String name = logFile.getName();

          if (Metadata.HTTP_RECEIVE.equals(name)) {
            log.setReceiveHttp(new HttpLog(Streams.toBytes(logFile)));

          } else if (Metadata.HTTP_SEND.equals(name)) {
            log.setSendHttp(new HttpLog(Streams.toBytes(logFile)));

          } else if (Metadata.METADATA.equals(name)) {
            ObjectMapper objectMapper = new ObjectMapper();
            Metadata metadata = objectMapper.readValue(logFile, Metadata.class);
            log.setClient(metadata.client);
            log.setError(metadata.error);
            log.setLogDate(metadata.date);
            log.setComponent(limitLength(metadata.componentName, 255));
            if (metadata.bid != null) {
              log.setBidId(metadata.bid);
            }
            if (metadata.send != null) {
              log.setSendPacket(getSoapPacket(metadata.send));
            }
            if (metadata.receive != null) {
              log.setReceivePacket(getSoapPacket(metadata.receive));
            }
            // в сущности нет processInstanceId
          }
          deleteFile(logFile);
        }
        if (log.getBidId() == null && !log.isClient()) {
          ExternalGlue glue = getExternalGlue(log);
          if (glue != null) {
            log.setBidId(glue.getId());
          }
        }
        if (isEmpty(log.getInfoSystem())) {
          String infoSystem = null;
          if (log.isClient()) {
            SoapPacket sendPacket = log.getSendPacket();
            if (sendPacket != null) {
              infoSystem = sendPacket.getRecipient();
            }
          } else {
            SoapPacket receivePacket = log.getReceivePacket();
            if (receivePacket != null) {
              infoSystem = receivePacket.getSender();
            }
          }
          log.setInfoSystem(infoSystem);
        }
        storage.store(log);
      }
      deleteFile(logPackage);
    } catch (Exception e) {
      logger.log(Level.WARNING, "failure", e);
      return false;
    }
    return true;
  }

  @TransactionAttribute(REQUIRES_NEW)
  public boolean logToZip() {
    Date edgeDate = calcEdge();

    Number count = em.createQuery("select count(o) from SmevLog o where o.logDate is null or o.logDate < :date", Number.class)
      .setParameter("date", edgeDate)
      .getSingleResult();

    if (count == null || count.intValue() == 0) {
      return false;
    }

    ZipOutputStream zip = null;
    try {
      zip = new ZipOutputStream(new FileOutputStream(createZipFileName(edgeDate)));
      for (int i = 0; i < 100; i++) {
        List<SmevLog> logs = em.createQuery("select o from SmevLog o where o.logDate is null or o.logDate < :date", SmevLog.class)
          .setParameter("date", edgeDate)
          .setMaxResults(1)
          .getResultList();
        if (logs.isEmpty()) {
          return false;
        }
        for (SmevLog log : logs) {
          final long packageTime = log.getLogDate() == null ? log.getDate().getTime() : log.getLogDate().getTime();
          final String packagePath;
          {
            final String logId;
            if (log.getMarker() == null || log.getMarker().length() < 2) {
              logId = String.valueOf(Math.round(Math.random() * 100000.0));
            } else {
              logId = log.getMarker();
            }
            int mlen = logId.length();
            String d1 = logId.substring(mlen - 2, mlen - 1);
            String d2 = logId.substring(mlen - 1);
            packagePath = d1 + "/" + d2 + "/" + logId + "/";
            ZipEntry logPackage = new ZipEntry(packagePath);
            logPackage.setTime(packageTime);
            zip.putNextEntry(logPackage);
          }

          if (log.getSendHttp() != null && log.getSendHttp().getData() != null) {
            ZipEntry httpSend = new ZipEntry(packagePath + Metadata.HTTP_SEND);
            httpSend.setTime(packageTime);
            zip.putNextEntry(httpSend);
            zip.write(log.getSendHttp().getData());
            zip.closeEntry();
          }

          if (log.getReceiveHttp() != null && log.getReceiveHttp().getData() != null) {
            ZipEntry httpReceive = new ZipEntry(packagePath + Metadata.HTTP_RECEIVE);
            httpReceive.setTime(packageTime);
            zip.putNextEntry(httpReceive);
            zip.write(log.getReceiveHttp().getData());
            zip.closeEntry();
          }

          ObjectMapper objectMapper = new ObjectMapper();
          Metadata metadata = new Metadata();
          metadata.client = log.isClient();
          metadata.error = log.getError();
          metadata.date = log.getLogDate();
          metadata.componentName = log.getComponent();
          Long bidId = log.getBidId();
          if (bidId != null) {
            metadata.bid = bidId;
            List<String> ids = em.createQuery("select b.processInstanceId from Bid b where b.id = :id", String.class)
              .setParameter("id", bidId).getResultList();
            if (!ids.isEmpty()) {
              metadata.processInstanceId = ids.get(0);
            }
          }
          metadata.send = getPack(log.getSendPacket());
          metadata.receive = getPack(log.getReceivePacket());
          ZipEntry logMetadata = new ZipEntry(packagePath + Metadata.METADATA);
          logMetadata.setTime(packageTime);
          zip.putNextEntry(logMetadata);
          zip.write(objectMapper.writeValueAsBytes(metadata));
          zip.closeEntry();

          em.remove(log);
          em.flush();
        }
      }
      zip.flush();
    } catch (IOException e) {
      logger.log(Level.WARNING, "io error", e);
    } finally {
      Streams.close(zip);
    }
    return true;
  }

  // ---- internals ----

  private SmevLog getOepLog(String marker) {
    SmevLog entry = storage.findLogEntry(marker);
    if (entry == null) {
      entry = new SmevLog();
      entry.setDate(new Date());
      entry.setMarker(marker);
    }
    return entry;
  }

  private String getDirPath() {
    if (dirPath == null) {
      dirPath = storage.getLazyDirPath();
    }
    return dirPath;
  }

  private ExternalGlue getExternalGlue(SmevLog log) {
    ExternalGlue externalGlue = getExternalGlue(log.getReceivePacket());
    if (externalGlue != null) {
      return externalGlue;
    }
    return getExternalGlue(log.getSendPacket());
  }

  private ExternalGlue getExternalGlue(SoapPacket soapPacket) {
    if (em == null || soapPacket == null) {
      return null;
    }
    String[] idRefs = {soapPacket.getOriginRequestIdRef(), soapPacket.getRequestIdRef()};
    for (String idRef : idRefs) {
      if (!isEmpty(idRef)) {
        List resultList = em.createQuery("select g from ExternalGlue g where g.requestIdRef = :requestIdRef")
          .setParameter("requestIdRef", idRef).getResultList();
        if (!resultList.isEmpty()) {
          return (ExternalGlue) resultList.get(0);
        }
      }
    }
    return null;
  }

  private SoapPacket getSoapPacket(Pack packet) {
    SoapPacket result = new SoapPacket();
    result.setSender(packet.sender);
    result.setRecipient(packet.recipient);
    result.setOriginator(packet.originator);
    result.setService(packet.serviceName);
    result.setTypeCode(packet.typeCode);
    result.setStatus(packet.status);
    result.setDate(packet.date);
    result.setRequestIdRef(packet.requestIdRef);
    result.setOriginRequestIdRef(packet.originRequestIdRef);
    result.setServiceCode(packet.serviceCode);
    result.setCaseNumber(packet.caseNumber);
    result.setExchangeType(packet.exchangeType);
    return result;
  }

  private Pack getPack(SoapPacket packet) {
    if (packet == null) {
      return null;
    }
    Pack result = new Pack();
    result.sender = packet.getSender();
    result.recipient = packet.getRecipient();
    result.originator = packet.getOriginator();
    result.serviceName = packet.getService();
    result.typeCode = packet.getTypeCode();
    result.status = packet.getStatus();
    result.date = packet.getDate();
    result.requestIdRef = packet.getRequestIdRef();
    result.originRequestIdRef = packet.getOriginRequestIdRef();
    result.serviceCode = packet.getServiceCode();
    result.caseNumber = packet.getCaseNumber();
    result.exchangeType = packet.getExchangeType();
    return result;
  }

  private String getZipPath() {
    String instanceRoot = System.getProperty("com.sun.aas.instanceRoot");
    File file;
    if (instanceRoot != null) {
      file = new File(new File(new File(instanceRoot), "logs"), "zip");
    } else {
      file = new File("/var/", "zip");
    }
    if (!file.exists()) {
      file.mkdirs();
    }
    return file.getAbsolutePath();
  }

  private boolean isEmpty(String str) {
    return str == null || str.length() == 0;
  }

  private File listLowDirs(File root) {
      File[] files = root.listFiles();
      if (files != null) {
        Arrays.sort(files);
        for (File file : files) {
          if (file.isDirectory()) {
            File file1 = listLowDirs(file);
            if (file1 != null) {
              return file1;
            }
          } else {
            return root;
          }
        }
      }
    return null;
    }

  private void setInfoSystem(SmevLog log, String sender) {
    if (isEmpty(log.getInfoSystem()) && !isEmpty(sender)) {
      log.setInfoSystem(sender);
    }
  }

  private void deleteFile(File file) {
    if (!file.delete()) {
      logger.info("can't delete " + file);
    }
  }


  private String limitLength(String src, int maxLen) {
    if (src == null || src.length() <= maxLen) {
      return src;
    }
    return src.substring(0, maxLen);
  }

  private Date calcEdge() {
    int depth = -1;
    String depthConfig = AdminServiceProvider.get().getSystemProperty(API.LOG_DEPTH);
    if (depthConfig != null) {
      try {
        depth = Integer.parseInt(depthConfig);
      } catch (NumberFormatException e) {
      }
    }
    if (depth <= 0) {
      depth = API.DEFAULT_LOG_DEPTH;
    }
    Calendar cal = Calendar.getInstance(getTimeZone());
    cal.add(Calendar.DATE, -depth);
    return cal.getTime();
  }

  private TimeZone getTimeZone() {
    return TimeZone.getTimeZone("Europe/Moscow");
  }

  private File createZipFileName(Date edge) {
    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
    dateTimeFormat.setTimeZone(getTimeZone());
    return new File(getZipPath(), dateTimeFormat.format(edge) + ".zip");
  }

}
