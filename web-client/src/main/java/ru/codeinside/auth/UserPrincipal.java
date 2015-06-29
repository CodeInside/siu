/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2015, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.auth;

import com.google.common.collect.Sets;

import java.util.Set;

public class UserPrincipal implements HasRolePrincipal {

  protected String name;

  protected Set<String> roles = Sets.newHashSet();

  UserPrincipal() { }

  public UserPrincipal(String name, Set<String> roles) {
    this.name = name;
    this.roles.addAll(roles);
  }

  /**
   * Returns the name of this principal.
   *
   * @return the name of this principal.
   */
  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean hasRole(String role) {
    return roles.contains(role);
  }

  @Override
  public Set<String> getRoles() {
    return Sets.newHashSet(roles);
  }

  @Override
  public boolean isNull() {
    return false;
  }
}
