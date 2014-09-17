/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.beans;

import com.google.common.collect.Maps;
import commons.Streams;
import org.eclipse.persistence.queries.ScrollableCursor;
import ru.codeinside.adm.database.Directory;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

@TransactionManagement
@Singleton
@Lock(LockType.READ)
@TransactionAttribute
public class DirectoryBean {

  @PersistenceContext(unitName = "myPU")
  EntityManager em;

  public Directory create(String name) {
    Directory directory = em.find(Directory.class, name.trim());
    if (directory == null) {
      directory = new Directory(name);
      em.merge(directory);
    }
    return directory;
  }

  public void add(String name, String key, String value) {
    em.createNativeQuery("insert into directory_values (directory_name,\"values\", values_key) values (?,?,?)")
      .setParameter(1, name)
      .setParameter(2, value)
      .setParameter(3, key)
      .executeUpdate();
  }

  public void remove(String name, String key) {
    em.createNativeQuery("delete from directory_values d where directory_name = ? and values_key = ?")
      .setParameter(1, name)
      .setParameter(2, key)
      .executeUpdate();
  }

  public void delete(String name) {
    Directory directory = em.find(Directory.class, name.trim());
    if (directory != null) {
      em.remove(directory);
    }
  }

  public String value(String name, String key) {
    if (key == null) {
      return null;
    }
    return getValues(name).get(key);
  }

  public Map<String, String> getValues(String name) {
    Directory directory = em.find(Directory.class, name.trim());
    if (directory == null) {
      return Maps.newHashMap();
    }
    return directory.getValues();
  }

  public List<Object[]> getValues(String name, int start,
                                  int count, String[] order, boolean[] asc) {
    StringBuilder q = new StringBuilder("select values_key, values from directory_values d where directory_name = ?");
    if (order == null || order.length == 0) {
      q.append(" order by values_key").append(" asc");
    } else {
      for (int i = 0; i < order.length; i++) {
        if (i == 0) {
          q.append(" order by ");
        } else {
          q.append(", ");
        }
        String orderItem = order[i];
        // map property to columns:
        if ("key".equals(orderItem)) {
          orderItem = "values_key";
        } else if ("value".equals(orderItem)) {
          orderItem = "values";
        }
        q.append(orderItem).append(asc[i] ? " asc" : " desc");
      }
    }
    return em.createNativeQuery(q.toString())
      .setParameter(1, name)
      .setFirstResult(start)
      .setMaxResults(count)
      .getResultList();
  }

  public int getCountValues(String name) {
    Object singleResult = em.createNativeQuery("select count (*) from directory_values d where directory_name = ?")
      .setParameter(1, name)
      .getSingleResult();
    return ((Long) singleResult).intValue();
  }

  public String getValue(String name, String key) {
    List<Object> resultList = em.createNativeQuery("select values from directory_values d where directory_name = ? and values_key = ?")
      .setParameter(1, name)
      .setParameter(2, key)
      .getResultList();
    if (resultList == null || resultList.isEmpty()) {
      return null;
    }
    return (String) resultList.get(0);
  }

  public String getKey(String name, String value) {
    List<Object> resultList = em.createNativeQuery("select values_key from directory_values d where directory_name = ? and \"values\" = ?")
      .setParameter(1, name)
      .setParameter(2, value)
      .getResultList();
    if (resultList == null || resultList.isEmpty()) {
      return null;
    }
    return (String) resultList.get(0);
  }


  public File createTmpFile(String name) {
    ScrollableCursor cursor = (ScrollableCursor) em.createNativeQuery(
      "select directory_name, values_key, \"values\" from directory_values where directory_name = ?")
      .setParameter(1, name)
      .setHint("eclipselink.cursor.scrollable", true)
      .getSingleResult();
    File file = null;
    Writer writer = null;
    try {
      file = Streams.createTempFile("dictionary-" + name, ".csv");
      writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
      while (cursor.hasNext()) {
        Object[] message = (Object[]) cursor.next();
        writer.write("\"" + message[0] + "\",\"" + message[1] + "\",\"" + message[2] + "\"\n");
        // НЕ копить кеш!
        em.clear();
      }
      return file;
    } catch (IOException e) {
      if (file != null) {
        file.delete();
      }
      throw new RuntimeException(e);
    } finally {
      Streams.close(writer);
      cursor.close();
    }
  }
}
