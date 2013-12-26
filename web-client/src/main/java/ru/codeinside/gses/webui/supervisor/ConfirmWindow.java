/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.supervisor;

import com.vaadin.ui.*;

import static com.vaadin.ui.Button.*;

public class ConfirmWindow extends Window {
  public ConfirmWindow(String confirmMessage) {
    setCaption("Внимание!");
    setWidth("30%");
    VerticalLayout vl = new VerticalLayout();
    vl.setSizeFull();
    vl.setSpacing(true);
    Label messageLabel = new Label(confirmMessage);
    messageLabel.setStyleName("h1");
    vl.addComponent(messageLabel);
    HorizontalLayout hl = new HorizontalLayout();
    hl.setSizeFull();
    hl.setSpacing(true);
    Button okButton = new Button("Да");
    Button noButton = new Button("Нет");
    okButton.addListener(new ClickListener(){
      @Override
      public void buttonClick(Button.ClickEvent event) {
        fireEvent(new ConfirmOkEvent(event.getComponent()));
        close();
      }
    });
    noButton.addListener(new ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        close();
      }
    });
    hl.addComponent(okButton);
    hl.addComponent(noButton);
    hl.setExpandRatio(okButton, 0.99f);
    vl.addComponent(hl);
    addComponent(vl);
    center();
  }

  public class ConfirmOkEvent extends Event {
    public ConfirmOkEvent(Component source) {
      super(source);
    }
  }
}
