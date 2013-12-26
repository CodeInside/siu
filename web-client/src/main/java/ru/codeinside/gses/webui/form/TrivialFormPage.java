/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.vaadin.ui.Form;
import ru.codeinside.gses.activiti.FormID;

import java.util.List;

public final class TrivialFormPage implements FormSeq {

  private final FieldTree fieldTree;
  private final Form form;

  public TrivialFormPage(FieldTree fieldTree, Form form) {
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

}
