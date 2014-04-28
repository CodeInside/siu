/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.stubs;

import junit.framework.Assert;
import ru.codeinside.gws.api.DeclarerContext;
import ru.codeinside.gws.api.RequestContext;
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;

import java.util.Arrays;
import java.util.List;

public class DummyRequestContext implements RequestContext {

    public boolean first;
    public String bid;
    public DeclarerContextStub declarerContext = new DeclarerContextStub();
    public ServerRequest request;
    public ServerResponse state;
    public ServerResponse result;
    public Long procedureCode;

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
    return Arrays.asList(bid);
  }

  @Override
  public ServerResponse getState(String bid) {
    return state;
  }

  @Override
  public ServerResponse getResult(String bid) {
    return result;
  }
}
