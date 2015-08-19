/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vaadin.event.MouseEvents;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.adm.database.Organization;
import ru.codeinside.gses.API;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.form.FormEntry;
import ru.codeinside.gses.service.BidID;
import ru.codeinside.gses.service.F2;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.service.Functions;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.components.api.WithTaskId;
import ru.codeinside.gses.webui.eventbus.TaskChanged;
import ru.codeinside.gses.webui.utils.Components;
import ru.codeinside.gses.webui.wizard.Wizard;
import ru.codeinside.gses.webui.wizard.WizardStep;
import ru.codeinside.gses.webui.wizard.event.WizardCancelledEvent;
import ru.codeinside.gses.webui.wizard.event.WizardCompletedEvent;
import ru.codeinside.gses.webui.wizard.event.WizardProgressListener;
import ru.codeinside.gses.webui.wizard.event.WizardStepActivationEvent;
import ru.codeinside.gses.webui.wizard.event.WizardStepSetChangedEvent;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static com.vaadin.ui.Window.Notification.TYPE_ERROR_MESSAGE;
import static com.vaadin.ui.Window.Notification.TYPE_WARNING_MESSAGE;

final public class TaskForm extends VerticalLayout implements WithTaskId {

  private static final long serialVersionUID = 1L;
  final static ThemeResource EDITOR_ICON = new ThemeResource("../custom/icon/linedpaperpencil32.png");
  final static ThemeResource VIEW_ICON = new ThemeResource("../custom/icon/paper32.png");

  private final FormID id;
  private final ImmutableList<FormSeq> flow;
  private final FormFlow formFlow;
  private final CloseListener closeListener;
  private final DataAccumulator accumulator;

  final Wizard wizard;

  private Logger log = Logger.getLogger(TaskForm.class.getName());

  public interface CloseListener extends Serializable {
    void onFormClose(TaskForm form);
  }

  final Embedded editorIcon;
  final HorizontalLayout header;
  final Embedded viewIcon;

  Component mainContent;

