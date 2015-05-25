/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.vaadin.ui.Component;
import ru.codeinside.gses.webui.wizard.ResultTransition;
import ru.codeinside.gses.webui.wizard.TransitionAction;
import ru.codeinside.gses.webui.wizard.Wizard;
import ru.codeinside.gses.webui.wizard.WizardStep;

import java.util.ArrayList;
import java.util.List;

final class FormStep implements WizardStep {

  private static final long serialVersionUID = 1L;

  private final FormSeq seq;
  private final FormFlow flow;
  private final Wizard wizard;

  public FormStep(final FormSeq seq, final FormFlow flow, Wizard wizard) {
    this.seq = seq;
    this.flow = flow;
    this.wizard = wizard;
  }

  @Override
  public String getCaption() {
    return seq.getCaption();
  }

  @Override
  public Component getContent() {
    return flow.getFrom(seq);
  }

  @Override
  public boolean onAdvance() {
    return flow.forward(seq, wizard);
  }

  @Override
  public boolean onBack() {
    flow.backward(seq);
    return true;
  }

  @Override
  public TransitionAction getTransitionAction() {
    FormSeq previous = flow.getPrevious();
    List<FormField> formFields = new ArrayList<FormField>();
    if (previous != null) {
      formFields = previous.getFormFields();
    }
    return seq.getTransitionAction(formFields);
  }

  @Override
  public void setResultTransition(ResultTransition resultTransition) {
    seq.setResultTransition(resultTransition);
  }
}
