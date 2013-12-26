/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.declarant;


import com.google.common.base.Predicate;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.gses.webui.Flash;

public class GroupPredicate implements Predicate<Group> {

    final Employee employee;

    public GroupPredicate(String login) {
        this.employee = Flash.flash().getAdminService().findEmployeeByLogin(login);
    }

    @Override
    public boolean apply(Group g) {
        for(ru.codeinside.adm.database.Group gr : employee.getGroups()){
            if(gr.getName().equals(g.getId())){
                return true;
            }
        }
        return false;
    }
}
