/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.manager;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.adm.database.Procedure;
import ru.codeinside.adm.database.ProcedureType;
import ru.codeinside.adm.database.Service;
import ru.codeinside.adm.ui.LazyLoadingContainer2;
import ru.codeinside.gses.activiti.ftarchive.validators.FilteredLongValidator;
import ru.codeinside.gses.vaadin.MaskedTextField;
import ru.codeinside.gses.webui.components.ApInfo;
import ru.codeinside.gses.webui.components.LayoutChanger;

import java.util.Iterator;
import java.util.List;

public class ProcedureForm extends VerticalLayout implements Container.ItemSetChangeListener {

  private static final String DESCRIPTION = "description";
  private static final String SERVICE_ID = "serviceId";
  private static final String ID = "id";
  private static final String CODE = "code";
  public static final String NAME = "name";
  private ProcedureType type;

  private static final long serialVersionUID = -4762441232549644314L;

  private final Form createUpdateForm;
  private final Button newProcButton;
  private final Button editProcButton;
  private final HorizontalLayout buttonsLayout;
  private final VerticalLayout changerLayout;
  private final LayoutChanger changer;
  private LazyLoadingContainer2 dependentContainer;
  private VerticalLayout procedureLayout;
  private ApInfo procedureInfo;
  private Procedure p;
  private final VerticalLayout clean;
  private ComboBox servicesComboBox;

  public void setDependentContainer(LazyLoadingContainer2 container) {
    dependentContainer = container;
  }

  public void setProcedureType(ProcedureType type) {
    if (type != null && this.type != type) {
      this.type = type;
      if (type == ProcedureType.Administrative) {
        createUpdateForm.getField(SERVICE_ID).setVisible(true);
        createUpdateForm.getField(SERVICE_ID).setRequired(true);
//        createUpdateForm.getField(CODE).setVisible(true);
      } else {
        createUpdateForm.getField(SERVICE_ID).setVisible(false);
        createUpdateForm.getField(SERVICE_ID).setRequired(false);
//        createUpdateForm.getField(CODE).setVisible(false);
      }
    }
  }

  public ProcedureForm() {
    buttonsLayout = new HorizontalLayout();
    changerLayout = new VerticalLayout();
    changer = new LayoutChanger(changerLayout);
    createUpdateForm = buildCreateUpdateForm();
    changer.set(createUpdateForm, "createUpdateForm");
    addComponent(buttonsLayout);
    addComponent(changerLayout);
    procedureLayout = buildProcedureInfoPanel();
    changer.set(procedureLayout, "procedureLayout");
    newProcButton = new Button("Новая процедура");
    newProcButton.addListener(new ClickListener() {
      private static final long serialVersionUID = 12334L;

      @Override
      public void buttonClick(ClickEvent event) {
        activateCreateForm(changer, createUpdateForm);
      }
    });
    editProcButton = new Button("Редактировать процедуру");
    editProcButton.addListener(new ClickListener() {
      private static final long serialVersionUID = -8265937205484323504L;

      @Override
      public void buttonClick(ClickEvent event) {
        activateUpdateForm(changer, createUpdateForm);
      }
    });
    buttonsLayout.addComponent(newProcButton);
    buttonsLayout.addComponent(editProcButton);
    buttonsLayout.setSpacing(true);
    editProcButton.setVisible(false);
    clean = new VerticalLayout();
    clean.setSizeFull();
    changer.set(clean, "clean");
    setSizeFull();
  }

  private VerticalLayout buildProcedureInfoPanel() {
    VerticalLayout layout = new VerticalLayout();
    procedureInfo = new ApInfo();
    layout.addComponent(procedureInfo);
    return layout;
  }

  private void activateCreateForm(LayoutChanger changer, Form createForm) {
    setFormToCreateMode();
    changer.change(createForm);
    editProcButton.setVisible(false);
  }

  private void activateUpdateForm(LayoutChanger changer, Form updateForm) {
    editProcButton.setVisible(false);
    setFormToUpdateMode();
    changer.change(updateForm);
  }

