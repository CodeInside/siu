/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.supervisor;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.BidStatus;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.adm.database.Procedure;
import ru.codeinside.adm.database.TaskDates;
import ru.codeinside.adm.ui.FilterDecorator_;
import ru.codeinside.adm.ui.FilterGenerator_;
import ru.codeinside.adm.ui.LazyLoadingContainer2;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.beans.ActivitiBean;
import ru.codeinside.gses.cert.NameParts;
import ru.codeinside.gses.cert.X509;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryContainer;
import ru.codeinside.gses.webui.CertificateInvalid;
import ru.codeinside.gses.webui.CertificateReader;
import ru.codeinside.gses.webui.CertificateVerifier;
import ru.codeinside.gses.webui.CertificateVerifyClientProvider;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.components.LayoutChanger;
import ru.codeinside.gses.webui.components.ProcedureHistoryPanel;
import ru.codeinside.gses.webui.components.api.Changer;
import ru.codeinside.gses.webui.components.sign.SignApplet;
import ru.codeinside.gses.webui.components.sign.SignAppletListener;
import ru.codeinside.gses.webui.data.BatchItemBuilder;
import ru.codeinside.gses.webui.data.ControlledTasksQuery;
import ru.codeinside.gses.webui.data.Durations;
import ru.codeinside.gses.webui.data.ItemBuilder;
import ru.codeinside.gses.webui.data.TaskStylist;
import ru.codeinside.gses.webui.eventbus.TaskChanged;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static ru.codeinside.gses.webui.utils.Components.stringProperty;


public class SupervisorWorkplace extends HorizontalSplitPanel {

  private Panel filterPanel;
  private Panel infoPanel;
  private Panel listPanel;
  private Changer bidChanger;
  private Component item2;
  private Component item3;
  private ControlledTasksQuery controlledTasksQuery;
  private ControlledTasksTable controlledTasksTable;
  private Component item1;
  private Changer infoChanger;
  private Component bidComponent;
  private String taskIdToAssign;
  private Set<String> taskIdsToAssign;
  private Component infoComponent;
  private Object filterComponent;
  private ProcedureHistoryPanel procedureHistoryPanel;

  public SupervisorWorkplace() {
    super();

    Employee emp = Flash.flash().getAdminService().findEmployeeByLogin(Flash.login());
    ItemBuilder<Task> taskItemBuilder = getTaskItemBuilder();
    controlledTasksQuery = new ControlledTasksQuery(taskItemBuilder, emp);
    controlledTasksTable = new ControlledTasksTable(controlledTasksQuery);
    buildMainLayout();
    buildFilterPanel();
    buildListPanel();
    buildInfoPanel();
  }

  public void onTaskChanged(final TaskChanged event) {
    final Object source = event.getSource();
    if (source != this) {
      controlledTasksTable.refresh();
      controlledTasksTable.select(null);
      // ставим пустую панель
      ((VerticalLayout) item1).removeAllComponents();
      bidChanger.change(item1);
    }
  }

  @Override
  public void attach() {
    super.attach();
    Flash.bind(TaskChanged.class, this, "onTaskChanged");
  }

  @Override
  public void detach() {
    Flash.unbind(TaskChanged.class, this, "onTaskChanged");
    super.detach();
  }

  private void buildInfoPanel() {
    VerticalLayout layout = new VerticalLayout();
    layout.setSizeFull();
    infoChanger = new LayoutChanger(layout);
    infoPanel.addComponent(layout);
    infoComponent = createInfoComponent();
    bidComponent = createBidComponent();
    infoChanger.set(infoComponent, "info");
    infoChanger.set(bidComponent, "bid");
    infoChanger.change(infoComponent);
  }


  private Component createBidComponent() {
    VerticalLayout mainLayout = new VerticalLayout();
    mainLayout.setSizeFull();
    bidChanger = new LayoutChanger(mainLayout);
    item1 = createBidChangerItem1();
    item2 = createBidChangerItem2();
    item3 = createBidChangerItem3();
    bidChanger.set(item1, "item1");
    bidChanger.set(item2, "item2");
    bidChanger.set(item3, "item3");
    bidChanger.change(item1);
    return mainLayout;
  }

