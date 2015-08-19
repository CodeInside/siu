/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.google.common.collect.Maps;
import com.vaadin.ui.Field;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.interceptor.CommandContext;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.activiti.forms.Signatures;
import ru.codeinside.gses.activiti.history.HistoricDbSqlSession;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.form.SignatureType;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FormSignatureFFT  {

  final private static Logger logger = Logger.getLogger(FormSignatureFFT.class.getName());

  final private static ThreadLocal<Map<Integer, Signatures>> THREAD_LOCAL_MAP = new ThreadLocal<Map<Integer, Signatures>>();

  //@Override
  public Field createField(String taskId, String fieldId, String name, String value, boolean writable, boolean required) {
    if (!writable) {
      return new ReadOnly("ЭЦП");
    }
    return new ReadOnly("Редактирование ЭЦП не поддерживается!", false);
  }

  /**
   * Вызывается как во время преобразования из базы, так и из UI.
   */
  //@Override
  public String convertModelValueToFormValue(Object modelValue) {
    final CommandContext ctx = Context.getCommandContext();
    if (ctx != null) {
      // контекст преобразования из базы
      THREAD_LOCAL_MAP.remove();
    } else {
      // контекст преобразования из UI
      if (modelValue instanceof Signatures) {
        final Signatures signatures = (Signatures) modelValue;
        Map<Integer, Signatures> map = getOrCreateForwardMap();
        int id = 1 + map.size();
        map.put(id, signatures);

        final FormID formID = signatures.formID;
        if (formID.taskId != null) {
          AdminServiceProvider.get().createLog(Flash.getActor(), "userTask", formID.taskId, "sign form", null, true);
        } else {
          AdminServiceProvider.get().createLog(Flash.getActor(), "startEvent", formID.processDefinitionId, "sign form", null,
            true);
        }

        return "" + id;
      }
    }
    // иначе никак не представляем в формате формы
    return null;
  }


  /**
   * Вызывается во время преобразования из формата формы в формат базы.
   * В контексте команды Activiti.
   */
  //@Override
  public Object convertFormValueToModelValue(final String propertyValue) {
    if (propertyValue != null) {
      try {
        int id = Integer.parseInt(propertyValue);
        final Signatures signatures = extractFromForwardMap(id);
        if (signatures != null) {
          final HistoricDbSqlSession session = (HistoricDbSqlSession) Context.getCommandContext().getSession(DbSqlSession.class);
          session.addSignatures(SignatureType.FIELDS, signatures);
        } else {
          logger.log(Level.SEVERE, "invalid forwardingId " + id);
        }
      } catch (Exception e) {
        logger.log(Level.SEVERE, "convert fail", e);
        throw new RuntimeException(e);
      }
    }
    return null;
  }


  // Временное решение для передачи подписи с формы в базу.
  // Желательно переработать Signatures в реальные сущности!

  private Map<Integer, Signatures> getOrCreateForwardMap() {
    Map<Integer, Signatures> map = THREAD_LOCAL_MAP.get();
    if (map == null) {
      map = Maps.newLinkedHashMap();
      THREAD_LOCAL_MAP.set(map);
    }
    return map;
  }

  private Signatures extractFromForwardMap(int id) {
    Signatures signatures = null;
    Map<Integer, Signatures> map = THREAD_LOCAL_MAP.get();
    if (map != null) {
      signatures = map.remove(id);
      if (map.isEmpty()) {
        THREAD_LOCAL_MAP.remove();
      }
    }
    return signatures;
  }
}
