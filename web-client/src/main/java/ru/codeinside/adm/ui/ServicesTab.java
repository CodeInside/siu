package ru.codeinside.adm.ui;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;

final class ServicesTab extends VerticalLayout implements TabSheet.SelectedTabChangeListener {

  final ServicesTable servicesTable;

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

    HorizontalLayout margin = new HorizontalLayout();
    VerticalLayout vl = new VerticalLayout();
    FormLayout c = new FormLayout();
    c.setMargin(false, false, false, true);
    vl.addComponent(c);
    margin.addComponent(upload);
    margin.setSpacing(true);
    margin.addComponent(vl);
    margin.setSizeFull();

    addComponent(margin);
    addComponent(servicesTable);
    setExpandRatio(margin, 0.1f);
    setExpandRatio(servicesTable, 0.9f);
  }

  @Override
  public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
    if (this == event.getTabSheet().getSelectedTab()) {
      servicesTable.reload();
    }
  }
}
