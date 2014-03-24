/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.google.common.collect.ImmutableList;
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

  public interface CloseListener extends Serializable {
    void onFormClose(TaskForm form);
  }

  public TaskForm(final FormDescription formDesc, CloseListener closeListener) {
    this.closeListener = closeListener;
    flow = formDesc.flow;
    id = formDesc.id;
    formFlow = new FormFlow(id);
    addLabel(formDesc.procedureName, "h1");
    addLabel(formDesc.processDefinition.getDescription());
    if (formDesc.task != null) {
      addLabel(formDesc.task.getName(), "h2");
      addLabel(formDesc.task.getDescription());
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
    setSizeFull();
  }

  private void addLabel(final String raw) {
    addLabel(raw, null);
  }

  private void addLabel(final String raw, final String style) {
    final String text = StringUtils.trimToNull(raw);
    if (text != null) {
      final Label label = new Label(text);
      if (style != null) {
        label.setStyleName(style);
      }
      addComponent(label);
      //setExpandRatio(label, 1);
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
