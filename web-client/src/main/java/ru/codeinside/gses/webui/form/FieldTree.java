/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.google.common.base.Joiner;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.activiti.forms.api.definitions.BlockNode;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyCollection;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyTree;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyType;
import ru.codeinside.gses.activiti.forms.api.definitions.ToggleNode;
import ru.codeinside.gses.activiti.forms.api.values.Audit;
import ru.codeinside.gses.activiti.forms.api.values.BlockValue;
import ru.codeinside.gses.activiti.forms.api.values.FormValue;
import ru.codeinside.gses.activiti.forms.api.values.PropertyValue;
import ru.codeinside.gses.activiti.forms.types.FieldType;
import ru.codeinside.gses.activiti.forms.types.FieldTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

final public class FieldTree implements Serializable {

  final public Entry root;
  final FormID formID;
  PropertyTree propertyTree;
  boolean hasSignature;
  int maxDepth;

  public FieldTree(FormID formID) {
    this.formID = formID;
    Entry root = new Entry();
    root.type = Type.ROOT;
    this.root = root;
  }

  public void create(FormValue formValue) {
    propertyTree = formValue.getFormDefinition();
    hasSignature = new Builder(formID, formValue.getPropertyValues(), formValue.isArchiveMode()).createAll(root);
    maxDepth = getDeep(propertyTree);
  }

  public List<Entry> getEntries(String id) {
    List<Entry> list = new ArrayList<Entry>();
    root.getEntries(id, list);
    return list;
  }

  public int getCols() {
    return maxDepth + 1 + (hasSignature ? 1 : 0);
  }

  void updateColumnIndex() {
    root.updateIndex(new AtomicInteger(-1));
  }

  int getDeep(final PropertyCollection collection) {
    int deep = 0;
    for (final PropertyNode node : collection.getNodes()) {
      if (node.getPropertyType() == PropertyType.BLOCK) {
        deep = Math.max(deep, getDeep((BlockNode) node));
      }
    }
    return 1 + deep;
  }

  public String dumpTree() {
    StringBuilder sb = new StringBuilder();
    dumpTree(root, sb, 0);
    return sb.toString();
  }

  private void dumpTree(FieldTree.Entry entry, StringBuilder sb, int level) {
    for (int i = 0; i < level; i++) {
      sb.append(' ');
    }
    if (entry.node != null) {
      sb.append(entry.node.getId());
    } else {
      sb.append("-1");
    }
    sb.append(' ');
    sb.append(entry.type);
    sb.append(' ');
    sb.append(entry.index);
    if (entry.hidden) {
      sb.append(" hidden");
    }
    sb.append('\n');
    if (entry.items != null) {
      for (FieldTree.Entry child : entry.items) {
        dumpTree(child, sb, level + 1);
      }
    }
  }

  public void update(List<PropertyValue<?>> clones, Entry block, int i) {
    new Builder(formID, null, false).createClone(clones, block, i);
  }

  public List<FormField> getFormFields() {
    final List<FormField> list = new ArrayList<FormField>();
    root.fillFormFields(list);
    return list;
  }

  public void collect(Map<String, Object> values) {
    root.collect(values);
  }

  enum Type {
    ROOT, // корневой
    ITEM, // обычный
    BLOCK, // блок
    CLONE, // клон блока
    CONTROLS // управление блока
  }

  final public static class Entry implements Serializable, FormField {
    Type type; // тип
    PropertyNode node; // идентификатор шаблона свойства
    String path; // путь (можно расчитывать)
    int index; // индекс в сетке

    int cloneCount; // число клонированых
    int cloneIndex; // индекс клона
    int cloneMin;
    int cloneMax;

    VerticalLayout underline;// поле с подписью под полем
    Field field; // поле
    boolean readOnly;
    String caption;
    Component sign; // блок подписи

    Entry parent; // владелец
    List<Entry> items; // дочерние элементы
    boolean hidden;
    boolean readable; // давать ли просматривать значение поля
    List<ToggleNode> togglers;

    void add(Entry entry) {
      if (items == null) {
        items = new ArrayList<Entry>();
      }
      entry.parent = this;
      items.add(entry);
    }

