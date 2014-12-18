/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.tepi.filtertable.FilterTable;

import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.beans.DirectoryBeanProvider;
import ru.codeinside.gses.webui.Flash;
import au.com.bytecode.opencsv.CSVReader;

import com.vaadin.ui.Table;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Window;

public class UploadDirectory implements Upload.Receiver, Upload.FinishedListener {

  private static final long serialVersionUID = 11323324231L;

  static Logger log = Logger.getLogger(UploadDirectory.class.getName());

  byte[] csvData;

  final FilterTable directoryTable;
  final Table dirMapTable;

  public UploadDirectory(FilterTable directoryTable, Table dirMapTable) {
    this.directoryTable = directoryTable;
    this.dirMapTable = dirMapTable;
  }

  @Override
  public OutputStream receiveUpload(String filename, String mimeType) {
    return new ByteArrayOutputStream() {
      @Override
      public void close() throws IOException {
        csvData = Arrays.copyOf(buf, count);
      }
    };
  }

  @Override
  public void uploadFinished(Upload.FinishedEvent finishedEvent) {
    CSVReader csvReader = null;
    try {
      csvReader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(csvData), "UTF-8"));
      String[] record;
      int lineNum = 0;
      while ((record = csvReader.readNext()) != null) {
        lineNum++;
        if (record.length != 3) {
          finishedEvent
              .getUpload()
              .getWindow()
              .showNotification("Должен быть csv файл с разделителем ','. Проверьте данные, строка: " + lineNum,
                  Window.Notification.TYPE_ERROR_MESSAGE);
          log.log(Level.SEVERE, "can't parse cvs file, err-line:  " + lineNum);
          return;
        }
        String dirName = record[0].trim();
        DirectoryBeanProvider.get().create(dirName);
        AdminServiceProvider.get().createLog(Flash.getActor(), "Directory", dirName, "create", "Upload create", true);
        String key = record[1].trim();
        String value = record[2].trim();
        DirectoryBeanProvider.get().add(dirName, key, value);
        AdminServiceProvider.get().createLog(Flash.getActor(), "Directory value", dirName, "add",
            "key => ".concat(key).concat("value =>").concat(value), true);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return;
    } finally {
      if (csvReader != null) {
        try {
          csvReader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    ManagerWorkplace.refreshDirectoryTable(directoryTable);
    Object rowId = directoryTable.getValue();
    if (rowId != null) {
      final String dirName = (String) directoryTable.getContainerProperty(rowId, "name").getValue();
      ManagerWorkplace.reloadMap(dirName, dirMapTable);
    }
  }
}
