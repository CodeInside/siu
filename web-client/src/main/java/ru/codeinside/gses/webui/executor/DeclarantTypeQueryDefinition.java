/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.executor;

import ru.codeinside.gses.lazyquerycontainer.LazyQueryDefinition;

public class DeclarantTypeQueryDefinition extends LazyQueryDefinition{
  public DeclarantTypeQueryDefinition(){
    super(false, 20);
    addProperty("name", String.class, null, true, true);
    addProperty("value", String.class, null, true, true);
  }
}
