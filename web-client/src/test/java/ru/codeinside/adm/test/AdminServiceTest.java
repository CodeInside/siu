/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.test;

import com.google.common.collect.ImmutableSet;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
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
import ru.codeinside.adm.AdminService;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.UserItem;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.adm.database.Organization;
import ru.codeinside.adm.database.Role;
import ru.codeinside.adm.fixtures.FxDefinition;
import ru.codeinside.gses.manager.ManagerService;
import ru.codeinside.gses.migrations.BaseBean;
import ru.codeinside.gses.service.impl.DeclarantServiceImpl;
import ru.codeinside.gses.webui.Configurator;
import ru.codeinside.gses.webui.osgi.TRefRegistryImpl;
import ru.codeinside.log.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

@RunWith(Arquillian.class)
public class AdminServiceTest extends Assert {

  @Deployment
  public static JavaArchive createDeployment() {
    return ShrinkWrap
      .create(JavaArchive.class)
      .addPackage(Employee.class.getPackage())
      .addPackage(FxDefinition.class.getPackage())
      .addPackage(AdminServiceProvider.class.getPackage())
      .addClass(ManagerService.class)
      .addClass(DeclarantServiceImpl.class)
      .addClass(ActivitiContext.class)
      .addClass(Configurator.class)
      .addClass(GlassfihSecurity.class)
      .addClass(BaseBean.class)
      .addPackage(Logger.class.getPackage())
      .addPackage(TRefRegistryImpl.class.getPackage())
      .addClass(DummyActivitiJobProvider.class)
      .add(new PersistanceAsset(), "META-INF/persistence.xml")
      .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
  }

  @Inject
  UserTransaction tx;

  @Inject
  public ProcessEngine processEngine;

  @PersistenceContext(unitName = "myPU")
  EntityManager em;

  @Test
  public void defaultOrg() throws Exception {
    assertEquals(1, AdminServiceProvider.get().findAllOrganizations().size());
  }

  @Test
  public void processFixtures() throws Exception {
    final AdminService srv = AdminServiceProvider.get();
    tx.begin();
    try {

      assertFalse(srv.loadProcessFixtures(processEngine,
        getClass().getClassLoader().getResourceAsStream("process-fixtures.json")));

      FxMarker.enabled = true;
      assertTrue(srv.loadProcessFixtures(processEngine,
        getClass().getClassLoader().getResourceAsStream("process-fixtures.json")));
    } finally {
      FxMarker.enabled = false;
      tx.rollback();
    }
  }

  @Test
  public void membeships() throws Exception {
    tx.begin();
    try {
      AdminService at = AdminServiceProvider.get();
      Organization org = at.createOrganization("xxx", null, null);
      at.setOrgGroupNames(org.getId(), ImmutableSet.of("g0"));
      org = at.findOrganizationById(org.getId());
      assertEquals(1, org.getGroups().size());
    } finally {
      tx.rollback();
    }
  }

