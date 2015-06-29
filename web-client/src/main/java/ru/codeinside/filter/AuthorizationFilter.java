/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2015, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.filter;

import com.google.common.collect.Sets;
import ru.codeinside.auth.EmptyPrincipal;
import ru.codeinside.auth.HasRolePrincipal;
import ru.codeinside.auth.PrincipalRequestWrapper;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@WebFilter(urlPatterns = {"/*"}, initParams = {
    @WebInitParam(name = "/ui", value = "Executor,Supervisor,SuperSupervisor,Declarant,Manager"),
    @WebInitParam(name = "/admin", value = "Administrator")
})
public class AuthorizationFilter implements Filter {
  public static final String SESSION_ATTR_USER_PRINCIPAL = "userPrincipal";
  private Map<String, Set<String>> securityResources;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    initParams(filterConfig);
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
    String path = httpRequest.getServletPath();
    HasRolePrincipal principal = getPrincipal(httpRequest);
    PrincipalRequestWrapper requestWrapper = new PrincipalRequestWrapper(httpRequest, principal);

    if (securityResources.containsKey(path)) {
      if (principal.isNull()) {
        httpRequest.getRequestDispatcher("/login").forward(requestWrapper, servletResponse);
        return;
      }

      Set<String> roles = securityResources.get(path);
      if (Sets.intersection(roles, principal.getRoles()).isEmpty()) {
        ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_FORBIDDEN);
        servletResponse.setCharacterEncoding("UTF-8");
        requestWrapper.getRequestDispatcher("/403.jsp").include(requestWrapper, servletResponse);
        return;
      }
    }

    filterChain.doFilter(requestWrapper, servletResponse);
  }

  @Override
  public void destroy() {
    if (securityResources != null) {
      securityResources.clear();
      securityResources = null;
    }
  }

  private void initParams(FilterConfig config) {
    securityResources = new HashMap<String, Set<String>>();
    for (Enumeration<String> e = config.getInitParameterNames(); e.hasMoreElements();) {
      String urlPattern = e.nextElement();
      String[] roles = config.getInitParameter(urlPattern).split(",");
      Set<String> rolesSet = new HashSet<String>();
      for (String role : roles) {
        rolesSet.add(role);
      }
      securityResources.put(urlPattern, rolesSet);
    }
  }

  private HasRolePrincipal getPrincipal(HttpServletRequest request) {
    Object principal = request.getSession().getAttribute(SESSION_ATTR_USER_PRINCIPAL);
    if (principal instanceof HasRolePrincipal) {
      return (HasRolePrincipal) principal;
    }
    return new EmptyPrincipal();
  }
}
