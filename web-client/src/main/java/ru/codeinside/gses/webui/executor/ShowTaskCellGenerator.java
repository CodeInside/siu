/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.executor;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.BaseTheme;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.task.Task;

import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.gses.service.Functions;
import ru.codeinside.gses.service.PF;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.components.api.Changer;

final class ShowTaskCellGenerator implements Table.ColumnGenerator {
  // TODO: как избавится?
  private final Changer changer;
  private static final long serialVersionUID = 1L;

  ShowTaskCellGenerator(Changer changer) {
    this.changer = changer;
  }

  public Component generateCell(Table source, Object itemId, Object columnId) {
    final Item item = source.getItem(itemId);
    final String id = (String) item.getItemProperty("id").getValue();
    Bid bid = AdminServiceProvider.get().getBidByTask(id);
    
    String bidId = bid == null ? "" : bid.getId().toString();
    
    Button showButton = new Button(bidId, new Button.ClickListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void buttonClick(Button.ClickEvent event) {
        final Object[] a = Functions.withEngine(new PF<Object[]>() {
          public Object[] apply(final ProcessEngine engine) {
            engine.getIdentityService().setAuthenticatedUserId(Flash.login());
            Task task = engine.getTaskService().createTaskQuery().taskId(id).singleResult();
            RepositoryServiceImpl impl = (RepositoryServiceImpl) engine.getRepositoryService();
            ProcessDefinitionEntity def = (ProcessDefinitionEntity) impl.getDeployedProcessDefinition(task
              .getProcessDefinitionId());
            return new Object[]{task, def};
          }
        });
        ExecutorFactory.showTask(changer, (Task) a[0], (ProcessDefinitionEntity) a[1]);
      }
    });
    showButton.setStyleName(BaseTheme.BUTTON_LINK);
    return showButton;
  }
}
