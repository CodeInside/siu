package ru.codeinside.adm.ui;

import com.vaadin.data.Property;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import ru.codeinside.gses.webui.osgi.LogCustomizer;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static ru.codeinside.gws.api.Packet.Status.*;

final class ServicesTab extends VerticalLayout implements TabSheet.SelectedTabChangeListener {

  final ServicesTable servicesTable;
  final CheckBox logEnabled;
  final CheckBox logErrors;
  final OptionGroup logStatus;

  ServicesTab() {
    servicesTable = new ServicesTable();
    setSizeFull();
    setSpacing(true);
    setMargin(true);

    UploadDeployer uploader = new UploadDeployer(servicesTable);

    Upload upload = new Upload("Установка модуля", uploader);
    upload.setDescription("Выберите OSGI модуль (файл с раширением jar),\nреализующий интерфейс поставщика СМЭВ");
    upload.addListener(uploader);
    upload.setButtonCaption("Загрузить");
    upload.setWidth(100, Sizeable.UNITS_PERCENTAGE);

    final Boolean serverLogEnabled = LogCustomizer.isServerLogEnabled();
    logEnabled = new CheckBox("Вести журнал сообщений, в зависимости от настроек модулей");
    logEnabled.setImmediate(true);
    logEnabled.setValue(serverLogEnabled);
    logEnabled.addListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        LogCustomizer.setShouldWriteServerLog(Boolean.TRUE.equals(event.getProperty().getValue()));
        logStatus.setEnabled(Boolean.TRUE.equals(event.getProperty().getValue()));
        servicesTable.reload();
      }
    });

    logErrors = new CheckBox("Журналировать ошибки всех сервисов");
    logErrors.setImmediate(true);
    logErrors.setValue(LogCustomizer.isServerLogErrorsEnabled());
    logErrors.addListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        LogCustomizer.setShouldWriteServerLogErrors(Boolean.TRUE.equals(event.getProperty().getValue()));
      }
    });

    logStatus = new OptionGroup(
      "Фильтрация по статусу:",
      Arrays.asList(REQUEST.toString(), RESULT.toString())
    );
    logStatus.setMultiSelect(true);
    String status = LogCustomizer.getServerLogStatus();
    if (status != null ) {
      Set<String> values = new HashSet<String>();
      for (Object v : logStatus.getItemIds()) {
        if (status.contains((String)v)) {
          values.add((String)v);
        }
      }
      logStatus.setValue(values);
    }
    logStatus.setImmediate(true);
    logStatus.setEnabled(serverLogEnabled);
    logStatus.addListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        String value = "";
        for (Object v : (Collection) event.getProperty().getValue()) {
          value += v;
        }
        LogCustomizer.setServerLogStatus(value);
      }
    });


    HorizontalLayout margin = new HorizontalLayout();
    VerticalLayout vl = new VerticalLayout();
    vl.addComponent(logErrors);
    vl.addComponent(logEnabled);
    FormLayout c = new FormLayout();
    c.setMargin(false, false, false, true);
    c.addComponent(logStatus);
    vl.addComponent(c);
    margin.addComponent(upload);
    margin.setSpacing(true);
    margin.addComponent(vl);
    margin.setSizeFull();

    addComponent(margin);
    addComponent(servicesTable);
    setExpandRatio(margin, 0.2f);
    setExpandRatio(servicesTable, 1f);
  }

  @Override
  public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
    if (this == event.getTabSheet().getSelectedTab()) {
      Boolean serverLogEnabled = LogCustomizer.isServerLogEnabled();
      logEnabled.setValue(serverLogEnabled);
      logStatus.setEnabled(serverLogEnabled);
      servicesTable.reload();
    }
  }
}
