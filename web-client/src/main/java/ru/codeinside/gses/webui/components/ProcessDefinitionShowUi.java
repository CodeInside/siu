/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import com.vaadin.data.Item;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.form.DefaultTaskFormHandler;
import org.activiti.engine.impl.form.FormPropertyHandler;
import org.activiti.engine.impl.form.TaskFormHandler;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.ReadOnlyProcessDefinition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.activiti.forms.CustomTaskFormHandler;
import ru.codeinside.gses.activiti.forms.api.definitions.BlockNode;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyCollection;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyTree;
import ru.codeinside.gses.activiti.forms.api.definitions.VariableType;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.components.api.Changer;

import java.util.List;
import java.util.Set;

public class ProcessDefinitionShowUi extends CustomComponent {

	private static final long serialVersionUID = -2168823678201493358L;
  private final String processDefinitionId;
	private final Changer changer;

	public ProcessDefinitionShowUi(ProcessDefinition processDefinition, Changer changer) {
		processDefinitionId = processDefinition.getId();
		this.changer = changer;
		setCompositionRoot(buildMainLayout());
		setWidth(1100, Sizeable.UNITS_PIXELS);
		setHeight(600, Sizeable.UNITS_PIXELS);
	}

  private Component buildMainLayout() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setSpacing(true);
		layout.setMargin(true);

		Label label = new Label();
    String name = getProcessDefinitionById(processDefinitionId).getName();
    label.setCaption(name);
		label.setStyleName(Reindeer.LABEL_H2);

