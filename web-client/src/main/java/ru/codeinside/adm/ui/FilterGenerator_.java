package ru.codeinside.adm.ui;


import com.vaadin.addon.jpacontainer.filter.Filters;
import com.vaadin.data.Container;
import com.vaadin.ui.AbstractField;

import java.util.Arrays;
import java.util.List;

public class FilterGenerator_ implements org.tepi.filtertable.FilterGenerator {

  private List<String> numbers;
  private List<String> caseSensitives;

  public FilterGenerator_() {
    this.numbers = Arrays.asList("id");
  }

  public FilterGenerator_(List<String> numbers, List<String> caseSensitives) {
    this.numbers = numbers;
    this.caseSensitives = caseSensitives;
  }

  @Override
  public Container.Filter generateFilter(Object propertyId, Object value) {
    if (numbers != null) {
      for (String propId : numbers) {
        if (propId.equals(propertyId)) {
          try {
            return Filters.eq(propertyId, Long.valueOf(value.toString()));
          } catch (NumberFormatException e) {
            return Filters.isNull(propertyId);
          }
        }
      }
    }
    if (caseSensitives != null) {
      for (String propId : caseSensitives) {
        if (propId.equals(propertyId)) {
          return Filters.eq(propertyId, value);
        }
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

}
