/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.webui.gws.TRef;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.ServiceDefinition;
import ru.codeinside.gws.api.ServiceDefinitionParser;

import javax.xml.namespace.QName;
import java.util.Map;

final class ActiveGwsClientsTable extends Table {

  GwsClientSink sink;

  String currentName;
  String currentVersion;
  boolean selectionMode;

  void setSink(GwsClientSink sink) {
    this.sink = sink;
  }

  ActiveGwsClientsTable() {
    super("Активные модули");
    addStyleName("small striped");
    setImmediate(true);
    setSelectable(true);
    setSizeFull();
    addContainerProperty("name", String.class, null);
    addContainerProperty("version", String.class, null);
    setVisibleColumns(new String[]{"name", "version"});
    setColumnHeaders(new String[]{"Имя", "Вер."});
    setColumnExpandRatio("name", 0.8f);
    setPageLength(0);
    setSortContainerPropertyId("name");

    addListener(new ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        if (selectionMode) {
          return;
        }
        Item item = getItem(event.getProperty().getValue());
        if (item != null) {
          String name = (String) item.getItemProperty("name").getValue();
          String version = (String) item.getItemProperty("version").getValue();
          TRef<Client> ref = AdminServiceProvider.get().getClientRefByNameAndVersion(name, version);
          if (ref != null) {
            currentName = name;
            currentVersion = version;
            Revision revision = ref.getRef().getRevision();
            final ServiceDefinitionParser serviceDefinitionParser = AdminServiceProvider.get().getServiceDefinitionParser();
            ServiceDefinition serviceDefinition = serviceDefinitionParser.parseServiceDefinition(ref.getRef().getWsdlUrl());
            String address = "";
            for (Map.Entry<QName, ServiceDefinition.Service> service : serviceDefinition.services.entrySet()) {
              for (Map.Entry<QName, ServiceDefinition.Port> port : service.getValue().ports.entrySet()) {
                address = port.getValue().soapAddress;
                break;
              }
              break;
            }
            String osgiName = ref.getName();
            sink.selectClient(null, revision, address, osgiName, version, null, null, null, null, null);
          }

        } else {
          currentName = null;
          currentVersion = null;
        }
      }
    });
  }

  void setCurrent(String newName, String newVersion) {
    selectionMode = true;
    currentName = newName;
    currentVersion = newVersion;
    try {
      removeAllItems();
      int i = 0;
      int currentId = -1;
      for (TRef client : AdminServiceProvider.get().getClientRefs()) {
        String name = client.getName();
        String version = client.getVersion();
        addItem(new Object[]{name, version}, i);
        if (name.equals(currentName) && version.equals(currentVersion)) {
          currentId = i;
        }
        i++;
      }
      if (currentId >= 0) {
        setValue(currentId);
      }
      sort();
    } finally {
      selectionMode = false;
    }
  }
}
