/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.vaadin.data.Property;

final class MandatoryChangeListener implements Property.ValueChangeListener {
  private final MandatoryToggle mandatoryToggle;

  public MandatoryChangeListener(final MandatoryToggle mandatoryToggle) {
    this.mandatoryToggle = mandatoryToggle;
  }

  @Override
  public void valueChange(Property.ValueChangeEvent event) {
    mandatoryToggle.toggle(event.getProperty());
  }
}
