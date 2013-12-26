/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package org.activiti.designer.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class MyProcessTest extends Assert {

	@Rule
	public SimpleActivitiRule activitiRule = new SimpleActivitiRule();

	@Test
	public void startProcess() throws Exception {
		final RepositoryService repositoryService = activitiRule.getRepositoryService();
		final RuntimeService runtimeService = activitiRule.getRuntimeService();
		final TaskService taskService = activitiRule.getTaskService();

		final String resourceName = "MyProcess.bpmn20.xml";
		final String login = "login";

		InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName);

		repositoryService.createDeployment().addInputStream(resourceName, is).deploy();

		assertEquals(0, runtimeService.createProcessInstanceQuery().list().size());

		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("description", "Запуск");
		variableMap.put("employeeLogin", login);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process1", "key", variableMap);

		assertEquals("key", processInstance.getBusinessKey());
		assertNotNull(processInstance.getId());
		assertFalse(processInstance.isEnded());
		assertFalse(processInstance.isSuspended());

		assertEquals(1, runtimeService.createProcessInstanceQuery().list().size());

		System.out.println("attachments: "
		        + taskService.getProcessInstanceAttachments(processInstance.getProcessInstanceId()));
		System.out.println("comments: "
		        + taskService.getProcessInstanceComments(processInstance.getProcessInstanceId()));

		writePng(processInstance);

		System.out.println("pid: " + processInstance.getProcessInstanceId() + " pdid: "
		        + processInstance.getProcessDefinitionId());

		assertEquals(1, taskService.createTaskQuery().count());

		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId())
		        .singleResult();
		System.out.println("task eid: " + task.getExecutionId());
		System.out.println("task  id: " + task.getId());
		System.out.println("task key: " + task.getTaskDefinitionKey());

		assertNull(task.getAssignee());
		{
			taskService.setAssignee(task.getId(), login);
			Map<String, Object> formMap = new HashMap<String, Object>();
			formMap.put("surnameId", "Фамилия");
			taskService.complete(task.getId(), formMap);
		}

		assertEquals(0, taskService.createTaskQuery().count());

		assertEquals(0, runtimeService.createProcessInstanceQuery().active().count());

	}

	private void writePng(ProcessInstance processInstance) throws FileNotFoundException, IOException {

		final RepositoryServiceImpl repositoryService = (RepositoryServiceImpl) activitiRule.getRepositoryService();
		final RuntimeService runtimeService = activitiRule.getRuntimeService();

		// метод не из API!
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) repositoryService
		        .getDeployedProcessDefinition(processInstance.getProcessDefinitionId());

		InputStream definitionImageStream = ProcessDiagramGenerator.generateDiagram(def, "png",
		        runtimeService.getActiveActivityIds(processInstance.getProcessInstanceId()));

		FileOutputStream out = new FileOutputStream("target/x.png");
		int read = 0;
		byte[] bytes = new byte[1024];
		while ((read = definitionImageStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		definitionImageStream.close();
		out.close();
	}
}