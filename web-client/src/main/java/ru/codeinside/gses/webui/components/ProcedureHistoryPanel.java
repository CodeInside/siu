/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryContainer;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.utils.Components;

public class ProcedureHistoryPanel extends VerticalLayout {
  private Table historyTable;
  public ProcedureHistoryPanel(String taskId) {
    buildLayout(taskId);
  }
  private void buildLayout(final String taskId) {
    final Task task = Flash.flash().getProcessEngine().getTaskService().createTaskQuery().taskId(taskId).singleResult();

    Panel bidPanel = new Panel("История исполнения заявки");
    addComponent(bidPanel);
    VerticalLayout bidLayout = new VerticalLayout();
    bidLayout.setSizeFull();
    bidLayout.setSpacing(true);
    bidPanel.addComponent(bidLayout);

    historyTable = Components.createTable(null, null);
    final String pid = task.getProcessInstanceId();
    HistoricTaskInstancesQueryDefinition queryDefinition = new HistoricTaskInstancesQueryDefinition(pid, taskId);
    LazyQueryContainer container = new LazyQueryContainer(queryDefinition, new HistoricTaskInstancesQueryFactory());
    historyTable.setContainerDataSource(container);
    historyTable.setPageLength(0);
    historyTable.setWidth("100%");
    historyTable.setSortDisabled(true);
    historyTable.setSelectable(true);
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
    historyTable.setColumnExpandRatio("form", 0.1f);
    historyTable.addStyleName("disable-scroll");
    historyTable.setVisibleColumns(new String[]{"id", "name", "procedure", "startDate", "endDate", "assignee", "form"});
    historyTable.setColumnHeaders(new String[]{"Заявка", "Этап", "Процедура", "Начало", "Окончание", "Назначен", ""});
    historyTable.addListener(new ItemClickEvent.ItemClickListener() {
      @Override
      public void itemClick(ItemClickEvent event) {
        HistoricTaskInstance i = (HistoricTaskInstance) event.getItem().getItemProperty("hid").getValue();
        fireEvent(new HistoryStepClickedEvent(event.getComponent(), i));
      }
    });
    bidLayout.addComponent(historyTable);
    bidLayout.setExpandRatio(historyTable, 0.97f);
  }

  public void refresh() {
    historyTable.getContainerDataSource().removeAllItems();
    historyTable.refreshRowCache();
  }

  public class HistoryStepClickedEvent extends Event {
    private final HistoricTaskInstance historicTaskInstance;

    public HistoryStepClickedEvent(Component source, HistoricTaskInstance historicTaskInstance) {
      super(source);
      this.source = source;
      this.historicTaskInstance = historicTaskInstance;
    }

    public HistoricTaskInstance getHistoricTaskInstance() {
      return historicTaskInstance;
    }
  }
}
