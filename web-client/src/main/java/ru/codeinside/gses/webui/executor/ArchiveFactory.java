/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.executor;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.vaadin.data.Item;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.persistence.entity.HistoricVariableUpdateEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.apache.commons.lang.StringUtils;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.ui.FilterDecorator_;
import ru.codeinside.adm.ui.LazyLoadingContainer2;
import ru.codeinside.gses.API;
import ru.codeinside.gses.activiti.forms.CustomTaskFormHandler;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.activiti.forms.GetStartArchiveFormCmd;
import ru.codeinside.gses.activiti.forms.GetTaskArchiveFormCmd;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.api.values.FormValue;
import ru.codeinside.gses.activiti.forms.api.values.PropertyValue;
import ru.codeinside.gses.service.F1;
import ru.codeinside.gses.service.F2;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.vaadin.JsonFormIntegration;
import ru.codeinside.gses.webui.ActivitiApp;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.components.api.IRefresh;
import ru.codeinside.gses.webui.data.OwnHistoryBeanQuery;
import ru.codeinside.gses.webui.form.EFormBuilder;
import ru.codeinside.gses.webui.form.FieldTree;
import ru.codeinside.gses.webui.form.GridForm;
import ru.codeinside.gses.webui.form.JsonForm;
import ru.codeinside.gses.webui.utils.Components;
import ru.codeinside.gses.webui.wizard.ExpandRequired;

import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

//TODO: првести рефакторинг этой мешанины кода
final public class ArchiveFactory implements Serializable {

  private static final long serialVersionUID = -3060552897820352216L;

  final static private SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");

  public static Component create() {
    final FilterTable bidsTable = createBidsTable();
    final FilterTable phaseTable = createPhaseTable();
    bidsTable.addGeneratedColumn("id", new IdColumnGenerator(bidsTable, phaseTable));

    final HorizontalLayout bidsLayout = new HorizontalLayout();
    bidsLayout.setSizeFull();
    bidsLayout.setMargin(true);
    bidsLayout.setSpacing(true);
    bidsLayout.addComponent(bidsTable);

    final VerticalLayout candidateLayout = new VerticalLayout();
    candidateLayout.setSizeFull();
    candidateLayout.setMargin(true);
    candidateLayout.addComponent(phaseTable);

    final TasksSplitter vSplitter = new TasksSplitter(new TableRefresh(bidsTable), new TableRefresh(phaseTable));
    vSplitter.setSizeFull();
    vSplitter.setFirstComponent(bidsLayout);
    vSplitter.setSecondComponent(candidateLayout);
    vSplitter.setSplitPosition(57);

    return vSplitter;
  }

  static FilterTable createPhaseTable() {
    final FilterTable candidate = Components.createFilterTable("100%", "100%");

    candidate.setCaption("Этапы");
    candidate.addContainerProperty("name", String.class, null);
    candidate.addContainerProperty("date", String.class, null);
    candidate.addContainerProperty("declarant", String.class, null);
    candidate.addContainerProperty("form", Component.class, null);

    candidate.setVisibleColumns(new Object[]{"name", "date", "declarant", "form"});
    candidate.setColumnHeaders(new String[]{"Название", "Даты исполнения", "Исполнитель", ""});
    candidate.setSelectable(false);
    candidate.setFilterBarVisible(true);
    candidate.setFilterDecorator(new FilterDecorator_());
    candidate.setColumnExpandRatio("name", 1.5f);
    candidate.setColumnExpandRatio("date", 2f);
    candidate.setColumnExpandRatio("declarant", 1.5f);
    candidate.setColumnExpandRatio("form", 1f);
    return candidate;
  }

  static FilterTable createBidsTable() {

    LazyLoadingContainer2 container = new LazyLoadingContainer2(new OwnHistoryBeanQuery());
    container.addContainerProperty("id", Long.class, null);
    container.addContainerProperty("procedure.name", String.class, null);
    container.addContainerProperty("dateCreated", Date.class, null);
    container.addContainerProperty("dateFinished", Date.class, null);
    FilterTable bidTable = new FilterTable();
    bidTable.setFilterBarVisible(true);
    bidTable.setFilterDecorator(new FilterDecorator_());
    bidTable.setCaption("Заявки");
    bidTable.setImmediate(true);
    bidTable.setContainerDataSource(container);
    bidTable.setSizeFull();
    bidTable.setColumnHeaders(new String[]{"№ Заявки", "Процедура", "Дата заявления", "Дата завершения"});
    bidTable.setSelectable(false);

    bidTable.setColumnExpandRatio("id", 0.1f);
    bidTable.setColumnExpandRatio("procedure.name", 0.5f);
    bidTable.setColumnExpandRatio("dateCreated", 0.2f);
    bidTable.setColumnExpandRatio("dateFinished", 0.2f);

    return bidTable;
  }

