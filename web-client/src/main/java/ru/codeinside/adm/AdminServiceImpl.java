/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.cmd.GetAttachmentCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.glassfish.osgicdi.OSGiService;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.BidStatus;
import ru.codeinside.adm.database.BidWorkers;
import ru.codeinside.adm.database.ClientRequestEntity;
import ru.codeinside.adm.database.DefinitionStatus;
import ru.codeinside.adm.database.Directory;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.adm.database.EnclosureEntity;
import ru.codeinside.adm.database.ExternalGlue;
import ru.codeinside.adm.database.Group;
import ru.codeinside.adm.database.InfoSystem;
import ru.codeinside.adm.database.InfoSystemService;
import ru.codeinside.adm.database.InfoSystemService_;
import ru.codeinside.adm.database.InfoSystem_;
import ru.codeinside.adm.database.News;
import ru.codeinside.adm.database.Organization;
import ru.codeinside.adm.database.Procedure;
import ru.codeinside.adm.database.ProcedureProcessDefinition;
import ru.codeinside.adm.database.ProcedureType;
import ru.codeinside.adm.database.Role;
import ru.codeinside.adm.database.ServiceResponseEntity;
import ru.codeinside.adm.database.ServiceUnavailable;
import ru.codeinside.adm.database.SystemProperty;
import ru.codeinside.adm.fixtures.Fx;
import ru.codeinside.adm.fixtures.FxDefinition;
import ru.codeinside.adm.fixtures.FxDirectory;
import ru.codeinside.adm.fixtures.FxDirectoryBase;
import ru.codeinside.adm.fixtures.FxInfoSystem;
import ru.codeinside.adm.fixtures.FxInfoSystemBase;
import ru.codeinside.adm.fixtures.FxInfoSystemService;
import ru.codeinside.adm.fixtures.FxMarker;
import ru.codeinside.adm.fixtures.FxProcedure;
import ru.codeinside.adm.fixtures.FxService;
import ru.codeinside.adm.parser.EmployeeFixtureParser;
import ru.codeinside.gses.activiti.Activiti;
import ru.codeinside.gses.activiti.ActivitiFormProperties;
import ru.codeinside.gses.manager.ManagerService;
import ru.codeinside.gses.service.DeclarantService;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.gws.ClientRefRegistry;
import ru.codeinside.gses.webui.gws.TRef;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ServerResponse;
import ru.codeinside.gws.api.ServiceDefinitionParser;
import ru.codeinside.log.Actor;
import ru.codeinside.log.Log;

import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;

@TransactionManagement
@TransactionAttribute
@Singleton
@Stateless
@DependsOn("BaseBean")
public class AdminServiceImpl implements AdminService {

  final String INSTANCE = System.getProperty("com.sun.aas.instanceName");
  final String HOST = System.getProperty("com.sun.aas.hostName");
  final Logger logger = Logger.getLogger(getClass().getName());

  @PersistenceContext(unitName = "myPU")
  EntityManager em;

  @PersistenceUnit(unitName = "myPU")
  EntityManagerFactory emf;

  @PersistenceContext(unitName = "logPU")
  EntityManager emLog;

  @PersistenceUnit(unitName = "logPU")
  EntityManagerFactory emfLog;

  @Inject
  Instance<ProcessEngine> processEngine;

  @Inject
  ManagerService mService;

  @Inject
  DeclarantService dService;

  @Inject
  ClientRefRegistry registry;

  @Inject
  @OSGiService(dynamic = true)
  protected ServiceDefinitionParser serviceDefinitionParser;

  private AtomicReference TICKET = new AtomicReference();

  @TransactionAttribute(REQUIRES_NEW)
  public Object afterCreate() {
    final Object ticket = new Object();
    if (TICKET.compareAndSet(null, ticket)) {
      emLog.persist(new Log(null, "instance", INSTANCE != null ? INSTANCE : HOST, "started", null, true));
      return ticket;
    }
    return null;
  }

  @TransactionAttribute(REQUIRES_NEW)
  public void preDestroy(Object ticket) {
    if (ticket != null && TICKET.compareAndSet(ticket, null)) {
      emLog.persist(new Log(null, "instance", INSTANCE != null ? INSTANCE : HOST, "shutdown", null, true));
    }
  }

  @Override
  public Organization createOrganization(String name, String login, Organization parent) {
    final Organization org = new Organization();
    org.setName(name);
    if (login != null) {
      org.setCreator(findEmployeeByLogin(login));
    }
    if (parent != null) {
      parent = em.merge(parent);
      org.setParent(parent);
      parent.getOrganizations().add(org);
    }
    em.persist(org);
    return org;
  }

  @Override
  public List<Organization> findAllOrganizations() {
    TypedQuery<Organization> query = em.createNamedQuery("findAllOrganizations", Organization.class);
    return query.getResultList();
  }

  @Override
  public List<Long> findAllOrganizationIds() {
    TypedQuery<Long> query = em.createNamedQuery("findAllOrganizationIds", Long.class);
    return query.getResultList();
  }

  @Override
  public Organization findOrganizationById(Long id) {
    return em.find(Organization.class, id);
  }

  @Override
  public void updateOrganization(Organization organization) {
    em.merge(organization);
  }

  @Override
  @TransactionAttribute(REQUIRED)
  public Employee createEmployee(String login, String password, String fio, Set<Role> roles, String creator,
                                 long orgId, TreeSet<String> groupExecutor, TreeSet<String> groupSupervisorEmp,
                                 TreeSet<String> groupSupervisorOrg) {
    final Organization org = em.getReference(Organization.class, orgId);
    return createUser(login, password, fio, roles, creator, org, groupExecutor, groupSupervisorEmp,
      groupSupervisorOrg);
  }

  @Override
  @TransactionAttribute(REQUIRED)
  public Employee createEmployee(String login, String password, String fio, Set<Role> roles, String creator,
                                 long orgId) {
    final Organization org = em.getReference(Organization.class, orgId);
    return createUser(login, password, fio, roles, creator, org);
  }

  Employee createUser(String login, String password, String fio, Set<Role> roles, String creator,
                      final Organization org) {
    Employee employee = new Employee();
    employee.setLogin(login);
    employee.setPasswordHash(DigestUtils.sha256Hex(password));
    employee.setFio(fio);
    employee.getRoles().addAll(roles);
    employee.setCreator(creator);
    employee.setOrganization(org);
    employee.setLocked(false);
    org.getEmployees().add(employee);
    em.persist(employee);
    final Set<String> groups = new HashSet<String>();
    for (Group group : org.getGroups()) {
      groups.add(group.getName());
    }
    logger.log(Level.FINE, "GROUPS: " + groups);
    syncUser(employee, Collections.<Group>emptySet(), groups, processEngine.get().getIdentityService());
    return employee;
  }

