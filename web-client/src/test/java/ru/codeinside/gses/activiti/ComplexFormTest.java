/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import ru.codeinside.adm.database.DefinitionStatus;
import ru.codeinside.adm.database.Procedure;
import ru.codeinside.adm.database.ProcedureProcessDefinition;
import ru.codeinside.adm.database.ProcedureType;
import ru.codeinside.gses.activiti.forms.BlockNode;
import ru.codeinside.gses.activiti.forms.PropertyCollection;
import ru.codeinside.gses.activiti.forms.PropertyNode;
import ru.codeinside.gses.activiti.forms.PropertyTree;
import ru.codeinside.gses.activiti.forms.PropertyTreeProvider;
import ru.codeinside.gses.service.BidID;
import ru.codeinside.gses.webui.form.FormField;
import ru.codeinside.gses.webui.form.FormSeq;
import ru.codeinside.gses.webui.form.FormSeqBuilder;
import ru.codeinside.gses.webui.form.FullFormData;
import ru.codeinside.gses.webui.form.FullFormDataBuilder;
import ru.codeinside.gses.webui.form.GridForm;
import ru.codeinside.gses.webui.form.TrivialFormPage;

import javax.persistence.EntityManager;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ComplexFormTest extends Assert {

  @Rule
  final public InMemoryEngineRule engine = new InMemoryEngineRule();

  @Test
  public void toggleOnly() {
    deployForm("Form1");
    ProcessDefinition def = engine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey("cform1").singleResult();
    assertNotNull(def);
    StartFormData form = engine.getFormService().getStartFormData(def.getId());
    assertNotNull(form);
    PropertyTree map = ((PropertyTreeProvider) form).getPropertyTree();
    final List<String> expected = Arrays.asList("a", "b", "^a", "~a", "c", "d", "^c", "~c");
    assertEquals(expected, ids(map));
    assertEquals(expected, ImmutableList.copyOf(map.getIndex().keySet()));
  }

  @Test
  public void toggleInsideBlock() {
    deployForm("Form2");
    ProcessDefinition def = engine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey("cform2").singleResult();
    assertNotNull(def);
    StartFormData form = engine.getFormService().getStartFormData(def.getId());
    assertNotNull(form);
    PropertyTree map = ((PropertyTreeProvider) form).getPropertyTree();
    assertEquals(ImmutableList.of("+1"), ids(map));

    final List<String> all = Arrays.asList("+1", "a", "b", "^a", "~a", "c", "d", "^c", "~c");
    assertEquals(all, ImmutableList.copyOf(map.getIndex().keySet()));

    BlockNode block = (BlockNode) map.getNodes()[0];
    assertEquals(ImmutableList.of("a", "b", "^a", "~a", "c", "d", "^c", "~c"), ids(block));
  }

  @Test
  public void badBlockEnd() {
    try {
      deployForm("BadForm1");
      fail("Форма содержит ошибку");
    } catch (ActivitiException e) {
      assertEquals("Заверешение блока без начала -1 | cform/BadForm1.bpmn | line 17 | column 40", e.getMessage().trim());
    }
  }

  @Test
  public void badBlockStart() {
    try {
      deployForm("BadForm2");
      fail("Форма содержит ошибку");
    } catch (ActivitiException e) {
      assertEquals("Начало блока без завершения +1 | cform/BadForm2.bpmn | line 6 | column 40", e.getMessage().trim());
    }
  }

  @Test
  public void emptyBlock() {
    try {
      deployForm("BadForm3");
      fail("Форма содержит ошибку");
    } catch (ActivitiException e) {
      assertEquals("Пустой блок +1 | cform/BadForm3.bpmn | line 6 | column 40", e.getMessage().trim());
    }
  }

  @Test
  public void blockInsideBlock() {
    deployForm("Form3");
    ProcessDefinition def = engine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey("cform3").singleResult();
    assertNotNull(def);

    createProcessDefinition(def);

    StartFormData form = engine.getFormService().getStartFormData(def.getId());
    assertNotNull(form);
    PropertyTree map = ((PropertyTreeProvider) form).getPropertyTree();
    assertEquals(ImmutableList.of("+1"), ids(map));

    final List<String> all = Arrays.asList("+1", "a", "b", "^a", "~a", "c", "d", "^c", "~c", "+2", "comment");
    assertEquals(all, ImmutableList.copyOf(map.getIndex().keySet()));

    BlockNode block1 = (BlockNode) map.getNodes()[0];
    assertEquals(ImmutableList.of("a", "b", "^a", "~a", "c", "d", "^c", "~c", "+2"), ids(block1));

    BlockNode block2 = (BlockNode) block1.getNodes()[8];
    assertEquals(ImmutableList.of("comment"), ids(block2));

    List<FormProperty> formProperties = form.getFormProperties();
    assertEquals(17, formProperties.size());

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
    assertEquals(
      ImmutableSet.of(
        "+1",
        "a_1", "b_1", "c_1", "d_1",
        "+2_1",
        "comment_1_1", "comment_1_2", "comment_1_3",
        "a_2", "b_2", "c_2", "d_2",
        "+2_2",
        "comment_2_1", "comment_2_2", "comment_2_3"),
      ImmutableSet.copyOf(paths)
    );

    GridForm gridForm = (GridForm) page.getForm(formID, null);
    System.out.println(gridForm.fieldTree.dumpTree());

    Map<String, String> values = ImmutableMap.of("1", "1", "a_1", "2");
    Map<String, FileValue> files = ImmutableMap.of();
    BidID bidID = ((ServiceImpl) engine.getFormService())
      .getCommandExecutor()
      .execute(new SubmitStartFormCommand(null, null, def.getId(), values, files, "x", null, null /*em*/));

    assertEquals(1L, engine.getRuntimeService().getVariable(Long.toString(bidID.processId), "1"));
  }


  @Test
  public void manyForms() {
    final boolean DUMP_PIDS = false;
    ProcessDefinition def = engine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey("cform3").singleResult();
    if (def == null) {
      deployForm("Form3");
      def = engine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey("cform3").singleResult();
    }

    createProcessDefinition(def);

    final Map<String, String> values = ImmutableMap.of("1", "1", "a_1", "2");
    final Map<String, FileValue> files = ImmutableMap.of();
    final String id = def.getId();
    final ServiceImpl formService = (ServiceImpl) engine.getFormService();

    int N = 120;
    final ExecutorService executor = Executors.newCachedThreadPool();
    final List<Future<String>> futures = new ArrayList<Future<String>>();
    for (int i = 1; i <= N; i++) {
      final Future<String> future = executor.submit(new Callable<String>() {
        @Override
        public String call() throws Exception {
          BidID bidID = formService.getCommandExecutor().execute(new SubmitStartFormCommand(null, null, id, values, files, "x", null, null /*em*/));
          return Long.toString(bidID.bidId);
        }
      });
      futures.add(future);
    }
    int ok = 0;
    for (final Future<String> future : futures) {
      try {
        String pid = future.get(1, TimeUnit.MINUTES);
        if (DUMP_PIDS) {
          System.out.print(" " + pid);
        }
        ok++;
      } catch (InterruptedException e) {
        e.printStackTrace();
        break;
      } catch (ExecutionException e) {
        e.printStackTrace();
        break;
      } catch (TimeoutException e) {
        e.printStackTrace();
        break;
      }
    }
    executor.shutdownNow();
    assertEquals(N, ok);
  }

  private void createProcessDefinition(ProcessDefinition def) {
    EntityManager em = engine.emf.createEntityManager();
    em.getTransaction().begin();
    if (em.find(ProcedureProcessDefinition.class, def.getId()) == null) {
      Procedure procedure = new Procedure();
      procedure.setName("test");
      procedure.setVersion("1.0");
      procedure.setType(ProcedureType.Administrative);
      em.persist(procedure);

      ProcedureProcessDefinition pd = new ProcedureProcessDefinition();
      pd.setProcessDefinitionKey("cform3");
      pd.setProcessDefinitionId(def.getId());
      pd.setVersion(new Double("1.0"));
      pd.setStatus(DefinitionStatus.Work);
      pd.setProcedure(procedure);
      em.persist(pd);
    }
    em.getTransaction().commit();
    em.close();
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

  @Test
  public void hideBlock() {
    deployForm("pfr-1018-1");
    ProcessDefinition def = engine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey("pfr1018").singleResult();
    assertNotNull(def);
    StartFormData form = engine.getFormService().getStartFormData(def.getId());
    assertNotNull(form);
    PropertyTree map = ((PropertyTreeProvider) form).getPropertyTree();
    assertEquals(ImmutableList.of("result_LastName", "result_FirstName", "result_SecondName", "result_SNILS",
      "result_DateBirth", "result_ResidenceAddress", "result_DateQuery", "result_PresenceData", "+PeriodRegistrationAccount",
      "+InfoPeriodsSeniority", "+BasisInclusionData", "result_NameOrgSZN", "result_DateFormationData", "~trigger"), ids(map));

    final List<String> all = Arrays.asList("result_LastName", "result_FirstName", "result_SecondName", "result_SNILS",
      "result_DateBirth", "result_ResidenceAddress", "result_DateQuery", "result_PresenceData", "+PeriodRegistrationAccount",
      "+AsTheUnemployed", "result_AtuDateStart", "result_AtuDateEnd", "+AsSearchWork", "result_AswDateStart", "result_AswDateEnd",
      "+InfoPeriodsSeniority", "+PeriodReceptionUnemploymentBenefit", "result_PrubDateStart", "result_PrubDateEnd",
      "+PeriodParticipationPaidPublicWorks", "result_PpppwDateStart", "result_PpppwDateEnd", "+PeriodMovingEmployment",
      "result_PmeDateStart", "result_PmeDateEnd", "+BasisInclusionData", "result_NumberPrivateAffair", "result_DatePrivateAffair",
      "result_NameOrgSZN", "result_DateFormationData", "~trigger");
    assertEquals(all, ImmutableList.copyOf(map.getIndex().keySet()));

    BlockNode block1 = (BlockNode) map.getNodes()[8];
    assertEquals(ImmutableList.of("+AsTheUnemployed", "+AsSearchWork"), ids(block1));

    BlockNode block2 = (BlockNode) block1.getNodes()[1];
    assertEquals(ImmutableList.of("result_AswDateStart", "result_AswDateEnd"), ids(block2));

    List<FormProperty> formProperties = form.getFormProperties();
    assertEquals(30, formProperties.size());

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
    assertEquals(
      ImmutableSet.of("result_PresenceData", "+PeriodRegistrationAccount", "+AsTheUnemployed_1", "result_AtuDateStart_1_1",
        "result_AtuDateEnd_1_1", "+AsSearchWork_1", "result_AswDateStart_1_1", "result_AswDateEnd_1_1", "+InfoPeriodsSeniority",
        "+PeriodReceptionUnemploymentBenefit_1", "result_PrubDateStart_1_1", "result_PrubDateEnd_1_1",
        "+PeriodParticipationPaidPublicWorks_1", "result_PpppwDateStart_1_1", "result_PpppwDateEnd_1_1",
        "+PeriodMovingEmployment_1", "result_PmeDateStart_1_1", "result_PmeDateEnd_1_1", "+BasisInclusionData",
        "result_NumberPrivateAffair_1", "result_DatePrivateAffair_1", "result_NameOrgSZN", "result_DateFormationData"),
      ImmutableSet.copyOf(paths)
    );

    GridForm gridForm = (GridForm) page.getForm(formID, null);
    System.out.println(gridForm.fieldTree.dumpTree());

  }
}
