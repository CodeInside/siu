/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui.employee;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.dialogs.ConfirmDialog;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.UserItem;
import ru.codeinside.adm.database.Group;
import ru.codeinside.adm.database.Role;
import ru.codeinside.adm.ui.LazyLoadingContainer2;
import ru.codeinside.adm.ui.RepeatPasswordValidator;
import ru.codeinside.gses.webui.components.EmployeeInfo;

import java.util.*;

public abstract class TableEmployee extends VerticalLayout {


  static final Action ACTION_VIEW = new Action("Просмотр информации пользователя");
  static final Action ACTION_EDIT = new Action("Редактировать пользователя");
  static final Action ACTION_LOCK = new Action("Заблокировать пользователя");
  static final Action ACTION_UNLOCK = new Action("Разблокировать пользователя");
  static final Action[] ACTIONS = new Action[]{ACTION_VIEW, ACTION_EDIT};
  private static final long serialVersionUID = -8903404478050765452L;
  protected boolean lockedFilterValue;
  public Layout hr;


  static public OptionGroup createRoleOptionGroup(String caption) {
    final OptionGroup roles = new OptionGroup(caption, ImmutableSet.copyOf(Role.values()));
    for (final Role role : Role.values()) {
      roles.setItemCaption(role, role.description);
    }
    roles.setMultiSelect(true);
    roles.setNullSelectionAllowed(true);
    roles.setImmediate(true);
    return roles;
  }

  public static void addListener(final Table one, final Table two) {
    one.addListener(new Property.ValueChangeListener() {

      private static final long serialVersionUID = 1L;

      public void valueChange(ValueChangeEvent event) {
        Object value = event.getProperty().getValue();
        if (value != null) {
          one.removeItem(value);
          for (Group group : AdminServiceProvider.get().findGroupByName(value.toString())) {
            two.addItem(new Object[]{group.getName(), group.getTitle()}, group.getName());
          }
        }
      }
    });
  }

  public static void table(final HorizontalLayout executorGroups, Table table) {
    table.addContainerProperty("Код", String.class, "");
    table.addContainerProperty("Название", String.class, "");
    table.setHeight("250px");
    table.setWidth("350px");
    table.setSelectable(true);
    table.setMultiSelect(false);
    table.setImmediate(true);
    executorGroups.addComponent(table);
  }

  public static void setRolesEnabled(OptionGroup optionGroup,
                                     Component certificateBlock,
                                     Component executorGroups,
                                     HorizontalLayout supervisorGroupsEmp,
                                     HorizontalLayout supervisorGroupsOrg) {

    Set<Role> roles = (Set) optionGroup.getValue();
    Boolean e = roles.isEmpty();
    Boolean c = roles.contains(Role.Administrator);
    for (Object role : optionGroup.getItemIds()) {
      if (role == Role.Administrator) {
        optionGroup.setItemEnabled(role, e || c);
      } else {
        optionGroup.setItemEnabled(role, e || !c);
      }
    }
    certificateBlock.setVisible(roles.contains(Role.Executor) || roles.contains(Role.Declarant)
      || roles.contains(Role.Supervisor) || roles.contains(Role.SuperSupervisor));
    executorGroups.setVisible(roles.contains(Role.Executor));
    supervisorGroupsEmp.setVisible(roles.contains(Role.Supervisor));
    supervisorGroupsOrg.setVisible(roles.contains(Role.Supervisor));
  }

  public static TextField addTextField(AbstractComponentContainer container, String widthColumn, String content) {
    HorizontalLayout l;
    l = new HorizontalLayout();
    Label label = new Label(content);
    label.setWidth(widthColumn);
    l.addComponent(label);
    l.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
    final TextField field = new TextField();
    l.addComponent(field);
    container.addComponent(l);
    return field;
  }

  public static PasswordField addPasswordField(AbstractComponentContainer container, String widthColumn, String content) {
    HorizontalLayout l;
    l = new HorizontalLayout();
    Label label = new Label(content);
    label.setWidth(widthColumn);
    l.addComponent(label);
    l.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
    final PasswordField field = new PasswordField();
    l.addComponent(field);
    container.addComponent(l);
    return field;
  }

  final static class RolesColumn implements CustomTable.ColumnGenerator {

    final static Joiner joiner = Joiner.on(", ");

