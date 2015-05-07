/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2015, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.security.Principal;

public class PrincipalRequestWrapper extends HttpServletRequestWrapper {
  private final HasRolePrincipal principal;

  public PrincipalRequestWrapper(HttpServletRequest request, HasRolePrincipal principal) {
    super(request);
    this.principal = principal;
  }

  @Override
  public Principal getUserPrincipal() {
    return principal;
  }

  @Override
  public boolean isUserInRole(String role) {
    return principal.hasRole(role);
  }
}
