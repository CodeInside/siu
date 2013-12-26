/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.vaadin;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import ru.codeinside.gses.vaadin.client.VScrollableForm;

@ClientWidget(VScrollableForm.class)
public class ScrollableForm extends Form {
  private Component scrollTo;

  public void scrollTo(final Component component) {
    scrollTo = component;
    if (component != null) {
      requestRepaint();
    }
  }

  @Override
  public void paintContent(final PaintTarget target) throws PaintException {
    if (scrollTo != null) {
      target.addAttribute("scrollTo", scrollTo);
      scrollTo = null;
    }
    super.paintContent(target);
  }

}
