/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.executor;

import com.google.common.base.Function;
import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.BidStatus;
import ru.codeinside.adm.database.TaskDates;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.beans.ActivitiBean;
import ru.codeinside.gses.service.ExecutorService;
import ru.codeinside.gses.service.Functions;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.components.ProcedureHistoryPanel;
import ru.codeinside.gses.webui.components.ShowDiagramComponent;
import ru.codeinside.gses.webui.components.ShowDiagramComponentParameterObject;
import ru.codeinside.gses.webui.components.TableForExecutor;
import ru.codeinside.gses.webui.components.TaskGraphListener;
import ru.codeinside.gses.webui.components.api.Changer;
import ru.codeinside.gses.webui.components.api.WithTaskId;
import ru.codeinside.gses.webui.data.AssigneeTaskListQuery;
import ru.codeinside.gses.webui.data.BatchItemBuilder;
import ru.codeinside.gses.webui.data.CandidateTaskListQuery;
import ru.codeinside.gses.webui.data.Durations;
import ru.codeinside.gses.webui.data.ItemBuilder;
import ru.codeinside.gses.webui.data.TaskStylist;
import ru.codeinside.gses.webui.eventbus.TaskChanged;
import ru.codeinside.gses.webui.form.DataAccumulator;
import ru.codeinside.gses.webui.form.FormDescription;
import ru.codeinside.gses.webui.form.FormDescriptionBuilder;
import ru.codeinside.gses.webui.form.TaskForm;
import ru.codeinside.gses.webui.supervisor.TaskFilter;
import ru.codeinside.gses.webui.utils.Components;

import java.text.SimpleDateFormat;

import static ru.codeinside.gses.webui.utils.Components.buttonProperty;
import static ru.codeinside.gses.webui.utils.Components.stringProperty;

public class ExecutorFactory {

  public final static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");

