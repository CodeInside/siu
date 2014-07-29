/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.log;

import ru.codeinside.adm.AdminService;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.adm.database.EnclosureEntity;
import ru.codeinside.adm.database.Group;
import ru.codeinside.adm.database.InfoSystem;
import ru.codeinside.adm.database.InfoSystemService;
import ru.codeinside.adm.database.Organization;
import ru.codeinside.adm.database.Procedure;
import ru.codeinside.adm.database.ProcedureProcessDefinition;
import ru.codeinside.adm.database.Service;
import ru.codeinside.adm.database.ServiceResponseEntity;

import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

@Singleton
@DependsOn("BaseBean")
public class Logger {

  private static final String PERSIST = "Persist";
  private static final String UPDATE = "Update";
  private static final String REMOVE = "Remove";

  @PostPersist
  public void persist(Object obj) {
    createLog(obj, PERSIST);
  }

  @PostUpdate
  public void update(Object obj) {
    createLog(obj, UPDATE);
  }

  @PostRemove
  public void remove(Object obj) {
    createLog(obj, REMOVE);
  }

  private void createLog(Object obj, String action) {
    AdminService adminService = AdminServiceProvider.tryGet();
    if (adminService == null) {
      return;
    }
    Actor actor = adminService.createActor();
    String entityName = obj.getClass().getSimpleName();
    String entityId = "unknow";
    String info = null;


    //TODO: использовать метаданные!
    if (obj instanceof Employee) {
      Employee employee = (Employee) obj;
      entityId = employee.getLogin();

    } else if (obj instanceof Organization) {
      Organization organization = (Organization) obj;
      entityId = organization.getId().toString();
    } else if (obj instanceof Group) {
      Group group = (Group) obj;
      entityId = group.getId().toString();
    } else if (obj instanceof Procedure) {
      Procedure procedure = (Procedure) obj;
      entityId = procedure.getId();
    } else if (obj instanceof ProcedureProcessDefinition) {
      ProcedureProcessDefinition ppf = (ProcedureProcessDefinition) obj;
      entityId = ppf.getProcessDefinitionId();
    } else if (obj instanceof Service) {
      Service service = (Service) obj;
      entityId = service.getId().toString();
    } else if (obj instanceof Bid) {
      Bid bid = (Bid) obj;
      entityId = bid.getId().toString();
      if (PERSIST == action) {
        final Procedure procedure = bid.getProcedure();
        info = " procedure id:  " + procedure.getId() + " ; заявитель: " + bid.getDeclarant();
      }
    } else if (obj instanceof InfoSystem) {
      InfoSystem infoSystem = (InfoSystem) obj;
      entityId = infoSystem.getCode();
    } else if (obj instanceof InfoSystemService) {
      InfoSystemService iss = (InfoSystemService) obj;
      entityId = iss.getId().toString();
    } else if (obj instanceof EnclosureEntity) {
      EnclosureEntity ee = (EnclosureEntity) obj;
      entityId = ee.getId();
    } else if (obj instanceof ServiceResponseEntity) {
      ServiceResponseEntity sre = (ServiceResponseEntity) obj;
      entityId = sre.getId().toString();
    }
    AdminServiceProvider.get().createLog(actor, entityName, entityId, action, info, true);
  }
}