/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.Reindeer;
import ru.codeinside.adm.database.Role;
import ru.codeinside.gses.webui.components.TabChanger;
import ru.codeinside.gses.webui.components.UserInfoPanel;
import ru.codeinside.gses.webui.declarant.DeclarantFactory;
import ru.codeinside.gses.webui.executor.ArchiveFactory;
import ru.codeinside.gses.webui.executor.ExecutorFactory;
import ru.codeinside.gses.webui.manager.ManagerWorkplace;
import ru.codeinside.gses.webui.supervisor.SupervisorWorkplace;
import ru.codeinside.gses.webui.supervisor.TaskManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

final public class Workplace extends CustomComponent {
  ActivitiApp app;

  public Workplace(String login, Set<Role> roles, boolean production, ActivitiApp app) {

    this.app = app;
    TabSheet tabSheet = new TabSheet();
    tabSheet.setSizeFull();
    tabSheet.setStyleName(Reindeer.TABSHEET_BORDERLESS);
    tabSheet.setCloseHandler(new DelegateCloseHandler());

    if (!production) {
      new MemoryUsage(tabSheet);
    }

    UserInfoPanel.addClosableToTabSheet(tabSheet, login);

    if (roles.contains(Role.Declarant) || roles.contains(Role.Executor)) {
      new TabChanger(tabSheet).set(new ExportJsonPanel(true), "Подписанные формы");
    }

    if (roles.contains(Role.Executor)) {
      TabChanger archiveChanger = new TabChanger(tabSheet);
      archiveChanger.set(ArchiveFactory.create(), "Архив");

      TabChanger executorChanger = new TabChanger(tabSheet);
      executorChanger.set(ExecutorFactory.create(executorChanger, tabSheet), "Исполнение");
    }

    if (roles.contains(Role.Declarant)) {
      new TabChanger(tabSheet).set(DeclarantFactory.create(), "Подача заявки");
    }

    if (roles.contains(Role.Supervisor) || roles.contains(Role.SuperSupervisor)) {
      new TabChanger(tabSheet).set(new SupervisorWorkplace(), "Контроль исполнения");
      tabSheet.addTab(statisticTab(app), "Статистика");
    }

    if (roles.contains(Role.SuperSupervisor)) {
      new TabChanger(tabSheet).set(new TaskManager(), "Состояние исполнения");
    }

    if (roles.contains(Role.Manager)) {
      new TabChanger(tabSheet).set(new ManagerWorkplace(), "Управление процедурами");
    }

    if (roles.contains(Role.Executor) || roles.contains(Role.Declarant) || roles.contains(Role.Supervisor) || roles.contains(Role.SuperSupervisor)) {
      SmevTasksPanel smevTasksPanel = new SmevTasksPanel();
      tabSheet.addTab(smevTasksPanel, "Управление вызовами СМЭВ");
      tabSheet.addListener(smevTasksPanel);
    }

    setCompositionRoot(tabSheet);

    setSizeFull();
  }

  private Component statisticTab(ActivitiApp app) {
    try {
      URL url = new URL(app.getServerUrl(), "//194.85.124.90:8888/Statistic/");
      Embedded embedded = new Embedded("", new ExternalResource(url));
      embedded.setType(Embedded.TYPE_BROWSER);
      embedded.setWidth("100%");
      embedded.setHeight("100%");
      return embedded;
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }
}
