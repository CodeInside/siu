/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.*;
import com.vaadin.ui.TabSheet.Tab;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.adm.AdminService;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.InfoSystem;
import ru.codeinside.adm.ui.employee.EmployeeWidget;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryContainer;
import ru.codeinside.gses.webui.CertificateVerifier;
import ru.codeinside.gses.webui.DelegateCloseHandler;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.components.UserInfoPanel;
import ru.codeinside.gses.webui.gws.TRef;
import ru.codeinside.gses.webui.osgi.LogCustomizer;
import ru.codeinside.gws.api.*;

import javax.xml.namespace.QName;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class AdminApp extends Application {

  private static final long serialVersionUID = 1L;
  private UserInfoPanel userInfoPanel;
  private ComboBox infosys;
  private TreeTable table;
  private Table registerSevice;

/*  public static SystemMessages getSystemMessages() {
    CustomizedSystemMessages messages = new CustomizedSystemMessages();
    messages.setSessionExpiredNotificationEnabled(false);
    messages.setCommunicationErrorNotificationEnabled(false);
    return messages;
  }

  public UserInfoPanel getUserInfoPanel() {
    return this.userInfoPanel;
  }*/

  @Override
  public void init() {
    setUser(Flash.login());
    setTheme("custom");
    TabSheet t = new TabSheet();
    t.setSizeFull();
    t.setCloseHandler(new DelegateCloseHandler());
    userInfoPanel = UserInfoPanel.addClosableToTabSheet(t, getUser().toString());
    TreeTableOrganization treeTableOrganization = new TreeTableOrganization();
    CrudNews showNews = new CrudNews();
    table = treeTableOrganization.getTreeTable();
    Tab orgsTab = t.addTab(treeTableOrganization, "Организации");
    t.setSelectedTab(orgsTab);
    t.addTab(new GroupTab(), "Группы");
    Component infoSystemEditor = createInfoSystemEditor();
    t.addTab(infoSystemEditor, "Информационные системы");
    Component infoSystemServiceEditor = createInfoSystemServiceEditor();
    t.addTab(infoSystemServiceEditor, "Сервисы информационных систем");
    t.addTab(createEmployeeWidget(), "Пользователи");
    Component certValidatePreference = createCertVerifyParamsEditor();
    t.addTab(certValidatePreference, "Проверка сертификатов");
    LogTab logTab = new LogTab();
    t.addListener(logTab);
    t.addTab(logTab, "Логи");
    t.addTab(showNews, "Новости");
    t.addTab(registryTab(), "Реестр");
    setMainWindow(new Window("СИУ(" + getUser() + ")", t));
    AdminServiceProvider.get().createLog(Flash.getActor(), "Admin application", (String) getUser(), "login", null, true);
  }

  private Component registryTab() {
    Embedded embedded = new Embedded("", new ExternalResource("/registry"));
    embedded.setType(Embedded.TYPE_BROWSER);
    embedded.setWidth("100%");
    embedded.setHeight("100%");
    return embedded;
  }

  @Override
  public void close() {
    final AdminService adminService = AdminServiceProvider.tryGet();
    if (adminService != null) {
      adminService.createLog(Flash.getActor(), "Admin application", (String) getUser(), "logout", null, true);
    }
    super.close();
  }

  private Component createEmployeeWidget() {
    final TabSheet tabSheet = new TabSheet();
    tabSheet.setSizeFull();
    tabSheet.addTab(new EmployeeWidget(false, table), "Активные");
    tabSheet.addTab(new EmployeeWidget(true, table), "Заблокированные");
    tabSheet.addListener(new TabSheet.SelectedTabChangeListener() {

      @Override
      public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
        EmployeeWidget currentTab = (EmployeeWidget) tabSheet.getSelectedTab();
        currentTab.refreshList();
      }
    });
    return tabSheet;
  }

  private Component createInfoSystemEditor() {

    String width = "300px";
    final TextField code = new TextField("Код", "");
    code.setMaxLength(255);
    code.setWidth(width);
    final TextField name = new TextField("Название", "");
    name.setMaxLength(255);
    name.setWidth(width);

    final InfoSysQ query = new InfoSysQ();
    final LazyQueryContainer container = new LazyQueryContainer(query, query.getFactory());

    final Table listInfoSystem = new Table("Список:", container);
    listInfoSystem.setSizeFull();
    listInfoSystem.setPageLength(0);
    listInfoSystem.setColumnHeaders(new String[]{"Код", "Название"});
    listInfoSystem.setSelectable(true);
    listInfoSystem.addListener(new ItemClickEvent.ItemClickListener() {
      @Override
      public void itemClick(ItemClickEvent event) {
        Item item = event.getItem();
        code.setEnabled(false);
        code.setValue(item.getItemProperty("code").getValue());
        name.setValue(item.getItemProperty("name").getValue());
      }
    });

    final Form systemForm = new Form();
    code.setRequired(true);
    name.setRequired(true);
    systemForm.addField("code", code);
    systemForm.addField("name", name);
    systemForm.setWriteThrough(false);
    systemForm.setInvalidCommitted(false);

    final Button commit = new Button("Сохранить");

    commit.addListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        try {
          systemForm.commit();
          AdminServiceProvider.get().createInfoSystem((String) code.getValue(), (String) name.getValue());
          AdminServiceProvider.get().createLog(Flash.getActor(), "InfoSystem", (String) code.getValue(),
              "create/update",
              "value => " + name.getValue(), true);
          cleanFields(code, name);
          code.setEnabled(true);
          systemForm.setValidationVisible(false);
          container.refresh();
          refreshInfoSystemsCombo();
          listInfoSystem.setValue(null);
        } catch (Validator.InvalidValueException e) {
          //
        }
      }

    });
    final Button clean = new Button("Очистить");

    clean.addListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        systemForm.setValidationVisible(false);
        cleanFields(code, name);
        listInfoSystem.setValue(null);
      }
    });

    final VerticalLayout layout = new VerticalLayout();
    layout.setSpacing(true);
    layout.setSizeFull();

    final HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    buttons.addComponent(commit);
    buttons.addComponent(clean);

    systemForm.getFooter().addComponent(buttons);
    Panel upperPanel = new Panel();
    upperPanel.setSizeFull();
    upperPanel.addComponent(systemForm);
    Panel lowerPanel = new Panel();
    lowerPanel.setSizeFull();
    lowerPanel.addComponent(listInfoSystem);
    layout.addComponent(upperPanel);
    layout.addComponent(lowerPanel);
    layout.setExpandRatio(upperPanel, 0.3f);
    layout.setExpandRatio(lowerPanel, 0.7f);
    layout.setMargin(true);
    layout.setSpacing(true);
    return layout;
  }

  private Component createCertVerifyParamsEditor() {
    String serviceLocationValue = AdminServiceProvider.get().getSystemProperty(CertificateVerifier.VERIFY_SERVICE_LOCATION);
    boolean allowVerify = AdminServiceProvider.getBoolProperty(CertificateVerifier.ALLOW_VERIFY_CERTIFICATE_PROPERTY);
    String width = "300px";
    final TextField serviceLocation = new TextField("Адрес сервиса проверки", StringUtils.defaultString(serviceLocationValue));
    serviceLocation.setMaxLength(1024);
    serviceLocation.setWidth(width);
    final CheckBox allowValidate = new CheckBox("Проверка сертификатов разрешена");
    allowValidate.setValue(allowVerify);

    final Form systemForm = new Form();
    serviceLocation.setRequired(true);
    allowValidate.setRequired(true);
    systemForm.addField("location", serviceLocation);
    systemForm.addField("allowVerify", allowValidate);
    systemForm.setWriteThrough(false);
    systemForm.setInvalidCommitted(false);

    final Button commit = new Button("Изменить");

    commit.addListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
       AdminServiceProvider.get().saveSystemProperty(CertificateVerifier.VERIFY_SERVICE_LOCATION, serviceLocation.getValue().toString());
       AdminServiceProvider.get().saveSystemProperty(CertificateVerifier.ALLOW_VERIFY_CERTIFICATE_PROPERTY, allowValidate.getValue().toString());
       event.getButton().getWindow().showNotification("Настройки сохранены", Window.Notification.TYPE_HUMANIZED_MESSAGE);
      }

    });

    final VerticalLayout layout = new VerticalLayout();
    layout.setSpacing(true);
    layout.setSizeFull();

    final HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    buttons.addComponent(commit);

    systemForm.getFooter().addComponent(buttons);
    Panel upperPanel = new Panel();
    upperPanel.setSizeFull();
    upperPanel.addComponent(systemForm);
    Panel lowerPanel = new Panel();
    lowerPanel.setSizeFull();
    layout.addComponent(upperPanel);
    layout.addComponent(lowerPanel);
    layout.setExpandRatio(upperPanel, 0.3f);
    layout.setExpandRatio(lowerPanel, 0.7f);
    layout.setMargin(true);
    layout.setSpacing(true);
    return layout;
  }

  private void cleanFields(final TextField code, final TextField name) {
    code.setValue("");
    code.setEnabled(true);
    name.setValue("");
  }

  private void refreshInfoSystemsCombo() {
    AdminService adminService = AdminServiceProvider.get();
    List<InfoSystem> apServices = adminService.queryInfoSystems(new String[]{"name"}, new boolean[]{true}, 0, adminService.countInfoSystems());
    BeanItemContainer<InfoSystem> objects = new BeanItemContainer<InfoSystem>(InfoSystem.class, apServices);
    infosys.setContainerDataSource(objects);
  }

  private Component createInfoSystemServiceEditor() {
    String fieldWidth = "300px";

    AdminService adminService = AdminServiceProvider.get();
    List<InfoSystem> apServices = adminService.queryInfoSystems(new String[]{"name"}, new boolean[]{true}, 0, adminService.countInfoSystems());
    BeanItemContainer<InfoSystem> objects = new BeanItemContainer<InfoSystem>(InfoSystem.class, apServices);
    infosys = new ComboBox("Информационная система");
    infosys.setContainerDataSource(objects);
    infosys.setWidth(fieldWidth);
    infosys.setItemCaptionPropertyId("name");
    infosys.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);

    final TextField id = new TextField("Id", "");
    id.setWidth(fieldWidth);
    id.setEnabled(false);

    final TextField address = new TextField("Адрес", "");
    address.setMaxLength(255);
    address.setWidth(fieldWidth);
    final ComboBox revision = new ComboBox("Ревизия");
    revision.setWidth(fieldWidth);
    revision.setNullSelectionAllowed(false);
    revision.addItem(Revision.rev110801);
    revision.addItem(Revision.rev111111);
    revision.addItem(Revision.rev120315);

    final TextField sname = new TextField("Osgi имя", "");
    sname.setMaxLength(255);
    sname.setWidth(fieldWidth);
    final TextField sversion = new TextField("Osgi версия", "");
    sversion.setMaxLength(255);
    sversion.setWidth(fieldWidth);
    final TextField name = new TextField("Наименование", "");
    name.setMaxLength(255);
    name.setWidth(fieldWidth);
    final CheckBox available = new CheckBox("Доступен");

    final InfoSysServiceQ query = new InfoSysServiceQ();
    final LazyQueryContainer container = new LazyQueryContainer(query, query.getFactory());

    registerSevice = new Table("Зарегистрированные в системе сервисы:", container);
    registerSevice.setSizeFull();
    registerSevice.setPageLength(6);
    registerSevice.setColumnHeaders(new String[]{"Id", "Информационная система", "Адрес", "Ревизия", "Osgi имя", "Osgi версия", "Наименование", "Доступен"});
    registerSevice.setSelectable(true);
    registerSevice.setColumnExpandRatio("id", 0.01f);
    registerSevice.setColumnExpandRatio("infosys", 0.1f);
    registerSevice.setColumnExpandRatio("address", 0.1f);
    registerSevice.setColumnExpandRatio("available", 0.05f);
    registerSevice.addListener(new ItemClickEvent.ItemClickListener() {
      @Override
      public void itemClick(ItemClickEvent event) {
        Item item = event.getItem();
        id.setValue(item.getItemProperty("id").getValue());
        Object value = item.getItemProperty("infosys").getValue();
        infosys.setValue(getItem(infosys, value.toString()));
        address.setValue(item.getItemProperty("address").getValue());
        revision.setValue(Revision.valueOf(item.getItemProperty("revision").getValue().toString()));
        sname.setValue(item.getItemProperty("sname").getValue());
        sversion.setValue(item.getItemProperty("sversion").getValue());
        name.setValue(item.getItemProperty("name").getValue());
        available.setValue(item.getItemProperty("available").getValue());
      }
    });

    final Form masterSystemForm = new Form();
    infosys.setRequired(true);
    revision.setRequired(true);
    sname.setRequired(true);
    sversion.setRequired(true);
    name.setRequired(true);
    available.setRequired(true);
    masterSystemForm.setWriteThrough(false);
    masterSystemForm.setInvalidCommitted(false);

    masterSystemForm.addField("id", id);
    masterSystemForm.addField("infosys", infosys);
    masterSystemForm.addField("name", name);
    masterSystemForm.addField("revision", revision);
    masterSystemForm.addField("address", address);
    masterSystemForm.addField("sname", sname);
    masterSystemForm.addField("sversion", sversion);
    masterSystemForm.addField("available", available);

    masterSystemForm.getField("infosys").setDescription("Выберите ИС поставщика, чтобы связать сервис с ней");
    masterSystemForm.getField("name").setDescription("Введите внутренне наименование сервиса");
    masterSystemForm.getField("revision").setDescription("Выберите используемую сервисом ревизию СМЭВ");
    masterSystemForm.getField("address").setDescription("Введите точку подключения к сервису поставщика");
    masterSystemForm.getField("sname").setDescription("Введите имя сервиса, по которому будет осуществляться его вызов в маршруте");
    masterSystemForm.getField("sversion").setDescription("Введите версию реализации сервиса");

    final Button commit = new Button("Сохранить", new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        if (formContainsEmptyFields(masterSystemForm)) {
          event.getButton().getWindow().showNotification("Форма содержит пустые поля", Window.Notification.TYPE_ERROR_MESSAGE);
          return;
        }
        try {
          masterSystemForm.commit();
        } catch (Validator.InvalidValueException e) {
          event.getButton().getWindow().showNotification("Форма содержит недопустимые значения", Window.Notification.TYPE_ERROR_MESSAGE);
          return;
        }
        String infosysCode = ((InfoSystem) infosys.getValue()).getCode();
        String sversionValue = (String) sversion.getValue();
        String snameValue = (String) sname.getValue();
        String entityId = id.getValue().toString();

        if (StringUtils.isNotEmpty(entityId)) {
          AdminServiceProvider.get().updateInfoSystemService(entityId, infosysCode, (String) address.getValue(), ((Revision) revision.getValue()).name(), snameValue, sversionValue, (String) name.getValue(), (Boolean) available.getValue());
          AdminServiceProvider.get().createLog(Flash.getActor(), "InfoSystemService", entityId, "update", "Update from adm interface", true);
        } else {
          if (AdminServiceProvider.get().findUsesInfoSystemService(snameValue, sversionValue)) {
            Long infoSystemService = AdminServiceProvider.get().createInfoSystemService(infosysCode, (String) address.getValue(), ((Revision) revision.getValue()).name(), snameValue, sversionValue, (String) name.getValue(), (Boolean) available.getValue());
            AdminServiceProvider.get().createLog(Flash.getActor(), "InfoSystemService", infoSystemService.toString(),
                "create", "Create from adm interface", true);
          } else {
            event.getButton().getWindow().showNotification("Комбинация OSGI-имени и OSGI-версии должна быть уникальной", Window.Notification.TYPE_ERROR_MESSAGE);
            return;
          }
        }

        cleanServiceFields(infosys, id, address, revision, sname, sversion, name, available);
        masterSystemForm.setValidationVisible(false);
        container.refresh();
        registerSevice.setValue(null);
      }
    });

    final Button clean = new Button("Очистить");

    clean.addListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        masterSystemForm.setValidationVisible(false);
        cleanServiceFields(infosys, id, address, revision, sname, sversion, name, available);
        registerSevice.setValue(null);
      }
    });

    final HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    buttons.addComponent(commit);
    buttons.addComponent(clean);

    VerticalLayout left = new VerticalLayout();
    left.setSizeFull();
    Panel leftPanel = new Panel();
    leftPanel.setSizeFull();
    left.addComponent(leftPanel);
    Table clientRefList = new Table();
    List<TRef<Client>> clientRefs = AdminServiceProvider.get().getClientRefs();
    clientRefList.addContainerProperty("name", String.class, "");
    clientRefList.addContainerProperty("version", String.class, "");
    clientRefList.setVisibleColumns(new String[]{"name", "version"});
    clientRefList.setColumnHeaders(new String[]{"name", "version"});
    int i = 0;
    for (TRef clientRef : clientRefs) {
      String clientRefName = clientRef.getName();
      String version = clientRef.getVersion();
      clientRefList.addItem(new Object[]{clientRefName, version}, i++);
    }
    clientRefList.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
    clientRefList.setPageLength(0);
    clientRefList.setSelectable(true);
    clientRefList.setStyleName("clickable-item-table");
    clientRefList.setSizeFull();
    clientRefList.addListener(new ItemClickEvent.ItemClickListener() {
      @Override
      public void itemClick(ItemClickEvent event) {
        String name = (String) event.getItem().getItemProperty("name").getValue();
        String version = (String) event.getItem().getItemProperty("version").getValue();
        TRef<Client> ref = AdminServiceProvider.get().getClientRefByNameAndVersion(name, version);
        String revision1 = ref.getRef().getRevision().toString();
        final ServiceDefinitionParser serviceDefinitionParser = AdminServiceProvider.get().getServiceDefinitionParser();
        ServiceDefinition serviceDefinition = serviceDefinitionParser.parseServiceDefinition(ref.getRef().getWsdlUrl());
        String address1 = "";
        for (Map.Entry<QName, ServiceDefinition.Service> service : serviceDefinition.services.entrySet()) {
          for (Map.Entry<QName, ServiceDefinition.Port> port : service.getValue().ports.entrySet()) {
            address1 = port.getValue().soapAddress;
            break;
          }
          break;
        }
        if (!smevRevisionIsSupported(revision1)) {
          getMainWindow().showNotification("Ревизия " + revision1 + " не поддерживается");
        } else {
          String osgiName = ref.getName();
          revision.setValue(Revision.valueOf(revision1));
          address.setValue(address1);
          sname.setValue(osgiName);
          sversion.setValue(version);
        }
      }
    });

    leftPanel.addComponent(new Label("Размещенные в системе сервисы"));
    leftPanel.addComponent(clientRefList);

    VerticalLayout right = new VerticalLayout();
    right.setSizeFull();
    right.setSpacing(true);

    Panel rightUpPanel = new Panel();
    HorizontalLayout newContent = new HorizontalLayout();
    newContent.setMargin(true);
    newContent.setSpacing(true);
    newContent.setSizeFull();

    Panel formPanel = new Panel();
    formPanel.setSizeFull();
    masterSystemForm.getFooter().addComponent(buttons);
    formPanel.addComponent(masterSystemForm);

    rightUpPanel.setContent(newContent);
    rightUpPanel.setSizeFull();
    rightUpPanel.addComponent(formPanel);


    final UnavailableServiceQ unavailableQuery = new UnavailableServiceQ();
    final UnavailableServiceQ.Factory factory = unavailableQuery.getFactory();
    final LazyQueryContainer unavailableСontainer = new LazyQueryContainer(unavailableQuery, factory);

    final Table serviceUnavailableTable = new Table("Недоступность сервиса:", unavailableСontainer);
    serviceUnavailableTable.setSizeFull();
    serviceUnavailableTable.setPageLength(6);
    serviceUnavailableTable.setWidth("100%");
    serviceUnavailableTable.setColumnHeaders(new String[]{"Название сервиса", "Адрес", "Дата-время"});
    serviceUnavailableTable.setSortDisabled(true);

    {
      final VerticalLayout layout = new VerticalLayout();
      layout.setSizeFull();
      layout.setSpacing(true);
      layout.addComponent(createLogPanel());
      layout.addComponent(serviceUnavailableTable);
      layout.setExpandRatio(serviceUnavailableTable, 1f);
      rightUpPanel.addComponent(layout);
    }


    registerSevice.addListener(new ItemClickEvent.ItemClickListener() {
      @Override
      public void itemClick(ItemClickEvent event) {
        Item item = event.getItem();
        factory.setInfoSystemId(Long.parseLong(item.getItemProperty("id").getValue().toString()));
        unavailableСontainer.refresh();
      }
    });


    Panel rightBottomPanel = new Panel();
    rightBottomPanel.setSizeFull();
    rightBottomPanel.addComponent(registerSevice);

    right.addComponent(rightUpPanel);
    right.addComponent(rightBottomPanel);
    right.setExpandRatio(rightUpPanel, 0.6f);
    right.setExpandRatio(rightBottomPanel, 0.4f);


    final HorizontalLayout layout = new HorizontalLayout();
    layout.setSpacing(true);
    layout.setSizeFull();
    layout.addComponent(left);
    layout.addComponent(right);
    layout.setExpandRatio(left, 0.2f);
    layout.setExpandRatio(right, 0.8f);
    layout.setMargin(true);
    return layout;
  }

  private boolean formContainsEmptyFields(Form masterSystemForm) {
    Object infosys = masterSystemForm.getField("infosys").getValue();
    Object name = masterSystemForm.getField("name").getValue();
    Object revision = masterSystemForm.getField("revision").getValue();
    Object address = masterSystemForm.getField("address").getValue();
    Object sname = masterSystemForm.getField("sname").getValue();
    Object sversion = masterSystemForm.getField("sversion").getValue();

    if (infosys != null) {
      if (infosys.toString().trim().isEmpty())
        return true;
    } else {
      return true;
    }

    if (name != null) {
      if (name.toString().trim().isEmpty())
        return true;
    } else {
      return true;
    }

    if (revision != null) {
      if (revision.toString().trim().isEmpty())
        return true;
    } else {
      return true;
    }

    if (address != null) {
      if (address.toString().trim().isEmpty())
        return true;
    } else {
      return true;
    }

    if (sname != null) {
      if (sname.toString().trim().isEmpty())
        return true;
    } else {
      return true;
    }

    if (sversion != null) {
      if (sversion.toString().trim().isEmpty())
        return true;
    } else {
      return true;
    }

    return false;
  }

  private boolean smevRevisionIsSupported(String revision1) {
    return revision1.equals(Revision.rev111111.name()) ||
        revision1.equals(Revision.rev120315.name()) ||
        revision1.equals(Revision.rev110801.name());
  }

  private void cleanServiceFields(final ComboBox infosys, final TextField id, final TextField address,
                                  final ComboBox revision, final TextField sname, final TextField sversion, final TextField name,
                                  final CheckBox available) {
    id.setValue("");
    address.setValue("");
    revision.setValue(null);
    sname.setValue("");
    sversion.setValue("");
    name.setValue("");
    available.setValue(false);
    infosys.setValue(null);
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

  private Component createLogPanel() {
    final VerticalLayout layout = new VerticalLayout();
    layout.setMargin(true);
    layout.setSpacing(true);

    final Panel logPanel = new Panel();
    logPanel.setContent(layout);

    logPanel.addComponent(new Label("Выводить в системный журнал все HTTP пакеты:"));

    CheckBox server = new CheckBox("для поставщиков СМЭВ", AdminServiceProvider.getBoolProperty(LogService.httpAdapterDump));
    server.setImmediate(true);
    server.addListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        boolean value = Boolean.TRUE == event.getProperty().getValue();
        AdminServiceProvider.get().saveSystemProperty(LogService.httpAdapterDump, Boolean.toString(value));
        LogCustomizer.getLogger().setShouldWriteServerLog(value);
        Logger.getAnonymousLogger().info("Журналировать поставщиков СМЭВ:" + value + " " + Flash.login());
      }
    });

    CheckBox client = new CheckBox("для потребителей СМЭВ", AdminServiceProvider.getBoolProperty(LogService.httpTransportPipeDump));
    client.setImmediate(true);
    client.addListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        boolean value = Boolean.TRUE == event.getProperty().getValue();
        AdminServiceProvider.get().saveSystemProperty(LogService.httpTransportPipeDump, Boolean.toString(value));
        LogCustomizer.getLogger().setShouldWriteClientLog(value);
        Logger.getAnonymousLogger().info("Журналировать потребителей СМЭВ:" + value + " " + Flash.login());
      }
    });

    logPanel.addComponent(server);
    logPanel.addComponent(client);
    return logPanel;
  }

}
