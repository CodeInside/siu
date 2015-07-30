/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.vaadin.ui.Form;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.form.FormEntry;
import ru.codeinside.gses.webui.wizard.TransitionAction;

import java.util.List;

public final class TrivialFormPage extends AbstractFormSeq implements FormDataSource {

  private final FieldTree fieldTree;
  private final GridForm form;

  public TrivialFormPage(FieldTree fieldTree, GridForm form) {
    this.fieldTree = fieldTree;
    this.form = form;
  }

  @Override
  public String getCaption() {
    return "Заполнение данных";
  }

  @Override
  public List<FormField> getFormFields() {
    return fieldTree.getFormFields();
  }

  @Override
  public Form getForm(FormID formId, FormSeq previous) {
    assert previous == null;
    assert formId != null;
    return form;
  }

  /**
   * Получить действие перехода
   */
  @Override
  public TransitionAction getTransitionAction() {
    return new EmptyAction();
  }

  @Override
  public FormEntry createFormTree() {
    return form.createFormTree();
  }
}
