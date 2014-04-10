/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
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
    setTheme("runo");

    TabSheet t = new TabSheet();
    t.addStyleName("borderless");
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
    Component certValidatePreference = createCertVerifyParamsEditor();
    t.addTab(certValidatePreference, "Настройки");
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
    tabSheet.addStyleName("light");
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
    Panel panel3 = new Panel("Журналирование");
    final Form form = new Form();
    final TextField tf = new TextField("Хранить логи, дн.");
    tf.setRequired(true);
    form.addField(API.LOG_DEPTH, tf);
    String logDepth = AdminServiceProvider.get().getSystemProperty(API.LOG_DEPTH);
    if (logDepth != null && logDepth.matches("[1-9][0-9]*")) {
      tf.setValue(logDepth);
    } else {
      tf.setValue(String.valueOf(API.DEFAULT_LOG_DEPTH));
    }
    tf.addValidator(new Validator() {

      private static final long serialVersionUID = 1L;

      public void validate(Object value) throws InvalidValueException {
        if (!isValid(value)) {
          throw new InvalidValueException("Введите положительное числовое значение");
        }
      }

      public boolean isValid(Object value) {
        if (value == null || !(value instanceof String)) {
          return false;
        }

        return ((String) value).matches("[1-9][0-9]*");
      }
    });
    panel3.setSizeFull();
    Button b = new Button("Применить", new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        try {
          form.commit();
          AdminServiceProvider.get().saveSystemProperty(API.LOG_DEPTH, tf.getValue().toString());
          event.getButton().getWindow().showNotification("Настройки сохранены", Window.Notification.TYPE_HUMANIZED_MESSAGE);
        } catch (Exception e) {
          //
        }
      }
    });
    panel3.addComponent(form);
    panel3.addComponent(b);

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

    panel1.addStyleName("light");
    panel2.addStyleName("light");
    panel3.addStyleName("light");
    panel4.addStyleName("light");

    layout.addComponent(panel1);
    layout.addComponent(panel2);
    layout.addComponent(panel3);
    layout.addComponent(panel4);
    layout.setExpandRatio(panel1, 0.3f);
    layout.setExpandRatio(panel2, 0.2f);
    layout.setExpandRatio(panel3, 0.2f);
    layout.setExpandRatio(panel4, 0.2f);
    layout.setMargin(true);
    layout.setSpacing(true);
    return layout;
  }

}