  private void activateProcedureInfo(LayoutChanger changer, VerticalLayout procedureLayout, Procedure p) {
    if (this.p != p) {
      this.p = p;
    }
    editProcButton.setVisible(true);
    procedureInfo.render(p, dependentContainer);
    changer.change(procedureLayout);
  }


  protected void setFormToCreateMode() {
    createUpdateForm.setValidationVisible(false);
    createUpdateForm.getField(ID).setValue("");
    createUpdateForm.getField(NAME).setValue("");
    createUpdateForm.getField(DESCRIPTION).setValue("");
    createUpdateForm.getField(SERVICE_ID).setValue(null);
    createUpdateForm.getField(CODE).setValue("");
    Component submit = createUpdateForm.getFooter().getComponentIterator().next();
    submit.setCaption("Создать");
  }

  protected void setFormToUpdateMode() {
    createUpdateForm.getField(ID).setValue(p.getId());
    createUpdateForm.getField(NAME).setValue(p.getName());
    createUpdateForm.getField(DESCRIPTION).setValue(p.getDescription());
    createUpdateForm.getField(CODE).setValue(ApInfo.formatCode(p.getRegisterCode()));
    Field select = createUpdateForm.getField(SERVICE_ID);
    if (p.getService() != null) {
      select.setValue(getItem(select, p.getService().getId().toString()));
    }
    Component submit = createUpdateForm.getFooter().getComponentIterator().next();
    submit.setCaption("Обновить");
  }

  protected void showProcedureInfo(Procedure p) {
    this.p = p;
    activateProcedureInfo(changer, procedureLayout, p);
  }

  // TODO разобраться как устанавливать значения
  private Object getItem(Field field, String servId) {
    ComboBox select = (ComboBox) field;
    for (Object o : select.getContainerDataSource().getItemIds()) {
      if (o.toString().equals(servId)) {
        return o;
      }
    }
    return null;
  }

  private Form buildCreateUpdateForm() {
    final Form form = new Form();
    TextField createTextField = createTextField("Номер", false, true);
    createTextField.setEnabled(false);
    createTextField.setWidth("100%");
    form.addField(ID, createTextField);
    TextArea areaName = new TextArea("Название");
    areaName.setRequired(true);
    areaName.setWidth("100%");
    areaName.setMaxLength(1500);
    form.addField(NAME, areaName);
    TextArea descriptionArea = new TextArea("Описание");
    descriptionArea.setWidth("100%");
    descriptionArea.setMaxLength(1500);
    form.addField(DESCRIPTION, descriptionArea);
    servicesComboBox = createServicesComboBox("Услуга");
    servicesComboBox.setNullSelectionAllowed(false);
    form.addField(SERVICE_ID, servicesComboBox);
    MaskedTextField code = new MaskedTextField("Код в реестре", "# ### ### ### ### ### ###");
    code.setColumns(19);
    code.addValidator(new FilteredLongValidator("[_ ]", "Код не может быть больше " + ApInfo.formatCode(Long.MAX_VALUE)));
    form.addField(CODE, code);
    Layout footer = form.getFooter();
    ((HorizontalLayout) footer).setSpacing(true);
    footer.addComponent(createUpdateButton());
    footer.addComponent(createCancelButton());
    return form;
  }

