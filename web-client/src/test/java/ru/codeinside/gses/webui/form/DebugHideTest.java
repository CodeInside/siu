/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;


import com.vaadin.ui.ComboBox;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import ru.codeinside.gses.activiti.InMemoryEngineRule;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.webui.FlashSupport;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DebugHideTest extends Assert {

  @Rule
  final public InMemoryEngineRule engine = new InMemoryEngineRule();

  @Test
  public void hideBlock() throws NoSuchFieldException, IllegalAccessException {
    FlashSupport.setLogin("test");
    deployForm("CloneFieldECPSmev2");
    ProcessDefinition def = engine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey("CloneECPSmev").singleResult();
    assertNotNull(def);

    final FormID formID = FormID.byProcessDefinitionId(def.getId());
    FormDescriptionBuilder builder = new FormDescriptionBuilder(formID, null, new DataAccumulator());
    FormDescription formDescription = builder.apply(engine.getProcessEngine());
    assertNotNull(formDescription);
    TrivialFormPage page = (TrivialFormPage) formDescription.flow.get(0);

    List<FormField> formFields = page.getFormFields();
    List<String> paths = new ArrayList<String>();
    for (FormField formField : formFields) {
      paths.add(formField.getPropId());
    }

    GridForm gridForm = (GridForm) page.getForm(formID, null);
    String dump1 = gridForm.fieldTree.dumpTree();
    assertTrue(dump1, dump1.contains(" +parents BLOCK 1\n"));

    List<FieldTree.Entry> appData_child = gridForm.fieldTree.getEntries("appData_child");
    ComboBox o = (ComboBox) appData_child.get(0).field;
    o.setValue("no");

    String dump2 = gridForm.fieldTree.dumpTree();
    assertTrue(dump2, dump2.contains(" +parents BLOCK 1 hidden\n"));

    o.setValue("yes");

    String dump3 = gridForm.fieldTree.dumpTree();
    assertTrue(dump3, dump3.contains(" +parents BLOCK 1\n"));
  }

  private void deployForm(String form) {
    final String resource = "cform/" + form + ".bpmn";
    final InputStream is = getClass().getClassLoader().getResourceAsStream(resource);
    assertNotNull(resource, is);
    engine.getRepositoryService().createDeployment().addInputStream(resource, is).deploy();
  }
}
