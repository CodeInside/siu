/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.apservice;

import com.google.common.collect.Lists;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import ru.codeinside.adm.database.Service;
import ru.codeinside.adm.ui.LazyLoadingContainer2;
import ru.codeinside.gses.activiti.ftarchive.validators.FilteredLongValidator;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryContainer;
import ru.codeinside.gses.manager.ManagerService;
import ru.codeinside.gses.service.impl.DeclarantServiceImpl;
import ru.codeinside.gses.vaadin.MaskedTextField;
import ru.codeinside.gses.webui.DeclarantTypeChanged;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.components.ApInfo;
import ru.codeinside.gses.webui.components.api.IRefresh;
import ru.codeinside.gses.webui.executor.DeclarantTypeQueryDefinition;
import ru.codeinside.gses.webui.executor.DeclarantTypeQueryFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public class ApServiceForm extends VerticalLayout implements IRefresh {

  private static final String CODE = "code";
  private static final String NAME = "name";
  private static final String ID = "id";

  private static final long serialVersionUID = -4762441232549644314L;

  private final Form createForm;
  private final Form editForm;
  private List<LazyLoadingContainer2> dependentContainers = Lists.newArrayList();

  public void addDependentContainer(LazyLoadingContainer2 container) {
    dependentContainers.add(container);
  }

  public void addItemSetChangeListener(Container.ItemSetChangeListener l) {
    for (LazyLoadingContainer2 d : dependentContainers) {
      d.addListener(l);
    }
  }

  public ApServiceForm() {
    createForm = createForm();
    editForm = editForm();
    addComponent(createForm);
    addComponent(editForm);
    show(createForm);
    hide(editForm);
    setSizeFull();
    Flash.bind(DeclarantTypeChanged.class, this, "refresh");
    //TODO undind DeclarantTypeChanged
  }

  protected void showForm() {
    activateForm();
  }

  private void show(Form form) {
    form.setVisible(true);
  }

  private void hide(Form form) {
    form.setVisible(false);
  }


  private void activateForm() {
    addComponent(createForm);
    show(editForm);
  }

  protected void showForm(Service s) {
    show(editForm);
    hide(createForm);
    Field idFIeld = editForm.getField(ID);
    idFIeld.setReadOnly(false);
    idFIeld.setValue(s.getId().toString());
    idFIeld.setReadOnly(true);
    editForm.getField(NAME).setValue(s.getName());
    editForm.getField(CODE).setValue(ApInfo.formatCode(s.getRegisterCode()));
    ListSelect ls = (ListSelect) editForm.getField(DeclarantServiceImpl.DECLARANT_TYPES);
    //TODO избавиться от хака
    for (Object o : ls.getItemIds()) {
      ls.unselect(o);
    }
    ls.setValue(null);
    for (String declarantType : s.getDeclarantTypes()) {
      for (Object o : ls.getItemIds()) {
        if (declarantType.equals(ls.getItem(o).getItemProperty("name").getValue().toString())) {
          ls.select(o);
        }
      }
    }
  }

  private Form createForm() {
    final Form form = new Form();
    form.setCaption("Создание услуги");
    form.setWidth("100%");

    TextArea field = new TextArea("Наименование");
    field.setRequired(true);
    field.setWidth("100%");
    field.setMaxLength(1500);
    form.addField(NAME, field);

    MaskedTextField code = new MaskedTextField("Код в реестре", "# ### ### ### ### ### ###");
    code.setColumns(19);
    code.addValidator(new FilteredLongValidator("[_ ]", "Код не может быть больше " + ApInfo.formatCode(Long.MAX_VALUE)));
    form.addField(CODE, code);

    ListSelect declarantTypes = new ListSelect("Категории заявителей");
    declarantTypes.setWidth("100%");
    declarantTypes.setHeight("50px");
    declarantTypes.setMultiSelect(true);
    LazyQueryContainer container = new LazyQueryContainer(new DeclarantTypeQueryDefinition(), new DeclarantTypeQueryFactory());
    declarantTypes.setContainerDataSource(container);
    declarantTypes.setItemCaptionPropertyId("value");
    declarantTypes.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
    Flash.bind(DeclarantTypeChanged.class, container, "refresh");

    field.setRequired(true);
    field.setWidth("100%");
    field.setMaxLength(1500);
    form.addField(DeclarantServiceImpl.DECLARANT_TYPES, declarantTypes);

    Layout footer = form.getFooter();
    Button updateButton = createCreateButton();
    footer.addComponent(updateButton);
    ((HorizontalLayout) footer).setSpacing(true);
    return form;
  }

  private Form editForm() {
    final Form form = new Form();
    form.setCaption("Редактирование услуги");
    form.setWidth("100%");
    TextField idField = new TextField("Код услуги");
    idField.setRequired(false);
    idField.setEnabled(true);
    idField.setVisible(true);
    idField.setReadOnly(true);

    form.addField(ID, idField);
    TextArea field = new TextArea("Наименование");
    field.setRequired(true);
    field.setWidth("100%");
    field.setMaxLength(1500);
    form.addField(NAME, field);

    MaskedTextField code = new MaskedTextField("Код в реестре", "# ### ### ### ### ### ###");
    code.setColumns(19);
    code.addValidator(new FilteredLongValidator("[_ ]", "Код не может быть больше " + ApInfo.formatCode(Long.MAX_VALUE)));
    form.addField(CODE, code);

    ListSelect declarantTypes = new ListSelect("Категории заявителей");
    declarantTypes.setWidth("100%");
    declarantTypes.setHeight("50px");
    declarantTypes.setMultiSelect(true);

    LazyQueryContainer container = new LazyQueryContainer(new DeclarantTypeQueryDefinition(), new DeclarantTypeQueryFactory());
    declarantTypes.setContainerDataSource(container);
    declarantTypes.setItemCaptionPropertyId("value");
    declarantTypes.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
    Flash.bind(DeclarantTypeChanged.class, container, "refresh");

    field.setRequired(true);
    field.setWidth("100%");
    field.setMaxLength(1500);
    form.addField(DeclarantServiceImpl.DECLARANT_TYPES, declarantTypes);

    Layout footer = form.getFooter();
    Button updateButton = createUpdateButton();
    Button cancelButton = createCancelButton(form);
    footer.addComponent(updateButton);
    footer.addComponent(cancelButton);
    ((HorizontalLayout) footer).setSpacing(true);
    return form;
  }

  private Button createCreateButton() {
    Button createProcedure = new Button("Создать");
    createProcedure.addListener(new ClickListener() {
      private static final long serialVersionUID = -7774602726746226851L;

      @Override
      public void buttonClick(ClickEvent event) {
        Field fieldName = createForm.getField(NAME);
        Field fieldCode = createForm.getField(CODE);
        try {
          fieldName.setValue(fieldName.getValue().toString().trim());
          createForm.commit();
        } catch (InvalidValueException e) {
          return;
        }
        String creatorLogin = getApplication().getUser().toString();
        String name = createForm.getField(NAME).getValue().toString();
        List<String> declarantTypes = getDeclarantTypes((ListSelect) createForm.getField(DeclarantServiceImpl.DECLARANT_TYPES));
        Long code = null;
        if (fieldCode.getValue() != null) {
          Iterator<Validator> i = fieldCode.getValidators().iterator();
          i.next();// пропускаем валидатор маски
          final FilteredLongValidator flv = (FilteredLongValidator) i.next();
          code = flv.toLong(fieldCode.getValue().toString());
        }
        long apService = ManagerService.get().createApService(name, code, creatorLogin, declarantTypes);
        if (apService == 0) {
          getWindow().showNotification("Услуга с таким кодом уже существует");
        } else {
          for (LazyLoadingContainer2 d : dependentContainers) {
            d.fireItemSetChange();
          }
          getWindow().showNotification("Услуга " + name + " создана");
          createForm.getField(NAME).setValue("");
          createForm.getField(CODE).setValue("");
          createForm.setValidationVisible(false);
          cleanForm();
        }
      }
    });
    return createProcedure;
  }

  private List<String> getDeclarantTypes(ListSelect field) {
    Collection typeValues = (Collection) field.getValue();
    List<String> declarantTypes = new ArrayList<String>(typeValues.size());
    Iterator i = typeValues.iterator();
    while (i.hasNext()) {
      Item item = field.getItem(i.next());
      declarantTypes.add(item.getItemProperty("name").getValue().toString());
    }
    return declarantTypes;
  }

  private Button createUpdateButton() {
    Button updateProcedure = new Button("Сохранить");
    updateProcedure.addListener(new ClickListener() {
      private static final long serialVersionUID = 7102711218014031544L;

      @Override
      public void buttonClick(ClickEvent event) {
        Field fieldName = editForm.getField(NAME);
        Field fieldCode = editForm.getField(CODE);
        try {
          fieldName.setValue(fieldName.getValue().toString().trim());
          editForm.commit();
        } catch (InvalidValueException e) {
          return;
        }
        String id = editForm.getField(ID).getValue().toString();
        String name = editForm.getField(NAME).getValue().toString();
        List<String> declarantTypes = getDeclarantTypes((ListSelect) editForm.getField(DeclarantServiceImpl.DECLARANT_TYPES));
        Long code = null;
        if (fieldCode.getValue() != null) {
          Iterator<Validator> i = fieldCode.getValidators().iterator();
          i.next();// пропускаем валидатор маски
          final FilteredLongValidator flv = (FilteredLongValidator) i.next();
          code = flv.toLong(fieldCode.getValue().toString());
        }
        if (ManagerService.get().updateApservice(id, name, code, declarantTypes)) {
          getWindow().showNotification("Услуга с таким кодом уже существует");
        } else {
          for (LazyLoadingContainer2 d : dependentContainers) {
            d.fireItemSetChange();
          }
          cleanForm();
          getWindow().showNotification("Услуга " + name + " изменена");
        }
      }
    });
    return updateProcedure;

  }

  private Button createCancelButton(final Form form) {
    Button cancelButton = new Button("Отмена");
    cancelButton.addListener(new ClickListener() {

      private static final long serialVersionUID = 3836238707161959082L;

      @Override
      public void buttonClick(ClickEvent event) {
        cleanForm();
      }
    });
    return cancelButton;
  }

  private void cleanForm() {
    show(createForm);
    hide(editForm);
    //TODO избавиться от хака
    ListSelect ls = (ListSelect) createForm.getField(DeclarantServiceImpl.DECLARANT_TYPES);
    for (Object o : ls.getItemIds()) {
      ls.unselect(o);
    }
    ls.setValue(null);
  }

  @Override
  public void refresh() {
    cleanForm();
  }
}