  private Component createBidChangerItem3() {
    return new VerticalLayout();
  }

  private Component createBidChangerItem2() {
    VerticalLayout layout = new VerticalLayout();
    layout.setSizeFull();
    return layout;
  }

  private Component createBidChangerItem1() {
    VerticalLayout layout = new VerticalLayout();
    layout.setSpacing(true);
    layout.setSizeFull();
    return layout;
  }

  private Component createInfoComponent() {
    VerticalLayout layout = new VerticalLayout();
    layout.setSizeFull();
    return layout;
  }

  private void buildListPanel() {
    controlledTasksTable.setHeight("255px");
    controlledTasksTable.setWidth("100%");
    controlledTasksTable.setImmediate(true);
    controlledTasksTable.setSelectable(true);
    controlledTasksTable.setSortDisabled(true);
    controlledTasksTable.addContainerProperty("id", Component.class, null);
    controlledTasksTable.addContainerProperty("dateCreated", String.class, null);
    controlledTasksTable.addContainerProperty("task", String.class, null);
    controlledTasksTable.addContainerProperty("procedure", String.class, null);
    controlledTasksTable.addContainerProperty("declarant", String.class, null);
    controlledTasksTable.addContainerProperty("version", String.class, null);
    controlledTasksTable.addContainerProperty("status", String.class, null);
    controlledTasksTable.addContainerProperty("employee", String.class, null);
    controlledTasksTable.addContainerProperty("priority", String.class, null);
    controlledTasksTable.addContainerProperty("bidDays", String.class, null);
    controlledTasksTable.addContainerProperty("taskDays", String.class, null);
    controlledTasksTable.setVisibleColumns(new Object[]{"id", "dateCreated", "task", "procedure", "declarant", "version", "status", "employee", "bidDays", "taskDays"});
    controlledTasksTable.setColumnHeaders(new String[]{"Номер", "Дата подачи заявки", "Этап", "Процедура", "Заявитель", "Версия", "Статус", "Исполнитель", "Ср.з.", "Ср.эт."});
    controlledTasksTable.setColumnExpandRatio("id", 0.05f);
    controlledTasksTable.setColumnExpandRatio("dateCreated", 0.15f);
    controlledTasksTable.setColumnExpandRatio("task", 0.2f);
    controlledTasksTable.setColumnExpandRatio("procedure", 0.25f);
    controlledTasksTable.setColumnExpandRatio("declarant", 0.1f);
    controlledTasksTable.setColumnExpandRatio("version", 0.05f);
    controlledTasksTable.setColumnExpandRatio("status", 0.1f);
    controlledTasksTable.setColumnExpandRatio("employee", 0.1f);
    controlledTasksTable.setCellStyleGenerator(new TaskStylist(controlledTasksTable));
    controlledTasksTable.setMultiSelect(true);
    listPanel.addComponent(controlledTasksTable);
    
    final Button assignExecutorButton = new Button("Назначить исполнителя");
    assignExecutorButton.addListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        ((Layout) item3).removeAllComponents();
        if (taskIdsToAssign.size() > 0) {
          ((Layout) item3).addComponent(createAssignerToTaskComponent(taskIdsToAssign, procedureHistoryPanel, controlledTasksTable));
          bidChanger.change(item3);
        } else {
          alreadyGone();
        }
      }
    });

    controlledTasksTable.addListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        Table table = (Table) event.getProperty();
        Set<Object> itemIds = (Set<Object>) table.getValue();
        Item item;
        Set<String> taskIds = new LinkedHashSet<String>();
        for (Object id : itemIds) {
          item = table.getItem(id);
          if (item != null && item.getItemProperty("id") != null)
            taskIds.add(item.getItemProperty("taskId").getValue().toString());
        }

        if (taskIds.size() > 0) {
          procedureHistoryPanel = new ProcedureHistoryPanel(taskIds, SupervisorWorkplace.this);

          List<HistoricTaskInstance> tasks = procedureHistoryPanel.getInstances();
          taskIdsToAssign = new HashSet<String>();
          for (HistoricTaskInstance historicTaskInstance : tasks) {
            Date endDateTime = historicTaskInstance.getEndTime();
            if (endDateTime == null) {
              taskIdToAssign = findTaskByHistoricInstance(historicTaskInstance);
              if (taskIdToAssign != null) {
                taskIdsToAssign.add(taskIdToAssign);
              }
            }
          }
          if (taskIdsToAssign.size() == 0) {
            assignExecutorButton.setVisible(false);
          }

          ((VerticalLayout) item1).removeAllComponents();
          ((VerticalLayout) item1).addComponent(assignExecutorButton);
          ((VerticalLayout) item1).addComponent(procedureHistoryPanel);

          infoChanger.change(bidComponent);
          bidChanger.change(item1);
        } else {
          ((VerticalLayout) item1).removeAllComponents();
        }
      }
    });
  }

  private String findTaskByHistoricInstance(HistoricTaskInstance historicTaskInstance) {
    Task task = Flash.flash().getProcessEngine().getTaskService().createTaskQuery().taskId(historicTaskInstance.getId()).singleResult();
    if (task == null) {
      return null;
    }
    return task.getId();
  }

  private void alreadyGone() {
    controlledTasksTable.refresh();
    ((Layout) item3).removeAllComponents();
    bidChanger.change(item3);
    getWindow().showNotification("Заявка уже исполнена");
  }

  private Component createAssignerToTaskComponent(final Set<String> taskIds, final ProcedureHistoryPanel procedureHistoryPanel, final ControlledTasksTable table) {
    final List<Task> tasks = new ArrayList<Task>();
    for (String taskId : taskIds) {
      Task task = Flash.flash().getProcessEngine().getTaskService().createTaskQuery().taskId(taskId).singleResult();
      if (task != null) {
        tasks.add(task);
      }
    }
    final VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.setSizeFull();
    verticalLayout.setSpacing(true);
    Button backButton = new Button("Назад");
    backButton.addListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        bidChanger.back();
      }
    });
    verticalLayout.addComponent(backButton);

    HorizontalLayout horizontalLayout = new HorizontalLayout();
    horizontalLayout.setSizeFull();
    horizontalLayout.setSpacing(true);

    LazyLoadingContainer2 orgGroupsLazyQueryContainer = new LazyLoadingContainer2(new GroupsQuery(GroupsQueryDefinition.Mode.ORG, Flash.login()));
    final FilterTable orgGroupsTable = new FilterTable();
    orgGroupsTable.setFilterBarVisible(true);
    orgGroupsTable.setSizeFull();
    orgGroupsTable.setImmediate(true);
    orgGroupsTable.setSelectable(true);
    orgGroupsTable.setFilterDecorator(new FilterDecorator_());
    orgGroupsTable.setFilterGenerator(new FilterGenerator_());
    orgGroupsLazyQueryContainer.addContainerProperty("name", String.class, null);
    orgGroupsLazyQueryContainer.addContainerProperty("title", String.class, null);
    orgGroupsTable.setContainerDataSource(orgGroupsLazyQueryContainer);
    orgGroupsTable.setHeight("150px");
    orgGroupsTable.setCaption("Доступные группы организаций");
    orgGroupsTable.setColumnHeaders(new String[]{"Код группы", "Название группы"});


    LazyLoadingContainer2 empGroupsLazyQueryContainer = new LazyLoadingContainer2(new GroupsQuery(GroupsQueryDefinition.Mode.EMP, Flash.login()));
    final FilterTable empGroupsTable = new FilterTable();
    empGroupsTable.setFilterBarVisible(true);
    empGroupsTable.setSizeFull();
    empGroupsTable.setImmediate(true);
    empGroupsTable.setSelectable(true);
    empGroupsTable.setFilterDecorator(new FilterDecorator_());
    empGroupsTable.setFilterGenerator(new FilterGenerator_());
    empGroupsLazyQueryContainer.addContainerProperty("name", String.class, null);
    empGroupsLazyQueryContainer.addContainerProperty("title", String.class, null);
    empGroupsTable.setContainerDataSource(empGroupsLazyQueryContainer);
    empGroupsTable.setCaption("Доступные группы сотрудников");
    empGroupsTable.setHeight("150px");
    empGroupsTable.setColumnHeaders(new String[]{"Код группы", "Название группы"});

    final Table employeesTable = new Table();
    employeesTable.setSizeFull();
    employeesTable.setSelectable(true);
    employeesTable.setHeight("150px");
    employeesTable.setCaption("Исполнители доступные для назначения, входящие в выбранную группу");
    employeesTable.addContainerProperty("login", String.class, null);
    employeesTable.addContainerProperty("fio", String.class, null);
    employeesTable.setColumnHeaders(new String[]{"Логин", "ФИО"});
    employeesTable.setVisible(false);

    orgGroupsTable.addListener(new ItemClickEvent.ItemClickListener() {
      @Override
      public void itemClick(ItemClickEvent event) {
        if (tasks.size() == 0) {
          alreadyGone();
          return;
        }
        empGroupsTable.select(null);
        employeesTable.setVisible(true);
        String groupName = event.getItem().getItemProperty("name") != null ? (String) event.getItem().getItemProperty("name").getValue() : "";
        GroupMembersQueryDefinition groupMembersQueryDefinition = new GroupMembersQueryDefinition(groupName, GroupMembersQuery.Mode.ORG);
        LazyQueryContainer groupMembersContainer = new LazyQueryContainer(groupMembersQueryDefinition, new GroupMembersFactory(taskIds));
        employeesTable.setContainerDataSource(groupMembersContainer);
      }
    });

    empGroupsTable.addListener(new ItemClickEvent.ItemClickListener() {
      @Override
      public void itemClick(ItemClickEvent event) {
        if (tasks.size() == 0) {
          alreadyGone();
          return;
        }
        orgGroupsTable.select(null);
        employeesTable.setVisible(true);
        String groupName = event.getItem().getItemProperty("name") != null ? (String) event.getItem().getItemProperty("name").getValue() : "";
        GroupMembersQueryDefinition groupMembersQueryDefinition = new GroupMembersQueryDefinition(groupName, GroupMembersQuery.Mode.SOC);
        LazyQueryContainer groupMembersContainer = new LazyQueryContainer(groupMembersQueryDefinition, new GroupMembersFactory(taskIds));
        employeesTable.setContainerDataSource(groupMembersContainer);
      }
    });

    final VerticalLayout assignButtonLayout = new VerticalLayout();

    employeesTable.addListener(new ItemClickEvent.ItemClickListener() {
      @Override
      public void itemClick(ItemClickEvent event) {
        if (tasks.size() == 0) {
          alreadyGone();
          return;
        }
        final String assigneeLogin = event.getItem().getItemProperty("login").getValue().toString();
        final String assigneeFio = event.getItem().getItemProperty("fio").getValue().toString();

        final Button assignButton = new Button("Назначить");
        assignButton.addListener(new Button.ClickListener() {
          @Override
          public void buttonClick(Button.ClickEvent event) {
            Panel assignedPanel = new Panel("Назначенные этапы");
            assignedPanel.setHeight(250, UNITS_PIXELS);
            Panel failedPanel = new Panel("Неназначенные этапы");
            failedPanel.setHeight(250, UNITS_PIXELS);
            List<List<String>> assignedTasks = new ArrayList<List<String>>();
            List<List<String>> failedTasks = new ArrayList<List<String>>();

            for (Task task : tasks) {
              String taskId = task.getId();
              String bidId = AdminServiceProvider.get().getBidByTask(taskId).getId().toString();
              String taskName = task.getName();
              String procedureName = Flash.flash().getExecutorService().getProcedureNameByDefinitionId(task.getProcessDefinitionId());

              List<String> row = new ArrayList<String>();
              row.add(bidId);
              row.add(taskName);
              row.add(procedureName);

              final String error = ActivitiBean.get().claim(taskId, assigneeLogin, Flash.login(), true);
              if (StringUtils.isEmpty(error)) {
                AdminServiceProvider.get().createLog(Flash.getActor(), "activiti task", taskId, "claim",
                    "Claim to =>" + assigneeLogin, true);
                fireTaskChangedEvent(taskId, SupervisorWorkplace.this);
                assignedTasks.add(row);
              } else {
                AdminServiceProvider.get().createLog(Flash.getActor(), "activiti task", taskId, "claim",
                    "Fail claim to =>" + assigneeLogin, false);
                failedTasks.add(row);
              }
            }

            Table assignedTasksTable = createTaskTable();
            Table failedTasksTable = createTaskTable();

            final Window window = new Window("Исполнитель: " + assigneeLogin + " (" + assigneeFio + ")");
            window.setModal(true);
            window.setWidth(800, UNITS_PIXELS);
            window.setHeight(600, UNITS_PIXELS);
            VerticalLayout layout = new VerticalLayout();
            layout.setMargin(true);
            window.setContent(layout);

            int assignedItemId = 1;
            for (List<String> assignedTask : assignedTasks) {
              String bidId = assignedTask.get(0);
              String taskName = assignedTask.get(1);
              String procedureName = assignedTask.get(2);
              assignedTasksTable.addItem(new Object[] { bidId, taskName, procedureName }, assignedItemId);
              assignedItemId++;
            }
            assignedTasksTable.setPageLength(0);
            assignedPanel.addComponent(assignedTasksTable);

            int failedItemId = 1;
            for (List<String> failedTask : failedTasks) {
              String bidId = failedTask.get(0);
              String taskName = failedTask.get(1);
              String procedureName = failedTask.get(2);
              failedTasksTable.addItem(new Object[] { bidId, taskName, procedureName }, failedItemId);
              failedItemId++;
            }
            failedTasksTable.setPageLength(0);
            failedPanel.addComponent(failedTasksTable);

            if (assignedTasksTable.getItemIds().size() > 0) {
              layout.addComponent(assignedPanel);
            }
            if (failedTasksTable.getItemIds().size() > 0) {
              layout.addComponent(failedPanel);
            }
            window.center();
            event.getButton().getWindow().addWindow(window);
            procedureHistoryPanel.refresh();
            table.refresh();
            bidChanger.change(item1);
          }
        });
        assignButtonLayout.removeAllComponents();
        assignButtonLayout.addComponent(assignButton);
      }
    });

    horizontalLayout.addComponent(orgGroupsTable);
    horizontalLayout.addComponent(empGroupsTable);
    verticalLayout.addComponent(horizontalLayout);
    verticalLayout.addComponent(employeesTable);
    verticalLayout.addComponent(assignButtonLayout);
    return verticalLayout;
  }


  private ItemBuilder<Task> getTaskItemBuilder() {
    return new BatchItemBuilder<Task>() {

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
      public Item createItem(Task task) {
        final ProcessDefinition def = ActivitiBean.get().getProcessDefinition(task.getProcessDefinitionId(), Flash.login());
        final PropertysetItem item = new PropertysetItem();
        Bid bid = Flash.flash().getAdminService().getBidByTask(task.getId());
        bid = (bid != null && bid.getId() != null) ? bid : createNullBidObject();
        item.addItemProperty("id", stringProperty(bid.getId().toString()));
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        item.addItemProperty("dateCreated", stringProperty(formatter.format(bid.getDateCreated())));
        item.addItemProperty("task", stringProperty(task.getName()));
        if (bid.getTag().isEmpty()) {
          item.addItemProperty("procedure", stringProperty(Flash.flash().getExecutorService().getProcedureNameByDefinitionId(def.getId())));
        } else {
          item.addItemProperty("procedure", stringProperty(bid.getTag() + " - " + Flash.flash().getExecutorService().getProcedureNameByDefinitionId(def.getId())));
        }
        item.addItemProperty("declarant", stringProperty(bid.getDeclarant()));
        item.addItemProperty("version", stringProperty(bid.getVersion()));
        item.addItemProperty("status", stringProperty(bid.getStatus().getName()));
        item.addItemProperty("employee", stringProperty(task.getAssignee()));
        item.addItemProperty("taskId", stringProperty(task.getId()));
        item.addItemProperty("priority", stringProperty(String.valueOf(task.getPriority())));
        TaskDates td = AdminServiceProvider.get().getTaskDatesByTaskId(task.getId());
        durations.fillBidAndTask(bid, td, item);
        return item;
      }

      private Bid createNullBidObject() {
        Bid bid = new Bid();
        bid.setId(0L);
        bid.setDeclarant("");
        bid.setVersion("");
        bid.setStatus(BidStatus.Execute);
        bid.setProcedure(new Procedure());
        bid.setTag("");
        return bid;
      }
    };
  }

  private void buildFilterPanel() {
    TaskFilter filterForm = new TaskFilter(controlledTasksQuery, controlledTasksTable, TaskFilter.Mode.Supervisor);
    filterComponent = filterForm;
    filterPanel.addComponent(filterForm);
  }

  private void buildMainLayout() {
    int mainLayoutWidth = 100;
    int filterPanelHeight = 100;
    int filterPanelWidth = 100;
    int infoPanelWidth = 100;
    int infoPanelHeight = 100;
    int listPanelHeight = 100;
    float listPanelRatio = 0.4f;
    float infoPanelRatio = 0.6f;

    setSizeFull();
    setMargin(true);
    VerticalLayout rightLayout = new VerticalLayout();
    filterPanel = new Panel();
    infoPanel = new Panel();
    listPanel = new Panel();
    setFirstComponent(filterPanel);
    setSecondComponent(rightLayout);
    setSplitPosition(35);
    rightLayout.addComponent(infoPanel);
    rightLayout.addComponent(listPanel);
    infoPanel.setSizeFull();
    listPanel.setSizeFull();
    rightLayout.setSpacing(true);
    rightLayout.setSizeFull();
    setHeight(mainLayoutWidth, UNITS_PERCENTAGE);
    filterPanel.setHeight(filterPanelHeight, UNITS_PERCENTAGE);
    filterPanel.setWidth(filterPanelWidth, UNITS_PERCENTAGE);
    infoPanel.setWidth(infoPanelWidth, UNITS_PERCENTAGE);
    infoPanel.setHeight(infoPanelHeight, UNITS_PERCENTAGE);
    listPanel.setHeight(listPanelHeight, UNITS_PERCENTAGE);
    listPanel.setWidth(listPanelHeight, UNITS_PERCENTAGE);
    rightLayout.setExpandRatio(listPanel, listPanelRatio);
    rightLayout.setExpandRatio(infoPanel, infoPanelRatio);
  }

  static private void fireTaskChangedEvent(final String taskId, final Object source) {
    Flash.fire(new TaskChanged(source, taskId));
  }

  public final class DeleteClickListener implements Button.ClickListener {
    private final String taskId;

    public DeleteClickListener(String taskId) {
      this.taskId = taskId;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
      final Window mainWindow = getWindow();
      final Window rejectWindow = new Window();
      rejectWindow.setWidth("38%");
      rejectWindow.center();
      rejectWindow.setCaption("Внимание!");
      final VerticalLayout verticalLayout = new VerticalLayout();
      verticalLayout.setSpacing(true);
      verticalLayout.setMargin(true);
      final Label messageLabel = new Label("Введите причину отклонения заявки");
      messageLabel.setStyleName("h2");
      final TextArea textArea = new TextArea();
      textArea.setSizeFull();
      HorizontalLayout buttons = new HorizontalLayout();
      buttons.setSpacing(true);
      buttons.setSizeFull();
      final Button ok = new Button("Ok");
      Button cancel = new Button("Cancel");

      buttons.addComponent(ok);
      buttons.addComponent(cancel);
      buttons.setExpandRatio(ok, 0.99f);
      verticalLayout.addComponent(messageLabel);
      verticalLayout.addComponent(textArea);
      verticalLayout.addComponent(buttons);
      verticalLayout.setExpandRatio(textArea, 0.99f);
      rejectWindow.setContent(verticalLayout);
      mainWindow.addWindow(rejectWindow);

      Button.ClickListener ok1 = new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent event) {
          ok.setEnabled(false);
          verticalLayout.removeComponent(messageLabel);
          verticalLayout.removeComponent(textArea);
          final byte[] block;
          final String textAreaValue = (String) textArea.getValue();
          if (textAreaValue != null) {
            block = textAreaValue.getBytes();
          } else {
            block = null;
          }
          Label reason = new Label(textAreaValue);
          reason.setCaption("Причина отказа:");
          verticalLayout.addComponent(reason, 0);
          event.getButton().removeListener(this);

          SignApplet signApplet = new SignApplet(new SignAppletListener() {

            @Override
            public void onLoading(SignApplet signApplet) {

            }

            @Override
            public void onNoJcp(SignApplet signApplet) {
              verticalLayout.removeComponent(signApplet);
              ReadOnly field = new ReadOnly("В вашей операционной системе требуется установить КриптоПРО JCP", false);
              verticalLayout.addComponent(field);

            }

            @Override
            public void onCert(SignApplet signApplet, X509Certificate certificate) {
              boolean ok = false;
              String errorClause = null;
              try {
                boolean link = AdminServiceProvider.getBoolProperty(CertificateVerifier.LINK_CERTIFICATE);
                if (link) {
                  byte[] x509 = AdminServiceProvider.get().withEmployee(Flash.login(), new CertificateReader());
                  ok = Arrays.equals(x509, certificate.getEncoded());
                } else {
                  ok = true;
                }
                CertificateVerifyClientProvider.getInstance().verifyCertificate(certificate);
              } catch (CertificateEncodingException e) {
              } catch (CertificateInvalid err) {
                errorClause = err.getMessage();
                ok = false;
              }
              if (ok) {
                signApplet.block(1, 1);
              } else {
                NameParts subject = X509.getSubjectParts(certificate);
                String fieldValue = (errorClause == null) ? "Сертификат " + subject.getShortName() + " отклонён" : errorClause;
                ReadOnly field = new ReadOnly(fieldValue, false);
                verticalLayout.addComponent(field, 0);
              }
            }

            @Override
            public void onBlockAck(SignApplet signApplet, int i) {
              logger().fine("AckBlock:" + i);
              signApplet.chunk(1, 1, block);
            }

            @Override
            public void onChunkAck(SignApplet signApplet, int i) {
              logger().fine("AckChunk:" + i);
            }

            @Override
            public void onSign(SignApplet signApplet, byte[] sign) {
              final int i = signApplet.getBlockAck();
              logger().fine("done block:" + i);
              if (i < 1) {
                signApplet.block(i + 1, 1);
              } else {
                verticalLayout.removeComponent(signApplet);
                NameParts subjectParts = X509.getSubjectParts(signApplet.getCertificate());
                Label field2 = new Label(subjectParts.getShortName());
                field2.setCaption("Подписано сертификатом:");
                verticalLayout.addComponent(field2, 0);
                ok.setEnabled(true);
              }
            }

            private Logger logger() {
              return Logger.getLogger(getClass().getName());
            }
          });
          byte[] x509 = AdminServiceProvider.get().withEmployee(Flash.login(), new CertificateReader());
          if (x509 != null) {
            signApplet.setSignMode(x509);
          } else {
            signApplet.setUnboundSignMode();
          }
          verticalLayout.addComponent(signApplet, 0);

          ok.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
              Task result = Flash.flash().getProcessEngine().getTaskService().createTaskQuery().taskId(taskId).singleResult();
              if (result == null) {
                alreadyGone();
                return;
              }
              ActivitiBean.get().deleteProcessInstance(taskId, textAreaValue);
              AdminServiceProvider.get().createLog(Flash.getActor(), "activiti.task", taskId, "remove",
                  "Отклонить заявку", true);
              fireTaskChangedEvent(taskId, SupervisorWorkplace.this);
              infoChanger.change(infoComponent);
              controlledTasksTable.setValue(null);
              controlledTasksTable.refresh();
              mainWindow.removeWindow(rejectWindow);
            }
          });
        }
      };
      ok.addListener(ok1);

      cancel.addListener(new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent event) {

          controlledTasksTable.refresh();
          mainWindow.removeWindow(rejectWindow);
        }
      });
    }
  }

  private Table createTaskTable() {
    Table table = new Table();
    table.setSelectable(false);
    table.setWidth(100, UNITS_PERCENTAGE);
    table.setHeight(SIZE_UNDEFINED, 0);
    table.addContainerProperty("id", Integer.class, null);
    table.addContainerProperty("task", String.class, null);
    table.addContainerProperty("procedure", String.class, null);
    table.setColumnExpandRatio("id", 0.05f);
    table.setColumnExpandRatio("task", 0.2f);
    table.setColumnExpandRatio("procedure", 0.5f);
    table.setColumnHeaders(new String[]{"Номер", "Этап", "Процедура"});
    table.addStyleName("assigned-tasks");
    return table;
  }
}