  public static Component create(final Changer changer, final TabSheet tabs) {


    final ItemBuilder<Task> assigneeBuilder = new BatchItemBuilder<Task>() {
      private static final long serialVersionUID = 1L;

      transient Durations durations;

      @Override
      public void batchStart() {
        durations = new Durations();
      }

      @Override
      public void batchFinish() {
        durations = null;
      }

      @Override
      public Item createItem(final Task task) {
        final String taskId = task.getId();
        final ProcessDefinition def = ActivitiBean.get().getProcessDefinition(task.getProcessDefinitionId(), Flash.login());
        String procedureName = Flash.flash().getExecutorService().getProcedureNameByDefinitionId(def.getId());
        final PropertysetItem item = new PropertysetItem();
        final Bid bid = getBid(task);
        final String bidId = bid.getId() == null ? "" : bid.getId().toString();
        item.addItemProperty("id", buttonProperty(bidId, new TaskGraphListener(changer, task)));
        item.addItemProperty("name", stringProperty(task.getName()));
        item.addItemProperty("startDate", stringProperty(formatter.format(bid.getDateCreated())));
        item.addItemProperty("declarant", stringProperty(bid.getDeclarant()));
        if (bid.getTag() == null || bid.getTag().isEmpty()) {
          item.addItemProperty("process", stringProperty(procedureName));
        } else {
          item.addItemProperty("process", stringProperty(bid.getTag() + " - " + procedureName));
        }
        item.addItemProperty("version", stringProperty(bid.getVersion()));
        final Button b = new Button("Обработать", new Button.ClickListener() {
          private static final long serialVersionUID = 1L;

          @Override
          public void buttonClick(ClickEvent event) {
            try {
              showForm(tabs, taskId);
            } catch (Exception e) {
              Components.showException(event.getButton().getWindow(), e);
            }
          }
        });
        b.setStyleName(Reindeer.BUTTON_SMALL);
        item.addItemProperty("claim", new ObjectProperty<Component>(b));
        item.addItemProperty("priority", stringProperty(String.valueOf(task.getPriority())));
        TaskDates td = AdminServiceProvider.get().getTaskDatesByTaskId(taskId);
        durations.fillBidAndTask(bid, td, item);
        return item;
      }

      private Bid getBid(final Task task) {
        Bid bid = AdminServiceProvider.get().getBidByTask(task.getId());

        if (bid == null) {
          bid = new Bid();
          bid.setDeclarant("");
          bid.setVersion("");
          bid.setStatus(BidStatus.Execute);
        }
        return bid;
      }

    };
    final AssigneeTaskListQuery assigneeTaskQuery = new AssigneeTaskListQuery(assigneeBuilder);
    final TableForExecutor assignee = new TableForExecutor(assigneeTaskQuery);
    assignee.setCaption("Исполняемые заявки");
    assignee.addContainerProperty("id", Component.class, null);
    assignee.addContainerProperty("name", String.class, null);
    assignee.addContainerProperty("startDate", String.class, null);
    assignee.addContainerProperty("declarant", String.class, null);
    assignee.addContainerProperty("process", String.class, null);
    assignee.addContainerProperty("version", String.class, null);
    assignee.addContainerProperty("status", String.class, null);
    assignee.addContainerProperty("claim", Component.class, null);
    assignee.addContainerProperty("priority", String.class, null);
    assignee.addContainerProperty("bidDays", String.class, null);
    assignee.addContainerProperty("taskDays", String.class, null);
    assignee.setVisibleColumns(new Object[]{"id", "name", "startDate", "declarant", "process", "version", "claim", "bidDays", "taskDays"});
    assignee.setColumnHeaders(new String[]{"Номер", "Этап", "Дата подачи заявки", "Заявитель", "Процедура", "v", "", "Ср.з.", "Ср.эт."});
    assignee.setSelectable(false);
    assignee.setSortDisabled(true);

    assignee.setColumnAlignment("id", Table.ALIGN_RIGHT);
    assignee.setColumnExpandRatio("id", 0.3f);
    assignee.setColumnExpandRatio("name", 1f);
    assignee.setColumnExpandRatio("process", 1.2f);
    assignee.setColumnExpandRatio("bidDays", 0.2f);
    assignee.setColumnExpandRatio("taskDays", 0.2f);

    assignee.setCellStyleGenerator(new TaskStylist(assignee));

    final ItemBuilder<Task> claimBuilder = new BatchItemBuilder<Task>() {
      private static final long serialVersionUID = 1L;

      Durations durations;

      @Override
      public void batchStart() {
        durations = new Durations();
      }

      @Override
      public void batchFinish() {
        durations = null;
      }

      @Override
      public Item createItem(final Task task) {
        final String taskId = task.getId();
        final ProcessDefinition def = ActivitiBean.get().getProcessDefinition(task.getProcessDefinitionId(), Flash.login());
        String procedureName = Flash.flash().getExecutorService().getProcedureNameByDefinitionId(def.getId());
        PropertysetItem item = new PropertysetItem();
        final Bid bid = getBid(task);
        ObjectProperty idProperty;
        if (bid.getId() == null) {
          idProperty = new ObjectProperty<String>("");
        } else {
          idProperty = buttonProperty(bid.getId().toString(), new TaskGraphListener(changer, task));
        }
        item.addItemProperty("id", idProperty);
        item.addItemProperty("name", stringProperty(task.getName()));
        item.addItemProperty("startDate", stringProperty(formatter.format(bid.getDateCreated())));
        item.addItemProperty("declarant", stringProperty(bid.getDeclarant()));
        if (bid.getTag() == null || bid.getTag().isEmpty()) {
          item.addItemProperty("process", stringProperty(procedureName));
        } else {
          item.addItemProperty("process", stringProperty(bid.getTag() + " - " + procedureName));
        }
        item.addItemProperty("version", stringProperty(bid.getVersion()));
        item.addItemProperty("status", stringProperty(bid.getStatus().getName()));
        final Button b = new Button("Забрать", new Button.ClickListener() {
          private static final long serialVersionUID = 1L;

          @Override
          public void buttonClick(ClickEvent event) {
            final String login = Flash.login();
            final String errMessage = ActivitiBean.get().claim(taskId, login, login, false);
            if (!StringUtils.isEmpty(errMessage)) {
              event.getButton().getWindow()
                .showNotification("Ошибка", errMessage, Notification.TYPE_ERROR_MESSAGE);
            }
            fireTaskChangedEvent(taskId, this);
          }
        });
        b.setStyleName(Reindeer.BUTTON_SMALL);
        item.addItemProperty("claim", new ObjectProperty<Component>(b));
        item.addItemProperty("priority", stringProperty(String.valueOf(task.getPriority())));
        TaskDates td = AdminServiceProvider.get().getTaskDatesByTaskId(taskId);
        durations.fillBidAndTask(bid, td, item);
        return item;
      }

      private Bid getBid(final Task task) {
        Bid bid = AdminServiceProvider.get().getBidByTask(task.getId());

        if (bid == null) {
          bid = new Bid();
          bid.setDeclarant("");
          bid.setVersion("");
          bid.setStatus(BidStatus.Execute);
        }
        return bid;
      }

    };
    final CandidateTaskListQuery candidateTaskQuery = new CandidateTaskListQuery(claimBuilder, Flash.login());
    final TableForExecutor candidate = new TableForExecutor(candidateTaskQuery);
    candidate.setCaption("Открытые заявки");
    candidate.addContainerProperty("id", Component.class, null);
    candidate.addContainerProperty("name", String.class, null);
    candidate.addContainerProperty("startDate", String.class, null);
    candidate.addContainerProperty("declarant", String.class, null);
    candidate.addContainerProperty("process", String.class, null);
    candidate.addContainerProperty("version", String.class, null);
    candidate.addContainerProperty("status", String.class, null);
    candidate.addContainerProperty("claim", Component.class, null);
    candidate.addContainerProperty("priority", String.class, null);
    candidate.addContainerProperty("bidDays", String.class, null);
    candidate.addContainerProperty("taskDays", String.class, null);
    candidate.setVisibleColumns(new Object[]{"id", "name", "startDate", "declarant", "process", "version", "status", "claim", "bidDays", "taskDays"});
    candidate.setColumnHeaders(new String[]{"Номер", "Этап", "Дата подачи заявки", "Заявитель", "Процедура", "v", "Статус", "", "Ср.з.", "Ср.эт."});
    candidate.setSelectable(false);
    candidate.setSortDisabled(true);

    candidate.setColumnAlignment("id", Table.ALIGN_RIGHT);
    candidate.setColumnExpandRatio("name", 1f);
    candidate.setColumnExpandRatio("process", 1f);

    candidate.setCellStyleGenerator(new TaskStylist(candidate));

    final Form filter = new TaskFilter(candidateTaskQuery, candidate, assigneeTaskQuery, assignee, TaskFilter.Mode.Executor);

    HorizontalLayout asigneeLayout = new HorizontalLayout();
    asigneeLayout.setSizeFull();
    asigneeLayout.setMargin(true);
    asigneeLayout.setSpacing(true);
    asigneeLayout.addComponent(assignee);

    Panel filterPanel = new Panel();
    filterPanel.setHeight("100%");
    filterPanel.addComponent(filter);
    final HorizontalSplitPanel hSplitter = new HorizontalSplitPanel();
    hSplitter.setSizeFull();
    hSplitter.setFirstComponent(filterPanel);
    hSplitter.setSecondComponent(asigneeLayout);
    hSplitter.setSplitPosition(35);

    VerticalLayout candidateLayout = new VerticalLayout();
    candidateLayout.setSizeFull();
    candidateLayout.setMargin(true);
    candidateLayout.addComponent(candidate);

    final TasksSplitter vSplitter = new TasksSplitter(assignee, candidate);
    vSplitter.setSizeFull();
    vSplitter.setFirstComponent(hSplitter);
    vSplitter.setSecondComponent(candidateLayout);
    vSplitter.setSplitPosition(57);
    return vSplitter;
  }

