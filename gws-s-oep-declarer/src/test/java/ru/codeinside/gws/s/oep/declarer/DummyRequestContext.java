/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.s.oep.declarer;

import java.util.List;

import junit.framework.Assert;
import ru.codeinside.gws.api.DeclarerContext;
import ru.codeinside.gws.api.RequestContext;
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;

public class DummyRequestContext implements RequestContext {

  boolean first;
  String bid;
  DeclarerContext declarerContext;
  ServerRequest request;
  ServerResponse state;
  ServerResponse result;
  Long procedureCode;

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
    return state;
  }

  @Override
  public ServerResponse getResult() {
    return result;
  }

  @Override
  public List<String> getBids() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse getResult(String arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse getState(String arg0) {
    // TODO Auto-generated method stub
    return null;
  }
}
