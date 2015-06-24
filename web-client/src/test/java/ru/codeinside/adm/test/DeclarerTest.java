/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.ProcessDefinition;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.Procedure;
import ru.codeinside.adm.database.ProcedureProcessDefinition;
import ru.codeinside.adm.database.ProcedureType;
import ru.codeinside.adm.database.SmevChain;
import ru.codeinside.adm.fixtures.FxDefinition;
import ru.codeinside.gses.beans.ActivitiDeclarerContext;
import ru.codeinside.gses.manager.ManagerService;
import ru.codeinside.gses.migrations.BaseBean;
import ru.codeinside.gses.service.DeclarantServiceProvider;
import ru.codeinside.gses.service.impl.DeclarantServiceImpl;
import ru.codeinside.gses.webui.Configurator;
import ru.codeinside.gses.webui.osgi.TRefRegistryImpl;
import ru.codeinside.gws.api.DeclarerContext;
import ru.codeinside.log.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;

//TODO: добавить в PersistanceAsset правильную очитску базы.
@RunWith(Arquillian.class)
@Ignore("Конфликтует с AdminServiceTest так как база данных не очищается")
public class DeclarerTest extends Assert {

  @Deployment
  public static JavaArchive createDeployment() {
    return ShrinkWrap
      .create(JavaArchive.class)
      .addPackage(FxDefinition.class.getPackage())
      .addPackage(AdminServiceProvider.class.getPackage())
      .addClass(ManagerService.class)
      .addClass(DeclarantServiceImpl.class)
      .addClass(ActivitiContext.class)
      .addClass(Configurator.class)
      .addClass(BaseBean.class)
      .addClass(DeclarantServiceProvider.class)
      .addPackage(Logger.class.getPackage())
      .addPackage(TRefRegistryImpl.class.getPackage())
      .addClass(DummyActivitiJobProvider.class)
      .add(new PersistanceAsset(), "META-INF/persistence.xml")
      .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
  }

  @Inject
  UserTransaction tx;

  @PersistenceContext(unitName = "myPU")
  EntityManager em;

  @Inject
  public ProcessEngine processEngine;

  @Before
  public void disableFixtures() {
    FxMarker.enabled = false;
  }

  @Test
  public void simple() throws Exception {
    tx.begin();
    ProcessDefinition def = deployForm("json-form/test.bpmn");

    Procedure procedure = new Procedure();
    procedure.setType(ProcedureType.Administrative);
    procedure.setDescription("test");
    procedure.setName("test");
    procedure.setVersion("1");
    em.persist(procedure);

    ProcedureProcessDefinition ppd = new ProcedureProcessDefinition();
    ppd.setProcessDefinitionId(def.getId());
    ppd.setVersion(0.0d + def.getVersion());
    ppd.setProcessDefinitionKey("uniJsonForm");
    ppd.setProcedure(procedure);
    em.persist(ppd);
    em.flush();
    tx.commit();

    tx.begin();
    AtomicLong gid = new AtomicLong(0L);
    SmevChain smevChain = new SmevChain(false, null, "1234", null, null, null);
    DeclarerContext declarerContext = new ActivitiDeclarerContext(smevChain, gid, def.getId(), "123");
    String json = String.format("{num: %5000d}", 1);
    assertEquals(5007, json.length());
    declarerContext.setValue("someVar", json);
    String bidId = declarerContext.declare();
    assertEquals(gid.get(), Long.parseLong(bidId));
    if (false) { // пробелмы с byteArray у ibatis и H2
      Bid bid = em.createQuery(
        "select b from Bid b where b.id=:bidId", Bid.class).setParameter("bidId", Long.parseLong(bidId)).getSingleResult();
      Object someVar = processEngine.getRuntimeService().getVariable(bid.getProcessInstanceId(), "someVar");
      byte[] asBytes = (byte[]) someVar;
      assertArrayEquals(json.getBytes(), asBytes);
    }
    tx.rollback();
  }

  private ProcessDefinition deployForm(String resource) {
    final InputStream is = getClass().getClassLoader().getResourceAsStream(resource);
    assertNotNull(resource, is);
    org.activiti.engine.repository.Deployment deploy = processEngine.getRepositoryService().createDeployment().addInputStream(resource, is).deploy();
    ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
    return processDefinition;
  }

}
