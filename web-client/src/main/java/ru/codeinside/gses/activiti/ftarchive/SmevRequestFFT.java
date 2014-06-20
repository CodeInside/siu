/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.ui.Field;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import ru.codeinside.adm.AdminService;
import ru.codeinside.adm.database.ClientRequestEntity;
import ru.codeinside.adm.database.InfoSystemService;
import ru.codeinside.gses.activiti.Activiti;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.SmevRequestField;
import ru.codeinside.gses.activiti.SmevRequestValue;
import ru.codeinside.gses.activiti.forms.FieldConstructor;
import ru.codeinside.gses.webui.Flash;

import java.util.List;

public class SmevRequestFFT implements FieldConstructor {

  private static final long serialVersionUID = 1L;

  @Override
  public Field createField(String taskId, String fieldId, String name, String value, boolean writable, boolean required) {
    if (value == null) {
      return new ReadOnly("Ошибочный запрос в СМЭВ", false);
    }
    // TODO: преобразовать в атомарную операцию
    final AdminService adminService = Flash.flash().getAdminService();
    long id = Long.parseLong(value);
    final ClientRequestEntity entity = adminService.getClientRequestEntity(id);
    final List<InfoSystemService> services = adminService.getInfoSystemServiceBySName(entity.name);
    final String label = services.get(0).getName();
    return new SmevRequestField(label, id);
  }

  @Override
  public String convertModelValueToFormValue(Object modelValue) {
    final CommandContext ctx = Context.getCommandContext();
    if (ctx != null) {
      // Activiti
      if (modelValue instanceof ClientRequestEntity) {
        return ((ClientRequestEntity) modelValue).getId().toString();
      }
    } else {
      // UI
      if (modelValue instanceof SmevRequestValue) {
        return Long.toString(((SmevRequestValue) modelValue).id);
      }
    }
    return null;
  }

  @Override
  public Object convertFormValueToModelValue(String propertyValue) {
    final CommandContext ctx = Context.getCommandContext();
    final long id = Long.parseLong(propertyValue);
    if (ctx != null) {
      // Activiti
      return Activiti.getEm().getReference(ClientRequestEntity.class, id);
    } else {
      throw new UnsupportedOperationException();
    }

  }
}
