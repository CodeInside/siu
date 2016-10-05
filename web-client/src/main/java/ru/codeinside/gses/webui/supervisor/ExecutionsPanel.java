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
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryContainer;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.webui.executor.ExecutorFactory;
import ru.codeinside.jpa.ActivitiEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.codeinside.gses.webui.utils.Components.stringProperty;

final public class ExecutionsPanel extends VerticalLayout {

  final static ObjectProperty<Boolean> TRUE_VALUE = new ObjectProperty<Boolean>(true);
  final Persistence persistence = new Persistence();
  final LazyQueryContainer container = persistence.createContainer();
  final Table table;
  final PersistenceFilter filter;
  final VerticalLayout diagramLayout;

  public ExecutionsPanel() {
    setSizeFull();
    setSpacing(true);
    setMargin(true);


    table = new Table(null, container);
    table.setSizeFull();
    table.setColumnHeaders(new String[]{
        "Заявка", "Дата подачи заявки", "Процедура", "Версия", "Маршрут", "Процесс", "Ветвь"
    });
    table.setColumnIcon("eid", new ThemeResource("icon/branch.png"));
    table.setSelectable(true);
    table.setMultiSelect(false);
    table.setNullSelectionAllowed(true);
    table.setValue(null);
    table.setImmediate(true);
    table.addListener(new ShowExecutionListener());

    filter = new PersistenceFilter(table, persistence);

    diagramLayout = new VerticalLayout();
    diagramLayout.setMargin(false);
    diagramLayout.setSizeFull();

    addComponent(filter);
    addComponent(table);
    addComponent(diagramLayout);
    setExpandRatio(table, 0.4f);
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

  final static class Persistence extends SimpleQuery implements FilterablePersistence {

    private Map<FilterableValue, Object> filters = new HashMap<FilterableValue, Object>();
    private EntityManager em = ActivitiEntityManager.INSTANCE;

    Persistence() {
      super(false, 10);
      addProperty("bid", Long.class, null, true, false);
      addProperty("startDate", String.class, null, true, false);
      addProperty("name", String.class, null, true, false);
      addProperty("ver", String.class, null, true, false);
      addProperty("did", String.class, null, true, false);
      addProperty("pid", String.class, null, true, false);
      addProperty("eid", String.class, null, true, false);
    }

    @Override
    public void addFilter(FilterableValue key, Object value) {
      if (value != null) {
        filters.put(key, value);
      } else if (filters != null && filters.containsKey(key)) {
        filters.remove(key);
      }
    }

    @Override
    public int size() {
      return getFilteredExecutionsCount();
    }

    @Override
    public List<Item> loadItems(final int startIndex, final int count) {
      return constructItems(startIndex, count);
    }

    private List<Object[]> getFilteredExecutions(int startIndex, int count) {
      Query query = createNativeQuery(false)
          .setFirstResult(startIndex)
          .setMaxResults(count);
      return query.getResultList();
    }

    private int getFilteredExecutionsCount() {
      Query query = createNativeQuery(true);
      try {
        return ((Long) query.getSingleResult()).intValue();
      } catch (NoResultException ignore) {
        return 0;
      }
    }

    private Query createNativeQuery(final boolean isCount) {
      String sql = "SELECT " + (isCount ? "COUNT(e.*) " :
          "e.proc_inst_id_, e.id_, e.act_id_, e.proc_def_id_, e.is_concurrent_, b.id, b.datecreated, b.tag, p.name, p.version ") +
          "FROM act_ru_execution e " +
          "LEFT JOIN bid b ON e.proc_inst_id_ = b.processinstanceid " +
          "LEFT JOIN procedure p ON p.id = b.procedure_id ";

      if (filters != null && filters.size() > 0) {
        String processInstanceId = (String) filters.get(FilterableValue.PROCESS_INSTANCE_ID);
        Date dateFrom = filters.get(FilterableValue.DATE_FROM) instanceof Date ? (Date) filters.get(FilterableValue.DATE_FROM) : null;
        Date dateTo = filters.get(FilterableValue.DATE_TO) instanceof Date ? (Date) filters.get(FilterableValue.DATE_TO) : null;
        String procedureName = (String) filters.get(FilterableValue.PROCEDURE_NAME);
        boolean isAdded = false;

        if (processInstanceId != null) {
          sql += "WHERE e.proc_inst_id_ = '" + processInstanceId + "'";
          isAdded = true;
        }
        if (dateFrom != null) {
          sql += (isAdded ? " AND" : "WHERE ") + " b.datecreated >= '" + dateFrom + "'";
          isAdded = true;
        }
        if (dateTo != null) {
          sql += (isAdded ? " AND" : "WHERE ") + " b.datecreated <= '" + dateTo + "'";
          isAdded = true;
        }
        if (procedureName != null) {
          procedureName = procedureName.toLowerCase();
          sql += (isAdded ? " AND" : "WHERE ") + " lower(p.name) LIKE '" + procedureName + "%'";
        }
      }
      return em.createNativeQuery(sql);
    }

    private List<Item> constructItems(final Integer startIndex, final Integer count) {
      final List<Object[]> resultList = getFilteredExecutions(startIndex, count);
      final List<Item> items = new ArrayList<Item>(resultList.size());
      for (final Object[] result : resultList) {
        final PropertysetItem item = new PropertysetItem();
        String processInstanceId = (String) result[0];
        String executionId = (String) result[1];
        String activityId = (String) result[2];
        String processDefinitionId = (String) result[3];
        boolean isConcurrent = (Boolean) result[4];
        Long bidId = (Long) result[5];
        Date dateCreated = (Date) result[6];
        String bidTag = (String) result[7];
        String procedureName = (String) result[8];
        String procedureVersion = (String) result[9];


        item.addItemProperty("pid", stringProperty(processInstanceId));
        if (!executionId.equals(processInstanceId)) {
          item.addItemProperty("eid", stringProperty(executionId));
        }
        item.addItemProperty("_eid", stringProperty(executionId));
        item.addItemProperty("act", stringProperty(activityId));
//        item.addItemProperty("actName", stringProperty(activityName));
        item.addItemProperty("did", stringProperty(processDefinitionId));
        if (isConcurrent) {
          item.addItemProperty("concurrent", TRUE_VALUE);
        }
        if (dateCreated != null) {
          item.addItemProperty("bid", new ObjectProperty<Long>(bidId));
          item.addItemProperty("startDate", stringProperty(ExecutorFactory.formatter.format(dateCreated)));
          if (procedureName != null) {
            if (bidTag == null || bidTag.isEmpty()) {
              item.addItemProperty("name", stringProperty(procedureName));
            } else {
              item.addItemProperty("name", stringProperty(bidTag + " - " + procedureName));
            }
            item.addItemProperty("ver", stringProperty(procedureVersion));
          }
        }
        items.add(item);
      }
      return items;
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
