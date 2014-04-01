package ru.codeinside.adm.ui;


import com.vaadin.addon.jpacontainer.filter.Filters;
import com.vaadin.data.Container;
import com.vaadin.ui.AbstractField;
import org.tepi.filtertable.FilterGenerator;

public class IdFilterGenerator implements FilterGenerator {

  private String propertyId;

  public IdFilterGenerator(){
    this.propertyId = "id";
  }

  public IdFilterGenerator(String propertyId){
    this.propertyId = propertyId;
  }


  @Override
  public Container.Filter generateFilter(Object propertyId, Object value) {
    if (this.propertyId.equals(propertyId)) {
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

}