  static List<ActivityImpl> getActivityList(final Bid bid) {
    return Fn.withEngine(new GetActivities(), Flash.login(), bid);
  }

  static boolean canShowUi(final ActivityImpl activity) {
    return
      isPropertyType(activity, "startEvent") ||
        (isPropertyType(activity, "userTask") && activity.getActivityBehavior() instanceof UserTaskActivityBehavior);
  }

  static String getExecutionDate(HistoricActivityInstance cur) {
    String executionDate;
    if (cur != null) {
      executionDate = formatter.format(cur.getStartTime());
      if (cur.getEndTime() != null) {
        executionDate += " - " + formatter.format(cur.getEndTime());
      }
    } else {
      executionDate = "";
    }
    return executionDate;
  }

  static Map<String, String> getHistoryValues(Bid bid, Date toDate) {
    Map<String, String> values = Maps.newHashMap();
    for (HistoricVariableUpdateEntity hu : getLastVariableValues(bid, toDate)) {
      String value = null;
      Object rawValue = hu.getValue();
      if (rawValue == null) {
        value = "";
      } else if (hu.getVariableType().getTypeName().equals("date")) {
        value = new SimpleDateFormat("dd/MM/yyyy").format(rawValue);
      } else {
        if (rawValue instanceof byte[]) {
          byte[] bytes = (byte[]) rawValue;
          try {
            value = new String(bytes, "UTF-8");
          } catch (UnsupportedEncodingException e) {
            value = toBase64HumanString(bytes);
          }
        }
        if (value == null) {
          value = rawValue.toString();
        }
      }
      values.put(hu.getName(), value);
    }
    return values;
  }

  private static Collection<HistoricVariableUpdateEntity> getLastVariableValues(Bid bid, Date toDate) {
    Map<String, HistoricVariableUpdateEntity> lastChanges = Maps.newHashMap();
    {
      List<HistoricDetail> details = Fn.withEngine(new GetHistoricVariableUpdatesOrderByTimeDesc(), bid);
      for (HistoricDetail detail : details) {
        HistoricVariableUpdateEntity update = (HistoricVariableUpdateEntity) detail;
        if (update.getTime().compareTo(toDate) <= 0) {
          String variableName = update.getVariableName();
          if (!lastChanges.containsKey(variableName)) {
            lastChanges.put(variableName, update);
          }
        }
      }
    }
    return lastChanges.values();
  }

  public static String toBase64HumanString(byte[] bytes) {
    return Joiner.on('\n').join(
      Splitter.fixedLength(64).split(DatatypeConverter.printBase64Binary(bytes))
    );
  }

  static ActivityImpl activityInstance(final List<ActivityImpl> acs, HistoricActivityInstance history) {
    ActivityImpl result = null;
    for (ActivityImpl ac : acs) {
      if (ac.getId().equals(history.getActivityId())) {
        result = ac;
        break;
      }
    }
    return result;
  }

  static boolean isPropertyType(ActivityImpl ac, String typeName) {
    return typeName.equals(ac.getProperty("type").toString());
  }

  static String getAssignee(Bid bid, ActivityImpl ac, HistoricActivityInstance cur) {
    String assignee = "";
    if (isPropertyType(ac, "startEvent")) {
      assignee = bid.getDeclarant();
    } else if (cur != null) {
      assignee = cur.getAssignee();
    }
    return assignee;
  }

