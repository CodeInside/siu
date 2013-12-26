/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.actions;

import com.google.common.collect.Maps;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.activiti.engine.form.FormData;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.form.TaskFormHandler;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import ru.codeinside.gses.webui.components.TaskFormHandlerShowUi;
import ru.codeinside.gses.webui.components.api.Changer;

import java.io.Serializable;

public class TaskFormHandlerShowListener implements ClickListener, Serializable {

  private static final long serialVersionUID = -2128963119155466549L;

  private final Changer changer;
  private final TaskFormHandler taskFormHandler;
  private final FormData formData;

  public TaskFormHandlerShowListener(FormData formData, Changer changer) {
    this.changer = changer;
    this.formData = formData;
    this.taskFormHandler = null;
  }

  //TODO: Заменить на taskFormHandler на идентификатор!
  public TaskFormHandlerShowListener(TaskFormHandler taskFormHandler, Changer changer) {
    this.changer = changer;
    this.formData = null;
    this.taskFormHandler = taskFormHandler;
  }

  @Override
  public void buttonClick(ClickEvent event) {
    TaskFormHandlerShowUi su = taskFormHandler != null ? new TaskFormHandlerShowUi(taskFormHandler, Maps.<String, String>newHashMap()) : new TaskFormHandlerShowUi(formData, Maps.<String, String>newHashMap());
    Button back = new Button("Назад");
    back.addListener(new ClickListener() {
      private static final long serialVersionUID = -2254232821122927192L;
      @Override
      public void buttonClick(ClickEvent event) {
        changer.back();
      }
    });
    VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.setSpacing(true);
    verticalLayout.setMargin(true);
    verticalLayout.addComponent(back);
    Panel p = new Panel();
    p.addComponent(su);
    p.setWidth("100%");
    p.setHeight("100%");
    su.setWidth("100%");
    su.setHeight("100%");
    verticalLayout.addComponent(p);
    verticalLayout.setWidth("1100px");
    verticalLayout.setHeight("600px");
    verticalLayout.setExpandRatio(back, 0.01f);
    verticalLayout.setExpandRatio(p, 0.99f);
    changer.change(verticalLayout);

  }

}