  private Button createUpdateButton() {
    Button updateProcedure = new Button("Создать");
    updateProcedure.addListener(new ClickListener() {

      private static final long serialVersionUID = -8265937205484323504L;

      @Override
      public void buttonClick(ClickEvent event) {
        Field fieldName = createUpdateForm.getField(NAME);
        Field fieldDescription = createUpdateForm.getField(DESCRIPTION);
        Field fieldCode = createUpdateForm.getField(CODE);
        try {
          // form.validate();
          fieldName.setValue(fieldName.getValue().toString().trim());
          fieldDescription.setValue(fieldDescription.getValue().toString().trim());
          createUpdateForm.commit();
        } catch (InvalidValueException e) {
          return;
        }
        String id = createUpdateForm.getField(ID).getValue().toString();
        String creatorLogin = getApplication().getUser().toString();
        String name = fieldName.getValue().toString();
        String description = fieldDescription.getValue().toString();
        Long code = null;
        if (fieldCode.getValue() != null) {
          Iterator<Validator> i = fieldCode.getValidators().iterator();
          i.next();// пропускаем валидатор маски
          final FilteredLongValidator flv = (FilteredLongValidator) i.next();
          code = flv.toLong(fieldCode.getValue().toString());
        }
        String serviceId = type == ProcedureType.Administrative ? createUpdateForm.getField(SERVICE_ID).getValue().toString() : null;
        try {
          Procedure procedure;
          if (StringUtils.isEmpty(id)) {
            if (type == ProcedureType.Administrative) {
              procedure = ManagerService.get().createProcedure(name, description, serviceId, code, creatorLogin, type);
            } else {
              procedure = ManagerService.get().createProcedure(name, description, null, code, creatorLogin, type);
            }
          } else {
            ManagerService.get().updateProcedure(id, name, description, serviceId, code);
            procedure = ManagerService.get().getProcedure(id);
          }
          dependentContainer.fireItemSetChange();
          if (procedure != null) {
            activateProcedureInfo(changer, procedureLayout, procedure);
          }
          getWindow().showNotification("Процедура '" + name + "' создана");
        } catch (RuntimeException e) {
          Throwable cause = e;
          String message = "";
          while (cause instanceof RuntimeException) {
            Throwable root = cause.getCause();
            if (root == null) {
              break;
            }
            if (root.getMessage() != null) {
              message = root.getMessage();
            }
            cause = root;
          }
          getWindow().showNotification(StringUtils.isNotEmpty(message) ? message : "Процедура с таким кодом уже существует", Window.Notification.TYPE_ERROR_MESSAGE);
        }
      }
    });
    return updateProcedure;
  }

  private Button createCancelButton() {
    Button cancelButton = new Button("Отмена");
    cancelButton.addListener(new ClickListener() {
      private static final long serialVersionUID = 3836238707161959082L;

      @Override
      public void buttonClick(ClickEvent event) {
        if (changer.getPrevious() != null &&
          changer.getPrevious().equals(createUpdateForm) &&
          !createUpdateForm.getField(ID).getValue().toString().isEmpty()) {
          editProcButton.setVisible(true);
        }
        changer.back();
      }
    });
    return cancelButton;
  }

  public static ComboBox createServicesComboBox(String caption) {
    BeanItemContainer<Service> objects = getContainer();
    ComboBox comboBox = new ComboBox(caption, objects);
    comboBox.setItemCaptionPropertyId(NAME);
    comboBox.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
    comboBox.setRequired(true);
    comboBox.setWidth("200px");
    return comboBox;
  }

  private static BeanItemContainer<Service> getContainer() {
    ManagerService managerService = ManagerService.get();

    List<Service> apServices = managerService.getApServices(0, managerService.getApServiceCount(null),
      new String[]{NAME}, new boolean[]{true}, null);

    return new BeanItemContainer<Service>(Service.class, apServices);
  }

  private TextField createTextField(String caption, boolean required, boolean enabled) {
    TextField field = new TextField(caption);
    field.setRequired(required);
    field.setEnabled(enabled);
    return field;
  }


  @Override
  public void containerItemSetChange(ItemSetChangeEvent event) {
    ComboBox serviceCombobox = (ComboBox) createUpdateForm.getField(SERVICE_ID);
    serviceCombobox.removeAllItems();
    ManagerService managerService = ManagerService.get();

    List<Service> apServices = managerService.getApServices(0, managerService.getApServiceCount(null),
      new String[]{ProcedureForm.NAME}, new boolean[]{true}, null);

    BeanItemContainer<Service> objects = new BeanItemContainer<Service>(Service.class, apServices);

    serviceCombobox.setContainerDataSource(objects);

  }

  public void clean() {
    changer.change(clean);
    editProcButton.setVisible(false);
  }


}
