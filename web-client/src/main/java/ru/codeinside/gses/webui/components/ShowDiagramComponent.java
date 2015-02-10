/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ShowDiagramComponent extends VerticalLayout {

  private final boolean modal;

  public ShowDiagramComponent(ShowDiagramComponentParameterObject param) {
    this(param, false);
  }

  public ShowDiagramComponent(ShowDiagramComponentParameterObject param, boolean modal) {
    buildLayout(param);
    this.modal = modal;
  }

  private void buildLayout(final ShowDiagramComponentParameterObject param) {
    setSizeFull();
    setSpacing(true);
    final Panel panel = new Panel();
    panel.getContent().setSizeUndefined();
    panel.setCaption(param.caption);
    TaskGraph tg = new TaskGraph(param.processDefinitionId, param.executionId);
    if (param.height != null) {
      tg.setHeight(param.height);
    }
    if (param.width != null) {
      tg.setWidth(param.width);
    }
    tg.setStyleName("scheme-image");
    final TaskGraph bigGraph = new TaskGraph(param.processDefinitionId, param.executionId);
    tg.addListener(new MouseEvents.ClickListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void click(MouseEvents.ClickEvent event) {
        final Window schemeWindow = new Window(param.windowHeader);

        if (!modal)
          getWindow().addWindow(schemeWindow);

        schemeWindow.addComponent(bigGraph);
        schemeWindow.setWidth("90%");
        schemeWindow.setHeight("90%");
        schemeWindow.center();
        schemeWindow.focus();
        schemeWindow.setCloseShortcut(ShortcutAction.KeyCode.ESCAPE, 0);
        bigGraph.addListener(new MouseEvents.ClickListener() {
          @Override
          public void click(MouseEvents.ClickEvent event) {
            System.out.println("MODAL: " + modal);
            if (!modal) {
              getWindow().removeWindow(schemeWindow);
            }
          }
        });
      }
    });
    panel.addComponent(tg);
    addComponent(panel);
  }
}
