/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.service.impl;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import ru.codeinside.adm.AdminService;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.BidStatus;
import ru.codeinside.adm.database.BidWorkers;
import ru.codeinside.adm.database.DefinitionStatus;
import ru.codeinside.adm.database.Directory;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.adm.database.ExternalGlue;
import ru.codeinside.adm.database.Procedure;
import ru.codeinside.adm.database.ProcedureProcessDefinition;
import ru.codeinside.adm.database.ProcedureProcessDefinition_;
import ru.codeinside.adm.database.ProcedureType;
import ru.codeinside.adm.database.Procedure_;
import ru.codeinside.adm.database.Service;
import ru.codeinside.adm.database.Service_;
import ru.codeinside.gses.activiti.ActivitiFormProperties;
import ru.codeinside.gses.service.DeclarantService;
import ru.codeinside.gses.webui.form.SubmitStartFormCommand;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.ejb.TransactionManagementType.CONTAINER;

@TransactionManagement(CONTAINER)
@Stateless
public class DeclarantServiceImpl implements DeclarantService {

  public static final String DECLARANT_TYPES = "DeclarantTypes";

  public static final String VAR_SERVICE_ID = "serviceId";
  public static final String VAR_PROCEDURE_TYPE_NAME = "procedureTypeName";
  public static final String VAR_PROCEDURE_ID = "procedureId";
  public static final String VAR_REQUESTER_LOGIN = "declarantLogin";

  @PersistenceContext(unitName = "myPU")
  EntityManager em;

  @Override
  public String createProcess(final ProcessEngine engine, final ProcedureProcessDefinition def, final ActivitiFormProperties properties, String login) {
    return createProcess(null, engine, def, properties, login, false);
  }

