/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package eform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Form implements Serializable {

  public boolean archiveMode;

  final public Map<String, Property> props = new LinkedHashMap<String, Property>();

  public Map<String, Property> plusBlock(String login, String name, String suffix, Integer newVal) {
    return  Collections.emptyMap();
  }

  public void minusBlock(String name, String suffix, Integer newVal) {
    return;
  }

  public Property getProperty (String name) {
    return getProperty(props, name);
  }

  public List<String> propertyKeySet() {
    return propertyKeySet(props);
  }

  public List<String> propertyKeySet(Map<String, Property> props) {
    List<String> list = new ArrayList<String>();
    for (String key : props.keySet()) {
      list.add(key);
      for (Map<String, Property> child : getProperty(key).children) {
        list.addAll(propertyKeySet(child));
      }
    }
    return list;
  }

  private Property getProperty (Map<String, Property> props, String name) {
    Property property = props.get(name);
    if (property != null) {
      return property;
    }
    for (Property property1 : props.values()) {
      for (Map<String, Property> child : property1.children) {
        Property property2 = getProperty(child, name);
        if (property2 != null) {
          return property2;
        }
      }
    }
    return null;
  }

}