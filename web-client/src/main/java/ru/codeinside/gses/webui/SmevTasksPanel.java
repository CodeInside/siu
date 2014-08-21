/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui;

import com.google.common.base.Function;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.provider.LocalEntityProvider;
import com.vaadin.addon.jpacontainer.util.DefaultQueryModifierDelegate;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.adm.database.Group;
import ru.codeinside.adm.database.Role;
import ru.codeinside.adm.database.SmevTask;
import ru.codeinside.adm.ui.DateColumnGenerator;
import ru.codeinside.adm.ui.FilterDecorator_;
import ru.codeinside.adm.ui.FilterGenerator_;
import ru.codeinside.gses.activiti.behavior.SmevTaskBehavior;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.webui.supervisor.DiagramPanel;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

final public class SmevTasksPanel extends VerticalLayout implements TabSheet.SelectedTabChangeListener {

  final JPAContainer<SmevTask> container;
  final FilterTable table;
  Button restart = new Button("Повторить");
  Button end = new Button("Завершить");
  Button refresh = new Button("Обновить", new Button.ClickListener() {
    @Override
    public void buttonClick(Button.ClickEvent event) {
      refresh();
    }
  });
  Set<Role> roles = Flash.flash().getRoles();
  final ErrorBlock errorBlock = new ErrorBlock();
  EntityManager em = ActivitiEntityManager.INSTANCE;


  public SmevTasksPanel() {
    setSizeFull();
    setMargin(true);
    setSpacing(true);

    container = new JPAContainer<SmevTask>(SmevTask.class);
    container.setEntityProvider(new LocalEntityProvider<SmevTask>(SmevTask.class, em));
    if (!roles.contains(Role.SuperSupervisor)) {
      container.setQueryModifierDelegate(new DefaultQueryModifierDelegate() {
        @Override
        public void filtersWillBeAdded(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> query, List<Predicate> predicates) {
          Root<?> smevTask = query.getRoots().iterator().next();
          Path<Set<String>> groups = smevTask.get("groups");
          Employee currentEmployee = em.find(Employee.class, Flash.login());
          List<String> groupNames = new ArrayList<String>();
          if (roles.contains(Role.Supervisor)) {
            for (Group group : currentEmployee.getEmployeeGroups()) {
              groupNames.add(group.getName());
            }
            for (Group group : currentEmployee.getOrganizationGroups()) {
              groupNames.add(group.getName());
            }
          }
          if (roles.contains(Role.Executor)) {
            for (Group group : currentEmployee.getGroups()) {
              groupNames.add(group.getName());
            }
          }
          if (roles.contains(Role.Declarant)) {
            Path<String> path = smevTask.get("employee").get("login");
            Predicate login = criteriaBuilder.equal(path, Flash.login());
            if (!groupNames.isEmpty()) {
              predicates.add(criteriaBuilder.or(login, groups.in(groupNames), criteriaBuilder.isEmpty(groups)));
            } else if (roles.contains(Role.Executor) || roles.contains(Role.Executor)) {
              predicates.add(criteriaBuilder.or(login, criteriaBuilder.isEmpty(groups)));
            } else {
              predicates.add(login);
            }
          } else if (!groupNames.isEmpty()) {
            predicates.add(criteriaBuilder.or(criteriaBuilder.isEmpty(groups), groups.in(groupNames)));
          } else {
            predicates.add(criteriaBuilder.isEmpty(groups));
          }
        }
      });
    }
    container.addNestedContainerProperty("employee.login");
    container.addNestedContainerProperty("bid.id");
    table = new FilterTable();
    table.setFilterBarVisible(true);
    table.setFilterDecorator(new FilterDecorator_());
    table.setFilterGenerator(new FilterGenerator_());
    table.setContainerDataSource(container);
    table.setDescription("Процессы на этапе вызова СМЭВ");
    table.setSizeFull();
    table.setVisibleColumns(new String[]{
      "bid.id", "processInstanceId", "consumer", "requestType", "responseType", "employee.login", "dateCreated", "lastChange", "pingCount", "errorCount"
    });
    table.setColumnHeaders(new String[]{
      "Заявка", "Процесс", "Сервис", "Тип запроса", "Тип ответа", "Заявитель", "Дата создания", "Последнее изменение", "К-во опросов", "К-во ошибок"
    });
    table.addGeneratedColumn("dateCreated", new DateColumnGenerator("dd.MM.yyyy"));
    table.addGeneratedColumn("lastChange", new DateColumnGenerator("dd.MM.yyyy"));

    table.setSelectable(true);
    table.setNullSelectionAllowed(true);
    table.setImmediate(true);
    table.addListener(new SmevTaskClickListener());
    addComponent(table);
    setExpandRatio(table, 1f);
    addComponent(errorBlock);
    setExpandRatio(errorBlock, 0.7f);
    restart.addListener(new UserActionListener(true));
    end.addListener(new UserActionListener(false));
  }

