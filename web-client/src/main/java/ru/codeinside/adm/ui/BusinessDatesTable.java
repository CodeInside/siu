/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.provider.CachingLocalEntityProvider;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.Action;
import org.tepi.filtertable.FilterTable;
import org.vaadin.dialogs.ConfirmDialog;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.BusinessCalendarDate;

import javax.persistence.EntityManagerFactory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

final class BusinessDatesTable extends FilterTable {
  static final Action ACTION_DELETE = new Action("Удалить");
  static final Action[] ACTIONS = new Action[]{ACTION_DELETE};
  BusinessDatesTable() {
    super("Производственный календарь");
    addStyleName("small striped");
    setImmediate(true);
    setFilterBarVisible(true);
    setSelectable(true);
    setSizeFull();
    addContainerProperty("date", Date.class, null);
    addContainerProperty("workedDay", Boolean.class, null);

    setVisibleColumns(new String[]{"date", "workedDay"});
    setColumnHeaders(new String[]{"Дата", "Рабочий день"});
    setColumnExpandRatio("date", 0.8f);
    setPageLength(0);
    setSortContainerPropertyId("date");
    setFilterDecorator(new FilterDecorator_() {
      @Override
      public DateFormat getDateFormat(Object propertyId) {
        return new SimpleDateFormat("dd.MM.yyyy");
      }
    });
    setFilterGenerator(new FilterGenerator_(null, Arrays.asList("workedDay")));
    EntityManagerFactory pu = AdminServiceProvider.get().getMyPU();
    final JPAContainer<BusinessCalendarDate> container = new JPAContainer<BusinessCalendarDate>(BusinessCalendarDate.class);
    container.setReadOnly(true);
    container.setEntityProvider(new CachingLocalEntityProvider<BusinessCalendarDate>(BusinessCalendarDate.class, pu.createEntityManager()));
    setContainerDataSource(container);
    addGeneratedColumn("date", new DateColumnGenerator("dd.MM.yyyy"));
    addGeneratedColumn("workedDay", new BooleanColumnGenerator());
    addContextMenu();
  }

  protected void addContextMenu() {
    addActionHandler(new Action.Handler() {

      private static final long serialVersionUID = 1L;

      public Action[] getActions(Object target, Object sender) {
        ArrayList<Action> result = new ArrayList<Action>(ACTIONS.length + 1);
        result.addAll(Arrays.asList(ACTIONS));
        return result.toArray(new Action[result.size()]);
      }

      public void handleAction(Action action, Object sender, Object target) {
        final Item item = getItem(getValue());
        if (item != null) {
          if (ACTION_DELETE == action) {
            deleteBusinessCalendarItem();
            refresh();
          }
        }
      }
    });
  }

  public void deleteBusinessCalendarItem() {
    final Item item = getItem(getValue());
    ConfirmDialog.show(getWindow(),
        "Производственный календарь",
        "Подтвердите удаление",
        "Подтверждаю",
        "Отмена",
        new ConfirmDialog.Listener() {
          public void onClose(ConfirmDialog dialog) {
            if (dialog.isConfirmed()) {
              deleteCalendarItem(item);
            }
          }
        });
  }

  private void deleteCalendarItem(Item item) {
    final Date dateForRemove = (Date) item.getItemProperty("date").getValue();
    AdminServiceProvider.get().deleteDateFromBusinessCalendar(dateForRemove);
    refresh();
    getWindow().showNotification("Дата из календаря удалена");
  }

  public void refresh() {
    setValue(null);
    Container container = getContainerDataSource();
    if (container instanceof JPAContainer) {
      ((JPAContainer) container).getEntityProvider().refresh();
    }
    refreshRowCache();
  }
}
