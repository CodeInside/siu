/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
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

import java.util.List;

public class AdminApp extends Application {

  private static final long serialVersionUID = 1L;

  TreeTable table;

  @SuppressWarnings("unused") // Do NOT remove (used in Vaadin reflection API)!
  public static SystemMessages getSystemMessages() {
    CustomizedSystemMessages messages = new CustomizedSystemMessages();
    messages.setSessionExpiredNotificationEnabled(false);
    messages.setCommunicationErrorNotificationEnabled(false);
    return messages;
  }

  @Override
  public void init() {
    setUser(Flash.login());
    setTheme("custom");
    TabSheet t = new TabSheet();
    t.setSizeFull();
    t.setCloseHandler(new DelegateCloseHandler());
    UserInfoPanel.addClosableToTabSheet(t, getUser().toString());
    TreeTableOrganization treeTableOrganization = new TreeTableOrganization();
    CrudNews showNews = new CrudNews();
    table = treeTableOrganization.getTreeTable();
    Tab orgsTab = t.addTab(treeTableOrganization, "Организации");
    t.setSelectedTab(orgsTab);
    t.addTab(new GroupTab(), "Группы");
    Component infoSystemEditor = createInfoSystemEditor();
    t.addTab(infoSystemEditor, "Информационные системы");

    GwsLazyTab gwsLazyTab = new GwsLazyTab();
    t.addTab(gwsLazyTab, "Сервисы информационных систем");
    t.addListener(gwsLazyTab);

    t.addTab(createEmployeeWidget(), "Пользователи");
    Component certValidatePreference = createCertVerifyParamsEditor();
    t.addTab(certValidatePreference, "Сертификаты");
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
      try {
        adminService.createLog(Flash.getActor(), "Admin application", (String) getUser(), "logout", null, true);
      } catch (Exception ignore) {
        // возможен вызов после того, как jee контейнер будет в состоянии UnDeployed.
      }
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
    Panel upperPanel = new Panel("Проверка сертификатов");
    upperPanel.setSizeFull();
    upperPanel.addComponent(systemForm);
    Panel lowerPanel = new Panel("Привязка сертификатов");
    lowerPanel.setSizeFull();
    boolean linkCertificate = AdminServiceProvider.getBoolProperty(CertificateVerifier.LINK_CERTIFICATE);
    final CheckBox switchLink = new CheckBox("Привязка включена");
    switchLink.setValue(linkCertificate);
    switchLink.setImmediate(true);
    switchLink.addListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        AdminServiceProvider.get().saveSystemProperty(CertificateVerifier.LINK_CERTIFICATE, switchLink.getValue().toString());
        event.getButton().getWindow().showNotification("Настройки сохранены", Window.Notification.TYPE_HUMANIZED_MESSAGE);
      }
    });
    lowerPanel.addComponent(switchLink);
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
  }
}
