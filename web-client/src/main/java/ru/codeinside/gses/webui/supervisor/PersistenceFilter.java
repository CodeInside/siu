package ru.codeinside.gses.webui.supervisor;

import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryContainer;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.webui.Flash;

import java.util.Date;

final public class PersistenceFilter extends HorizontalLayout {
  final Label bidHint;
  Table table;
  FilterablePersistence persistence;
  String lastProcessInstanceId;
  String lastProcedureName;

  PersistenceFilter(Table table, FilterablePersistence persistence) {
    this.persistence = persistence;
    this.table = table;
    setSpacing(true);
    final TextField bidField = new TextField();
    bidField.setInputPrompt("Фильтр по заявке");
    bidField.setImmediate(true);
    bidField.addListener(new BidChangeListener());

    final PopupDateField dataFromField = getDateField("Дата подачи с");
    dataFromField.addListener(new DataFromChangeListener());

    final PopupDateField dataToField = getDateField("Дата подачи по");
    dataToField.addListener(new DataToChangeListener());

    final TextField procField = new TextField();
    procField.setInputPrompt("Фильтр по процедуре");
    procField.setImmediate(true);
    procField.addListener(new ProcedureChangeListener());

    bidHint = new Label();
    bidHint.setStyleName("small");

    addComponent(bidField);
    addComponent(dataFromField);
    addComponent(dataToField);
    addComponent(procField);
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
        persistence.addFilter(FilterableValue.PROCESS_INSTANCE_ID, processInstanceId);
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

  class DataFromChangeListener implements Property.ValueChangeListener {
    @Override
    public void valueChange(final Property.ValueChangeEvent event) {
      final Date dateFrom = (Date) event.getProperty().getValue();
      final Object selectionId = table.getValue();
      persistence.addFilter(FilterableValue.DATE_FROM, dateFrom);
      ((LazyQueryContainer) table.getContainerDataSource()).refresh();
      if (selectionId != null) {
        table.setValue(null);
        if (table.size() > 0) {
          table.setValue(0); // оставим выделенным первый элемент
        }
      }
    }
  }

  class DataToChangeListener implements Property.ValueChangeListener {
    @Override
    public void valueChange(final Property.ValueChangeEvent event) {
      final Date dateTo = (Date) event.getProperty().getValue();
      final Object selectionId = table.getValue();
      persistence.addFilter(FilterableValue.DATE_TO, dateTo);
      ((LazyQueryContainer) table.getContainerDataSource()).refresh();
      if (selectionId != null) {
        table.setValue(null);
        if (table.size() > 0) {
          table.setValue(0); // оставим выделенным первый элемент
        }
      }
    }
  }

  class ProcedureChangeListener implements FieldEvents.TextChangeListener {
    @Override
    public void textChange(final FieldEvents.TextChangeEvent event) {
      final String procedureName = Fn.trimToNull(event.getText());
      if (!Fn.isEqual(lastProcedureName, procedureName)) {
        final Object selectionId = table.getValue();
        persistence.addFilter(FilterableValue.PROCEDURE_NAME, procedureName);
        ((LazyQueryContainer) table.getContainerDataSource()).refresh();
        if (selectionId != null) {
          table.setValue(null);
          if (table.size() > 0) {
            table.setValue(0); // оставим выделенным первый элемент
          }
        }
        lastProcedureName = procedureName;
      }
    }
  }

  private static PopupDateField getDateField(String caption) {
    final PopupDateField dateField = new PopupDateField();
    dateField.setLocale(TaskFilter.RUSSIAN_LOCALE);
    dateField.setInputPrompt(caption);
    dateField.setImmediate(true);
    dateField.setShowISOWeekNumbers(false);
    dateField.setImmediate(true);
    dateField.setDateFormat("dd.MM.yyyy HH:mm:ss");
    dateField.setParseErrorMessage("Введите правильную дату и время");
    return dateField;
  }
}