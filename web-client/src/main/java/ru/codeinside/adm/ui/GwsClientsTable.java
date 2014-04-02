/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.provider.CachingLocalEntityProvider;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.InfoSystemService;
import ru.codeinside.gws.api.Revision;

import java.util.Arrays;

final class GwsClientsTable extends FilterTable {


  GwsClientSink sink;

  final JPAContainer<InfoSystemService> container;

  boolean selectionMode;

  GwsClientsTable() {
    super("Зарегистрированные модули");
    container = new JPAContainer<InfoSystemService>(InfoSystemService.class);
    container.setEntityProvider(new CachingLocalEntityProvider<InfoSystemService>(InfoSystemService.class, AdminServiceProvider.get().getMyPU().createEntityManager()));
    setFilterBarVisible(true);
    setContainerDataSource(container);
    container.addNestedContainerProperty("infoSystem.code");
    container.addNestedContainerProperty("source.code");
    setVisibleColumns(new Object[]{"id", "sname", "sversion", "infoSystem.code", "source.code", "address", "revision", "name", "available", "logEnabled"});
    setFilterDecorator(new FilterDecorator_());
    setFilterGenerator(new FilterGenerator_(Arrays.asList("id"), Arrays.asList("available", "logEnabled")));
    setImmediate(true);
    setSizeFull();
    setPageLength(0);
    setColumnHeaders(new String[]{
      "Id", "Имя", "Вер.", "Код системы", "Источник", "Адрес", "Рев.", "Описание", "Доступен", "Журнал"
    });
    setSelectable(true);
    setColumnExpandRatio("id", 0.01f);
    setColumnExpandRatio("infoSystem.code", 0.1f);
    setColumnExpandRatio("address", 0.1f);
    setColumnExpandRatio("available", 0.05f);

    addGeneratedColumn("available", new YesColumnGenerator());
    addGeneratedColumn("logEnabled", new YesColumnGenerator());


    addListener(new ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        if (selectionMode) {
          return;
        }
        Object itemId = event.getProperty().getValue();
        Item item = itemId == null ? null : getItem(itemId);
        if (item != null) {
          String name = (String) item.getItemProperty("sname").getValue();
          String version = (String) item.getItemProperty("sversion").getValue();
          if (sink != null) {
            Long id = (Long) item.getItemProperty("id").getValue();
            String infoSys = (String) item.getItemProperty("infoSystem.code").getValue();
            String source = (String) item.getItemProperty("source.code").getValue();
            String url = (String) item.getItemProperty("address").getValue();
            Revision revision = Revision.valueOf((String) item.getItemProperty("revision").getValue());
            String description = (String) item.getItemProperty("name").getValue();
            Boolean available = item.getItemProperty("available") == null ? null : (Boolean) item.getItemProperty("available").getValue();
            Boolean logEnabled = item.getItemProperty("logEnabled") == null ? null : (Boolean) item.getItemProperty("logEnabled").getValue();
            sink.selectClient(id, revision, url, name, version, infoSys, source, description, available, logEnabled);
          }
        }
      }
    });
  }

  void setSink(GwsClientSink sink) {
    this.sink = sink;
  }

  boolean setCurrent(String name, String version, boolean mode) {
    selectionMode = mode;
    try {
      container.refresh();
      if (name != null && version != null) {
        Container container = getContainerDataSource();
        for (Object itemId : container.getItemIds()) {
          Object sname = container.getContainerProperty(itemId, "sname").getValue();
          Object sversion = container.getContainerProperty(itemId, "sversion").getValue();
          if (name.equals(sname) && version.equals(sversion)) {
            setValue(itemId);
            return true;
          }
        }
      }
      setValue(null);
    } finally {
      selectionMode = false;
    }
    return false;
  }
}
