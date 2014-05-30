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
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.BusinessCalendarDate;

import javax.persistence.EntityManagerFactory;
import java.util.Date;

final class BusinessDatesTable extends FilterTable {

  BusinessDatesTable() {
    super("Производтственный календарь");
    addStyleName("small striped");
    setImmediate(true);
    setFilterBarVisible(true);
    setSelectable(true);
    setSizeFull();
    addContainerProperty("date", Date.class, null);
    addContainerProperty("isWorkedDay", Boolean.class, null);

    setVisibleColumns(new String[]{"date", "isWorkedDay"});
    setColumnHeaders(new String[]{"Дата", "Рабочий день"});
    setColumnExpandRatio("date", 0.8f);
    setPageLength(0);
    setSortContainerPropertyId("date");
    setFilterDecorator(new FilterDecorator_());

    EntityManagerFactory pu = AdminServiceProvider.get().getMyPU();
    final JPAContainer<BusinessCalendarDate> container = new JPAContainer<BusinessCalendarDate>(BusinessCalendarDate.class);
    container.setReadOnly(true);
    container.setEntityProvider(new CachingLocalEntityProvider<BusinessCalendarDate>(BusinessCalendarDate.class, pu.createEntityManager()));
    setContainerDataSource(container);
    addGeneratedColumn("date", new DateColumnGenerator("dd.MM.yyyy"));
    addGeneratedColumn("isWorkedDay", new BooleanColumnGenerator());
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