  public TaskForm(final FormDescription formDesc, final CloseListener closeListener, final DataAccumulator accumulator) {
    this.closeListener = closeListener;
    flow = formDesc.flow;
    id = formDesc.id;
    formFlow = new FormFlow(id);
    this.accumulator = accumulator;

    header = new HorizontalLayout();
    header.setWidth(100, UNITS_PERCENTAGE);
    header.setSpacing(true);
    addComponent(header);

    editorIcon = new Embedded(null, EDITOR_ICON);
    editorIcon.setDescription("Редактирование");
    editorIcon.setStyleName("icon-inactive");
    editorIcon.setHeight(32, UNITS_PIXELS);
    editorIcon.setWidth(32, UNITS_PIXELS);
    editorIcon.setImmediate(true);
    header.addComponent(editorIcon);
    header.setComponentAlignment(editorIcon, Alignment.BOTTOM_CENTER);


    if (flow.get(0) instanceof FormDataSource) {
      viewIcon = new Embedded(null, VIEW_ICON);
      viewIcon.setDescription("Предварительный просмотр");
      viewIcon.setStyleName("icon-inactive");
      viewIcon.setHeight(32, UNITS_PIXELS);
      viewIcon.setWidth(32, UNITS_PIXELS);
      viewIcon.setImmediate(true);
      header.addComponent(viewIcon);
      header.setComponentAlignment(viewIcon, Alignment.BOTTOM_CENTER);

      editorIcon.addListener(new MouseEvents.ClickListener() {
        @Override
        public void click(MouseEvents.ClickEvent event) {
          if (mainContent != wizard) {
            TaskForm.this.replaceComponent(mainContent, wizard);
            TaskForm.this.setExpandRatio(wizard, 1f);
            mainContent = wizard;
            editorIcon.setStyleName("icon-inactive");
            viewIcon.setStyleName("icon-active");
          }
        }
      });

      viewIcon.addListener(new MouseEvents.ClickListener() {
        @Override
        public void click(MouseEvents.ClickEvent event) {
          if (mainContent == wizard && flow.get(0) instanceof FormDataSource) {
            FormDataSource dataSource = (FormDataSource) flow.get(0);

            Map<String, String> response = null;
            String serviceLocation = AdminServiceProvider.get().getSystemProperty(API.PRINT_TEMPLATES_SERVICELOCATION);
            String json = buildJsonStringWithFormData(dataSource);

            boolean responseContainsTypeKey = false;
            if (serviceLocation != null &&
                !serviceLocation.isEmpty() &&
                json != null &&
                !json.isEmpty()) {

              response = callPrintService(serviceLocation, json);
              if (response != null && response.containsKey("type")) {
                responseContainsTypeKey = true;
                log.info("PRINT SERVICE. Response type: " + response.get("type"));
              } else {
                log.info("PRINT SERVICE. Response type: null");
              }
            }

            PrintPanel printPanel;
            if (responseContainsTypeKey &&
                response.get("type").equals("success") &&
                response.get("content") != null &&
                !response.get("content").isEmpty()) {
              printPanel = new PrintPanel(response.get("content"), getApplication());
            } else {
              printPanel = new PrintPanel(dataSource, getApplication(), formDesc.procedureName, id.taskId);
            }

            TaskForm.this.replaceComponent(wizard, printPanel);
            TaskForm.this.setExpandRatio(printPanel, 1f);
            mainContent = printPanel;

            editorIcon.setStyleName("icon-active");
            viewIcon.setStyleName("icon-inactive");
            editorIcon.setVisible(true);
          }
        }
      });
      viewIcon.setStyleName("icon-active");
      viewIcon.setVisible(true);
    } else {
      viewIcon = null;
    }

    VerticalLayout labels = new VerticalLayout();
    labels.setSpacing(true);
    header.addComponent(labels);
    header.setComponentAlignment(labels, Alignment.MIDDLE_LEFT);
    header.setExpandRatio(labels, 1f);
    addLabel(labels, formDesc.procedureName, "h1");
    addLabel(labels, formDesc.processDefinition.getDescription(), null);
    if (formDesc.task != null) {
      addLabel(labels, formDesc.task.getName(), "h2");
      addLabel(labels, formDesc.task.getDescription(), null);
    }

    wizard = new Wizard(accumulator);
    wizard.setImmediate(true);
    wizard.addListener(new ProgressActions());
    for (FormSeq seq : flow) {
      wizard.addStep(new FormStep(seq, formFlow, wizard));
    }
    mainContent = wizard;
    addComponent(wizard);
    setExpandRatio(wizard, 1f);
    if (formDesc.task != null) {
      setMargin(true);
    } else {
      // у заявителя уже есть обрамление
      setMargin(true, false, false, false);
    }
    setImmediate(true);
    setSpacing(true);
    setSizeFull();
    setExpandRatio(wizard, 1f);
  }


  private void addLabel(ComponentContainer container, String text, String style) {
    text = StringUtils.trimToNull(text);
    if (text != null) {
      Label label = new Label(text);
      if (style != null) {
        label.setStyleName(style);
      }
      container.addComponent(label);
    }
  }

  @Override
  public String getTaskId() {
    return id.taskId;
  }

  void complete() {
    try {
      final boolean processed;
      final BidID bidID;
      if (id.taskId == null) {
        processed = true;
        bidID = Functions.withEngine(new StartTaskFormSubmitter(id.processDefinitionId, formFlow.getForms(), accumulator));
      } else {
        bidID = null;
        processed = Functions.withEngine(new TaskFormSubmitter(id.taskId, formFlow.getForms(), accumulator));
      }
      Flash.fire(new TaskChanged(this, id.taskId));
      if (!processed) {
        getWindow().showNotification("Ошибка", "Этап уже обработан или передан другому исполнителю", TYPE_ERROR_MESSAGE);
      }
      if (bidID != null) {
        getWindow().showNotification("Заявка " + bidID.bidId + " подана!", TYPE_WARNING_MESSAGE);
      }
      close();
    } catch (Exception e) {
      Components.showException(getWindow(), e);
    }
  }


  void close() {
    if (closeListener != null) {
      closeListener.onFormClose(this);
    }
  }

  final class ProgressActions implements WizardProgressListener {

    @Override
    public void activeStepChanged(WizardStepActivationEvent event) {
      if (viewIcon != null) {
        List<WizardStep> steps = event.getWizard().getSteps();
        WizardStep step = event.getActivatedStep();
        viewIcon.setVisible(steps.indexOf(step) == 0);
      }
    }

