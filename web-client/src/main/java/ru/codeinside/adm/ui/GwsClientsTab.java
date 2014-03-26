/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.API;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryContainer;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gws.api.Revision;

import java.util.logging.Logger;

public class GwsClientsTab extends HorizontalLayout implements TabSheet.SelectedTabChangeListener {

  final GwsClientsTable gwsClientsTable;
  final ComboBox infosys;
  final ActiveGwsClientsTable activeGwsClientsTable;
  final Table serviceUnavailableTable;
  final UnavailableServiceQ.Factory unavailableQF;
  final LazyQueryContainer unavailableСontainer;
  final GwsClientSink sink;
  final Button removeButton;
  final CheckBox logEnabled;

  String currentName;
  String currentVersion;


  GwsClientsTab() {
    String fieldWidth = "300px";

    InfoSysQ query = new InfoSysQ();
    LazyQueryContainer container = new LazyQueryContainer(query, query.getFactory());
    infosys = new ComboBox("Информационная система", container);
    infosys.setWidth(fieldWidth);
    infosys.setItemCaptionPropertyId("name");
    infosys.setDescription("Выберите ИС поставщика, чтобы связать сервис с ней");
    infosys.setInvalidAllowed(true);
    infosys.setInvalidCommitted(false);
    infosys.setRequired(true);
    infosys.setRequiredError("Информационная система - обязательно к заполнению");


    final TextField id = text("Id", fieldWidth, false, false, null);
    final TextField address = text("Адрес", fieldWidth, true, true, "Адрес HTTP подключения к сервису поставщика");
    final TextField revision = text("Ревизия", fieldWidth, false, true, "Ревизия методических рекомендаций СМЭВ");
    final TextField sname = text("Имя", fieldWidth, false, true, "Имя, по которому осуществляться вызов в маршрутах");
    final TextField sversion = text("Версия", fieldWidth, false, true, null);
    final TextField name = text("Описание", fieldWidth, true, true, null);
    final CheckBox available = new CheckBox("Доступен в маршрутах");
    logEnabled = new CheckBox("Вести журнал сообщений");
    logEnabled.setReadOnly(!Boolean.TRUE.equals(AdminServiceProvider.getBoolProperty(API.ENABLE_CLIENT_LOG)));

    gwsClientsTable = new GwsClientsTable();

    final Form form = new Form();
    form.addField("id", id);
    form.addField("sname", sname);
    form.addField("sversion", sversion);
    form.addField("revision", revision);
    form.addField("infosys", infosys);
    form.addField("name", name);
    form.addField("address", address);
    form.addField("available", available);
    form.addField("log", logEnabled);
    form.setEnabled(false);
    form.setWriteThrough(false);


    final Button commit = new Button("Сохранить", new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        try {
          form.commit();
        } catch (Validator.InvalidValueException e) {
          return;
        }
        String infosysCode = (String) infosys.getContainerProperty(infosys.getValue(), "code").getValue();
        String sversionValue = (String) sversion.getValue();
        String snameValue = (String) sname.getValue();
        Long entityId = (Long) id.getValue();

        boolean serviceEnabled = Boolean.TRUE == available.getValue();
        boolean _logEnabled = Boolean.TRUE == logEnabled.getValue();
        String revisionName = ((Revision) revision.getValue()).name();
        String description = (String) name.getValue();
        String url = (String) address.getValue();

        if (entityId != null) {
          AdminServiceProvider
            .get()
            .updateInfoSystemService(entityId.toString(),
              infosysCode, url, revisionName, snameValue, sversionValue, description, serviceEnabled, _logEnabled
            );
          AdminServiceProvider
            .get()
            .createLog(Flash.getActor(), "InfoSystemService", entityId.toString(),
              "update", "Update from adm interface", true);
        } else {
          // TODO: strange method name
          if (AdminServiceProvider.get().findUsesInfoSystemService(snameValue, sversionValue)) {
            Long infoSystemService = AdminServiceProvider.get().createInfoSystemService(
              infosysCode, url, revisionName, snameValue, sversionValue, description, serviceEnabled, _logEnabled
            );
            id.setValue(infoSystemService);
            removeButton.setEnabled(true);
            AdminServiceProvider
              .get()
              .createLog(Flash.getActor(), "InfoSystemService", infoSystemService.toString(),
                "create", "Create from adm interface", true);
          } else {
            getWindow()
              .showNotification("Комбинация OSGI-имени и OSGI-версии должна быть уникальной",
                Window.Notification.TYPE_ERROR_MESSAGE);
            return;
          }
        }
        activeGwsClientsTable.setCurrent(currentName, currentVersion);
        gwsClientsTable.setCurrent(currentName, currentVersion, true);
      }
    });

    final Button clean = new Button("Очистить", new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        disableForm();
      }
    });

    removeButton = new Button("Удалить", new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        Long _id = (Long) id.getValue();
        AdminServiceProvider.get().removeInfoSystemService(_id);
        disableForm();
      }
    });


    final HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    buttons.setWidth(100, UNITS_PERCENTAGE);
    buttons.addComponent(clean);
    buttons.addComponent(commit);
    buttons.addComponent(removeButton);
    buttons.setComponentAlignment(clean, Alignment.MIDDLE_LEFT);
    buttons.setComponentAlignment(commit, Alignment.MIDDLE_CENTER);
    buttons.setComponentAlignment(removeButton, Alignment.MIDDLE_RIGHT);
    form.setFooter(buttons);

    activeGwsClientsTable = new ActiveGwsClientsTable();

    VerticalLayout right = new VerticalLayout();
    right.setSizeFull();
    right.setSpacing(true);


    HorizontalLayout newContent = new HorizontalLayout();
    newContent.setMargin(true);
    newContent.setSpacing(true);
    newContent.setSizeFull();
    newContent.addComponent(form);

    UnavailableServiceQ unavailableQuery = new UnavailableServiceQ();
    unavailableQF = unavailableQuery.getFactory();
    unavailableСontainer = new LazyQueryContainer(unavailableQuery, unavailableQF);

    serviceUnavailableTable = new Table("Недоступность сервиса:", unavailableСontainer);
    serviceUnavailableTable.setSizeFull();
    serviceUnavailableTable.setPageLength(6);
    serviceUnavailableTable.setWidth("100%");
    serviceUnavailableTable.setColumnHeaders(new String[]{"Название сервиса", "Адрес", "Дата-время"});
    serviceUnavailableTable.setSortDisabled(true);

    final VerticalLayout layout = new VerticalLayout();
    layout.setSizeFull();
    layout.setSpacing(true);
    layout.addComponent(serviceUnavailableTable);
    layout.setExpandRatio(serviceUnavailableTable, 1f);
    newContent.addComponent(layout);


    right.addComponent(gwsClientsTable);
    right.addComponent(createLogPanel());
    right.addComponent(newContent);
    right.setExpandRatio(newContent, 0.6f);
    right.setExpandRatio(gwsClientsTable, 0.4f);

    setSpacing(true);
    setSizeFull();

    addComponent(activeGwsClientsTable);
    addComponent(right);
    setExpandRatio(activeGwsClientsTable, 0.15f);
    setExpandRatio(right, 0.85f);
    setMargin(true);

    sink = new GwsClientSink() {
      @Override
      public void selectClient(Long _id, Revision _revision, String _url, String _componentName, String _version,
                               String _infoSys, String _description, Boolean _available, Boolean _logEnabled) {

        // re-routing
        if (_id == null && gwsClientsTable.setCurrent(_componentName, _version, false)) {
          return;
        }

        currentName = _componentName;
        currentVersion = _version;

        boolean enabled = _componentName != null;
        form.setEnabled(enabled);
        form.setValidationVisible(false);
        buttons.setEnabled(enabled);
        removeButton.setEnabled(_id != null);

        id.setValue(_id);
        infosys.setValue(findInfoSystem(infosys, _infoSys));
        address.setValue(_url);
        revision.setValue(_revision);
        sname.setValue(_componentName);
        sversion.setValue(_version);
        name.setValue(_description);
        available.setValue(_available);
        logEnabled.setReadOnly(false);
        logEnabled.setValue(_logEnabled);
        logEnabled.setReadOnly(!Boolean.TRUE.equals(AdminServiceProvider.getBoolProperty(API.ENABLE_CLIENT_LOG)));

        activeGwsClientsTable.setCurrent(_componentName, _version);
        serviceUnavailableTable.setEnabled(_id != null && _id > 0);
        unavailableQF.setInfoSystemId(_id);
        unavailableСontainer.refresh();
      }
    };
    gwsClientsTable.setSink(sink);
    activeGwsClientsTable.setSink(sink);
  }

  private void disableForm() {
    currentName = null;
    currentVersion = null;
    sink.selectClient(null, null, null, null, null, null, null, null, null);
  }

  @Override
  public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
    if (this == event.getTabSheet().getSelectedTab()) {
      activeGwsClientsTable.setCurrent(currentName, currentVersion);
      gwsClientsTable.setCurrent(currentName, currentVersion, true);
      unavailableСontainer.refresh();
    }
  }

  Object findInfoSystem(Container.Viewer viewer, String code) {
    if (code != null) {
      Container container = viewer.getContainerDataSource();
      for (Object itemId : container.getItemIds()) {
        if (code.equals(container.getContainerProperty(itemId, "code").getValue())) {
          return itemId;
        }
      }
    }
    return null;
  }

  private Component createLogPanel() {
    CheckBox clientLogEnabled = new CheckBox(
      "Вести журнал сообщений, в зависимости от настроек модулей",
      AdminServiceProvider.getBoolProperty(API.ENABLE_CLIENT_LOG)
    );
    clientLogEnabled.setImmediate(true);
    clientLogEnabled.addListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        boolean value = Boolean.TRUE.equals(event.getProperty().getValue());
        AdminServiceProvider.get().saveSystemProperty(API.ENABLE_CLIENT_LOG, Boolean.toString(value));
        //TODO: почему анонимный?
        Logger.getAnonymousLogger().info("Журналировать потребителей СМЭВ:" + value + " " + Flash.login());
        logEnabled.setReadOnly(!Boolean.TRUE.equals(AdminServiceProvider.getBoolProperty(API.ENABLE_CLIENT_LOG)));
      }
    });
    return clientLogEnabled;
  }

  TextField text(String name, String fieldWidth, boolean enabled, boolean required, String description) {
    final TextField field = new TextField(name);
    field.setImmediate(true);
    field.setNullRepresentation("");
    field.setWidth(fieldWidth);
    field.setMaxLength(255);
    field.setEnabled(enabled);
    field.setDescription(description);
    if (required) {
      String msg = name + " - обязательно к заполнению";
      field.setRequired(true);
      field.setRequiredError(msg);
      field.addValidator(new AbstractValidator(msg) {
        @Override
        public boolean isValid(Object value) {
          return value != null && !value.toString().trim().isEmpty();
        }
      });
    }
    return field;
  }

}
