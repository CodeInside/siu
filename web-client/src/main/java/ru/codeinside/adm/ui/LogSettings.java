/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.LogScheduler;
import ru.codeinside.gses.API;
import ru.codeinside.gses.webui.components.api.IRefresh;
import ru.codeinside.gses.webui.osgi.LogCustomizer;
import ru.codeinside.gws.api.Packet.Status;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

final class LogSettings extends CustomComponent implements IRefresh {

  final OptionGroup logErrors;
  final OptionGroup logStatus;
  final TextArea ipSet;
  final TextField tf;

  final Block b1;
  final Block b2;
  final Block b3;

  LogSettings() {

    logErrors = new OptionGroup("Для всех модулей:", Arrays.asList(Status.FAILURE.name()));
    logErrors.setItemCaption(Status.FAILURE.name(), "Ошибки");
    logErrors.setImmediate(true);
    logErrors.setMultiSelect(true);

    logStatus = new OptionGroup("Для модулей с флагом 'Журнал':", statusKeys());
    logStatus.setItemCaption(Status.REQUEST.name(), "Заявки");
    logStatus.setItemCaption(Status.RESULT.name(), "Результаты");
    logStatus.setItemCaption(Status.PING.name(), "Опросы");
    logStatus.setMultiSelect(true);
    logStatus.setImmediate(true);

    ipSet = new TextArea("Список IP адресов:");
    ipSet.setWordwrap(true);
    ipSet.setNullRepresentation("");
    ipSet.setWidth(100f, UNITS_PERCENTAGE);
    ipSet.setRows(10);

    tf = new TextField("Хранить журнал, дней:");
    tf.setRequired(true);
    tf.addValidator(new Validator() {
      public void validate(Object value) throws InvalidValueException {
        if (!isValid(value)) {
          throw new InvalidValueException("Введите положительное числовое значение");
        }
      }

      public boolean isValid(Object value) {
        return value instanceof String && ((String) value).matches("[1-9][0-9]*");
      }
    });


    b1 = new Block("Журналировать") {

      @Override
      void onLayout(Layout layout) {
        layout.addComponent(logErrors);
        layout.addComponent(logStatus);
      }

      @Override
      void onChange() {
        logErrors.setReadOnly(false);
        logStatus.setReadOnly(false);
      }

      @Override
      void onRefresh() {
        boolean _logErrors = AdminServiceProvider.getBoolProperty(API.LOG_ERRORS);
        if (_logErrors) {
          logErrors.setReadOnly(false);
          logErrors.setValue(Arrays.asList(Status.FAILURE.name()));
        } else {
          logErrors.setValue(Collections.emptySet());
        }
        logErrors.setReadOnly(true);

        String _logStatus = AdminServiceProvider.get().getSystemProperty(API.LOG_STATUS);
        if (_logStatus != null) {
          Set<String> set = new HashSet<String>();
          for (String key : statusKeys()) {
            if (_logStatus.contains(key)) {
              set.add(key);
            }
          }
          logStatus.setReadOnly(false);
          logStatus.setValue(set);
        } else {
          logStatus.setValue(Collections.emptySet());
        }
        logStatus.setReadOnly(true);
      }

      @Override
      void onApply() {
        Collection logErrorsValue = (Collection) logErrors.getValue();
        boolean errorsEnabled = logErrorsValue.contains(Status.FAILURE.name());
        AdminServiceProvider.get().saveSystemProperty(API.LOG_ERRORS, Boolean.toString(errorsEnabled));
        LogCustomizer.setShouldWriteServerLogErrors(errorsEnabled);

        Collection logStatusValue = (Collection) logStatus.getValue();

        Set<Status> statuses = new TreeSet<Status>();

        if (logStatusValue.contains(Status.REQUEST.name())) {
          statuses.add(Status.REQUEST);
          statuses.add(Status.ACCEPT);
          statuses.add(Status.CANCEL);
        }

        if (logStatusValue.contains(Status.RESULT.name())) {
          statuses.add(Status.RESULT);
          statuses.add(Status.REJECT);
          statuses.add(Status.STATE);
          statuses.add(Status.NOTIFY);
        }

        if (logStatusValue.contains(Status.PING.name())) {
          statuses.add(Status.PING);
          statuses.add(Status.PROCESS);
          statuses.add(Status.PACKET);
        }

        StringBuilder statusBuilder = new StringBuilder();
        for (Status status : statuses) {
          if (statusBuilder.length() > 0) {
            statusBuilder.append(", ");
          }
          statusBuilder.append(status);
        }
        String status = statusBuilder.toString();

        AdminServiceProvider.get().saveSystemProperty(API.LOG_STATUS, status);
        LogCustomizer.setServerLogStatus(status);

        boolean enabled = !status.isEmpty();
        LogCustomizer.setShouldWriteServerLog(enabled);
        AdminServiceProvider.get().saveSystemProperty(API.ENABLE_CLIENT_LOG, Boolean.toString(enabled));
      }
    };


    b2 = new Block("Не журналировать") {
      @Override
      void onLayout(Layout layout) {
        layout.addComponent(ipSet);
      }

      @Override
      void onRefresh() {
        String ips = AdminServiceProvider.get().getSystemProperty(API.SKIP_LOG_IPS);
        ipSet.setReadOnly(false);
        ipSet.setValue(ips);
        ipSet.setReadOnly(true);
      }

      @Override
      void onChange() {
        ipSet.setReadOnly(false);
      }

      @Override
      void onApply() {
        String value = (String) ipSet.getValue();
        TreeSet<String> items = new TreeSet<String>();
        if (value != null) {
          for (String item : value.split("[,;\\s]+")) {
            items.add(item);
          }
          StringBuilder sb = new StringBuilder();
          for (String item : items) {
            if (sb.length() > 0) {
              sb.append(", ");
            }
            sb.append(item);
          }
          value = sb.toString();
        }
        AdminServiceProvider.get().saveSystemProperty(API.SKIP_LOG_IPS, value);
        LogCustomizer.setIgnoreSet(items);
      }
    };


    b3 = new Block("Очистка и резервное копирование") {
      @Override
      void onButtons(Layout layout) {
        layout.addComponent(new Button("Очистить журнал", new Button.ClickListener() {
          @Override
          public void buttonClick(Button.ClickEvent event) {
            LogScheduler.cleanLog();
            getWindow().showNotification("Процесс запущен", Window.Notification.TYPE_HUMANIZED_MESSAGE);
          }
        }));
      }

      @Override
      void onLayout(Layout layout) {
        layout.addComponent(tf);
      }

      @Override
      void onRefresh() {
        String logDepth = AdminServiceProvider.get().getSystemProperty(API.LOG_DEPTH);
        tf.setReadOnly(false);
        if (logDepth != null && logDepth.matches("[1-9][0-9]*")) {
          tf.setValue(logDepth);
        } else {
          tf.setValue(String.valueOf(API.DEFAULT_LOG_DEPTH));
        }
        tf.setReadOnly(true);
      }

      @Override
      void onChange() {
        tf.setReadOnly(false);
      }

      @Override
      void onApply() {
        tf.validate();
        AdminServiceProvider.get().saveSystemProperty(API.LOG_DEPTH, tf.getValue().toString());
      }
    };

    final CheckBox logSpSign = new CheckBox("Логирование подписи СП");
    logSpSign.setValue(Boolean.valueOf(AdminServiceProvider.get().getSystemProperty(API.LOG_SP_SIGN)));
    logSpSign.addListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
        AdminServiceProvider.get().saveSystemProperty(API.LOG_SP_SIGN, String.valueOf(valueChangeEvent.getProperty().getValue()));
        notifySuccess();
      }
    });
    logSpSign.setImmediate(true);

    final CheckBox logOvSign = new CheckBox("Логирование подписи ОВ");
    logOvSign.setValue(Boolean.valueOf(AdminServiceProvider.get().getSystemProperty(API.LOG_OV_SIGN)));
    logOvSign.addListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
        AdminServiceProvider.get().saveSystemProperty(API.LOG_OV_SIGN, String.valueOf(valueChangeEvent.getProperty().getValue()));
        notifySuccess();
      }
    });
    logOvSign.setImmediate(true);

    Label userSignsLabel = new Label("Пользовательские подписи");
    userSignsLabel.addStyleName(Reindeer.LABEL_H2);

    VerticalLayout userSignsVl = new VerticalLayout();
    userSignsVl.setMargin(true);
    userSignsVl.setSpacing(true);
    userSignsVl.addComponent(userSignsLabel);
    userSignsVl.addComponent(logSpSign);
    userSignsVl.addComponent(logOvSign);

    Panel userSingsWrapper = new Panel();
    userSingsWrapper.setScrollable(true);
    userSingsWrapper.setContent(userSignsVl);
    userSingsWrapper.setSizeFull();

    VerticalLayout vl = new VerticalLayout();
    vl.addComponent(b1);
    vl.addComponent(userSingsWrapper);
    vl.setExpandRatio(b1, 0.7f);
    vl.setExpandRatio(userSingsWrapper, 0.3f);
    vl.setSizeFull();

    HorizontalLayout layout = new HorizontalLayout();
    layout.setSpacing(true);
    layout.addComponent(vl);
    layout.addComponent(b2);
    layout.addComponent(b3);
    layout.setSizeFull();
    layout.setExpandRatio(vl, 0.333f);
    layout.setExpandRatio(b2, 0.333f);
    layout.setExpandRatio(b3, 0.333f);

    Panel wrapper = new Panel("Журнал СМЭВ", layout);
    wrapper.addStyleName(Reindeer.PANEL_LIGHT);
    wrapper.setSizeFull();

    setCompositionRoot(wrapper);
    setSizeFull();
  }

  public void refresh() {
    b1.refresh();
    b2.refresh();
    b3.refresh();
  }

  static List<String> statusKeys() {
    return Arrays.asList(Status.REQUEST.name(), Status.RESULT.name(), Status.PING.name());
  }

  private void notifySuccess() {
    getWindow().showNotification("Настройки сохранены", Window.Notification.TYPE_HUMANIZED_MESSAGE);
  }

  static class Block extends CustomComponent {

    boolean editMode;

    Block(String caption) {

      VerticalLayout layout = new VerticalLayout();

      Label label = new Label(caption);
      label.addStyleName(Reindeer.LABEL_H2);

      final Button change = new Button("Изменить");
      final Button cancel = new Button("Отменить");
      final Button apply = new Button("Применить");


      cancel.setImmediate(true);
      cancel.setVisible(false);
      cancel.addListener(new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent event) {
          cancel.setVisible(false);
          apply.setVisible(false);
          change.setVisible(true);
          editMode = false;
          onRefresh();
        }
      });

      apply.setImmediate(true);
      apply.setVisible(false);
      apply.addListener(new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent event) {
          try {
            onApply();
          } catch (Validator.InvalidValueException ignore) {
            return;
          }
          getWindow().showNotification("Настройки сохранены", Window.Notification.TYPE_HUMANIZED_MESSAGE);
          cancel.setVisible(false);
          apply.setVisible(false);
          change.setVisible(true);
          editMode = false;
          onRefresh();
        }
      });

      change.addStyleName(Reindeer.BUTTON_SMALL);
      change.setImmediate(true);
      change.addListener(new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent event) {
          cancel.setVisible(true);
          apply.setVisible(true);
          change.setVisible(false);
          onChange();
          editMode = true;
        }
      });

      layout.setMargin(true);
      layout.setSpacing(true);
      layout.addComponent(label);
      onLayout(layout);

      HorizontalLayout buttons = new HorizontalLayout();
      buttons.setSpacing(true);
      buttons.addComponent(change);
      buttons.addComponent(apply);
      buttons.addComponent(cancel);
      onButtons(buttons);
      layout.addComponent(buttons);

      Panel panel = new Panel(layout);
      panel.setSizeFull();
      setCompositionRoot(panel);
      setSizeFull();
    }

    void onButtons(Layout layout) {

    }

    void onLayout(Layout layout) {

    }

    void onRefresh() {
    }

    void onChange() {

    }

    void onApply() {

    }

    final public void refresh() {
      if (!editMode) {
        onRefresh();
      }
    }
  }
}
