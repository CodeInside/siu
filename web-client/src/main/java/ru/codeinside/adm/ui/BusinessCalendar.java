/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import org.apache.tika.mime.MimeTypes;
import org.slf4j.LoggerFactory;
import ru.codeinside.adm.AdminServiceProvider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Calendar;

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

    Panel filterPanel = new Panel("Фильтр");
    filterPanel.setSizeFull();
    filterPanel.setHeight("150px");
    filterPanel.addComponent(createYearFilter());

    VerticalLayout vr = new VerticalLayout();
    vr.setSizeFull();
    vr.setHeight("250px");
    vr.setSpacing(true);
    vr.setMargin(true);

    vr.addComponent(upload);
    // vr.addComponent(filterPanel);
    datesTable = new BusinessDatesTable();
    vr.addComponent(datesTable);

    addComponent(vr);

    setExpandRatio(vr, 0.01f);
    setSizeFull();
  }

  private Select createYearFilter() {
    Calendar cal = Calendar.getInstance();
    int currentYear = cal.get(Calendar.YEAR);

    Select yearSelect = new Select("Год");
    for (int year = currentYear - 3; year < currentYear + 3; year++) {
      yearSelect.addItem(new Integer(year));
    }
    return yearSelect;
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
      LoggerFactory.getLogger(this.getClass()).error(e.getMessage(), e);
      getWindow().showNotification("Ошибка загрузки", "При импорте дат из файла произошла ошибка " + e.getMessage(), TYPE_ERROR_MESSAGE);
    }
    // закрывать поток вроде бы не надо...
  }
}