  static public void showTask(final Changer changer, final Task task) {
    ProcessDefinitionEntity def = Functions.withRepository(Flash.login(),
      new Function<RepositoryService, ProcessDefinitionEntity>() {
        public ProcessDefinitionEntity apply(final RepositoryService srv) {
          RepositoryServiceImpl impl = (RepositoryServiceImpl) srv;
          return (ProcessDefinitionEntity) impl.getDeployedProcessDefinition(task
            .getProcessDefinitionId());
        }
      }
    );
    showTask(changer, task, def);
  }

  static void showTask(final Changer changer, final Task task, final ProcessDefinitionEntity def) {
    final VerticalLayout layout = new VerticalLayout();
    layout.setMargin(true);
    layout.setSpacing(true);
    layout.setSizeFull();
    final Button cancel = new Button("Назад", new Button.ClickListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void buttonClick(ClickEvent event) {
        changer.back();
      }
    });
    cancel.setClickShortcut(KeyCode.ESCAPE, 0);
    layout.addComponent(cancel);

    HorizontalLayout hl = new HorizontalLayout();
    hl.setSizeFull();
    hl.setSpacing(true);
    layout.addComponent(hl);
    layout.setExpandRatio(cancel, 0.01f);
    layout.setExpandRatio(hl, 0.99f);

