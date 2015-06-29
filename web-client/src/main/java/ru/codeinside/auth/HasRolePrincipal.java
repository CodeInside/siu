/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2015, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.auth;

import java.io.Serializable;
import java.security.Principal;
import java.util.Set;

public interface HasRolePrincipal extends Principal, Nullable, Serializable {
  boolean hasRole(String role);

  Set<String> getRoles();
}
