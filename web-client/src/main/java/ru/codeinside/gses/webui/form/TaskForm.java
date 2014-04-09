/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.google.common.collect.ImmutableList;
import com.vaadin.event.MouseEvents;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.activiti.engine.ActivitiException;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.activiti.FormID;
import ru.codeinside.gses.service.Functions;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.components.api.WithTaskId;
import ru.codeinside.gses.webui.eventbus.TaskChanged;
import ru.codeinside.gses.webui.wizard.Wizard;
import ru.codeinside.gses.webui.wizard.WizardStep;
import ru.codeinside.gses.webui.wizard.event.WizardCancelledEvent;
import ru.codeinside.gses.webui.wizard.event.WizardCompletedEvent;
import ru.codeinside.gses.webui.wizard.event.WizardProgressListener;
import ru.codeinside.gses.webui.wizard.event.WizardStepActivationEvent;
import ru.codeinside.gses.webui.wizard.event.WizardStepSetChangedEvent;

import javax.ejb.EJBException;
import java.io.Serializable;
import java.util.List;

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

  final Wizard wizard;

  public interface CloseListener extends Serializable {
    void onFormClose(TaskForm form);
  }

  final Embedded editorIcon;
  final HorizontalLayout header;
  final Embedded viewIcon;

  Component mainContent;

  public TaskForm(final FormDescription formDesc, final CloseListener closeListener) {
    this.closeListener = closeListener;
    flow = formDesc.flow;
    id = formDesc.id;
    formFlow = new FormFlow(id);

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
            PrintPanel printPanel = new PrintPanel(dataSource, getApplication(), formDesc.procedureName, id.taskId);
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

    wizard = new Wizard();
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
      final String bid;
      if (id.taskId == null) {
        processed = true;
        String processId = Functions.withEngine(new StartTaskFormSubmiter(id.processDefinitionId, formFlow.getForms()));
        bid = Flash.flash().getDeclarantService().getBidIdByProcessDefinitionId(processId);
      } else {
        bid = null;
        processed = Functions.withEngine(new TaskFormSubmiter(id.taskId, formFlow.getForms()));
      }
      Flash.fire(new TaskChanged(this, id.taskId));
      if (!processed) {
        getWindow().showNotification("Ошибка", "Этап уже обработан или передан другому исполнителю", TYPE_ERROR_MESSAGE);
      }
      if (bid != null) {
        getWindow().showNotification("Заявка " + bid + " подана!", TYPE_WARNING_MESSAGE);
      }
      close();
    } catch (Exception e) {
      processException(e);
    }
  }

  private void processException(Exception e) {
    String reason = "Ошибка";
    Throwable cause = e;
    if (e instanceof EJBException) {
      reason = "Проблема сохранения";
      final Exception causedByException = ((EJBException) e).getCausedByException();
      if (causedByException != null) {
        cause = causedByException;
      }
    }
    if (cause instanceof ActivitiException) {
      reason = "Ошибка в маршруте";
      if (cause.getCause() != null) {
        cause = cause.getCause();
      }
    }
    getWindow().showNotification(reason, cause.getMessage(), TYPE_ERROR_MESSAGE);
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
}
