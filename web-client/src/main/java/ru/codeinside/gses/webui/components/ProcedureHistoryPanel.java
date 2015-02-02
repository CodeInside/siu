/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryContainer;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.supervisor.SupervisorWorkplace;
import ru.codeinside.gses.webui.utils.Components;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProcedureHistoryPanel extends VerticalLayout {
  private Table historyTable;

  public ProcedureHistoryPanel(Set<String> taskIds, SupervisorWorkplace workspace) {
    buildLayout(taskIds, workspace);
  }

  private void buildLayout(final Set<String> taskIds, SupervisorWorkplace workspace) {

    Panel bidPanel = new Panel("История исполнения заявки");
    addComponent(bidPanel);
    VerticalLayout bidLayout = new VerticalLayout();
    bidLayout.setSizeFull();
    bidLayout.setSpacing(true);
    bidPanel.addComponent(bidLayout);

    historyTable = Components.createTable(null, null);

    final Map<String, String> tasks = new LinkedHashMap<String, String>();

    for (String taskId : taskIds) {
      final Task task = Flash.flash().getProcessEngine().getTaskService().createTaskQuery().taskId(taskId).singleResult();
      final String pid = task.getProcessInstanceId();
      tasks.put(pid, taskId);
    }


    HistoricTaskInstancesQueryDefinition queryDefinition = new HistoricTaskInstancesQueryDefinition(tasks, workspace);
    LazyQueryContainer container = new LazyQueryContainer(queryDefinition, new HistoricTaskInstancesQueryFactory());
    historyTable.setContainerDataSource(container);
    historyTable.setPageLength(0);
    historyTable.setWidth("100%");
    historyTable.setSortDisabled(true);
    historyTable.setSelectable(false);
    historyTable.addContainerProperty("id", String.class, null);
    historyTable.addContainerProperty("name", String.class, null);
    historyTable.addContainerProperty("procedure", String.class, null);
    historyTable.addContainerProperty("startDate", String.class, null);
    historyTable.addContainerProperty("endDate", String.class, null);
    historyTable.addContainerProperty("assignee", String.class, null);
    historyTable.addContainerProperty("form", Component.class, null);
    historyTable.setColumnExpandRatio("id", 0.05f);
    historyTable.setColumnExpandRatio("startDate", 0.1f);
    historyTable.setColumnExpandRatio("endDate", 0.1f);
    historyTable.setColumnExpandRatio("name", 0.1f);
    historyTable.setColumnExpandRatio("procedure", 0.1f);
    historyTable.setColumnExpandRatio("assignee", 0.1f);
    historyTable.setColumnExpandRatio("form", 0.07f);
    historyTable.addStyleName("disable-scroll");
    historyTable.setVisibleColumns(new String[]{"id", "name", "procedure", "startDate", "endDate", "assignee", "form"});
    historyTable.setColumnHeaders(new String[]{"Заявка", "Этап", "Процедура", "Начало", "Окончание", "Назначен", ""});

    bidLayout.addComponent(historyTable);
    bidLayout.setExpandRatio(historyTable, 0.97f);
  }

  public void refresh() {
    historyTable.getContainerDataSource().removeAllItems();
    historyTable.refreshRowCache();
  }

  public List<HistoricTaskInstance> getInstances() {
    List<Object> itemIds = (List<Object>) historyTable.getItemIds();
    List<HistoricTaskInstance> result = new ArrayList<HistoricTaskInstance>();
    for (Object id : itemIds) {
      result.add((HistoricTaskInstance) historyTable.getItem(id).getItemProperty("hid").getValue());
    }
    return result;
  }

}
