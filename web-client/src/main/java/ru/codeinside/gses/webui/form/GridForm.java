/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.google.common.base.Strings;
import com.vaadin.data.Validator;
import com.vaadin.terminal.CompositeErrorMessage;
import com.vaadin.terminal.ErrorMessage;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Select;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.activiti.FormDecorator;
import ru.codeinside.gses.activiti.FormID;
import ru.codeinside.gses.activiti.forms.PropertyCollection;
import ru.codeinside.gses.activiti.forms.PropertyNode;
import ru.codeinside.gses.activiti.forms.PropertyType;
import ru.codeinside.gses.activiti.forms.ToggleNode;
import ru.codeinside.gses.form.FormEntry;
import ru.codeinside.gses.service.F3;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.vaadin.ScrollableForm;
import ru.codeinside.gses.webui.Flash;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GridForm extends ScrollableForm implements FormDataSource {

  public static final String REQUIRED_MESSAGE = "Обязательно к заполнению!";

  SourceException bsex;

  final public FieldTree fieldTree;
  final int colsCount;
  final int valueColumn;
  final FormDecorator decorator;
  final GridLayout gridLayout;

  public GridForm(FormDecorator decorator, FieldTree fieldTree) {
    setWriteThrough(false);
    setInvalidCommitted(false);
    setSizeFull();

    this.decorator = decorator;
    this.fieldTree = fieldTree;
    colsCount = fieldTree.getCols();
    int rowsCount = fieldTree.root.getControlsCount();
    if (rowsCount == 0) {
      getLayout().addComponent(new Label("Не требуется ввод данных"));
      valueColumn = 0;
      gridLayout = null;
    } else {
      valueColumn = colsCount - (fieldTree.hasSignature ? 2 : 1);
      gridLayout = new GridLayout(colsCount, rowsCount);
      gridLayout.setStyleName("lined-grid");
      gridLayout.setMargin(false);
      gridLayout.setSpacing(true);
      gridLayout.setSizeFull();
      setLayout(gridLayout);

      fieldTree.updateColumnIndex();
      buildControls(fieldTree.root, 0);
      buildToggle(fieldTree.propertyTree);
      updateExpandRatios();
    }
  }

  void updateExpandRatios() {
    if (colsCount == 3) {
      gridLayout.setColumnExpandRatio(0, 1f);
      gridLayout.setColumnExpandRatio(1, 5f);
      gridLayout.setColumnExpandRatio(2, 1f);
    } else {
      // если расшиоения нет, то колонки НЕ расширяются и занимают минимум.
      //for (int i = 0; i < valueColumn - 1; i++) {
      //  gridLayout.setColumnExpandRatio(i, 0.5f);
      //}
      //gridLayout.setColumnExpandRatio(valueColumn - 1, 0.5f);

      gridLayout.setColumnExpandRatio(valueColumn, 1f);

      //if (fieldTree.hasSignature) {
      //  gridLayout.setColumnExpandRatio(valueColumn + 1, 0.2f);
      //}
    }
    // форсировать изменения
    gridLayout.requestRepaint();
    this.requestRepaint();
  }

  void buildControls(final FieldTree.Entry entry, int level) {
    switch (entry.type) {
      case ITEM:
      case BLOCK:
        if (!entry.readable) break; // если поле не доступно для чтения, то не надо его отображать на форме
        Label caption = new Label(entry.caption);
        caption.setSizeUndefined();// важно!
        caption.setStyleName("liquid2");
        gridLayout.addComponent(caption, level, entry.index, valueColumn - 1, entry.index);
        gridLayout.setComponentAlignment(caption, entry.type == FieldTree.Type.ITEM ? Alignment.TOP_RIGHT : Alignment.TOP_LEFT);
        final Component sign = entry.sign;
        if (sign != null) {
          sign.setSizeUndefined();// важно!
          gridLayout.addComponent(sign, valueColumn + 1, entry.index);
          gridLayout.setComponentAlignment(sign, Alignment.TOP_LEFT);
        }
        // регистрируется в форме
        addField(entry.path, entry.field);
        break;
      case CONTROLS:
        int dx = valueColumn - level - 1;
        HorizontalLayout layout = createLayout();
        Button plus = createButton("+");
        layout.addComponent(plus);
        Button minus = createButton("-");
        layout.addComponent(minus);
        FieldTree.Entry block = getBlock(entry);
        plus.addListener(new AppendAction(entry, minus));
        minus.addListener(new RemoveAction(entry, plus));
        if (block.field != null) {
          final StringBuilder sb = new StringBuilder();
          if (!StringUtils.isBlank(block.caption)) {
            sb.append(' ')
              .append('\'')
              .append(block.caption)
              .append('\'');
          }
          if (block.field.getDescription() != null) {
            sb.append(' ')
              .append('(')
              .append(block.field.getDescription())
              .append(')');
          }
          plus.setDescription("Добавить" + sb);
          minus.setDescription("Удалить" + sb);
        }
        updateCloneButtons(plus, minus, block);
        gridLayout.addComponent(layout, level, entry.index, level + dx, entry.index);
        gridLayout.setComponentAlignment(layout, Alignment.TOP_LEFT);
        break;
      case CLONE:
        int y = entry.index;
        int dy = entry.getControlsCount() - 1;
        Label cloneCaption = new Label(entry.cloneIndex + ")");
        cloneCaption.setSizeUndefined();
        gridLayout.addComponent(cloneCaption, level - 1, y, level - 1, y + dy);
        gridLayout.setComponentAlignment(cloneCaption, Alignment.TOP_LEFT);
        break;
      case ROOT:
        break;
      default:
        throw new IllegalStateException("Встретился не известный тип поля " + entry.type);
    }

    if (entry.items != null) { // работаем с подчиненными полями
      if (entry.type == FieldTree.Type.BLOCK) {
        level++;
      }
      for (FieldTree.Entry child : entry.items) {
        buildControls(child, level);
      }
    }
  }

  private Button createButton(String caption) {
    Button button = new Button(caption);
    button.setImmediate(true);
    return button;
  }

  private HorizontalLayout createLayout() {
    HorizontalLayout layout = new HorizontalLayout();
    layout.setSizeUndefined();
    layout.setImmediate(true);
    layout.setSpacing(true);
    return layout;
  }

  private void updateCloneButtons(Button plus, Button minus, FieldTree.Entry block) {
    minus.setEnabled(block.cloneMin < block.cloneCount);
    plus.setEnabled(block.cloneCount < block.cloneMax);
  }

  @Override
  public void validate() throws Validator.InvalidValueException {
    scrollTo(null);
    for (final Object id : getItemPropertyIds()) {
      final Field field = getField(id);
      try {
        field.validate();
      } catch (Validator.InvalidValueException e) {
        scrollTo(field);
        throw e;
      }
    }
  }

  @Override
  public void commit() throws SourceException, Validator.InvalidValueException {
    bsex = null;
    try {
      super.commit();
    } catch (SourceException e) {
      bsex = e;
      throw e;
    }
  }

  @Override
  public void discard() throws SourceException {
    bsex = null;
    try {
      super.discard();
    } catch (SourceException e) {
      bsex = e;
      throw e;
    }
  }

  @Override
  protected void attachField(final Object propertyId, final Field field) {
    addToLayout(fieldTree.root.getEntry(field));
  }

  private void addToLayout(final FieldTree.Entry entry) {
    final Field field = entry.field;
    field.setCaption(field.isRequired() ? "" : null);
    final Component component = entry.underline == null ? field : entry.underline;
    gridLayout.addComponent(component, valueColumn, entry.index);
    gridLayout.setComponentAlignment(component, Alignment.TOP_LEFT);
  }

  @Override
  protected void detachField(final Field field) {
    gridLayout.removeComponent(field);
  }

  public boolean isAttachedField(final FieldTree.Entry target) {
    if (target.underline != null) {
      return null != gridLayout.getComponentArea(target.underline);
    }
    return null != gridLayout.getComponentArea(target.field);
  }

  private String getCaption(final Field f) {
    return fieldTree.root.getEntry(f).getCaption();
  }

  @Override
  public ErrorMessage getErrorMessage() {
    final List<ErrorMessage> errors = new ArrayList<ErrorMessage>();

    final ErrorMessage formError = getComponentError();
    if (formError != null) {
      errors.add(formError);
    }

    if (isValidationVisible()) {
      for (final Object id : getItemPropertyIds()) {
        final Field field = getField(id);
        if (field instanceof AbstractComponent) {
          final String caption = StringUtils.trimToEmpty(getCaption(field));
          final ErrorMessage validationError = ((AbstractComponent) field).getErrorMessage();
          if (validationError != null) {
            errors.addAll(convertError(validationError, caption));
          } else if (!field.isValid()) {
            // считаем что это стандартная ошибка
            errors.add(new Validator.InvalidValueException(convertErrorMessage(caption, REQUIRED_MESSAGE)));
          }
        }
      }
    }

    if (bsex != null) {
      errors.add(bsex);
    }

    if (errors.isEmpty()) {
      return null;
    }

    return new CompositeErrorMessage(errors);
  }

  private List<ErrorMessage> convertError(final ErrorMessage validationError, final String caption) {
    final List<ErrorMessage> converted = new ArrayList<ErrorMessage>();
    convertError(validationError, caption, converted);
    return converted;
  }

  private void convertError(ErrorMessage validationError, final String caption, List<ErrorMessage> acc) {
    if (validationError instanceof Validator.EmptyValueException) {
      final Validator.EmptyValueException original = (Validator.EmptyValueException) validationError;
      String text = original.getMessage();
      if (StringUtils.isBlank(text)) {
        text = REQUIRED_MESSAGE;
      }
      acc.add(new Validator.InvalidValueException(convertErrorMessage(caption, text)));
    } else if (validationError instanceof Validator.InvalidValueException) {
      final Validator.InvalidValueException original = (Validator.InvalidValueException) validationError;
      acc.add(new Validator.InvalidValueException(convertErrorMessage(caption, original.getMessage())));
    } else if (validationError instanceof CompositeErrorMessage) {
      final CompositeErrorMessage original = (CompositeErrorMessage) validationError;
      final Iterator<ErrorMessage> iterator = original.iterator();
      while (iterator.hasNext()) {
        convertError(iterator.next(), caption, acc);
      }
    } else {
      acc.add(validationError);
    }
  }

  private String convertErrorMessage(final String caption, final String message) {
    final StringBuilder sb = new StringBuilder();
    if (!message.contains(caption)) {
      sb.append(caption);
      if (!caption.endsWith(":")) {
        sb.append(':');
      }
      sb.append(' ');
    }
    sb.append(message);
    return sb.toString();
  }

  static FieldTree.Entry getBlock(final FieldTree.Entry entry) {
    FieldTree.Entry parent = entry.parent;
    return parent.items.get(parent.items.indexOf(entry) - 1);
  }

  static GridForm getGridForm(Button.ClickEvent event) {
    Component c = event.getButton().getParent();
    while (!(c instanceof GridForm)) {
      c = c.getParent();
    }
    return (GridForm) c;
  }


  private FormEntry generateFormData(FieldTree.Entry entry) {
    FormEntry formEntry = null;
    switch (entry.type) {
      case ITEM:
      case BLOCK:
        if (!entry.readable || entry.hidden) {
          return null;
        }
        formEntry = new FormEntry();
        formEntry.name = entry.caption;
        formEntry.value = getUserFriendlyContent(entry);
        break;

      case CONTROLS:
        break;

      case CLONE:
        formEntry = new FormEntry();
        formEntry.name = Integer.toString(entry.cloneIndex) + ")";
        break;

      case ROOT:
        formEntry = new FormEntry();
        break;

      default:
        throw new IllegalStateException();
    }
    if (formEntry != null && entry.items != null) {
      List<FormEntry> children = new ArrayList<FormEntry>(entry.items.size());
      for (FieldTree.Entry child : entry.items) {
        FormEntry childEntry = generateFormData(child);
        if (childEntry != null) {
          children.add(childEntry);
        }
      }
      if (!children.isEmpty()) {
        formEntry.children = children.toArray(new FormEntry[children.size()]);
      }
    }
    return formEntry;
  }

  String getUserFriendlyContent(FieldTree.Entry entry) {
    Field field = entry.field;
    Object value = field.getValue();
    String result = null;
    if (value != null) {
      if (field instanceof Select) {
        result = ((Select) field).getItemCaption(value);
      } else if (field instanceof DateField) {
        result = new SimpleDateFormat(((DateField) field).getDateFormat()).format(value);
      } else if (value instanceof Boolean) {
        result = Boolean.TRUE.equals(value) ? "Да" : "Нет";
      } else {
        result = value.toString();
      }
    }
    return result;
  }

  @Override
  public FormEntry createFormTree() {
    return generateFormData(fieldTree.root);
  }

  final static class RemoveAction implements Button.ClickListener {

    final FieldTree.Entry entry;
    final Button plus;

    RemoveAction(FieldTree.Entry entry, Button plus) {
      this.entry = entry;
      this.plus = plus;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
      FieldTree.Entry block = getBlock(entry);
      if (block.items != null && !block.items.isEmpty() && block.cloneCount > block.cloneMin) {
        block.cloneCount--;
        final FieldTree.Entry clone = block.items.remove(block.cloneCount);
        GridForm gridForm = getGridForm(event);
        clone.removeFields(gridForm);
        int index = clone.index;
        int count = clone.getControlsCount();
        for (int i = 0; i < count; i++) {
          gridForm.gridLayout.removeRow(index);
        }
        block.field.setValue(block.cloneCount);
        gridForm.fieldTree.updateColumnIndex();
        gridForm.updateExpandRatios();
        gridForm.updateCloneButtons(plus, event.getButton(), block);
      }
    }

  }


  static class AppendAction implements Button.ClickListener {
    final FieldTree.Entry controls;
    private Button minus;

    public AppendAction(FieldTree.Entry controls, Button minus) {
      this.controls = controls;
      this.minus = minus;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
      final FieldTree.Entry block = getBlock(controls);
      if (block.cloneCount < block.cloneMax) {
        GridForm gridForm = getGridForm(event);
        int cloneIndex = ++block.cloneCount;
        String blockSuffix = block.calcSuffix();
        String suffix = blockSuffix + "_" + cloneIndex;
        FormPropertyClones clones = Fn.withEngine(new Fetcher(), gridForm.decorator.id, block.pid, suffix);
        int insertIndex = controls.index;
        gridForm.fieldTree.update(clones, block, gridForm.decorator.toComplex(clones.snapshots), blockSuffix, cloneIndex);
        block.field.setValue(block.cloneCount);
        gridForm.fieldTree.updateColumnIndex();
        //gridForm.fieldTree.dumpTree();
        final FieldTree.Entry clone = block.items.get(cloneIndex - 1);
        int count = clone.getControlsCount();
        // вставка пустого места
        for (int i = 0; i < count; i++) {
          gridForm.gridLayout.insertRow(insertIndex);
        }
        int level = clone.getLevel();
        gridForm.buildControls(clone, level);
        gridForm.buildToggle(gridForm.fieldTree.propertyTree);
        gridForm.updateExpandRatios();
        gridForm.updateCloneButtons(event.getButton(), minus, block);
      }
    }
  }

  final static class Fetcher implements F3<FormPropertyClones, FormID, String, String> {
    @Override
    public FormPropertyClones apply(ProcessEngine engine, FormID id, String pid, String path) {
      final CommandExecutor commandExecutor = ((ServiceImpl) engine.getFormService()).getCommandExecutor();
      return commandExecutor.execute(new CloneFormPropertiesCommand(
        id, pid, path, Flash.login()
      ));
    }
  }

  private void buildToggle(final PropertyCollection collection) {
    for (final PropertyNode node : collection.getNodes()) {
      buildToggle(node);
    }
  }

  private void buildToggle(final PropertyNode node) {
    final PropertyType type = node.getPropertyType();
    if (type == PropertyType.BLOCK) {
      buildToggle((PropertyCollection) node);
    } else if (type == PropertyType.TOGGLE || type == PropertyType.VISIBILITY_TOGGLE) {
      final ToggleNode toggleDef = (ToggleNode) node;
      final List<FieldTree.Entry> sources = fieldTree.getEntries(toggleDef.getToggler().getId());
      for (FieldTree.Entry source : sources) {
        if (source.togglers == null || !source.togglers.contains(toggleDef)) {
          if (source.togglers == null) {
            source.togglers = new LinkedList<ToggleNode>();
          }
          source.togglers.add(toggleDef);
          final Field field = source.field;
          if (type == PropertyType.TOGGLE) {
            final MandatoryToggle toggle = new MandatoryToggle(toggleDef, source, fieldTree);
            toggle.toggle(field);
            field.addListener(new MandatoryChangeListener(toggle));
          } else {
            final VisibilityToggle toggle = new VisibilityToggle(toggleDef, source);
            toggle.toggle(this, field);
            field.addListener(new VisibilityChangeListener(this, toggle));
          }
        }
      }
    }
  }
}
