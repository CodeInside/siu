/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.vaadin.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.VForm;

public class VScrollableForm extends VForm {

  public static final String HIGHLIGHTED = "highlighted";
  public static final String SCROLL_TO = "scrollTo";

  @Override
  public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
    super.updateFromUIDL(uidl, client);
    if (uidl.hasAttribute(SCROLL_TO)) {
      final Paintable paintable = uidl.getPaintableAttribute(SCROLL_TO, client);
      if (paintable != null) {
        final Widget widget = (Widget) paintable;
        final Widget parent = widget.getParent();
        parent.getElement().scrollIntoView();
        if (widget instanceof Focusable) {
          ((Focusable) widget).setFocus(true);
        }
        parent.addStyleName(HIGHLIGHTED);
        new Timer() {
          public void run() {
            parent.removeStyleName(HIGHLIGHTED);
          }
        }.schedule(1333);
      }
    }
  }
}
