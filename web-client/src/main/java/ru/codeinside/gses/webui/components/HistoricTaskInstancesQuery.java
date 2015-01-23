/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.gses.lazyquerycontainer.Query;
import ru.codeinside.gses.service.Functions;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.executor.ArchiveFactory;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoricTaskInstancesQuery implements Query, Serializable {
  private static final long serialVersionUID = 1L;
  private String processDefinitionId;
  private String taskId;

  public HistoricTaskInstancesQuery(String processDefinitionId, String taskId) {
    this.processDefinitionId = processDefinitionId;
    this.taskId = taskId;
  }

  @Override
  public int size() {
    List<HistoricTaskInstance> histories = Functions
      .withHistory(new Function<HistoryService, List<HistoricTaskInstance>>() {
        public List<HistoricTaskInstance> apply(HistoryService srv) {
          return srv.createHistoricTaskInstanceQuery().processInstanceId(processDefinitionId).list();
        }
      });
    return histories.size();
  }

  @Override
  public List<Item> loadItems(final int startIndex, final int count) {
    final Task task = Flash.flash().getProcessEngine().getTaskService().createTaskQuery().taskId(taskId).singleResult();
    final String tag = Flash.flash().getAdminService().getBidByTask(taskId).getTag();
    String procedureName = Flash.flash().getExecutorService().getProcedureNameByDefinitionId(task.getProcessDefinitionId());
    if (!tag.isEmpty()) {
      procedureName = tag + " - " + procedureName;
    }
    List<HistoricTaskInstance> histories = Functions
      .withHistory(new Function<HistoryService, List<HistoricTaskInstance>>() {
        public List<HistoricTaskInstance> apply(HistoryService srv) {
          return srv.createHistoricTaskInstanceQuery().processInstanceId(processDefinitionId).listPage(startIndex, count);
        }
      });
    List<Item> items = Lists.newArrayListWithExpectedSize(histories.size());
    for (final HistoricTaskInstance i : histories) {
      String startTime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(i.getStartTime());
      String endTime = (i.getEndTime() != null) ? new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(i.getEndTime()) : "";
      Bid bid = AdminServiceProvider.get().getBidByTask(taskId);
      String bidId = bid != null ? bid.getId().toString() : "";
      PropertysetItem item = new PropertysetItem();
      item.addItemProperty("id", new ObjectProperty<String>(bidId));
      item.addItemProperty("hid", new ObjectProperty<HistoricTaskInstance>(i));
      item.addItemProperty("name", new ObjectProperty<String>(i.getName()));
      item.addItemProperty("procedure", new ObjectProperty<String>(procedureName));
      item.addItemProperty("startDate", new ObjectProperty<String>(startTime));
      item.addItemProperty("endDate", new ObjectProperty<String>(endTime));
      item.addItemProperty("assignee", new ObjectProperty<String>(i.getAssignee() != null ? i.getAssignee() : ""));
      Date time = i.getEndTime() == null ? i.getStartTime() : i.getEndTime();
      Button button = new Button("Просмотр");
      button.addListener(new ArchiveFactory.ShowClickListener(i.getTaskDefinitionKey(), bidId, time));
      button.setEnabled(i.getAssignee() != null);
      button.setWidth(80, Sizeable.UNITS_PIXELS);

      Button showDiagram = new Button("Схема");
      showDiagram.addListener(new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent event) {
          Bid bid = AdminServiceProvider.get().getBidByTask(taskId);
          final Window window = new Window("Sub Window");
          window.setModal(true);
          VerticalLayout layout = new VerticalLayout();
          layout.setMargin(true);
          window.setContent(layout);
          final ShowDiagramComponentParameterObject param = new ShowDiagramComponentParameterObject();
          param.changer = new LayoutChanger(layout);
          param.processDefinitionId = task.getProcessDefinitionId();
          param.executionId = task.getExecutionId();
          param.height = "300px";
          param.width = null;
          param.windowHeader = bid == null ? "" : bid.getProcedure().getName() + " v. " + bid.getVersion();

          Execution execution = Flash.flash().getProcessEngine().getRuntimeService().createExecutionQuery().executionId(param.executionId).singleResult();

            if (execution == null) {
            window.showNotification("Заявка уже исполнена");
            return;
          }
          ShowDiagramComponent showDiagramComponent = new ShowDiagramComponent(param);
          layout.addComponent(showDiagramComponent);
          window.center();
          event.getButton().getWindow().addWindow(window);
        }
      });
      showDiagram.setWidth(80, Sizeable.UNITS_PIXELS);

      VerticalLayout buttons = new VerticalLayout();
      buttons.addComponent(button);
      buttons.addComponent(showDiagram);
      buttons.setStyleName("historicButtons");

      item.addItemProperty("form", new ObjectProperty<VerticalLayout>(buttons));

      items.add(item);
    }
    return items;
  }

  @Override
  public void saveItems(List<Item> addedItems, List<Item> modifiedItems, List<Item> removedItems) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean deleteAllItems() {
    return true;
    //    throw new UnsupportedOperationException();
  }

  @Override
  public Item constructItem() {
    throw new UnsupportedOperationException();
  }
}
