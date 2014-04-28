/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.s.oep.declarer;

import junit.framework.Assert;
import ru.codeinside.gws.api.DeclarerContext;
import ru.codeinside.gws.api.RequestContext;
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DummyRequestContext implements RequestContext {

  boolean first;
  String bid;
  DeclarerContext declarerContext;
  ServerRequest request;
  Long procedureCode;

  Map<String, ServerResponse> states = new LinkedHashMap<String, ServerResponse>();
  Map<String, ServerResponse> results = new LinkedHashMap<String, ServerResponse>();

  @Override
  public boolean isFirst() {
    return first;
  }

  @Override
  public String getBid() {
    return bid;
  }

  @Override
  public DeclarerContext getDeclarerContext(long procedureCode) {
    Assert.assertEquals(this.procedureCode, Long.valueOf(procedureCode));
    return declarerContext;
  }

  @Override
  public ServerRequest getRequest() {
    return request;
  }

  @Override
  public ServerResponse getState() {
    return getState(bid);
  }

  @Override
  public ServerResponse getResult() {
    return getResult(bid);
  }

  @Override
  public List<String> getBids() {
    Set<String> ids = new HashSet<String>();
    ids.addAll(states.keySet());
    ids.addAll(results.keySet());
    return new ArrayList<String>(ids);
  }

  @Override
  public ServerResponse getState(String bid) {
    return states.get(bid);
  }

  @Override
  public ServerResponse getResult(String bid) {
    return results.get(bid);
  }
}