    @Override
    public void stepSetChanged(WizardStepSetChangedEvent unusedEvent) {

    }

    @Override
    public void wizardCancelled(WizardCancelledEvent unusedEvent) {
      close();
    }

    @Override
    public void wizardCompleted(WizardCompletedEvent unusedEvent) {
      complete();
    }
  }

  private String buildJsonStringWithFormData(FormDataSource dataSource) {
    String taskId = getTaskId();

    String procedureCode = getProcedureCode(taskId);
    String organizationId = getOrganizationId();
    String userTaskId = null;

    if (taskId != null && !taskId.isEmpty()) {
      userTaskId = getUserTaskId(taskId);
    }

    List<Map<String, String>> elements = getElements(dataSource);

    Map<String, Object> data = new LinkedHashMap<String, Object>();
    data.put("procedure_id", procedureCode);
    data.put("organization_id", organizationId);
    data.put("task_id", userTaskId);
    data.put("elements", elements);

    Gson gson = new Gson();

    return gson.toJson(data);
  }

  private List<Map<String, String>> getElements(FormDataSource dataSource) {
    List<Map<String, String>> result = new LinkedList<Map<String, String>>();

    FormEntry formEntry = dataSource.createFormTree();
    FormEntry[] children = formEntry.children;

    for (FormEntry childEntry : children) {
      Map<String, String> element = new LinkedHashMap<String, String>();
      element.put("name", childEntry.id);
      element.put("value", (childEntry.value == null || "value".equals(childEntry.value)) ? "" : childEntry.value);
      result.add(element);
    }

    return result;
  }

  private Map<String, String> callPrintService(String serviceLocation, String json) {
    HttpURLConnection connection = null;

    Map<String, String> result = null;
    try {
      URL url = new URL(serviceLocation);
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);
      connection.setConnectTimeout(5000);
      connection.setReadTimeout(5000);

      String postParameters = "data=" + json;
      OutputStream os = new BufferedOutputStream(connection.getOutputStream());
      os.write(postParameters.getBytes("UTF-8"));
      os.flush();
      os.close();

      BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

      log.info("PRINT SERVICE. Response code: " + connection.getResponseCode());

      String line;
      StringBuffer stringBuffer = new StringBuffer();
      while ((line = input.readLine()) != null) {
        stringBuffer.append(line);
      }
      input.close();

      result = new Gson().fromJson(stringBuffer.toString(), new TypeToken<Map<String, String>>() {
      }.getType());
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }

    return result;
  }

  private String getProcedureCode(String taskId) {
    if (taskId != null && !taskId.isEmpty()) {
      return String.valueOf(AdminServiceProvider.get().getBidByTask(taskId).getProcedure().getRegisterCode());
    } else if (id.processDefinitionId != null && !id.processDefinitionId.isEmpty()){
      return String.valueOf(AdminServiceProvider.get().getProcedureCodeByProcessDefinitionId(id.processDefinitionId));
    } else {
      return null;
    }
  }

  private String getOrganizationId() {
    Employee user = AdminServiceProvider.get().findEmployeeByLogin(Flash.login());
    Organization organization = user.getOrganization();
    if (organization != null) {
      return organization.getId().toString();
    } else {
      return null;
    }
  }

  private String getUserTaskId(String taskId) {
    return Fn.withEngine(new GetUserTaskId(), Flash.login(), taskId);
  }

  final private static class GetUserTaskId implements F2<String, String, String> {
    @Override
    public String apply(ProcessEngine engine, String login, String taskId) {

      CommandExecutor commandExecutor = ((ServiceImpl) engine.getFormService()).getCommandExecutor();
      return String.valueOf(commandExecutor.execute(new GetUserTaskIdCommand(taskId)));
    }

    final private static class GetUserTaskIdCommand implements Command {
      private final String taskId;

      GetUserTaskIdCommand(String taskId) {
        this.taskId = taskId;
      }

      @Override
      public Object execute(CommandContext commandContext) {
        String processInstanceId = AdminServiceProvider.get().getBidByTask(taskId).getProcessInstanceId();


        ExecutionEntity execution = Context.getCommandContext()
            .getExecutionManager()
            .findExecutionById(processInstanceId);

        return execution.getActivity().getId();
      }
    }
  }
}
