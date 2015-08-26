/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.beans.filevalues;

import ru.codeinside.adm.database.Employee;
import ru.codeinside.gses.activiti.Activiti;
import ru.codeinside.gses.webui.Flash;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;

/**
 * Вызывается из BPMN скрипта и возвращает ФИО или Имя Организации по логину.
 */

@Named("users")
@Singleton
public class Users {

  public String fio(String login) {
    Employee employee = getEmployee(login);
    if (employee != null) {
      return employee.getFio();
    }
    return "";
  }

  public String orgName() {
    Employee employee = getEmployee(Flash.login());
    if (employee != null) {
      return employee.getOrganization().getName();
    }
    return "";
  }

  public String orgName(String login) {
    Employee employee = getEmployee(login);
    if (employee != null) {
      return employee.getOrganization().getName();
    }
    return "";
  }

  private Employee getEmployee(String login) {
    if (login != null) {
      List<Employee> list = Activiti.getEm().createQuery(
        "SELECT e FROM Employee e WHERE e.login = :login", Employee.class)
        .setParameter("login", login).getResultList();
      if (!list.isEmpty()) {
        return list.get(0);
      }
    }
    return null;
  }
}
