/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2015, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.gses.webui.form;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.variable.EntityManagerSession;
import org.apache.commons.codec.binary.Base64;
import org.osgi.framework.ServiceReference;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.adm.database.ExportFormEntity;
import ru.codeinside.gses.activiti.FileValue;
import ru.codeinside.gses.activiti.VariableToBytes;
import ru.codeinside.gses.activiti.forms.Signatures;
import ru.codeinside.gses.cert.X509;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.osgi.Activator;
import ru.codeinside.gws.api.CryptoProvider;
import ru.codeinside.gws.api.Signature;

import javax.persistence.EntityManager;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExportJson {
  public static final String EXPORT_JSON_PROPERTY_ID = "EXPORT_JSON_ID";

  private JsonObject json;
  private Date date;

  public ExportJson() {
    json = new JsonObject();
    date = new Date();

    json.addProperty("date", new SimpleDateFormat("dd.MM.yyyy hh:mm:ss").format(date));
  }

  public void addField(FormField formField) {
    Object value = formField.getValue();
    String strValue;
    if (value instanceof FileValue) {
      byte[] content = VariableToBytes.toBytes(value);
      strValue = Base64.encodeBase64String(content);
    } else {
      strValue = String.valueOf(value);
    }
    json.addProperty(formField.getName(), strValue);
  }

  public byte[] toBytes() {
    Gson gson = new Gson();
    return gson.toJson(json).getBytes(Charset.forName("UTF-8"));
  }

  public void export(CommandContext commandContext, Signatures signatures, String taskId, String processInstanceId) {
    int sign = signatures.findSign(ExportJson.EXPORT_JSON_PROPERTY_ID);
    Signature exportToPkcs = new Signature(X509.decode(signatures.certificate), toBytes(), signatures.signs[sign], true);

    ServiceReference serviceReference = Activator.getContext().getServiceReference(CryptoProvider.class.getName());
    byte[] pkcs7;
    try {
      CryptoProvider cryptoProvider = (CryptoProvider) Activator.getContext().getService(serviceReference);
      if (cryptoProvider == null) {
        throw new IllegalStateException("Сервис криптографии не доступен.");
      }
      pkcs7 = cryptoProvider.toPkcs7(exportToPkcs);
    } finally {
      Activator.getContext().ungetService(serviceReference);
    }

    EntityManager em = commandContext.getSession(EntityManagerSession.class).getEntityManager();
    Employee employee = em.find(Employee.class, Flash.flash().getLogin());

    String task = "Начало";
    if (taskId != null) {
      TaskEntity taskEntity = commandContext.getTaskManager().findTaskById(taskId);
      task = taskEntity.getName();
    }

    Bid bid = em.createQuery("select b from Bid b where b.processInstanceId = :processInstanceId", Bid.class)
        .setParameter("processInstanceId", processInstanceId).getResultList().get(0);

    ExportFormEntity entity = new ExportFormEntity();
    entity.setDate(date);
    entity.setJson(exportToPkcs.content);
    entity.setPkcs7(pkcs7);
    entity.setProcedure(bid.getProcedure().getName());
    entity.setTask(task);
    entity.setEmployee(employee);
    entity.setBid(bid);
    em.persist(entity);
    em.flush();
  }
}
