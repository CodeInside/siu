/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.*;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.themes.Reindeer;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.adm.AdminService;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.ui.employee.EmployeeWidget;
import ru.codeinside.gses.API;
import ru.codeinside.gses.webui.CertificateVerifier;
import ru.codeinside.gses.webui.DelegateCloseHandler;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.components.UserInfoPanel;
import ru.codeinside.gses.webui.osgi.Activator;

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
    t.addStyleName(Reindeer.TABSHEET_MINIMAL);
    t.setSizeFull();
    t.setCloseHandler(new DelegateCloseHandler());
    UserInfoPanel.addClosableToTabSheet(t, getUser().toString());
    TreeTableOrganization treeTableOrganization = new TreeTableOrganization();
    CrudNews showNews = new CrudNews();
    table = treeTableOrganization.getTreeTable();
    Tab orgsTab = t.addTab(treeTableOrganization, "Организации");
    t.setSelectedTab(orgsTab);
    t.addTab(new GroupTab(), "Группы");

    GwsSystemTab systemTab = new GwsSystemTab();
    t.addTab(systemTab, "Информационные системы");
    t.addListener(systemTab);

    GwsLazyTab gwsLazyTab = new GwsLazyTab();
    t.addTab(gwsLazyTab, "Сервисы информационных систем");
    t.addListener(gwsLazyTab);

    t.addTab(createEmployeeWidget(), "Пользователи");
    RefreshableTab settings = createSettings();
    t.addTab(settings, "Настройки");
    t.addListener(settings);

    Component businessCalendar = createBusinessCalendar();
    t.addTab(businessCalendar, "Производственный календарь");

    LogTab logTab = new LogTab();
    t.addListener(logTab);
    t.addTab(logTab, "Логи");
    t.addTab(showNews, "Новости");
    t.addTab(registryTab(), "Реестр");
    setMainWindow(new Window(getUser() + " | Управление | СИУ-" + Activator.getContext().getBundle().getVersion(), t));
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
    tabSheet.addStyleName(Reindeer.TABSHEET_MINIMAL);
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

  private Component createBusinessCalendar() {
    return new BusinessCalendar();
  }

  void addOption(AbstractSelect select, String id, String caption, boolean autoSelect) {
    id = StringUtils.trimToNull(id);
    caption = StringUtils.trimToNull(caption);
    if (id != null) {
      if (select.getItem(id) == null) {
        select.addItem(id);
        select.setItemCaption(id, caption);
      }
      if (autoSelect) {
        select.setValue(id);
      }
    }
  }

  private RefreshableTab createSettings() {

    final Form systemForm;
    {
      final ComboBox serviceLocation;
      {
        String[][] defs = {
            {"Тестовый контур", "http://195.245.214.33:7777/esv"},
            {"Производственный контур", "http://oraas.rt.ru:7777/gateway/services/SID0003318"}
        };
        serviceLocation = new ComboBox("Адрес сервиса проверки");
        serviceLocation.setItemCaptionMode(ComboBox.ITEM_CAPTION_MODE_EXPLICIT);
        for (String[] def : defs) {
          addOption(serviceLocation, def[1], def[0], false);
        }
        serviceLocation.setImmediate(true);
        serviceLocation.setInputPrompt("http://");
        serviceLocation.setNewItemsAllowed(true);
        serviceLocation.setNewItemHandler(new AbstractSelect.NewItemHandler() {
          @Override
          public void addNewItem(String newItemCaption) {
            addOption(serviceLocation, newItemCaption, newItemCaption, true);
          }
        });
        String href = AdminServiceProvider.get().getSystemProperty(CertificateVerifier.VERIFY_SERVICE_LOCATION);
        addOption(serviceLocation, href, href, true);
      }

      final CheckBox allowValidate;
      {
        allowValidate = new CheckBox("Проверка сертификатов разрешена");
        allowValidate.setRequired(true);
        allowValidate.setImmediate(true);
        allowValidate.addListener(new Property.ValueChangeListener() {
          @Override
          public void valueChange(Property.ValueChangeEvent event) {
            serviceLocation.setRequired(Boolean.TRUE.equals(event.getProperty().getValue()));
          }
        });
        allowValidate.setValue(AdminServiceProvider.getBoolProperty(CertificateVerifier.ALLOW_VERIFY_CERTIFICATE_PROPERTY));
      }

      systemForm = new Form();
      systemForm.addField("location", serviceLocation);
      systemForm.addField("allowVerify", allowValidate);
      systemForm.setImmediate(true);
      systemForm.setWriteThrough(false);
      systemForm.setInvalidCommitted(false);

      Button commit = new Button("Изменить", new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent event) {
          try {
            systemForm.commit();
            AdminServiceProvider.get().saveSystemProperty(CertificateVerifier.VERIFY_SERVICE_LOCATION, (String) serviceLocation.getValue());
            AdminServiceProvider.get().saveSystemProperty(CertificateVerifier.ALLOW_VERIFY_CERTIFICATE_PROPERTY, allowValidate.getValue().toString());
            event.getButton().getWindow().showNotification("Настройки сохранены", Window.Notification.TYPE_HUMANIZED_MESSAGE);
          } catch (Validator.InvalidValueException ignore) {
          }
        }
      });

      HorizontalLayout buttons = new HorizontalLayout();
      buttons.setSpacing(true);
      buttons.addComponent(commit);
      systemForm.getFooter().addComponent(buttons);
    }

    Panel panel1 = new Panel("Проверка сертификатов");
    panel1.setSizeFull();
    panel1.addComponent(systemForm);

    Panel panel2 = new Panel("Привязка сертификатов");
    panel2.setSizeFull();
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
    panel2.addComponent(switchLink);


    CheckBox productionMode = new CheckBox(
        "Производственный режим СМЭВ", AdminServiceProvider.getBoolProperty(API.PRODUCTION_MODE)
    );
    productionMode.setImmediate(true);
    productionMode.setDescription("В производственном режиме в запросах к внешним сервисам не будет передаваться testMsg");
    productionMode.addListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        boolean value = Boolean.TRUE.equals(event.getProperty().getValue());
        AdminServiceProvider.get().saveSystemProperty(API.PRODUCTION_MODE, Boolean.toString(value));
      }
    });
    Panel panel4 = new Panel("Режим СМЭВ");
    panel4.setSizeFull();
    panel4.addComponent(productionMode);

    LogSettings logSettings = new LogSettings();

    final VerticalLayout layout = new VerticalLayout();
    layout.setSpacing(true);
    layout.setSizeFull();
    layout.addComponent(panel1);
    layout.addComponent(panel2);
    layout.addComponent(logSettings);
    layout.addComponent(panel4);
    layout.setExpandRatio(panel1, 0.25f);
    layout.setExpandRatio(panel2, 0.10f);
    layout.setExpandRatio(logSettings, 0.55f);
    layout.setExpandRatio(panel4, 0.10f);
    layout.setMargin(true);
    layout.setSpacing(true);

    return new RefreshableTab(layout, logSettings);
  }

}
