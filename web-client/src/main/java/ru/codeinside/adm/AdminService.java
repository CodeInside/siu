/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm;

import com.google.common.base.Function;
import org.activiti.engine.ProcessEngine;
import ru.codeinside.adm.database.*;
import ru.codeinside.gses.webui.gws.TRef;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ServerResponse;
import ru.codeinside.gws.api.ServiceDefinitionParser;
import ru.codeinside.log.Actor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@RolesAllowed("Administrator")
public interface AdminService {

  /**
   * контракт создания.
   */
  Object afterCreate();

  /**
   * контракт разрушения. Вызывающий провайдер обычно Startup-бин и транзакции в нём доступны.
   */
  void preDestroy(Object ticket);

  public Organization createOrganization(String name, String login, Organization parent);

  public List<Organization> findAllOrganizations();

  public Organization findOrganizationById(Long id);

  public void updateOrganization(Organization organization);

  public Employee createEmployee(String login, String password, String fio, Set<Role> roles, String creator,
                                 long orgId, TreeSet<String> groupExecutor, TreeSet<String> groupSupervisorEmp,
                                 TreeSet<String> groupSupervisorOrg);

  public Employee createEmployee(String login, String password, String fio, Set<Role> roles, String creator,
                                 long orgId);

  @PermitAll
  public Employee findEmployeeByLogin(String login);

  public Set<String> getOrgGroupNames(long orgId);

  public Set<String> getOrgGroupNames();

  public Set<String> getEmpGroupNames();

  @PermitAll
  public void init();

  public boolean setOrgGroupNames(long orgId, Set<String> names);

  public UserItem getUserItem(String login);

  public void setUserItem(String login, UserItem userItem);

  public int getEmployeesCount(long orgId, boolean locked);

  public <T> T withEmployee(long orgId, String login, Function<Employee, T> callback);

  public <T> T withEmployee(String login, Function<Employee, T> callback);

  public <T> T withEmployees(long orgId, boolean locked, int start, int count, String[] order, boolean[] asc,
                             Function<List<Employee>, T> callback);

  // TODO: вытащить в отдельный сервис генерации данных?
  public boolean loadProcessFixtures(ProcessEngine engine, InputStream is) throws IOException;

  public boolean createGroup(String name, String title, Boolean social);

  public void deleteGroup(Long id);

  public List<Group> findGroupByName(String name);

  public Boolean findUsesInfoSystemService(String sname, String sversion);

  public List<Employee> findAllEmployees();

  public void setOrganizationInGroup(Group group, TreeSet<String> twinValue);

  public void setEmloyeeInGroup(Group group, TreeSet<String> twinValue);

  // TODO определится где расположить метод
  public Bid getBidByTask(String taskId);

  public Bid getBid(String bidId);

  public Bid getBidByProcessInstanceId(String processInstanceId);

  public List<Group> selectGroupsBySocial(boolean social);

  public Set<String> selectGroupNamesBySocial(boolean social);

  public Set<Organization> getRootOrganizations();

  public int countInfoSystems();

  public List<InfoSystem> queryInfoSystems(String[] sort, boolean[] asc, int start, int count);

  InfoSystem createInfoSystem(String code, String value);

  public News createNews(String title, String text);

  public void updateNews(Long obj1, Object obj2, Object obj3);

  public void deleteNews(Long id);

  public List<News> getNews();

  public int countInfoSystemServices();

  public List<InfoSystemService> queryInfoSystemServices(String[] sort, boolean[] asc, int start, int count);

  void updateInfoSystemService(String id, String infoSysId, String address, String revision, String sname, String sversion, String name, boolean available);

  Long createInfoSystemService(String infoSysId, String address, String revision, String sname, String sversion, String name, boolean available);

  public List<InfoSystemService> getInfoSystemServiceBySName(String name);

  public InfoSystem getInfoSystemByCode(String code);

  public Set<Group> getControlledOrgGroupsOf(String login);

  public Set<Group> getControlledEmpGroupsOf(String login);

  List<Employee> getOrgGroupMembers(String groupName, String taskId, int startIndex, int count);

  List<Employee> getEmpGroupMembers(String groupName, String taskId, int startIndex, int count);

  int getOrgGroupMembersCount(String groupName, String taskId);

  int getEmpGroupMembersCount(String groupName, String taskId);

  ClientRequestEntity getClientRequestEntity(long id);

  public void createLog(Actor actor,
                        String entityName,
                        String entityId,
                        String action,
                        String info,
                        boolean actionResult);


  String getSystemProperty(String key);

  void saveSystemProperty(String key, String value);

  public Actor createActor();

  List<TRef<Client>> getClientRefs();

  TRef<Client> getClientRefByNameAndVersion(String name, String version);

  ExternalGlue getGlueByBidId(String bidId);

  ExternalGlue getGlueByProcessInstanceId(String processInstanceId);

  void saveServiceResponse(ServiceResponseEntity response, List<Enclosure> enclosures, Map<Enclosure, String[]> usedEnclosures);

  int countOfServerResponseByBidIdAndStatus(String bidId, String status);

  ServerResponse getServerResponseByBidIdAndStatus(String bidId, String status);

  ExternalGlue createExternalGlue(String processInstanceId, String requestIdRef, String name);

  ExternalGlue createNotReadyExternalGlue(Bid declarantBid, String requestIdRef, String name);

  ProcedureProcessDefinition getProcedureProcessDefinitionByProcedureCode(long procedureCode);

  ExternalGlue getGlueByRequestIdRef(String requestIdRef);


  EntityManagerFactory getLogPU();

  EntityManagerFactory getMyPU();

  int countOfBidByEmail(String login);

  List<Bid> bidsByLogin(String login, final int startIndex, final int count);


  ServiceDefinitionParser getServiceDefinitionParser();

  int countServiceUnavailableByInfoSystem(long id);

  List<ServiceUnavailable> queryServiceUnavailableByInfoSystem(long id, int start, int count);

  void saveServiceUnavailable(InfoSystemService curService);

  void setUserGroups(Employee e, Set<String> names);

  void loadEmployeeData(InputStream data, String currentUserName) throws IOException;

  @RolesAllowed({"Executor", "Supervisor"})
  void saveBidAssignment(ProcessEngine engine, String superProcessId, String toLogin);

  ExternalGlue getGlueById(Long id);
}