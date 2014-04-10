/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.filter.Filters;
import com.vaadin.addon.jpacontainer.provider.CachingLocalEntityProvider;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.*;
import org.tepi.filtertable.FilterGenerator;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.News;

public class CrudNews extends VerticalLayout {
  FilterTable tableNews;
  Form systemForm;
  TextField id = (TextField) createField("Идентификатор", true);
  TextField title = (TextField) createField("Заголовок", false);
  RichTextArea text = (RichTextArea) createField("Содержимое", false);
  TextField dateCreated = (TextField) createField("Дата создания", true);

  Button create = new Button("Создать", new Button.ClickListener() {
    @Override
    public void buttonClick(Button.ClickEvent event) {
      try {
        if (minLength(null)) {
          return;
        }
        systemForm.commit();
        tableNews.setValue(AdminServiceProvider.get().createNews(title.getValue().toString(), text.getValue().toString()).getId());
      } catch (Exception e) {
        //
      }
    }
  });

  Button update = new Button("Изменить", new Button.ClickListener() {
    @Override
    public void buttonClick(Button.ClickEvent event) {
      String id1 = systemForm.getField("id").getValue().toString();
      if (minLength(id1)) {
        return;
      }
      AdminServiceProvider.get().updateNews(Long.parseLong(id1),
          systemForm.getField("title").getValue(),
          systemForm.getField("text").getValue()
      );
      tableNews.setValue(null);
    }
  });

  Button remove = new Button("Удалить", new Button.ClickListener() {
    @Override
    public void buttonClick(Button.ClickEvent event) {
      long itemId = 0;
      try {
        itemId = Long.parseLong(id.getValue().toString());
      } catch (Exception ex) {
        getWindow().showNotification("Не выбрана новость для удаления.", Window.Notification.TYPE_ERROR_MESSAGE);
      }
      if (itemId != 0) {
        AdminServiceProvider.get().deleteNews(itemId);
        tableNews.setValue(null);
      }
    }
  });

  Button reset = new Button("Сбросить", new Button.ClickListener() {
    @Override
    public void buttonClick(Button.ClickEvent event) {
      tableNews.setValue(null);
    }
  });

  public CrudNews() {
    addComponent(createForm());
    addComponent(createTable());

    final HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    buttons.addComponent(create);
    update.setVisible(false);
    buttons.addComponent(update);
    remove.setVisible(false);
    buttons.addComponent(remove);
    buttons.addComponent(reset);
    reset.setVisible(false);
    systemForm.getFooter().addComponent(buttons);

    setExpandRatio(getComponent(0), 0.3f);
    setExpandRatio(getComponent(1), 0.7f);
    setSpacing(true);
    setMargin(true);
  }

  private Panel createForm() {
    systemForm = new Form();
    title.setRequired(true);
    text.setRequired(true);
    systemForm.setValidationVisibleOnCommit(false);
    systemForm.addField("id", id);
    systemForm.addField("title", title);
    systemForm.addField("text", text);
    systemForm.addField("dateCreated", dateCreated);
    systemForm.setWriteThrough(false);
    systemForm.setInvalidCommitted(false);

    Panel upperPanel = new Panel();
    upperPanel.setSizeFull();
    upperPanel.addComponent(systemForm);
    return upperPanel;
  }

