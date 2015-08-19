/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.types;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.Attachment;
import ru.codeinside.adm.database.BytesBuffer;
import ru.codeinside.adm.database.FieldBuffer;
import ru.codeinside.gses.activiti.FileValue;
import ru.codeinside.gses.activiti.forms.FileBufferValue;
import ru.codeinside.gses.activiti.forms.api.definitions.VariableType;
import ru.codeinside.gses.activiti.ftarchive.AttachmentFileValue;
import ru.codeinside.gses.service.Functions;
import ru.codeinside.gses.service.PF;
import ru.codeinside.gses.webui.form.TmpAttachment;

import java.util.Map;

public class AttachmentType implements VariableType<FileValue> {

  final public static String SUFFIX = ":attachment";

  @Override
  public FileValue convertFormValueToModelValue(Object propertyValue, String pattern, Map<String, String> values) {
    if (propertyValue == null) {
      return null;
    }
    if (propertyValue instanceof FileValue) {
      return (FileValue) propertyValue;
    }
    if (propertyValue instanceof TmpAttachment) {
      return new AttachmentFileValue((TmpAttachment) propertyValue);
    }
    String value = propertyValue.toString();

    if (value.length() <= SUFFIX.length() || !value.endsWith(SUFFIX)) {
      return null;
    }
    final String id = value.substring(0, value.length() - SUFFIX.length());
    Attachment attachment = Functions.withEngine(new PF<Attachment>() {
      public Attachment apply(ProcessEngine engine) {
        return engine.getTaskService().getAttachment(id);
      }
    });
    if (attachment == null) {
      return null;
    }
    return new AttachmentFileValue(attachment);
  }

  @Override
  public void validateParams(String pattern, Map<String, String> values, boolean sandbox) {
    if (pattern != null) {
      throw VariableTypes.badPattern(GsesTypes.ATTACHMENT);
    }
    if (values != null) {
      throw VariableTypes.badValues(GsesTypes.ATTACHMENT);
    }
  }

  @Override
  public Class<FileValue> getJavaType() {
    return FileValue.class;
  }

  @Override
  public FileValue convertBufferToModelValue(FieldBuffer fieldBuffer) {
    BytesBuffer bytesValue = fieldBuffer.getBytesValue();
    return new FileBufferValue(
      fieldBuffer.getTextValue(), fieldBuffer.getMime(), bytesValue == null ? null : bytesValue.getId()
    );
  }

  @Override
  public String getName() {
    return "attachment";
  }
}
