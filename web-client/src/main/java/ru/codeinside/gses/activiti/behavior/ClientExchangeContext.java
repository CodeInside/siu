/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.behavior;

import com.google.common.collect.ImmutableSet;
import org.activiti.engine.delegate.DelegateExecution;
import ru.codeinside.gses.beans.ActivitiExchangeContext;

import java.util.HashMap;
import java.util.Map;

public class ClientExchangeContext extends ActivitiExchangeContext {

  static final String SMEV_REQUEST_ID = "smevRequestId";
  static final String SMEV_ORIGIN_REQUEST_ID = "smevOriginRequestId";
  static final String SMEV_POOL = "smevPool";
  static final String SMEV_ERROR = "smevError";

  static final ImmutableSet<String> SMEV_NAMES = ImmutableSet.of(
    SMEV_REQUEST_ID,
    SMEV_ORIGIN_REQUEST_ID,
    SMEV_POOL,
    SMEV_ERROR
  );

  final Map<String, Object> smevVars = new HashMap<String, Object>();
  final String component;


  public ClientExchangeContext(DelegateExecution execution, String component) {
    super(execution);
    this.component = component;
  }

  @Override
  public void setVariable(String name, Object value) {
    if (SMEV_NAMES.contains(name)) {
      logger.fine("{" + component + "} установил {" + name + "} = {" + value + "}");
      if (SMEV_ERROR.equals(name)) {
        smevVars.put(name, value);
      }
      return;
    }
    super.setVariable(name, value);
  }

  @Override
  public Object getVariable(String name) {
    if (SMEV_NAMES.contains(name)) {
      Object value = smevVars.get(name);
      logger.fine("{" + component + "} получил {" + name + "} = {" + value + "}");
      return value;
    }
    return super.getVariable(name);
  }

  public void setRequestId(String requestId) {
    smevVars.put(SMEV_REQUEST_ID, requestId);
  }

  public void setOriginRequestId(String originRequestId) {
    smevVars.put(SMEV_ORIGIN_REQUEST_ID, originRequestId);
  }

  public void setPool(boolean pool) {
    smevVars.put(SMEV_POOL, pool);
  }

  public String getSmevError() {
    Object smevError = smevVars.get(SMEV_ERROR);
    return smevError == null ? null : smevError.toString();
  }

}
