package ru.codeinside.adm.ui;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.InfoSystem;
import ru.codeinside.gses.API;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryContainer;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryDefinition;
import ru.codeinside.gses.lazyquerycontainer.Query;
import ru.codeinside.gses.lazyquerycontainer.QueryDefinition;
import ru.codeinside.gses.lazyquerycontainer.QueryFactory;
import ru.codeinside.gses.webui.Flash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

final public class GwsSystemTab extends CustomComponent implements TabSheet.SelectedTabChangeListener {

  Form form;
  TextField code;
  TextField name;
  TextField comment;
  Button commit;
  Button clean;
  Button delete;
  Table table;
  LazyQueryContainer container;

  GwsSystemTab() {
    setSizeFull();
    setCompositionRoot(new Label("Загрузка..."));
  }


  private Component createInfoSystemEditor() {
    code = new TextField("Код", "");
    code.setMaxLength(10);
    code.setNullRepresentation("");
    code.setInputPrompt("мнемоника СМЭВ");
    code.setRequiredError("Пропущен код!");
    code.setImmediate(true);

    name = new TextField("Название", "");
    name.setNullRepresentation("");
    name.setInputPrompt("согласнованое с кодом");
    name.setMaxLength(255);
    name.setRequiredError("Пропущено название!");
    name.setWidth(99, UNITS_PERCENTAGE);
    name.setImmediate(true);

    comment = new TextField("Комментарий", "");
    comment.setNullRepresentation("");
    comment.setMaxLength(255);
    comment.setWidth(99, UNITS_PERCENTAGE);
    comment.setImmediate(true);

    QDF qdf = new QDF();
    container = new LazyQueryContainer(qdf, qdf);

    table = new Table(null, container);
    table.addGeneratedColumn("source", new Table.ColumnGenerator() {
      @Override
      public Object generateCell(Table table, Object itemId, Object columnId) {
        final String code = (String) container.getContainerProperty(itemId, "code").getValue();
        boolean isSource = (Boolean) container.getContainerProperty(itemId, "source").getValue();
        CheckBox source = new CheckBox();
        source.setValue(isSource);
        source.setImmediate(true);
        source.setDescription(isSource ? "Сбросить признак источника" : "Назначить источником для потребителей СМЭВ");
        source.addListener(new Property.ValueChangeListener() {
          @Override
          public void valueChange(Property.ValueChangeEvent event) {
            boolean newValue = (Boolean) event.getProperty().getValue();
            AdminServiceProvider.get().toggleSource(code, newValue);
            container.refresh();
          }
        });
        return source;
      }
    });
    table.addGeneratedColumn("main", new Table.ColumnGenerator() {
      @Override
      public Object generateCell(Table source, Object itemId, Object columnId) {
        boolean isSource = (Boolean) container.getContainerProperty(itemId, "source").getValue();
        if (!isSource) {
          return null;
        }
        final String code = (String) container.getContainerProperty(itemId, "code").getValue();
        boolean isMain = (Boolean) container.getContainerProperty(itemId, "main").getValue();
        CheckBox box = new CheckBox();
        box.setValue(isMain);
        if (isMain) {
          box.setReadOnly(true);
          box.setDescription("Основная ИС для потребителей СМЭВ");
        } else {
          box.setDescription("Назначить основной ИС для потребителей СМЭВ");
          box.setImmediate(true);
          box.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
              boolean newValue = (Boolean) event.getProperty().getValue();
              AdminServiceProvider.get().toggleMain(code, newValue);
              container.refresh();
            }
          });
        }
        return box;
      }
    });

    table.setSizeFull();
    table.setPageLength(0);
    table.setVisibleColumns(new String[]{"code", "name", "comment", "source", "main"});
    table.setColumnHeaders(new String[]{"Код", "Название", "Комментарий", "Источник", "Основная"});
    table.setSelectable(true);
    table.setImmediate(true);
    table.setSortContainerPropertyId("code");

    form = new Form();
    code.setRequired(true);
    name.setRequired(true);
    form.addField("code", code);
    form.addField("name", name);
    form.addField("comment", comment);
    form.setWriteThrough(false);

    commit = new Button("Сохранить");
    comment.setImmediate(true);
    clean = new Button("Очистить");
    clean.setImmediate(true);
    delete = new Button("Удалить");
    delete.setImmediate(true);
    delete.setEnabled(false);

    clean.addListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        cleanForm();
      }
    });

    delete.addListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        String codeValue = (String) code.getValue();
        if (AdminServiceProvider.get().deleteInfoSystem(codeValue)) {
          cleanForm();
        } else {
          getWindow().showNotification("С " + codeValue + " связанны потребители СМЭВ!", Window.Notification.TYPE_HUMANIZED_MESSAGE);
        }
      }
    });

    commit.addListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        try {
          form.setValidationVisible(true);
          form.commit();
          delete.setEnabled(true);
          String codeValue = (String) code.getValue();
          AdminServiceProvider.get().createInfoSystem(codeValue, (String) name.getValue(), (String) comment.getValue());
          AdminServiceProvider.get().createLog(Flash.getActor(), "InfoSystem", (String) code.getValue(),
            "create/update",
            "value => " + name.getValue(), true);
          container.refresh();
          for (Object itemId : container.getItemIds()) {
            if (codeValue.equals(container.getContainerProperty(itemId, "code").getValue())) {
              table.setValue(itemId);
              break;
            }
          }
        } catch (Validator.InvalidValueException ignore) {
        }
      }
    });

    table.addListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        Table table = (Table) event.getProperty();
        Object itemId = event.getProperty().getValue();
        Item item = itemId == null ? null : table.getItem(itemId);
        if (item != null) {
          code.setReadOnly(false);
          code.setValue(item.getItemProperty("code").getValue());
          name.setValue(item.getItemProperty("name").getValue());
          comment.setValue(item.getItemProperty("comment").getValue());
          code.setReadOnly(true);
          delete.setEnabled(true);
        } else if (code.isReadOnly()) {
          code.setReadOnly(false);
          code.setValue("");
          delete.setEnabled(false);
        }
      }
    });

    HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    buttons.addComponent(clean);
    buttons.addComponent(commit);
    buttons.addComponent(delete);
    form.setFooter(buttons);

    Panel formPanel = new Panel();
    formPanel.addComponent(form);

    VerticalLayout layout = new VerticalLayout();
    layout.setMargin(true);
    layout.setSpacing(true);
    layout.setSizeFull();
    layout.addComponent(createProductionMode());
    layout.addComponent(formPanel);
    layout.addComponent(table);
    layout.setExpandRatio(table, 0.9f);
    return layout;
  }

  private Component createProductionMode() {
    CheckBox productionMode = new CheckBox(
      "Производственный режим СМЭВ", AdminServiceProvider.getBoolProperty(API.PRODUCTION_MODE)
    );
    productionMode.setImmediate(true);
    productionMode.addListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        boolean value = Boolean.TRUE.equals(event.getProperty().getValue());
        AdminServiceProvider.get().saveSystemProperty(API.PRODUCTION_MODE, Boolean.toString(value));
      }
    });
    Panel panel = new Panel();
    panel.addComponent(productionMode);
    return panel;
  }

  void cleanForm() {
    code.setReadOnly(false);
    code.setValue(null);
    name.setValue(null);
    comment.setValue(null);
    delete.setEnabled(false);
    form.setValidationVisible(false);
    table.setValue(null);
    container.refresh();
  }

  @Override
  public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
    if (this != event.getTabSheet().getSelectedTab()) {
      return;
    }
    if (form == null) {
      setCompositionRoot(createInfoSystemEditor());
    }
  }

  final static class QDF extends LazyQueryDefinition implements QueryFactory, Serializable {

    private static final long serialVersionUID = 1L;

    QDF() {
      super(false, 10);
      addProperty("code", String.class, null, true, true);
      addProperty("name", String.class, null, true, true);
      addProperty("comment", String.class, null, true, true);
      addProperty("source", Boolean.class, null, true, true);
      addProperty("main", Boolean.class, null, true, true);
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
      return AdminServiceProvider.get().countInfoSystems(false);
    }

    @Override
    public List<Item> loadItems(final int start, final int count) {
      final List<InfoSystem> systems = AdminServiceProvider.get().queryInfoSystems(false, ids, asc, start, count);
      final List<Item> items = new ArrayList<Item>(systems.size());
      for (final InfoSystem s : systems) {
        final PropertysetItem item = new PropertysetItem();
        item.addItemProperty("code", new ObjectProperty<String>(s.getCode(), String.class));
        item.addItemProperty("name", new ObjectProperty<String>(s.getName(), String.class));
        item.addItemProperty("comment", new ObjectProperty<String>(s.getComment(), String.class));
        item.addItemProperty("source", new ObjectProperty<Boolean>(s.isSource(), Boolean.class));
        item.addItemProperty("main", new ObjectProperty<Boolean>(s.isSource() && s.isMain(), Boolean.class));
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
