/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree.CollapseEvent;
import com.vaadin.ui.Tree.CollapseListener;
import com.vaadin.ui.Tree.ExpandEvent;
import com.vaadin.ui.Tree.ExpandListener;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;
import org.apache.commons.lang.StringUtils;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.UserItem;
import ru.codeinside.adm.database.Group;
import ru.codeinside.adm.database.Organization;
import ru.codeinside.adm.database.Role;
import ru.codeinside.adm.ui.employee.CertificateBlock;
import ru.codeinside.adm.ui.employee.ExecutorGroupsBlock;
import ru.codeinside.adm.ui.employee.TableEmployee;
import ru.codeinside.adm.ui.employee.TableOrganizationEmployee;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class TreeTableOrganization extends HorizontalLayout implements Property.ValueChangeListener {

  protected static final String ID_PROPERTY = "id";
  protected static final String NAME_PROPERTY = "Организации";
  private static final long serialVersionUID = -8903404478050765452L;
  private static final String NAME_ORG = "Название организации";
  public static Pattern GROUP = Pattern.compile("[a-zA-Zа-яА-Я][a-zA-Zа-яА-Я0-9_]*");
  final TreeTable treetable;
  private Panel panel = new Panel();
  private boolean lockExpandListener;

  public TreeTableOrganization() {
    setSizeFull();
    treetable = new TreeTable();
    treetable.setSizeFull();
    treetable.setSelectable(true);
    treetable.setMultiSelect(false);
    treetable.addListener(this);
    treetable.setImmediate(true);
    treetable.setValue(null);
    setMargin(true);
    // Add Table columns
    treetable.addContainerProperty(NAME_PROPERTY, String.class, "");

    fillTable(treetable);

    treetable.addListener(new ExpandListener() {

      private static final long serialVersionUID = 1L;

      public void nodeExpand(ExpandEvent event) {
        if (lockExpandListener) {
          return;
        }
        Object valuePropertyEvent = event.getItemId();
        Organization org = AdminServiceProvider.get().findOrganizationById((Long) valuePropertyEvent);
        Set<Organization> childs = org.getOrganizations();
        if (!(childs.isEmpty())) {
          treetable.setChildrenAllowed((Long) valuePropertyEvent, true);

          for (Organization o : childs) {
            treetable.addItem(new Object[]{o.getName()}, o.getId());
            treetable.setCollapsed(o.getId(), true);
          }

          for (Organization o : childs) {
            treetable.setParent(o.getId(), (Long) valuePropertyEvent);
          }

          for (Organization o : childs) {
            treetable.setChildrenAllowed(o.getId(), !(o.getOrganizations().isEmpty()));
          }
        }

      }
    });

    treetable.addListener(new CollapseListener() {

      private static final long serialVersionUID = 1L;

      public void nodeCollapse(CollapseEvent event) {
        if (lockExpandListener) {
          return;
        }
        Set<Object> delete = new HashSet<Object>();
        Collection<?> children = treetable.getChildren(event.getItemId());
        if (children != null) {
          for (Object child : children) {
            removeRecursively(child, delete);
          }
        }
        for (Object o : delete) {
          treetable.setCollapsed(o, true);
          treetable.removeItem(o);
        }
      }

      private void removeRecursively(Object object, Set<Object> delete) {
        Collection<?> children = treetable.getChildren(object);
        if (children != null) {
          for (Object child : children) {
            removeRecursively(child, delete);
          }
        }
        delete.add(object);
      }
    });

    panel.setStyleName(Reindeer.PANEL_LIGHT);
    panel.setSizeFull();
    panel.addComponent(new ButtonCreateOrganization(treetable));

    final HorizontalSplitPanel horiz = new HorizontalSplitPanel();
    horiz.setSplitPosition(25); // percent
    horiz.setSizeFull();
    addComponent(horiz);
    TextField orgFilter = new TextField();
    orgFilter.setImmediate(true);
    orgFilter.addListener(new FieldEvents.TextChangeListener() {

      List<Organization> organizations;
      List<Long> organizationIds;

      @Override
      public void textChange(FieldEvents.TextChangeEvent event) {
        String name = StringUtils.trimToNull(event.getText());
        if (name != null) {
          lockExpandListener = true;
          organizations = AdminServiceProvider.get().findOrganizationIdsByName(name);
          treetable.removeAllItems();
          organizationIds = new ArrayList<Long>();
          for (Organization org : organizations) {
            for (Organization o1 : getPath(org)) {
              if (!treetable.containsId(o1.getId())) {
                treetable.addItem(new Object[]{o1.getName()}, o1.getId());
                if (o1.getParent() != null) {
                  treetable.setParent(o1.getId(), o1.getParent().getId());
                }
                treetable.setChildrenAllowed(o1.getId(), !(o1.getOrganizations().isEmpty()));
                treetable.setCollapsed(o1.getId(), false);
              }
            }
            organizationIds.add(org.getId());
          }

          treetable.setCellStyleGenerator(new Table.CellStyleGenerator() {
            @Override
            public String getStyle(Object itemId, Object propertyId) {
              if (propertyId == null) {
                if (!organizationIds.contains(itemId)) {
                  return "gray";
                }
              }
              return null;
            }
          });

        } else {
          lockExpandListener = false;
          treetable.removeAllItems();
          fillTable(treetable);
          treetable.setCellStyleGenerator(new Table.CellStyleGenerator() {
            @Override
            public String getStyle(Object itemId, Object propertyId) {
              return null;
            }
          });
        }
      }

      private List<Organization> getPath(Organization org) {
        List<Organization> list = new LinkedList<Organization>();
        while (org != null) {
          list.add(0, org);
          org = org.getParent();
        }
        return list;
      }
    });
    VerticalLayout vl = new VerticalLayout();
    vl.setSpacing(true);
    vl.setSizeFull();
    vl.addComponent(orgFilter);
    vl.addComponent(treetable);
    vl.setExpandRatio(treetable, 0.9f);
    horiz.addComponent(vl);
    horiz.addComponent(panel);
  }

  @Override
  public void valueChange(ValueChangeEvent event) {
    Object valuePropertyEvent = event.getProperty().getValue();
    if (valuePropertyEvent != null) {
      showOrganization((Long) valuePropertyEvent);
    } else {
      panel.removeAllComponents();
      panel.addComponent(new ButtonCreateOrganization(treetable));
    }
  }

  void showOrganization(Long id) {
    TableEmployee tableEmployee = showOrganizationLabelsAndButtons(id);
    panel.addComponent(tableEmployee);
  }

  private TableEmployee showOrganizationLabelsAndButtons(Long id) {
    panel.removeAllComponents();
    panel.addComponent(new ButtonCreateOrganization(treetable));
    treetable.setCollapsed(id, false);
    Organization org = AdminServiceProvider.get().findOrganizationById(id);
    Label nameLabel = new Label("Название: " + org.getName());
    nameLabel.setStyleName("h2");
    panel.addComponent(nameLabel);
    if (org.getCreator() != null) {
      Label creatorLabel = new Label("Создатель: " + org.getCreator().getLogin());
      panel.addComponent(creatorLabel);
    }

    Format formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    Label dateLabel = new Label("Дата создания: " + formatter.format(org.getDate()));
    panel.addComponent(dateLabel);
    panel.addComponent(groupLabel(id));
    HorizontalLayout layoutButton = new HorizontalLayout();
    layoutButton.addComponent(buttonEditOrganization(org, nameLabel));
    layoutButton.addComponent(buttonCreateOrganization(org));
    TableEmployee tableEmployee = new TableOrganizationEmployee(id);
    tableEmployee.setMargin(true, false, false, false);
    layoutButton.addComponent(buttonCreateEmployee(id));
    layoutButton.setMargin(true, false, false, false);
    layoutButton.addComponent(createGroupLabel(id));
    panel.addComponent(layoutButton);
    return tableEmployee;
  }

  private Component buttonCreateOrganization(final Organization orgHigh) {

    HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    buttons.setMargin(false, true, false, false);

    final Button createOrg = new Button("Добавить орг. единицу", new Button.ClickListener() {

      private static final long serialVersionUID = 1L;

      public void buttonClick(ClickEvent event) {
        showOrganizationLabelsAndButtons(orgHigh.getId());
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        panel.addComponent(layout);
        final Form form = new Form();
        form.addField(NAME_ORG, new TextField(NAME_ORG));
        form.getField(NAME_ORG).setRequired(true);
        form.getField(NAME_ORG).setRequiredError("Введите название организации");
        form.getField(NAME_ORG).addValidator(
          new StringLengthValidator("Название организации должно быть не более 255 символов", 1, 255,
            false));
        form.getField(NAME_ORG).setWidth("500px");
        layout.addComponent(form);
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        Button create = new Button("Добавить", new Button.ClickListener() {
          private static final long serialVersionUID = 1L;

          public void buttonClick(ClickEvent event) {
            try {
              form.commit();
            } catch (Exception e) {
              return;
            }
            String nameOrg = form.getField(NAME_ORG).getValue().toString();

            Set<Organization> orgs = orgHigh.getOrganizations();
            Boolean bool = true;
            for (Organization org : orgs) {
              if (org.getName().equals(nameOrg)) {
                bool = false;
                break;
              }
            }
            if (bool) {
              Organization org = AdminServiceProvider.get().createOrganization(nameOrg,
                getApplication().getUser().toString(), orgHigh);
              treetable.addItem(new Object[]{org.getName()}, org.getId());
              treetable.setCollapsed(org.getId(), true);
              treetable.setChildrenAllowed(orgHigh.getId(), true);
              treetable.setChildrenAllowed(org.getId(), false);
              treetable.setParent(org.getId(), orgHigh.getId());
              treetable.setValue(org.getId());
              treetable.requestRepaint();
              showOrganization(orgHigh.getId());
              getWindow().showNotification("Организация " + nameOrg + " создана");
            } else {
              getWindow().showNotification("Организация c названием " + nameOrg + " уже существует");
            }
          }
        });
        buttons.addComponent(create);
        addButtonCancel(orgHigh, buttons);
        form.getFooter().addComponent(buttons);
      }
    });
    buttons.addComponent(createOrg);
    return buttons;
  }

  private Component buttonEditOrganization(final Organization org, final Label nameLabel) {

    setSpacing(true);

    HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    buttons.setMargin(false, true, false, false);
    addComponent(buttons);

    final Button createOrg = new Button("Редактировать организацию", new Button.ClickListener() {

      private static final long serialVersionUID = 1L;

      public void buttonClick(ClickEvent event) {
        showOrganizationLabelsAndButtons(org.getId());
        final String oldNameOrg = org.getName();
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        panel.addComponent(layout);
        final Form form = new Form();
        form.addField(NAME_ORG, new TextField(NAME_ORG));
        form.getField(NAME_ORG).addValidator(
          new StringLengthValidator("Название организации должно быть не более 255 символов", 0, 255,
            true));
        form.getField(NAME_ORG).setWidth("500px");
        form.getField(NAME_ORG).setValue(oldNameOrg);
        layout.addComponent(form);
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        Button save = new Button("Сохранить", new Button.ClickListener() {
          private static final long serialVersionUID = 1L;

          public void buttonClick(ClickEvent event) {
            try {
              form.commit();
            } catch (Exception e) {
              return;
            }
            String newNameOrg = form.getField(NAME_ORG).getValue().toString();
            if (!(newNameOrg.equals(oldNameOrg) || newNameOrg.equals(""))) {
              org.setName(newNameOrg);
              AdminServiceProvider.get().updateOrganization(org);
              treetable.getItem(org.getId()).getItemProperty(TreeTableOrganization.NAME_PROPERTY)
                .setValue(newNameOrg);
              treetable.requestRepaint();
              nameLabel.setValue("Название: " + newNameOrg);
              nameLabel.requestRepaint();
              // TODO : обновление userInfoPanel
              //if (AdminServiceProvider.get()
              //		.findEmployeeByLogin(getApplication().getUser().toString()).getOrganization()
              //		.getId() == org.getId()) {
              //	((AdminApp) getApplication()).getUserInfoPanel().setOrganization(newNameOrg);
              //}
              showOrganization(org.getId());
              getWindow().showNotification("Организация " + oldNameOrg + " изменена");
            } else {
              showOrganization(org.getId());
              getWindow().showNotification("Изменений нет");
            }
          }
        });
        buttons.addComponent(save);
        addButtonCancel(org, buttons);
        form.getFooter().addComponent(buttons);

      }

    });
    buttons.addComponent(createOrg);
    return buttons;
  }

  private void addButtonCancel(final Organization org, HorizontalLayout buttons) {
    Button cancel = new Button("Отменить", new Button.ClickListener() {

      private static final long serialVersionUID = 1L;

      public void buttonClick(ClickEvent event) {
        showOrganization(org.getId());
      }
    });
    buttons.addComponent(cancel);
  }

  private Component buttonCreateEmployee(final Long id) {
    HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    buttons.setMargin(false, true, false, false);
    addComponent(buttons);
    Button createUser = new Button("Добавить пользователя", new Button.ClickListener() {

      private static final long serialVersionUID = 1L;

      public void buttonClick(ClickEvent event) {
        showOrganizationLabelsAndButtons(id);
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeFull();
        panel.addComponent(layout);

        String widthColumn = "100px";
        final TextField fieldLogin = TableEmployee.addTextField(layout, widthColumn, "Логин");
        final PasswordField fieldPass = TableEmployee.addPasswordField(layout, widthColumn, "Пароль");
        final PasswordField fieldPassRepeat = TableEmployee.addPasswordField(layout, widthColumn,
          "Повторите пароль");
        fieldPassRepeat.addValidator(new RepeatPasswordValidator(fieldPass));
        final TextField fieldFIO = TableEmployee.addTextField(layout, widthColumn, "ФИО");
        HorizontalLayout l1 = new HorizontalLayout();
        Label labelRole = new Label("Права");
        labelRole.setWidth(widthColumn);
        l1.addComponent(labelRole);
        l1.setComponentAlignment(labelRole, Alignment.MIDDLE_LEFT);
        final OptionGroup roleOptionGroup = TableEmployee.createRoleOptionGroup(null);
        l1.addComponent(roleOptionGroup);
        layout.addComponent(l1);

        UserItem emptyItem = new UserItem();
        emptyItem.setGroups(ImmutableSet.<String>of());

        final CertificateBlock certificateBlock = new CertificateBlock(emptyItem);
        layout.addComponent(certificateBlock);

        final ExecutorGroupsBlock executorGroupsBlock = new ExecutorGroupsBlock(emptyItem);
        layout.addComponent(executorGroupsBlock);

        final HorizontalLayout supervisorGroupsEmp = new HorizontalLayout();
        supervisorGroupsEmp.setMargin(true, true, true, false);
        supervisorGroupsEmp.setSpacing(true);
        supervisorGroupsEmp.setCaption("Назначить группы сотрудников для контроля");
        final FilterTable allSupervisorGroupsEmp = new FilterTable();
        allSupervisorGroupsEmp.setCaption("Доступные");
        TableEmployee.table(supervisorGroupsEmp, allSupervisorGroupsEmp);
        final FilterTable currentSupervisorGroupsEmp = new FilterTable();
        currentSupervisorGroupsEmp.setCaption("Отобранные");
        TableEmployee.table(supervisorGroupsEmp, currentSupervisorGroupsEmp);
        for (String groupName : AdminServiceProvider.get().getEmpGroupNames()) {
          for (Group group : AdminServiceProvider.get().findGroupByName(groupName)) {
            allSupervisorGroupsEmp.addItem(new Object[]{groupName, group.getTitle()}, groupName);
          }
        }
        TableEmployee.addListener(allSupervisorGroupsEmp, currentSupervisorGroupsEmp);
        TableEmployee.addListener(currentSupervisorGroupsEmp, allSupervisorGroupsEmp);
        layout.addComponent(supervisorGroupsEmp);

        final HorizontalLayout supervisorGroupsOrg = new HorizontalLayout();
        supervisorGroupsOrg.setMargin(true, true, true, false);
        supervisorGroupsOrg.setSpacing(true);
        supervisorGroupsOrg.setCaption("Назначить группы организаций для контроля");
        final FilterTable allSupervisorGroupsOrg = new FilterTable();
        allSupervisorGroupsOrg.setCaption("Доступные");
        TableEmployee.table(supervisorGroupsOrg, allSupervisorGroupsOrg);
        final FilterTable currentSupervisorGroupsOrg = new FilterTable();
        currentSupervisorGroupsOrg.setCaption("Отобранные");
        TableEmployee.table(supervisorGroupsOrg, currentSupervisorGroupsOrg);
        for (String groupName : AdminServiceProvider.get().getOrgGroupNames()) {
          for (Group group : AdminServiceProvider.get().findGroupByName(groupName)) {
            allSupervisorGroupsOrg.addItem(new Object[]{groupName, group.getTitle()}, groupName);
          }
        }
        TableEmployee.addListener(allSupervisorGroupsOrg, currentSupervisorGroupsOrg);
        TableEmployee.addListener(currentSupervisorGroupsOrg, allSupervisorGroupsOrg);
        layout.addComponent(supervisorGroupsOrg);

        TableEmployee
          .setRolesEnabled(roleOptionGroup, certificateBlock, executorGroupsBlock, supervisorGroupsEmp, supervisorGroupsOrg);
        roleOptionGroup.addListener(new Listener() {
          private static final long serialVersionUID = 1L;

          public void componentEvent(Event event) {
            TableEmployee.setRolesEnabled(roleOptionGroup, certificateBlock, executorGroupsBlock, supervisorGroupsEmp,
              supervisorGroupsOrg);
          }
        });

        HorizontalLayout l2 = new HorizontalLayout();
        Label labelPrint = new Label("Распечатать данные доступа");
        labelPrint.setWidth(widthColumn);
        l2.addComponent(labelPrint);
        l2.setComponentAlignment(labelPrint, Alignment.MIDDLE_LEFT);
        final CheckBox checkBoxPrint = new CheckBox();
        checkBoxPrint.setDescription("Распечатать данные доступа");
        l2.addComponent(checkBoxPrint);
        layout.addComponent(l2);

        HorizontalLayout layoutButton = new HorizontalLayout();
        layoutButton.setSpacing(true);

        Button buttonUserForm = new Button("Добавить", new Button.ClickListener() {

          private static final long serialVersionUID = -7193894183022375021L;

          public void buttonClick(ClickEvent event) {
            if (!fieldPassRepeat.isValid()) {
              return;
            }
            String loginUser = (String) fieldLogin.getValue();
            String password = (String) fieldPass.getValue();
            String passwordRepeat = (String) fieldPassRepeat.getValue();
            String fio = (String) fieldFIO.getValue();
            Set<Role> roles = (Set) roleOptionGroup.getValue();
            TreeSet<String> groupExecutor = executorGroupsBlock.getGroups();
            TreeSet<String> groupSupervisorEmp = new TreeSet<String>(
              (Collection<String>) currentSupervisorGroupsEmp.getItemIds());
            TreeSet<String> groupSupervisorOrg = new TreeSet<String>(
              (Collection<String>) currentSupervisorGroupsOrg.getItemIds());

            if (loginUser.equals("") || password.equals("") || passwordRepeat.equals("") || fio.equals("")) {
              getWindow().showNotification("Заполните все поля!", Notification.TYPE_WARNING_MESSAGE);
            } else if (!(password.equals(passwordRepeat))) {
              getWindow().showNotification("Пароли не совпадают!", Notification.TYPE_WARNING_MESSAGE);
            } else if (AdminServiceProvider.get().findEmployeeByLogin(loginUser) == null) {
              if (roles.contains(Role.SuperSupervisor)) {
                groupSupervisorEmp = new TreeSet<String>(AdminServiceProvider.get().selectGroupNamesBySocial(true));
                groupSupervisorOrg = new TreeSet<String>(AdminServiceProvider.get().selectGroupNamesBySocial(false));
              }
              String creator = getApplication().getUser().toString();
              AdminServiceProvider.get().createEmployee(loginUser, password, fio, roles, creator, id,
                groupExecutor, groupSupervisorEmp, groupSupervisorOrg);
              showOrganization(id);
              getWindow().showNotification("Пользователь " + loginUser + " создан");
              if (checkBoxPrint.booleanValue()) {
                // Create a window that contains what you want to print
                Window window = new Window();
                window.addComponent(new Label("<h1>Логин: " + loginUser + "</h1>\n" + "<h1>Пароль: "
                  + password + "</h1>\n", Label.CONTENT_XHTML));
                getApplication().addWindow(window);
                getWindow().open(new ExternalResource(window.getURL()), "_blank", 500, 200, // Width and
                  // height
                  Window.BORDER_NONE);
                window.executeJavaScript("print();");
                window.executeJavaScript("self.close();");
              }
            } else {
              getWindow().showNotification("Логин занят!", Notification.TYPE_WARNING_MESSAGE);
            }

          }
        });
        layoutButton.addComponent(buttonUserForm);
        Button buttonCancel = new Button("Отменить", new Button.ClickListener() {

          private static final long serialVersionUID = 1L;

          public void buttonClick(ClickEvent event) {
            showOrganization(id);
          }
        });
        layoutButton.addComponent(buttonCancel);
        layout.addComponent(layoutButton);

      }

    });
    createUser.addListener(this);
    buttons.addComponent(createUser);
    return buttons;
  }

  private Component groupLabel(Long id) {
    final Set<String> names = AdminServiceProvider.get().getOrgGroupNames(id);
    final HorizontalLayout layout = new HorizontalLayout();
    String s = "Группы: ";
    if (!names.isEmpty()) {
      s += Joiner.on(", ").join(names);
    } else {
      s += "не состоит ни в одной";
    }
    layout.addComponent(new Label(s));
    return layout;
  }

  Component createGroupLabel(final long id) {
    final Set<String> names = AdminServiceProvider.get().getOrgGroupNames(id);
    final Button button = new Button(names.isEmpty() ? "Добавить группы" : "Изменить группы",
      new Button.ClickListener() {
        private static final long serialVersionUID = 6657371896778851327L;

        @Override
        public void buttonClick(Button.ClickEvent event) {
          createGroupEditor(id, names);
        }
      });
    final HorizontalLayout layout = new HorizontalLayout();
    layout.setSpacing(true);
    layout.setMargin(false, true, false, false);
    layout.addComponent(button);
    return layout;
  }

  // TODO: учеть версионность!
  void createGroupEditor(final long id, final Set<String> names) {
    final Organization org = AdminServiceProvider.get().findOrganizationById(id);
    final Set<String> all = AdminServiceProvider.get().getOrgGroupNames();

    final VerticalLayout layout = new VerticalLayout();
    layout.setSpacing(true);

    final TwinColSelect twin = new TwinColSelect();
    twin.setSizeFull();
    twin.setNullSelectionAllowed(true);
    twin.setLeftColumnCaption("Доступные");
    twin.setRightColumnCaption("Отобранные для " + org.getName());
    twin.setImmediate(true);
    for (final String name : all) {
      twin.addItem(name);
      if (names.contains(name)) {
        twin.select(name);
      }
    }
    layout.addComponent(twin);

    final HorizontalLayout h = new HorizontalLayout();
    h.setSpacing(true);
    Button cancel = new Button("Отменить", new Button.ClickListener() {

      private static final long serialVersionUID = -2885182304929510066L;

      @Override
      public void buttonClick(Button.ClickEvent event) {
        showOrganization(id);
      }
    });
    cancel.setClickShortcut(KeyCode.ESCAPE, 0);

    Button ok = new Button("Применить", new Button.ClickListener() {

      private static final long serialVersionUID = -3182280627040233669L;

      @Override
      public void buttonClick(Button.ClickEvent event) {
        AdminServiceProvider.get().setOrgGroupNames(id,
          new TreeSet<String>((Collection<String>) twin.getValue()));
        showOrganization(id);
      }
    });
    ok.setClickShortcut(KeyCode.O, ModifierKey.CTRL);

    h.addComponent(ok);
    h.addComponent(cancel);

    layout.addComponent(h);
    layout.setSizeFull();
    panel.removeAllComponents();
    panel.addComponent(layout);
    twin.focus();
  }

  boolean isGoodGroup(final Set<String> names, final String group0) {
    final String group = StringUtils.trimToNull(group0);
    return group != null && !names.contains(group) && GROUP.matcher(group).matches();
  }

  public TreeTable getTreeTable() {
    return treetable;
  }

  public static void fillTable(TreeTable treetable) {
    List<Organization> rootOrganizations = AdminServiceProvider.get().getRootOrganizations();
    for (Organization org : rootOrganizations) {
      treetable.addItem(new Object[]{org.getName()}, org.getId());
      treetable.setCollapsed(org.getId(), true);
    }

    for (Organization org : rootOrganizations) {
      treetable.setChildrenAllowed(org.getId(), !(org.getOrganizations().isEmpty()));
    }
  }

}
