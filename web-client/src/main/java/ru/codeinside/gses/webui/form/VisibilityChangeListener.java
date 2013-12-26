/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.vaadin.data.Property;

final class VisibilityChangeListener implements Property.ValueChangeListener {
  private final VisibilityToggle toggle;
  private final GridForm form;

  public VisibilityChangeListener(final GridForm form, final VisibilityToggle toggle) {
    this.toggle = toggle;
    this.form = form;
  }

  @Override
  public void valueChange(Property.ValueChangeEvent event) {
    toggle.toggle(form, event.getProperty());
  }
}
