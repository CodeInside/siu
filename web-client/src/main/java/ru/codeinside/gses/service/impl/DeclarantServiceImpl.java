/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.service.impl;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import ru.codeinside.adm.database.DefinitionStatus;
import ru.codeinside.adm.database.Procedure;
import ru.codeinside.adm.database.ProcedureProcessDefinition;
import ru.codeinside.adm.database.ProcedureProcessDefinition_;
import ru.codeinside.adm.database.ProcedureType;
import ru.codeinside.adm.database.Procedure_;
import ru.codeinside.adm.database.Service;
import ru.codeinside.adm.database.Service_;
import ru.codeinside.adm.database.SmevChain;
import ru.codeinside.gses.activiti.SubmitStartFormCommand;
import ru.codeinside.gses.activiti.forms.Signatures;
import ru.codeinside.gses.service.BidID;
import ru.codeinside.gses.service.DeclarantService;
import ru.codeinside.gses.webui.form.SignatureType;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static javax.ejb.TransactionAttributeType.REQUIRED;


@TransactionAttribute(REQUIRED)
@TransactionManagement
@Singleton
@Lock(LockType.READ)
@SuppressWarnings("PackageAccessibility")
public class DeclarantServiceImpl implements DeclarantService {

  @PersistenceContext(unitName = "myPU")
  EntityManager em;

  @Override
  public BidID declare(ProcessEngine engine, String processDefinitionId,
                       Map<String, Object> properties, Map<SignatureType, Signatures> signatures,
                       String declarer) {
    return commandExecutor(engine).execute(
        new SubmitStartFormCommand(null, null, processDefinitionId, properties, signatures, declarer, null, null)
    );
  }

  @Override
  public BidID smevDeclare(SmevChain smevChain, String componentName,
                           ProcessEngine engine, String processDefinitionId,
                           Map<String, Object> properties, String declarer, String tag) {
    return commandExecutor(engine).execute(
        new SubmitStartFormCommand(smevChain, componentName, processDefinitionId, properties, null, declarer, tag, null)
    );
  }

  @Override
  public List<String> getBids(long gid) {
    List<Long> list = em.createQuery("select e.id from Bid e where e.glue.id = :gid", Long.class)
        .setParameter("gid", gid).getResultList();
    List<String> strings = new ArrayList<String>(list.size());
    for (Long id : list) {
      strings.add(Long.toString(id));
    }
    return strings;
  }

  @Override
  public long getGlueIdByRequestIdRef(String requestIdRef) {
    List<Long> rs = em.createQuery(
        "select s.id from ExternalGlue s where s.requestIdRef=:requestIdRef", Long.class)
        .setParameter("requestIdRef", requestIdRef)
        .getResultList();
    if (rs.isEmpty()) {
      return 0L;
    }
    return rs.get(0);
  }

  @Override
  public int activeProceduresCount(ProcedureType type, long serviceId) {
    return proceduresCount(type, serviceId, true);
  }

  @Override
  public List<Procedure> selectActiveProcedures(ProcedureType type, long serviceId, int start, int count) {
    return selectProcedures(type, serviceId, start, count, true);
  }

  @Override
  public List<Procedure> selectDeclarantProcedures(ProcedureType type, long serviceId, int start, int count) {
    return selectProcedures(type, serviceId, start, count, false);
  }

  @Override
  public int activeServicesCount(ProcedureType type) {
    CriteriaBuilder b = em.getCriteriaBuilder();
    CriteriaQuery<Number> query = b.createQuery(Number.class);
    Root<Service> service = query.from(Service.class);
    query.select(b.countDistinct(service))
        .where(typeAndStatus(type, b, service));
    return count(query);
  }

  @Override
  public List<Service> selectActiveServices(ProcedureType type, int start, int count) {
    CriteriaBuilder b = em.getCriteriaBuilder();
    CriteriaQuery<Service> query = b.createQuery(Service.class);
    Root<Service> service = query.from(Service.class);
    query.select(service)
        .where(typeAndStatus(type, b, service))
        .distinct(true)
        .orderBy(b.asc(service.get(Service_.name)));
    return chunk(start, count, query);
  }

  @Override
  public int filteredActiveServicesCount(ProcedureType type, String employee) {
    Query query = getServiceQuery(true, type, employee);
    return ((Long) query.getSingleResult()).intValue();
  }

  @Override
  public List<Service> selectFilteredActiveServices(ProcedureType type, String employee, int start, int count) {
    Query query = getServiceQuery(false, type, employee)
        .setFirstResult(start)
        .setMaxResults(count);
    return (List<Service>) query.getResultList();
  }

  @Override
  public ProcedureProcessDefinition selectActive(long procedureId) {
    CriteriaBuilder b = em.getCriteriaBuilder();
    CriteriaQuery<ProcedureProcessDefinition> query = b.createQuery(ProcedureProcessDefinition.class);
    Root<ProcedureProcessDefinition> def = query.from(ProcedureProcessDefinition.class);
    Path<Procedure> procedure = def.get(ProcedureProcessDefinition_.procedure);
    query.select(def).where(
        b.and(
            b.equal(procedure.get(Procedure_.id), procedureId),
            b.equal(def.get(ProcedureProcessDefinition_.status), DefinitionStatus.Work)
        )
    );
    List<ProcedureProcessDefinition> defs = em.createQuery(query).setMaxResults(1).getResultList();
    return defs.isEmpty() ? null : defs.get(0);
  }


