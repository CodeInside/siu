/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.google.common.base.Joiner;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.activiti.engine.form.FormProperty;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.gses.activiti.FormDecorator;
import ru.codeinside.gses.activiti.forms.BlockNode;
import ru.codeinside.gses.activiti.forms.EnclosureItem;
import ru.codeinside.gses.activiti.forms.PropertyCollection;
import ru.codeinside.gses.activiti.forms.PropertyNode;
import ru.codeinside.gses.activiti.forms.PropertyTree;
import ru.codeinside.gses.activiti.forms.PropertyType;
import ru.codeinside.gses.activiti.forms.ToggleNode;
import ru.codeinside.gses.activiti.history.VariableSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

final public class FieldTree implements Serializable {

  enum Type {
    ROOT, // корневой
    ITEM, // обычный
    BLOCK, // блок
    CLONE, // клон блока
    CONTROLS // управление блока
  }

  final public static class Entry implements Serializable, FormField {
    Type type; // тип
    String pid; // идентификатор шаблона свойства
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
      if (id.equals(pid)) {
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
        Logger.getAnonymousLogger().info("скрытое поле " + path + "/" + type);
        return null;
      }
      return field.getValue();
    }

    void fillExtra(final Field field, final PropertyNode node, final boolean writable) {
      final String nodeTip = node.getTip();
      final String nodeUnderline = node.getUnderline();
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

  }

  final public Entry root;
  PropertyTree propertyTree;
  boolean hasSignature;
  int maxDepth;

  public FieldTree() {
    Entry root = new Entry();
    root.type = Type.ROOT;
    this.root = root;
  }

  public void create(final FormDecorator decorator) {
    propertyTree = decorator.variableFormData.nodeMap;
    final List<FormProperty> values = new ArrayList<FormProperty>(decorator.getGeneral().values());
    new Builder(values, decorator).createAll(propertyTree, root);
    hasSignature = true;//decorator.countOfHistory() > 0;
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

  static class Builder {

    final List<FormProperty> values;
    final FormDecorator decorator;

    Builder(List<FormProperty> values, final FormDecorator decorator) {
      this.values = values;
      this.decorator = decorator;
    }

    FormProperty getByPath(String path) {
      for (FormProperty property : values) {
        if (path.equals(property.getId())) {
          return property;
        }
      }
      return null;
    }

    void createAll(PropertyCollection c, Entry root) {
      for (final PropertyNode node : c.getNodes()) {
        create(node, root, "");
      }
    }

    //TODO: унифицировать
    void update(BlockNode block, Entry owner, String suffix, int i) {
      final Entry iClone = new Entry();
      iClone.type = Type.CLONE;
      iClone.cloneIndex = i;
      iClone.pid = owner.pid;
      owner.add(iClone);
      for (final PropertyNode child : block.getNodes()) {
        create(child, iClone, suffix + "_" + i);
      }
    }

    void create(final PropertyNode node, final Entry owner, final String suffix) {
      final String path = node.getId() + suffix;
      final PropertyType propertyType = node.getPropertyType();
      final FormProperty formProperty = getByPath(path);
      // учитываем фильтр
      if (formProperty != null) {
        final Entry entry;
        final boolean writable = formProperty.isWritable();
        switch (propertyType) {
          case TOGGLE:
            break;
          case VISIBILITY_TOGGLE:
            break;
          case ITEM:
            entry = createEntry(node, owner, path, formProperty);
            entry.type = Type.ITEM;
            entry.fillExtra(decorator.createField(formProperty), node, writable);
            break;
          case ENCLOSURE:
            entry = createEntry(node, owner, path, formProperty);
            for (final PropertyNode child : ((EnclosureItem) node).enclosures.values()) {
              create(child, owner, suffix);
            }
            entry.fillExtra(decorator.createField(formProperty), node, writable);
            entry.type = Type.ITEM;
            entry.readable = false;
            break;
          case BLOCK:
            entry = createEntry(node, owner, path, formProperty);
            entry.type = Type.BLOCK;
            final String value = formProperty.getValue();
            entry.fillExtra(new BlockField(value, entry.readOnly), node, writable);
            if (value == null) {
              entry.cloneCount = 0;
            } else {
              try {
                entry.cloneCount = Integer.parseInt(value);
              } catch (NumberFormatException e) {
                Logger.getLogger(getClass().getName()).info("Ошибка в значении блока:" + value);
                entry.cloneCount = 0;
              }
            }
            final BlockNode block = (BlockNode) node;
            entry.cloneMin = block.getMinimum();
            entry.cloneMax = block.getMaximum();
            for (int i = 1; i <= entry.cloneCount; i++) {
              final Entry iClone = new Entry();
              iClone.type = Type.CLONE;
              iClone.cloneIndex = i;
              iClone.pid = entry.pid;
              entry.add(iClone);
              for (final PropertyNode child : block.getNodes()) {
                create(child, iClone, suffix + "_" + i);
              }
            }
            if (writable && entry.cloneMax > entry.cloneMin) {
              final Entry controls = new Entry();
              controls.type = Type.CONTROLS;
              owner.add(controls);
            }
            break;
          default:
            throw new IllegalArgumentException("Встретился неподдерживаем PropertyType " + propertyType);
        }
      }

    }

    private Entry createEntry(PropertyNode node, Entry owner, String path, FormProperty formProperty) {
      Entry entry = new Entry();
      owner.add(entry);
      entry.pid = node.getId();
      entry.path = path;
      entry.caption = formProperty.getName();
      if (entry.caption == null) {
        entry.caption = entry.pid;
      }
      entry.sign = createSignInfo(formProperty);
      entry.readable = true;
      return entry;
    }

    private Label createSignInfo(final FormProperty p) {
      final VariableSnapshot variableSnapshot = decorator.getHistory(p);
      if (variableSnapshot != null && variableSnapshot.login != null) {
        final Employee employee = AdminServiceProvider.get().findEmployeeByLogin(variableSnapshot.login);
        String certOwnerData = variableSnapshot.sign ? " [" + variableSnapshot.certOwnerName + " (" + variableSnapshot.certOwnerOrgName + ")]" : "";
        final Label info = new Label(employee != null ? (employee.getFio() + certOwnerData) : variableSnapshot.login + certOwnerData);
        if (variableSnapshot.sign) {
          info.setCaption(variableSnapshot.verified ? "Подписано" : "Изменено");
        } else {
          info.setCaption("Изменено");
        }
        info.setStyleName(Reindeer.LABEL_SMALL);
        return info;
      }
      return null;
    }

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
    sb.append(entry.pid);
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

  public void update(FormPropertyClones clones, Entry block, FormDecorator simpleDecorator, String suffix, int i) {
    BlockNode node = (BlockNode) propertyTree.getIndex().get(block.pid);
    new Builder(clones.properties, simpleDecorator).update(node, block, suffix, i);
  }

  public List<FormField> getFormFields() {
    final List<FormField> list = new ArrayList<FormField>();
    root.fillFormFields(list);
    return list;
  }

}
