/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import org.apache.tika.mime.MimeTypes;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.activiti.Pair;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.vaadin.ui.Window.Notification.TYPE_ERROR_MESSAGE;
import static com.vaadin.ui.Window.Notification.TYPE_TRAY_NOTIFICATION;

/**
 * Виджет для управления производственным календарем системы
 */
public class BusinessCalendar extends VerticalLayout implements Upload.Receiver, Upload.SucceededListener {
  private final BusinessDatesTable datesTable;
  private ByteArrayOutputStream outputStream;
  private Button removeButton;


  public BusinessCalendar() {
    Upload upload = new Upload();
    upload.setImmediate(false);
    upload.setButtonCaption("Импортировать из файла");
    upload.setReceiver(this);
    upload.addListener(this);

    datesTable = new BusinessDatesTable();
    removeButton = createButton("Удалить");

    VerticalLayout vr = new VerticalLayout();
    vr.setSizeFull();
    vr.setSpacing(true);
    vr.setMargin(true);

    HorizontalLayout horLayout = new HorizontalLayout();
    horLayout.setSizeFull();
    horLayout.setSpacing(true);
    horLayout.setMargin(true);
    horLayout.addComponent(upload);
    horLayout.addComponent(removeButton);
    vr.addComponent(horLayout);
    vr.addComponent(datesTable);

    addComponent(vr);
    vr.setExpandRatio(horLayout, .1f);
    vr.setExpandRatio(datesTable, .9f);
    datesTable.addListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        removeButton.setVisible(event.getProperty().getValue() != null);
      }
    });
    setSizeFull();
  }

  private Button createButton(String buttonCaption) {
    final Button button = new Button(buttonCaption, new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        datesTable.deleteBusinessCalendarItem();
      }
    });
    button.setVisible(false);
    return button;
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
      Pair<Integer, Integer> count = AdminServiceProvider.get().importBusinessCalendar(stream);
      datesTable.refresh();
      getWindow().showNotification(
        "Обновление календаря",
        "Обновлено заявок: " + count.get_1() + ", задач: " + count.get_2(),
        TYPE_TRAY_NOTIFICATION);
    } catch (Exception e) {
      Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
      getWindow().showNotification("Ошибка загрузки", "При импорте дат из файла произошла ошибка " + e.getMessage(), TYPE_ERROR_MESSAGE);
    }
    // закрывать поток вроде бы не надо...
  }
}


