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
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
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

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public List<Object[]> getValues(String name, int start,
                                       int count, String[] order, boolean[] asc) {
    StringBuilder q = new StringBuilder("select values_key, values from directory_values d where directory_name = ?");
    for (int i = 0; i < order.length; i++) {
      if (i == 0) {
        q.append(" order by ");
      } else {
        q.append(", ");
      }
      q.append("p.").append(order[i]).append(asc[i] ? " asc" : " desc");
    }
    return em.createNativeQuery(q.toString())
      .setParameter(1, name)
      .setFirstResult(start)
      .setMaxResults(count)
      .getResultList();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public int getCountValues(String name) {
    Object singleResult = em.createNativeQuery("select count (*) from directory_values d where directory_name = ?")
      .setParameter(1, name)
      .getSingleResult();
    return ((Long)singleResult).intValue();
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public String getValue(String name, String key) {
    Object singleResult = em.createNativeQuery("select values from directory_values d where directory_name = ? and values_key = ?")
      .setParameter(1, name)
      .setParameter(2, key)
      .getSingleResult();
    return (String)singleResult;
  }
}
