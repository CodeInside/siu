/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2015, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.auth;

import com.google.common.collect.Sets;

public class EmptyPrincipal extends UserPrincipal {
  public EmptyPrincipal() {
    super("not authorized", Sets.<String>newHashSet());
  }

  @Override
  public boolean hasRole(String role) {
    return false;
  }

  @Override
  public boolean isNull() {
    return true;
  }
}
