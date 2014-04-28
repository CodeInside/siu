/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.s.oep.declarer;

import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ReceiptContext;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class DummyContext implements ReceiptContext {

  final Map<String, Object> vars = new LinkedHashMap<String, Object>();

  @Override
  public Set<String> getPropertyNames() {
    return vars.keySet();
  }

  @Override
  public Set<String> getAllPropertyNames() {
    return null;
  }

  @Override
  public Set<String> getEnclosureNames() {
    return null;
  }

  @Override
  public Set<String> getAllEnclosureNames() {
    return null;
  }

  @Override
  public Object getVariable(String name) {
    return vars.get(name);
  }

  @Override
  public Object getVariableByFullName(String name) {
    return null;
  }

  @Override
  public Enclosure getEnclosure(String name) {
    return null;
  }

  @Override
  public Enclosure getEnclosureByFullName(String name) {
    return null;
  }

  @Override
  public void setEnclosure(String name, Enclosure enclosure) {
  }

  @Override
  public void setVariable(String name, Object value) {
  }

  @Override
  public String getBid() {
    return "1";
  }

}