    public String calcSuffix() {
      final LinkedList<String> suffix = new LinkedList<String>();
      Entry p = parent;
      while (p != null) {
        if (p.cloneIndex > 0) {
          suffix.addFirst(Integer.toString(p.cloneIndex));
        }
        p = p.parent;
      }
      if (suffix.isEmpty()) {
        return "";
      }
      return "_" + Joiner.on('_').join(suffix);
    }

    public int getDeepCount() {
      int size = 0;
      if (items != null) {
        size += items.size();
        for (Entry child : items) {
          size += child.getDeepCount();
        }
      }
      return size;
    }

    public int getControlsCount() {
      int size = isSyntetic() ? 0 : (hidden ? 0 : 1);
      if (!hidden && items != null) {
        for (Entry child : items) {
          size += child.getControlsCount();
        }
      }
      return size;
    }

    boolean isSyntetic() {
      return (type == Type.ROOT || type == Type.CLONE);
    }

    public void updateIndex(final AtomicInteger n) {
      if (type == Type.CLONE || hidden) {
        index = n.get();
      } else {
        index = n.getAndIncrement();
      }
      if (!hidden && items != null) {
        for (final Entry child : items) {
          child.updateIndex(n);
        }
      }
    }

    public Entry getEntry(final Field field) {
      if (this.field == field) {
        return this;
      }
      if (items != null) {
        for (Entry child : items) {
          Entry e = child.getEntry(field);
          if (e != null) {
            return e;
          }
        }
      }
      return null;
    }

    public int getLevel() {
      int level = 0;
      FieldTree.Entry p = parent;
      while (p != null) {
        if (!p.isSyntetic()) {
          level++;
        }
        p = p.parent;
      }
      return level;
    }

    public void removeFields(GridForm gridForm) {
      if (field != null) {
        gridForm.removeItemProperty(path);
      }
      if (items != null) {
        for (final Entry child : items) {
          child.removeFields(gridForm);
        }
      }
    }

    public void getEntries(String id, List<Entry> list) {
      if (node != null && id.equals(node.getId())) {
        list.add(this);
      }
      if (items != null) {
        for (final Entry child : items) {
          child.getEntries(id, list);
        }
      }
    }

    public String getCaption() {
      String result = caption;
      FieldTree.Entry p = parent;
      while (p != null) {
        if (p.cloneIndex > 0) {
          result = p.cloneIndex + ") " + result;
        } else {
          if (p.caption != null) {
            result = p.caption + " " + result;
          }
        }
        p = p.parent;
      }
      return result;
    }

    public void fillFormFields(final List<FormField> list) {
      if (field != null && !readOnly) {
        list.add(this);
      }
      if (items != null) {
        for (final Entry child : items) {
          child.fillFormFields(list);
        }
      }
    }

    @Override
    public String getPropId() {
      return path;
    }

    @Override
    public String getName() {
      return getCaption();
    }

    @Override
    public Object getValue() {
      if (hidden || field.getParent() == null) {
        return null;
      }
      return field.getValue();
    }

    void fillExtra(Field field, PropertyValue value, boolean writable) {
      final String nodeTip = value.getNode().getTip();
      final String nodeUnderline = value.getNode().getUnderline();
      this.field = field;
      if (StringUtils.isNotEmpty(nodeTip)) {
        field.setDescription(nodeTip);
      }
      if (StringUtils.isNotEmpty(nodeUnderline)) {
        final VerticalLayout layout = new VerticalLayout();
        layout.addComponent(field);
        final Label hint = new Label("<sub>" + nodeUnderline + "</sub>", Label.CONTENT_XHTML);
        if (StringUtils.isNotEmpty(nodeTip)) {
          hint.setDescription(nodeTip);
        }
        layout.addComponent(hint);
        underline = layout;
      }
      readOnly = !writable;
    }

    public void collect(Map<String, Object> values) {
      if (field != null && !readOnly && node.isFieldWritable()) {
        values.put(path, field.getValue());
      }
      if (items != null) {
        for (Entry child : items) {
          child.collect(values);
        }
      }
    }
  }

  static class Builder {
    final List<PropertyValue<?>> propertyValues;
    final FormID formID;
    private final boolean archiveMode;
    boolean hasAudit;


    public Builder(FormID formID, List<PropertyValue<?>> propertyValues, boolean archiveMode) {
      this.propertyValues = propertyValues;
      this.formID = formID;
      this.archiveMode = archiveMode;
    }