  Employee createUser(String login, String password, String fio, Set<Role> roles, String creator,
                      final Organization org, TreeSet<String> groupExecutor, TreeSet<String> groupSupervisorEmp,
                      TreeSet<String> groupSupervisorOrg) {
    Employee employee = new Employee();
    employee.setLogin(login);
    employee.setPasswordHash(DigestUtils.sha256Hex(password));
    employee.setFio(fio);
    employee.getRoles().addAll(roles);
    employee.setCreator(creator);
    employee.setOrganization(org);
    employee.setLocked(false);
    Set<Group> organizationGroups = new HashSet<Group>();
    for (Group g : selectGroupsBySocial(false)) {
      if (groupSupervisorOrg.contains(g.getName())) {
        organizationGroups.add(g);
      }
    }
    employee.setOrganizationGroups(organizationGroups);

    Set<Group> employeeGroups = new HashSet<Group>();
    for (Group g : selectGroupsBySocial(true)) {
      if (groupSupervisorEmp.contains(g.getName())) {
        employeeGroups.add(g);
      }
    }
    employee.setEmployeeGroups(employeeGroups);
    org.getEmployees().add(employee);
    em.persist(employee);
    setUserGroups(employee, groupExecutor);
    final Set<String> groups = new HashSet<String>();
    for (Group group : org.getGroups()) {
      groups.add(group.getName());
    }
    logger.log(Level.FINE, "GROUPS: " + groups);
    syncUser(employee, Collections.<Group>emptySet(), groups, processEngine.get().getIdentityService());
    return employee;
  }

  @Override
  public Employee findEmployeeByLogin(String login) {
    return em.find(Employee.class, login);
  }

  @Override
  public Set<String> getOrgGroupNames(long orgId) {
    final Organization org = em.getReference(Organization.class, orgId);
    final Set<String> item = new TreeSet<String>();
    for (Group group : org.getGroups()) {
      item.add(group.getName());
    }
    return item;
  }

  @Override
  public Set<String> getOrgGroupNames() {
    return selectGroupNamesBySocial(false);
  }

  @Override
  public Set<String> getEmpGroupNames() {
    return selectGroupNamesBySocial(true);
  }

  @Override
  public Set<String> selectGroupNamesBySocial(boolean social) {
    return new TreeSet<String>(em.createNamedQuery("groupNamesBySocial", String.class)
      .setParameter("social", social).getResultList());
  }

  @Override
  public List<Group> selectGroupsBySocial(boolean social) {
    TypedQuery<Group> query = em.createNamedQuery("groupsBySocial", Group.class).setParameter("social", social);
    return query.getResultList();
  }

