/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm;

import com.google.common.base.Function;
import com.vaadin.addon.jpacontainer.filter.util.AdvancedFilterableSupport;
import org.activiti.engine.ProcessEngine;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.ClientRequestEntity;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.adm.database.ExternalGlue;
import ru.codeinside.adm.database.Group;
import ru.codeinside.adm.database.InfoSystem;
import ru.codeinside.adm.database.InfoSystemService;
import ru.codeinside.adm.database.News;
import ru.codeinside.adm.database.Organization;
import ru.codeinside.adm.database.ProcedureProcessDefinition;
import ru.codeinside.adm.database.Role;
import ru.codeinside.adm.database.ServiceResponseEntity;
import ru.codeinside.adm.database.ServiceUnavailable;
import ru.codeinside.adm.database.TaskDates;
import ru.codeinside.calendar.DueDateCalculator;
import ru.codeinside.gses.activiti.Pair;
import ru.codeinside.gses.webui.gws.TRef;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ServerResponse;
import ru.codeinside.gws.api.ServiceDefinitionParser;
import ru.codeinside.log.Actor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
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

  public List<Organization> findOrganizationIdsByName(String name);

  public void updateOrganization(Organization organization);

  public Employee createEmployee(String login, String password, String fio, String snils, Set<Role> roles, String creator,
                                 long orgId, TreeSet<String> groupExecutor, TreeSet<String> groupSupervisorEmp,
                                 TreeSet<String> groupSupervisorOrg);

  public Employee createEmployee(String login, String password, String fio, String snils, Set<Role> roles, String creator,
                                 long orgId);

  @PermitAll
  public Employee findEmployeeByLogin(String login);

  @PermitAll
  public Employee findEmployeeBySnils(String snils);

  @PermitAll
  public boolean isUniqueSnils(String login, String snils);

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

  public boolean loadProcessFixtures(ProcessEngine engine, InputStream is) throws IOException;

  public boolean createGroup(String name, String title, Boolean social);

  public void deleteGroup(Long id);

  public List<Group> findGroupByName(String name);

  public Boolean findUsesInfoSystemService(String sname, String sversion);

  public List<Employee> findAllEmployees();

  public void setOrganizationInGroup(Group group, TreeSet<String> twinValue);

  public void setEmloyeeInGroup(Group group, TreeSet<String> twinValue);

  public Bid getBidByTask(String taskId);

  public Bid getBid(String bidId);

  public Bid getBidByProcessInstanceId(String processInstanceId);

  public TaskDates getTaskDatesByTaskId(String taskId);

  public Set<String> selectGroupNamesBySocial(boolean social);

  public List<Organization> getRootOrganizations();

  public int countInfoSystems(boolean source);

  public List<InfoSystem> queryInfoSystems(boolean source, String[] sort, boolean[] asc, int start, int count);

  InfoSystem createInfoSystem(String code, String value, String comment);

  public News createNews(String title, String text);

  public void updateNews(Long obj1, Object obj2, Object obj3);

  public void deleteNews(Long id);

  public List<News> getNews();

  public void removeInfoSystemService(long id);

  void updateInfoSystemService(String id, String infoSysId, String source, String address,
                               String revision, String sname, String sversion, String name,
                               boolean available, boolean logEnabled);

  Long createInfoSystemService(String infoSysId, String source, String address, String revision, String sname,
                               String sversion, String name, boolean available, boolean logEnabled);

  public List<InfoSystemService> getInfoSystemServiceBySName(String name);

  public InfoSystem getMainInfoSystem();

  boolean deleteInfoSystem(String code);

  public List<Group> getControlledOrgGroupsOf(String login, int startIndex, int count, String[] order, boolean[] asc, AdvancedFilterableSupport newSender);

  public List<Group> getControlledEmpGroupsOf(String login, int startIndex, int count, String[] order, boolean[] asc, AdvancedFilterableSupport newSender);

  int getControlledOrgGroupsCount(String login, AdvancedFilterableSupport newSender);

  int getControlledEmpGroupsCount(String login, AdvancedFilterableSupport newSender);

  List<Employee> getOrgGroupMembers(String groupName, String taskId, int startIndex, int count);

  List<Employee> getEmpGroupMembers(String groupName, String taskId, int startIndex, int count);

  int getOrgGroupMembersCount(String groupName, String taskId);

  int getEmpGroupMembersCount(String groupName, String taskId);

  long saveClientRequestEntity(ClientRequestEntity entity);

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

  ExternalGlue getGlueByProcessInstanceId(String processInstanceId);

  void saveServiceResponse(ServiceResponseEntity response, List<Enclosure> enclosures, Map<Enclosure, String[]> usedEnclosures);

  int countOfServerResponseByBidIdAndStatus(long bid, String status);

  ServiceResponseEntity getServerResponseEntity(long bidId, String status);

  ServerResponse getServerResponseByBidIdAndStatus(long bid1, long bid, String status);

  ProcedureProcessDefinition getProcedureProcessDefinitionByProcedureCode(long procedureCode);

  Long getProcedureCodeByProcessDefinitionId(String processDefinitionId);

  int countOfBidByEmail(String login, AdvancedFilterableSupport newSender);

  List<Bid> bidsByLogin(String login, final int startIndex, final int count, String[] order, boolean[] asc, AdvancedFilterableSupport newSender);

  ServiceDefinitionParser getServiceDefinitionParser();

  int countServiceUnavailableByInfoSystem(long id);

  List<ServiceUnavailable> queryServiceUnavailableByInfoSystem(long id, int start, int count);

  void saveServiceUnavailable(InfoSystemService curService);

  void loadEmployeeData(InputStream data, String currentUserName) throws IOException;

  @RolesAllowed({"Executor", "Supervisor"})
  void saveBidAssignment(ProcessEngine engine, String superProcessId, String toLogin);

  void toggleSource(String code, boolean source);

  void toggleMain(String code, boolean main);

  /**
   * Возвращает калькулятор периодов который исчисляются в рабочих или календарных днях
   *
   * @param business - рабочие дни
   * @return экземпляр DueDateCalculator
   */
  DueDateCalculator getCalendarBasedDueDateCalculator(boolean business);

  /**
   * Выполняет импорт спраочника рабочих дней
   *
   * @param inputStream поток данных из справочника
   * @return пару количества обновленных заявок и этапов
   */
  Pair<Integer, Integer> importBusinessCalendar(InputStream inputStream) throws IOException, ParseException;

  /**
   * Удаляет дату из справочника рабочих дней
   *
   * @param dateForRemove дата для удаления
   * @return пару количества обновленных заявок и этапов
   */
  Pair<Integer, Integer> deleteDateFromBusinessCalendar(Date dateForRemove);
}