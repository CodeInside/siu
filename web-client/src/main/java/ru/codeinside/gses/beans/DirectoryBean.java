/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.beans;

import com.google.common.collect.Maps;
import ru.codeinside.adm.database.Directory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;

import static javax.ejb.TransactionManagementType.CONTAINER;

@TransactionManagement(CONTAINER)
@javax.ejb.Singleton
@Stateless
public class DirectoryBean {

    @PersistenceContext(unitName = "myPU")
    EntityManager em;

    public Directory create(String name) {
        Directory directory = em.find(Directory.class, name.trim());
        if(directory == null){
            directory = new Directory(name);
            em.merge(directory);
        }
        return directory;
    }

    public void add(String name, String key, String value) {
        Directory directory = em.find(Directory.class, name.trim());
        if(directory != null) {
            Map<String, String> values = directory.getValues();
            values.put(key, value);
            directory.setValues(values);
            em.merge(directory);
        }
    }

    public void remove(String name, String key) {
        Directory directory = em.find(Directory.class, name.trim());
        if(directory != null) {
            Map<String, String> values = directory.getValues();
            values.remove(key);
            directory.setValues(values);
            em.merge(directory);
        }
    }

    public void delete(String name) {
        Directory directory = em.find(Directory.class, name.trim());
        if(directory != null) {
            em.remove(directory);
        }
    }

    public String value(String name, String key) {
        if(key == null){
            return null;
        }
        return getValues(name).get(key);
    }

    public Map<String, String> getValues(String name) {
        Directory directory = em.find(Directory.class, name.trim());
        if(directory == null) {
            return Maps.newHashMap();
        }
        return directory.getValues();
    }
}
