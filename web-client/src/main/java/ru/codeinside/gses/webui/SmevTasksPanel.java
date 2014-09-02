/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.provider.LocalEntityProvider;
import com.vaadin.addon.jpacontainer.util.DefaultQueryModifierDelegate;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;
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
import ru.codeinside.adm.ui.BooleanColumnGenerator;
import ru.codeinside.adm.ui.DateColumnGenerator;
import ru.codeinside.adm.ui.FilterDecorator_;
import ru.codeinside.adm.ui.FilterGenerator_;
import ru.codeinside.gses.activiti.behavior.SmevTaskBehavior;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.webui.supervisor.DiagramPanel;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

final public class SmevTasksPanel extends VerticalLayout implements TabSheet.SelectedTabChangeListener {


  final FilterTable table;

  Set<Role> roles = Flash.flash().getRoles();
  final ErrorBlock errorBlock = new ErrorBlock();

  public SmevTasksPanel() {
    setSizeFull();
    setMargin(true);
    setSpacing(true);

    final JPAContainer<SmevTask> container = new JPAContainer<SmevTask>(SmevTask.class);
    container.setEntityProvider(new LocalEntityProvider<SmevTask>(SmevTask.class, ActivitiEntityManager.INSTANCE));
    if (!roles.contains(Role.SuperSupervisor)) {
      container.setQueryModifierDelegate(new DefaultQueryModifierDelegate() {
        @Override
        public void filtersWillBeAdded(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> query, List<Predicate> predicates) {
          Root<?> smevTask = query.getRoots().iterator().next();
          Path<Set<String>> groups = smevTask.get("groups");
          Employee currentEmployee = ActivitiEntityManager.INSTANCE.find(Employee.class, Flash.login());
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
    table.setFilterGenerator(new FilterGenerator_(ImmutableList.of(
      "id", "bid.id", "revision", "pingCount", "errorCount", "responseType", "requestType"), ImmutableList.of(
      "needUserReaction")));
    table.setContainerDataSource(container);
    table.setDescription("Процессы на этапе вызова СМЭВ");
    table.setSizeFull();
    table.setVisibleColumns(new String[]{
      "bid.id", "consumer", "revision", "requestType", "responseType", "employee.login", "needUserReaction", "lastChange", "pingCount", "errorCount"
    });
    table.setColumnHeaders(new String[]{
      "Заявка", "Потребитель", "Ревизия", "Отправлено", "Получено", "Заявитель", "Требует внимания", "Последнее изменение", "К-во опросов", "К-во ошибок"
    });
    table.addGeneratedColumn("dateCreated", new DateColumnGenerator("dd.MM.yyyy"));
    table.addGeneratedColumn("lastChange", new DateColumnGenerator("dd.MM.yyyy"));
    table.addGeneratedColumn("needUserReaction", new BooleanColumnGenerator());

    table.setSelectable(true);
    table.setNullSelectionAllowed(true);
    table.setImmediate(true);
    table.addListener(new SmevTaskClickListener());
    table.setCellStyleGenerator(new SmevStylist(table));
    addComponent(table);
    setExpandRatio(table, 1f);
    addComponent(errorBlock);
    setExpandRatio(errorBlock, 0.7f);
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
        final SmevTask smevTask = ActivitiEntityManager.INSTANCE.find(SmevTask.class, smevTaskId);
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
        SmevTask current = ActivitiEntityManager.INSTANCE.find(SmevTask.class, smevTaskId);
        if (current.getRevision() == revision) {
          executeActivity(current, repeat);
          getWindow().showNotification(
            repeat ? "Исполнение возобновлено" : "Исполнение прервано",
            Window.Notification.TYPE_HUMANIZED_MESSAGE);
        } else {
          getWindow().showNotification("Действие уже произошло!");
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

    final TextArea textArea = new TextArea("Последняя ошибка:");
    final VerticalLayout diagramLayout = new VerticalLayout();
    final Button refresh = new Button("Обновить");
    final Button restart = new Button("Возобновить");
    final Button end = new Button("Прервать");

    ErrorBlock() {
      setSizeFull();
      setSpacing(true);

      textArea.setSizeFull();
      textArea.setStyleName("small");

      diagramLayout.setCaption("Маршрут исполнения заявки:");
      diagramLayout.setSizeFull();

      VerticalLayout buttons = new VerticalLayout();
      buttons.setSizeUndefined();
      buttons.setMargin(false);
      buttons.setSpacing(true);
      buttons.addComponent(refresh);
      buttons.addComponent(new Label(" "));
      buttons.addComponent(new Label(" "));
      buttons.addComponent(restart);
      buttons.addComponent(end);

      addComponent(buttons);
      addComponent(diagramLayout);
      addComponent(textArea);
      setExpandRatio(diagramLayout, 0.42f);
      setExpandRatio(textArea, 0.42f);
      setTask(null);

      refresh.setStyleName(BaseTheme.BUTTON_LINK);
      refresh.setDescription("Обновить таблицу СМЭВ");
      refresh.setIcon(new ThemeResource("../runo/icons/32/reload.png"));
      refresh.addListener(new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent event) {
          refresh();
          getWindow().showNotification("Таблица обновлена", Window.Notification.TYPE_HUMANIZED_MESSAGE);
        }
      });

      restart.setStyleName(BaseTheme.BUTTON_LINK);
      restart.setDescription("Возобновить процесс");
      restart.setIcon(new ThemeResource("../runo/icons/32/ok.png"));
      restart.addListener(new UserActionListener(true));

      end.setStyleName(BaseTheme.BUTTON_LINK);
      end.setDescription("Остановить процесс");
      end.setIcon(new ThemeResource("../runo/icons/32/cancel.png"));
      end.addListener(new UserActionListener(false));

    }

    public void setTask(final SmevTask smevTask) {
      boolean enabled = smevTask != null;

      if (!enabled) {
        textArea.setVisible(false);
      } else {
        String failure = smevTask.getFailure();
        if (failure == null) {
          textArea.setVisible(false);
        } else {
          textArea.setVisible(true);
          textArea.setReadOnly(false);
          textArea.setValue(failure);
          textArea.setReadOnly(true);
        }
      }
      boolean actionEnabled = enabled && smevTask.isNeedUserReaction();
      restart.setEnabled(actionEnabled);
      restart.setVisible(actionEnabled);
      end.setEnabled(actionEnabled);
      end.setVisible(actionEnabled);
      diagramLayout.setVisible(enabled);
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