    VerticalLayout vl = new VerticalLayout();
    vl.setSizeFull();
    vl.setSpacing(true);
    hl.addComponent(vl);

    final String procedureName = Flash.flash().getExecutorService().getProcedureNameByDefinitionId(def.getId());
    vl.addComponent(new ProcedureHistoryPanel(task.getId()));

    ShowDiagramComponentParameterObject parameterObject = new ShowDiagramComponentParameterObject();
    final String procVersion = def.getVersion() + "";
    parameterObject.windowHeader = procedureName + "v. " + procVersion;
    parameterObject.width = "600px";
    parameterObject.executionId = task.getExecutionId();
    parameterObject.changer = changer;
    parameterObject.processDefinitionId = def.getId();
    parameterObject.caption = "Диаграмма";
    ShowDiagramComponent showDiagramComponent = new ShowDiagramComponent(parameterObject);
    hl.addComponent(showDiagramComponent);
    hl.setExpandRatio(vl, 0.5f);
    hl.setExpandRatio(showDiagramComponent, 0.5f);

    changer.change(layout);
    cancel.focus();
  }

  static private void showForm(final TabSheet tabs, final String taskId) {
    // Определяем открыта ли уже
    for (int i = tabs.getComponentCount(); i > 0; i--) {
      final Tab tab = tabs.getTab(i - 1);
      if (tab.getComponent() instanceof WithTaskId) {
        if (taskId.equals(((WithTaskId) tab.getComponent()).getTaskId())) {
          tabs.setSelectedTab(tab);
          return;
        }
      }
    }
    DataAccumulator accumulator = new DataAccumulator();
    ExecutorService executorService = Flash.flash().getExecutorService();
    final FormDescription formDescription = Functions.withEngine(
            new FormDescriptionBuilder(FormID.byTaskId(taskId), executorService, accumulator));
    final TaskForm taskForm = new TaskForm(formDescription, new TaskForm.CloseListener() {

      private static final long serialVersionUID = 3726145663843346543L;

      @Override
      public void onFormClose(final TaskForm form) {
        final TabSheet.Tab tab = tabs.getTab(tabs.getSelectedTab());
        int pos = tabs.getTabPosition(tab);
        tabs.removeTab(tab);
        int count = tabs.getComponentCount();
        tabs.setSelectedTab(count == pos ? pos - 1 : pos);
      }
    }, accumulator);
    final Tab tab = tabs.addTab(taskForm, formDescription.task.getName());
    tab.setDescription("Услуга \"" + formDescription.processDefinition.getName() + "\"");
    tabs.setSelectedTab(tab);
  }

  static private void fireTaskChangedEvent(final String taskId, final Object source) {
    Flash.fire(new TaskChanged(source, taskId));
  }

}
