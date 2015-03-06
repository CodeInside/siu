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
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;
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
import ru.codeinside.gses.webui.supervisor.SupervisorWorkplace;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HistoricTaskInstancesQuery implements Query, Serializable {
  private static final long serialVersionUID = 1L;
  private Map<String, String> ids;//key - ProcessDefinitionId, value - TaskId
  private final SupervisorWorkplace workspace;

  public HistoricTaskInstancesQuery(Map<String, String> ids, SupervisorWorkplace workspace) {
    this.ids = ids;
    this.workspace = workspace;
  }

  @Override
  public int size() {
    List<HistoricTaskInstance> histories = new ArrayList<HistoricTaskInstance>();
    for (final String processDefinitionId : ids.keySet()) {
      List<HistoricTaskInstance> result = Functions
          .withHistory(new Function<HistoryService, List<HistoricTaskInstance>>() {
            public List<HistoricTaskInstance> apply(HistoryService srv) {
              return srv.createHistoricTaskInstanceQuery().processInstanceId(processDefinitionId).list();
            }
          });
      histories.addAll(result);
    }
    return histories.size();
  }

  @Override
  public List<Item> loadItems(final int startIndex, final int count) {
    Map<HistoricTaskInstance, List<String>> histories = new LinkedHashMap<HistoricTaskInstance, List<String>>();
    for (final Map.Entry<String, String> entry : ids.entrySet()) {
      List<String> data = new ArrayList<String>();
      final Task task = Flash.flash().getProcessEngine().getTaskService().createTaskQuery().taskId(entry.getValue()).singleResult();
      final String tag = Flash.flash().getAdminService().getBidByTask(entry.getValue()).getTag();
      String procedureName = Flash.flash().getExecutorService().getProcedureNameByDefinitionId(task.getProcessDefinitionId());
      if (!tag.isEmpty()) {
        procedureName = tag + " - " + procedureName;
      }
      final String diagramTitle = procedureName;
      List<HistoricTaskInstance> result = Functions
          .withHistory(new Function<HistoryService, List<HistoricTaskInstance>>() {
            public List<HistoricTaskInstance> apply(HistoryService srv) {
              return srv.createHistoricTaskInstanceQuery().processInstanceId(entry.getKey()).listPage(startIndex, count);
            }
          });
      data.add(entry.getValue());
      data.add(procedureName);
      data.add(diagramTitle);

      for (HistoricTaskInstance his : result) {
        histories.put(his, data);
      }
    }
    List<Item> items = Lists.newArrayListWithExpectedSize(histories.size());

    for (Map.Entry<HistoricTaskInstance, List<String>> entry : histories.entrySet()) {
      final HistoricTaskInstance i = entry.getKey();
      final String taskId = entry.getValue().get(0);
      final String procedureName = entry.getValue().get(1);
      final String diagramTitle = entry.getValue().get(2);
      final Task task = Flash.flash().getProcessEngine().getTaskService().createTaskQuery().taskId(taskId).singleResult();

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
      Button view = new Button();
      view.setStyleName(BaseTheme.BUTTON_LINK);
      view.setDescription("Просмотр");
      view.setIcon(new ThemeResource("../custom/icon/view20.png"));
      view.addListener(new ArchiveFactory.ShowClickListener(i.getTaskDefinitionKey(), bidId, time));
      view.setEnabled(i.getAssignee() != null);

      HorizontalLayout buttons = new HorizontalLayout();
      buttons.addComponent(view);

      if (workspace != null) {
        Button showDiagram = new Button();
        showDiagram.setStyleName(BaseTheme.BUTTON_LINK);
        showDiagram.setDescription("Схема");
        showDiagram.setIcon(new ThemeResource("../custom/icon/scheme20.png"));


        Button deleteBid = new Button();
        deleteBid.setStyleName(BaseTheme.BUTTON_LINK);
        deleteBid.setDescription("Отклонить заявку");
        deleteBid.setIcon(new ThemeResource("../custom/icon/delete20.png"));

        if (endTime.isEmpty()) {
          deleteBid.addListener(workspace.new DeleteClickListener(taskId));
          showDiagram.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
              Bid bid = AdminServiceProvider.get().getBidByTask(taskId);
              final Window window = new Window(diagramTitle);
              window.setModal(true);
              window.setWidth(800, Sizeable.UNITS_PIXELS);
              window.setHeight(600, Sizeable.UNITS_PIXELS);
              VerticalLayout layout = new VerticalLayout();
              layout.setMargin(true);
              window.setContent(layout);

              final ShowDiagramComponentParameterObject param = new ShowDiagramComponentParameterObject();
              param.changer = new LayoutChanger(layout);
              param.processDefinitionId = task.getProcessDefinitionId();
              param.executionId = task.getExecutionId();
              param.windowHeader = bid == null ? "" : bid.getProcedure().getName() + " v. " + bid.getVersion();
              param.width = "100%";
              param.height = "100%";

              Execution execution = Flash.flash().getProcessEngine().getRuntimeService().createExecutionQuery().executionId(param.executionId).singleResult();

              if (execution == null) {
                layout.addComponent(new Label("Заявка уже исполнена"));
                window.center();
                event.getButton().getWindow().addWindow(window);
                return;
              }

              ShowDiagramComponent showDiagramComponent = new ShowDiagramComponent(param, true);
              layout.addComponent(showDiagramComponent);
              window.center();
              event.getButton().getWindow().addWindow(window);
            }
          });
        } else {
          deleteBid.setEnabled(false);
          showDiagram.setEnabled(false);
        }

        buttons.addComponent(showDiagram);
        buttons.addComponent(deleteBid);
      }

      item.addItemProperty("form", new ObjectProperty<HorizontalLayout>(buttons));

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
    PropertysetItem item = new PropertysetItem();
    item.addItemProperty("id", new ObjectProperty<String>(""));
    item.addItemProperty("name", new ObjectProperty<String>(""));
    item.addItemProperty("procedure", new ObjectProperty<String>(""));
    item.addItemProperty("startDate", new ObjectProperty<String>(""));
    item.addItemProperty("endDate", new ObjectProperty<String>(""));
    item.addItemProperty("assignee", new ObjectProperty<String>(""));
    return item;
  }
}
