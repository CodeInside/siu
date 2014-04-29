/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.manager;

import com.vaadin.data.Item;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import ru.codeinside.adm.database.Procedure;
import ru.codeinside.adm.database.ProcedureType;
import ru.codeinside.gses.webui.containers.LazyLoadingContainer;
import ru.codeinside.gses.webui.containers.LazyLoadingQuery;
import ru.codeinside.gses.webui.utils.Components;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ProcedureQuery implements LazyLoadingQuery {
  private static final long serialVersionUID = 1L;
  final private SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
  private String[] sortProps = {};
  private boolean[] sortAsc = {};
  private final ProcedureForm procedureForm;
  private Long serviceId = null;
  private ProcedureType type;
  LazyLoadingContainer container;

  public ProcedureQuery(ProcedureForm procedureForm, ProcedureType type) {
    this.procedureForm = procedureForm;
    this.type = type;
  }

  public void setServiceId(Long serviceId) {
    this.serviceId = serviceId;
  }

  @Override
  public int size() {
    if (serviceId != null) {
      if (container.sender != null) {
        return ManagerService.get().getProcedureCountByServiceId(serviceId, container.sender);
      } else {
        return ManagerService.get().getProcedureCountByServiceId(serviceId, null);
      }
    } else {
      if (container.sender != null) {
        return ManagerService.get().getProcedureCount(type, container.sender);
      } else {
        return ManagerService.get().getProcedureCount(type, null);
      }
    }
  }

  @Override
  public List<Item> loadItems(int start, int count) {
    ArrayList<Item> items = new ArrayList<Item>();
    List<Procedure> procedures;
    if (serviceId != null) {
      if (container != null) {
        procedures = ManagerService.get().getProceduresByServiceId(serviceId, start, count, sortProps, sortAsc, container.sender);
      } else {
        procedures = ManagerService.get().getProceduresByServiceId(serviceId, start, count, sortProps, sortAsc, null);

      }
    } else {
      if (container != null) {
        procedures = ManagerService.get().getProcedures(start, count, sortProps, sortAsc, type, container.sender);
      } else {
        procedures = ManagerService.get().getProcedures(start, count, sortProps, sortAsc, type, null);
      }
    }

    for (Procedure procedure : procedures) {
      items.add(createItem(procedure));
    }
    return items;
  }

  @Override
  public Item loadSingleResult(String id) {
    final Procedure procedure = ManagerService.get().getProcedure(id);
    return createItem(procedure);
  }

  PropertysetItem createItem(final Procedure p) {
    ClickListener listener = new ClickListener() {
      private static final long serialVersionUID = -7375656268811051709L;

      @Override
      public void buttonClick(ClickEvent event) {
        procedureForm.showProcedureInfo(p);
      }
    };
    PropertysetItem item = new PropertysetItem();
    item.addItemProperty("id", Components.buttonProperty(p.getId(), listener));
    item.addItemProperty("name", Components.stringProperty(p.getName()));
    item.addItemProperty("description", Components.stringProperty(p.getDescription()));
    item.addItemProperty("version", Components.stringProperty(p.getVersion()));
    item.addItemProperty("status", Components.stringProperty(p.getStatus()));
    item.addItemProperty("dateCreated", Components.stringProperty(formatter.format(p.getDateCreated())));
    return item;
  }

  @Override
  public void setSorting(Object[] propertyIds, boolean[] ascending) {
    String[] props = new String[propertyIds.length];
    for (int i = 0; i < propertyIds.length; i++) {
      props[i] = propertyIds[i].toString();
    }
    sortProps = props;
    sortAsc = ascending;
  }

  @Override
  public void setLazyLoadingContainer(LazyLoadingContainer container) {
    this.container = container;
  }
}
