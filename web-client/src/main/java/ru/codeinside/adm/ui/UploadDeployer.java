/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.ui.Table;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Window;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import ru.codeinside.gses.webui.Configurator;
import ru.codeinside.gses.webui.manager.ManagerWorkplace;
import ru.codeinside.gses.webui.utils.Components;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import static ru.codeinside.gses.webui.utils.JarParseUtils.getChildForNlByNames;
import static ru.codeinside.gses.webui.utils.JarParseUtils.isOsgiComponent;
import static ru.codeinside.gses.webui.utils.JarParseUtils.readXml;

final class UploadDeployer implements Upload.Receiver, Upload.SucceededListener {

  private static final long serialVersionUID = 11324239231L;

  String filename;
  byte[] fileData;

  final ServicesTable table;

  public UploadDeployer(ServicesTable table) {
    this.table = table;
  }

  @Override
  public OutputStream receiveUpload(String name, String mimeType) {
    filename = name;
    return new ByteArrayOutputStream() {
      @Override
      public void close() throws IOException {
        fileData = Arrays.copyOf(buf, count);
      }
    };
  }

  @Override
  public void uploadSucceeded(Upload.SucceededEvent event) {
    try {
      checkSupportInterface(event, "ru.codeinside.gws.api.Server");

      String name = filename.substring(0, filename.length() - 4);

      Configurator.getDeployer().deploy(new ByteArrayInputStream(fileData), "--name=" + name, "--availabilityenabled=true", "--type=osgi", "--target=server");

      table.reload();
    } catch (Exception e) {
      e.printStackTrace();
      String errorMessage = e.getMessage() != null ? e.getMessage() : "Ошибка развертывания, смотрите лог файл";
      Components.showMessage(event, errorMessage, Window.Notification.TYPE_ERROR_MESSAGE);
    }
  }

  private void checkSupportInterface(Upload.SucceededEvent event, String supportedInterface) throws Exception {
    if (!event.getFilename().endsWith(".jar")) {
      throw new Exception("Нужен jar файл");
    }
    JarInputStream jarStream = null;
    try {
      jarStream = new JarInputStream(new ByteArrayInputStream(fileData));
      if (!isOsgiComponent(jarStream)) {
        throw new Exception("Нужен osgi компонент");
      }
      if (!hasApiServer(supportedInterface, jarStream)) {
        throw new Exception("Не реализует интерфейс [" + supportedInterface + "]");
      }
    } finally {
      if (jarStream != null) {
        jarStream.close();
      }
    }
  }

  //TODO переделать for(некорректно работает с getNextEntry)
  private boolean hasApiServer(String supportedInterface, JarInputStream jarStream) throws IOException, SAXException, ParserConfigurationException {
    for (String xmlPath : jarStream.getManifest().getMainAttributes().getValue("Service-Component").split(",")) {
      ZipEntry nextZipEntry = jarStream.getNextEntry();
      while (nextZipEntry != null && !xmlPath.equals(nextZipEntry.getName())) {
        nextZipEntry = jarStream.getNextEntry();
      }
      Document xmlDoc = readXml(jarStream);

      Node node = getChildForNlByNames(xmlDoc.getChildNodes(), "component", "service", "provide");
      if (node != null) {
        String interfaceValue = node.getAttributes().getNamedItem("interface").getNodeValue();

        if (StringUtils.isNotEmpty(interfaceValue) && supportedInterface.equals(interfaceValue.trim())) {
          return true;
        }
      }
    }
    return false;
  }
}
