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
import ru.codeinside.gses.activiti.forms.BlockNode;
import ru.codeinside.gses.activiti.forms.PropertyCollection;
import ru.codeinside.gses.activiti.forms.PropertyNode;
import ru.codeinside.gses.activiti.forms.PropertyTree;
import ru.codeinside.gses.activiti.forms.PropertyType;
import ru.codeinside.gses.activiti.ftarchive.AttachmentFFT;
import ru.codeinside.gses.activiti.history.VariableSnapshot;
import ru.codeinside.gses.service.ActivitiService;
import ru.codeinside.gses.service.Functions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
  Map<String, FormType> types;


  public EFormBuilder(FormDecorator decorator) {
    this(decorator, false);
  }

  public EFormBuilder(FormDecorator decorator, boolean archiveMode) {
    templateRef = decorator.variableFormData.formData.getFormKey();
    attachmentsIds = new HashMap<String, String>();
    form = createExternalForm(decorator);
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

  private eform.Form createExternalForm(final FormDecorator decorator) {
    final PropertyTree propertyTree = decorator.variableFormData.nodeMap;
    final FormDecorator simple = decorator.toSimple();
    final eform.Form form = new eform.Form() {
      @Override
      public Map<String, Property> plusBlock(String login, String name, String suffix, Integer newVal) {
        FormPropertyClones clones = ActivitiService.INSTANCE.get().withEngine(new Fetcher(login), simple.id, name, suffix + "_" + newVal);
        BlockNode cloneNode = (BlockNode) findInTree(propertyTree, name);
        Map<String, Property> map = new LinkedHashMap<String, Property>();
        for (PropertyNode propertyNode : cloneNode.getNodes()) {
          Property property = propertyToTree(propertyNode, decorator, clones.properties, suffix + "_" + newVal, eForm.fields);
          if (property != null) {
            map.put(propertyNode.getId() + suffix + "_" + newVal, property);
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
    Collection<FormProperty> values = decorator.getGeneral().values();
    for (PropertyNode propertyNode : propertyTree.getNodes()) {
      Property property = propertyToTree(propertyNode, decorator, values, "", null);
      if (property != null) {
        form.props.put(propertyNode.getId(), property);
      }
    }
    return form;
  }

  Property propertyToTree(PropertyNode propertyNode, FormDecorator decorator, Collection<FormProperty> values,
                          String suffix, Map<String, EField> fields) {

    FormProperty formProperty = getByPath(propertyNode.getId() + suffix, values);
    if (formProperty == null) {
      return null;
    }
    Property property = createProperty(formProperty, decorator, suffix);
    if (AttachmentFFT.isAttachment(formProperty)) {
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
    if (fields != null) {
      fields.put(formProperty.getId(), new EField(formProperty.getId(), property, formProperty.getType()));
    }
    if (PropertyType.BLOCK.equals(propertyNode.getPropertyType())) {
      final BlockNode block = (BlockNode) propertyNode;
      int value;
      try {
        value = Integer.parseInt(property.value);
      } catch (NumberFormatException e) {
        value = 0;
      }
      for (int i = 1; i <= value; i++) {
        Map<String, Property> map = new LinkedHashMap<String, Property>();
        for (PropertyNode node : block.getNodes()) {
          Property child = propertyToTree(node, decorator, values, suffix + "_" + i, fields);
          if (child != null) {
            map.put(node.getId() + suffix + "_" + i, child);
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

  FormProperty getByPath(String path, Collection<FormProperty> values) {
    for (FormProperty property : values) {
      if (path.equals(property.getId())) {
        return property;
      }
    }
    return null;
  }

  PropertyNode findInTree(PropertyCollection propertyTree, String name) {
    for (PropertyNode propertyNode : propertyTree.getNodes()) {
      if (propertyNode.getId().equals(name)) {
        return propertyNode;
      }
      if (PropertyType.BLOCK.equals(propertyNode.getPropertyType())) {
        PropertyNode inTree = findInTree((BlockNode) propertyNode, name);
        if (inTree != null) {
          return inTree;
        }
      }
    }
    return null;
  }

  public Property createProperty(FormProperty formProperty, FormDecorator decorator, String suffix) {
    String prefix;
    if (suffix.isEmpty()) {
      prefix = suffix;
    } else {
      prefix = suffix.substring(1).replace('_', '.') + ") ";
    }
    Property property = new Property();
    property.label = prefix + formProperty.getName();
    FormType type = formProperty.getType();
    property.type = type == null ? "string" : type.getName();
    property.required = formProperty.isRequired();
    property.writable = formProperty.isWritable();
    if (decorator.variableFormData != null) {
      VariableSnapshot snapshot = decorator.variableFormData.variables.get(formProperty.getId());
      if (snapshot != null) {
        property.sign = snapshot.verified;
        if (snapshot.verified) {
          property.certificate = snapshot.certOwnerName + "(" + snapshot.certOwnerOrgName + ")";
        }
      }
    }
    if (!AttachmentFFT.isAttachment(formProperty)) {
      property.value = formProperty.getValue();
    }
    return property;
  }

}
