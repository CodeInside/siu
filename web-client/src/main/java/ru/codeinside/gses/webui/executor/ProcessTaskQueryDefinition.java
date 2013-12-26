/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.executor;

import ru.codeinside.gses.lazyquerycontainer.LazyQueryDefinition;

final public class  ProcessTaskQueryDefinition extends LazyQueryDefinition {
  private static final long serialVersionUID = 1L;
  long procedureId = -1;
  public void setProcedureId(long procedureId) {
    this.procedureId = procedureId;
  }
  public ProcessTaskQueryDefinition(long procedureId){
    super(false, 15);
    this.procedureId = procedureId;
    addProperty("name", String.class, null, true, true);
    addProperty("taskDefKey", String.class, null, true, true);
  }
}
