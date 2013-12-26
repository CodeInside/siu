/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.wizard;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import ru.codeinside.gses.webui.wizard.event.WizardCancelledEvent;
import ru.codeinside.gses.webui.wizard.event.WizardCompletedEvent;
import ru.codeinside.gses.webui.wizard.event.WizardProgressListener;
import ru.codeinside.gses.webui.wizard.event.WizardStepActivationEvent;
import ru.codeinside.gses.webui.wizard.event.WizardStepSetChangedEvent;

import java.util.List;

/**
 * WizardProgressBar displays the progress bar for a {@link Wizard}.
 */
public class WizardProgressBar extends CustomComponent implements WizardProgressListener {
  private static final long serialVersionUID = 1L;

  private final Wizard wizard;
  private Label indicator = new Label();
  private Label caption = new Label();


  public WizardProgressBar(Wizard wizard) {
    this.wizard = wizard;
    setWidth("100%");

    updateState();
    HorizontalLayout v = new HorizontalLayout();
    v.setWidth("100%");
    v.addComponent(indicator);
    v.addComponent(caption);
    v.setSpacing(true);
    v.setExpandRatio(indicator, 1f);
    v.setExpandRatio(caption, 9f);
    setCompositionRoot(v);
  }


  public void activeStepChanged(WizardStepActivationEvent event) {
    updateState();
  }

  public void stepSetChanged(WizardStepSetChangedEvent event) {
    updateState();
  }

  public void wizardCompleted(WizardCompletedEvent event) {
    updateState();
  }

  public void wizardCancelled(WizardCancelledEvent event) {
    // NOP, no need to react to cancellation
  }

  private void updateState() {
    final List<WizardStep> steps = wizard.getSteps();
    int completed = 0;
    WizardStep active = null;
    for (WizardStep step : wizard.getSteps()) {
      if (wizard.isCompleted(step)) {
        completed++;
      }
      if (wizard.isActive(step)) {
        active = step;
      }
    }
    indicator.setVisible(steps.size() > 1);
    indicator.setValue("Шаг " + (completed + 1) + " из " + steps.size());
    if (active == null || steps.size() < 2) {
      caption.setValue(null);
    } else {
      caption.setValue(active.getCaption());
    }
  }

}
