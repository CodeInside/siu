/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c.context;


import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws3564c.enclosure.grp.EnclosureGRPBuilder;

import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

public class TypedContext implements ExchangeContext {
  private ExchangeContext context;

  public TypedContext(ExchangeContext context) {
    this.context = context;
  }

  public Object getLocal() {
    return context.getLocal();
  }

  public void setVariable(String name, Object value) {
    context.setVariable(name, value);
  }

  public Set<String> getVariableNames() {
    return context.getVariableNames();
  }

  public void addEnclosure(String name, Enclosure enclosure) {
    context.addEnclosure(name, enclosure);
  }

  public void setLocal(Object value) {
    context.setLocal(value);
  }

  public Enclosure getEnclosure(String name) {
    return context.getEnclosure(name);
  }

  public Object getVariable(String name) {
    return context.getVariable(name);
  }

  public boolean isEnclosure(String name) {
    return context.isEnclosure(name);
  }

  public String getStrFromContext(String varName) {
    return (String) getVariable(varName);
  }

  public Date getDateFromContext(String varName) {
    return (Date) getVariable(varName);
  }

  public Long getLongFromContext(String varName) {
    Object value = getVariable(varName);
    if (value == null) return 0L;
    if (value instanceof String) {
      if ("".equals(value)) return 0L;
      return Long.parseLong((String)value);
    }

    return (Long) value;
  }

  public boolean isStringVariableHasValue(String varName) {
    String varValue = (String) getVariable(varName);
    return varValue != null && !"".equals(varValue);
  }

  public BigInteger getBigIntFromContext(String varName) {
    return BigInteger.valueOf(getLongFromContext(varName));
  }

  public boolean hasAtLeastOneVariableWithPrefix(String prefixName) {
    for (String varName : getVariableNames()) {
      if (varName.startsWith(prefixName)) return true;
    }
    return false;
  }

  public Boolean getBooleanValue(String varName) {
    if (getVariable(varName) == null) return false;
    return (Boolean) getVariable(varName);
  }
}
