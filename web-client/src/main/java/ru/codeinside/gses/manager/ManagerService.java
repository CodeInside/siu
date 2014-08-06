/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.manager;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.vaadin.addon.jpacontainer.filter.util.AdvancedFilterableSupport;
import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Between;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.SimpleStringFilter;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.adm.database.DefinitionStatus;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.adm.database.Procedure;
import ru.codeinside.adm.database.ProcedureProcessDefinition;
import ru.codeinside.adm.database.ProcedureType;
import ru.codeinside.adm.database.Service;
import ru.codeinside.adm.parser.ServiceFixtureParser;
import ru.codeinside.gses.webui.Flash;

import javax.ejb.DependsOn;
import javax.ejb.EJBException;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static javax.ejb.TransactionAttributeType.REQUIRED;
import static javax.ejb.TransactionManagementType.CONTAINER;

@TransactionManagement(CONTAINER)
@Singleton
@DependsOn("BaseBean")
@Lock(LockType.READ)
public class ManagerService {

  @PersistenceContext(unitName = "myPU")
  EntityManager em;

  /**
   * Внимание! Не может регистрировать сам себя!
   */
  transient static ManagerService instance;

  public static ManagerService get() {
    if (instance == null) {

      throw new IllegalStateException("Сервис не зарегистрирован!");
    }
    return instance;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public Procedure createProcedure(String name, String description, String serviceId, Long code, String login, ProcedureType type) {
    try {
      if (hasProcedureWithSameRegisterCode(code)) {
        throw new RuntimeException("Процедура с таким кодом уже существует");
      }
      final Procedure procedure = new Procedure();
      mergeProcedure(procedure, name, description, serviceId, code, type);
      procedure.setCreator(em.find(Employee.class, login));
      em.persist(procedure);
      return procedure;
    } catch (EJBException e) {
      final String message;
      if (e.getMessage() != null) {
        message = e.getMessage();
      } else if (e.getCausedByException() != null) {
        message = e.getCausedByException().getMessage();
      } else {
        message = e.getCause() != null ? e.getCause().getMessage() : e.getClass().toString();
      }
      throw new RuntimeException(message);
    }
  }

  private void mergeProcedure(final Procedure procedure, String name, String description, String serviceId, Long code, ProcedureType type) {
    procedure.setName(name);
    procedure.setDescription(description);
    if (serviceId != null) {
      procedure.setService(em.find(Service.class, Long.parseLong(serviceId)));
    }
    if (type != null) {
      procedure.setType(type);
    }
    procedure.setRegisterCode(code);
  }

  private boolean hasProcedureWithSameRegisterCode(Long code) {
    if (code == null) {
      return false;
    }
    int countWithCode = em.createQuery("select p from Procedure p where p.registerCode=:registerCode ", Procedure.class).setParameter("registerCode", code).getResultList().size();
    return countWithCode != 0;
  }

  private boolean hasServiceWithSameRegisterCode(Long code) {
    if (code == null) {
      return false;
    }
    return em.createQuery("select count(p) from Service p where p.registerCode=:registerCode ", Long.class).setParameter("registerCode", code).getSingleResult() > 0;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void updateProcedure(String id, String name, String description, String serviceId, Long code) {
    Procedure procedure = em.find(Procedure.class, Long.parseLong(id));
    if (code != null && !code.equals(procedure.getRegisterCode()) && hasProcedureWithSameRegisterCode(code)) {
      throw new RuntimeException("Процедура с таким кодом уже существует");
    }
    mergeProcedure(procedure, name, description, serviceId, code, null);
    em.persist(procedure);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void updateProcedure(Long id, String name, Long code) {
    Procedure procedure = em.find(Procedure.class, id);
    if (code != null && !code.equals(procedure.getRegisterCode()) && hasProcedureWithSameRegisterCode(code)) {
      throw new RuntimeException("Процедура с таким кодом уже существует");
    }
    mergeProcedure(procedure, name, procedure.getDescription(), null, code, null);
    em.persist(procedure);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public List<Procedure> findAllProcedures() {
    TypedQuery<Procedure> query = em.createNamedQuery("findAllProcedures", Procedure.class);
    return query.getResultList();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public List<Service> findAllServices() {
    TypedQuery<Service> query = em.createNamedQuery("findAllServices", Service.class);
    return query.getResultList();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public int getProcedureCount() {
    return em.createQuery("select count(p) from Procedure p", Number.class).getSingleResult().intValue();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public List<Procedure> getProcedures(int start, int count, String[] order, boolean[] asc, ProcedureType type, AdvancedFilterableSupport newSender) {
    StringBuilder q = new StringBuilder("select p from Procedure p where p.type=:type");
    Set<Timestamp> timestamps = null;
    if (newSender != null) {
      timestamps = procedureQueryFilters(newSender, q);
    }

    for (int i = 0; i < order.length; i++) {
      if (i == 0) {
        q.append(" order by ");
      } else {
        q.append(", ");
      }
      q.append("p.").append(order[i]).append(asc[i] ? " asc" : " desc");
    }

    if (newSender != null) {
      Iterator<Timestamp> iterator = timestamps.iterator();
      if (timestamps.size() == 1) {
        return em.createQuery(q.toString(), Procedure.class).setParameter("type", type).setParameter("value", iterator.next()).setFirstResult(start).setMaxResults(count).getResultList();
      } else if (timestamps.size() == 2) {
        Timestamp next = iterator.next();
        Timestamp next1 = iterator.next();
        return em.createQuery(q.toString(), Procedure.class).setParameter("type", type).setParameter("startValue", next).setParameter("endValue", next1).setFirstResult(start).setMaxResults(count).getResultList();
      }
    }

    return em.createQuery(q.toString(), Procedure.class).setParameter("type", type).setFirstResult(start).setMaxResults(count).getResultList();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public List<Procedure> getProceduresByServiceId(Long serviceId, int start, int count, String[] order, boolean[] asc, AdvancedFilterableSupport newSender) {
    StringBuilder q = new StringBuilder("select p from Procedure p where p.service.id=:serviceId ");

    Set<Timestamp> timestamps = null;
    if (newSender != null) {
      timestamps = procedureQueryFilters(newSender, q);
    }

    for (int i = 0; i < order.length; i++) {
      if (i == 0) {
        q.append(" order by ");
      } else {
        q.append(", ");
      }
      q.append("p.").append(order[i]).append(asc[i] ? " asc" : " desc");
    }

    if (newSender != null) {
      Iterator<Timestamp> iterator = timestamps.iterator();
      if (timestamps.size() == 1) {
        return em.createQuery(q.toString(), Procedure.class).setParameter("serviceId", serviceId).setParameter("value", iterator.next()).setFirstResult(start).setMaxResults(count).getResultList();
      } else if (timestamps.size() == 2) {
        Timestamp next = iterator.next();
        Timestamp next1 = iterator.next();
        return em.createQuery(q.toString(), Procedure.class).setParameter("serviceId", serviceId).setParameter("startValue", next).setParameter("endValue", next1).setFirstResult(start).setMaxResults(count).getResultList();
      }
    }

    return em.createQuery(q.toString(), Procedure.class).setParameter("serviceId", serviceId).setFirstResult(start)
      .setMaxResults(count).getResultList();
  }

  private Set<Timestamp> procedureQueryFilters(AdvancedFilterableSupport newSender, StringBuilder q) {

    Set result = new HashSet();
    for (Container.Filter filter : newSender.getFilters()) {
      if (filter instanceof Between) {
        String field = ((Between) filter).getPropertyId().toString();
        result.add(new Timestamp(((Date) ((Between) filter).getStartValue()).getTime()));
        result.add(new Timestamp(((Date) ((Between) filter).getEndValue()).getTime()));
        q.append(" and p." + field + "  >= :startValue and p." + field + " <= :endValue");
      } else if (filter instanceof Compare.GreaterOrEqual) {
        String field = ((Compare.GreaterOrEqual) filter).getPropertyId().toString();
        result.add(new Timestamp(((Date) ((Compare.GreaterOrEqual) filter).getValue()).getTime()));
        q.append(" and p." + field + "  >= :value");
      } else if (filter instanceof Compare.LessOrEqual) {
        String field = ((Compare.LessOrEqual) filter).getPropertyId().toString();
        result.add(new Timestamp(((Date) ((Compare.LessOrEqual) filter).getValue()).getTime()));
        q.append(" and p." + field + " <= :value");
      } else {
        String field = ((SimpleStringFilter) filter).getPropertyId().toString();
        String value = ((SimpleStringFilter) filter).getFilterString();
        if (field.equals("name") || field.equals("description") || field.equals("status") || field.equals("version")) {
          q.append(" and lower(p." + field + ") LIKE '" + value + "%'");
        } else if (field.equals("id")) {
          if (checkString(value)) {
            q.append(" and p." + field + " = '" + value + "'");
          }
        }
      }
    }
    return result;
  }

  private Set<Timestamp> serviceQueryFilters(AdvancedFilterableSupport newSender, StringBuilder q) {

    Set result = new HashSet();
    for (Container.Filter filter : newSender.getFilters()) {
      if (filter instanceof Between) {
        String field = ((Between) filter).getPropertyId().toString();
        result.add(new Timestamp(((Date) ((Between) filter).getStartValue()).getTime()));
        result.add(new Timestamp(((Date) ((Between) filter).getEndValue()).getTime()));
        q.append(" where s." + field + "  >= :startValue and s." + field + " <= :endValue");
      } else if (filter instanceof Compare.GreaterOrEqual) {
        String field = ((Compare.GreaterOrEqual) filter).getPropertyId().toString();
        result.add(new Timestamp(((Date) ((Compare.GreaterOrEqual) filter).getValue()).getTime()));
        q.append(" where s." + field + "  >= :value");
      } else if (filter instanceof Compare.LessOrEqual) {
        String field = ((Compare.LessOrEqual) filter).getPropertyId().toString();
        result.add(new Timestamp(((Date) ((Compare.LessOrEqual) filter).getValue()).getTime()));
        q.append(" where s." + field + " <= :value");
      } else {
        String field = ((SimpleStringFilter) filter).getPropertyId().toString();
        String value = ((SimpleStringFilter) filter).getFilterString();
        if (field.equals("name")) {
          q.append(" where lower(s." + field + ") LIKE '" + value + "%'");
        } else if (field.equals("id")) {
          if (checkString(value)) {
            q.append(" where s." + field + " = '" + value + "'");
          }
        }
      }
    }
    return result;
  }

  public boolean checkString(String string) {
    try {
      Integer.parseInt(string);
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public int getProcedureCountByServiceId(Long serviceId, AdvancedFilterableSupport newSender) {
    StringBuilder q = new StringBuilder("select count(p) from Procedure p where p.service.id=:serviceId ");

    if (newSender != null) {
      Set<Timestamp> timestamps = procedureQueryFilters(newSender, q);
      Iterator<Timestamp> iterator = timestamps.iterator();
      if (timestamps.size() == 1) {
        return em.createQuery(q.toString(), Number.class).setParameter("serviceId", serviceId).setParameter("value", iterator.next()).getSingleResult().intValue();
      } else if (timestamps.size() == 2) {
        Timestamp next = iterator.next();
        Timestamp next1 = iterator.next();
        return em.createQuery(q.toString(), Number.class).setParameter("serviceId", serviceId).setParameter("startValue", next).setParameter("endValue", next1).getSingleResult().intValue();
      }
    }

    return em.createQuery(q.toString(), Number.class)
      .setParameter("serviceId", serviceId).getSingleResult().intValue();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public int getProcedureCount(ProcedureType type, AdvancedFilterableSupport newSender) {
    StringBuilder q = new StringBuilder("select count(p) from Procedure p where p.type=:type");

    if (newSender != null) {
      Set<Timestamp> timestamps = procedureQueryFilters(newSender, q);
      Iterator<Timestamp> iterator = timestamps.iterator();
      if (timestamps.size() == 1) {
        return em.createQuery(q.toString(), Number.class).setParameter("type", type).setParameter("value", iterator.next()).getSingleResult().intValue();
      } else if (timestamps.size() == 2) {
        Timestamp next = iterator.next();
        Timestamp next1 = iterator.next();
        return em.createQuery(q.toString(), Number.class).setParameter("type", type).setParameter("startValue", next).setParameter("endValue", next1).getSingleResult().intValue();
      }
    }

    return em.createQuery(q.toString(), Number.class).setParameter("type", type).getSingleResult().intValue();
  }

  public Procedure getProcedure(String id) {
    return em.createQuery("select p from Procedure p where p.id = :procedureId?", Procedure.class)
      .setParameter("procedureId", Long.parseLong(id)).getSingleResult();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public int getApServiceCount(AdvancedFilterableSupport newSender) {

    StringBuilder q = new StringBuilder("select count(s) from Service s");

    if (newSender != null) {
      Set<Timestamp> timestamps = serviceQueryFilters(newSender, q);
      Iterator<Timestamp> iterator = timestamps.iterator();
      if (timestamps.size() == 1) {
        return em.createQuery(q.toString(), Number.class).setParameter("value", iterator.next()).getSingleResult().intValue();
      } else if (timestamps.size() == 2) {
        Timestamp next = iterator.next();
        Timestamp next1 = iterator.next();
        return em.createQuery(q.toString(), Number.class).setParameter("startValue", next).setParameter("endValue", next1).getSingleResult().intValue();
      }
    }

    return em.createQuery(q.toString(), Number.class).getSingleResult().intValue();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public Service getApService(String id) {
    return em.find(Service.class, Long.parseLong(id));
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public List<Service> getApServices(int start, int count, String[] order, boolean[] asc, AdvancedFilterableSupport newSender) {
    StringBuilder q = new StringBuilder("select s from Service s");

    Set<Timestamp> timestamps = null;
    if (newSender != null) {
      timestamps = serviceQueryFilters(newSender, q);
    }

    for (int i = 0; i < order.length; i++) {
      if (i == 0) {
        q.append(" order by ");
      } else {
        q.append(", ");
      }
      q.append("s.").append(order[i]).append(asc[i] ? " asc" : " desc");
    }

    if (newSender != null) {
      Iterator<Timestamp> iterator = timestamps.iterator();
      if (timestamps.size() == 1) {
        return em.createQuery(q.toString(), Service.class).setParameter("value", iterator.next()).setFirstResult(start).setMaxResults(count).getResultList();
      } else if (timestamps.size() == 2) {
        Timestamp next = iterator.next();
        Timestamp next1 = iterator.next();
        return em.createQuery(q.toString(), Service.class).setParameter("startValue", next).setParameter("endValue", next1).setFirstResult(start).setMaxResults(count).getResultList();
      }
    }

    return em.createQuery(q.toString(), Service.class).setFirstResult(start).setMaxResults(count).getResultList();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public Service createApService(String name, Long code, String login) {
    if (hasServiceWithSameRegisterCode(code)) {
      throw new RuntimeException("Услуга с таким кодом уже существует");
    }
    Service service = new Service();
    service.setCreator(em.find(Employee.class, login));
    service.setName(name);
    service.setRegisterCode(code);
    em.persist(service);
    return service;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public long createApService(String name, Long code, String login, List<String> declarantTypes) {
    if (hasServiceWithSameRegisterCode(code)) {
      return 0;
    }
    Service service = new Service();
    service.setCreator(em.find(Employee.class, login));
    service.setName(name);
    service.setRegisterCode(code);
    service.setDeclarantTypes(declarantTypes);
    em.persist(service);
    return service.getId();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public Boolean updateApservice(String id, String name, Long code, List<String> declarantTypes) {
    Service service = em.find(Service.class, Long.parseLong(id));
    if (code != null && !code.equals(service.getRegisterCode()) && hasServiceWithSameRegisterCode(code)) {
      return true;
    }
    service.setRegisterCode(code);
    service.setName(name);
    service.setDeclarantTypes(declarantTypes);
    em.persist(service);
    return false;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public Service updateApservice(Long id, String name, Long code) {
    Service service = em.find(Service.class, id);
    if (code != null && !code.equals(service.getRegisterCode()) && hasServiceWithSameRegisterCode(code)) {
      throw new RuntimeException("Услуга с таким кодом уже существует");
    }
    service.setRegisterCode(code);
    service.setName(name);
    em.persist(service);
    return service;
  }

  public boolean existProcessDefinitionWithKeyOtherProcedure(String procedureId, String key) {
    int count = em
      .createQuery(
        "select count(e) from procedure_process_definition e where e.procedure.id!=:procedureId and e.processDefinitionKey=:processDefinitionKey and e.child is null ",
        Number.class).setParameter("procedureId", Long.parseLong(procedureId))
      .setParameter("processDefinitionKey", key.trim()).getSingleResult().intValue();
    return count > 0;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public ProcedureProcessDefinition createProcessDefination(String procedureId, ProcessDefinition processDefinition, String login,
                                                            String processDefId) {
    ProcedureProcessDefinition ppd = new ProcedureProcessDefinition();
    ProcedureProcessDefinition processDefenition = StringUtils.isEmpty(processDefId) ? null
      : getProcessDefenition(processDefId);
    if (StringUtils.isEmpty(processDefId)) {
      Double newVersion = 0.00;
      if (getProcessDefenitionCountByProcedureId(procedureId) > 0) {
        newVersion = em
          .createQuery(
            "select max(s.version) from procedure_process_definition s where s.procedure.id=:procedureId",
            Number.class).setParameter("procedureId", Long.parseLong(procedureId))
          .getSingleResult().doubleValue();
      }
      ppd.setVersion(newVersion.intValue() + 1.00);
      ppd.setStatus(DefinitionStatus.Created);
    } else {
      Double version = (processDefenition == null || processDefenition.getVersion() == null) ? 1.00 : processDefenition.getVersion();
      ppd.setVersion(version + 0.01);
      ppd.setStatus(DefinitionStatus.Debugging);
    }
    Procedure procedure = getProcedure(procedureId);

    ppd.setProcedure(procedure);
    ppd.setCreator(em.find(Employee.class, login));
    ppd.setProcessDefinitionId(processDefinition.getId());
    ppd.setProcessDefinitionKey(processDefinition.getKey());
    em.persist(ppd);

    if (!StringUtils.isEmpty(processDefId) && processDefenition != null) {
      processDefenition.setStatus(DefinitionStatus.Archive);
      processDefenition.setChild(ppd);
      em.persist(processDefenition);
    }
    updateProcedureStatusAndVersion(procedureId);
    return ppd;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public boolean updateProcessDefinationStatus(String processDefinitionId, DefinitionStatus newStatus) {
    return updateProcessDefinationStatus(processDefinitionId, newStatus, 0);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public boolean updateProcessDefinationStatus(String processDefinitionId, DefinitionStatus newStatus, int waitOfMinCount) {
    ProcedureProcessDefinition processDefenition = getProcessDefenition(processDefinitionId);
    DefinitionStatus status = processDefenition.getStatus();
    if (!status.getAvailableStatus().contains(newStatus)) {
      return false;
    }
    if (DefinitionStatus.Work.equals(newStatus)) {
      for (ProcedureProcessDefinition ppd : getProcessDefenitionWithStatus(processDefenition, DefinitionStatus.Work)) {
        updateProcessDefinationStatus(ppd.getProcessDefinitionId(), DefinitionStatus.Archive);
      }
    }
    if (DefinitionStatus.Archive.equals(newStatus)) {
      if (Flash.flash().getProcessEngine().getRuntimeService().createProcessInstanceQuery().processDefinitionId(processDefinitionId).count() > waitOfMinCount) {
        newStatus = DefinitionStatus.PathToArchive;
      }
    }
    processDefenition.setStatus(newStatus);
    em.persist(processDefenition);
    updateProcedureStatusAndVersion(processDefenition.getProcedure().getId());
    return true;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void updateProcedureStatusAndVersion(String procedureId) {
    Procedure procedure = getProcedure(procedureId);

    String version = procedure.getVersion();
    String status = procedure.getStatus();

    for (ProcedureProcessDefinition p : getProcessDefenitionsByProcedureId(procedureId, 0, 1, new String[]{
      "status", "version"}, new boolean[]{true, false})) {
      status = p.getStatus().getLabelName();
      version = df.format(p.getVersion());
    }

    procedure.setVersion(version);
    procedure.setStatus(status);
    em.persist(procedure);
  }

  DecimalFormat df = new DecimalFormat("##.00");

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public int getProcessDefenitionCountByProcedureId(String procedureId) {
    return em
      .createQuery(
        "select count(e) from procedure_process_definition e where e.procedure.id=:procedureId and e.child is null",
        Number.class).setParameter("procedureId", Long.parseLong(procedureId)).getSingleResult()
      .intValue();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public List<ProcedureProcessDefinition> getProcessDefenitionsByProcedureId(String procedureId, int start,
                                                                             int count, String[] order, boolean[] asc) {
    StringBuilder q = new StringBuilder(
      "select s from procedure_process_definition s where s.procedure.id=:procedureId and s.child is null");
    for (int i = 0; i < order.length; i++) {
      if (i == 0) {
        q.append(" order by ");
      } else {
        q.append(", ");
      }
      q.append("s.").append(order[i]).append(asc[i] ? " asc" : " desc");
    }
    return em.createQuery(q.toString(), ProcedureProcessDefinition.class)
      .setParameter("procedureId", Long.parseLong(procedureId)).setFirstResult(start).setMaxResults(count)
      .getResultList();
  }

  public ProcedureProcessDefinition getProcessDefenition(String id) {
    return em.find(ProcedureProcessDefinition.class, id);
  }

  public List<ProcedureProcessDefinition> getProcessDefenitionWithStatus(ProcedureProcessDefinition p, DefinitionStatus status) {
    StringBuilder q = new StringBuilder("select s from procedure_process_definition s where s.procedure.id=:procedureId and s.child is null and s.status=:status");
    return em.createQuery(q.toString(), ProcedureProcessDefinition.class).setParameter("procedureId", Long.parseLong(p.getProcedure().getId())).setParameter("status", status).getResultList();
  }

  private class ServSearchByIdPredicate implements Predicate<Service> {
    private Long servId;

    public ServSearchByIdPredicate(Long servId) {
      this.servId = servId;
    }

    @Override
    public boolean apply(Service input) {
      return input != null && input.getId().equals(this.servId);
    }
  }

  @TransactionAttribute(REQUIRED)
  public void loadServiceData(InputStream data, final String currentUserName) throws IOException {
    Preconditions.checkNotNull(data);
    Preconditions.checkArgument(StringUtils.isNotBlank(currentUserName), "User name is blank");
    ServiceFixtureParser parser = new ServiceFixtureParser();
    // загрузка справочников
    final Collection<Service> serviceList = new LinkedList<Service>();
    serviceList.addAll((findAllServices()));
    final Collection<Procedure> procedures = new LinkedList<Procedure>();
    procedures.addAll(findAllProcedures());
    parser.loadFixtures(data, new ServiceFixtureParser.PersistenceCallback() {
      @Override
      public Long onServiceComplete(String srvName, Long regCode) {
        List<Service> searchedSrv = ImmutableList.copyOf(Collections2.filter(serviceList,
          new ServiceByRegCodePredicate(regCode)));
        Long result;
        if (searchedSrv.size() > 0) {
          Service service = updateApservice(searchedSrv.get(0).getId(), srvName, regCode);
          result = service.getId();
        } else {
          Service service = createApService(srvName, regCode, currentUserName);
          serviceList.add(service);
          result = service.getId();
        }
        return result;
      }

      @Override
      public void onProcedureComplete(String name, Long regCode, long servId) {
        List<Service> parentList = ImmutableList.copyOf(Collections2.filter(serviceList, new ServSearchByIdPredicate(servId)));
        Service parent = parentList.size() > 0 ? parentList.get(0) : null;
        if (parent != null) {
          List<Long> children = em.createQuery("select p.id from Procedure p where p.service.id = :service" +
            " and p.registerCode = :regCode", Long.class)
            .setParameter("service", servId)
            .setParameter("regCode", regCode)
            .getResultList();
          if (children.size() > 0) {
            updateProcedure(children.get(0), name, regCode);
          } else {
            List<String> list = em.createQuery("select p.name from Procedure p where p.registerCode = :regCode", String.class)
              .setParameter("regCode", regCode)
              .getResultList();
            if (list.size() > 0) {
              throw new RuntimeException("Процедура с кодом " + regCode + " уже существует: " + list.get(0));
            } else {
              Procedure procedure = createProcedure(name, null, Long.toString(servId), regCode, currentUserName, ProcedureType.Administrative);
              procedures.add(procedure);
            }
          }
        } else {
          throw new IllegalStateException("Для порцедуры " + name + " не найдена услуга.");
        }


      }
    });
  }

  private class ServiceByRegCodePredicate implements Predicate<Service> {
    private Long regCode;

    public ServiceByRegCodePredicate(Long regCode) {
      this.regCode = regCode;
    }

    @Override
    public boolean apply(Service input) {
      return input != null && input.getRegisterCode() != null && input.getRegisterCode().equals(this.regCode);
    }
  }

}
