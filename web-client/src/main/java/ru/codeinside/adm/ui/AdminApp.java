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
import com.vaadin.terminal.Sizeable;
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
import com.vaadin.ui.PasswordField;
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

import java.net.MalformedURLException;
import java.net.URL;

public class AdminApp extends Application {

  private static final long serialVersionUID = 1L;

  public static final String STATISTIC = "//194.85.124.90:8888/Statistic/";
  private static final String REGISTRY = "/registry";

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

    t.addTab(statisticTab(), "Статистика");

    t.addTab(registryTab(), "Реестр");
    setMainWindow(new Window(getUser() + " | Управление | СИУ-" + Activator.getContext().getBundle().getVersion(), t));
    AdminServiceProvider.get().createLog(Flash.getActor(), "Admin application", (String) getUser(), "login", null, true);
  }

  private Component statisticTab() {
    try {
      String serviceLocation = AdminServiceProvider.get().getSystemProperty(API.STATISTIC_SERVICELOCATION);
      if (serviceLocation == null || serviceLocation.isEmpty()) {
        serviceLocation = STATISTIC;
      }
      Embedded embedded = new Embedded("", new ExternalResource(new URL(getURL(), serviceLocation)));
      embedded.setType(Embedded.TYPE_BROWSER);
      embedded.setWidth("100%");
      embedded.setHeight("100%");
      return embedded;
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  private Component registryTab() {
    Embedded embedded = new Embedded("", new ExternalResource(REGISTRY));
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
          {"Производственный контур", "http://oraas.rt.ru:7777/gateway/services/SID0003318"},
          {"Локальный контур", "local"}
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

      final TextField certPasswordAttempts = new TextField("Количество попыток ввода пароля");
      certPasswordAttempts.setValue(get(CertificateVerifier.CERT_PASSWORD_ATTEMPTS));
      certPasswordAttempts.setEnabled(true);
      certPasswordAttempts.setWidth(30, Sizeable.UNITS_PIXELS);

      systemForm = new Form();
      systemForm.addField("location", serviceLocation);
      systemForm.addField("allowVerify", allowValidate);
      systemForm.addField("passwordAttempts", certPasswordAttempts);
      systemForm.setImmediate(true);
      systemForm.setWriteThrough(false);
      systemForm.setInvalidCommitted(false);

      Button commit = new Button("Изменить", new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent event) {
          try {
            if (Integer.valueOf(certPasswordAttempts.getValue().toString()) > 0) {
              systemForm.commit();
              if (serviceLocation.getValue() != null) {
                set(CertificateVerifier.VERIFY_SERVICE_LOCATION, serviceLocation.getValue());
              }
              set(CertificateVerifier.ALLOW_VERIFY_CERTIFICATE_PROPERTY, allowValidate.getValue());
              set(CertificateVerifier.CERT_PASSWORD_ATTEMPTS, certPasswordAttempts.getValue());
              event.getButton().getWindow().showNotification("Настройки сохранены", Window.Notification.TYPE_HUMANIZED_MESSAGE);
            } else {
              event.getButton().getWindow().showNotification("Количество попыток ввода пароля: неверный формат", Window.Notification.TYPE_WARNING_MESSAGE);
            }
          } catch (NumberFormatException e) {
            event.getButton().getWindow().showNotification("Количество попыток ввода пароля: неверный формат", Window.Notification.TYPE_WARNING_MESSAGE);
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

    Panel certificatesPanel = new Panel("Сертификаты", certificates);
    certificatesPanel.setSizeFull();
    certificatesPanel.addStyleName(Reindeer.PANEL_LIGHT);

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
    Panel smevPanel = new Panel("Режим СМЭВ");
    smevPanel.setSizeFull();
    smevPanel.addComponent(productionMode);

    HorizontalLayout bottomHl = new HorizontalLayout();
    bottomHl.setSizeFull();
    bottomHl.setSpacing(true);

    LogSettings logSettings = new LogSettings();
    Panel emailDatesPanel = createEmailDatesPanel();

    Panel mailTaskConfigPanel = createMilTaskConfigPanel();

    topHl.addComponent(certificatesPanel);
    topHl.addComponent(emailDatesPanel);
    topHl.addComponent(mailTaskConfigPanel);
    topHl.setExpandRatio(certificatesPanel, 0.4f);
    topHl.setExpandRatio(emailDatesPanel, 0.6f);
    topHl.setExpandRatio(mailTaskConfigPanel, 0.5f);

    Panel esiaPanel = buildAuthPanel();
    Panel printTemplatesPanel = buildPrintTemplatesPanel();
    Panel statisticPanel = buildStatisticPanel();

    bottomHl.addComponent(smevPanel);
    bottomHl.addComponent(esiaPanel);
    bottomHl.addComponent(printTemplatesPanel);
    bottomHl.addComponent(statisticPanel);
    bottomHl.setExpandRatio(smevPanel, 0.1f);
    bottomHl.setExpandRatio(esiaPanel, 0.3f);
    bottomHl.setExpandRatio(printTemplatesPanel, 0.3f);
    bottomHl.setExpandRatio(statisticPanel, 0.3f);

    final VerticalLayout layout = new VerticalLayout();
    layout.setSpacing(true);
    layout.setSizeFull();
    layout.addComponent(topHl);
    layout.addComponent(logSettings);
    layout.addComponent(bottomHl);
    layout.setExpandRatio(topHl, 0.40f);
    layout.setExpandRatio(logSettings, 0.40f);
    layout.setExpandRatio(bottomHl, 0.20f);
    layout.setMargin(true);
    layout.setSpacing(true);

    return new RefreshableTab(layout, logSettings);
  }

  private Panel buildAuthPanel() {
    boolean isEsiaAuth = "true".equals(get(API.ALLOW_ESIA_LOGIN));

    final TextField esiaServiceLocation = new TextField("Адрес сервиса проверки");
    esiaServiceLocation.setValue(get(API.ESIA_SERVICE_ADDRESS));
    esiaServiceLocation.setEnabled(isEsiaAuth);
    esiaServiceLocation.setRequired(isEsiaAuth);
    esiaServiceLocation.setWidth(370, Sizeable.UNITS_PIXELS);

    final TextField passwordAttempts = new TextField("Количество попыток ввода пароля");
    passwordAttempts.setValue(get(API.AUTH_PASSWORD_ATTEMPTS));
    passwordAttempts.setEnabled(true);
    passwordAttempts.setWidth(30, Sizeable.UNITS_PIXELS);

    final CheckBox allowEsiaLogin = new CheckBox("Разрешить вход через ЕСИА");
    allowEsiaLogin.setValue(isEsiaAuth);
    allowEsiaLogin.setImmediate(true);
    allowEsiaLogin.addListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
        Boolean newValue = (Boolean) valueChangeEvent.getProperty().getValue();
        esiaServiceLocation.setEnabled(newValue);
        esiaServiceLocation.setRequired(newValue);
      }
    });

    final Form form = new Form();
    form.addField(API.ALLOW_ESIA_LOGIN, allowEsiaLogin);
    form.addField(API.ESIA_SERVICE_ADDRESS, esiaServiceLocation);
    form.addField(API.AUTH_PASSWORD_ATTEMPTS, passwordAttempts);
    form.setImmediate(true);
    form.setWriteThrough(false);
    form.setInvalidCommitted(false);

    Button commit = new Button("Изменить", new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        try {
          if (Integer.valueOf(passwordAttempts.getValue().toString()) > 0) {
            form.commit();
            set(API.ALLOW_ESIA_LOGIN, allowEsiaLogin.getValue());
            set(API.ESIA_SERVICE_ADDRESS, esiaServiceLocation.getValue());
            set(API.AUTH_PASSWORD_ATTEMPTS, passwordAttempts.getValue());
            event.getButton().getWindow().showNotification("Настройки сохранены", Window.Notification.TYPE_HUMANIZED_MESSAGE);
          } else {
            event.getButton().getWindow().showNotification("Количество попыток ввода пароля: неверный формат", Window.Notification.TYPE_WARNING_MESSAGE);
          }
        } catch (NumberFormatException e) {
          event.getButton().getWindow().showNotification("Количество попыток ввода пароля: неверный формат", Window.Notification.TYPE_WARNING_MESSAGE);
        } catch (Validator.InvalidValueException ignore) { }
      }
    });

    Panel panel = new Panel("Настройки авторизации");
    panel.setSizeFull();
    panel.addComponent(form);
    panel.addComponent(commit);
    return panel;
  }

  private Panel buildPrintTemplatesPanel() {
    boolean isUseService = "true".equals(get(API.PRINT_TEMPLATES_USE_OUTER_SERVICE));

    final TextField serviceLocation = new TextField("Адрес сервиса");
    serviceLocation.setValue(get(API.PRINT_TEMPLATES_SERVICELOCATION));
    serviceLocation.setEnabled(isUseService);
    serviceLocation.setRequired(isUseService);
    serviceLocation.setWidth(370, Sizeable.UNITS_PIXELS);

    final CheckBox useOuterService = new CheckBox("Использовать внешний сервис");
    useOuterService.setValue(isUseService);
    useOuterService.setImmediate(true);
    useOuterService.addListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
        Boolean newValue = (Boolean) valueChangeEvent.getProperty().getValue();
        serviceLocation.setEnabled(newValue);
        serviceLocation.setRequired(newValue);
      }
    });

    final Form form = new Form();
    form.addField(API.PRINT_TEMPLATES_USE_OUTER_SERVICE, useOuterService);
    form.addField(API.PRINT_TEMPLATES_SERVICELOCATION, serviceLocation);
    form.setImmediate(true);
    form.setWriteThrough(false);
    form.setInvalidCommitted(false);

    Button commit = new Button("Изменить", new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        try {
          form.commit();
          set(API.PRINT_TEMPLATES_USE_OUTER_SERVICE, useOuterService.getValue());
          set(API.PRINT_TEMPLATES_SERVICELOCATION, serviceLocation.getValue());
          event.getButton().getWindow().showNotification("Настройки сохранены", Window.Notification.TYPE_HUMANIZED_MESSAGE);
        } catch (Validator.InvalidValueException ignore) { }
      }
    });

    Panel panel = new Panel("Печатные формы");
    panel.setSizeFull();
    panel.addComponent(form);
    panel.addComponent(commit);
    return panel;
  }

  private Panel buildStatisticPanel() {
    final TextField serviceLocation = new TextField("Адрес сервиса статистики");
    String serviceUrl = get(API.STATISTIC_SERVICELOCATION);
    if (serviceUrl == null || serviceUrl.isEmpty()) {
      serviceUrl = STATISTIC;
    }
    serviceLocation.setValue(serviceUrl);
    serviceLocation.setWidth(370, Sizeable.UNITS_PIXELS);

    final Form form = new Form();
    form.addField(API.STATISTIC_SERVICELOCATION, serviceLocation);
    form.setImmediate(true);
    form.setWriteThrough(false);
    form.setInvalidCommitted(false);

    Button commit = new Button("Изменить", new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        try {
          form.commit();
          set(API.STATISTIC_SERVICELOCATION, serviceLocation.getValue());
          event.getButton().getWindow().showNotification("Настройки сохранены", Window.Notification.TYPE_HUMANIZED_MESSAGE);
        } catch (Validator.InvalidValueException ignore) { }
      }
    });

    Panel panel = new Panel("Сервис статистики");
    panel.setSizeFull();
    panel.addComponent(form);
    panel.addComponent(commit);
    return panel;
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

    final PasswordField passwordField = new PasswordField("Пароль:");
    passwordField.setValue(API.PASSWORD);
    passwordField.setRequired(true);
    passwordField.setReadOnly(true);

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
          e.printStackTrace();
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
        passwordField.setReadOnly(false);
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
        passwordField.setReadOnly(true);
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
        passwordField.setValue(get(API.PASSWORD));
        hostField.setValue(get(API.HOST));
        portField.setValue(get(API.PORT));
        tls.setValue(AdminServiceProvider.getBoolProperty(API.TLS));

        emailToField.setReadOnly(true);
        receiverNameField.setReadOnly(true);
        emailFromField.setReadOnly(true);
        senderLoginField.setReadOnly(true);
        senderNameField.setReadOnly(true);
        passwordField.setReadOnly(true);
        hostField.setReadOnly(true);
        portField.setReadOnly(true);
        tls.setReadOnly(true);

        save.setVisible(false);
        cancel.setVisible(false);
        change.setVisible(true);
        check.setVisible(true);
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

    Label label = new Label("Настройки почты");
    label.addStyleName(Reindeer.LABEL_H2);
    emailDates.addComponent(label);
    emailDates.addComponent(fields);
    emailDates.addComponent(buttons);
    emailDates.setExpandRatio(fields, 1f);
    return panel2;
  }

  private Panel createMilTaskConfigPanel() {
    VerticalLayout mailConfig = new VerticalLayout();
    mailConfig.setSpacing(true);
    mailConfig.setMargin(true);
    mailConfig.setSizeFull();
    Panel emailTaskPanel = new Panel("Настройки SMTP для Email Task", mailConfig);
    emailTaskPanel.setSizeFull();

    final TextField mtDefaultFrom = new TextField("email по умолчанию:");
    mtDefaultFrom.setValue(get(API.MT_DEFAULT_FROM));
    mtDefaultFrom.setRequired(true);
    mtDefaultFrom.setReadOnly(true);
    mtDefaultFrom.addValidator(new EmailValidator("Введите корректный e-mail адрес"));

    final TextField mtSenderLoginField = new TextField("Логин отправителя:");
    mtSenderLoginField.setValue(get(API.MT_SENDER_LOGIN));
    mtSenderLoginField.setRequired(true);
    mtSenderLoginField.setReadOnly(true);

    final PasswordField mtPasswordField = new PasswordField("Пароль:");
    mtPasswordField.setValue(API.MT_PASSWORD);
    mtPasswordField.setRequired(true);
    mtPasswordField.setReadOnly(true);

    final TextField mtHostField = new TextField("SMTP сервер:");
    String host = get(API.MT_HOST);
    mtHostField.setValue(host == null ? "" : host);
    mtHostField.setRequired(true);
    mtHostField.setReadOnly(true);

    final TextField mtPortField = new TextField("Порт:");
    String port = get(API.MT_PORT);
    mtPortField.setValue(port == null ? "" : port);
    mtPortField.setRequired(true);
    mtPortField.setReadOnly(true);
    mtPortField.addValidator(new IntegerValidator("Введите цифры"));

    final CheckBox mtTls = new CheckBox("Использовать TLS", AdminServiceProvider.getBoolProperty(API.MT_TLS));
    mtTls.setReadOnly(true);

    final Button save = new Button("Сохранить");
    save.setVisible(false);
    final Button cancel = new Button("Отменить");
    cancel.setVisible(false);
    final Button change = new Button("Изменить");

    change.addListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        mtSenderLoginField.setReadOnly(false);
        mtDefaultFrom.setReadOnly(false);
        mtPasswordField.setReadOnly(false);
        mtHostField.setReadOnly(false);
        mtPortField.setReadOnly(false);
        mtTls.setReadOnly(false);

        change.setVisible(false);
        save.setVisible(true);
        cancel.setVisible(true);
      }
    });
    save.addListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        if (
            StringUtils.isEmpty((String) mtSenderLoginField.getValue()) ||
                StringUtils.isEmpty((String) mtDefaultFrom.getValue()) ||
                StringUtils.isEmpty((String) mtPasswordField.getValue()) ||
                StringUtils.isEmpty((String) mtHostField.getValue()) ||
                mtPortField.getValue() == null
            ) {
          mtSenderLoginField.getWindow().showNotification("Заполните поля", Window.Notification.TYPE_HUMANIZED_MESSAGE);
          return;
        }
        boolean errors = false;
        try {
          mtDefaultFrom.validate();
        } catch (Validator.InvalidValueException ignore) {
          errors = true;
        }
        try {
          mtPortField.validate();
        } catch (Validator.InvalidValueException ignore) {
          errors = true;
        }
        if (errors) {
          return;
        }
        set(API.MT_SENDER_LOGIN, mtSenderLoginField.getValue());
        set(API.MT_DEFAULT_FROM, mtDefaultFrom.getValue());
        set(API.MT_PASSWORD, mtPasswordField.getValue());
        set(API.MT_HOST, mtHostField.getValue());
        set(API.MT_PORT, mtPortField.getValue());
        set(API.MT_TLS, mtTls.getValue());

        mtSenderLoginField.setReadOnly(true);
        mtDefaultFrom.setReadOnly(true);
        mtPasswordField.setReadOnly(true);
        mtHostField.setReadOnly(true);
        mtPortField.setReadOnly(true);
        mtTls.setReadOnly(true);

        save.setVisible(false);
        cancel.setVisible(false);
        change.setVisible(true);
        mtSenderLoginField.getWindow().showNotification("Настройки сохранены", Window.Notification.TYPE_HUMANIZED_MESSAGE);
      }
    });
    cancel.addListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        mtSenderLoginField.setValue(get(API.MT_SENDER_LOGIN));
        mtDefaultFrom.setValue(get(API.MT_DEFAULT_FROM));
        mtPasswordField.setValue(get(API.MT_PASSWORD));
        mtHostField.setValue(get(API.MT_HOST));
        mtPortField.setValue(get(API.MT_PORT));
        mtTls.setValue(AdminServiceProvider.getBoolProperty(API.MT_TLS));

        mtSenderLoginField.setReadOnly(true);
        mtDefaultFrom.setReadOnly(true);
        mtPasswordField.setReadOnly(true);
        mtHostField.setReadOnly(true);
        mtPortField.setReadOnly(true);
        mtTls.setReadOnly(true);

        save.setVisible(false);
        cancel.setVisible(false);
        change.setVisible(true);
      }
    });

    FormLayout leftFields = new FormLayout();
    leftFields.setSizeFull();
    leftFields.addComponent(mtSenderLoginField);
    leftFields.addComponent(mtDefaultFrom);
    leftFields.addComponent(mtPasswordField);
    leftFields.addComponent(mtHostField);
    leftFields.addComponent(mtPortField);

    FormLayout rightFields = new FormLayout();
    rightFields.setSizeFull();
    rightFields.addComponent(mtTls);

    HorizontalLayout fieldsLayout = new HorizontalLayout();
    fieldsLayout.setSpacing(true);
    fieldsLayout.setSizeFull();
    fieldsLayout.addComponent(leftFields);
    fieldsLayout.addComponent(rightFields);
    fieldsLayout.setExpandRatio(leftFields, 0.6f);
    fieldsLayout.setExpandRatio(rightFields, 0.4f);

    HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    buttons.addComponent(change);
    buttons.addComponent(save);
    buttons.addComponent(cancel);

    Label label = new Label("Настройки Email Task");
    label.addStyleName(Reindeer.LABEL_H2);
    mailConfig.addComponent(label);
    mailConfig.addComponent(fieldsLayout);
    mailConfig.addComponent(buttons);
    mailConfig.setExpandRatio(fieldsLayout, 1f);
    return emailTaskPanel;
  }

  private String get(String property) {
    return StringUtils.trimToEmpty(AdminServiceProvider.get().getSystemProperty(property));
  }

  private void set(String property, Object value) {
    AdminServiceProvider.get().saveSystemProperty(property, value.toString());
  }
}
