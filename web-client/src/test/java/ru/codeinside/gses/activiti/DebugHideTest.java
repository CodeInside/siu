/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.vaadin.ui.TextField;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.impl.form.FormPropertyImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import ru.codeinside.gses.activiti.forms.*;
import ru.codeinside.gses.activiti.ftarchive.EnumFFT;
import ru.codeinside.gses.webui.components.HackFormPropertyHandler;
import ru.codeinside.gses.webui.form.*;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Ignore
public class DebugHideTest extends Assert {

    @Rule
    final public InMemoryEngineRule engine = new InMemoryEngineRule();

    @Test
    public void hideBlock() {
        deployForm("CloneFieldECPSmev2");
        ProcessDefinition def = engine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey("CloneECPSmev").singleResult();
        assertNotNull(def);
        StartFormData form = engine.getFormService().getStartFormData(def.getId());
        assertNotNull(form);

        final FormID formID = FormID.byProcessDefinitionId(def.getId());
        FullFormDataBuilder builder = new FullFormDataBuilder(formID, null);
        FullFormData fullFormData = builder.build(engine.getProcessEngine());
        FormSeqBuilder seqBuilder = new FormSeqBuilder(fullFormData.decorator);
        ImmutableList<FormSeq> pages = seqBuilder.build();
        TrivialFormPage page = (TrivialFormPage) pages.get(0);

        List<FormField> formFields = page.getFormFields();
        List<String> paths = new ArrayList<String>();
        for (FormField formField : formFields) {
            paths.add(formField.getPropId());
        }

        GridForm gridForm = (GridForm) page.getForm(formID, null);
        System.out.println(gridForm.fieldTree.dumpTree());
        List<FieldTree.Entry> appData_child = gridForm.fieldTree.getEntries("appData_child");
        Class<? extends FieldTree.Entry> aClass = appData_child.get(0).getClass();
        try {
            Field field = aClass.getDeclaredField("field");
            field.setAccessible(true);
            TextField o = (TextField) field.get(appData_child.get(0));
            o.setValue("no");

            gridForm = (GridForm) page.getForm(formID, null);
            System.out.println(gridForm.fieldTree.dumpTree());

            o.setValue("yes");

            gridForm = (GridForm) page.getForm(formID, null);
            System.out.println(gridForm.fieldTree.dumpTree());


        } catch (NoSuchFieldException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    private void deployForm(String form) {
        final String resource = "cform/" + form + ".bpmn";
        final InputStream is = getClass().getClassLoader().getResourceAsStream(resource);
        assertNotNull(resource, is);
        engine.getRepositoryService().createDeployment().addInputStream(resource, is).deploy();
    }

    private List<String> ids(PropertyCollection collection) {
        final PropertyNode[] nodes = collection.getNodes();
        final ArrayList<String> ids = new ArrayList<String>(nodes.length);
        for (final PropertyNode node : nodes) {
            ids.add(node.getId());
        }
        return ids;
    }
}