  static Window createFormWindow(Component su, ActivityImpl ac, String id) {
    VerticalLayout content = new VerticalLayout();
    content.setMargin(true);
    content.addComponent(su);
    Window window = new Window();
    window.setWidth(800 + 50, Sizeable.UNITS_PIXELS);
    window.setHeight(600 + 100, Sizeable.UNITS_PIXELS);
    window.center();
    window.setContent(content);
    window.setCaption("Форма этапа " + ac.getId() + " по заявке #" + id);
    if (su instanceof JsonFormIntegration || su instanceof ExpandRequired) {
      content.setSizeFull();
      content.setExpandRatio(su, 1f);
    } else {
      window.setResizable(false); // нет подстройки под размер
    }
    return window;
  }

  static Component createForm(Bid bid, ActivityImpl activity, ProcessEngine engine, ActivitiApp app, Date toDate) {

    //TODO: в контексте Activity через commandExecutor и defaultExpression.getValue(new VariableScope())

    Map<String, String> historyValues = getHistoryValues(bid, toDate);
    FormValue formValue;
    CommandExecutor commandExecutor = ((ServiceImpl) engine.getRuntimeService()).getCommandExecutor();
    String processDefinitionId = bid.getProcedureProcessDefinition().getProcessDefinitionId();
    FormID formID = FormID.byProcessDefinitionId(processDefinitionId);
    if (isPropertyType(activity, "startEvent")) {
      formValue = commandExecutor.execute(new GetStartArchiveFormCmd(processDefinitionId, historyValues));
    } else if (isPropertyType(activity, "userTask") && activity.getActivityBehavior() instanceof UserTaskActivityBehavior) {
      CustomTaskFormHandler taskFormHandler = (CustomTaskFormHandler) ((UserTaskActivityBehavior) activity.getActivityBehavior()).getTaskDefinition().getTaskFormHandler();
      formValue = commandExecutor.execute(new GetTaskArchiveFormCmd(processDefinitionId, taskFormHandler, historyValues));

    } else {
      formValue = null;
    }
    if (formValue == null) {
      return null;
    }

    if (StringUtils.isNotEmpty(formValue.getFormDefinition().getFormKey())) {
      return new EFormBuilder(formValue, formID, true).getForm(null, null);
    }

    ImmutableMap<String, PropertyNode> propertyNodes = formValue.getFormDefinition().getIndex();
    if (propertyNodes.containsKey(API.JSON_FORM)) {
      String templateRef = null;
      String value = null;
      for (PropertyValue<?> propertyValue : formValue.getPropertyValues()) {
        if (propertyValue.getId().equals(API.JSON_FORM)) {
          templateRef = (String) propertyValue.getValue();
        } else {
          try {
            value = new String((byte[]) propertyValue.getValue(), "UTF-8");
          } catch (UnsupportedEncodingException e) {
            Logger.getAnonymousLogger().info("can't decode model!");
          }
          break;
        }
      }
      if (value != null) {
        return JsonForm.createIntegration(formID, app, templateRef, value, true);
      }
    }

    FieldTree fieldTree = new FieldTree(formID);
    fieldTree.create(formValue);
    GridForm form = new GridForm(formID, fieldTree);
    form.setImmediate(true);
    return form;
  }

  final public static class TableRefresh implements IRefresh, Serializable {
    private static final long serialVersionUID = -3060552897820352219L;
    private final FilterTable[] tables;

    public TableRefresh(FilterTable... tables) {
      this.tables = tables;
    }

    @Override
    public void refresh() {
      for (FilterTable t : tables) {
        t.removeAllItems();
        t.refreshRowCache();
      }
    }
  }

  final private static class IdColumnGenerator implements CustomTable.ColumnGenerator {
    private static final long serialVersionUID = 1L;
    private final FilterTable bidsTable;
    private final FilterTable phaseTable;

    public IdColumnGenerator(final FilterTable bidsTable, final FilterTable phaseTable) {
      this.bidsTable = bidsTable;
      this.phaseTable = phaseTable;
    }

    public Component generateCell(CustomTable source, Object itemId, Object columnId) {
      final Item item = bidsTable.getContainerDataSource().getItem(itemId);
      final String id = item.getItemProperty("id").getValue().toString();
      Button button = new Button(id, new IdClickListener(id, phaseTable));
      button.setStyleName(Reindeer.BUTTON_LINK);
      return button;
    }

  }

  final static class IdClickListener implements Button.ClickListener {
    private static final long serialVersionUID = 1L;

    private final String bidId;
    private final FilterTable phaseTable;