  @Test
  public void memberships2() throws Exception {

    final int USERS = 6;
    final int GROUPS = 1;
    final AdminService adm = AdminServiceProvider.get();

    tx.begin();
    try {
      final IdentityService ids = processEngine.getIdentityService();

      assertEquals(USERS, ids.createUserQuery().count());
      assertEquals(GROUPS, ids.createGroupQuery().count());

      // Создаём организацию и добавляем ей группы
      final Organization org = adm.createOrganization("org1", null, null);
      assertTrue(adm.setOrgGroupNames(org.getId(), ImmutableSet.of("g1", "g2")));
      assertEquals(ImmutableSet.of("g1", "g2"), adm.getOrgGroupNames(org.getId()));
      assertEquals(USERS, ids.createUserQuery().count());
      assertEquals(GROUPS, ids.createGroupQuery().count());

      // Создаём пользователя
      adm.createEmployee("login", "pass", "fio", ImmutableSet.<Role>of(), null, org.getId());
      assertEquals(USERS + 1, ids.createUserQuery().count());
      assertEquals(GROUPS + 2, ids.createGroupQuery().count());
      assertEquals(1, ids.createGroupQuery().groupId("g1").count());
      assertEquals(1, ids.createGroupQuery().groupId("g2").count());
      assertEquals(1, ids.createGroupQuery().groupId("g1").groupMember("login").count());
      assertEquals(1, ids.createGroupQuery().groupId("g2").groupMember("login").count());
      UserItem u = adm.getUserItem("login");
      assertEquals("fio", u.getFio());
      assertEquals(ImmutableSet.of(), u.getGroups());
      String elements[] = {"test"};
      Set set = new TreeSet(Arrays.asList(elements));
      assertEquals(set, u.getAllSocialGroups());

      // Добавляем персональные группы
      u.setGroups(ImmutableSet.of("ug1", "ug2"));
      adm.setUserItem("login", u);
      u = adm.getUserItem("login");
      assertEquals(ImmutableSet.of("ug1", "ug2"), u.getGroups());
      assertEquals(USERS + 1, ids.createUserQuery().count());
      assertEquals(GROUPS + 4, ids.createGroupQuery().count());
      assertEquals(1, ids.createGroupQuery().groupId("ug1").count());
      assertEquals(1, ids.createGroupQuery().groupId("ug2").count());
      assertEquals(1, ids.createGroupQuery().groupId("g1").groupMember("login").count());
      assertEquals(1, ids.createGroupQuery().groupId("g2").groupMember("login").count());
      assertEquals(1, ids.createGroupQuery().groupId("ug1").groupMember("login").count());
      assertEquals(1, ids.createGroupQuery().groupId("ug2").groupMember("login").count());

      // Отнимаем персональные группы
      u.setGroups(ImmutableSet.<String>of());
      adm.setUserItem("login", u);
      u = adm.getUserItem("login");
      assertEquals(ImmutableSet.of(), u.getGroups());
      assertEquals(USERS + 1, ids.createUserQuery().count());
      assertEquals(GROUPS + 4, ids.createGroupQuery().count());
      assertEquals(1, ids.createGroupQuery().groupId("ug1").count());
      assertEquals(1, ids.createGroupQuery().groupId("ug2").count());
      assertEquals(1, ids.createGroupQuery().groupId("g1").groupMember("login").count());
      assertEquals(1, ids.createGroupQuery().groupId("g2").groupMember("login").count());
      assertEquals(0, ids.createGroupQuery().groupId("ug1").groupMember("login").count());
      assertEquals(0, ids.createGroupQuery().groupId("ug2").groupMember("login").count());

      // Отнимаем группы у организации
      assertTrue(adm.setOrgGroupNames(org.getId(), ImmutableSet.<String>of()));
      assertEquals(ImmutableSet.of(), adm.getOrgGroupNames(org.getId()));
      assertEquals(USERS + 1, ids.createUserQuery().count());
      assertEquals(GROUPS + 4, ids.createGroupQuery().count());
      assertEquals(1, ids.createGroupQuery().groupId("g1").count());
      assertEquals(1, ids.createGroupQuery().groupId("g2").count());
      assertEquals(0, ids.createGroupQuery().groupId("g1").groupMember("login").count());
      assertEquals(0, ids.createGroupQuery().groupId("g2").groupMember("login").count());

      // Добавляем персональную группу
      u.setGroups(ImmutableSet.of("ug1"));
      adm.setUserItem("login", u);
      u = adm.getUserItem("login");
      assertEquals(ImmutableSet.of("ug1"), u.getGroups());
      assertEquals(USERS + 1, ids.createUserQuery().count());
      assertEquals(GROUPS + 4, ids.createGroupQuery().count());
      assertEquals(1, ids.createGroupQuery().groupId("g1").count());
      assertEquals(1, ids.createGroupQuery().groupId("g2").count());
      assertEquals(1, ids.createGroupQuery().groupId("ug1").groupMember("login").count());
      assertEquals(0, ids.createGroupQuery().groupId("ug2").groupMember("login").count());
      assertEquals(0, ids.createGroupQuery().groupId("g1").groupMember("login").count());
      assertEquals(0, ids.createGroupQuery().groupId("g2").groupMember("login").count());

      // Отнимаем персональную группу
      u.setGroups(ImmutableSet.<String>of());
      adm.setUserItem("login", u);
      u = adm.getUserItem("login");
      assertEquals(ImmutableSet.of(), u.getGroups());
      assertEquals(USERS + 1, ids.createUserQuery().count());
      assertEquals(GROUPS + 4, ids.createGroupQuery().count());
      assertEquals(1, ids.createGroupQuery().groupId("g1").count());
      assertEquals(1, ids.createGroupQuery().groupId("g2").count());
      assertEquals(0, ids.createGroupQuery().groupId("ug1").groupMember("login").count());
      assertEquals(0, ids.createGroupQuery().groupId("ug2").groupMember("login").count());
      assertEquals(0, ids.createGroupQuery().groupId("g1").groupMember("login").count());
      assertEquals(0, ids.createGroupQuery().groupId("g2").groupMember("login").count());

    } finally {
      tx.rollback();
    }

  }

  @Test
  @Ignore("не выходит каменный цветок")
  public void z() throws Exception {
    tx.begin();
    Connection connection = em.unwrap(Connection.class);
    String database = connection.getMetaData().getDatabaseProductName();
    System.err.println("Database is " + database);
    if (!database.equals("H2")) {
      for (Object o : em.createNativeQuery("select tablename from pg_tables where schemaname = 'public'").getResultList()) {
        String table = (String) o;
        if (!"pg_ts_dict".equals(table) && !"pg_ts_parser".equals(table)) {
          em.createNativeQuery("drop table if exists \"" + table + "\" cascade").executeUpdate();
        }
      }
      for (Object o : em.createNativeQuery("select relname from pg_class where relkind = 'S'").getResultList()) {
        String sequence = (String) o;
        em.createNativeQuery("drop sequence " + sequence).executeUpdate();
      }
    }
    tx.commit();
  }

  @Inject
  GlassfihSecurity security;

  static boolean adminCreated;

  /**
   * Arquillian не предоставляет стандартрного механизма для указания пользователя, делаем сами.
   */
  @Before
  public void loginAsAdmin() throws Exception {
    if (!adminCreated) {
      adminCreated = true;
      security.create("administrator", ImmutableSet.of("Administrator"));
    }
    security.login("administrator", "1");
  }

}
