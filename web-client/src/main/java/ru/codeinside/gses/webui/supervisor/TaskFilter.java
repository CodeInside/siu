/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.supervisor;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import ru.codeinside.adm.database.ProcedureType;
import ru.codeinside.gses.activiti.ftarchive.validators.LongValidator;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryContainer;
import ru.codeinside.gses.vaadin.NumericField;
import ru.codeinside.gses.webui.DeclarantTypeChanged;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.components.TasksQueryFilter;
import ru.codeinside.gses.webui.components.api.IRefresh;
import ru.codeinside.gses.webui.declarant.ProcedureQueryDefinition;
import ru.codeinside.gses.webui.declarant.ProcedureQueryFactory;
import ru.codeinside.gses.webui.declarant.ServiceQueryDefinition;
import ru.codeinside.gses.webui.declarant.ServiceQueryFactory;
import ru.codeinside.gses.webui.eventbus.TaskChanged;
import ru.codeinside.gses.webui.executor.DeclarantTypeQueryDefinition;
import ru.codeinside.gses.webui.executor.DeclarantTypeQueryFactory;
import ru.codeinside.gses.webui.executor.ProcessTaskQueryDefinition;
import ru.codeinside.gses.webui.executor.ProcessTaskQueryFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static ru.codeinside.gses.service.Fn.trimToNull;

public class TaskFilter extends Form {

  private static final long serialVersionUID = 1L;

  public enum Mode {
    Executor, Supervisor
  }

  private static final String ADMINISTRATIVE_PROCEDURE = "Административная процедура";
  private static final String INTERDEPARTAMENTAL_PROCEDURE = "Межведомственная процедура";
  private final TasksQueryFilter executionTasksQuery;
  private final IRefresh executionTasksTable;
  private final TasksQueryFilter controlledTasksQuery;
  private final IRefresh controlledTasksTable;
  private Mode mode;

  public TaskFilter(TasksQueryFilter controlledTasksQuery, IRefresh controlledTasksTable, Mode mode) {
    this.controlledTasksQuery = controlledTasksQuery;
    this.controlledTasksTable = controlledTasksTable;
    this.executionTasksQuery = null;
    this.executionTasksTable = null;
    this.mode = mode;
    buildMainLayout();
  }

  public TaskFilter(TasksQueryFilter controlledTasksQuery, IRefresh controlledTasksTable,
                    TasksQueryFilter executionTasksQuery, IRefresh executionTasksTable, Mode mode) {
    this.controlledTasksQuery = controlledTasksQuery;
    this.controlledTasksTable = controlledTasksTable;
    this.executionTasksQuery = executionTasksQuery;
    this.executionTasksTable = executionTasksTable;
    this.mode = mode;
    buildMainLayout();
  }

