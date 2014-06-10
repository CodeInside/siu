/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.*;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.adm.ui.LazyLoadingContainer2;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.ftarchive.helpers.FieldHelper;
import ru.codeinside.gses.beans.DirectoryBeanProvider;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryContainer;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryDefinition;
import ru.codeinside.gses.lazyquerycontainer.Query;
import ru.codeinside.gses.lazyquerycontainer.QueryDefinition;
import ru.codeinside.gses.lazyquerycontainer.QueryFactory;
import ru.codeinside.gses.vaadin.FieldConstructor;
import ru.codeinside.gses.vaadin.FieldFormType;
import ru.codeinside.gses.webui.manager.DictionaryQuery;
import ru.codeinside.gses.webui.utils.Components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

//TODO: а как же спиоск вариантов?!
public class DirectoryFFT implements FieldFormType, FieldConstructor, Serializable {

  private static final long serialVersionUID = -6022623500161277978L;


  @Override
  public String getFromType() {
    return "directory";
  }

  private Map<String, String> values;


  public DirectoryFFT() {
    values = new HashMap<String, String>();
  }

  public DirectoryFFT(Map<String, String> values) {
    this.values = values;
  }

  @Override
  public Field createField(String name, final String value, Layout layout, boolean writable, boolean required) {
    if (values.get("directory_id") == null) {
      return new ReadOnly("Неверно указан directory_id");
    }
    final String id = values.get("directory_id").trim();
    String trimValue = value == null ? null : value.trim();
    if (writable) {
      //TODO множественный выбор будет делаться в отдельной задаче
      //String multiselect = values.get("directory_multiselect");
      final boolean isMultiselect = false;//StringUtils.isNotEmpty(multiselect) && Boolean.parseBoolean(multiselect);
      final Select comboBox = isMultiselect ? new Select(name) {
        @Override
        public Object getValue() {
          Set<Object> set = new HashSet<Object>();
          for (Object o : ((Collection) super.getValue())) {
            set.add(getItem(o).getItemProperty("key").toString());
          }
          return set;
        }
      } : new ComboBox(name) {
        @Override
        public Object getValue() {
          Object value1 = super.getValue();
          if (value1 == null) {
            return null;
          }
          return getItem(value1).getItemProperty("key").toString();
        }
          /*return super.getValue();
        }*/
      };
      comboBox.setWidth("400px");

      DirectoryLazyQuery directoryLazyQuery = new DirectoryLazyQuery(id);
      final LazyQueryContainer container = new LazyQueryContainer(directoryLazyQuery, directoryLazyQuery);
      //По возможности перейти на этот контейнер
/*      final LazyLoadingContainer2 container = new LazyLoadingContainer2(new DictionaryQuery(id) {

        public Item createItem(String key, String value) {
          PropertysetItem item = new PropertysetItem();
          item.addItemProperty("key", Components.stringProperty(key));
          item.addItemProperty("value", Components.stringProperty(value));
          return item;
        }
      });*/
      comboBox.setContainerDataSource(container);
      comboBox.setItemCaptionPropertyId("value");
      comboBox.setEnabled(writable);
      //не работает при переопределении getValue();
//      comboBox.setValue(findInfoSystem(comboBox, id, trimValue));
      comboBox.setRequired(required);
      comboBox.setImmediate(true);
      comboBox.setInvalidAllowed(true);
      comboBox.setInvalidCommitted(false);
      return comboBox;
    }
    String kName = DirectoryBeanProvider.get().getValue(id, trimValue);
    if (StringUtils.isEmpty(kName)) {
      kName = trimValue;
    }
    ReadOnly readOnly = new ReadOnly(kName);
    FieldHelper.setCommonFieldProperty(readOnly, writable, name, required);
    return readOnly;
  }

  @Override
  public String getFieldValue(String formPropertyId, Form form) {
    Field field = form.getField(formPropertyId);
    Object value = field.getValue();
    if (value != null) {
      return value.toString();
    }
    return null;
  }

  @Override
  public String convertModelValueToFormValue(Object modelValue) {
    return modelValue != null ? modelValue.toString() : null;
  }

  @Override
  public Object convertFormValueToModelValue(String propertyValue) {
    return propertyValue;
  }

  @Override
  public boolean usePattern() {
    return false;
  }

  @Override
  public boolean useMap() {
    return true;
  }


  @Override
  public FieldConstructor createConstructorOfField() {
    return new DirectoryFFT(values);
  }

  @Override
  public void setMap(Map<String, String> values) {
    this.values = values;
  }

  @Override
  public void setPattern(String patternText) {
    throw new UnsupportedOperationException();
  }

  Object findInfoSystem(Container.Viewer viewer, String id, String code) {
    if (code != null) {
      Container container = viewer.getContainerDataSource();
      if (DirectoryBeanProvider.get().getValue(id, code) != null) {
        for (Object itemId : container.getItemIds()) {
          if (code.equals(container.getContainerProperty(itemId, "key").getValue())) {
            return itemId;
          }
        }
      }
      if (DirectoryBeanProvider.get().getKey(id, code) != null) {
        for (Object itemId : container.getItemIds()) {
          if (code.equals(container.getContainerProperty(itemId, "value").getValue())) {
            return itemId;
          }
        }
      }
    }
    return null;
  }

  class DirectoryLazyQuery extends LazyQueryDefinition implements QueryFactory, Serializable {

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

  class QueryImpl implements Query, Serializable {

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
