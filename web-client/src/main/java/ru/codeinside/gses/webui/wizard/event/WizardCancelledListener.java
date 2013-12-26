/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.wizard.event;

import java.io.Serializable;

public interface WizardCancelledListener extends Serializable {

  /**
   * Called when a {@link ru.codeinside.gses.webui.wizard.Wizard} is cancelled by the user.
   *
   * @param event {@link ru.codeinside.gses.webui.wizard.event.WizardCancelledEvent} object containing details about
   *              the event
   */
  void wizardCancelled(WizardCancelledEvent event);
}
