/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.ComboBox;
import ru.codeinside.gses.beans.DirectoryBeanProvider;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryContainer;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryDefinition;
import ru.codeinside.gses.lazyquerycontainer.Query;
import ru.codeinside.gses.lazyquerycontainer.QueryDefinition;
import ru.codeinside.gses.lazyquerycontainer.QueryFactory;
import ru.codeinside.gses.vaadin.customfield.CustomField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


final public class DirectoryField extends CustomField {

  private final ComboBox comboBox;
  private final String directoryId;
  int insideListener = 0;

  public DirectoryField(String directoryId, String name) {
    this.directoryId = directoryId;
    comboBox = new ComboBox(name);
    comboBox.setWidth("400px");
    DirectoryLazyQuery directoryLazyQuery = new DirectoryLazyQuery(directoryId);
    final LazyQueryContainer container = new LazyQueryContainer(directoryLazyQuery, directoryLazyQuery);
    container.sort(new String[]{"values"}, new boolean[]{true});
    comboBox.setContainerDataSource(container);
    comboBox.setItemCaptionPropertyId("value");
    comboBox.setEnabled(true);
    comboBox.setRequired(false);
    comboBox.setImmediate(true);
    comboBox.setInvalidAllowed(true);
    comboBox.setInvalidCommitted(false);
    setImmediate(true);

    // связать изменение в связном элементе с контейнером
    comboBox.addListener(new ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        insideListener++;
        try {
          Object newValue = event.getProperty().getValue();
          if (newValue == null) {
            setValue(null);
          } else {
            setValue(getKeyByIndex(newValue));
          }
        } finally {
          insideListener--;
        }
      }
    });

    setCompositionRoot(comboBox);
  }

  @Override
  public Class<?> getType() {
    return Void.class;
  }

  @Override
  public Object getValue() {
    Object index = comboBox.getValue();
    if (index == null) {
      return null;
    }
    return getKeyByIndex(index);
  }

  public String getDirectoryId() {
    return directoryId;
  }


  @Override
  public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
    if (insideListener == 0) {
      if (newValue == null || !setIndexByKey(newValue)) {
        comboBox.setValue(null);
        super.setValue(null);
      } else {
        super.setValue(newValue);
      }
    } else {
      super.setValue(newValue);
    }
  }

  Object getKeyByIndex(Object index) {
    return comboBox.getItem(index).getItemProperty("key").getValue();
  }

  boolean setIndexByKey(Object newValue) {
    LazyQueryContainer container = (LazyQueryContainer) comboBox.getContainerDataSource();
    int size = container.size();
    for (int i = 0; i < size; i++) {
      if ((i % 100) == 0) {
        container.refresh(); // prevent OOM
      }
      Item item = container.getItem(i);
      if (item == null) {
        return false;
      }
      if (newValue.equals(item.getItemProperty("key").getValue())) {
        comboBox.setValue(i);
        return true;
      }
    }
    return false;
  }

  @Override
  public void setRequired(boolean required) {
    super.setRequired(required);
    comboBox.setRequired(required);
  }

  @Override
  public void setReadOnly(boolean readOnly) {
    super.setReadOnly(readOnly);
    comboBox.setReadOnly(readOnly);
  }

  @Override
  public String getRequiredError() {
    return comboBox.getRequiredError();
  }

  @Override
  public void setRequiredError(String requiredMessage) {
    super.setRequiredError(requiredMessage);
    comboBox.setRequiredError(requiredMessage);
  }

  final static class DirectoryLazyQuery extends LazyQueryDefinition implements QueryFactory, Serializable {

    private final String dir;

    /**
     * Constructor which sets the batch size.
     */
    public DirectoryLazyQuery(String dir) {
      super(false, 10);
      this.dir = dir;
      addProperty("key", String.class, null, true, true);
      addProperty("value", String.class, null, true, true);
    }

    @Override
    public void setQueryDefinition(QueryDefinition queryDefinition) {
    }

    @Override
    public Query constructQuery(Object[] sortPropertyIds, boolean[] asc) {
      return new QueryImpl(dir, convertTypes(sortPropertyIds), asc);
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

    final String dir;
    final String[] ids;
    final boolean[] asc;

    public QueryImpl(String dir, String[] ids, boolean[] asc) {
      this.dir = dir;
      this.ids = ids;
      this.asc = asc;
    }

    @Override
    public int size() {
      return DirectoryBeanProvider.get().getCountValues(dir);
    }

    @Override
    public List<Item> loadItems(final int start, final int count) {
      final List<Object[]> values = DirectoryBeanProvider.get().getValues(dir, start, count, ids, asc);
      final List<Item> items = new ArrayList<Item>(values.size());
      for (final Object[] s : values) {
        final PropertysetItem item = new PropertysetItem();
        item.addItemProperty("key", new ObjectProperty<String>((String) s[0]));
        item.addItemProperty("value", new ObjectProperty<String>((String) s[1]));
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
