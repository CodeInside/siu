/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.duration;

import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Rule;
import org.junit.Test;
import ru.codeinside.gses.activiti.InMemoryEngineRule;
import ru.codeinside.gses.activiti.forms.CustomStartFormData;
import ru.codeinside.gses.activiti.forms.CustomTaskFormData;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * тесты для исследования возможности получения настроек длительности выполнения задач и маршрутов
 */
public class DurationFormsTest {
  @Rule
  final public InMemoryEngineRule engine = new InMemoryEngineRule();

  @Test
  public void testGetDurationRestrictionOnCurrentTask() {
    deployForm("without_defaults");
    // запускаю процесс
    Map<String, Object> startFormValues = new HashMap<String, Object>();
    startFormValues.put("result_systemParams", "any_value");
    ProcessInstance processInstance = engine.getProcessEngine().getRuntimeService().startProcessInstanceByKey("duration_test", startFormValues);
    // получаю текущую задачу
    Task task = engine.getProcessEngine().getTaskService().createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
    TaskFormData taskFormData = engine.getProcessEngine().getFormService().getTaskFormData(task.getId());
    assertTrue(taskFormData instanceof CustomTaskFormData);
    CustomTaskFormData form = (CustomTaskFormData) taskFormData;
    DurationPreference durationPreference = form.getPropertyTree().getDurationPreference();
    assertTrue(durationPreference.dataExists);
    assertFalse(durationPreference.workedDays);
    assertEquals(4, durationPreference.inactivePeriod);
    assertEquals(6, durationPreference.notificationPeriod);
    assertEquals(8, durationPreference.executionPeriod);
  }

  @Test
  public void testGetDefaultDurationRestrictionOnCurrentTask() {
    deployForm("default_form_restriction");
    // запускаю процесс
    Map<String, Object> startFormValues = new HashMap<String, Object>();
    startFormValues.put("result_systemParams", "any_value");
    ProcessInstance processInstance = engine.getProcessEngine().getRuntimeService().startProcessInstanceByKey("duration_test", startFormValues);
    // получаю текущую задачу
    Task task = engine.getProcessEngine().getTaskService().createTaskQuery().processInstanceId(processInstance.getId()).singleResult();


    TaskFormData taskFormData = engine.getProcessEngine().getFormService().getTaskFormData(task.getId());
    assertTrue(taskFormData instanceof CustomTaskFormData);
    CustomTaskFormData form = (CustomTaskFormData) taskFormData;
    DurationPreference durationPreference = form.getPropertyTree().getDurationPreference();
    // ограничений в задаче нет
    assertFalse(durationPreference.dataExists);
    assertFalse(durationPreference.defaultDataExists);
    assertFalse(durationPreference.workedDays);
    // получаем по тек. задаче данные о стартовой форме процесса
    ProcessDefinition def = engine.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
    StartFormData startFormData = engine.getFormService().getStartFormData(def.getId());

    assertTrue(startFormData instanceof CustomStartFormData);
    CustomStartFormData startForm = (CustomStartFormData) startFormData;
    DurationPreference duration = startForm.getPropertyTree().getDurationPreference();
    assertTrue(duration.dataExists);
    assertFalse(duration.workedDays);
    assertEquals(4, duration.notificationPeriod);
    assertEquals(6, duration.executionPeriod);
    assertTrue(duration.defaultDataExists);
    assertEquals(5, duration.defaultNotificationPeriod);
    assertEquals(11, duration.defaultExecutionPeriod);


    // пытаемся получить TaskFormHandler
    assertTrue(def instanceof ProcessDefinitionEntity);
    ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) def;
    assertNull(definitionEntity.getTaskDefinitions()); // что-то можно получить только, если вызов был со стороны Activiti, например, при обработке события task listener

  }


  @Test
  public void testGetDurationRestrictionOnStartForm() {
    deployForm("start_form_restriction");
    // получаю определение формы для стартовой формы
    ProcessDefinition def = engine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey("duration_test").singleResult();
    assertNotNull(def);
    StartFormData startFormData = engine.getFormService().getStartFormData(def.getId());
    assertTrue(startFormData instanceof CustomStartFormData);
    CustomStartFormData form = (CustomStartFormData) startFormData;
    DurationPreference duration = form.getPropertyTree().getDurationPreference();
    assertTrue(duration.dataExists);
    assertFalse(duration.workedDays);
    assertEquals(4, duration.notificationPeriod);
    assertEquals(6, duration.executionPeriod);
    assertTrue(duration.defaultDataExists);
    assertEquals(5, duration.defaultNotificationPeriod);
    assertEquals(11, duration.defaultExecutionPeriod);
  }


  private void deployForm(String form) {
    final String resource = "business_calendar/" + form + ".bpmn";
    final InputStream is = getClass().getClassLoader().getResourceAsStream(resource);
    assertNotNull(resource, is);
    engine.getRepositoryService().createDeployment().addInputStream(resource, is).deploy();
  }
}
