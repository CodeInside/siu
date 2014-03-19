/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.vaadin.data.Validator;
import com.vaadin.terminal.CompositeErrorMessage;
import com.vaadin.terminal.ErrorMessage;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.*;
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
import ru.codeinside.gses.service.F3;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.vaadin.ScrollableForm;
import ru.codeinside.gses.webui.Flash;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.commons.lang.StringUtils.defaultString;

public class GridForm extends ScrollableForm {

  public static final String REQUIRED_MESSAGE = "Обязательно к заполнению!";

  SourceException bsex;

  final public FieldTree fieldTree;
  final int colsCount;
  final int valueColumn;
  final FormDecorator decorator;
  final GridLayout inputPanel;
  final GridLayout previewPanel;

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
      inputPanel = null;
      previewPanel = null;
    } else {
      valueColumn = colsCount - (fieldTree.hasSignature ? 2 : 1);
      inputPanel = createPanel(rowsCount);
      VerticalLayout mainLayout = new VerticalLayout();
      previewPanel = createPreviewPanel(rowsCount);
      mainLayout.addComponent(createTabSheet(inputPanel, previewPanel));
      mainLayout.setSizeFull();
      setLayout(mainLayout);
      fieldTree.updateColumnIndex();
      buildControls(fieldTree.root, 0);
    //  buildPreviewControls(fieldTree.root, 0);
      buildToggle(fieldTree.propertyTree);
      updateExpandRatios();
    }
  }

  private TabSheet createTabSheet(final GridLayout inputPanel, final GridLayout previewPanel) {
    final TabSheet sheet = new TabSheet();
    sheet.addComponent(inputPanel);
    sheet.getTab(inputPanel).setCaption("Ввод данных");

    sheet.addComponent(previewPanel);
    sheet.getTab(previewPanel).setCaption("Предварительный просмотр");
    sheet.addListener(new TabSheet.SelectedTabChangeListener() {
      @Override
      public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
        TabSheet tabSheet = (TabSheet)event.getSource();
        if (tabSheet.getSelectedTab() == previewPanel){
          previewPanel.removeAllComponents();
          buildPreviewControls(fieldTree.root, 0);
          createPrintButton(previewPanel);
        }
      }
    });
    return sheet;
  }

  private void createPrintButton(GridLayout previewPanel) {
    final String result = "<table>" + generateHtmlTableContent(fieldTree.root, 0) + "</table><script type='text/javascript'>window.onload=function(){window.print();};</script>";
    Button printButton = new Button("Печать", new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        Embedded frame = new Embedded("", new StreamResource(new StreamResource.StreamSource() {
          @Override
          public InputStream getStream() {

            try {
              return new ByteArrayInputStream(result.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
              return null;

            }
          }
        }, "print" + UUID.randomUUID().toString() + ".html", getApplication()) );
        frame.setType(Embedded.TYPE_BROWSER);

        VerticalLayout vl = new VerticalLayout();
        vl.setMargin(true);
        vl.setSizeFull();
        vl.addComponent(frame);
        vl.setExpandRatio(frame, 1f);
        frame.setSizeFull();
        final Window w = new Window("Печать формы", vl);
        w.center();
        w.setClosable(true);
        w.setWidth(50, Sizeable.UNITS_PERCENTAGE);
        w.setHeight(50, Sizeable.UNITS_PERCENTAGE);
        getApplication().getMainWindow().addWindow(w);
      }
    });

    previewPanel.addComponent(printButton, colsCount, 0);
  }

  private String generateHtmlTableContent(FieldTree.Entry entry, int level) {
    String result = "";
    switch (entry.type) {
      case ITEM:
      case BLOCK:
        if (!entry.readable || entry.hidden) return ""; // если поле не доступно для чтения, то не надо его отображать на форме
        result =  String.format("<tr><td>%s</td><td>%s</td></tr>", entry.caption, getUserFriendlyContent(entry));
        break;
      case CONTROLS:
        FieldTree.Entry block = getBlock(entry);
        if (block.field != null) {
          result = String.format("<tr><td>%s</td><td>%s</td></tr>", defaultString(block.caption), defaultString(block.field.getDescription()));
        }
        break;
      case CLONE:
        result =  String.format("<tr><td colspan=2>%d)</td></tr>", entry.cloneIndex);
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
        result = result + generateHtmlTableContent(child, level);
      }
    }
    return result;
  }

  private GridLayout createPanel(int rowsCount) {
    GridLayout layout = new GridLayout(colsCount, rowsCount);
    adjustProperties(layout);
    return layout;
  }

  private GridLayout createPreviewPanel(int rowsCount) {
    GridLayout layout = new GridLayout(colsCount+1, rowsCount);
    adjustProperties(layout);
    return layout;
  }

  private void adjustProperties(GridLayout layout) {
    layout.setStyleName("lined-grid");
    layout.setMargin(false);
    layout.setSpacing(true);
    layout.setSizeFull();
  }

  void updateExpandRatios() {
    if (colsCount == 3) {
      inputPanel.setColumnExpandRatio(0, 1f);
      inputPanel.setColumnExpandRatio(1, 5f);
      inputPanel.setColumnExpandRatio(2, 1f);

      previewPanel.setColumnExpandRatio(0, 1f);
      previewPanel.setColumnExpandRatio(1, 5f);
      previewPanel.setColumnExpandRatio(2, 1f);
    } else {
      // если расшиоения нет, то колонки НЕ расширяются и занимают минимум.
      //for (int i = 0; i < valueColumn - 1; i++) {
      //  inputPanel.setColumnExpandRatio(i, 0.5f);
      //}
      //inputPanel.setColumnExpandRatio(valueColumn - 1, 0.5f);

      inputPanel.setColumnExpandRatio(valueColumn, 1f);
      previewPanel.setColumnExpandRatio(valueColumn, 1f);

      //if (fieldTree.hasSignature) {
      //  inputPanel.setColumnExpandRatio(valueColumn + 1, 0.2f);
      //}
    }
    // форсировать изменения
    inputPanel.requestRepaint();
    previewPanel.requestRepaint();

    this.requestRepaint();
  }

  private void buildPreviewControls(FieldTree.Entry entry, int level) {
    switch (entry.type) {
      case ITEM:
      case BLOCK:
        if (!entry.readable || entry.hidden) break; // если поле не доступно для чтения, то не надо его отображать на форме
        Label caption = new Label(entry.caption);
        caption.setSizeUndefined();// важно!
        caption.setStyleName("liquid2");
        previewPanel.addComponent(caption, level, entry.index, valueColumn - 1, entry.index);
        previewPanel.setComponentAlignment(caption, entry.type == FieldTree.Type.ITEM ? Alignment.TOP_RIGHT : Alignment.TOP_LEFT);
        final Component sign = entry.sign;
        if (sign != null) {
          sign.setSizeUndefined();// важно!
          previewPanel.addComponent(sign, valueColumn + 1, entry.index);
          previewPanel.setComponentAlignment(sign, Alignment.TOP_LEFT);
        }
        // регистрируется в форме
        //addField(entry.path, entry.field);
        Label fieldValue = new Label(getUserFriendlyContent(entry));

        previewPanel.addComponent(fieldValue, valueColumn, entry.index);
        break;
      case CONTROLS:
        int dx = valueColumn - level - 1;
        HorizontalLayout layout = createLayout();

        FieldTree.Entry block = getBlock(entry);

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

        }

        previewPanel.addComponent(layout, level, entry.index, level + dx, entry.index);
        previewPanel.setComponentAlignment(layout, Alignment.TOP_LEFT);
        break;
      case CLONE:
        int y = entry.index;
        int dy = entry.getControlsCount() - 1;
        Label cloneCaption = new Label(entry.cloneIndex + ")");
        cloneCaption.setSizeUndefined();
        previewPanel.addComponent(cloneCaption, level - 1, y, level - 1, y + dy);
        previewPanel.setComponentAlignment(cloneCaption, Alignment.TOP_LEFT);
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
        buildPreviewControls(child, level);
      }
    }
  }

  private String getUserFriendlyContent(FieldTree.Entry entry) {
    Field field = entry.field;
    if (field instanceof Select) {
      Select select = (Select) field;
      Object value = select.getValue();
      if (value != null) {
        return select.getItemCaption(value);
      }
    } else if (field instanceof DateField) {
      DateField dateField = (DateField) field;
      if (dateField.getValue() != null) {
        return new SimpleDateFormat(dateField.getDateFormat()).format(dateField.getValue());
      }
    } else if (field instanceof CheckBox){
      CheckBox checkBox = (CheckBox)field;
      return Boolean.TRUE.equals(checkBox.getValue()) ? "Да" : "Нет";
    }
    return field.getValue() != null ? field.getValue().toString() : "";
  }

  void buildControls(final FieldTree.Entry entry, int level) {
    switch (entry.type) {
      case ITEM:
      case BLOCK:
        if (!entry.readable) break; // если поле не доступно для чтения, то не надо его отображать на форме
        Label caption = new Label(entry.caption);
        caption.setSizeUndefined();// важно!
        caption.setStyleName("liquid2");
        inputPanel.addComponent(caption, level, entry.index, valueColumn - 1, entry.index);
        inputPanel.setComponentAlignment(caption, entry.type == FieldTree.Type.ITEM ? Alignment.TOP_RIGHT : Alignment.TOP_LEFT);
        final Component sign = entry.sign;
        if (sign != null) {
          sign.setSizeUndefined();// важно!
          inputPanel.addComponent(sign, valueColumn + 1, entry.index);
          inputPanel.setComponentAlignment(sign, Alignment.TOP_LEFT);
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
        inputPanel.addComponent(layout, level, entry.index, level + dx, entry.index);
        inputPanel.setComponentAlignment(layout, Alignment.TOP_LEFT);
        break;
      case CLONE:
        int y = entry.index;
        int dy = entry.getControlsCount() - 1;
        Label cloneCaption = new Label(entry.cloneIndex + ")");
        cloneCaption.setSizeUndefined();
        inputPanel.addComponent(cloneCaption, level - 1, y, level - 1, y + dy);
        inputPanel.setComponentAlignment(cloneCaption, Alignment.TOP_LEFT);
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
    inputPanel.addComponent(component, valueColumn, entry.index);
    inputPanel.setComponentAlignment(component, Alignment.TOP_LEFT);
  }

  @Override
  protected void detachField(final Field field) {
    inputPanel.removeComponent(field);
  }

  public boolean isAttachedField(final FieldTree.Entry target) {
    if (target.underline != null) {
      return null != inputPanel.getComponentArea(target.underline);
    }
    return null != inputPanel.getComponentArea(target.field);
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
          gridForm.removeRow(index);
        }
        block.field.setValue(block.cloneCount);
        gridForm.fieldTree.updateColumnIndex();
        gridForm.updateExpandRatios();
        gridForm.updateCloneButtons(plus, event.getButton(), block);
      }
    }

  }

   void removeRow(int index) {
    inputPanel.removeRow(index);
    previewPanel.removeRow(index);
  }
  void insertRow(int insertIndex){
    inputPanel.insertRow(insertIndex);
    previewPanel.insertRow(insertIndex);
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
          gridForm.insertRow(insertIndex);
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
