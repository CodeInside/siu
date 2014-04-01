/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.filter.Filters;
import com.vaadin.addon.jpacontainer.provider.CachingLocalEntityProvider;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.InfoSystemService;
import ru.codeinside.gws.api.Revision;


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
    setFilterDecorator(new TableEmployeeFilterDecorator());
    setFilterGenerator(new IdFilterGenerator() {

      @Override
      public Container.Filter generateFilter(Object propertyId, Object value) {
        if ("id".equals(propertyId)) {
          try {
            return Filters.eq(propertyId, Long.valueOf(value.toString()));
          } catch (NumberFormatException e) {
            return Filters.isNull(propertyId);
          }
        }
        if ("available".equals(propertyId) || "logEnabled".equals(propertyId)) {
          return Filters.eq(propertyId, value);
        }
        return null;
      }
    });
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


  final static class InfoSysServiceQ extends LazyQueryDefinition implements QueryFactory, Serializable {

    private static final long serialVersionUID = 1L;

    public InfoSysServiceQ() {
      super(false, 10);
      addProperty("id", String.class, null, true, true);
      addProperty("sname", String.class, null, true, true);
      addProperty("sversion", String.class, null, true, true);
      addProperty("infoSystem", String.class, null, true, true);
      addProperty("source", String.class, null, true, true);
      addProperty("address", String.class, null, true, true);
      addProperty("revision", String.class, null, true, true);
      addProperty("name", String.class, null, true, true);
      addProperty("available", Boolean.class, null, true, true);
      addProperty("logenabled", Boolean.class, null, true, true);
    }

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
        InfoSystem source = s.getSource();
        item.addItemProperty("id", new ObjectProperty<Long>(s.getId()));
        item.addItemProperty("sname", new ObjectProperty<String>(s.getSname()));
        item.addItemProperty("sversion", new ObjectProperty<String>(s.getSversion()));
        item.addItemProperty("infoSystem", new ObjectProperty<String>(infoSystem == null ? "" : infoSystem.getCode()));
        item.addItemProperty("source", new ObjectProperty<String>(source == null ? null : source.getCode(), String.class));
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