    @Override
    public Object generateCell(CustomTable source, Object itemId, Object columnId) {
      Object object = source.getContainerProperty(itemId, columnId).getValue();
      if (object instanceof Set) {
        final Set<Role> roles = (Set) object;
        final TreeSet<String> result = new TreeSet<String>();
        for (Role role : roles) {
          result.add(role.name() + "(" + role.description + ")");
        }
        return joiner.join(result);
      }
      return null;
    }
  }

  protected void addContextMenu(final CustomTable table) {
    table.addActionHandler(new Action.Handler() {

      private static final long serialVersionUID = 1L;

      public Action[] getActions(Object target, Object sender) {
        ArrayList<Action> result = new ArrayList<Action>(ACTIONS.length + 1);
        result.addAll(Arrays.asList(ACTIONS));
        if (lockedFilterValue) {
          result.add(ACTION_UNLOCK);
        } else {
          result.add(ACTION_LOCK);
        }
        return result.toArray(new Action[result.size()]);
      }

      public void handleAction(Action action, Object sender, Object target) {
        final Item item = table.getItem(table.getValue());
        if (item != null) {
          if (ACTION_EDIT == action) {
            edit(table);
            table.refreshRowCache();
          } else if (ACTION_VIEW == action) {
            view(table);
            table.refreshRowCache();
          } else if (ACTION_LOCK == action) {
            lockUserActionHandler(table);
            table.refreshRowCache();
          } else if (ACTION_UNLOCK == action) {
            unlockUserActionHandler(table);
            table.refreshRowCache();
          }
        }
      }
    });
  }

  protected void unlockUserActionHandler(final CustomTable table) {
    ConfirmDialog.show(table.getWindow(),
        "Снятие Блокировки",
        "Подтвердите снятие блокировки",
        "Подтверждаю",
        "Отмена",
        new ConfirmDialog.Listener() {
          public void onClose(ConfirmDialog dialog) {
            if (dialog.isConfirmed()) {
              changeLockAttribute(table, false);
            }
          }
        });
  }

  protected void lockUserActionHandler(final CustomTable table) {
    ConfirmDialog.show(table.getWindow(),
        "Блокировка",
        "Подтвердите блокировку",
        "Подтверждаю",
        "Отмена",
        new ConfirmDialog.Listener() {
          public void onClose(ConfirmDialog dialog) {
            if (dialog.isConfirmed()) {
              changeLockAttribute(table, true);
            }
          }
        });
  }

  private void changeLockAttribute(CustomTable table, boolean newLockedPropertyValue) {
    final Item item = table.getItem(table.getValue());
    final String login = (String) item.getItemProperty("login").getValue();
    final UserItem userItem = AdminServiceProvider.get().getUserItem(login);
    userItem.setLocked(newLockedPropertyValue);
    AdminServiceProvider.get().setUserItem(login, userItem);
    refresh(table);
    if (newLockedPropertyValue) {
      getWindow().showNotification("Пользователь " + login + " заблокирован");
    } else {
      getWindow().showNotification("Пользователь " + login + " разблокирован");
    }
  }

