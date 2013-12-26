/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.google.common.collect.ImmutableList;
import org.activiti.engine.form.FormProperty;
import ru.codeinside.gses.API;
import ru.codeinside.gses.activiti.FormDecorator;

import java.util.HashMap;
import java.util.Map;

final public class FormSeqBuilder {

  final private FormDecorator decorator;

  public FormSeqBuilder(final FormDecorator decorator) {
    this.decorator = decorator;
  }

  public ImmutableList<FormSeq> build() {
    ImmutableList.Builder<FormSeq> steps = ImmutableList.builder();
    steps.add(buildFormPage());
    Map<String, FormProperty> postProcessed = decorator.gePostProcessed();
    for (FormProperty p : postProcessed.values()) {
      steps.add(decorator.getPostProcessor(p).createFormSeq(p));
    }
    return steps.build();
  }

  FormSeq buildFormPage() {
    if (decorator.variableFormData.formData.getFormKey() != null) {
      return new EFormBuilder(decorator);
    }
    Map<String, FormProperty> properties = decorator.getGeneral();
    if (properties.containsKey(API.JSON_FORM)) {
      return new JsonFormBuilder(properties);
    }

    FieldTree fieldTree = new FieldTree();
    fieldTree.create(decorator);
    GridForm form = new GridForm(decorator.toSimple(), fieldTree);
    form.setImmediate(true);
    return new TrivialFormPage(fieldTree, form);
  }
}
