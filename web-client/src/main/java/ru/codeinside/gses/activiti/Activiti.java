/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import org.activiti.engine.impl.cmd.GetAttachmentCmd;
import org.activiti.engine.impl.cmd.GetAttachmentContentCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.variable.EntityManagerSession;
import org.activiti.engine.task.Attachment;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.io.IOUtils;
import ru.codeinside.adm.database.AuditValue;
import ru.codeinside.gses.activiti.history.HistoricDbSqlSession;
import ru.codeinside.gses.cert.X509;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.Signature;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * Вспомогательные методы для Actititi
 */
final public class Activiti {

  public static EntityManager getEm() {
    final CommandContext commandContext = Context.getCommandContext();
    if (commandContext == null) {
      throw new IllegalStateException("Требование EntityManager вне сессии Activiti!");
    }
    return commandContext.getSession(EntityManagerSession.class).getEntityManager();
  }

  public static Enclosure createEnclosureInCommandContext(String attId, String executionId, String variableName) {
    Attachment attachment = new GetAttachmentCmd(attId).execute(Context.getCommandContext());
    if (attachment != null) {
      byte[] content = contentByInputStream(new GetAttachmentContentCmd(attId).execute(Context.getCommandContext()));
      if (content != null) {
        String name = StringUtils.trimToNull(attachment.getName());
        if (name == null) {
          name = StringUtils.trimToNull(variableName);
          if (name == null) {
            name = attId;
          }
        }
        Enclosure enclosure = new Enclosure(name, name, content);
        enclosure.id = attachment.getId();
        enclosure.number = attachment.getId();
        enclosure.mimeType = attachment.getType();
        enclosure.digest = createDigest(content);
        enclosure.signature = createSignature(executionId, variableName, content);
        enclosure.code = variableName;
        return enclosure;
      }
    }
    return null;
  }

  public static byte[] createDigest(byte[] enclosureContent) {
    try {
      final MessageDigest gost3411 = MessageDigest.getInstance("GOST3411");
      gost3411.update(enclosureContent);
      return gost3411.digest();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    }
  }

  private static Signature createSignature(String executionId, String variableName, byte[] enclosureContent) {
    final CommandContext ctx = Context.getCommandContext();
    final HistoricDbSqlSession session = (HistoricDbSqlSession) ctx.getDbSqlSession();
    AuditValue auditValue = getAuditValueWithSign(executionId, variableName, session);
    if (auditValue == null) {
      return null;
    }
    final byte[] cert = auditValue.getCert();
    final byte[] sign = auditValue.getSign();
    X509Certificate x509 = X509.decode(cert);
    return new Signature(x509, enclosureContent, sign, true);
  }

  private static AuditValue getAuditValueWithSign(String executionId, String variableName, final HistoricDbSqlSession session) {
    AuditValue auditValue = session.getAuditSnapshotValue(Long.parseLong(executionId), variableName);
    if (hasAuditSign(auditValue)) {
      return auditValue;
    }
    auditValue = session.getTempAudit(Long.parseLong(executionId), variableName);
    return hasAuditSign(auditValue) ? auditValue : null;
  }

  private static boolean hasAuditSign(AuditValue auditValue) {
    return !(auditValue == null || auditValue.getSign() == null || auditValue.getCert() == null);
  }

  private static byte[] contentByInputStream(InputStream is) {
    try {
      return IOUtils.toByteArray(is);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getAttachmentName(Enclosure enclosure, String varName) {
    String name = StringUtils.trimToNull(enclosure.fileName);
    if (name == null) {
      name = getFileName(enclosure.zipPath);
      if (name == null) {
        name = varName;
      }
    }
    return name;
  }

  public static String getFileName(String pathName) {
    pathName = StringUtils.trimToNull(pathName);
    if (pathName != null) {
      int slash = pathName.lastIndexOf('/');
      if (slash >= 0) {
        pathName = StringUtils.trimToNull(pathName.substring(slash + 1));
      }
    }
    return pathName;
  }

}