  private String createProcess(Bid bid, final ProcessEngine engine, final ProcedureProcessDefinition def, final ActivitiFormProperties properties, String login, boolean fromSmev) {
    engine.getIdentityService().setAuthenticatedUserId(login);

    properties.formPropertyValues.put(VAR_PROCEDURE_TYPE_NAME, Integer.toString(def.getProcedure().getType().ordinal()));
    if (def.getProcedure().getService() != null) {
      properties.formPropertyValues.put(VAR_SERVICE_ID, Long.toString(def.getProcedure().getService().getId()));
    }
    properties.formPropertyValues.put(VAR_PROCEDURE_ID, def.getProcedure().getId());
    properties.formPropertyValues.put(VAR_REQUESTER_LOGIN, login);
    if (def.getProcedure().getService() != null) {
      for (String dt : def.getProcedure().getService().getDeclarantTypes()) {
        Directory directory = em.find(Directory.class, DeclarantServiceImpl.DECLARANT_TYPES);
        final String val;
        if (directory != null && directory.getValues().get(dt) != null) {
          val = directory.getValues().get(dt);
        } else {
          val = dt;
        }
        properties.formPropertyValues.put(dt, val);
      }
    }
    if (bid == null) {
      bid = createDeclarantBid(def.getProcessDefinitionId(), login);
    }

    ProcessInstance processInstance = ((ServiceImpl) engine.getFormService())
      .getCommandExecutor()
      .execute(
        new SubmitStartFormCommand(
          def.getProcessDefinitionId(),
          properties.formPropertyValues,
          properties.getFiles(),
          fromSmev,
          bid.getId()));

    String processInstanceId = processInstance.getId();

    List<Task> tasks = engine.getTaskService().createTaskQuery().processInstanceId(processInstanceId).list();

    createOrUpdateBid(bid, def, tasks, processInstance, em.find(Employee.class, login), login);

    return processInstanceId;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  @Override
  public String createProcess(ProcessEngine engine, String processDefinitionId, ActivitiFormProperties properties, String login) {
    ProcedureProcessDefinition procedureProcessDefinition = em.getReference(ProcedureProcessDefinition.class, processDefinitionId);
    return createProcess(engine, procedureProcessDefinition, properties, login);
  }

  @Override
  public String declareProcess(ProcessEngine processEngine, String processDefinitionId, ActivitiFormProperties properties) {
    ProcedureProcessDefinition procedureProcessDefinition = em.getReference(ProcedureProcessDefinition.class, processDefinitionId);
    return createProcess(null, processEngine, procedureProcessDefinition, properties, "smev", true);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  @Override
  public ExternalGlue declareProcess(String requestIdRef, String name, ProcessEngine engine, String processDefinitionId, ActivitiFormProperties properties, String login) {
    AdminService adminService = AdminServiceProvider.get();
    Bid declarantBid = createDeclarantBid(processDefinitionId, login);
    final ExternalGlue glue = adminService.createNotReadyExternalGlue(declarantBid, requestIdRef, name);
    properties.formPropertyValues.put("glueId", glue.getId().toString());

    ProcedureProcessDefinition procedureProcessDefinition = em.getReference(ProcedureProcessDefinition.class, processDefinitionId);
    String processInstanceId = createProcess(declarantBid, engine, procedureProcessDefinition, properties, login, true);

    glue.setProcessInstanceId(processInstanceId);
    em.merge(glue);
    return glue;
  }


  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  @Override
  public Bid createDeclarantBid(String processDefinitionId, String declarant) {
    Employee curEmployee = em.find(Employee.class, declarant);
    ProcedureProcessDefinition def = em.getReference(ProcedureProcessDefinition.class, processDefinitionId);
    Bid bid = new Bid();
    bid.setDeclarant(declarant);
    bid.setProcedure(def.getProcedure());
    bid.setProcedureProcessDefinition(def);
    bid.setVersion(def.getProcedure().getVersion());
    bid.setStatus(BidStatus.New);
    bid.setEmployee(curEmployee);
    bid.setProcessInstanceId("");
    em.persist(bid);
    em.flush();
    return bid;
  }

  private Bid createOrUpdateBid(Bid bid, ProcedureProcessDefinition def, List<Task> tasks, ProcessInstance processInstance, Employee curEmployee, String declarant) {
    if (bid == null) {
      bid = new Bid();
    }
    Set<String> currentStep = new HashSet<String>();
    for (Task task : tasks) {
      currentStep.add(task.getId());
    }

    bid.setCurrentSteps(currentStep);
    bid.setDeclarant(declarant);
    bid.setProcedure(def.getProcedure());
    bid.setProcedureProcessDefinition(def);
    bid.setProcessInstanceId(processInstance.getId());
    bid.setStatus(processInstance.isEnded() ? BidStatus.Executed : BidStatus.New);
    bid.setVersion(def.getProcedure().getVersion());
    bid.setEmployee(curEmployee);
    em.merge(bid);
    if (curEmployee != null) {
      em.merge(new BidWorkers(bid, curEmployee));
    }
    em.flush();
    return bid;
  }

  @Override
  public int activeProceduresCount(ProcedureType type, long serviceId) {
    return proceduresCount(type, serviceId, true);
  }

  @Override
  public int declarantProceduresCount(ProcedureType type, long serviceId) {
    return proceduresCount(type, serviceId, false);
  }

  private int proceduresCount(ProcedureType type, long serviceId, boolean usePathToArchive) {
    final CriteriaBuilder c = em.getCriteriaBuilder();
    final CriteriaQuery<Number> query = c.createQuery(Number.class);
    final Root<Procedure> procedure = query.from(Procedure.class);
    query.select(c.countDistinct(procedure))//
      .where(typeAndService(type, serviceId, c, procedure, usePathToArchive));
    return count(query);
  }

  @Override
  public List<Procedure> selectActiveProcedures(ProcedureType type, long serviceId, int start, int count) {
    return selectProcedures(type, serviceId, start, count, true);
  }

  @Override
  public List<Procedure> selectDeclarantProcedures(ProcedureType type, long serviceId, int start, int count) {
    return selectProcedures(type, serviceId, start, count, false);
  }

  private List<Procedure> selectProcedures(ProcedureType type, long serviceId, int start, int count, boolean usePathToArchive) {
    final CriteriaBuilder c = em.getCriteriaBuilder();
    final CriteriaQuery<Procedure> query = c.createQuery(Procedure.class);
    final Root<Procedure> procedure = query.from(Procedure.class);
    query.select(procedure)//
      .distinct(true)
      .where(typeAndService(type, serviceId, c, procedure, usePathToArchive))//
      .orderBy(c.asc(procedure.get(Procedure_.name)));
    return chunk(start, count, query);
  }

  @Override
  public int activeServicesCount(ProcedureType type) {
    final CriteriaBuilder c = em.getCriteriaBuilder();
    final CriteriaQuery<Number> query = c.createQuery(Number.class);
    final Root<Service> service = query.from(Service.class);
    query.select(c.countDistinct(service))//
      .where(typeAndStatus(type, c, service));
    return count(query);
  }

  @Override
  public List<Service> selectActiveServices(ProcedureType type, int start, int count) {
    final CriteriaBuilder c = em.getCriteriaBuilder();
    final CriteriaQuery<Service> query = c.createQuery(Service.class);
    final Root<Service> service = query.from(Service.class);
    query.select(service)//
      .where(typeAndStatus(type, c, service))
      .distinct(true)//
      .orderBy(c.asc(service.get(Service_.name)));
    return chunk(start, count, query);
  }

  @Override
  public ProcedureProcessDefinition selectActive(long procedureId) {
    final CriteriaBuilder _ = em.getCriteriaBuilder();
    final CriteriaQuery<ProcedureProcessDefinition> query = _.createQuery(ProcedureProcessDefinition.class);
    final Root<ProcedureProcessDefinition> def = query.from(ProcedureProcessDefinition.class);
    final Path<Procedure> procedure = def.get(ProcedureProcessDefinition_.procedure);
    query.select(def).where(_.and(//
      _.equal(procedure.get(Procedure_.id), procedureId),//
      _.equal(def.get(ProcedureProcessDefinition_.status), DefinitionStatus.Work)//
    ));
    final List<ProcedureProcessDefinition> defs = em.createQuery(query).setMaxResults(1).getResultList();
    return defs.isEmpty() ? null : defs.get(0);
  }

  public String getBidIdByProcessDefinitionId(String procDefId) {
    Bid bid = em.createQuery("select b from Bid b where b.processInstanceId=:processInstanceId", Bid.class).setParameter("processInstanceId", procDefId).getSingleResult();
    return bid.getId().toString();
  }

  private <T> List<T> chunk(int start, int count, final CriteriaQuery<T> query) {
    return em.createQuery(query).setFirstResult(start).setMaxResults(count).getResultList();
  }

  private int count(final CriteriaQuery<Number> query) {
    return em.createQuery(query).getSingleResult().intValue();
  }

  private Predicate typeAndService(ProcedureType type, long serviceId, final CriteriaBuilder _, final Root<Procedure> procedure, boolean findInPathToArchive) {
    final Predicate typeAndStatus = typeAndStatus(type, _, procedure, findInPathToArchive);
    if (serviceId < 0) {
      return typeAndStatus;
    }
    final Path<Service> service = procedure.get(Procedure_.service);
    return _.and(//
      typeAndStatus,//
      _.equal(service.get(Service_.id), serviceId)//
    );
  }

  private Predicate typeAndStatus(ProcedureType type, final CriteriaBuilder _, final Root<Service> service) {
    return typeAndStatus(type, _, service.join(Service_.procedures), true);
  }

  private Predicate typeAndStatus(ProcedureType type, final CriteriaBuilder _,
                                  final From<?, Procedure> procedures, boolean findInPathToArchive) {
    final SetJoin<Procedure, ProcedureProcessDefinition> defs = procedures.join(Procedure_.processDefinitions);
    if (findInPathToArchive) {
      return _.and(//
        _.equal(procedures.get(Procedure_.type), type),//
        _.or(_.equal(defs.get(ProcedureProcessDefinition_.status), DefinitionStatus.Work), _.equal(defs.get(ProcedureProcessDefinition_.status), DefinitionStatus.PathToArchive))//
      );
    }
    return _.and(//
      _.equal(procedures.get(Procedure_.type), type),//
      _.equal(defs.get(ProcedureProcessDefinition_.status), DefinitionStatus.Work)//
    );
  }

}
