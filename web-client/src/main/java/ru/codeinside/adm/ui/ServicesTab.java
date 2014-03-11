package ru.codeinside.adm.ui;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;

final class ServicesTab extends VerticalLayout implements TabSheet.SelectedTabChangeListener {

  final ServicesTable servicesTable;

  ServicesTab() {
    servicesTable = new ServicesTable();

    UploadDeployer uploader = new UploadDeployer(servicesTable);
    Upload upload = new Upload("Установка модуля", uploader);
    upload.addListener(uploader);
    upload.setButtonCaption("Загрузить");
    upload.setWidth(100, Sizeable.UNITS_PERCENTAGE);

    setSizeFull();
    setSpacing(true);
    setMargin(true);
    addComponent(upload);
    addComponent(servicesTable);
    setExpandRatio(servicesTable, 1f);
  }

  @Override
  public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
    if (this == event.getTabSheet().getSelectedTab()) {
      servicesTable.reload();
    }
  }
}
