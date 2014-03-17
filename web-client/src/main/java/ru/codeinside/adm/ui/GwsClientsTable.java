/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.google.common.collect.Lists;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Table;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.InfoSystem;
import ru.codeinside.adm.database.InfoSystemService;
import ru.codeinside.gses.activiti.Pair;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryContainer;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryDefinition;
import ru.codeinside.gses.lazyquerycontainer.Query;
import ru.codeinside.gses.lazyquerycontainer.QueryDefinition;
import ru.codeinside.gses.lazyquerycontainer.QueryFactory;
import ru.codeinside.gws.api.Revision;

import java.io.Serializable;
import java.util.List;

final class GwsClientsTable extends Table {


  GwsClientSink sink;

  final LazyQueryContainer container;

  boolean selectionMode;

  GwsClientsTable() {
    super("Зарегистрированные модули");
    InfoSysServiceQ query = new InfoSysServiceQ();
    container = new LazyQueryContainer(query, query.getFactory());
    setContainerDataSource(container);

    setImmediate(true);
    setSizeFull();
    setPageLength(0);
    // порядок объявлений в констуркторе InfoSysServiceQ
    setColumnHeaders(new String[]{
      "Id", "Имя", "Вер.", "Код системы", "Адрес", "Рев.", "Описание", "Доступен", "Журнал"
    });
    setSelectable(true);
    setColumnExpandRatio("id", 0.01f);
    setColumnExpandRatio("infoSystem", 0.1f);
    setColumnExpandRatio("address", 0.1f);
    setColumnExpandRatio("available", 0.05f);


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
            String infoSys = (String) item.getItemProperty("infoSystem").getValue();
            String url = (String) item.getItemProperty("address").getValue();
            Revision revision = Revision.valueOf((String) item.getItemProperty("revision").getValue());
            String description = (String) item.getItemProperty("name").getValue();
            Boolean available = (Boolean) item.getItemProperty("available").getValue();
            Boolean logEnabled = (Boolean) item.getItemProperty("logenabled").getValue();
            sink.selectClient(id, revision, url, name, version, infoSys, description, available, logEnabled);
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


  final static class InfoSysServiceQ extends LazyQueryDefinition {

    private static final long serialVersionUID = 1L;

    public InfoSysServiceQ() {
      super(false, 10);
      addProperty("id", String.class, null, true, true);
      addProperty("sname", String.class, null, true, true);
      addProperty("sversion", String.class, null, true, true);
      addProperty("infoSystem", String.class, null, true, true);
      addProperty("address", String.class, null, true, true);
      addProperty("revision", String.class, null, true, true);
      addProperty("name", String.class, null, true, true);
      addProperty("available", Boolean.class, null, true, true);
      addProperty("logenabled", Boolean.class, null, true, true);
    }

    public Factory getFactory() {
      return new Factory();
    }

    final static class Factory implements QueryFactory, Serializable {
      private static final long serialVersionUID = 1L;

      @Override
      public void setQueryDefinition(QueryDefinition queryDefinition) {
      }

      @Override
      public Query constructQuery(Object[] sortPropertyIds, boolean[] asc) {
        return new QueryImpl(convertTypes(sortPropertyIds), asc);
      }

      private String[] convertTypes(final Object[] objects) {
        boolean notEmpty = objects != null && objects.length > 0;
        String[] strings = null;
        if (notEmpty) {
          strings = new String[objects.length];
          for (int i = 0; i < objects.length; i++) {
            strings[i] = (String) objects[i];
          }
        }
        return strings;
      }
    }

    final static class QueryImpl implements Query, Serializable {

      private static final long serialVersionUID = 1L;

      final private String[] ids;
      final private boolean[] asc;

      public QueryImpl(String[] ids, boolean[] asc) {
        this.ids = ids;
        this.asc = asc;
      }

      @Override
      public int size() {
        return AdminServiceProvider.get().countInfoSystemServices();
      }

      @Override
      public List<Item> loadItems(int start, int count) {
        List<InfoSystemService> systems = AdminServiceProvider.get().queryInfoSystemServices(ids, asc, start, count);
        List<Item> items = Lists.newArrayListWithExpectedSize(systems.size());
        for (InfoSystemService s : systems) {
          final PropertysetItem item = new PropertysetItem();
          InfoSystem infoSystem = s.getInfoSystem();
          item.addItemProperty("id", new ObjectProperty<Long>(s.getId()));
          item.addItemProperty("sname", new ObjectProperty<String>(s.getSname()));
          item.addItemProperty("sversion", new ObjectProperty<String>(s.getSversion()));
          item.addItemProperty("infoSystem", new ObjectProperty<String>(infoSystem == null ? "" : infoSystem.getCode()));
          item.addItemProperty("address", new ObjectProperty<String>(s.getAddress()));
          item.addItemProperty("revision", new ObjectProperty<String>(s.getRevision()));
          item.addItemProperty("name", new ObjectProperty<String>(s.getName()));
          item.addItemProperty("available", new ObjectProperty<Boolean>(s.isAvailable()));
          item.addItemProperty("logenabled", new ObjectProperty<Boolean>(s.isLogEnabled()));
          items.add(item);
        }
        return items;
      }

      @Override
      public void saveItems(List<Item> addedItems, List<Item> modifiedItems, List<Item> removedItems) {
        throw new UnsupportedOperationException();
      }

      @Override
      public boolean deleteAllItems() {
        throw new UnsupportedOperationException();
      }

      @Override
      public Item constructItem() {
        throw new UnsupportedOperationException();
      }

    }

  }
}