    public IdClickListener(String bidId, FilterTable phaseTable) {
      this.bidId = bidId;
      this.phaseTable = phaseTable;
    }

    @Override
    public void buttonClick(ClickEvent event) {
      phaseTable.setCaption("Этапы для заявки #" + bidId);
      final Bid bid = AdminServiceProvider.get().getBid(bidId);

      phaseTable.removeAllItems();

      int index = 0;
      List<HistoricActivityInstance> histories = Fn.withEngine(new GetHistoricInstances(), bid);
      List<ActivityImpl> activities = getActivityList(bid);
      for (final HistoricActivityInstance cur : histories) {
        ActivityImpl activity = activityInstance(activities, cur);
        if (activity == null) {
          continue;
        }
        String assignee = getAssignee(bid, activity, cur);
        Button button = null;
        if (Flash.login().equals(assignee)) {
          if (canShowUi(activity)) {
            button = new Button("просмотреть");
            Date time = cur.getEndTime() == null ? cur.getStartTime() : cur.getEndTime();
            button.addListener(new ShowClickListener(activity.getId(), bidId, time));
          }
        }
        String actName = activity.getProperty("name") != null ? activity.getProperty("name").toString() : "Без названия";
        String executionDate = getExecutionDate(cur);
        phaseTable.addItem(new Object[]{actName, executionDate, assignee, button}, index++);
      }
    }
  }


  // ----------------- persistence -----------------

  public final static class ShowClickListener implements Button.ClickListener {
    private final String activityId;
    private final String bidId;
    private final Date toDate;

    public ShowClickListener(String activityId, String bidId, Date toDate) {
      this.activityId = activityId;
      this.bidId = bidId;
      this.toDate = toDate;
    }

    @Override
    public void buttonClick(ClickEvent event) {
      ActivitiApp app = (ActivitiApp) event.getButton().getApplication();
      Window win = Fn.withEngine(new CreateUi(), new Object[]{bidId, activityId, toDate}, app);
      event.getButton().getWindow().addWindow(win);
      AdminServiceProvider.get().createLog(Flash.getActor(), "Activity", activityId, "View in archive", "", true);
    }
  }

  final private static class GetHistoricInstances implements F1<List<HistoricActivityInstance>, Bid> {
    public List<HistoricActivityInstance> apply(final ProcessEngine engine, final Bid bid) {
      return engine.getHistoryService().createHistoricActivityInstanceQuery().processInstanceId(bid.getProcessInstanceId()).list();
    }
  }

  final private static class GetActivities implements F2<List<ActivityImpl>, String, Bid> {
    @Override
    public List<ActivityImpl> apply(ProcessEngine engine, String login, Bid bid) {
      RepositoryServiceImpl impl = (RepositoryServiceImpl) engine.getRepositoryService();
      String processDefinitionId = bid.getProcedureProcessDefinition().getProcessDefinitionId();
      engine.getIdentityService().setAuthenticatedUserId(login);
      ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) impl.getDeployedProcessDefinition(processDefinitionId);
      return processDefinition.getActivities();
    }
  }

  final static class CreateUi implements F2<Window, Object[], ActivitiApp> {
    @Override
    public Window apply(ProcessEngine engine, Object[] arg, ActivitiApp app) {
      String bidId = (String) arg[0];
      String activityId = (String) arg[1];
      Date toDate = (Date) arg[2];
      Bid bid = AdminServiceProvider.get().getBid(bidId);
      for (ActivityImpl activity : Fn.withEngine(new GetActivities(), Flash.login(), bid)) {
        if (activityId.equals(activity.getId())) {
          Component form = createForm(bid, activity, engine, app, toDate);
          return createFormWindow(form, activity, bidId);
        }
      }
      throw new IllegalStateException();
    }
  }

  final private static class GetHistoricVariableUpdatesOrderByTimeDesc implements F1<List<HistoricDetail>, Bid> {
    @Override
    public List<HistoricDetail> apply(ProcessEngine engine, Bid bid) {
      String processInstanceId = bid.getProcessInstanceId().trim();
      return engine
        .getHistoryService()
        .createHistoricDetailQuery()
        .processInstanceId(processInstanceId)
        .variableUpdates()
        .orderByTime()
        .desc()
        .list();
    }
  }

}


