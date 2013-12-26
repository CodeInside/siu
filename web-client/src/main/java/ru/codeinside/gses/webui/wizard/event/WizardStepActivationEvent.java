/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.wizard.event;

import ru.codeinside.gses.webui.wizard.Wizard;
import ru.codeinside.gses.webui.wizard.WizardStep;

public class WizardStepActivationEvent extends AbstractWizardEvent {

  private final WizardStep activatedStep;

  public WizardStepActivationEvent(Wizard source, WizardStep activatedStep) {
    super(source);
    this.activatedStep = activatedStep;
  }

  /**
   * Returns the {@link WizardStep} that was the activated.
   *
   * @return the activated {@link WizardStep}.
   */
  public WizardStep getActivatedStep() {
    return activatedStep;
  }

}