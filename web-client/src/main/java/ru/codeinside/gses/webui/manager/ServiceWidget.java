/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.manager;

import com.vaadin.ui.*;
import ru.codeinside.gses.apservice.ApServiceTable;
import ru.codeinside.gses.manager.ManagerService;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;


public class ServiceWidget extends FormLayout implements Upload.Receiver, Upload.SucceededListener {
    private ByteArrayOutputStream outputStream;

  ApServiceTable c;

    public ServiceWidget(ApServiceTable c) {
      this.c = c;
        Panel panel = new Panel();
        Upload upload = new Upload();
        upload.setImmediate(false);
        upload.setButtonCaption("Импортировать");
        upload.setReceiver(this);
        upload.addListener(this);
        panel.addComponent(upload);
        Panel panel1 = new Panel();
        Button button = new Button("Автоматическая загрузка", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                URL url;
                try {
//                    url = new URL("http://localhost:8888/registry-1.0-SNAPSHOT/services");
                    url = new URL("http://"+getApplication().getURL().getHost()+"/registry-1.0-SNAPSHOT/services");
                    loadServiceData(url.openStream());
                } catch (MalformedURLException e) {
                    System.out.println(e);
                } catch (IOException e) {
                    System.out.println(e);
                }

            }
        });
        panel1.addComponent(button);
        this.setSpacing(true);
        this.addComponent(panel);
//        this.addComponent(panel1);

    }


    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        this.outputStream = new ByteArrayOutputStream();
        return outputStream;
    }

    @Override
    public void uploadSucceeded(Upload.SucceededEvent event) {
        byte[] buffer = outputStream.toByteArray();
        final ByteArrayInputStream data = new ByteArrayInputStream(buffer);
        loadServiceData(data);
    }

    private void loadServiceData(InputStream data){
        String currentUserName = getApplication().getUser().toString();
        try{
            ManagerService.get().loadServiceData(data, currentUserName);
            getWindow().showNotification("Услуги загружены");
            c.refreshTable();
        } catch (Exception e) {
            getWindow().showNotification("Ошибка загрузки", e.getCause().getMessage(),
                    Window.Notification.TYPE_ERROR_MESSAGE);
        }
    }


}