  protected void edit(final CustomTable table) {
    final Item item = table.getItem(table.getValue());
    final String login = (String) item.getItemProperty("login").getValue();
    final UserItem userItem = AdminServiceProvider.get().getUserItem(login);

    final Panel layout = new Panel();
    ((Layout.SpacingHandler) layout.getContent()).setSpacing(true);
    layout.setSizeFull();
    layout.addComponent(new Label("Информация пользователя " + login));

    String widthColumn = "100px";

    final PasswordField fieldPass = addPasswordField(layout, widthColumn, "Пароль");
    final PasswordField fieldPassRepeat = addPasswordField(layout, widthColumn, "Подтверждение пароля");
    fieldPassRepeat.addValidator(new RepeatPasswordValidator(fieldPass));
    final TextField fieldFIO = addTextField(layout, widthColumn, "ФИО");
    fieldFIO.setValue(userItem.getFio());

    HorizontalLayout l1 = new HorizontalLayout();
    Label labelRole = new Label("Права");
    labelRole.setWidth(widthColumn);
    l1.addComponent(labelRole);
    l1.setComponentAlignment(labelRole, Alignment.MIDDLE_LEFT);
    final OptionGroup roleOptionGroup = TableEmployee.createRoleOptionGroup(null);
    roleOptionGroup.setValue(userItem.getRoles());
    l1.addComponent(roleOptionGroup);
    layout.addComponent(l1);

    final CertificateBlock certificateBlock = new CertificateBlock(userItem);
    layout.addComponent(certificateBlock);

    final ExecutorGroupsBlock executorGroupsBlock = new ExecutorGroupsBlock(userItem);
    layout.addComponent(executorGroupsBlock);

    final HorizontalLayout supervisorGroupsEmp = new HorizontalLayout();
    supervisorGroupsEmp.setMargin(true, true, true, false);
    supervisorGroupsEmp.setSpacing(true);
    supervisorGroupsEmp.setCaption("Назначить группы сотрудников для контроля");
    final Table allSupervisorGroupsEmp = new Table();
    allSupervisorGroupsEmp.setCaption("Доступные");
    table(supervisorGroupsEmp, allSupervisorGroupsEmp);
    final Table currentSupervisorGroupsEmp = new Table();
    currentSupervisorGroupsEmp.setCaption("Отобранные");
    table(supervisorGroupsEmp, currentSupervisorGroupsEmp);
    for (String groupName : AdminServiceProvider.get().getEmpGroupNames()) {
      for (Group group : AdminServiceProvider.get().findGroupByName(groupName)) {
        if (userItem.getEmployeeGroups().contains(groupName)) {
          currentSupervisorGroupsEmp.addItem(new Object[]{groupName, group.getTitle()}, groupName);
        } else {
          allSupervisorGroupsEmp.addItem(new Object[]{groupName, group.getTitle()}, groupName);
        }
      }
    }
    addListener(allSupervisorGroupsEmp, currentSupervisorGroupsEmp);
    addListener(currentSupervisorGroupsEmp, allSupervisorGroupsEmp);
    layout.addComponent(supervisorGroupsEmp);

    final HorizontalLayout supervisorGroupsOrg = new HorizontalLayout();
    supervisorGroupsOrg.setMargin(true, true, true, false);
    supervisorGroupsOrg.setSpacing(true);
    supervisorGroupsOrg.setCaption("Назначить группы организаций для контроля");
    final Table allSupervisorGroupsOrg = new Table();
    allSupervisorGroupsOrg.setCaption("Доступные");
    table(supervisorGroupsOrg, allSupervisorGroupsOrg);
    final Table currentSupervisorGroupsOrg = new Table();
    currentSupervisorGroupsOrg.setCaption("Отобранные");
    table(supervisorGroupsOrg, currentSupervisorGroupsOrg);
    for (String groupName : AdminServiceProvider.get().getOrgGroupNames()) {
      for (Group group : AdminServiceProvider.get().findGroupByName(groupName)) {
        if (userItem.getOrganizationGroups().contains(groupName)) {
          currentSupervisorGroupsOrg.addItem(new Object[]{groupName, group.getTitle()}, groupName);
        } else {
          allSupervisorGroupsOrg.addItem(new Object[]{groupName, group.getTitle()}, groupName);
        }
      }
    }
    addListener(allSupervisorGroupsOrg, currentSupervisorGroupsOrg);
    addListener(currentSupervisorGroupsOrg, allSupervisorGroupsOrg);
    layout.addComponent(supervisorGroupsOrg);

    setRolesEnabled(roleOptionGroup, certificateBlock, executorGroupsBlock, supervisorGroupsEmp, supervisorGroupsOrg);
    roleOptionGroup.addListener(new Listener() {
      private static final long serialVersionUID = 1L;

      public void componentEvent(Event event) {
        setRolesEnabled(roleOptionGroup, certificateBlock, executorGroupsBlock, supervisorGroupsEmp, supervisorGroupsOrg);
      }
    });

    Button cancel = new Button("Отмена", new Button.ClickListener() {

      private static final long serialVersionUID = 1L;

      @Override
      public void buttonClick(Button.ClickEvent event) {
        removeComponent(layout);
        addComponent(table);
        table.setValue(null);
        if (hr != null) {
          hr.setVisible(true);
        }
        setExpandRatio(table, 1f);
      }
    });

    Button apply = new Button("Применить", new Button.ClickListener() {

      private static final long serialVersionUID = 1L;

      public void buttonClick(ClickEvent event) {
        String password = (String) fieldPass.getValue();
        String passwordRepeat = (String) fieldPassRepeat.getValue();
        if (!fieldPassRepeat.isValid() || !(password.equals(passwordRepeat))) {
          getWindow().showNotification("Новый пароль не совпадает с подтверждением пароля", Window.Notification.TYPE_ERROR_MESSAGE);
          return;
        }

        String fio = (String) fieldFIO.getValue();
        Set<Role> roles = (Set) roleOptionGroup.getValue();

        TreeSet<String> groupExecutor = executorGroupsBlock.getGroups();
        TreeSet<String> groupSupervisorEmp = new TreeSet<String>(
            (Collection<String>) currentSupervisorGroupsEmp.getItemIds());
        TreeSet<String> groupSupervisorOrg = new TreeSet<String>(
            (Collection<String>) currentSupervisorGroupsOrg.getItemIds());

        boolean modified = false;

        if (certificateBlock.isCertificateWasRemoved()) {
          userItem.setX509(null);
          modified = true;
        }

        if (!password.equals("") && password.equals(passwordRepeat)) {
          userItem.setPassword1(password);
          userItem.setPassword2(passwordRepeat);
          modified = true;
        }
        if (!fio.trim().equals("") && !fio.equals(userItem.getFio())) {
          userItem.setFio(fio);
          userItem.setX509(null);
          modified = true;
        }
        if (!roles.equals(userItem.getRoles())) {
          userItem.setRoles(roles);
          modified = true;
        }
        if (!groupExecutor.equals(userItem.getGroups())) {
          userItem.setGroups(groupExecutor);
          modified = true;
        }
        if (!groupSupervisorEmp.equals(userItem.getEmployeeGroups())) {
          userItem.setEmployeeGroups(groupSupervisorEmp);
          modified = true;
        }
        if (!groupSupervisorOrg.equals(userItem.getOrganizationGroups())) {
          userItem.setOrganizationGroups(groupSupervisorOrg);
          modified = true;
        }

        if (modified) {
          // TODO : обновление userInfoPanel
          // if (getApplication().getUser().equals(login)) {
          // ((AdminApp) getApplication()).getUserInfoPanel().setRole(
          // userItem.getRoles().toString());
          // }
          AdminServiceProvider.get().setUserItem(login, userItem);
          final Container container = table.getContainerDataSource();
          if (container instanceof LazyLoadingContainer2) {
            ((LazyLoadingContainer2) container).fireItemSetChange();
          }
          getWindow().showNotification("Пользователь " + login + " изменен");
        } else {
          getWindow().showNotification("Изменений нет");
        }

        removeComponent(layout);
        addComponent(table);
        table.setValue(null);
        if (hr != null) {
          hr.setVisible(true);
        }
        setExpandRatio(table, 1f);
        refresh(table);
      }
    });

    cancel.setClickShortcut(KeyCode.ESCAPE, 0);
    HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    buttons.addComponent(apply);
    buttons.addComponent(cancel);
    layout.addComponent(buttons);
    removeComponent(table);
    addComponent(layout);
    setExpandRatio(layout, 1f);
  }

