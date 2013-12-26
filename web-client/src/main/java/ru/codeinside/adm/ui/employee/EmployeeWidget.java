/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui.employee;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.apache.tika.mime.MimeTypes;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.ui.TreeTableOrganization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


final public class EmployeeWidget extends VerticalLayout implements Upload.Receiver, Upload.SucceededListener {

  private TableAllEmployee tableEmployee;
  private ByteArrayOutputStream outputStream;
  private TreeTable table;

  public EmployeeWidget(boolean lockedFilterValue, TreeTable table) {
    this.table = table;
    Upload upload = new Upload();
    upload.setImmediate(false);
    upload.setButtonCaption("Импортировать из файла");
    upload.setReceiver(this);
    upload.addListener(this);
/*    Panel panel1 = new Panel();
    Button button = new Button("Автоматическая загрузка", new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        try {
          URL url = new URL(getApplication().getURL(), "/registry/structures");
          loadEmployeeData(url.openStream());
        } catch (MalformedURLException e) {
          getWindow().showNotification("Сбой " + e.getMessage());
        } catch (IOException e) {
          getWindow().showNotification("Сбой " + e.getMessage());
        }
      }
    });
    panel1.addComponent(button);*/
    tableEmployee = new TableAllEmployee(lockedFilterValue);
    tableEmployee.setSizeFull();
    HorizontalLayout hr = new HorizontalLayout();
    hr.setSizeFull();
    hr.setHeight("70px");
    hr.setSpacing(true);
    hr.setMargin(true);
    hr.addComponent(upload);
    addComponent(hr);
    addComponent(tableEmployee);
    tableEmployee.addButtonToLayout(hr);
    setExpandRatio(hr, 0.01f);
    setExpandRatio(tableEmployee, 0.99f);
    setSizeFull();
  }

  public void refreshList() {
    tableEmployee.refreshList();
  }

  @Override
  public OutputStream receiveUpload(String filename, String mimeType) {
    this.outputStream = new ByteArrayOutputStream();
    return outputStream;
  }

  @Override
  public void uploadSucceeded(Upload.SucceededEvent event) {
    byte[] buffer = outputStream.toByteArray();
    boolean isPlainText = MimeTypes.PLAIN_TEXT.equals(new MimeTypes().getMimeType(buffer).getName());
    if (isPlainText) {
      loadEmployeeData(new ByteArrayInputStream(buffer));
    } else {
      getWindow().showNotification("Ошибка загрузки", "Неверное содержимое загружаемого файла.", Window.Notification.TYPE_ERROR_MESSAGE);
    }
  }

  private void loadEmployeeData(InputStream data) {
    String currentUserName = getApplication().getUser().toString();
    try {
      AdminServiceProvider.get().loadEmployeeData(data, currentUserName);
      getWindow().showNotification("Исполнители загружены");
      if (table != null) {
        table.getContainerDataSource().removeAllItems();
        TreeTableOrganization.fillTable(table);
      }
    } catch (Exception e) {
      getWindow().showNotification("Ошибка загрузки", e.getCause().getMessage(), Window.Notification.TYPE_ERROR_MESSAGE);
    }
  }
}


