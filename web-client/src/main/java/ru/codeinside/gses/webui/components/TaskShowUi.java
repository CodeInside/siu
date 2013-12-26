/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.HistoricFormPropertyEntity;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;

import ru.codeinside.gses.service.Functions;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.utils.Components;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class TaskShowUi extends InfoShowUi {

	private static final long serialVersionUID = 8923680364413074630L;
	private String taskName;
	private String taskId;
	private String exeId;

	public TaskShowUi(HistoricTaskInstance task) {
		super(task.getProcessDefinitionId());
		taskName = task.getName();
		taskId = task.getId();
		exeId = task.getExecutionId();
		setCompositionRoot(buildMainLayout());
	}

	public TaskShowUi(Task task) {
		super(task.getProcessDefinitionId());
		taskName = task.getName();
		taskId = task.getId();
		exeId = task.getExecutionId();
		setCompositionRoot(buildMainLayout());
	}

	@Override
	String getLabelIdValue() {
		return "Этап " + taskId + " по маршруту";
	}

	@Override
	String getRouteLeg() {
		return "Этап: " + taskName;
	}

	@Override
	List<Object[]> getHistories() {
		List<HistoricTaskInstance> histories = Functions
		        .withHistory(new Function<HistoryService, List<HistoricTaskInstance>>() {
			        public List<HistoricTaskInstance> apply(HistoryService srv) {
				        return srv.createHistoricTaskInstanceQuery().taskId(taskId).list();
			        }
		        });
		List<Object[]> result = Lists.newArrayList();
		for (HistoricTaskInstance i : histories) {
			result.add(new Object[] { i.getId(), i.getName(), i.getStartTime(), i.getEndTime(), i.getAssignee(),
			        i.getOwner() });
		}
		return Lists.newArrayList();
	}

	@Override
	List<Object[]> getVariables() {
		List<Object[]> result = Lists.newArrayList();

		List<HistoricDetail> list = Functions.withHistory(new Function<HistoryService, List<HistoricDetail>>() {
			public List<HistoricDetail> apply(HistoryService srv) {
				return srv.createHistoricDetailQuery().taskId(taskId).list();
			}
		});

		for (HistoricDetail detail : list) {
			if (detail instanceof HistoricFormPropertyEntity) {
				HistoricFormPropertyEntity historyEntity = (HistoricFormPropertyEntity) detail;
				result.add(new Object[] { historyEntity.getPropertyId(), historyEntity.getPropertyValue() });
			}
		}

		try {
			Map<String, Object> variables = Functions.withRuntime(new Function<RuntimeService, Map<String, Object>>() {
				public Map<String, Object> apply(RuntimeService srv) {
					return srv.getVariables(exeId);
				}
			});
			for (Map.Entry<String, Object> m : variables.entrySet()) {
				result.add(new Object[] { m.getKey(), m.getValue() });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	List<Object[]> getAttachments() {
		List<Object[]> result = Lists.newArrayList();

		List<Attachment> attachments = Functions.withTask(new Function<TaskService, List<Attachment>>() {
			public List<Attachment> apply(TaskService s) {
				return s.getTaskAttachments(taskId);
			}
		});

		for (final Attachment a : attachments) {
			result.add(new Object[] { a.getName(), Components.createAttachShowButton(a, Flash.app()) });
		}

		return result;
	}

	@Override
	List<String> getHistoryActivitiIds() {
		List<String> activeActivityIds = Lists.newArrayList();
		try {
			activeActivityIds = Functions.withRuntime(new Function<RuntimeService, List<String>>() {
				public List<String> apply(RuntimeService s) {
					return s.getActiveActivityIds(exeId);
				}
			});
		} catch (ActivitiException e) {
			e.printStackTrace();
		}

		return activeActivityIds;
	}

}
