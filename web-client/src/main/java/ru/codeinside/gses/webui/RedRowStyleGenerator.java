package ru.codeinside.gses.webui;

import com.vaadin.data.Item;
import com.vaadin.ui.Table;

public class RedRowStyleGenerator implements Table.CellStyleGenerator {

  Table table;
  public RedRowStyleGenerator(Table table) {
    this.table = table;
  }

  @Override
  public String getStyle(Object itemId, Object propertyId) {
    if (propertyId == null) {
      // Styling for row
      Item item = table.getItem(itemId);
      String priority = (String) item.getItemProperty("priority").getValue();
      if (Integer.parseInt(priority) >= 70) {
        return "highlight-red";
      } else if (Integer.parseInt(priority) >= 60) {
        return "highlight-rosy";
      } else {
        return null;
      }
    } else {
      // styling for column propertyId
      return null;
    }
  }
}