		Button showScheme = new Button("Схема");
		showScheme.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = -5911713385519847639L;
			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				VerticalLayout imageLayout = new VerticalLayout();
				Button back = new Button("Назад");
				back.addListener(new Button.ClickListener() {
					private static final long serialVersionUID = 4154712522487297925L;
					@Override
					public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
						changer.back();
					}
				});
				imageLayout.addComponent(back);
				imageLayout.setMargin(true);
				imageLayout.setSpacing(true);
				imageLayout.setWidth(1100, Sizeable.UNITS_PIXELS);
				imageLayout.setHeight(600, Sizeable.UNITS_PIXELS);
				final Panel panel = new Panel();
				panel.getContent().setSizeUndefined();
        TaskGraph tg = new TaskGraph(processDefinitionId, null);
        panel.addComponent(tg);
				panel.setSizeFull();
				panel.setScrollable(true);
				imageLayout.addComponent(panel);
				imageLayout.setExpandRatio(back, 0.01f);
				imageLayout.setExpandRatio(panel, 0.99f);
				changer.change(imageLayout);
			}
		});
		layout.addComponent(showScheme);
		
		Table table = new Table();
		table.setSizeFull();
		table.setImmediate(true);
		table.setSelectable(true);
		table.setSortDisabled(true);
    table.setPageLength(0);
    table.setSelectable(false);
		table.addContainerProperty("id", String.class, null);
		table.addContainerProperty("name", String.class, null);
		table.addContainerProperty("accessPermissions", Component.class, null);
		table.addContainerProperty("formProperties", Component.class, null);
		table.addContainerProperty("other", String.class, null);
		table.setColumnHeaders(new String[] { "Код этапа", "Название", /*"Тип этапа",*/ "Права доступа", "Поля формы", "Остальные параметры" });
		table.setColumnExpandRatio("id", 0.1f);
		table.setColumnExpandRatio("name", 0.1f);
		table.setColumnExpandRatio("accessPermissions", 0.1f);
		table.setColumnExpandRatio("formProperties", 0.4f);
		table.setColumnExpandRatio("other", 0.2f);
		fillTable(table);

    layout.addComponent(label);
    layout.setExpandRatio(label, 1);

    layout.addComponent(table);
    layout.setExpandRatio(table, 40);

		return layout;
	}

  private ReadOnlyProcessDefinition getProcessDefinitionById(String definitionId) {
    return ((RepositoryServiceImpl) Flash.flash().getProcessEngine().getRepositoryService()).getDeployedProcessDefinition(definitionId);
  }

  private void fillTable(Table table) {
		ReadOnlyProcessDefinition processDefinition = getProcessDefinitionById(processDefinitionId);
    int index = 1;
		for (ActivityImpl ac : ((ProcessDefinitionEntity)processDefinition).getActivities()) {
			String name = ac.getProperty("name") != null ? ac.getProperty("name").toString() : "Без названия";
			String other = "";
			VerticalLayout formProperties = new VerticalLayout();
			Component accessSubjectsList = new VerticalLayout();
			if (ac.getActivityBehavior() instanceof UserTaskActivityBehavior) {
				UserTaskActivityBehavior utab = (UserTaskActivityBehavior) ac.getActivityBehavior();
				other = taskInfo(utab);
				TaskFormHandler taskFormHandler = utab.getTaskDefinition().getTaskFormHandler();
				PropertyTree propertyTree = ((CustomTaskFormHandler)taskFormHandler).getPropertyTree();
				formProperties.addComponent(createPropertyTable(propertyTree));
				TaskDefinition taskDefinition = utab.getTaskDefinition();
				Set<Expression> candidateUserIdExpressions = taskDefinition.getCandidateUserIdExpressions();
				Set<Expression> candidateGroupIdExpressions = taskDefinition.getCandidateGroupIdExpressions();
				accessSubjectsList = createAccessList(candidateUserIdExpressions, candidateGroupIdExpressions);
			}
			table.addItem(new Object[] { ac.getId(), name, /*component,*/ accessSubjectsList, formProperties, other }, index++);
		}
	}
	
	private Component createAccessList(Set<Expression> candidateUserIdExpressions, Set<Expression> candidateGroupIdExpressions){
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);

		if(!candidateUserIdExpressions.isEmpty()){
			VerticalLayout usersList = new VerticalLayout();
			usersList.setCaption("Пользователи:");
			for (Expression e : candidateUserIdExpressions) {
				String fio = getUserFio(e);
				String formatted = StringUtils.isEmpty(fio) ? "" : ("(" + fio + ")");
				usersList.addComponent(new Label(e.getExpressionText() + formatted + " "));
			}
			layout.addComponent(usersList);
		}
		if(!candidateGroupIdExpressions.isEmpty()){
			VerticalLayout groupsList = new VerticalLayout();
			groupsList.setCaption("Группы:");
			for (Expression e : candidateGroupIdExpressions) {
				groupsList.addComponent(new Label(e.getExpressionText()));
			}
			layout.addComponent(groupsList);
		}
		return layout;
	}

	private Component createPropertyTable(PropertyTree propertyTree) {
		TreeTable table = new TreeTable();
		table.setImmediate(true);
		table.setSelectable(false);
    table.setPageLength(0);
		table.setSortDisabled(true);
		table.addContainerProperty("id", String.class, null);
		table.addContainerProperty("name", String.class, null);
		table.addContainerProperty("variable", String.class, null);
		table.addContainerProperty("expression", String.class, null);
		table.addContainerProperty("type", String.class, null);
		table.setColumnHeaders(new String[] { "id", "name", "variable", "expression", "type" });
		table.setPageLength(0);
    fillPropertyTable(table, propertyTree, -1);
    return table;
	}

  private int fillPropertyTable(TreeTable treeTable, PropertyCollection Properties, int i) {
    int parentId = i;
    for (PropertyNode propertyNode : Properties.getNodes()) {
      String id = "" + propertyNode.getId();
      String name = "" + propertyNode.getName();
      String variable = "" + propertyNode.getVariableName();
      String expression = "" + propertyNode.getVariableExpression();
      VariableType variableType = propertyNode.getVariableType();
      String type = (variableType==null) ? "" : variableType.getName();
      treeTable.addItem(new Object[] { id, name, variable, expression, type }, ++i);
      if (parentId >= 0) {
        treeTable.setParent(i, parentId);
      }
      if (propertyNode instanceof BlockNode) {
        i = fillPropertyTable(treeTable, (PropertyCollection)propertyNode, i);
      } else {
        treeTable.setChildrenAllowed(i, false);
      }
    }
    return i;
  }
	
	private String taskInfo(UserTaskActivityBehavior utab){
		String result = "";
		if(utab.getMultiInstanceActivityBehavior() != null){
			result += " ElementVariable:" + utab.getMultiInstanceActivityBehavior().getCollectionElementVariable();
			result += " Variable:" + utab.getMultiInstanceActivityBehavior().getCollectionVariable();
			result += " Expression:" + utab.getMultiInstanceActivityBehavior().getCollectionExpression();
			result += " ConditionExpression:" + utab.getMultiInstanceActivityBehavior().getCompletionConditionExpression();
			result += " LoopCardinality:" + utab.getMultiInstanceActivityBehavior().getLoopCardinalityExpression();
		}
		
		TaskDefinition taskDefinition = utab.getTaskDefinition();
		
		if(taskDefinition.getAssigneeExpression() != null){
			result += " Assignee:" + taskDefinition.getAssigneeExpression().toString();
		}
		if(taskDefinition.getDescriptionExpression() != null){
			result += " Description:" + taskDefinition.getDescriptionExpression().toString();
		}
		if(taskDefinition.getDueDateExpression() != null){
			result += " DueDate:" + taskDefinition.getDueDateExpression().toString();
		}
		if(taskDefinition.getPriorityExpression() != null){
			result += " Priority:" + taskDefinition.getPriorityExpression().toString();
		}
		return result;
	}
		
	private String getUserFio(Expression e) {
		try {
			return AdminServiceProvider.get().getUserItem(e.getExpressionText()).getFio();
		} catch (Exception er) {
			return "";
		}
	}
}
