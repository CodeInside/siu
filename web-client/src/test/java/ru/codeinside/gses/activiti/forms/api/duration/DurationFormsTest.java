/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.api.duration;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Rule;
import org.junit.Test;
import ru.codeinside.gses.activiti.InMemoryEngineRule;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * тесты для исследования возможности получения настроек длительности выполнения этапов и заявок
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
    // получаю текущий этап
    Task task = engine.getProcessEngine().getTaskService().createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
    DurationPreference durationPreference = engine.getFormDefinition(task.getId()).getDurationPreference();
    assertTrue(durationPreference.dataExists);
    assertFalse(durationPreference.workedDays);
    assertEquals(4, durationPreference.inactiveInterval);
    assertEquals(6, durationPreference.notificationInterval);
    assertEquals(8, durationPreference.executionInterval);
  }

  @Test
  public void testGetDefaultDurationRestrictionOnCurrentTask() {
    deployForm("default_form_restriction");
    // запускаю процесс
    Map<String, Object> startFormValues = new HashMap<String, Object>();
    startFormValues.put("result_systemParams", "any_value");
    ProcessInstance processInstance = engine.getProcessEngine().getRuntimeService().startProcessInstanceByKey("duration_test", startFormValues);
    // получаю текущий этап
    Task task = engine.getProcessEngine().getTaskService().createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
    DurationPreference durationPreference = engine.getFormDefinition(task.getId()).getDurationPreference();
    // ограничений в задаче нет
    assertFalse(durationPreference.dataExists);
    assertFalse(durationPreference.defaultDataExists);
    assertFalse(durationPreference.workedDays);
    // получаем по тек. этапу данные о стартовой форме процесса
    ProcessDefinition def = engine.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
    DurationPreference duration = engine.getStartFormDefinition(def.getId()).getDurationPreference();
    assertTrue(duration.dataExists);
    assertFalse(duration.workedDays);
    assertEquals(4, duration.notificationInterval);
    assertEquals(6, duration.executionInterval);
    assertTrue(duration.defaultDataExists);
    assertEquals(5, duration.defaultNotificationInterval);
    assertEquals(11, duration.defaultExecutionInterval);
  }

  @Test
  public void testGetDurationRestrictionOnStartForm() {
    deployForm("start_form_restriction");
    Logger.getAnonymousLogger().info("get definition");
    ProcessDefinition def = engine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey("duration_test").singleResult();
    DurationPreference duration = engine.getStartFormDefinition(def.getId()).getDurationPreference();
    assertTrue(duration.dataExists);
    assertFalse(duration.workedDays);
    assertEquals(4, duration.notificationInterval);
    assertEquals(6, duration.executionInterval);
    assertTrue(duration.defaultDataExists);
    assertEquals(5, duration.defaultNotificationInterval);
    assertEquals(11, duration.defaultExecutionInterval);
  }

  private void deployForm(String form) {
    final String resource = "business_calendar/" + form + ".bpmn";
    final InputStream is = getClass().getClassLoader().getResourceAsStream(resource);
    assertNotNull(resource, is);
    engine.getRepositoryService().createDeployment().addInputStream(resource, is).deploy();
  }
}