  final private class SmevTaskClickListener implements Property.ValueChangeListener {

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
      final FilterTable table = (FilterTable) event.getProperty();
      final Object index = table.getValue();
      Long smevTaskId = null;
      if (index != null) {
        final Item item = table.getItem(index);
        smevTaskId = Fn.getValue(item, "id", Long.class);
      }
      if (smevTaskId != null) {
        final SmevTask smevTask = em.find(SmevTask.class, smevTaskId);
        errorBlock.setTask(smevTask);
      } else {
        errorBlock.setTask(null);
      }
    }
  }

  private class UserActionListener implements Button.ClickListener {
    boolean repeat;

    UserActionListener(boolean repeat) {
      this.repeat = repeat;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
      final Object index = table.getValue();
      Long smevTaskId = null;
      Integer revision = null;
      if (index != null) {
        final Item item = table.getItem(index);
        smevTaskId = Fn.getValue(item, "id", Long.class);
        revision = Fn.getValue(item, "revision", Integer.class);
      }
      if (smevTaskId != null) {
        SmevTask current = em.find(SmevTask.class, smevTaskId);
        if (current.getRevision() == revision) {
          executeActivity(current, repeat);
        } else {
          getWindow().showNotification("Действие уже произошло");
        }
        refresh();
      }
    }

    void executeActivity(final SmevTask smevTask, final boolean repeat) {
      Flash.flash().getActivitiService().withEngine(new Function<ProcessEngine, Object>() {
        @Override
        public Object apply(ProcessEngine engine) {
          CommandExecutor commandExecutor = ((ServiceImpl) engine.getTaskService()).getCommandExecutor();
          return commandExecutor.execute(
            new Command<Object>() {
              @Override
              public Object execute(CommandContext commandContext) {
                ExecutionEntity execution = commandContext.getExecutionManager().findExecutionById(smevTask.getExecutionId());
                ActivityImpl activity = execution.getProcessDefinition().findActivity(smevTask.getTaskId());
                try {
                  ((SmevTaskBehavior) activity.getActivityBehavior()).doUserAction(execution, repeat);
                } catch (Exception e) {
                  throw new ActivitiException("user reaction fail", e);
                }
                return null;
              }
            }
          );
        }
      });
    }
  }

  final class ErrorBlock extends HorizontalLayout {
    final TextArea textArea = new TextArea("Стек ошибки:");
    final VerticalLayout diagramLayout = new VerticalLayout();

    ErrorBlock() {
      setSizeFull();
      setSpacing(true);

      textArea.setSizeFull();
      textArea.setStyleName("small");
      diagramLayout.setCaption("Схема приостановленного процеса:");
      diagramLayout.setSizeFull();

      final VerticalLayout wrapper = new VerticalLayout();
      wrapper.setSizeUndefined();
      wrapper.setSpacing(true);
      wrapper.setWidth(110, UNITS_PIXELS);
      wrapper.addComponent(refresh);
      wrapper.addComponent(restart);
      wrapper.addComponent(end);

      final VerticalLayout buttonsLayout = new VerticalLayout();
      buttonsLayout.addComponent(wrapper);
      buttonsLayout.setSizeFull();
      buttonsLayout.setWidth(110, UNITS_PIXELS);

      addComponent(buttonsLayout);
      addComponent(diagramLayout);
      addComponent(textArea);

      setExpandRatio(diagramLayout, 0.42f);
      setExpandRatio(textArea, 0.42f);

      setTask(null);
    }

    public void setTask(final SmevTask smevTask) {
      final boolean enabled = smevTask != null;
      textArea.setReadOnly(false);
      textArea.setValue(enabled ? smevTask.getFailure() : "");
      textArea.setReadOnly(true);
      textArea.setEnabled(enabled);
      restart.setEnabled(enabled && smevTask.isNeedUserReaction());
      end.setEnabled(enabled && smevTask.isNeedUserReaction());
      diagramLayout.setEnabled(enabled);
      diagramLayout.removeAllComponents();
      if (enabled) {
        diagramLayout.addComponent(
          new DiagramPanel(smevTask.getBid().getProcedureProcessDefinition().getProcessDefinitionId(), smevTask.getExecutionId())
        );
      }
    }
  }

  @Override
  public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
    if (this != event.getTabSheet().getSelectedTab()) {
      return;
    }
    refresh();
  }

  void refresh() {
    Object oldValue = table.getValue();
    table.setValue(null);
    table.removeAllItems();
    table.refreshRowCache();
    if (oldValue != null) {
      table.setValue(oldValue);
    } else {
      errorBlock.setTask(null);
    }
  }

}
