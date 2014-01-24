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
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.BidStatus;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.adm.database.Procedure;
import ru.codeinside.gses.beans.ActivitiBean;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryContainer;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.actions.ItemBuilder;
import ru.codeinside.gses.webui.components.LayoutChanger;
import ru.codeinside.gses.webui.components.ProcedureHistoryPanel;
import ru.codeinside.gses.webui.components.ShowDiagramComponent;
import ru.codeinside.gses.webui.components.ShowDiagramComponentParameterObject;
import ru.codeinside.gses.webui.components.api.Changer;
import ru.codeinside.gses.webui.data.ControlledTasksQuery;
import ru.codeinside.gses.webui.eventbus.TaskChanged;

import java.text.SimpleDateFormat;
import java.util.Date;

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
  private Component infoComponent;
  private Object filterComponent;

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
    if (source != this && source != filterComponent) {
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
    VerticalLayout layout = new VerticalLayout();
    return layout;
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
    controlledTasksTable.setColumnHeaders(new String[]{"Номер", "Дата подачи заявки", "Этап", "Процедура", "Заявитель", "Версия", "Статус", "Исполнитель"});
    controlledTasksTable.setColumnExpandRatio("id", 0.05f);
    controlledTasksTable.setColumnExpandRatio("dateCreated", 0.15f);
    controlledTasksTable.setColumnExpandRatio("task", 0.2f);
    controlledTasksTable.setColumnExpandRatio("procedure", 0.25f);
    controlledTasksTable.setColumnExpandRatio("declarant", 0.1f);
    controlledTasksTable.setColumnExpandRatio("version", 0.05f);
    controlledTasksTable.setColumnExpandRatio("status", 0.1f);
    controlledTasksTable.setColumnExpandRatio("employee", 0.1f);
    listPanel.addComponent(controlledTasksTable);
    final Button assignButton = new Button("Назначить исполнителя");
    controlledTasksTable.addListener(new Property.ValueChangeListener() {
      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        Table table = (Table) event.getProperty();
        Item item = table.getItem(table.getValue());

        if (item != null && item.getItemProperty("id") != null) {
          final String taskId = item.getItemProperty("taskId").getValue().toString();
          final Component procedureHistoryPanel = new ProcedureHistoryPanel(taskId);
          procedureHistoryPanel.addListener(new Listener() {
            @Override
            public void componentEvent(Event event) {
              HistoricTaskInstance historicTaskInstance = ((ProcedureHistoryPanel.HistoryStepClickedEvent) event).getHistoricTaskInstance();
              Date endDateTime = historicTaskInstance.getEndTime();
              if (endDateTime == null) {
                taskIdToAssign = findTaskByHistoricInstance(historicTaskInstance);
                assignButton.setVisible(true);
              } else {
                assignButton.setVisible(false);
              }
            }
          });
          ((VerticalLayout) item1).removeAllComponents();
          Task task = Flash.flash().getProcessEngine().getTaskService().createTaskQuery().taskId(taskId).singleResult();
          Bid bid = Flash.flash().getAdminService().getBidByTask(taskId);
          String executionId = task.getExecutionId();
          final ProcessDefinition def = ActivitiBean.get().getProcessDefinition(task.getProcessDefinitionId(), Flash.login());
          final ShowDiagramComponentParameterObject param = new ShowDiagramComponentParameterObject();
          param.changer = bidChanger;
          param.processDefinitionId = def.getId();
          param.executionId = executionId;
          param.height = "300px";
          param.width = null;
          param.windowHeader = bid == null ? "" : bid.getProcedure().getName() + " v. " + bid.getVersion();
          Button showDiagram = new Button("Схема");
          showDiagram.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
              ShowDiagramComponent showDiagramComponent = new ShowDiagramComponent(param);
              VerticalLayout layout = new VerticalLayout();
              Button back = new Button("Назад");
              back.addListener(new Button.ClickListener() {
                private static final long serialVersionUID = 4154712522487297925L;

                @Override
                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                  bidChanger.back();
                }
              });
              layout.addComponent(back);
              layout.setSpacing(true);
              layout.addComponent(showDiagramComponent);
              bidChanger.set(layout, "showDiagram");
              bidChanger.change(layout);
            }
          });

          Button deleteBidButton = new Button("Отклонить заявку");
          deleteBidButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
              String confirmMessage = "Вы уверены, что хотите отклонить эту заявку?";
              Window confirmWindow = new ConfirmWindow(confirmMessage);
              confirmWindow.addListener(new Listener() {
                @Override
                public void componentEvent(Event event) {
                  if (event instanceof ConfirmWindow.ConfirmOkEvent) {
                    ActivitiBean.get().deleteProcessInstance(taskId);
                    AdminServiceProvider.get().createLog(Flash.getActor(), "activiti.task", taskId, "remove",
                      "Отклонить заявку", true);
                    fireTaskChangedEvent(taskId, SupervisorWorkplace.this);
                    infoChanger.change(infoComponent);
                    controlledTasksTable.setValue(null);
                    controlledTasksTable.refresh();
                  }
                }
              });
              getWindow().addWindow(confirmWindow);
            }
          });

          HorizontalLayout hl = new HorizontalLayout();
          hl.setSizeFull();
          hl.setSpacing(true);
          hl.addComponent(showDiagram);
          hl.addComponent(deleteBidButton);
          hl.setExpandRatio(showDiagram, 0.99f);
          hl.setExpandRatio(deleteBidButton, 0.01f);

          ((VerticalLayout) item1).addComponent(hl);
          ((VerticalLayout) item1).addComponent(procedureHistoryPanel);
          assignButton.setVisible(false);
          assignButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
              ((Layout) item3).removeAllComponents();
              ((Layout) item3).addComponent(createAssignerToTaskComponent(taskIdToAssign, (ProcedureHistoryPanel) procedureHistoryPanel, controlledTasksTable));
              bidChanger.change(item3);
            }
          });
          ((VerticalLayout) item1).addComponent(assignButton);
          infoChanger.change(bidComponent);
          bidChanger.change(item1);
        } else {
          ((VerticalLayout) item1).removeAllComponents();
        }
      }
    });
  }

  private String findTaskByHistoricInstance(HistoricTaskInstance historicTaskInstance) {
    return Flash.flash().getProcessEngine().getTaskService().createTaskQuery().taskId(historicTaskInstance.getId()).singleResult().getId();
  }

  private Component createAssignerToTaskComponent(final String taskId, final ProcedureHistoryPanel procedureHistoryPanel, final ControlledTasksTable table) {
    final Task task = Flash.flash().getProcessEngine().getTaskService().createTaskQuery().taskId(taskId).singleResult();
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

    GroupsQueryDefinition orgGroupsQueryDefinition = new GroupsQueryDefinition(Flash.login(), GroupsQueryDefinition.Mode.ORG);
    LazyQueryContainer orgGroupsLazyQueryContainer = new LazyQueryContainer(orgGroupsQueryDefinition, new GroupsQueryFactory());
    final Table orgGroupsTable = new Table();
    orgGroupsTable.setSizeFull();
    orgGroupsTable.setHeight("150px");
    orgGroupsTable.setCaption("Доступные группы организаций");
    orgGroupsTable.setSelectable(true);
    orgGroupsTable.addContainerProperty("id", String.class, null);
    orgGroupsTable.addContainerProperty("name", String.class, null);
    orgGroupsTable.setColumnHeaders(new String[]{"Код группы", "Название группы"});
    orgGroupsTable.setContainerDataSource(orgGroupsLazyQueryContainer);

    GroupsQueryDefinition empGroupsQueryDefinition = new GroupsQueryDefinition(Flash.login(), GroupsQueryDefinition.Mode.EMP);
    LazyQueryContainer empGroupsLazyQueryContainer = new LazyQueryContainer(empGroupsQueryDefinition, new GroupsQueryFactory());
    final Table empGroupsTable = new Table();
    empGroupsTable.setCaption("Доступные группы сотрудников");
    empGroupsTable.setSizeFull();
    empGroupsTable.setHeight("150px");
    empGroupsTable.setSelectable(true);
    empGroupsTable.addContainerProperty("id", String.class, null);
    empGroupsTable.addContainerProperty("name", String.class, null);
    empGroupsTable.setColumnHeaders(new String[]{"Код группы", "Название группы"});
    empGroupsTable.setContainerDataSource(empGroupsLazyQueryContainer);

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
        empGroupsTable.select(null);
        employeesTable.setVisible(true);
        String groupName = event.getItem().getItemProperty("id") != null ? (String) event.getItem().getItemProperty("id").getValue() : "";
        GroupMembersQueryDefinition groupMembersQueryDefinition = new GroupMembersQueryDefinition(groupName, GroupMembersQuery.Mode.ORG);
        LazyQueryContainer groupMembersContainer = new LazyQueryContainer(groupMembersQueryDefinition, new GroupMembersFactory(task.getId()));
        employeesTable.setContainerDataSource(groupMembersContainer);
      }
    });

    empGroupsTable.addListener(new ItemClickEvent.ItemClickListener() {
      @Override
      public void itemClick(ItemClickEvent event) {
        orgGroupsTable.select(null);
        employeesTable.setVisible(true);
        String groupName = event.getItem().getItemProperty("id") != null ? (String) event.getItem().getItemProperty("id").getValue() : "";
        GroupMembersQueryDefinition groupMembersQueryDefinition = new GroupMembersQueryDefinition(groupName, GroupMembersQuery.Mode.SOC);
        LazyQueryContainer groupMembersContainer = new LazyQueryContainer(groupMembersQueryDefinition, new GroupMembersFactory(task.getId()));
        employeesTable.setContainerDataSource(groupMembersContainer);
      }
    });

    final VerticalLayout assignButtonLayout = new VerticalLayout();

    employeesTable.addListener(new ItemClickEvent.ItemClickListener() {
      @Override
      public void itemClick(ItemClickEvent event) {
        final String assigneeLogin = event.getItem().getItemProperty("login").getValue().toString();
        final String taskName = task.getName();
        Button assignButton = new Button("Назначить");
        assignButton.addListener(new Button.ClickListener() {
          @Override
          public void buttonClick(Button.ClickEvent event) {
            final String error = ActivitiBean.get().claim(taskId, assigneeLogin, Flash.login(), true);
            if (!StringUtils.isEmpty(error)) {
              getWindow()
                .showNotification("Нельзя назначить " + assigneeLogin + " на исполнение " + taskName, Window.Notification.TYPE_WARNING_MESSAGE);
              AdminServiceProvider.get().createLog(Flash.getActor(), "activiti task", taskId, "claim",
                "Fail claim to =>" + assigneeLogin, false);
            } else {
              getWindow()
                .showNotification("Назначен " + assigneeLogin + " на исполнение " + taskName);
              AdminServiceProvider.get().createLog(Flash.getActor(), "activiti task", taskId, "claim",
                "Claim to =>" + assigneeLogin, true);
              fireTaskChangedEvent(taskId, SupervisorWorkplace.this);
            }
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
    return new ItemBuilder<Task>() {
      @Override
      public Item createItem(Task task) {
        final ProcessDefinition def = ActivitiBean.get().getProcessDefinition(task.getProcessDefinitionId(), Flash.login());
        final PropertysetItem item = new PropertysetItem();
        Bid bid = Flash.flash().getAdminService().getBidByTask(task.getId());
        bid = (bid != null && bid.getId() != null) ? bid : createNullBidObject();
        item.addItemProperty("id", stringProperty(bid.getId().toString()));
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh:mm");
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
        return item;
      }

      private Bid createNullBidObject() {
        Bid bid = new Bid();
        bid.setId(0L);
        bid.setDeclarant("");
        bid.setVersion("");
        bid.setStatus(BidStatus.Execute);
        bid.setProcedure(new Procedure());
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

}
