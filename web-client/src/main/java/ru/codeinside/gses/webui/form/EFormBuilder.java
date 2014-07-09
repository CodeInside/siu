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
import ru.codeinside.gses.activiti.FileValue;
import ru.codeinside.gses.activiti.FormDecorator;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.activiti.forms.api.definitions.BlockNode;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyCollection;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyTree;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyType;
import ru.codeinside.gses.activiti.forms.api.definitions.VariableType;
import ru.codeinside.gses.activiti.forms.api.values.FormValue;
import ru.codeinside.gses.activiti.forms.api.values.PropertyValue;
import ru.codeinside.gses.activiti.forms.values.Block;
import ru.codeinside.gses.activiti.ftarchive.AttachmentFFT;
import ru.codeinside.gses.activiti.history.VariableSnapshot;
import ru.codeinside.gses.service.ActivitiService;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.service.Functions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

final public class EFormBuilder implements FormSeq {

  String templateRef;
  Map<String, String> attachmentsIds;
  eform.Form form;
  EForm eForm;
  FormValue formValue;


  public EFormBuilder(FormValue formValue) {
    this(formValue, false);
  }

  public EFormBuilder(FormValue formValue, boolean archiveMode) {
    templateRef = formValue.getFormDefinition().getFormKey();
    attachmentsIds = new HashMap<String, String>();
    form = createExternalForm(formValue);
    form.archiveMode = archiveMode;
    this.formValue = formValue;
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
        Fn.withTask(new Function<TaskService, Void>() {
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
                    Property property = form.getProperty(e.getKey());
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
      eForm = new EForm(templateRef, form, formValue);
      templateRef = null;
      attachmentsIds = null;
      form = null;
      formValue = null;
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

  private eform.Form createExternalForm(final FormValue formValue) {
    final PropertyTree propertyTree = formValue.getFormDefinition();
    final eform.Form form = new eform.Form() {
      @Override
      public Map<String, Property> plusBlock(String login, String name, String suffix, Integer newVal) {
        BlockNode cloneNode = ((BlockNode) propertyTree.getIndex().get(name));
        List<PropertyValue<?>> clones = ActivitiService.INSTANCE.get()
          .withEngine(new Fetcher(login), FormID.byProcessDefinitionId(formValue.getProcessDefinition().getId()), cloneNode, suffix + "_" + newVal);

        Map<String, Property> map = new LinkedHashMap<String, Property>();
        for (PropertyValue<?> propertyValue : clones) {
          Property property = propertyToTree(propertyValue, suffix + "_" + newVal, eForm.fields);
          if (property != null) {
            map.put(propertyValue.getNode().getId() + suffix + "_" + newVal, property);
          }
        }
        Property cloneProperty = this.getProperty(name + suffix);
        cloneProperty.updateValue(newVal.toString());
        if (cloneProperty.children == null) {
          cloneProperty.children = new ArrayList<Map<String, Property>>();
        }
        cloneProperty.children.add(map);
        return map;
      }

      @Override
      public void minusBlock(String name, String suffix, Integer newVal) {
        Property cloneProperty = this.getProperty(name + suffix);
        cloneProperty.updateValue(newVal.toString());
        if (cloneProperty.children != null) {
          Map<String, Property> map = cloneProperty.children.remove(newVal.intValue());
          for (String key : propertyKeySet(map)) {
            eForm.fields.remove(key);
          }
        }
      }
    };
		for (PropertyValue propertyValue : formValue.getPropertyValues()) {
			Property property = propertyToTree(propertyValue, "", null);
			if (property != null) {
				form.props.put(propertyValue.getNode().getId(), property);
			}
		}
    return form;
  }

  Property propertyToTree(PropertyValue<?> propertyValue,
                          String suffix, Map<String, EField> fields) {

    if (propertyValue == null) {
      return null;
    }
    Property property = createProperty(propertyValue, suffix);
    if (AttachmentFFT.isAttachment(propertyValue)) {
      String value = propertyValue.getValue() == null ? null : propertyValue.getValue().toString();
      String attachmentId = null;
      if (value != null) {
        attachmentId = AttachmentFFT.getAttachmentIdByValue(value);
        if (attachmentId == null) {
          Logger
            .getLogger(EFormBuilder.class.getName())
            .warning("In form '" + FormID.byProcessDefinitionId(formValue.getProcessDefinition().getId()) +
              "' property '" + propertyValue.getId() +
              "' with value '" + value +
              "' does not contains attachment reference!");
        }
      }
      if (attachmentId != null) {
        attachmentsIds.put(propertyValue.getId(), attachmentId);
      }
    }
    if (fields != null) {
      fields.put(propertyValue.getId(), new EField(propertyValue.getId(), property, propertyValue.getNode().getVariableType()));
    }
    if (propertyValue instanceof Block) {
      final List<List<PropertyValue<?>>> clones = ((Block) propertyValue).getClones();
      int value;
      try {
        value = Integer.parseInt(property.value);
      } catch (NumberFormatException e) {
        value = 0;
      }
      for (int i = 1; i <= value; i++) {
        Map<String, Property> map = new LinkedHashMap<String, Property>();
        for (PropertyValue<?> childValue : clones.get(i-1)) {
          Property child = propertyToTree(childValue, suffix + "_" + i, fields);
          if (child != null) {
            map.put(childValue.getNode().getId() + suffix + "_" + i, child);
          }
        }
        if (!map.isEmpty()) {
          if (property.children == null) {
            property.children = new ArrayList<Map<String, Property>>();
          }
          property.children.add(map);
        }
      }
    }
    return property;
  }

  public Property createProperty(PropertyValue<?> propertyValue, String suffix) {
    String prefix;
    if (suffix.isEmpty()) {
      prefix = suffix;
    } else {
      prefix = suffix.substring(1).replace('_', '.') + ") ";
    }
    Property property = new Property();
    property.label = prefix + propertyValue.getNode().getName();
    VariableType type = propertyValue.getNode().getVariableType();
    property.type = type == null ? "string" : type.getName();
    property.required = propertyValue.getNode().isFiledRequired();
    property.writable = propertyValue.getNode().isVarWritable();
    if (propertyValue.getAudit() != null) {
        property.sign = propertyValue.getAudit().isVerified();
        if (propertyValue.getAudit().isVerified()) {
          property.certificate = propertyValue.getAudit().getLogin() + "(" + propertyValue.getAudit().getOrganization() + ")";
        }
    }
    if (!(propertyValue.getValue() instanceof FileValue)) {
      Object value = propertyValue.getValue();
      if (value instanceof byte[]) {
        try {
          property.value = new String((byte[])value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
          Logger.getAnonymousLogger().info("can't decode model!");
        }
      } else {
        property.value = value == null ? null : value.toString();
      }
    }
    return property;
  }

}
