/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.provider.CachingLocalEntityProvider;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.log.Log;

import javax.persistence.EntityManagerFactory;

public class LogTab extends FilterTable implements TabSheet.SelectedTabChangeListener {

    public LogTab() {
        setSizeFull();
        setFilterBarVisible(true);
        setSelectable(true);
        setImmediate(true);
        setRowHeaderMode(Table.ROW_HEADER_MODE_ID);
        setColumnCollapsingAllowed(true);
        setColumnReorderingAllowed(true);
        setFilterDecorator(new TableEmployeeFilterDecorator());

        //TODO: не ясно ещё как поведёт себя в кластере, нужно проверить
        EntityManagerFactory logPU = AdminServiceProvider.get().getLogPU();
        final JPAContainer<Log> container = new JPAContainer<Log>(Log.class);
        container.setReadOnly(true);
        container.setEntityProvider(new CachingLocalEntityProvider<Log>(Log.class, logPU.createEntityManager()));
        container.addNestedContainerProperty("actor.name");
        container.addNestedContainerProperty("actor.ip");
        container.addNestedContainerProperty("actor.browser");
        container.addNestedContainerProperty("actor.os");
        setContainerDataSource(container);
        setVisibleColumns(
                new Object[]{"actor.name", "actor.browser",  "actor.ip", "actor.os", "entityName", "entityId", "action", "info", "date"});
        setColumnHeaders(new String[]{"Субъект", "Браузер", "IP", "ОС", "Объект", "ID", "Действие", "Дополнительно", "Дата"});
        setSortContainerPropertyId("date");
        setSortAscending(false);
        addGeneratedColumn("date", new DateColumnGenerator("dd.MM.yyyy HH:mm:ss.SSS"));
    }

    /**
     * сбрасываем состояние.
     */
    @Override
    public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
        if (this == event.getTabSheet().getSelectedTab()) {
            ((JPAContainer) getContainerDataSource()).getEntityProvider().refresh();
            refreshRowCache();
        }
    }

}
