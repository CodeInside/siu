/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.declarant;


import com.google.common.base.Predicate;
import org.activiti.engine.identity.User;

public class EmployeePredicate implements Predicate<User> {

    private String employee;

    public EmployeePredicate(String employee) {
        this.employee = employee;
    }

    @Override
    public boolean apply(User u) {
        return employee.equals(u.getId());
    }
}