  protected void view(final AbstractSelect table) {
    final String login = (String) table.getItem(table.getValue()).getItemProperty("login").getValue();
    Button cancel = new Button("Назад");
    EmployeeInfo info = new EmployeeInfo(login, cancel);
    cancel.addListener(new BackAction(info, table));
    cancel.setClickShortcut(KeyCode.ESCAPE, 0);
    addComponent(info);
    setExpandRatio(info, 1f);
    removeComponent(table);
  }

  public void refreshList() {
    for (Component child : ImmutableList.copyOf(this.getComponentIterator())) {
      if (child instanceof CustomTable) {
        refresh((CustomTable) child);
      }
    }
  }

  protected void refresh(final CustomTable table) {
    table.setValue(null);
    Container container = table.getContainerDataSource();
    if (container instanceof JPAContainer) {
      ((JPAContainer) container).getEntityProvider().refresh();
    }
    table.refreshRowCache();
  }

  final class BackAction implements Button.ClickListener {
    private final EmployeeInfo info;
    private final AbstractSelect table;

    public BackAction(EmployeeInfo info, AbstractSelect table) {
      this.info = info;
      this.table = table;
    }

    @Override
    public void buttonClick(ClickEvent event) {
      removeComponent(info);
      addComponent(table);
      setExpandRatio(table, 1f);
      if (hr != null) {
        hr.setVisible(true);
      }
      if (table instanceof CustomTable) {
        refresh((CustomTable) table);
      }
    }
  }
}