    boolean createAll(Entry root) {
      for (PropertyValue<?> propertyValue : propertyValues) {
        create(propertyValue, root);
      }
      return hasAudit;
    }

    void createClone(List<PropertyValue<?>> clones, Entry owner, int i) {
      Entry iClone = new Entry();
      iClone.type = Type.CLONE;
      iClone.cloneIndex = i;
      iClone.node = owner.node;
      owner.add(iClone);
      for (PropertyValue<?> child : clones) {
        create(child, iClone);
      }
    }

    Field createField(PropertyValue value) {
      FieldType fieldType = FieldTypes.getType(value.getNode().getVariableType());
      String name = value.getNode().getName();
      //TODO: передавать PropertyValue
      return fieldType.createField(formID.taskId, value.getId(), name != null ? name : value.getId(), value.getValue(), value.getNode(), archiveMode);
    }

    void create(PropertyValue propertyValue, Entry owner) {
      final PropertyType propertyType = propertyValue.getNode().getPropertyType();
      final Entry entry;
      final boolean writable = propertyValue.getNode().isFieldWritable() && !archiveMode;
      switch (propertyType) {
        case TOGGLE:
        case VISIBILITY_TOGGLE:
          break;
        case ITEM:
          entry = createEntry(owner, propertyValue);
          entry.type = Type.ITEM;
          entry.fillExtra(createField(propertyValue), propertyValue, writable);
          break;
        case ENCLOSURE:
          entry = createEntry(owner, propertyValue);
          entry.fillExtra(createField(propertyValue), propertyValue, writable);
          entry.type = Type.ITEM;
          entry.readable = false;
          break;
        case BLOCK:
          entry = createEntry(owner, propertyValue);
          entry.type = Type.BLOCK;
          BlockValue blockValue = (BlockValue) propertyValue;
          Long value = blockValue.getValue();
          entry.fillExtra(new BlockField(formID.taskId, blockValue.getId(), value, entry.readOnly), propertyValue, writable);
          if (value == null) {
            entry.cloneCount = 0;
          } else {
            entry.cloneCount = value.intValue();
          }
          BlockNode block = (BlockNode) blockValue.getNode();
          entry.cloneMin = block.getMinimum();
          entry.cloneMax = block.getMaximum();
          int i = 1;
          for (List<PropertyValue<?>> childBlock : blockValue.getClones()) {
            createClone(childBlock, entry, i++);
          }
          if (writable && entry.cloneMax > entry.cloneMin) {
            Entry controls = new Entry();
            controls.type = Type.CONTROLS;
            owner.add(controls);
          }
          break;
        default:
          throw new IllegalArgumentException("Встретился неподдерживаем PropertyType " + propertyType);
      }
    }

    private Entry createEntry(Entry owner, PropertyValue propertyValue) {
      Entry entry = new Entry();
      owner.add(entry);
      entry.node = propertyValue.getNode();
      entry.path = propertyValue.getId();
      entry.caption = propertyValue.getNode().getName();
      if (entry.caption == null) {
        entry.caption = propertyValue.getId();
      }
      entry.sign = createSignInfo(propertyValue);
      entry.readable = true;
      return entry;
    }

    private Component createSignInfo(PropertyValue p) {
      Audit audit = p.getAudit();
      if (audit != null) {
        String author;
        {
          String owner = audit.getOwner();
          String organization = audit.getOrganization();
          if (owner == null && organization == null) {
            author = null;
          } else {
            author = "" + owner + " (" + organization + ")";
          }
        }
        if (author == null) {
          String login = audit.getLogin();
          if (login != null) {
            Employee employee = AdminServiceProvider.get().findEmployeeByLogin(login);
            author = employee != null ? employee.getFio() : ("'" + login + "'");
          }
        }
        if (author == null) {
          return null;
        }
        hasAudit = true;
        Label info = new Label(author);
        if (audit.isVerified()) {
          info.setCaption("Подписано:");
        }
        info.setStyleName("left");
        info.addStyleName(Reindeer.LABEL_SMALL);
        info.setWidth(150, Sizeable.UNITS_PIXELS);
        info.setHeight(100, Sizeable.UNITS_PERCENTAGE);
        return info;
      }
      return null;
    }

  }

}
