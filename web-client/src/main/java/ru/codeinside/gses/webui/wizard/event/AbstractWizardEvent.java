/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.wizard.event;

import com.vaadin.ui.Component;
import ru.codeinside.gses.webui.wizard.Wizard;

public class AbstractWizardEvent extends Component.Event {

  protected AbstractWizardEvent(Wizard source) {
    super(source);
  }

  /**
   * Returns the {@link Wizard} component that was the source of this event.
   *
   * @return the source {@link Wizard} of this event.
   */
  public Wizard getWizard() {
    return (Wizard) getSource();
  }
}
