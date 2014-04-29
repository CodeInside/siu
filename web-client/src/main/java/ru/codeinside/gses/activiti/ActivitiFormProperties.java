/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.form.FormType;
import org.activiti.engine.task.Attachment;
import ru.codeinside.gses.activiti.forms.BlockNode;
import ru.codeinside.gses.activiti.forms.PropertyNode;
import ru.codeinside.gses.activiti.forms.PropertyType;
import ru.codeinside.gses.activiti.forms.TypeTree;
import ru.codeinside.gses.activiti.ftarchive.AttachmentFFT;
import ru.codeinside.gses.activiti.ftarchive.StringFFT;
import ru.codeinside.gses.vaadin.FieldFormType;
import ru.codeinside.gses.webui.form.GridForm;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ActivitiFormProperties {

  final public Map<String, FileValue> files;
  public Map<String, String> formPropertyValues;

  private ActivitiFormProperties(Map<String, String> formPropertyValues, Map<String, FileValue> files) {
    this.formPropertyValues = formPropertyValues;
    this.files = files;
  }

  public static ActivitiFormProperties empty() {
    return new ActivitiFormProperties(new LinkedHashMap<String, String>(), new LinkedHashMap<String, FileValue>());
  }

  public static ActivitiFormProperties createWithFiles(Map<String, String> formPropertyValues,
                                                       Map<String, FileValue> files) {
    return new ActivitiFormProperties(formPropertyValues, ImmutableMap.copyOf(files));
  }

  private static FieldFormType getFieldFormType(FormType formType) {
    if (formType == null) {
      return new StringFFT();
    }
    if (!(formType instanceof DelegateFormType)) {
      throw new IllegalStateException("Unknown type " + formType.getClass());
    }
    final DelegateFormType delegateFormType = (DelegateFormType) formType;
    return delegateFormType.getType();
  }

  private static Pair<Field, Form> getFieldById(List<Form> forms, String id) {
    for (final Form form : forms) {
      final Field field = form.getField(id);
      if (field != null) {
        return new Pair<Field, Form>(field, form);
      }
    }
    throw new IllegalStateException("Не найдено поле " + id);
  }

  public void createAttachments(final ProcessEngine engine, final String taskId, final String processId) {
    for (final String propertyId : formPropertyValues.keySet()) {
      if (files.containsKey(propertyId)) {
        final FileValue fileValue = files.get(propertyId);
        final Attachment attachment;
        if (fileValue instanceof AttachmentFileValue) {
          // повторное использование вложения:
          attachment = ((AttachmentFileValue) fileValue).getAttachment();
        } else {
          attachment = engine.getTaskService().createAttachment(fileValue.getMimeType(), taskId,
            processId, fileValue.getFileName(), null, new ByteArrayInputStream(fileValue.getContent()));
        }
        formPropertyValues.put(propertyId, AttachmentFFT.stringValue(attachment));
      }
    }
  }

  public Map<String, FileValue> getFiles() {
    return files;
  }

  public static ActivitiFormProperties createForTypeTree(TypeTree typeTree, List<Form> forms) {
    return new Builder(typeTree, forms).build();
  }

  static class Builder {
    final Map<String, String> formPropertyValues = Maps.newHashMap();
    final Map<String, FileValue> files = Maps.newHashMap();
    final TypeTree typeTree;
    final List<Form> forms;

    Builder(TypeTree typeTree, List<Form> forms) {
      this.typeTree = typeTree;
      this.forms = forms;
    }

    ActivitiFormProperties build() {
      for (PropertyNode node : typeTree.tree.getNodes()) {
        build(node, "");
      }
      return new ActivitiFormProperties(formPropertyValues, ImmutableMap.copyOf(files));
    }

    /**
     * Строим список полей для submit в форму
     *
     * @param node   свойтво формы
     * @param suffix суффикс названия формы, в случае если он создан динамически
     */
    void build(PropertyNode node, String suffix) {
      final PropertyType type = node.getPropertyType();
      if (Arrays.asList(PropertyType.TOGGLE, PropertyType.VISIBILITY_TOGGLE, PropertyType.ENCLOSURE).contains(type)) {
        return;
      }
      boolean writable = typeTree.writeable.contains(node.getId());
      if (type != PropertyType.BLOCK && !writable) {
        return;
      }
      final String propertyId = node.getId() + suffix;
      Pair<Field, Form> fieldById = getFieldById(forms, propertyId);
      final Field field = fieldById.get_1();

      // Только записываемые значения!
      if (writable && !field.isReadOnly() && formIsParent(fieldById.get_2(), field)) {
        // должны поддержать требование NULL для невидимых/отсоединённых
        addNodeToFormPropertyValue(node, propertyId, field);
      }
      if (type == PropertyType.BLOCK) {
        if (field.getValue() != null) {
          final int items = toInt(field);
          final BlockNode block = (BlockNode) node;
          for (int i = 1; i <= items; i++) {
            for (final PropertyNode child : block.getNodes()) {
              build(child, suffix + "_" + i);
            }
          }
        }
      }
    }

    private boolean formIsParent(Form form, Component com) {
      if (form instanceof GridForm) {
        while (com != null) {
          if (com == form.getLayout()) {
            return true;
          }
          com = com.getParent();
        }
      } else {
        return true;
      }
      return false;
    }

    private void addNodeToFormPropertyValue(PropertyNode node, String propertyId, Field field) {
      if (isFormless(field)) {
        formPropertyValues.put(propertyId, null);
      } else {
        final FormType formType = typeTree.types.get(node.getId());
        if (isAttachment(formType)) {  // attachment нельзя клонировать?
          if (FileValue.class.equals(field.getType())) {
            final FileValue value = (FileValue) field.getValue();
            if (value != null) {
              files.put(propertyId, value);
              formPropertyValues.put(propertyId, "вложение");
            }
          }
        } else {
          Object modelValue = field.getValue();
          final String stringValue = getFieldFormType(formType).convertModelValueToFormValue(modelValue);
          if (stringValue != null || !field.isRequired()) {
            // разрешим null для опциональных полей
            formPropertyValues.put(propertyId, stringValue);
          }
        }
      }
    }
  }

  private static boolean isAttachment(FormType formType) {
    return formType != null
      && AttachmentFFT.TYPE_NAME.equals(formType.getName());
  }

  private static boolean isFormless(Component component) {
    Component parent = component.getParent();
    while (parent != null) {
      if (parent instanceof Form) {
        return false;
      }
      component = parent;
      parent = component.getParent();
    }
    return false;
  }

  private static int toInt(Field field) {
    final Object value = field.getValue();
    if (value instanceof Integer) {
      return (Integer) value;
    }
    return Integer.parseInt(value.toString());
  }

}
