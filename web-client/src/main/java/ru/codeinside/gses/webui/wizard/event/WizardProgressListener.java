/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.wizard.event;

import ru.codeinside.gses.webui.wizard.WizardStep;

public interface WizardProgressListener extends WizardCancelledListener, WizardCompletedListener {

  /**
   * Called when the currently active {@link WizardStep} is changed and this
   * {@code WizardProgressListener} is expected to update itself accordingly.
   *
   * @param event {@link WizardStepActivationEvent} object containing details
   *              about the event
   */
  void activeStepChanged(WizardStepActivationEvent event);

  /**
   * Called when collection {@link WizardStep}s is changed (i.e. a step is
   * added or removed) and this {@code WizardProgressListener} is expected to
   * update itself accordingly.
   *
   * @param event {@link WizardStepSetChangedEvent} object containing details
   *              about the event
   */
  void stepSetChanged(WizardStepSetChangedEvent event);

}