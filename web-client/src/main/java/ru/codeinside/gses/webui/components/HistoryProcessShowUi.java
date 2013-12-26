/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.HistoricFormPropertyEntity;
import org.activiti.engine.task.Attachment;

import ru.codeinside.gses.service.Functions;
import ru.codeinside.gses.webui.ActivitiApp;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.utils.Components;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class HistoryProcessShowUi extends InfoShowUi {

	private static final long serialVersionUID = -6134465414133876633L;
	private HistoricProcessInstance process;

	public HistoryProcessShowUi(HistoricProcessInstance process) {
		super(process.getProcessDefinitionId());
		this.process = process;
		setCompositionRoot(buildMainLayout());
	}

	@Override
	String getLabelIdValue() {
		return "Процедура " + process.getId() + " по маршруту";
	}

	@Override
	String getRouteLeg() {
		return "Конечный этап: " + this.process.getEndActivityId();
	}

	@Override
	List<Object[]> getHistories() {
		List<HistoricTaskInstance> histories = Functions
		        .withHistory(new Function<HistoryService, List<HistoricTaskInstance>>() {
			        public List<HistoricTaskInstance> apply(HistoryService s) {
				        return s.createHistoricTaskInstanceQuery().processInstanceId(process.getId()).list();
			        }
		        });

		List<Object[]> result = Lists.newArrayList();
		for (HistoricTaskInstance i : histories) {
			result.add(new Object[] { i.getId(), i.getName(), i.getStartTime(), i.getEndTime(), i.getAssignee(),
			        i.getOwner() }//
			);
		}
		return result;
	}

	@Override
	List<String> getHistoryActivitiIds() {
		final List<String> activeActivityIds = Lists.newArrayList();

		List<HistoricActivityInstance> historyActivities = Functions
		        .withHistory(new Function<HistoryService, List<HistoricActivityInstance>>() {
			        public List<HistoricActivityInstance> apply(HistoryService s) {
				        return s.createHistoricActivityInstanceQuery().processInstanceId(process.getId()).list();
			        }
		        });

		for (HistoricActivityInstance historyActivity : historyActivities) {
			activeActivityIds.add(historyActivity.getActivityId());
		}
		return activeActivityIds;
	}

	@Override
	List<Object[]> getVariables() {
		List<Object[]> result = Lists.newArrayList();

		List<HistoricDetail> list = Functions.withHistory(new Function<HistoryService, List<HistoricDetail>>() {
			public List<HistoricDetail> apply(HistoryService s) {
				return s.createHistoricDetailQuery().processInstanceId(process.getId()).list();
			}
		});

		for (HistoricDetail detail : list) {
			if (detail instanceof HistoricFormPropertyEntity) {
				HistoricFormPropertyEntity historyEntity = (HistoricFormPropertyEntity) detail;
				result.add(new Object[] { historyEntity.getPropertyId(), historyEntity.getPropertyValue() });
			} else {
				result.add(new Object[] { detail.toString(), detail.getActivityInstanceId() });
			}
		}
		return result;
	}

	@Override
	List<Object[]> getAttachments() {
		List<Object[]> result = Lists.newArrayList();
		List<Attachment> attachments = Functions.withTask(new Function<TaskService, List<Attachment>>() {
			public List<Attachment> apply(TaskService s) {
				return s.getProcessInstanceAttachments(process.getId());
			}
		});

		for (final Attachment a : attachments) {
			result.add(new Object[] { a.getName(), Components.createAttachShowButton(a, Flash.app()) });

		}
		return result;
	}

}
