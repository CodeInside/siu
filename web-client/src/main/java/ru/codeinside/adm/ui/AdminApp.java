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
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
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
            set(CertificateVerifier.VERIFY_SERVICE_LOCATION, serviceLocation.getValue());
            set(CertificateVerifier.ALLOW_VERIFY_CERTIFICATE_PROPERTY, allowValidate.getValue());
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

    Panel b1 = new Panel();
    b1.setSizeFull();
    Label b1label = new Label("Проверка сертификатов");
    b1label.addStyleName(Reindeer.LABEL_H2);
    b1.addComponent(b1label);
    b1.addComponent(systemForm);

    VerticalLayout certificates = new VerticalLayout();
    certificates.setSizeFull();
    certificates.setSpacing(true);

    HorizontalLayout topHl = new HorizontalLayout();
    topHl.setSizeFull();
    topHl.setSpacing(true);

    Panel panel1 = new Panel("Сертификаты", certificates);
    panel1.setSizeFull();
    panel1.addStyleName(Reindeer.PANEL_LIGHT);

    boolean linkCertificate = AdminServiceProvider.getBoolProperty(CertificateVerifier.LINK_CERTIFICATE);
    final CheckBox switchLink = new CheckBox("Привязка включена");
    switchLink.setValue(linkCertificate);
    switchLink.setImmediate(true);
    switchLink.addListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        set(CertificateVerifier.LINK_CERTIFICATE, switchLink.getValue());
        event.getButton().getWindow().showNotification("Настройки сохранены", Window.Notification.TYPE_HUMANIZED_MESSAGE);
      }
    });

    Panel b2 = new Panel();
    b2.setSizeFull();
    Label b2label = new Label("Привязка сертификатов");
    b2label.addStyleName(Reindeer.LABEL_H2);
    b2.addComponent(b2label);
    b2.addComponent(switchLink);

    certificates.addComponent(b1);
    certificates.addComponent(b2);
    certificates.setExpandRatio(b1, 0.7f);
    certificates.setExpandRatio(b2, 0.3f);

    CheckBox productionMode = new CheckBox(
      "Производственный режим СМЭВ", AdminServiceProvider.getBoolProperty(API.PRODUCTION_MODE)
    );
    productionMode.setImmediate(true);
    productionMode.setDescription("В производственном режиме в запросах к внешним сервисам не будет передаваться testMsg");
    productionMode.addListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        boolean value = Boolean.TRUE.equals(event.getProperty().getValue());
        set(API.PRODUCTION_MODE, value);
      }
    });
    Panel panel4 = new Panel("Режим СМЭВ");
    panel4.setSizeFull();
    panel4.addComponent(productionMode);

    LogSettings logSettings = new LogSettings();
    Panel emailDatesPanel = createEmailDatesPanel();

    topHl.addComponent(panel1);
    topHl.addComponent(emailDatesPanel);

    final VerticalLayout layout = new VerticalLayout();
    layout.setSpacing(true);
    layout.setSizeFull();
    layout.addComponent(topHl);
    layout.addComponent(logSettings);
    layout.addComponent(panel4);
    layout.setExpandRatio(topHl, 0.45f);
    layout.setExpandRatio(logSettings, 0.40f);
    layout.setExpandRatio(panel4, 0.10f);
    layout.setMargin(true);
    layout.setSpacing(true);

    return new RefreshableTab(layout, logSettings);
  }

  private Panel createEmailDatesPanel() {
    VerticalLayout emailDates = new VerticalLayout();
    emailDates.setSpacing(true);
    emailDates.setMargin(true);
    emailDates.setSizeFull();
    Panel panel2 = new Panel("Контроль сроков исполнения", emailDates);
    panel2.setSizeFull();

    final TextField emailToField = new TextField("e-mail получателя:");
    emailToField.setValue(get(API.EMAIL_TO));
    emailToField.setRequired(true);
    emailToField.setReadOnly(true);
    emailToField.addValidator(new EmailValidator("Введите корректный e-mail адрес"));

    final TextField receiverNameField = new TextField("Имя получателя:");
    receiverNameField.setValue(get(API.RECEIVER_NAME));
    receiverNameField.setRequired(true);
    receiverNameField.setReadOnly(true);

    final TextField emailFromField = new TextField("e-mail отправителя:");
    emailFromField.setValue(get(API.EMAIL_FROM));
    emailFromField.setRequired(true);
    emailFromField.setReadOnly(true);
    emailFromField.addValidator(new EmailValidator("Введите корректный e-mail адрес"));

    final TextField senderLoginField = new TextField("Логин отправителя:");
    senderLoginField.setValue(get(API.SENDER_LOGIN));
    senderLoginField.setRequired(true);
    senderLoginField.setReadOnly(true);

    final TextField senderNameField = new TextField("Имя отправителя:");
    senderNameField.setValue(get(API.SENDER_NAME));
    senderNameField.setRequired(true);
    senderNameField.setReadOnly(true);

    final TextField passwordField = new TextField("Пароль:");
    passwordField.setValue("");
    passwordField.setRequired(true);
    passwordField.setVisible(false);

    final TextField hostField = new TextField("SMTP сервер:");
    String host = get(API.HOST);
    hostField.setValue(host == null ? "" : host);
    hostField.setRequired(true);
    hostField.setReadOnly(true);

    final TextField portField = new TextField("Порт:");
    String port = get(API.PORT);
    portField.setValue(port == null ? "" : port);
    portField.setRequired(true);
    portField.setReadOnly(true);
    portField.addValidator(new IntegerValidator("Введите цифры"));

    final CheckBox tls = new CheckBox("Использовать TLS", AdminServiceProvider.getBoolProperty(API.TLS));
    tls.setReadOnly(true);

    final Button save = new Button("Сохранить");
    save.setVisible(false);
    final Button cancel = new Button("Отменить");
    cancel.setVisible(false);
    final Button change = new Button("Изменить");
    final Button check = new Button("Проверить");
    check.addListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        String emailTo = get(API.EMAIL_TO);
        String receiverName = get(API.RECEIVER_NAME);
        String hostName = get(API.HOST);
        String port = get(API.PORT);
        String senderLogin = get(API.SENDER_LOGIN);
        String password = get(API.PASSWORD);
        String emailFrom = get(API.EMAIL_FROM);
        String senderName = get(API.SENDER_NAME);
        if (emailTo.isEmpty() || receiverName.isEmpty() || hostName.isEmpty() || port.isEmpty() || senderLogin.isEmpty()
          || password.isEmpty() || emailFrom.isEmpty() || senderName.isEmpty()) {
          check.getWindow().showNotification("Установите все параметры");
          return;
        }
        Email email = new SimpleEmail();
        try {
          email.setSubject("Тестовое письмо");
          email.setMsg("Тестовое письмо");
          email.addTo(emailTo, receiverName);
          email.setHostName(hostName);
          email.setSmtpPort(Integer.parseInt(port));
          email.setTLS(AdminServiceProvider.getBoolProperty(API.TLS));
          email.setAuthentication(senderLogin, password);
          email.setFrom(emailFrom, senderName);
          email.setCharset("utf-8");
          email.send();
        } catch (EmailException e) {
          check.getWindow().showNotification(e.getMessage());
          return;
        }
        check.getWindow().showNotification("Письмо успешно отправлено");
      }
    });
    change.addListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        emailToField.setReadOnly(false);
        receiverNameField.setReadOnly(false);
        emailFromField.setReadOnly(false);
        senderLoginField.setReadOnly(false);
        senderNameField.setReadOnly(false);
        passwordField.setVisible(true);
        hostField.setReadOnly(false);
        portField.setReadOnly(false);
        tls.setReadOnly(false);

        change.setVisible(false);
        check.setVisible(false);
        save.setVisible(true);
        cancel.setVisible(true);
      }
    });
    save.addListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        if (
          StringUtils.isEmpty((String) emailToField.getValue()) ||
            StringUtils.isEmpty((String) receiverNameField.getValue()) ||
            StringUtils.isEmpty((String) emailFromField.getValue()) ||
            StringUtils.isEmpty((String) senderNameField.getValue()) ||
            StringUtils.isEmpty((String) senderLoginField.getValue()) ||
            StringUtils.isEmpty((String) passwordField.getValue()) ||
            StringUtils.isEmpty((String) hostField.getValue()) ||
            portField.getValue() == null
          ) {
          emailToField.getWindow().showNotification("Заполните поля", Window.Notification.TYPE_HUMANIZED_MESSAGE);
          return;
        }
        boolean errors = false;
        try {
          emailToField.validate();
        } catch (Validator.InvalidValueException ignore) {
          errors = true;
        }
        try {
          emailFromField.validate();
        } catch (Validator.InvalidValueException ignore) {
          errors = true;
        }
        try {
          portField.validate();
        } catch (Validator.InvalidValueException ignore) {
          errors = true;
        }
        if (errors) {
          return;
        }
        set(API.EMAIL_TO, emailToField.getValue());
        set(API.RECEIVER_NAME, receiverNameField.getValue());
        set(API.EMAIL_FROM, emailFromField.getValue());
        set(API.SENDER_LOGIN, senderLoginField.getValue());
        set(API.SENDER_NAME, senderNameField.getValue());
        set(API.PASSWORD, passwordField.getValue());
        set(API.HOST, hostField.getValue());
        set(API.PORT, portField.getValue());
        set(API.TLS, tls.getValue());

        emailToField.setReadOnly(true);
        receiverNameField.setReadOnly(true);
        emailFromField.setReadOnly(true);
        senderLoginField.setReadOnly(true);
        senderNameField.setReadOnly(true);
        passwordField.setValue("");
        passwordField.setVisible(false);
        hostField.setReadOnly(true);
        portField.setReadOnly(true);
        tls.setReadOnly(true);

        save.setVisible(false);
        cancel.setVisible(false);
        change.setVisible(true);
        check.setVisible(true);
        emailToField.getWindow().showNotification("Настройки сохранены", Window.Notification.TYPE_HUMANIZED_MESSAGE);
      }
    });
    cancel.addListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        emailToField.setValue(get(API.EMAIL_TO));
        receiverNameField.setValue(get(API.RECEIVER_NAME));
        emailFromField.setValue(get(API.EMAIL_FROM));
        senderLoginField.setValue(get(API.SENDER_LOGIN));
        senderNameField.setValue(get(API.SENDER_NAME));
        passwordField.setValue("");
        hostField.setValue(get(API.HOST));
        portField.setValue(get(API.PORT));
        tls.setValue(AdminServiceProvider.getBoolProperty(API.TLS));

        emailToField.setReadOnly(true);
        receiverNameField.setReadOnly(true);
        emailFromField.setReadOnly(true);
        senderLoginField.setReadOnly(true);
        senderNameField.setReadOnly(true);
        passwordField.setVisible(false);
        hostField.setReadOnly(true);
        portField.setReadOnly(true);
        tls.setReadOnly(true);

        save.setVisible(false);
        cancel.setVisible(false);
        change.setVisible(true);
        check.setValue(true);
      }
    });

    FormLayout fields1 = new FormLayout();
    fields1.setSizeFull();
    fields1.addComponent(senderLoginField);
    fields1.addComponent(passwordField);
    fields1.addComponent(hostField);
    fields1.addComponent(portField);
    fields1.addComponent(tls);

    FormLayout fields2 = new FormLayout();
    fields2.setSizeFull();
    fields2.addComponent(emailToField);
    fields2.addComponent(receiverNameField);
    fields2.addComponent(emailFromField);
    fields2.addComponent(senderNameField);

    HorizontalLayout fields = new HorizontalLayout();
    fields.setSpacing(true);
    fields.setSizeFull();
    fields.addComponent(fields1);
    fields.addComponent(fields2);

    HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    buttons.addComponent(change);
    buttons.addComponent(save);
    buttons.addComponent(cancel);
    buttons.addComponent(check);

    emailDates.addComponent(fields);
    emailDates.addComponent(buttons);
    emailDates.setExpandRatio(fields, 1f);
    return panel2;
  }

  private String get(String property) {
    return StringUtils.trimToEmpty(AdminServiceProvider.get().getSystemProperty(property));
  }

  private void set(String property, Object value) {
    AdminServiceProvider.get().saveSystemProperty(property, value.toString());
  }
}
