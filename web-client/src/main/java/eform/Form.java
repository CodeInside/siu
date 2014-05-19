/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package eform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

abstract public class Form implements Serializable {

  public boolean archiveMode;

  final public Map<String, Property> props = new LinkedHashMap<String, Property>();

  abstract public Map<String, Property> plusBlock(String login, String name, String suffix, Integer newVal);

  abstract public void minusBlock(String name, String suffix, Integer newVal);

  final public Property getProperty(String name) {
    return getProperty(props, name);
  }

  final public List<String> propertyKeySet() {
    return propertyKeySet(props);
  }

  final public List<String> propertyKeySet(Map<String, Property> props) {
    ArrayList<String> list = new ArrayList<String>();
    accumulateKeys(props, list);
    return list;
  }

  private void accumulateKeys(Map<String, Property> map, List<String> keys) {
    for (String key : map.keySet()) {
      keys.add(key);
      Property block = map.get(key);
      if (block.children != null) {
        for (Map<String, Property> clone : block.children) {
          accumulateKeys(clone, keys);
        }
      }
    }
  }

  private Property getProperty(Map<String, Property> map, String name) {
    if (map.containsKey(name)) {
      return map.get(name);
    }
    for (Property block : map.values()) {
      if (block.children != null) {
        for (Map<String, Property> clone : block.children) {
          Property property = getProperty(clone, name);
          if (property != null) {
            return property;
          }
        }
      }
    }
    return null;
  }

}