  // ---- internals ----

  private Query getServiceQuery(boolean count, ProcedureType type, String employee) {
    String fields =
        "s.id as \"ID\", " +
        "s.name as \"NAME\", " +
        "s.datecreated as \"DATECREATED\", " +
        "s.registercode as \"REGISTERCODE\" \n";

    String countExpression = "count(*) \n";

    String sql = "SELECT " + (count ? countExpression : fields) +
        "FROM service s \n" +
        "WHERE s.id IN ( \n" +
        "  SELECT DISTINCT p.service_id \n" +
        "  FROM procedure p, procedure_process_definition ppd \n" +
        "  WHERE ppd.procedure_id = p.id AND (ppd.status = ?1 OR ppd.status = ?2) AND p.type = ?3 AND (p.id NOT IN ( \n" +
        "    SELECT DISTINCT ppd2.procedure_id \n" +
        "    FROM procedure_process_definition ppd2, act_ru_identitylink il \n" +
        "    WHERE ppd2.processdefinitionid = il.proc_def_id_ \n" +
        "  ) OR p.id IN ( \n" +
        "    SELECT DISTINCT ppd3.procedure_id \n" +
        "    FROM procedure_process_definition ppd3, act_ru_identitylink il2 \n" +
        "    WHERE il2.proc_def_id_ = ppd3.processdefinitionid AND (il2.user_id_ = ?4 OR il2.group_id_ IN ( \n" +
        "      SELECT group_id_ \n" +
        "      FROM act_id_membership \n" +
        "      WHERE user_id_ = ?4 \n" +
        "    ))\n" +
        "  ))\n" +
        ")\n" + (!count ? "ORDER BY s.name" : "");

    Query query = count ? em.createNativeQuery(sql) : em.createNativeQuery(sql, Service.class);
    return query
        .setParameter(1, DefinitionStatus.Work.ordinal())
        .setParameter(2, DefinitionStatus.PathToArchive.ordinal())
        .setParameter(3, type.ordinal())
        .setParameter(4, employee);
  }

  private CommandExecutor commandExecutor(ProcessEngine engine) {
    return ((ServiceImpl) engine.getFormService()).getCommandExecutor();
  }

  private int proceduresCount(ProcedureType type, long serviceId, boolean usePathToArchive) {
    CriteriaBuilder b = em.getCriteriaBuilder();
    CriteriaQuery<Number> query = b.createQuery(Number.class);
    Root<Procedure> procedures = query.from(Procedure.class);
    query.select(b.countDistinct(procedures))
        .where(typeAndService(type, serviceId, b, procedures, usePathToArchive));
    return count(query);
  }

  private List<Procedure> selectProcedures(ProcedureType type, long serviceId, int start, int count, boolean usePathToArchive) {
    CriteriaBuilder b = em.getCriteriaBuilder();
    CriteriaQuery<Procedure> query = b.createQuery(Procedure.class);
    Root<Procedure> procedures = query.from(Procedure.class);
    query.select(procedures)
        .distinct(true)
        .where(typeAndService(type, serviceId, b, procedures, usePathToArchive))
        .orderBy(b.asc(procedures.get(Procedure_.name)));
    return chunk(start, count, query);
  }

  private <T> List<T> chunk(int start, int count, CriteriaQuery<T> query) {
    return em.createQuery(query).setFirstResult(start).setMaxResults(count).getResultList();
  }

  private int count(final CriteriaQuery<Number> query) {
    return em.createQuery(query).getSingleResult().intValue();
  }

  private Predicate typeAndService(ProcedureType type, long serviceId, CriteriaBuilder b,
                                   Root<Procedure> procedure, boolean findInPathToArchive) {
    Predicate typeAndStatus = typeAndStatus(type, b, procedure, findInPathToArchive);
    if (serviceId < 0) {
      return typeAndStatus;
    }
    Path<Service> service = procedure.get(Procedure_.service);
    return b.and(
        typeAndStatus,
        b.equal(service.get(Service_.id), serviceId)
    );
  }

  private Predicate typeAndStatus(ProcedureType type, CriteriaBuilder b, Root<Service> service) {
    return typeAndStatus(type, b, service.join(Service_.procedures), true);
  }

  private Predicate typeAndStatus(ProcedureType type, CriteriaBuilder b,
                                  From<?, Procedure> procedures, boolean findInPathToArchive) {
    SetJoin<Procedure, ProcedureProcessDefinition> defs = procedures.join(Procedure_.processDefinitions);
    if (findInPathToArchive) {
      return b.and(
          b.equal(procedures.get(Procedure_.type), type),
          b.or(
              b.equal(defs.get(ProcedureProcessDefinition_.status), DefinitionStatus.Work),
              b.equal(defs.get(ProcedureProcessDefinition_.status), DefinitionStatus.PathToArchive)
          )
      );
    }
    return b.and(
        b.equal(procedures.get(Procedure_.type), type),
        b.equal(defs.get(ProcedureProcessDefinition_.status), DefinitionStatus.Work)
    );
  }

}
