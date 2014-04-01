/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.google.common.collect.ImmutableList;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
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
import ru.codeinside.gses.webui.wizard.event.WizardCancelledEvent;
import ru.codeinside.gses.webui.wizard.event.WizardCancelledListener;
import ru.codeinside.gses.webui.wizard.event.WizardCompletedEvent;
import ru.codeinside.gses.webui.wizard.event.WizardCompletedListener;

import javax.ejb.EJBException;
import java.io.Serializable;

import static com.vaadin.ui.Window.Notification.TYPE_ERROR_MESSAGE;
import static com.vaadin.ui.Window.Notification.TYPE_WARNING_MESSAGE;

final public class TaskForm extends VerticalLayout implements WithTaskId, WizardCancelledListener, WizardCompletedListener {

  private static final long serialVersionUID = 1L;

  private final FormID id;
  private final ImmutableList<FormSeq> flow;
  private final FormFlow formFlow;
  private final CloseListener closeListener;


  final static ThemeResource EDITOR_ICON = new ThemeResource("../custom/icon/linedpaperpencil32.png");
  final static ThemeResource VIEW_ICON = new ThemeResource("../custom/icon/linedpaper32.png");
  final static ThemeResource PRINT_ICON = new ThemeResource("../custom/icon/printer32.png");

  public interface CloseListener extends Serializable {
    void onFormClose(TaskForm form);
  }

  public TaskForm(final FormDescription formDesc, CloseListener closeListener) {
    this.closeListener = closeListener;
    flow = formDesc.flow;
    id = formDesc.id;
    formFlow = new FormFlow(id);

    HorizontalLayout header = new HorizontalLayout();
    header.setWidth(100, UNITS_PERCENTAGE);
    header.setSpacing(true);
    addComponent(header);

    Embedded editor = new Embedded(null, EDITOR_ICON);
    editor.setHeight(32, UNITS_PIXELS);
    editor.setWidth(32, UNITS_PIXELS);
    editor.setEnabled(false);
    header.addComponent(editor);
    header.setComponentAlignment(editor, Alignment.BOTTOM_CENTER);


    Embedded view = new Embedded(null, VIEW_ICON);
    view.setHeight(32, UNITS_PIXELS);
    view.setWidth(32, UNITS_PIXELS);
    view.setEnabled(false);
    header.addComponent(view);
    header.setComponentAlignment(view, Alignment.BOTTOM_CENTER);


    Embedded print = new Embedded(null, PRINT_ICON);
    print.setHeight(32, UNITS_PIXELS);
    print.setWidth(32, UNITS_PIXELS);
    print.setEnabled(false);
    header.addComponent(print);
    header.setComponentAlignment(print, Alignment.BOTTOM_CENTER);

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

    final Wizard wizard = new Wizard();
    wizard.setImmediate(true);
    wizard.addCancelledListener(this);
    wizard.addCompletedListener(this);
    for (FormSeq seq : flow) {
      wizard.addStep(new FormStep(seq, formFlow, wizard));
    }
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

  @Override
  public void wizardCancelled(WizardCancelledEvent event) {
    close();
  }


  @Override
  public void wizardCompleted(WizardCompletedEvent event) {
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

}