  private Panel createTable() {
    tableNews = new FilterTable("Список новостей");
    tableNews.setFilterBarVisible(true);
    tableNews.setSizeFull();
    tableNews.setImmediate(true);
    tableNews.setSelectable(true);
    tableNews.setPageLength(5);
    tableNews.setFilterDecorator(new TableEmployeeFilterDecorator());
    final JPAContainer<News> container = new JPAContainer<News>(News.class);
    container.setEntityProvider(new CachingLocalEntityProvider<News>(News.class, AdminServiceProvider.get().getMyPU().createEntityManager()));
    tableNews.setContainerDataSource(container);
    tableNews.setVisibleColumns(new Object[]{"id", "title", "text", "dateCreated"});
    tableNews.setColumnHeaders(new String[]{"id", "Заголовок", "Содержимое", "Дата создания"});
    tableNews.setColumnExpandRatio("id", 4);
    tableNews.setColumnExpandRatio("title", 20);
    tableNews.setColumnExpandRatio("text", 60);
    tableNews.setColumnExpandRatio("dateCreated", 14);
    tableNews.setFilterGenerator(new FilterGenerator() {
      @Override
      public Container.Filter generateFilter(Object propertyId, Object value) {
        if ("id".equals(propertyId)) {
          try {
            return Filters.eq(propertyId, Long.valueOf(value.toString()));
          } catch (NumberFormatException e) {
            return Filters.isNull(propertyId);
          }
        }
        return null;
      }

      @Override
      public AbstractField getCustomFilterComponent(Object propertyId) {
        return null;
      }

      @Override
      public void filterRemoved(Object propertyId) {

      }

      @Override
      public void filterAdded(Object propertyId, Class<? extends Container.Filter> filterType, Object value) {

      }
    });
    tableNews.addListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        systemForm.setValidationVisible(false);
        ((JPAContainer) tableNews.getContainerDataSource()).refresh();
        if (event.getProperty().getValue() != null) {
          create.setVisible(false);
          update.setVisible(true);
          remove.setVisible(true);
          reset.setVisible(true);
        } else {
          systemForm.getField("id").setReadOnly(false);
          systemForm.getField("id").setValue("");
          systemForm.getField("id").setReadOnly(true);
          systemForm.getField("title").setValue("");
          systemForm.getField("text").setValue("");
          systemForm.getField("dateCreated").setReadOnly(false);
          systemForm.getField("dateCreated").setValue("");
          systemForm.getField("dateCreated").setReadOnly(true);
          create.setVisible(true);
          update.setVisible(false);
          remove.setVisible(false);
          reset.setVisible(false);
        }
      }
    });

    tableNews.addListener(new ItemClickEvent.ItemClickListener() {
      @Override
      public void itemClick(ItemClickEvent event) {
        Item item = event.getItem();
        id.setReadOnly(false);
        id.setValue(item.getItemProperty("id").getValue());
        id.setReadOnly(true);
        title.setValue(item.getItemProperty("title").getValue());
        text.setValue(item.getItemProperty("text").getValue());
        dateCreated.setReadOnly(false);
        dateCreated.setValue(item.getItemProperty("dateCreated").getValue());
        dateCreated.setReadOnly(true);
      }
    });
    Panel lowerPanel = new Panel();
    lowerPanel.setSizeFull();
    lowerPanel.addComponent(tableNews);
    return lowerPanel;
  }

  private AbstractField createField(String caption, Boolean readOnly) {
    AbstractField textField;
    if (caption.equals("Содержимое")) {
      textField = new RichTextArea(caption);
    } else {
      textField = new TextField(caption);
      ((TextField) textField).setMaxLength(255);
    }
    textField.setImmediate(true);
    textField.setWidth("40%");
    textField.setReadOnly(readOnly);
    return textField;
  }

  private Boolean minLength(String id1) {
    if (id1 != null && id1.length() == 0) {
      return true;
    } else if (title.getValue().toString().length() < 3) {
      systemForm.getWindow().showNotification("Поле \"Заголовок\" содержит меньше 3 символов", Window.Notification.TYPE_ERROR_MESSAGE);
      return true;
    }

    String str = text.getValue().toString();
    if (str.equals("&lt;br&gt;")) {
      systemForm.getWindow().showNotification("Поле \"Содержимое\" содержит меньше 5 символов", Window.Notification.TYPE_ERROR_MESSAGE);
      return true;
    }
    str = str.replaceAll("<(.)+?>", "");
    str = str.replaceAll("<(\n)+?>", "");

    if (str.length() < 5) {
      systemForm.getWindow().showNotification("Поле \"Содержимое\" содержит меньше 5 символов", Window.Notification.TYPE_ERROR_MESSAGE);
      return true;
    }
    return false;
  }
}
