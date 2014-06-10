/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.beans;

import ru.codeinside.adm.database.Directory;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Map;

@Named("directories")
@Singleton
public class Directories {

    public Directory create(String name) {
        return DirectoryBeanProvider.get().create(name);
    }

    public void add(String name, String key, String value) {
        DirectoryBeanProvider.get().add(name, key, value);
    }

    public void remove(String name, String key) {
        DirectoryBeanProvider.get().remove(name, key);
    }

    public void delete(String name) {
        DirectoryBeanProvider.get().delete(name);
    }

    public String value(String name, String key) {
        return DirectoryBeanProvider.get().getValue(name, key);
    }

    public Map<String, String> getValues(String name) {
        return DirectoryBeanProvider.get().getValues(name);
    }
}
