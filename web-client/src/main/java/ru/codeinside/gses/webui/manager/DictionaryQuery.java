package ru.codeinside.gses.webui.manager;

import com.vaadin.data.Item;
import ru.codeinside.gses.beans.DirectoryBeanProvider;
import ru.codeinside.gses.webui.containers.LazyLoadingContainer;
import ru.codeinside.gses.webui.containers.LazyLoadingQuery;

import java.util.ArrayList;
import java.util.List;

public abstract class DictionaryQuery implements LazyLoadingQuery {

  private final String dirName;
  private String[] sortProps = {};
  private boolean[] sortAsc = {};
  public LazyLoadingContainer container;

  public DictionaryQuery(String dirName) {
    this.dirName = dirName;
  }

  @Override
  public int size() {
    return DirectoryBeanProvider.get().getCountValues(dirName);
  }

  @Override
  public List<Item> loadItems(int start, int count) {
    List<Item> items = new ArrayList<Item>();
    List<Object[]> values = DirectoryBeanProvider.get().getValues(dirName, start, count, sortProps, sortAsc);
    for (Object[] o : values) {
      items.add(createItem((String) o[0], (String) o[1]));
    }
    return items;
  }

  public abstract Item createItem(String key, String value);/* {
    PropertysetItem item = new PropertysetItem();
    item.addItemProperty("key", Components.stringProperty(key));
    item.addItemProperty("value", Components.stringProperty(value));
    item.addItemProperty("form", Components.buttonProperty("Удалить", createDeleteEntryListener(dirMapTable, dirName, key)));
    return item;
  }*/

  @Override
  public Item loadSingleResult(String paramString) {
    String value = DirectoryBeanProvider.get().getValue(dirName, paramString);
    return createItem(paramString, value);
  }

  @Override
  public void setSorting(Object[] propertyIds, boolean[] ascending) {
    String[] props = new String[propertyIds.length];
    for (int i = 0; i < propertyIds.length; i++) {
      props[i] = propertyIds[i].toString();
    }
    sortProps = props;
    sortAsc = ascending;
  }

  @Override
  public void setLazyLoadingContainer(LazyLoadingContainer container) {
    this.container = container;
  }
}

