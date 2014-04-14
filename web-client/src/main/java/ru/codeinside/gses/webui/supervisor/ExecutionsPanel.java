/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.supervisor;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.event.FieldEvents;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import ru.codeinside.adm.AdminService;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.Procedure;
import ru.codeinside.adm.ui.LazyLoadingContainer2;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryContainer;
import ru.codeinside.gses.service.F1;
import ru.codeinside.gses.service.F3;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.containers.LazyLoadingContainer;
import ru.codeinside.gses.webui.containers.LazyLoadingQuery;
import ru.codeinside.gses.webui.executor.ExecutorFactory;

import java.util.ArrayList;
import java.util.List;

import static ru.codeinside.gses.webui.Flash.flash;
import static ru.codeinside.gses.webui.utils.Components.stringProperty;

final public class ExecutionsPanel extends VerticalLayout {

  final static ObjectProperty<Boolean> TRUE_VALUE = new ObjectProperty<Boolean>(true);
  final Persistence persistence = new Persistence();
  final LazyLoadingContainer2 container = new LazyLoadingContainer2(persistence);
  final Filter filter = new Filter();
  final Table table;
  final VerticalLayout diagramLayout;

  public ExecutionsPanel() {
    setSizeFull();
    setSpacing(true);
    setMargin(true);

    addComponent(filter);

    table = new Table(null, container);
    table.setSizeFull();
   /* table.setColumnHeaders(new String[]{
      "Заявка", "Дата подачи заявки", "Процедура", "Версия", "Маршрут", "Процесс", "Ветвь"
    });*/
    table.setColumnIcon("eid", new ThemeResource("icon/branch.png"));
    table.setSelectable(true);
    table.setMultiSelect(false);
    table.setNullSelectionAllowed(true);
    table.setValue(null);
    table.setImmediate(true);
    table.addListener(new ShowExecutionListener());
    addComponent(table);
    setExpandRatio(table, 0.4f);

    diagramLayout = new VerticalLayout();
    diagramLayout.setMargin(false);
    diagramLayout.setSizeFull();
    addComponent(diagramLayout);
    setExpandRatio(diagramLayout, 0.6f);

    showDiagram(null);
  }

  void showDiagram(Component component) {
    diagramLayout.removeAllComponents();
    if (component == null) {
      component = new Label("Выберите процесс для отображения схемы исполнения");
    }
    diagramLayout.addComponent(component);
  }

  final static class Persistence implements LazyLoadingQuery {

    String processInstanceFilter;

    Persistence() {
      /*super(false, 10);
      addProperty("bid", Long.class, null, true, false);
      addProperty("startDate", String.class, null, true, false);
      addProperty("name", String.class, null, true, false);
      addProperty("ver", String.class, null, true, false);
      addProperty("did", String.class, null, true, false);
      addProperty("pid", String.class, null, true, false);
      addProperty("eid", String.class, null, true, false);*/
    }

    public void setProcessInstanceFilter(String processInstanceFilter) {
      this.processInstanceFilter = processInstanceFilter;
    }

    @Override
    public int size() {
      return Fn.withEngine(new Count(), processInstanceFilter).intValue();
    }

    @Override
    public List<Item> loadItems(final int startIndex, final int count) {
      return Fn.withEngine(new Items(), processInstanceFilter, startIndex, count);
    }

      @Override
      public Item loadSingleResult(String paramString) {
          return null;
      }

      @Override
      public void setSorting(Object[] paramArrayOfObject, boolean[] paramArrayOfBoolean) {

      }

      @Override
      public void setLazyLoadingContainer(LazyLoadingContainer container) {

      }

      static ExecutionQuery createQuery(final ProcessEngine engine, final String processInstanceFilter) {
      final ExecutionQuery query = engine.getRuntimeService().createExecutionQuery();
      if (processInstanceFilter != null) {
        query.processInstanceId(processInstanceFilter);
      }
      return query;
    }

    final static class Count implements F1<Long, String> {
      @Override
      public Long apply(ProcessEngine engine, String processInstanceFilter) {
        return createQuery(engine, processInstanceFilter).count();
      }
    }

    final static class Items implements F3<List<Item>, String, Integer, Integer> {
      @Override
      public List<Item> apply(final ProcessEngine engine, final String processInstanceFilter, final Integer startIndex, final Integer count) {
        final AdminService adminService = flash().getAdminService();
        final List<Execution> processInstances = createQuery(engine, processInstanceFilter).listPage(startIndex, count);
        final List<Item> items = new ArrayList<Item>(processInstances.size());
        for (final Execution execution : processInstances) {
          final PropertysetItem item = new PropertysetItem();
          final ExecutionEntity entity = (ExecutionEntity) execution;
          item.addItemProperty("pid", stringProperty(execution.getProcessInstanceId()));
          if (!execution.getId().equals(execution.getProcessInstanceId())) {
            item.addItemProperty("eid", stringProperty(execution.getId()));
          }
          item.addItemProperty("_eid", stringProperty(execution.getId()));
          item.addItemProperty("act", stringProperty(entity.getCurrentActivityId()));
          item.addItemProperty("actName", stringProperty(entity.getCurrentActivityName()));
          item.addItemProperty("did", stringProperty(entity.getProcessDefinitionId()));
          if (entity.isConcurrent()) {
            item.addItemProperty("concurrent", TRUE_VALUE);
          }
          final Bid bid = adminService.getBidByProcessInstanceId(execution.getProcessInstanceId());
          if (bid != null) {
            item.addItemProperty("bid", new ObjectProperty<Long>(bid.getId()));
            item.addItemProperty("startDate", stringProperty(ExecutorFactory.formatter.format(bid.getDateCreated())));
            final Procedure procedure = bid.getProcedure();
            if (procedure != null) {
              if (bid.getTag().isEmpty()) {
                item.addItemProperty("name", stringProperty(procedure.getName()));
              } else {
                item.addItemProperty("name", stringProperty(bid.getTag()+ " - " +procedure.getName()));
              }
              item.addItemProperty("ver", stringProperty(procedure.getVersion()));
            }
          }
          items.add(item);
        }
        return items;
      }
    }
  }

  final class Filter extends HorizontalLayout {
    final Label bidHint;

    String lastProcessInstanceId;

    Filter() {
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
//          container.refresh();
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


  final class ShowExecutionListener implements Property.ValueChangeListener {

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
      final Table table = (Table) event.getProperty();
      Component diagram = null;
      final Object itemId = table.getValue();
      if (itemId != null) {
        final Item item = table.getItem(itemId);
        final String _eid = Fn.getValue(item, "_eid", String.class);
        final String eid = Fn.getValue(item, "eid", String.class);
        final String did = Fn.getValue(item, "did", String.class);
        if (_eid != null && did != null) {
          diagram = new DiagramPanel(did, _eid);
          if (eid != null) { // блок показываем только для ветвей, для процесса их может быть много
            final String act = Fn.getValue(item, "act", String.class);
            final String actName = Fn.trimToNull(Fn.getValue(item, "actName", String.class));
            final boolean concurrent = Boolean.TRUE == Fn.getValue(item, "concurrent", Boolean.class);
            diagram.setCaption((concurrent ? "Параллельно" : "Последовательно") + " исполяемый блок " + act + (actName == null ? "" : (" '" + actName + "'")));
          }
        }
      }
      showDiagram(diagram);
    }
  }
}
