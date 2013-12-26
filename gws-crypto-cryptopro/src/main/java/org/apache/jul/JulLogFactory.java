/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package org.apache.jul;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.Jdk14Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.LogManager;


final public class JulLogFactory extends LogFactory {

  protected Hashtable attributes = new Hashtable();
  protected Hashtable instances = new Hashtable();

  public JulLogFactory() {
    final InputStream is = getClass().getClassLoader().getResourceAsStream("logging.properties");
    try {
      if (is != null) {
        LogManager.getLogManager().readConfiguration(is);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Object getAttribute(String name) {
    return (attributes.get(name));
  }

  public String[] getAttributeNames() {
    ArrayList<String> names = new ArrayList<String>(attributes.size());
    Enumeration keys = attributes.keys();
    while (keys.hasMoreElements()) {
      names.add((String) keys.nextElement());
    }
    return names.toArray(new String[names.size()]);
  }

  public Log getInstance(Class clazz) throws LogConfigurationException {
    return getInstance(clazz.getName());
  }

  public Log getInstance(String name) throws LogConfigurationException {
    Log instance = (Log) instances.get(name);
    if (instance == null) {
      instance = new Jdk14Logger(name);
      instances.put(name, instance);
    }
    return (instance);
  }

  public void release() {
    instances.clear();
  }

  public void removeAttribute(String name) {
    attributes.remove(name);
  }

  public void setAttribute(String name, Object value) {
    if (value == null) {
      attributes.remove(name);
    } else {
      attributes.put(name, value);
    }
  }

}
