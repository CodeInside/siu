package ru.codeinside.gses.webui.supervisor;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryContainer;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.containers.LazyLoadingContainer;

final public class Filter extends HorizontalLayout {
  final Label bidHint;
  Table table;
  FilterablePersistence persistence;
  String lastProcessInstanceId;

  Filter(Table table, FilterablePersistence persistence) {
    this.persistence = persistence;
    this.table = table;
    setSpacing(true);
    bidHint = new Label();
    bidHint.setStyleName("small");
    final TextField bidField = new TextField();
    bidField.setInputPrompt("Фильтр по заявке");
    bidField.setImmediate(true);
    bidField.addListener(new BidChangeListener());
    addComponent(bidField);
    addComponent(bidHint);
  }

  class BidChangeListener implements FieldEvents.TextChangeListener {
    @Override
    public void textChange(final FieldEvents.TextChangeEvent event) {
      final String bidText = Fn.trimToNull(event.getText());
      String processInstanceId = null;
      String errorText = null;
      if (bidText != null) {
        try {
          Long.parseLong(bidText);
        } catch (NumberFormatException e) {
          errorText = "Введите номер";
        }
        if (errorText == null) {
          final Bid bid = Flash.flash().getAdminService().getBid(bidText);
          if (bid == null) {
            errorText = "Заявка " + bidText + " не существует";
          } else if (bid.getDateFinished() != null) {
            errorText = "Заявка " + bidText + " уже исполнена";
          } else {
            processInstanceId = bid.getProcessInstanceId();
          }
        }
      }
      bidHint.setValue(errorText != null ? errorText : null);
      if (!Fn.isEqual(lastProcessInstanceId, processInstanceId)) {
        final Object selectionId = table.getValue();
        persistence.setProcessInstanceFilter(processInstanceId);
        ((LazyQueryContainer) table.getContainerDataSource()).refresh();
        if (selectionId != null) {
          table.setValue(null);
          if (table.size() > 0) {
            table.setValue(0); // оставим выделенным первый элемент
          }
        }
        lastProcessInstanceId = processInstanceId;
      }
    }
  }
}