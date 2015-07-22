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
import com.vaadin.data.Validator;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.IsNull;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.InfoSystem;
import ru.codeinside.adm.database.ServiceUnavailable;
import ru.codeinside.gses.API;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryContainer;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryDefinition;
import ru.codeinside.gses.lazyquerycontainer.Query;
import ru.codeinside.gses.lazyquerycontainer.QueryDefinition;
import ru.codeinside.gses.lazyquerycontainer.QueryFactory;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.components.api.IRefresh;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.jpa.ActivitiEntityManager;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class GwsClientsTab extends HorizontalLayout implements TabSheet.SelectedTabChangeListener {

  final GwsClientsTable gwsClientsTable;
  final ComboBox infosys;
  final ActiveGwsClientsTable activeGwsClientsTable;
  final FilterTable serviceUnavailableTable;
  final UnavailableServiceQ.Factory unavailableQF;
  final JPAContainer<ServiceUnavailable> unavailableСontainer;
  final GwsClientSink sink;
  final Button removeButton;
  final CheckBox logEnabled;

  String currentName;
  String currentVersion;

  final LazyQueryContainer systemContainer;
  final LazyQueryContainer sourceContainer;
  final ComboBox source;


  GwsClientsTab() {
    String fieldWidth = "300px";

    InfoSysQ query = new InfoSysQ(false);
    systemContainer = new LazyQueryContainer(query, query);
    infosys = new ComboBox("Информационная система", systemContainer);
    infosys.setImmediate(true);
    infosys.setWidth(fieldWidth);
    infosys.setItemCaptionPropertyId("name");
    infosys.setDescription("Выберите ИС поставщика, чтобы связать сервис с ней");
    infosys.setInvalidAllowed(true);
    infosys.setInvalidCommitted(false);
    infosys.setRequired(true);
    infosys.setRequiredError("Информационная система - обязательно к заполнению");


    InfoSysQ sourceQuery = new InfoSysQ(true);
    sourceContainer = new LazyQueryContainer(sourceQuery, sourceQuery);
    source = new ComboBox("Источник", sourceContainer);
    source.setImmediate(true);
    source.setWidth(fieldWidth);
    source.setItemCaptionPropertyId("name");
    source.setDescription("Система-источник");
    source.setInvalidAllowed(true);
    source.setRequired(false);


    final TextField id = text("Id", fieldWidth, false, false, null);
    final TextField address = text("Адрес", fieldWidth, true, true, "Адрес HTTP подключения к сервису поставщика");
    final TextField revision = text("Ревизия", fieldWidth, false, true, "Ревизия методических рекомендаций СМЭВ");
    final TextField sname = text("Имя", fieldWidth, false, true, "Имя, по которому осуществляться вызов в маршрутах");
    final TextField sversion = text("Версия", fieldWidth, false, true, null);
    final TextField name = text("Описание", fieldWidth, true, true, null);
    final CheckBox available = new CheckBox("Доступен в маршрутах");
    logEnabled = new CheckBox("Вести журнал сообщений");
    logEnabled.setReadOnly(!Boolean.TRUE.equals(AdminServiceProvider.getBoolProperty(API.ENABLE_CLIENT_LOG)));

    gwsClientsTable = new GwsClientsTable();

    final Form form = new Form();
    form.addField("id", id);
    form.addField("sname", sname);
    form.addField("sversion", sversion);
    form.addField("revision", revision);
    form.addField("infosys", infosys);
    form.addField("source", source);
    form.addField("name", name);
    form.addField("address", address);
    form.addField("available", available);
    form.addField("log", logEnabled);
    form.setEnabled(false);
    form.setWriteThrough(false);


    final Button commit = new Button("Сохранить", new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        try {
          form.commit();
        } catch (Validator.InvalidValueException e) {
          return;
        }
        String infosysCode = (String) infosys.getContainerProperty(infosys.getValue(), "code").getValue();
        String sourceCode = null;
        if (source.getValue() != null) {
          sourceCode = (String) source.getContainerProperty(source.getValue(), "code").getValue();
        }
        String sversionValue = (String) sversion.getValue();
        String snameValue = (String) sname.getValue();
        Long entityId = (Long) id.getValue();

        boolean serviceEnabled = Boolean.TRUE.equals(available.getValue());
        boolean _logEnabled = Boolean.TRUE.equals(logEnabled.getValue());
        String revisionName = ((Revision) revision.getValue()).name();
        String description = (String) name.getValue();
        String url = (String) address.getValue();

        if (entityId != null) {
          AdminServiceProvider
            .get()
            .updateInfoSystemService(entityId.toString(),
              infosysCode, sourceCode, url, revisionName, snameValue, sversionValue,
              description, serviceEnabled, _logEnabled
            );
          AdminServiceProvider
            .get()
            .createLog(Flash.getActor(), "InfoSystemService", entityId.toString(),
              "update", "Update from adm interface", true);
        } else {
          // TODO: strange method name
          if (AdminServiceProvider.get().findUsesInfoSystemService(snameValue, sversionValue)) {
            Long infoSystemService = AdminServiceProvider.get().createInfoSystemService(
              infosysCode, sourceCode, url, revisionName, snameValue, sversionValue,
              description, serviceEnabled, _logEnabled
            );
            id.setValue(infoSystemService);
            removeButton.setEnabled(true);
            AdminServiceProvider
              .get()
              .createLog(Flash.getActor(), "InfoSystemService", infoSystemService.toString(),
                "create", "Create from adm interface", true);
          } else {
            getWindow()
              .showNotification("Комбинация OSGI-имени и OSGI-версии должна быть уникальной",
                Window.Notification.TYPE_ERROR_MESSAGE);
            return;
          }
        }
        activeGwsClientsTable.setCurrent(currentName, currentVersion);
        ((JPAContainer) gwsClientsTable.getContainerDataSource()).refresh();
        gwsClientsTable.setCurrent(currentName, currentVersion, true);
      }
    });

    final Button clean = new Button("Очистить", new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        disableForm();
      }
    });

    removeButton = new Button("Удалить", new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        Long _id = (Long) id.getValue();
        AdminServiceProvider.get().removeInfoSystemService(_id);
        gwsClientsTable.container.refresh();
        disableForm();
      }
    });


    final HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    buttons.setWidth(100, UNITS_PERCENTAGE);
    buttons.addComponent(clean);
    buttons.addComponent(commit);
    buttons.addComponent(removeButton);
    buttons.setComponentAlignment(clean, Alignment.MIDDLE_LEFT);
    buttons.setComponentAlignment(commit, Alignment.MIDDLE_CENTER);
    buttons.setComponentAlignment(removeButton, Alignment.MIDDLE_RIGHT);
    form.setFooter(buttons);

    activeGwsClientsTable = new ActiveGwsClientsTable();

    HorizontalLayout newContent = new HorizontalLayout();
    newContent.setMargin(false, false, false, true);
    newContent.setSpacing(true);
    newContent.setSizeFull();
    newContent.addComponent(form);

    UnavailableServiceQ unavailableQuery = new UnavailableServiceQ();
    unavailableQF = unavailableQuery.getFactory();
    unavailableСontainer = new JPAContainer<ServiceUnavailable>(ServiceUnavailable.class);
    unavailableСontainer.setEntityProvider(
      new CachingLocalEntityProvider<ServiceUnavailable>(
        ServiceUnavailable.class,
        ActivitiEntityManager.INSTANCE
      )
    );
    unavailableСontainer.addContainerFilter(new IsNull("infoSystemService"));
    serviceUnavailableTable = new FilterTable();
    serviceUnavailableTable.setCaption("Недоступность сервиса:");
    serviceUnavailableTable.setContainerDataSource(unavailableСontainer);
    serviceUnavailableTable.setSizeFull();
    serviceUnavailableTable.setWidth("100%");
    serviceUnavailableTable.setImmediate(true);
    serviceUnavailableTable.setVisibleColumns(new String[]{"name", "address", "createdDate"});
    serviceUnavailableTable.setColumnHeaders(new String[]{"Название сервиса", "Адрес", "Дата-время"});
    serviceUnavailableTable.setFilterBarVisible(true);
    serviceUnavailableTable.setFilterDecorator(new FilterDecorator_());
    serviceUnavailableTable.addGeneratedColumn("createdDate", new CustomTable.ColumnGenerator() {
      @Override
      public Object generateCell(CustomTable source, Object itemId, Object columnId) {
        Container containerDataSource = source.getContainerDataSource();
        Property containerProperty = containerDataSource.getContainerProperty(itemId, columnId);
        if (containerProperty != null) {
          Object object = containerProperty.getValue();
          return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(object);
        }
        return null;
      }
    });

    VerticalLayout vl = new VerticalLayout();
    vl.addComponent(serviceUnavailableTable);
    vl.setSizeFull();
    vl.setSpacing(true);
    vl.setExpandRatio(serviceUnavailableTable, 1f);
    newContent.addComponent(vl);

    VerticalLayout right = new VerticalLayout();
    right.setSizeFull();
    right.setSpacing(true);
    right.addComponent(gwsClientsTable);
    right.addComponent(newContent);
    right.setExpandRatio(newContent, 0.6f);
    right.setExpandRatio(gwsClientsTable, 0.4f);

    setSpacing(true);
    setSizeFull();

    addComponent(activeGwsClientsTable);
    addComponent(right);
    setExpandRatio(activeGwsClientsTable, 0.15f);
    setExpandRatio(right, 0.85f);
    setMargin(true);

    sink = new GwsClientSink() {
      @Override
      public void selectClient(Long _id, Revision _revision, String _url, String _componentName, String _version,
                               String _infoSys, String _source, String _description, Boolean _available, Boolean _logEnabled) {

        // re-routing
        if (_id == null && gwsClientsTable.setCurrent(_componentName, _version, false)) {
          return;
        }
        currentName = _componentName;
        currentVersion = _version;

        boolean enabled = _componentName != null;
        form.setEnabled(enabled);
        form.setValidationVisible(false);
        buttons.setEnabled(enabled);
        removeButton.setEnabled(_id != null);

        id.setValue(_id);
        infosys.setValue(findInfoSystem(infosys, _infoSys));
        source.setValue(findInfoSystem(source, _source));
        address.setValue(_url);
        revision.setValue(_revision);
        sname.setValue(_componentName);
        sversion.setValue(_version);
        name.setValue(_description);
        available.setValue(_available);
        logEnabled.setReadOnly(false);
        logEnabled.setValue(_logEnabled);
        logEnabled.setReadOnly(!Boolean.TRUE.equals(AdminServiceProvider.getBoolProperty(API.ENABLE_CLIENT_LOG)));

        activeGwsClientsTable.setCurrent(_componentName, _version);
        serviceUnavailableTable.setEnabled(_id != null && _id > 0);
        unavailableQF.setInfoSystemId(_id);
        unavailableСontainer.removeAllContainerFilters();
        unavailableСontainer.addContainerFilter(new Compare.Equal("infoSystemService.id", _id));
      }
    };
    gwsClientsTable.setSink(sink);
    activeGwsClientsTable.setSink(sink);
  }

  private void disableForm() {
    currentName = null;
    currentVersion = null;
    sink.selectClient(null, null, null, null, null, null, null, null, null, null);
  }

  @Override
  public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
    if (this == event.getTabSheet().getSelectedTab()) {
      activeGwsClientsTable.setCurrent(currentName, currentVersion);
      gwsClientsTable.setCurrent(currentName, currentVersion, true);
      unavailableСontainer.refresh();
      refreshSystem(infosys);
      refreshSystem(source);

      logEnabled.setReadOnly(!Boolean.TRUE.equals(AdminServiceProvider.getBoolProperty(API.ENABLE_CLIENT_LOG)));
    }
  }

  void refreshSystem(ComboBox box) {
    Object itemId = box.getValue();
    String systeId = null;
    if (itemId != null) {
      systeId = (String) box.getContainerProperty(itemId, "code").getValue();
    }
    ((IRefresh) box.getContainerDataSource()).refresh();
    if (systeId != null) {
      box.setValue(findInfoSystem(box, systeId));
    }
  }

  Object findInfoSystem(Container.Viewer viewer, String code) {
    if (code != null) {
      Container container = viewer.getContainerDataSource();
      for (Object itemId : container.getItemIds()) {
        if (code.equals(container.getContainerProperty(itemId, "code").getValue())) {
          return itemId;
        }
      }
    }
    return null;
  }

  TextField text(String name, String fieldWidth, boolean enabled, boolean required, String description) {
    final TextField field = new TextField(name);
    field.setImmediate(true);
    field.setNullRepresentation("");
    field.setWidth(fieldWidth);
    field.setMaxLength(255);
    field.setEnabled(enabled);
    field.setDescription(description);
    if (required) {
      String msg = name + " - обязательно к заполнению";
      field.setRequired(true);
      field.setRequiredError(msg);
      field.addValidator(new AbstractValidator(msg) {
        @Override
        public boolean isValid(Object value) {
          return value != null && !value.toString().trim().isEmpty();
        }
      });
    }
    return field;
  }

  static final class InfoSysQ extends LazyQueryDefinition implements QueryFactory, Serializable {

    private static final long serialVersionUID = 1L;

    final boolean source;

    public InfoSysQ(boolean source) {
      super(false, 10);
      this.source = source;
      addProperty("code", String.class, null, true, true);
      addProperty("name", String.class, null, true, true);
    }

    @Override
    public void setQueryDefinition(QueryDefinition queryDefinition) {
    }

    @Override
    public Query constructQuery(Object[] sortPropertyIds, boolean[] asc) {
      return new QueryImpl(source, convertTypes(sortPropertyIds), asc);
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

    final boolean source;
    final String[] ids;
    final boolean[] asc;

    public QueryImpl(boolean source, String[] ids, boolean[] asc) {
      this.source = source;
      this.ids = ids;
      this.asc = asc;
    }

    @Override
    public int size() {
      return AdminServiceProvider.get().countInfoSystems(source);
    }

    @Override
    public List<Item> loadItems(final int start, final int count) {
      final List<InfoSystem> systems = AdminServiceProvider.get().queryInfoSystems(source, ids, asc, start, count);
      final List<Item> items = new ArrayList<Item>(systems.size());
      for (final InfoSystem s : systems) {
        final PropertysetItem item = new PropertysetItem();
        item.addItemProperty("code", new ObjectProperty<String>(s.getCode()));
        item.addItemProperty("name", new ObjectProperty<String>(s.getCode() + " - " + s.getName()));
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

