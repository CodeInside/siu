/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.s.oep.declarer;

import ru.codeinside.gws.api.DeclarerContext;
import ru.codeinside.gws.api.Enclosure;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class DeclarerContextStub implements DeclarerContext {

  String id;
  Set<String> vars = new HashSet<String>();
  Map<String, Object> values = new HashMap<String, Object>();

  @Override
  public Set<String> getPropertyNames() {
    return vars;
  }

  @Override
  public boolean isRequired(String propertyName) {
    return false;
  }

  @Override
  public boolean isEnclosure(String propertyName) {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public String getType(String propertyName) {
    return "string";
  }

  @Override
  public void setValue(String propertyName, Object value) {
    values.put(propertyName, value);
  }

  @Override
  public void addEnclosure(String propertyName, Enclosure enclosure) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public String declare() {
    return id;
  }

  @Override
  public String declare(String tag) {
    return id;
  }

  @Override
  public Object getVariable(String name) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
