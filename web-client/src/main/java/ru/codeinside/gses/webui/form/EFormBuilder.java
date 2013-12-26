/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.google.common.base.Function;
import com.vaadin.ui.Form;
import commons.Streams;
import eform.Property;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;
import org.activiti.engine.task.Attachment;
import ru.codeinside.gses.activiti.FormDecorator;
import ru.codeinside.gses.activiti.FormID;
import ru.codeinside.gses.activiti.ftarchive.AttachmentFFT;
import ru.codeinside.gses.activiti.history.VariableSnapshot;
import ru.codeinside.gses.service.Functions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

final public class EFormBuilder implements FormSeq {

  String templateRef;
  Map<String, String> attachmentsIds;
  eform.Form form;
  EForm eForm;
  Map<String, FormType> types;


  public EFormBuilder(FormDecorator decorator) {
    this(decorator, false);
  }

  public EFormBuilder(FormDecorator decorator, boolean archiveMode) {
    templateRef = decorator.variableFormData.formData.getFormKey();
    attachmentsIds = new HashMap<String, String>();
    form = createExternalForm(decorator, attachmentsIds);
    form.archiveMode = archiveMode;
    types = new HashMap<String, FormType>();
    for (FormProperty p : decorator.getGeneral().values()) {
      types.put(p.getId(), p.getType());
    }
  }


  @Override
  public String getCaption() {
    return "";
  }

  @Override
  public List<FormField> getFormFields() {
    return eForm.getFormFields();
  }

  @Override
  public Form getForm(FormID formId, FormSeq previous) {
    if (eForm == null) {
      if (hasAttachments()) {
        Functions.withTask(new Function<TaskService, Void>() {
          @Override
          public Void apply(TaskService taskService) {
            for (Map.Entry<String, String> e : attachmentsIds.entrySet()) {
              String attachmentId = e.getValue();
              if (!attachmentId.isEmpty()) {
                Attachment attachment = taskService.getAttachment(attachmentId);
                if (attachment != null) {
                  try {
                    InputStream content = taskService.getAttachmentContent(attachmentId);
                    File file = Streams.copyToTempFile(content, "efrom-", ".attachment");
                    Property property = form.props.get(e.getKey());
                    property.updateContent(attachment.getName(), attachment.getType(), file, false);
                  } catch (IOException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.WARNING, "can't create tmpFile", ex);
                  }
                }
              }
            }
            return null;
          }
        });
      }
      eForm = new EForm(templateRef, form, types);
      templateRef = null;
      attachmentsIds = null;
      form = null;
      types = null;
    }
    return eForm;
  }

  private boolean hasAttachments() {
    for (String id : attachmentsIds.values()) {
      if (!id.isEmpty()) {
        return true;
      }
    }
    return false;
  }

  private static eform.Form createExternalForm(FormDecorator decorator, Map<String, String> attachmentsIds) {
    eform.Form form = new eform.Form();
    for (FormProperty formProperty : decorator.getGeneral().values()) {
      Property property = new Property();
      property.label = formProperty.getName();
      FormType type = formProperty.getType();
      property.type = type == null ? "string" : type.getName();
      property.required = formProperty.isRequired();
      property.writable = formProperty.isWritable();
      VariableSnapshot snapshot = decorator.variableFormData.variables.get(formProperty.getId());
      if (snapshot != null) {
        property.sign = snapshot.verified;
        if (snapshot.verified) {
          property.certificate = snapshot.certOwnerName + "(" + snapshot.certOwnerOrgName + ")";
        }
      }
      if (!AttachmentFFT.isAttachment(formProperty)) {
        property.value = formProperty.getValue();
      } else {
        String value = formProperty.getValue();
        String attachmentId = null;
        if (value != null) {
          attachmentId = AttachmentFFT.getAttachmentIdByValue(value);
          if (attachmentId == null) {
            Logger
              .getLogger(EFormBuilder.class.getName())
              .warning("In form '" + decorator.id +
                "' property '" + formProperty.getId() +
                "' with value '" + value +
                "' does not contains attachment reference!");
          }
        }
        if (attachmentId != null) {
          attachmentsIds.put(formProperty.getId(), attachmentId);
        }
      }
      form.props.put(formProperty.getId(), property);
    }
    return form;
  }

}
