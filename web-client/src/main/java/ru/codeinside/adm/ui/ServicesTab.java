package ru.codeinside.adm.ui;

import com.vaadin.data.Property;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.API;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.osgi.LogCustomizer;

import java.util.logging.Logger;

final class ServicesTab extends VerticalLayout implements TabSheet.SelectedTabChangeListener {

  final ServicesTable servicesTable;
  final CheckBox logEnabled;

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

    logEnabled = new CheckBox("Вести журнал сообщений, в зависимости от настроек модулей");
    logEnabled.setImmediate(true);
    logEnabled.setValue(LogCustomizer.isServerLogEnabled());
    logEnabled.addListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        LogCustomizer.setShouldWriteServerLog(Boolean.TRUE.equals(event.getProperty().getValue()));
        servicesTable.reload();
      }
    });


    // отступы сверху и снизу
    HorizontalLayout margin = new HorizontalLayout();
    margin.setMargin(true, false, true, false);
    margin.addComponent(logEnabled);

    addComponent(upload);
    addComponent(margin);
    addComponent(servicesTable);
    setExpandRatio(servicesTable, 1f);
  }

  @Override
  public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
    if (this == event.getTabSheet().getSelectedTab()) {
      logEnabled.setValue(LogCustomizer.isServerLogEnabled());
      servicesTable.reload();
    }
  }
}
