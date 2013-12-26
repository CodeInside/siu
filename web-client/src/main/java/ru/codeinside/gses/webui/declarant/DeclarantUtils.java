/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.declarant;


import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import ru.codeinside.adm.database.Procedure;
import ru.codeinside.adm.database.ProcedureProcessDefinition;
import ru.codeinside.gses.webui.Flash;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import static com.google.common.collect.Iterables.any;

public class DeclarantUtils {

  public static int MAX_COUNT = 2000;

  public static <T> List<T> sublist(LinkedHashSet<T> base, int start, int count) {
    Iterator<T> iterator = base.iterator();
    int index = 0;
    final List<T> result = new ArrayList<T>();
    while (iterator.hasNext() && index < (start + count)) {
      T next = iterator.next();
      if (index < start) {
        index++;
        continue;
      }
      index++;
      result.add(next);
    }
    return result;
  }

  public static LinkedHashSet<Procedure> filtered(final String employee, List<Procedure> procs) {
    LinkedHashSet<Procedure> result = new LinkedHashSet<Procedure>();
    for (final Procedure p : procs) {
      final ProcedureProcessDefinition ppd = Flash.flash().getDeclarantService().selectActive(Long.parseLong(p.getId()));
      if (ppd != null) {
        List<User> users = Flash.flash().getProcessEngine().getIdentityService().createUserQuery().potentialStarter(ppd.getProcessDefinitionId()).list();
        List<Group> groups = Flash.flash().getProcessEngine().getIdentityService().createGroupQuery().potentialStarter(ppd.getProcessDefinitionId()).list();
        if (users.isEmpty() && groups.isEmpty()) {
          result.add(p);
        } else {
          boolean shouldAdd = any(users, new EmployeePredicate(employee)) ? true : any(groups, new GroupPredicate(employee));
          if (shouldAdd) {
            result.add(p);
          }
        }
      }
    }
    return result;
  }
}