  @Override
  public void init() {
    final ProcessEngine engine = processEngine.get();
    if (em.createQuery("select count(o.id) from Organization o", Number.class).getSingleResult().intValue() == 0) {
      try {
        final long start = System.currentTimeMillis();
        logger.log(Level.WARNING, "Запуск наполнения базы");
        loadFixtures(getClass().getClassLoader().getResourceAsStream("fixtures.txt"));
        final long finish = System.currentTimeMillis();
        logger.log(Level.WARNING, "Завершено наполнение базы [" + (finish - start) + "мс]");
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      try {
        final long start = System.currentTimeMillis();
        logger.log(Level.WARNING, "Запуск наполнения маршрутами");
        loadProcessFixtures(engine, getClass().getClassLoader().getResourceAsStream("process-fixtures.json"));
        final long finish = System.currentTimeMillis();
        logger.log(Level.WARNING, "Завершено наполнение маршрутами [" + (finish - start) + "мс]");
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      try {
        final long start = System.currentTimeMillis();
        logger.log(Level.WARNING, "Запуск наполнения ВИС");
        loadInfoSystemFixtures(getClass().getClassLoader().getResourceAsStream("infosystem-fixtures.json"));
        final long finish = System.currentTimeMillis();
        logger.log(Level.WARNING, "Завершено наполнение ВИС [" + (finish - start) + "мс]");
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      try {
        final long start = System.currentTimeMillis();
        logger.log(Level.WARNING, "Запуск наполнения директорий");
        loadDirectoryFixtures(getClass().getClassLoader().getResourceAsStream("directory-fixtures.json"));
        final long finish = System.currentTimeMillis();
        logger.log(Level.WARNING, "Завершено наполнение директорий [" + (finish - start) + "мс]");
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      final long start = System.currentTimeMillis();
      logger.log(Level.WARNING, "Отключение привязки сертификатов по умолчанию");
      em.createNativeQuery("UPDATE systemproperty SET value='false' WHERE key = 'CertificateVerifier.linkCertificate'")
        .executeUpdate();
      final long finish = System.currentTimeMillis();
      logger.log(Level.WARNING, "Привязка отключена [" + (finish - start) + "мс]");
    }
    final IdentityService srv = engine.getIdentityService();
    final Set<String> domainGroups = ImmutableSet.copyOf(em.createQuery("SELECT g.name FROM Group g", String.class)
      .getResultList());
    for (org.activiti.engine.identity.Group act : srv.createGroupQuery().list()) {
      final String groupName = act.getId();
      if (!domainGroups.contains(groupName)) {
        logger.log(Level.WARNING, "Синхронизация потерянной группы " + groupName);
        final Group group = new Group();
        group.setName(groupName);
        group.setTitle(groupName);
        em.persist(group);
      }
    }

    // мы внутри транзакции, можем получить текущее соедиенение
    fixAssignmentHistory(em.unwrap(Connection.class));
  }

  /**
   * Восстановление связи между исполнителем userTask и заявкой.
   */
  private void fixAssignmentHistory(final Connection connection) {

    try {
      final DatabaseMetaData metaData = connection.getMetaData();
      if ("H2".equals(metaData.getDatabaseProductName())) {
        return; // увы, SQL не совместим с H2
      }

      final Statement s = connection.createStatement();

      final boolean hasFix;
      {
        final ResultSet rs = s.executeQuery(
          "select value_ from act_ge_property where name_ = 'assignFixed'"
        );
        hasFix = rs.next();
        rs.close();
      }

      if (!hasFix) {
        s.execute(
          "create temp table fix (" +
            " bid bigint not null," +
            " assignee varchar(64) not null" +
            ") on commit drop"
        );
        s.execute("insert into fix select distinct b.id, h.assignee_ from act_hi_taskinst h, bid b" +
          " where b.processinstanceid = h.proc_inst_id_ and h.assignee_ is not null");
        s.execute("delete from fix using bidworkers where bid=bidworkers.bid_id and assignee=bidworkers.employee_login");
        s.execute("insert into bidworkers select bid, assignee from fix");
        s.execute("insert into act_ge_property values ('assignFixed', '1', 0)");
      }
      s.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean setOrgGroupNames(final long orgId, final Set<String> names) {
    final Set<String> oldNames = new HashSet<String>();
    final Organization org = em.getReference(Organization.class, orgId);
    final Set<Group> deletions = new HashSet<Group>();
    for (final Group group : org.getGroups()) {
      final String name = group.getName();
      oldNames.add(name);
      if (!names.contains(name)) {
        deletions.add(group);
      }
    }
    if (!oldNames.equals(names)) {
      for (final Group group : deletions) {
        group.getOrganizations().remove(org);
        org.getGroups().remove(group);
      }
      final Set<String> addNames = new HashSet<String>(names);
      addNames.removeAll(oldNames);
      for (final String name : addNames) {
        final Group group = getOrCreateGroupByName(name, false);
        group.getOrganizations().add(org);
        org.getGroups().add(group);
      }
      final IdentityService act = processEngine.get().getIdentityService();
      for (final Employee employee : org.getEmployees()) {
        final Set<Group> deletions2 = new HashSet<Group>(deletions);
        deletions2.removeAll(employee.getGroups());
        syncUser(employee, deletions2, addNames, act);
      }
    }
    return !oldNames.equals(names);
  }

  Group getOrCreateGroupByName(final String name, boolean social) {
    final List<Group> groups = em.createNamedQuery("groupByName", Group.class).setParameter("name", name)
      .getResultList();
    final Group group;
    if (groups.isEmpty()) {
      group = new Group(social);
      group.setName(name);
      group.setTitle(name);
      em.persist(group);
    } else {
      group = groups.get(0);
    }
    if (social != group.isSocial()) {
      throw new IllegalStateException("Группа " + name + (social ? " не социальная" : " социальная"));
    }
    return group;
  }

  @Override
  public UserItem getUserItem(String login) {
    final Employee employee = em.find(Employee.class, login);
    final UserItem userItem = new UserItem();
    userItem.setFio(employee.getFio());
    userItem.setRoles(employee.getRoles().isEmpty() ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(employee
      .getRoles()));
    final Set<String> current = new TreeSet<String>();
    for (Group group : employee.getGroups()) {
      current.add(group.getName());
    }
    userItem.setGroups(current);
    final Set<String> employeeGroups = new TreeSet<String>();
    for (Group group : employee.getEmployeeGroups()) {
      employeeGroups.add(group.getName());
    }
    userItem.setEmployeeGroups(employeeGroups);
    final Set<String> organizationGroups = new TreeSet<String>();
    for (Group group : employee.getOrganizationGroups()) {
      organizationGroups.add(group.getName());
    }
    userItem.setOrganizationGroups(organizationGroups);
    userItem.setAllSocialGroups(selectGroupNamesBySocial(true));
    userItem.setLocked(employee.isLocked());
    if (employee.getCertificate() != null) {
      userItem.setX509(employee.getCertificate().getX509());
    }
    return userItem;
  }

  @Override
  public void setUserItem(final String login, final UserItem userItem) {
    final Employee e = AdminServiceProvider.get().findEmployeeByLogin(login);
    e.setFio(userItem.getFio());
    e.setLocked(userItem.isLocked());
    e.getRoles().clear();
    e.getRoles().addAll(userItem.getRoles());
    if (userItem.getPassword1() != null) {
      e.setPasswordHash(DigestUtils.sha256Hex(userItem.getPassword1()));
    }
    setUserGroups(e, userItem.getGroups());
    setAvailableGroups(e, userItem.getEmployeeGroups(), userItem.getOrganizationGroups());
    boolean canAllowCertificate = userItem.getRoles().contains(Role.Declarant) || userItem.getRoles().contains(Role.Executor)
      || userItem.getRoles().contains(Role.Supervisor) || userItem.getRoles().contains(Role.SuperSupervisor);
    if (userItem.getX509() == null || !canAllowCertificate) {
      e.setCertificate(null); // удалиться сам по orphanRemoval = true
    }
    em.persist(e);
  }

  @Override
  public void setUserGroups(final Employee e, Set<String> names) {
    final Set<String> oldNames = new HashSet<String>();
    final Set<Group> deletions = new HashSet<Group>();

    for (final Group group : e.getGroups()) {
      final String name = group.getName();
      oldNames.add(name);
      if (!names.contains(name)) {
        deletions.add(group);
      }
    }
    // хреновое условие!
    if (!oldNames.equals(names)) {
      for (final Group group : deletions) {
        group.getEmployees().remove(e);
        e.getGroups().remove(group);
      }
      final Set<String> addNames = new HashSet<String>(names);
      addNames.removeAll(oldNames);
      for (final String name : addNames) {
        final Group group = getOrCreateGroupByName(name, true);
        group.getEmployees().add(e);
        e.getGroups().add(group);
      }
      final Set<Group> deletions2 = new HashSet<Group>(deletions);
      deletions2.removeAll(e.getOrganization().getGroups());
      syncUser(e, deletions2, addNames, processEngine.get().getIdentityService());
    }
  }

  @Override
  @TransactionAttribute(REQUIRED)
  public void loadEmployeeData(InputStream data, final String currentUserName) throws IOException {
    Preconditions.checkNotNull(data);
    Preconditions.checkArgument(StringUtils.isNotBlank(currentUserName), "User name is blank");
    EmployeeFixtureParser parser = new EmployeeFixtureParser();
    // загрузка справочников
    final Collection<Organization> organizationList = new LinkedList<Organization>();
    organizationList.addAll((findAllOrganizations()));
    final Collection<Employee> employees = new LinkedList<Employee>();
    employees.addAll(findAllEmployees());
    parser.loadFixtures(data, new EmployeeFixtureParser.PersistenceCallback() {
      @Override
      public Long onOrganizationComplete(String orgName, Set<String> groups, Long ownerId) {
        List<Organization> searchedOrg = ImmutableList.copyOf(Collections2.filter(organizationList,
          new OrgBySearchNamePredicate(
            orgName)
        ));
        Long result;
        if (searchedOrg.size() > 0) {
          result = searchedOrg.get(0).getId();
        } else {
          // поиск родительской организации
          List<Organization> parentList = ImmutableList.copyOf(Collections2.filter(organizationList,
            new OrgSearchByIdPredicate(
              ownerId)
          ));
          Organization parent = parentList.size() > 0 ? parentList.get(0) : null;
          Organization organization = createOrganization(orgName, currentUserName, parent);
          organizationList.add(organization);
          // назначаем права
          result = organization.getId();
          if (!groups.isEmpty()) {
            setOrgGroupNames(result, groups);
          }
        }
        return result;
      }

      @Override
      public void onUserComplete(String login,
                                 String pwd,
                                 String name,
                                 long orgId,
                                 Set<Role> roles,
                                 Set<String> groups) {
        Preconditions.checkArgument(StringUtils.isNotBlank(pwd), "У пользователя %s не задан пароль", login);
        Preconditions.checkArgument(StringUtils.isNotBlank(login), "У пользователя %s не задан login", name);
        Preconditions.checkArgument(StringUtils.isNotBlank(name), "У пользователя %s не указано имя",
          login);
        // получить пользователя по логину
        List<Employee> filterEmployee = ImmutableList.copyOf(Collections2.filter(employees,
          new EmployeeByLoginPredicate(login)));
        if (filterEmployee.size() == 0) {// если пользователя нет, то создать
          List<Organization> parentList = ImmutableList.copyOf(Collections2.filter(organizationList,
            new OrgSearchByIdPredicate(orgId))); // поиск родительской организации
          Organization parent = parentList.size() > 0 ? parentList.get(0) : null;
          if (parent != null) {
            Employee user = createEmployee(login, pwd, name, roles, currentUserName, orgId);
            employees.add(user);
            if (!groups.isEmpty()) {
              setUserGroups(user, groups);
            }
          } else {
            throw new IllegalStateException("Для пользователя " + login + " не найдена организация.");
          }
        }
      }
    });
  }

  @Override
  public void saveBidAssignment(final ProcessEngine processEngine, final String processInstanceId, String userLogin) {
    assert processEngine != null;
    assert processInstanceId != null;
    assert userLogin != null;
    final String superProcessInstanceId = getSuperProcessInstanceId(processEngine, processInstanceId);
    final Bid bid = getBidByProcessInstanceId(superProcessInstanceId);
    if (bid != null) {
      bid.setStatus(BidStatus.Execute);
      em.merge(bid);
      final Employee employee = em.find(Employee.class, userLogin);
      em.merge(new BidWorkers(bid, employee));
    }
  }

  @Override
  public ExternalGlue getGlueById(Long id) {
    if (id == null) {
      return null;
    }
    return em.find(ExternalGlue.class, id);
  }

  /**
   * Получить процесс, запустивший данный процесс.
   */
  private String getSuperProcessInstanceId(final ProcessEngine engine, String processInstanceId) {
    final RuntimeService runtimeService = engine.getRuntimeService();
    while (true) {
      final ProcessInstance superProcess = runtimeService.createProcessInstanceQuery().subProcessInstanceId(processInstanceId).singleResult();
      if (superProcess == null) {
        return processInstanceId;
      }
      processInstanceId = superProcess.getProcessInstanceId();
    }
  }

  // нужно учесть как группы организации так и персональные!
  void syncUser(final Employee employee, final Set<Group> deletions, final Set<String> inserts,
                final IdentityService act) {
    final String login = employee.getLogin();
    if (act.createUserQuery().userId(login).count() < 1) {
      logger.log(Level.FINE, "+user " + login);
      act.saveUser(act.newUser(login));
    }
    for (final Group group : deletions) {
      final String groupName = group.getName();
      if (act.createGroupQuery().groupId(groupName).groupMember(login).count() > 0) {
        logger.log(Level.FINE, "-membership " + login + ", " + groupName);
        act.deleteMembership(login, groupName);
      }
    }
    for (final String groupName : inserts) {
      final boolean groupCreation = act.createGroupQuery().groupId(groupName).count() <= 0;
      if (groupCreation) {
        logger.log(Level.FINE, "+group " + groupName);
        act.saveGroup(act.newGroup(groupName));
      }
      if (groupCreation || act.createGroupQuery().groupId(groupName).groupMember(login).count() <= 0) {
        logger.log(Level.FINE, "+membership " + login + ", " + groupName);
        act.createMembership(login, groupName);
      }
    }
  }

  void setAvailableGroups(final Employee e, Set<String> namesEmp, Set<String> namesOrg) {

    final Set<Group> groupsEmp = new HashSet<Group>();
    for (final Group group : selectGroupsBySocial(true)) {
      final String name = group.getName();
      if (namesEmp.contains(name)) {
        groupsEmp.add(group);
      }
    }
    e.setEmployeeGroups(groupsEmp);
    final Set<Group> groupsOrg = new HashSet<Group>();
    for (final Group group : selectGroupsBySocial(false)) {
      final String name = group.getName();
      if (namesOrg.contains(name)) {
        groupsOrg.add(group);
      }
    }
    e.setOrganizationGroups(groupsOrg);
    syncUser(e, Collections.<Group>emptySet(), Collections.<String>emptySet(), processEngine.get()
      .getIdentityService());
  }

  void loadFixtures(InputStream is) throws IOException {
    EmployeeFixtureParser parser = new EmployeeFixtureParser();
    parser.loadFixtures(is, new EmployeeFixtureParser.PersistenceCallback() {
      @Override
      public Long onOrganizationComplete(String orgName, Set<String> groups, Long ownerId) {
        Organization org = new Organization();
        org.setName(orgName);
        if (ownerId != null) {
          Organization owner = em.getReference(Organization.class, ownerId);
          org.setParent(owner);
          owner.getOrganizations().add(org);
          logger.log(Level.FINE, "Подразделение " + org.getName() + " -> " + owner.getName() + ", " + groups);
        } else {
          logger.log(Level.FINE, "Организация " + org.getName() + ", " + groups);
        }
        em.persist(org);
        if (!groups.isEmpty()) {
          setOrgGroupNames(org.getId(), groups);
        }
        return org.getId();
      }

      @Override
      public void onUserComplete(String login, String pwd, String name, long orgId, Set<Role> roles, Set<String> groups) {
        Organization owner = em.getReference(Organization.class, orgId);
        logger.log(Level.FINE, "Пользователь " + name + "(" + login + "," + roles + ") -> " + owner.getName()
          + ", " + groups);
        if (roles.isEmpty()) {
          roles.add(Role.Executor);
        }
        final Employee user = createUser(login, StringUtils.defaultIfEmpty(pwd, "1"), name, roles, null, owner);
        if (!groups.isEmpty()) {
          setUserGroups(user, groups);
        }
      }
    });
  }

  @Override
  public boolean loadProcessFixtures(ProcessEngine engine, InputStream is) throws IOException {
    final Fx fx = new Gson().fromJson(new InputStreamReader(is, "UTF8"), Fx.class);
    for (final String name : fx.markers) {
      try {
        final Class<?> clazz = Class.forName(name);
        if (FxMarker.class.isAssignableFrom(clazz)) {
          if (!((FxMarker) clazz.newInstance()).enabled()) {
            return false;
          }
        }
      } catch (ClassNotFoundException e) {
        // skip
      } catch (InstantiationException e) {
        // skip
      } catch (IllegalAccessException e) {
        // skip
      }
    }
    for (final FxService srv : fx.services) {
      List<String> declarantTypes = Arrays.asList(srv.declarantTypes);
      final long serviceId = mService.createApService(srv.name, null, srv.creator, declarantTypes);
      for (final FxProcedure proc : srv.procs) {
        final Procedure procedure = mService.createProcedure(
          proc.name,
          proc.description,
          "" + serviceId,
          proc.code,
          proc.creator,
          ProcedureType.Administrative);
        String lastId = "";
        for (final FxDefinition def : proc.defs) {
          final InputStream ris = getClass().getClassLoader().getResourceAsStream(def.route);
          final Deployment deployment = engine.getRepositoryService().createDeployment()
            .addInputStream(def.route, ris).deploy();
          final ProcessDefinition processDef = engine.getRepositoryService().createProcessDefinitionQuery()
            .deploymentId(deployment.getId()).singleResult();
          ProcedureProcessDefinition procdef = mService.createProcessDefination(procedure.getId(),
            processDef, def.creator, lastId);
          lastId = procdef.getProcessDefinitionId();

          if (def.toStatus != null) {
            for (final DefinitionStatus status : def.toStatus) {
              if (!mService.updateProcessDefinationStatus(lastId, status)) {
                throw new IllegalStateException("Невозможно перевести в " + status + " " + def.route
                  + " из " + proc.name);
              }
            }
          }
          for (int i = 0; i < def.count; i++) {
            ActivitiFormProperties properties = ActivitiFormProperties.empty();
            properties.formPropertyValues.put("attach1", "1");
            properties.formPropertyValues.put("attach2", "2");
            dService.declare(null, null, engine, lastId, properties, def.creator, null);
          }
        }
      }
    }
    return true;
  }

  public boolean loadInfoSystemFixtures(InputStream is) throws IOException {
    final FxInfoSystemBase fx = new Gson().fromJson(new InputStreamReader(is, "UTF8"), FxInfoSystemBase.class);
    for (final FxInfoSystem system : fx.systems) {
      try {
        InfoSystem infosys = createInfoSystem(system.code, system.name, null);
        if (system.source) {
          toggleSource(system.code, true);
        }
        for (FxInfoSystemService service : system.services) {
          createInfoSystemService(infosys.getCode(), system.source ? system.code : null,
            service.address, service.revision, service.sname, service.sversion, service.name, service.available, false
          );
        }
      } catch (Exception e) {
        logger.log(Level.INFO, "fx infoSystem " + system.code, e);
      }
    }
    return true;
  }

  public boolean loadDirectoryFixtures(InputStream is) throws IOException {
    final FxDirectoryBase fx = new Gson().fromJson(new InputStreamReader(is, "UTF8"), FxDirectoryBase.class);
    for (final FxDirectory directory : fx.directories) {
      try {
        Directory dir = new Directory(directory.name);
        dir.setValues(directory.values);
        em.persist(dir);
      } catch (Exception e) {
        logger.log(Level.INFO, "fx infoSystem " + directory.name, e);
      }
    }
    return true;
  }

  @Override
  public int getEmployeesCount(long orgId, boolean locked) {
    return em.createQuery("select count(e) from Employee e where e.organization.id=:orgId and e.locked=:locked", Number.class)
      .setParameter("orgId", orgId).setParameter("locked", locked).getSingleResult().intValue();
  }

  @Override
  public Set<Group> getControlledOrgGroupsOf(String login) {
    Employee employee = findEmployeeByLogin(login);
    if (employee.getRoleNames().contains(Role.SuperSupervisor.description)) {
      return new HashSet<Group>(selectGroupsBySocial(false));
    }
    return employee.getOrganizationGroups();
  }

  @Override
  public Set<Group> getControlledEmpGroupsOf(String login) {
    Employee employee = findEmployeeByLogin(login);
    if (employee.getRoleNames().contains(Role.SuperSupervisor.description)) {
      return new HashSet<Group>(selectGroupsBySocial(true));
    }
    return employee.getEmployeeGroups();
  }

  @Override
  public List<Employee> getOrgGroupMembers(String groupName, String taskId, int startIndex, int count) {
    Group group = em.createQuery("select g from Group g where g.name=:groupName", Group.class).setParameter("groupName", groupName).getSingleResult();
    Set<Organization> organizations = group.getOrganizations();
    List<Employee> executors = new ArrayList<Employee>();
    ProcessEngine engine = Flash.flash().getProcessEngine();
    TaskService taskService = engine.getTaskService();
    IdentityService identityService = engine.getIdentityService();
    for (Organization organization : organizations) {
      for (Employee employee : organization.getEmployees()) {
        if (employee.getRoles().contains(Role.Executor) && canClaim(taskId, employee, taskService, identityService)) {
          executors.add(employee);
        }
      }
    }
    return executors.subList(startIndex, startIndex + count);
  }

  @Override
  public List<Employee> getEmpGroupMembers(String groupName, String taskId, int startIndex, int count) {
    Group group = em.createQuery("select g from Group g where g.name=:groupName", Group.class).setParameter("groupName", groupName).getSingleResult();
    List<Employee> allEmployees = new ArrayList<Employee>(group.getEmployees());
    List<Employee> executors = new ArrayList<Employee>();
    ProcessEngine engine = Flash.flash().getProcessEngine();
    TaskService taskService = engine.getTaskService();
    IdentityService identityService = engine.getIdentityService();
    for (Employee e : allEmployees) {
      if (e.getRoles().contains(Role.Executor) && canClaim(taskId, e, taskService, identityService)) {
        executors.add(e);
      }
    }
    return executors.subList(startIndex, startIndex + count);
  }

  @Override
  public int getOrgGroupMembersCount(String groupName, String taskId) {
    Group group = em.createQuery("select g from Group g where g.name=:groupName", Group.class).setParameter("groupName", groupName).getSingleResult();
    Set<Organization> organizations = group.getOrganizations();
    int size = 0;
    ProcessEngine engine = Flash.flash().getProcessEngine();
    TaskService taskService = engine.getTaskService();
    IdentityService identityService = engine.getIdentityService();
    for (Organization organization : organizations) {
      for (Employee employee : organization.getEmployees()) {
        if (employee.getRoles().contains(Role.Executor) && canClaim(taskId, employee, taskService, identityService)) {
          size++;
        }
      }
    }
    return size;
  }

  private boolean canClaim(String taskId, Employee employee, TaskService taskService, IdentityService identityService) {
    boolean canClaim = taskService.createTaskQuery().taskCandidateUser(employee.getLogin()).taskId(taskId).count() == 1;
    List<String> candidateGroups = Lists.newArrayList();
    for (org.activiti.engine.identity.Group g : identityService.createGroupQuery().groupMember(employee.getLogin()).list()) {
      candidateGroups.add(g.getId());
    }
    boolean canClaimByGroup = taskService.createTaskQuery().taskCandidateGroupIn(candidateGroups).taskId(taskId).count() == 1;
    return canClaim || canClaimByGroup;
  }

  @Override
  public int getEmpGroupMembersCount(String groupName, String taskId) {
    Group group = em.createQuery("select g from Group g where g.name=:groupName", Group.class).setParameter("groupName", groupName).getSingleResult();
    int size = 0;
    ProcessEngine engine = Flash.flash().getProcessEngine();
    TaskService taskService = engine.getTaskService();
    IdentityService identityService = engine.getIdentityService();
    for (Employee e : group.getEmployees()) {
      if (e.getRoles().contains(Role.Executor) && canClaim(taskId, e, taskService, identityService)) {
        size++;
      }
    }
    return size;
  }

  @Override
  public ClientRequestEntity getClientRequestEntity(long id) {
    return em.getReference(ClientRequestEntity.class, id);
  }

  @Override
  public <T> T withEmployee(final long orgId, final String login, final Function<Employee, T> callback) {
    final Employee employee = em
      .createQuery("select e from Employee e where e.login=:login and e.organization.id=:orgId",
        Employee.class).setParameter("login", login).setParameter("orgId", orgId).getSingleResult();
    return callback.apply(employee);
  }

  @Override
  public <T> T withEmployee(final String login, final Function<Employee, T> callback) {
    final Employee employee = em.getReference(Employee.class, login);
    return callback.apply(employee);
  }

  @Override
  public <T> T withEmployees(long orgId, boolean locked, int start, int count, String[] order, boolean[] asc,
                             Function<List<Employee>, T> callback) {
    StringBuilder q = new StringBuilder("select e from Employee e where e.organization.id=:orgId and e.locked=:locked");
    for (int i = 0; i < order.length; i++) {
      if (i == 0) {
        q.append(" order by ");
      } else {
        q.append(", ");
      }
      q.append("e.").append(order[i]).append(asc[i] ? " asc" : " desc");
    }
    List<Employee> employees = em.createQuery(q.toString(), Employee.class)
      .setParameter("orgId", orgId)
      .setParameter("locked", locked)
      .setFirstResult(start)
      .setMaxResults(count).getResultList();
    return callback.apply(employees);
  }

  public boolean createGroup(String name, String title, Boolean social) {
    final List<Group> groups = em.createNamedQuery("groupByName", Group.class).setParameter("name", name)
      .getResultList();
    final Group group;
    boolean empty = groups.isEmpty();
    if (empty) {
      group = new Group(social);
      group.setName(name);
      group.setTitle(title);
      em.persist(group);
    }
    return empty;
  }

  @Override
  public void deleteGroup(Long id) {
    final Group group = em.find(Group.class, id);
    if (group != null) {
      processEngine.get().getIdentityService().deleteGroup(group.getName());
      em.remove(group);
      Set<Employee> socialEmployees = ImmutableSet.copyOf(group.getEmployees());
      Set<Employee> organizationEmployees = new HashSet<Employee>();
      for (Organization organization : group.getOrganizations()) {
        for (Employee employee : organization.getEmployees()) {
          for (Role role : employee.getRoles()) {
            if (role == Role.Executor) {
              organizationEmployees.add(employee);
            }
          }
        }
      }
      removeUsersFromDeletedGroup(id, socialEmployees);
      removeUsersFromDeletedGroup(id, organizationEmployees);
    }
  }

  private void removeUsersFromDeletedGroup(Long id, Set<Employee> employees) {
    for (Employee employee : employees) {
      Set<String> employeeGroupsAfterDelete = new HashSet<String>();
      for (Group group1 : employee.getGroups()) {
        if (!group1.getId().equals(id)) {
          employeeGroupsAfterDelete.add(group1.getName());
        }
      }
      setUserGroups(employee, employeeGroupsAfterDelete);
    }
  }

  public List<Group> findGroupByName(String name) {
    return em.createNamedQuery("groupByName", Group.class).setParameter("name", name)
      .getResultList();
  }

  public Boolean findUsesInfoSystemService(String sname, String sversion) {
    return em.createQuery("select count(s) from InfoSystemService s where s.sname=:sname and s.sversion=:sversion", Number.class)
      .setParameter("sname", sname).setParameter("sversion", sversion).getSingleResult().intValue() == 0;
  }

  public List<Employee> findAllEmployees() {
    TypedQuery<Employee> query = em.createNamedQuery("findAllEmployees", Employee.class);
    return query.getResultList();
  }

  public List<String> findAllEmployeeLogins() {
    TypedQuery<String> query = em.createNamedQuery("findAllEmployeeLogins", String.class);
    return query.getResultList();
  }

  public void setOrganizationInGroup(Group group, TreeSet<String> twinValue) {
    List<Long> orgIds = findAllOrganizationIds();
    for (Long orgId : orgIds) {
      Set<String> groups = getOrgGroupNames(orgId);
      Boolean change;
      if (twinValue.contains(orgId.toString())) {
        change = groups.add(group.getName());
      } else {
        change = groups.remove(group.getName());
      }
      if (change) {
        setOrgGroupNames(orgId, groups);
      }
    }
  }

  public void setEmloyeeInGroup(Group group, TreeSet<String> twinValue) {
    List<String> empLogins = findAllEmployeeLogins();
    for (String empLogin : empLogins) {
      final UserItem userItem = AdminServiceProvider.get().getUserItem(empLogin);
      Set<String> groups = userItem.getGroups();
      Boolean change;
      if (twinValue.contains(empLogin)) {
        change = groups.add(group.getName());
      } else {
        change = groups.remove(group.getName());
      }
      if (change) {
        userItem.setGroups(groups);
        AdminServiceProvider.get().setUserItem(empLogin, userItem);
      }

    }
  }

  public Bid getBidByTask(String taskId) {
    List<Bid> resultList = em
      .createQuery("select e from Bid e join e.currentSteps c where c in (:taskId)", Bid.class)
      .setParameter("taskId", taskId).getResultList();
    if (resultList.isEmpty()) {
      return null;
    }
    return resultList.get(0);
  }

  public Bid getBidByProcessInstanceId(String processInstanceId) {
    List<Bid> resultList = em
      .createQuery("select e from Bid e where e.processInstanceId  = (:processInstanceId)", Bid.class)
      .setParameter("processInstanceId", processInstanceId).getResultList();
    if (resultList.isEmpty()) {
      return null;
    }
    return resultList.get(0);
  }

  public Bid getBid(String bidId) {
    if (StringUtils.isEmpty(bidId)) {
      return null;
    }
    return em.find(Bid.class, Long.parseLong(bidId));
  }

  public Set<Organization> getRootOrganizations() {
    Set<Organization> root = new HashSet<Organization>();
    List<Organization> all = findAllOrganizations();
    for (Organization org : all) {
      if (org.getParent() == null) {
        root.add(org);
      }
    }
    return root;
  }

  @Override
  public int countInfoSystems(boolean source) {
    final CriteriaBuilder _ = em.getCriteriaBuilder();
    final CriteriaQuery<Number> query = _.createQuery(Number.class);
    final Root<InfoSystem> infoSystems = query.from(InfoSystem.class);
    if (source) {
      query.where(_.equal(infoSystems.get(InfoSystem_.source), true));
    }
    return count(query.select(_.count(infoSystems)));
  }

  @Override
  public List<InfoSystem> queryInfoSystems(boolean source, String[] sort, boolean[] asc, int start, int count) {
    final CriteriaBuilder c = em.getCriteriaBuilder();
    final CriteriaQuery<InfoSystem> query = c.createQuery(InfoSystem.class);
    final Root<InfoSystem> system = query.from(InfoSystem.class);
    if (source) {
      query.where(c.equal(system.get(InfoSystem_.source), true));
    }
    query.select(system);
    if (sort != null) {
      final Order[] orders = new Order[sort.length];
      for (int i = 0; i < sort.length; i++) {
        final Path<String> path = system.get(sort[i]);
        orders[i] = asc[i] ? c.asc(path) : c.desc(path);
      }
      query.orderBy(orders);
    }
    return chunk(start, count, query);
  }

  @Override
  public InfoSystem createInfoSystem(String code, String name, String comment) {
    InfoSystem system = em.find(InfoSystem.class, code);
    if (system == null) {
      system = new InfoSystem(code, name);
    } else {
      system.setName(name);
    }
    system.setComment(comment);
    em.persist(system);
    return system;
  }

  @Override
  public boolean deleteInfoSystem(String code) {
    InfoSystem system = em.find(InfoSystem.class, code);
    if (system != null) {
      Number count = em
        .createQuery("select count(s) from InfoSystemService s where s.infoSystem=:sys or s.source=:sys", Number.class)
        .setParameter("sys", system).getSingleResult();
      if (count.intValue() == 0) {
        em.remove(system);
        singleMain(null);
        return true;
      }
    }
    return false;
  }

  @Override
  public News createNews(String title, String text) {
    News news = new News(title, text);
    em.persist(news);
    assert news.getId() != null;
    assert news.getDateCreated() != null;
    return news;
  }

  @Override
  public void updateNews(Long id, Object title, Object text) {
    News news = em.find(News.class, id);
    if (title != null) {
      news.setTitle((String) title);
    }

    if (text != null) {
      news.setText((String) text);
    }
    em.persist(news);
  }

  @Override
  public void deleteNews(Long itemId) {
    News news = em.find(News.class, itemId);
    em.remove(news);
  }

  @Override
  public List<News> getNews() {
    return em.createNamedQuery("allNews", News.class).setMaxResults(3).getResultList();
  }

  @Override
  public int countInfoSystemServices() {
    final CriteriaBuilder _ = em.getCriteriaBuilder();
    final CriteriaQuery<Number> query = _.createQuery(Number.class);
    final Root<InfoSystemService> infoSystemServices = query.from(InfoSystemService.class);
    return count(query.select(_.count(infoSystemServices)));
  }

  @Override
  public void removeInfoSystemService(long id) {
    InfoSystemService infoSystemService = em.find(InfoSystemService.class, id);
    if (infoSystemService != null) {
      em.remove(infoSystemService);
    }
  }

  @Override
  public List<InfoSystemService> queryInfoSystemServices(final String[] sort, final boolean[] asc, final int start, final int count) {
    final CriteriaBuilder c = em.getCriteriaBuilder();
    final CriteriaQuery<InfoSystemService> query = c.createQuery(InfoSystemService.class);
    final Root<InfoSystemService> service = query.from(InfoSystemService.class);
    query.select(service);
    if (sort != null) {
      final Order[] orders = new Order[sort.length];
      for (int i = 0; i < sort.length; i++) {
        String sortBy = sort[i];
        final Path<String> path;
        if ("infoSystem".equals(sortBy)) {
          path = service.join(InfoSystemService_.infoSystem).get(InfoSystem_.code);
        } else if ("source".equals(sortBy)) {
          path = service.join(InfoSystemService_.source, JoinType.LEFT).get(InfoSystem_.code);
        } else {
          path = service.get(sortBy);
        }
        orders[i] = asc[i] ? c.asc(path) : c.desc(path);
      }
      query.orderBy(orders);
    }
    return chunk(start, count, query);
  }

  @Override
  public Long createInfoSystemService(String infoSysId, String source, String address, String revision, String sname,
                                      String sversion, String name, boolean available, boolean logEnabled) {
    InfoSystem infoSys = em.find(InfoSystem.class, infoSysId);
    InfoSystem sourceSys = null;
    if (source != null) {
      sourceSys = em.find(InfoSystem.class, source);
      if (!sourceSys.isSource()) {
        sourceSys = null;
      }
    }
    InfoSystemService service = new InfoSystemService();
    initService(service, address, revision, sname, sversion, name, available, infoSys);
    service.setLogEnabled(logEnabled);
    service.setSource(sourceSys);
    em.persist(service);
    return service.getId();
  }

  @Override
  public void updateInfoSystemService(String id,
                                      String infoSysId, String source, String address, String revision, String sname,
                                      String sversion, String name, boolean available, boolean logEnabled) {
    InfoSystemService service = em.find(InfoSystemService.class, Long.parseLong(id));
    InfoSystem infoSys = em.find(InfoSystem.class, infoSysId);
    InfoSystem sourceSys = null;
    if (source != null) {
      sourceSys = em.find(InfoSystem.class, source);
      if (!sourceSys.isSource()) {
        sourceSys = null;
      }
    }
    initService(service, address, revision, sname, sversion, name, available, infoSys);
    service.setLogEnabled(logEnabled);
    service.setSource(sourceSys);
    em.merge(service);
  }

  public List<InfoSystemService> getInfoSystemServiceBySName(String name) {
    return em.createQuery
      (
        "select s from InfoSystemService s where s.sname=:servName and s.available = true",
        InfoSystemService.class
      )
      .setParameter("servName", name)
      .getResultList();
  }

  private void initService(InfoSystemService service, String address, String revision, String sname, String sversion, String name,
                           boolean available, InfoSystem infoSys) {
    service.setInfoSystem(infoSys);
    service.setAddress(address);
    service.setRevision(revision);
    service.setSname(sname);
    service.setSversion(sversion);
    service.setName(name);
    service.setAvailable(available);
  }

  private int count(final CriteriaQuery<Number> query) {
    return em.createQuery(query).getSingleResult().intValue();
  }

  private <T> List<T> chunk(int start, int count, final CriteriaQuery<T> query) {
    return em.createQuery(query).setFirstResult(start).setMaxResults(count).getResultList();
  }

  @Override
  public InfoSystem getMainInfoSystem() {
    List<InfoSystem> list = em.createQuery("select e from InfoSystem e where e.main = true", InfoSystem.class)
      .setMaxResults(1)
      .getResultList();
    if (list.isEmpty()) {
      return null;
    }
    return list.get(0);
  }

  @Override
  public void createLog(Actor actor,
                        String entityName,
                        String entityId,
                        String action,
                        String info,
                        boolean actionResult) {
    Log log = new Log(actor, entityName, entityId, action, info, actionResult);
    emLog.persist(log);
  }


  @Override
  public Actor createActor() {
    return Flash.getActor();
  }

  @TransactionAttribute(NOT_SUPPORTED)
  @Override
  public List<TRef<Client>> getClientRefs() {
    return registry.getClientRefs();
  }

  @TransactionAttribute(NOT_SUPPORTED)
  @Override
  public TRef<Client> getClientRefByNameAndVersion(String name, String version) {
    return registry.getClientByNameAndVersion(name, version);
  }

  @Override
  public ExternalGlue getGlueByBidId(String bidId) {
    return em.createQuery
      (
        "select s from ExternalGlue s where s.bidId=:bidId",
        ExternalGlue.class
      )
      .setParameter("bidId", bidId)
      .getSingleResult();
  }

  @Override
  public ExternalGlue getGlueByProcessInstanceId(String processInstanceId) {
    return nullOrFirst(em.createQuery
        (
          "select s from ExternalGlue s where s.processInstanceId=:processInstanceId",
          ExternalGlue.class
        )
        .setParameter("processInstanceId", processInstanceId)
        .getResultList()
    );
  }

  private <T> T nullOrFirst(List<T> resultList) {
    if (resultList == null || resultList.isEmpty()) {
      return null;
    }
    return resultList.get(0);
  }

  @Override
  @TransactionAttribute(REQUIRED)
  public void saveServiceResponse(ServiceResponseEntity response, List<Enclosure> enclosures, Map<Enclosure, String[]> enclosureToId) {
    List<ServiceResponseEntity> resultList = em.createQuery(
      "select s from ServiceResponseEntity s where s.bid=:bidId and s.status=:status", ServiceResponseEntity.class)
      .setParameter("bidId", response.getBid())
      .setParameter("status", response.status)
      .getResultList();
    for (final ServiceResponseEntity serviceResponseEntity : resultList) {
      em.remove(serviceResponseEntity);
    }
    em.persist(response);
    if (enclosures != null && !enclosures.isEmpty()) {

      // сбросить изменения
      Context.getCommandContext().getDbSqlSession().flush();

      for (Enclosure enclosure : enclosures) {
        if (enclosure.content == null || StringUtils.isEmpty(enclosure.zipPath)) {
          logger.info("skip invalid " + enclosure);
          continue;
        }
        if (enclosureToId.containsKey(enclosure)) {
          String[] idAndVar = enclosureToId.get(enclosure);
          String id = idAndVar[0];
          String var = idAndVar[1];
          Attachment attachment = new GetAttachmentCmd(id).execute(Context.getCommandContext());
          if (attachment == null) {
            logger.info("invalid attachment " + id + " for " + enclosure);
          } else {
            EnclosureEntity enclosureEntity = new EnclosureEntity(
              response, id, var, enclosure.fileName, enclosure.code, enclosure.number, enclosure.zipPath);
            response.getEnclosures().add(enclosureEntity);
            em.persist(enclosureEntity);
            logger.info("persist " + enclosureEntity);
          }
        } else {
          logger.info("skip out-of-process " + enclosure);
        }
      }
    }
  }

  @Override
  public ProcedureProcessDefinition getProcedureProcessDefinitionByProcedureCode(long procedureCode) {
    List<ProcedureProcessDefinition> resultList = em.createQuery("select e from procedure_process_definition e where e.procedure.registerCode =:procedureCode and e.status =:status", ProcedureProcessDefinition.class)
      .setParameter("procedureCode", procedureCode).setParameter("status", DefinitionStatus.Work).getResultList();

    if (resultList == null || resultList.isEmpty()) {
      return null;
    }
    return resultList.get(0);
  }

  @Override
  public int countOfServerResponseByBidIdAndStatus(long bidId, String status) {
    return em
      .createQuery(
        "select count(e) from ServiceResponseEntity e where e.bid.id =:bidId and e.status=:status",
        Number.class
      )
      .setParameter("bidId", bidId)
      .setParameter("status", status)
      .getSingleResult().intValue();
  }

  /**
   * Точка формирования транзакции.
   */
  @TransactionAttribute(REQUIRED)
  @Override
  public ServerResponse getServerResponseByBidIdAndStatus(long bid1, long bidId, String status) {
    final List<ServiceResponseEntity> entities = em.createQuery
      (
        "select e from ServiceResponseEntity e where e.bid.id =:bidId and e.status=:status",
        ServiceResponseEntity.class
      )
      .setParameter("bidId", bidId)
      .setParameter("status", status)
      .getResultList();
    if (entities.isEmpty()) {
      // no response
      return null;
    }
    final ServiceResponseEntity responseEntity = entities.get(0);
    final String processInstanceId = responseEntity.getBid().getProcessInstanceId();
    logger.info("response with " + responseEntity.getEnclosures().size() + " enclosures");
    ProcessEngine engine = processEngine.get();
    RuntimeService runtimeService = engine.getRuntimeService();
    final CommandExecutor commandExecutor = ((ServiceImpl) runtimeService).getCommandExecutor();
    final List<Enclosure> attachments = commandExecutor.execute(new Command<List<Enclosure>>() {
      @Override
      public List<Enclosure> execute(final CommandContext commandContext) {
        ExecutionEntity execution = commandContext
          .getExecutionManager()
          .findExecutionById(processInstanceId);
        if (execution == null) {
          logger.info("no runtime instance " + processInstanceId + ", signatures will be used from history");
        }
        final List<Enclosure> enclosures = new ArrayList<Enclosure>();
        for (final EnclosureEntity enclosureEntity : responseEntity.getEnclosures()) {
          String varName = enclosureEntity.getVar();
          logger.info("load attachment " + varName + " with id " + enclosureEntity.getId());
          Enclosure enclosure = Activiti.createEnclosureInCommandContext(enclosureEntity.getId(), processInstanceId, varName);
          if (enclosure == null) {
            logger.warning("not found attachment with id " + enclosureEntity.getId());
          } else {
            enclosure.number = enclosureEntity.getNumber();
            enclosure.code = enclosureEntity.getCode();
            enclosure.fileName = enclosureEntity.getFileName();
            // возможность задания пути:
            if (enclosureEntity.getZipPath() != null) {
              enclosure.zipPath = enclosureEntity.getZipPath();
            }
            enclosures.add(enclosure);
          }
        }
        return enclosures;
      }
    });
    final ServerResponse serverResponse = responseEntity.getServerResponse();
    serverResponse.attachmens = attachments;
    return serverResponse;
  }

  @TransactionAttribute(NOT_SUPPORTED)
  @Override
  public EntityManagerFactory getLogPU() {
    return emfLog;
  }

  @TransactionAttribute(NOT_SUPPORTED)
  @Override
  public EntityManagerFactory getMyPU() {
    return emf;
  }

  public int countOfBidByEmail(String login) {
    return em.createQuery("select count(distinct s.bid.id) from BidWorkers s where s.employee.login=:login",
      Number.class).setParameter("login", login).getSingleResult().intValue();
  }

  public List<Bid> bidsByLogin(String login, final int start, final int count) {
    return em.createQuery("select distinct(s.bid) from BidWorkers s where s.employee.login=:login",
      Bid.class).setParameter("login", login).setFirstResult(start).setMaxResults(count).getResultList();
  }

  @TransactionAttribute(NOT_SUPPORTED)
  @Override
  public ServiceDefinitionParser getServiceDefinitionParser() {
    return serviceDefinitionParser;
  }

  @Override
  public int countServiceUnavailableByInfoSystem(long id) {
    return em.createQuery("select count(s.id) from ServiceUnavailable s where s.infoSystemService.id=:id",
      Number.class).setParameter("id", id).getSingleResult().intValue();
  }

  @Override
  public List<ServiceUnavailable> queryServiceUnavailableByInfoSystem(long id, int start, int count) {
    return em.createQuery("select s from ServiceUnavailable s where s.infoSystemService.id=:id",
      ServiceUnavailable.class).setParameter("id", id).setFirstResult(start).setMaxResults(count).getResultList();
  }

  @Override
  @TransactionAttribute(REQUIRES_NEW)
  public void saveServiceUnavailable(InfoSystemService curService) {
    ServiceUnavailable serviceUnavailable = new ServiceUnavailable(curService);
    em.merge(serviceUnavailable);
  }

  @Override
  @TransactionAttribute(REQUIRES_NEW)
  public String getSystemProperty(String key) {
    SystemProperty property = em.find(SystemProperty.class, key);
    if (property == null) {
      return null;
    }
    return property.getValue();
  }

  @Override
  @TransactionAttribute(REQUIRES_NEW)
  public void saveSystemProperty(String key, String value) {
    SystemProperty property = em.find(SystemProperty.class, key);
    if (property == null) {
      property = new SystemProperty();
      property.setKey(key);
    }
    property.setValue(value);
    em.persist(property);
  }

  private class OrgBySearchNamePredicate implements Predicate<Organization> {
    private String orgName;

    public OrgBySearchNamePredicate(String orgName) {
      this.orgName = orgName;
    }

    @Override
    public boolean apply(Organization input) {
      return input != null && input.getName().equals(this.orgName);
    }
  }

  private class OrgSearchByIdPredicate implements Predicate<Organization> {
    private Long orgId;

    public OrgSearchByIdPredicate(Long orgId) {
      this.orgId = orgId;
    }

    @Override
    public boolean apply(Organization input) {
      return input != null && input.getId().equals(this.orgId);
    }
  }

  private class EmployeeByLoginPredicate implements Predicate<Employee> {
    private String login;

    public EmployeeByLoginPredicate(String login) {
      this.login = login;
    }

    @Override
    public boolean apply(Employee input) {
      return input != null && input.getLogin().equals(login);
    }
  }

  @Override
  public void toggleSource(String code, boolean source) {
    InfoSystem system = em.find(InfoSystem.class, code);
    if (system != null) {
      system.setSource(source);
      if (!source) {
        system.setMain(false);
        List<InfoSystemService> services = em
          .createQuery("select e from InfoSystemService e where e.source=:sys", InfoSystemService.class)
          .setParameter("sys", system).getResultList();
        for (InfoSystemService service : services) {
          service.setSource(null);
          em.persist(service);
        }
      }
      em.persist(system);
      singleMain(null);
    }
  }

  @Override
  public void toggleMain(String code, boolean main) {
    InfoSystem system = em.find(InfoSystem.class, code);
    if (system != null) {
      system.setSource(true);
      em.persist(system);
      singleMain(system);
    }
  }

  void singleMain(InfoSystem newChecked) {
    InfoSystem source = null;
    InfoSystem checked = null;
    List<InfoSystem> systems = em.createQuery("select s from InfoSystem s", InfoSystem.class).getResultList();
    for (InfoSystem system : systems) {
      if (system.isSource()) {
        if (source == null) {
          source = system;
        }
        if (system.isMain()) {
          checked = system;
        }
      }
      system.setMain(false);
      em.persist(system);
    }
    if (source != null) {
      if (newChecked != null) {
        checked = newChecked;
      } else if (checked == null) {
        checked = source;
      }
      checked.setMain(true);
      em.persist(checked);
    }
  }
}