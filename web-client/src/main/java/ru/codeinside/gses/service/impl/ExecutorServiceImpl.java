/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.service.impl;

import com.google.common.collect.ImmutableSet;
import commons.Streams;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.task.TaskDefinition;
import ru.codeinside.adm.database.BytesBuffer;
import ru.codeinside.adm.database.FieldBuffer;
import ru.codeinside.adm.database.FormBuffer;
import ru.codeinside.adm.database.Procedure;
import ru.codeinside.adm.database.ProcedureProcessDefinition;
import ru.codeinside.gses.activiti.FileValue;
import ru.codeinside.gses.activiti.forms.FileBufferValue;
import ru.codeinside.gses.service.ExecutorService;
import ru.codeinside.gses.service.Some;
import ru.codeinside.gses.webui.Flash;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static javax.ejb.TransactionManagementType.CONTAINER;

@TransactionManagement(CONTAINER)
@Stateless
public class ExecutorServiceImpl implements ExecutorService {
  @PersistenceContext(unitName = "myPU")
  EntityManager em;

  @Override
  public String getProcedureNameByDefinitionId(String processDefinitionId) {
    ProcedureProcessDefinition procedureProcessDefinition = em.find(ProcedureProcessDefinition.class, processDefinitionId);
    try {
      return procedureProcessDefinition.getProcedure().getName();
    } catch (NullPointerException e) {
      System.out.println("Can`t get procedure on process definition");
    }
    return null;
  }

  @Override
  public Map<String, TaskDefinition> selectTasksByProcedureId(long procedureId) {
    return getTaskDefinitions(procedureId);
  }

  @Override
  public int countTasksByProcedureId(long procedureId) {
    return getTaskDefinitions(procedureId).size();
  }

  @Override
  public void saveBuffer(String taskId, String fieldId, String value) {
    FieldBuffer fieldBuffer = createBuffer(taskId, fieldId);
    fieldBuffer.setTextValue(value);
    em.persist(fieldBuffer);
  }

  private FieldBuffer createBuffer(String taskId, String fieldId) {
    Date now = new Date();
    FormBuffer formBuffer = em.find(FormBuffer.class, taskId);
    if (formBuffer == null) {
      formBuffer = new FormBuffer(taskId, now);
    } else {
      formBuffer.setUpdateDate(now);
    }
    em.persist(formBuffer);
    FieldBuffer fieldBuffer = getBuffer(taskId, fieldId);
    if (fieldBuffer == null) {
      fieldBuffer = new FieldBuffer(formBuffer, fieldId, now);
      formBuffer.getFields().add(fieldBuffer);
    }
    fieldBuffer.setUpdateDate(now);
    return fieldBuffer;
  }

  @Override
  public void saveBuffer(String taskId, String fieldId, Long value) {
    FieldBuffer fieldBuffer = createBuffer(taskId, fieldId);
    fieldBuffer.setLongValue(value);
    em.persist(fieldBuffer);
  }

  @Override
  public Some<String> getTextBuffer(String taskId, String fieldId) {
    FieldBuffer fieldBuffer = getBuffer(taskId, fieldId);
    if (fieldBuffer == null) {
      return Some.empty();
    }
    return Some.of(fieldBuffer.getTextValue());
  }

  @Override
  public Some<Long> getLongBuffer(String taskId, String fieldId) {
    FieldBuffer fieldBuffer = getBuffer(taskId, fieldId);
    if (fieldBuffer == null) {
      return Some.empty();
    }
    return Some.of(fieldBuffer.getLongValue());
  }

  @Override
  public Some<FileValue> getFileBuffer(String taskId, String fieldId) {
    FieldBuffer fieldBuffer = getBuffer(taskId, fieldId);
    if (fieldBuffer == null) {
      return Some.empty();
    }
    BytesBuffer bytesValue = fieldBuffer.getBytesValue();
    FileValue value = new FileBufferValue(
      fieldBuffer.getTextValue(), fieldBuffer.getMime(), bytesValue == null ? null : bytesValue.getId());
    return Some.of(value);
  }

  @Override
  public byte[] getBytes(int contentId) {
    BytesBuffer bytesBuffer = em.find(BytesBuffer.class, contentId);
    return bytesBuffer == null ? new byte[0] : bytesBuffer.getBytes();
  }

  @Override
  public FileValue saveBytesBuffer(String taskId, String fieldId, String fileName, String mimeType, File tmpFile) {
    BytesBuffer newBytes;
    try {
      newBytes = new BytesBuffer(Streams.toBytes(tmpFile));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    em.persist(newBytes);
    FieldBuffer fieldBuffer = createBuffer(taskId, fieldId);
    BytesBuffer oldBytes = fieldBuffer.getBytesValue();
    fieldBuffer.setLongValue(tmpFile.length());
    fieldBuffer.setTextValue(fileName);
    fieldBuffer.setMime(mimeType);
    fieldBuffer.setBytesValue(newBytes);
    em.persist(fieldBuffer);
    if (oldBytes != null) {
      em.remove(oldBytes);
    }
    return new FileBufferValue(fileName, mimeType, newBytes.getId());
  }

  @Override
  public Set<String> getActiveFields(String taskId) {
    List<String> idList = em
      .createQuery("select b.fieldId from FieldBuffer b where b.formBuffer.taskId = :id", String.class)
      .setParameter("id", taskId).getResultList();
    return ImmutableSet.copyOf(idList);
  }

  private FieldBuffer getBuffer(String taskId, String fieldId) {
    return em.find(FieldBuffer.class, new FieldBuffer.PK(taskId, fieldId));
  }


  private Map<String, TaskDefinition> getTaskDefinitions(long procedureId) {
    Procedure procedure = em.find(Procedure.class, procedureId);
    ProcedureProcessDefinition ppd = (ProcedureProcessDefinition) em.createQuery("select p from procedure_process_definition p where p.procedure=:proc").
      setParameter("proc", procedure).getResultList().get(0);
    final RepositoryServiceImpl repositoryService = (RepositoryServiceImpl) Flash.flash().getProcessEngine().getRepositoryService();
    ProcessDefinitionEntity def = (ProcessDefinitionEntity) repositoryService.getDeployedProcessDefinition(ppd.getProcessDefinitionId());
    return def.getTaskDefinitions();
  }

}
