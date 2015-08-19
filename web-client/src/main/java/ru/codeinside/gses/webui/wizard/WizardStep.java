/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.wizard;

import com.vaadin.ui.Component;

import java.io.Serializable;

public interface WizardStep extends Serializable {

  /**
   * Returns the caption of this WizardStep.
   *
   * @return the caption of this WizardStep.
   */
  String getCaption();

  /**
   * Returns the {@link Component} that is to be used as the actual content of
   * this WizardStep.
   *
   * @return the content of this WizardStep as a Component.
   */
  Component getContent();

  /**
   * Returns true if user is allowed to navigate forward past this WizardStep.
   * Typically this method is called when user clicks the Next button of the
   * {@link Wizard}.
   *
   * @return true if user is allowed to navigate past this WizardStep.
   */
  boolean onAdvance();

  /**
   * Returns true if user is allowed to navigate backwards from this
   * WizardStep. Typically this method is called when user clicks the Back
   * button of the {@link Wizard}.
   *
   * @return true if user is allowed to navigate backwards from this
   *         WizardStep.
   */
  boolean onBack();

  /**
   * Получить дейтсвие перехода на этап
   */
  TransitionAction getTransitionAction();

  /**
   * Задать результат перехода
   */
  void setResultTransition(ResultTransition resultTransition);

  /**
   * Выполнить действие при обратном переходе
   */
  void backwardAction();
}
