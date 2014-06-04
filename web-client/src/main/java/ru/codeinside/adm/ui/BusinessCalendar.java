/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import org.apache.tika.mime.MimeTypes;
import ru.codeinside.adm.AdminServiceProvider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.vaadin.ui.Window.Notification.TYPE_ERROR_MESSAGE;

/**
 * Виджет для управления производтсвенным календарем системы
 */
public class BusinessCalendar extends VerticalLayout implements Upload.Receiver, Upload.SucceededListener {
  private final BusinessDatesTable datesTable;
  private ByteArrayOutputStream outputStream;

  public BusinessCalendar() {
    Upload upload = new Upload();
    upload.setImmediate(false);
    upload.setButtonCaption("Импортировать из файла");
    upload.setReceiver(this);
    upload.addListener(this);

    VerticalLayout vr = new VerticalLayout();
    vr.setSizeFull();
    vr.setSpacing(true);
    vr.setMargin(true);

    vr.addComponent(upload);
    datesTable = new BusinessDatesTable();
    vr.addComponent(datesTable);

    addComponent(vr);
    vr.setExpandRatio(upload, .1f);
    vr.setExpandRatio(datesTable, .9f);
    setSizeFull();
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
      loadBusinessCalendarData(new ByteArrayInputStream(buffer));
    } else {
      getWindow().showNotification("Ошибка загрузки", "Неверное содержимое загружаемого файла.", TYPE_ERROR_MESSAGE);
    }
  }

  private void loadBusinessCalendarData(ByteArrayInputStream stream) {
    try {
      AdminServiceProvider.get().importBusinessCalendar(stream);
      datesTable.refresh();
    } catch (Exception e) {
      Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
      getWindow().showNotification("Ошибка загрузки", "При импорте дат из файла произошла ошибка " + e.getMessage(), TYPE_ERROR_MESSAGE);
    }
    // закрывать поток вроде бы не надо...
  }
}