  private void buildMainLayout() {
    final ServiceQueryDefinition administrativeServiceQueryDefinition = new ServiceQueryDefinition(ProcedureType.Administrative);
    final LazyQueryContainer administrativeServiceQueryContainer = new LazyQueryContainer(administrativeServiceQueryDefinition, new ServiceQueryFactory(true));
    final ProcedureQueryDefinition administrativeProcedureQueryDefinition = new ProcedureQueryDefinition(ProcedureType.Administrative);
    final LazyQueryContainer administrativeProcedureQueryContainer = new LazyQueryContainer(administrativeProcedureQueryDefinition, new ProcedureQueryFactory(Flash.login(), true));
    final ProcedureQueryDefinition interdepartmentalProcedureQueryDefinition = new ProcedureQueryDefinition(ProcedureType.Interdepartmental);
    final LazyQueryContainer interdepartmentalProcedureQueryContainer = new LazyQueryContainer(interdepartmentalProcedureQueryDefinition, new ProcedureQueryFactory(Flash.login(), true));
    final ServiceQueryDefinition interdepartmentalServiceQueryDefinition = new ServiceQueryDefinition(ProcedureType.Interdepartmental);
    final LazyQueryContainer interdepartmentalQueryContainer = new LazyQueryContainer(interdepartmentalServiceQueryDefinition, new ServiceQueryFactory(true));
    final GroupsQueryDefinition orgGroupsQueryDefinition = new GroupsQueryDefinition(Flash.login(), GroupsQueryDefinition.Mode.ORG);
    final LazyQueryContainer orgGroupsQueryContainer = new LazyQueryContainer(orgGroupsQueryDefinition, new GroupsQueryFactory());
    final GroupsQueryDefinition empGroupsQueryDefinition = new GroupsQueryDefinition(Flash.login(), GroupsQueryDefinition.Mode.EMP);
    final LazyQueryContainer empGroupsQueryContainer = new LazyQueryContainer(empGroupsQueryDefinition, new GroupsQueryFactory());

    final String comboBoxWidth = "250px";

    PopupDateField popupFromDate = getFromDateField();
    PopupDateField popupToDate = getToDateField();
    ComboBox procedureTypeBox = getProcedureTypeField(comboBoxWidth);
    final ComboBox serviceBox = getServiceField(administrativeServiceQueryContainer, comboBoxWidth);
    final ComboBox procedureBox = getProcedureField(administrativeProcedureQueryContainer, comboBoxWidth);
    final ComboBox taskBox = getTaskField(comboBoxWidth);
    final ComboBox declarantTypeBox = getDeclarantTypeField(comboBoxWidth);
    CheckBox overdueBox = getOverdueField(comboBoxWidth);
    TextField numberTextField = getBidNumberField(comboBoxWidth);
    TextField requesterTextField = getRequesterLoginField(comboBoxWidth);
    final ListSelect orgGroups = getControlledOrgGroups(orgGroupsQueryContainer, comboBoxWidth);
    final ListSelect empGroups = getControlledEmployeeGroups(empGroupsQueryContainer, comboBoxWidth);
    orgGroups.setVisible(mode == Mode.Supervisor);
    empGroups.setVisible(mode == Mode.Supervisor);

    addField("fromDate", popupFromDate);
    addField("toDate", popupToDate);
    addField("procedureType", procedureTypeBox);
    addField("serviceId", serviceBox);
    addField("procedureName", procedureBox);
    addField("taskName", taskBox);
    addField("declarantType", declarantTypeBox);
    addField("bidId", numberTextField);
    addField("requester", requesterTextField);
    addField("overdue", overdueBox);
    addField("controlledOrgGroups", orgGroups);
    addField("controlledEmpGroups", empGroups);

    /*корректировка содержимого комбобоксов по событиям*/
    procedureTypeBox.addListener(new Property.ValueChangeListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        final Property prop = event.getProperty();
        if (prop.getValue() == null || prop.getValue().equals(ADMINISTRATIVE_PROCEDURE)) {
          serviceBox.setContainerDataSource(administrativeServiceQueryContainer);
          procedureBox.setContainerDataSource(administrativeProcedureQueryContainer);
          serviceBox.select(null);
          procedureBox.select(null);
          taskBox.select(null);
          declarantTypeBox.select(null);
        } else {
          serviceBox.setContainerDataSource(interdepartmentalQueryContainer);
          procedureBox.setContainerDataSource(interdepartmentalProcedureQueryContainer);
          procedureBox.select(null);
          procedureBox.select(null);
          taskBox.select(null);
          declarantTypeBox.select(null);
        }
      }
    });

    serviceBox.addListener(new Property.ValueChangeListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        final Property prop = event.getProperty();
        if (prop.getValue() == null) {
          administrativeProcedureQueryDefinition.setServiceId(-1);
        } else {
          administrativeProcedureQueryDefinition.setServiceId((Long) administrativeServiceQueryContainer.getItem(prop.getValue()).getItemProperty("id").getValue());
        }
        procedureBox.select(null);
        administrativeProcedureQueryContainer.refresh();
      }
    });

    procedureBox.addListener(new Property.ValueChangeListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        final Property prop = event.getProperty();
        if (prop.getValue() == null) {
          taskBox.setEnabled(false);
        } else {
          taskBox.setEnabled(true);
          String id = procedureBox.getItem(prop.getValue()).getItemProperty("id").toString();
          final ProcessTaskQueryDefinition tSQ = new ProcessTaskQueryDefinition(Long.parseLong(id));
          final LazyQueryContainer tSC = new LazyQueryContainer(tSQ, new ProcessTaskQueryFactory());
          taskBox.setContainerDataSource(tSC);
          taskBox.setItemCaptionPropertyId("name");
        }
      }
    });


    Button actionFilter = new Button("Фильтровать");
    actionFilter.addShortcutListener(new Button.ClickShortcut(actionFilter, ShortcutAction.KeyCode.ENTER));
    actionFilter.addListener(new Button.ClickListener() {
      private static final long serialVersionUID = 7234774931167363716L;

      @Override
      public void buttonClick(Button.ClickEvent event) {

        Object overdue = getField("overdue").getValue();
        controlledTasksQuery.setOverdue(Boolean.TRUE.equals(overdue));

        Object fromDate = getField("fromDate").getValue();
        controlledTasksQuery.setFromDate(fromDate != null ? (Date) fromDate : null);

        Object toDate = getField("toDate").getValue();
        controlledTasksQuery.setToDate(toDate != null ? (Date) toDate : null);

        Object requester = getField("requester").getValue();
        controlledTasksQuery.setRequester(requester != null ? requester.toString() : null);

        Object bidId = getField("bidId").getValue();
        String bidIdString = null;
        if (bidId != null) {
          bidIdString = bidId.toString();
          try {
            Long.parseLong(bidIdString);
          } catch (NumberFormatException e) {
            bidIdString = null;
          }
        }
        controlledTasksQuery.setBidId(bidIdString);

        Object type = getField("procedureType").getValue();
        controlledTasksQuery.setProcedureType(type != null ? type.toString() : null);

        Object serviceIdValue = getField("serviceId").getValue();
        Item serviceItem = serviceIdValue != null ? serviceBox.getItem(serviceIdValue) : null;
        String serviceId = null;
        if (serviceItem != null && serviceItem.getItemProperty("id") != null) {
          serviceId = serviceItem.getItemProperty("id").toString();
        }
        controlledTasksQuery.setServiceId(serviceId);

        Object procedureValue = getField("procedureName").getValue();
        Item procedureItem = procedureValue != null ? procedureBox.getItem(procedureValue) : null;
        String procedureId = null;
        if (procedureItem != null && procedureItem.getItemProperty("id") != null) {
          procedureId = procedureItem.getItemProperty("id").toString();
        }
        controlledTasksQuery.setProcedureId(procedureId);

        Object taskValue = getField("taskName").getValue();
        Item taskValueItem = taskValue != null ? taskBox.getItem(taskValue) : null;
        String taskKey = null;
        if (taskValue != null) {
          taskKey = taskValueItem.getItemProperty("taskDefKey") != null ? taskValueItem.getItemProperty("taskDefKey").toString() : null;
        }
        controlledTasksQuery.setTaskKey(taskKey);

        Object declarantTypeValue = getField("declarantType").getValue();
        Item declarantTypeValueItem = declarantTypeValue != null ? declarantTypeBox.getItem(declarantTypeValue) : null;
        String name = null;
        String val = null;
        if (declarantTypeValue != null) {
          name = declarantTypeValueItem.getItemProperty("name") != null ? declarantTypeValueItem.getItemProperty("name").toString() : null;
          val = declarantTypeValueItem.getItemProperty("value") != null ? declarantTypeValueItem.getItemProperty("value").toString() : null;
        }
        controlledTasksQuery.setDeclarantTypeName(name);
        controlledTasksQuery.setDeclarantTypeValue(val);

        Collection controlledOrgGroups = (Collection) getField("controlledOrgGroups").getValue();
        List<String> selectedOrgGroups = new ArrayList<String>();
        if (controlledOrgGroups.size() > 0) {
          Iterator iterator = controlledOrgGroups.iterator();
          while (iterator.hasNext()) {
            selectedOrgGroups.add(orgGroups.getItem(iterator.next()).getItemProperty("id").toString());
          }
        }
        if (mode == Mode.Supervisor) {
          controlledTasksQuery.setControlledOrgGroups(controlledOrgGroups.size() != 0 ? selectedOrgGroups : null);
        }

        Collection controlledEmpGroups = (Collection) getField("controlledEmpGroups").getValue();
        List<String> selectedEmpGroups = new ArrayList<String>();
        if (controlledEmpGroups.size() > 0) {
          Iterator iterator = controlledEmpGroups.iterator();
          while (iterator.hasNext()) {
            selectedEmpGroups.add(empGroups.getItem(iterator.next()).getItemProperty("id").toString());
          }
        }
        if (mode == Mode.Supervisor) {
          controlledTasksQuery.setControlledEmpGroups(controlledEmpGroups.size() != 0 ? selectedEmpGroups : null);
        }
        controlledTasksTable.refresh();

        if (executionTasksQuery != null) {
          executionTasksQuery.setFromDate(fromDate != null ? (Date) fromDate : null);
          executionTasksQuery.setToDate(toDate != null ? (Date) toDate : null);
          executionTasksQuery.setRequester(requester != null ? requester.toString() : null);
          executionTasksQuery.setBidId(bidIdString);
          executionTasksQuery.setProcedureType(type != null ? type.toString() : null);
          executionTasksQuery.setServiceId(serviceId);
          executionTasksQuery.setProcedureId(procedureId);
          executionTasksQuery.setTaskKey(taskKey);
          executionTasksQuery.setDeclarantTypeName(name);
          executionTasksQuery.setDeclarantTypeValue(val);
          executionTasksQuery.setOverdue(Boolean.TRUE.equals(overdue));
        }
        if (executionTasksTable != null) {
          executionTasksTable.refresh();
        }
      }
    });

    Button resetFilter = new Button("Очистить фильтр");
    resetFilter.addListener(new Button.ClickListener() {

      private static final long serialVersionUID = 7234774931167363717L;

      @Override
      public void buttonClick(Button.ClickEvent event) {

        Field overdue = getField("overdue");
        overdue.setValue(false);
        controlledTasksQuery.setOverdue(false);

        Field fromDate = getField("fromDate");
        fromDate.setValue(null);
        controlledTasksQuery.setFromDate(null);


        Field toDate = getField("toDate");
        toDate.setValue(null);
        controlledTasksQuery.setToDate(null);

        getField("requester").setValue("");
        controlledTasksQuery.setRequester(null);

        getField("bidId").setValue("");
        controlledTasksQuery.setBidId(null);

        getField("procedureType").setValue(null);
        controlledTasksQuery.setProcedureType(null);

        getField("serviceId").setValue(null);
        controlledTasksQuery.setServiceId(null);

        getField("procedureName").setValue(null);
        controlledTasksQuery.setProcedureId(null);

        getField("taskName").setValue(null);
        controlledTasksQuery.setTaskKey(null);

        getField("declarantType").setValue(null);
        controlledTasksQuery.setDeclarantTypeName(null);
        controlledTasksQuery.setDeclarantTypeValue(null);

        getField("controlledOrgGroups").setValue(null);
        if (mode == Mode.Supervisor) {
          controlledTasksQuery.setControlledOrgGroups(null);
        }

        getField("controlledEmpGroups").setValue(null);
        if (mode == Mode.Supervisor) {
          controlledTasksQuery.setControlledEmpGroups(null);
        }
        controlledTasksTable.refresh();
        if (executionTasksQuery != null) {
          executionTasksQuery.setFromDate(null);
          executionTasksQuery.setToDate(null);
          executionTasksQuery.setRequester(null);
          executionTasksQuery.setBidId(null);
          executionTasksQuery.setProcedureType(null);
          executionTasksQuery.setServiceId(null);
          executionTasksQuery.setProcedureId(null);
          executionTasksQuery.setTaskKey(null);
          executionTasksQuery.setDeclarantTypeName(null);
          executionTasksQuery.setDeclarantTypeValue(null);
          executionTasksQuery.setOverdue(false);
        }
        if (executionTasksTable != null) {
          executionTasksTable.refresh();
        }
      }
    });

    final HorizontalLayout footer = (HorizontalLayout) getFooter();
    footer.setSpacing(true);
    Button refreshLists = new Button("Обновить списки заявок", new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        Flash.fire(new TaskChanged(TaskFilter.this, ""));
      }
    });
    refreshLists.setImmediate(true);
    footer.addComponent(refreshLists);
    footer.addComponent(actionFilter);
    footer.addComponent(resetFilter);
  }

  private ListSelect getControlledOrgGroups(LazyQueryContainer groupsQueryContainer, String comboBoxWidth) {
    ListSelect cb = new ListSelect("Группы организаций");
    cb.setMultiSelect(true);
    cb.setContainerDataSource(groupsQueryContainer);
    cb.setNullSelectionAllowed(true);
    cb.setItemCaptionPropertyId("name");
    cb.setWidth(comboBoxWidth);
    return cb;
  }

  private ListSelect getControlledEmployeeGroups(LazyQueryContainer empGroupsQueryContainer, String comboBoxWidth) {
    ListSelect cb = new ListSelect("Группы сотрудников");
    cb.setMultiSelect(true);
    cb.setNullSelectionAllowed(true);
    cb.setContainerDataSource(empGroupsQueryContainer);
    cb.setItemCaptionPropertyId("name");
    cb.setWidth(comboBoxWidth);
    return cb;
  }

  private TextField getRequesterLoginField(String comboBoxWidth) {
    TextField requesterTextField = new TextField("Логин заявителя");
    requesterTextField.setWidth(comboBoxWidth);
    return requesterTextField;
  }

  private TextField getBidNumberField(String comboBoxWidth) {
    NumericField numberTextField = new NumericField("Номер заявки");
    numberTextField.setWidth(comboBoxWidth);
    numberTextField.addValidator(new LongValidator("Номер заявки должен быть числом"));
    return numberTextField;
  }

  private CheckBox getOverdueField(String comboBoxWidth) {
    CheckBox overdueBox = new CheckBox("Только просроченные заявки");
    overdueBox.setWidth(comboBoxWidth);
    return overdueBox;
  }

  private ComboBox getDeclarantTypeField(String comboBoxWidth) {
    final ComboBox declarantTypeBox = new ComboBox("Категория заявителей");
    LazyQueryContainer container = new LazyQueryContainer(new DeclarantTypeQueryDefinition(), new DeclarantTypeQueryFactory());
    declarantTypeBox.setWidth(comboBoxWidth);
    declarantTypeBox.setContainerDataSource(container);
    declarantTypeBox.setItemCaptionPropertyId("value");

    Flash.bind(DeclarantTypeChanged.class, container, "refresh");

    //TODO добавить unbind
    //Flash.unbind(DeclarantTypeChanged.class, container, "refresh");
    return declarantTypeBox;
  }

  private ComboBox getTaskField(String comboBoxWidth) {
    final ComboBox taskBox = new ComboBox("Этап");
    taskBox.setWidth(comboBoxWidth);
    taskBox.setRequired(false);
    taskBox.setImmediate(true);
    taskBox.setEnabled(false);
    return taskBox;
  }

  private ComboBox getProcedureField(LazyQueryContainer administrativeProcedureQueryContainer, String comboBoxWidth) {
    final ComboBox procedureBox = new ComboBox("Процедура");
    procedureBox.setContainerDataSource(administrativeProcedureQueryContainer);
    procedureBox.setWidth(comboBoxWidth);
    procedureBox.setRequired(false);
    procedureBox.setItemCaptionPropertyId("name");
    procedureBox.setImmediate(true);
    return procedureBox;
  }

  private ComboBox getServiceField(LazyQueryContainer administrativeServiceQueryContainer, String comboBoxWidth) {
    final ComboBox serviceBox = new ComboBox("Услуга");
    serviceBox.setContainerDataSource(administrativeServiceQueryContainer);
    serviceBox.setWidth(comboBoxWidth);
    serviceBox.setRequired(false);
    serviceBox.setItemCaptionPropertyId("name");
    serviceBox.setImmediate(true);
    return serviceBox;
  }

  private ComboBox getProcedureTypeField(String comboBoxWidth) {
    ComboBox procedureTypeBox = new ComboBox("Тип процедуры");
    procedureTypeBox.setWidth(comboBoxWidth);
    procedureTypeBox.addItem(ADMINISTRATIVE_PROCEDURE);
    procedureTypeBox.addItem(INTERDEPARTAMENTAL_PROCEDURE);
    procedureTypeBox.setNullSelectionAllowed(false);
    procedureTypeBox.setImmediate(true);
    return procedureTypeBox;
  }

  private PopupDateField getToDateField() {
    return new PopupDateFieldWithDef("Дата подачи до", false);
  }

  private PopupDateField getFromDateField() {
    return new PopupDateFieldWithDef("Дата подачи с", true);
  }

  static public final Locale RUSSIAN_LOCALE = new Locale("ru", "RU");


  final static class PopupDateFieldWithDef extends PopupDateField {
    String str = null;
    final boolean startDate;

    public PopupDateFieldWithDef(String caption, boolean startDate) {
      super(caption);
      this.startDate = startDate;
      setLocale(RUSSIAN_LOCALE);
      setShowISOWeekNumbers(false);
      setImmediate(true);
      setDateFormat("dd.MM.yyyy HH:mm:ss");
      setParseErrorMessage("Введите правильную дату и время");
      if (false) {
        // форсировать перерисовку/отправку свойств клиенту когда нет значения
        addListener(new FieldEvents.BlurListener() {
          @Override
          public void blur(FieldEvents.BlurEvent event) {
            if (getValue() == null) {
              event.getComponent().requestRepaint();
            }
          }
        });
      }
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
      super.paintContent(target);
      if (getValue() == null) {
        if (str == null) {
          target.addAttribute("parsable", false);
        }
        target.addVariable(this, "msec", startDate ? 0 : 999);
        target.addVariable(this, "sec", startDate ? 0 : 59);
        target.addVariable(this, "min", startDate ? 0 : 59);
        target.addVariable(this, "hour", startDate ? 0 : 23);
        //if (str == null) {
        final Calendar calendar = Calendar.getInstance(RUSSIAN_LOCALE);
        target.addVariable(this, "day", calendar.get(Calendar.DAY_OF_MONTH));
        target.addVariable(this, "month", calendar.get(Calendar.MONTH) + 1);
        target.addVariable(this, "year", calendar.get(Calendar.YEAR));
        //}
      }
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
      super.changeVariables(source, variables);
      str = trimToNull((String) variables.get("dateString"));
    }

  }
}
