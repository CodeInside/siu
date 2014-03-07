/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.p.router.web.wrap;

import ru.codeinside.gws.api.ServerLog;
import ru.codeinside.gws.p.router.web.Registry;
import ru.codeinside.gws.p.router.web.ServerLogResource;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

final public class LoggerFilter implements Filter {

  public void doFilter(ServletRequest servletRequest, ServletResponse resp,
                       FilterChain chain) throws ServletException, IOException {
    final HttpServletRequest req = (HttpServletRequest) servletRequest;
    // В СМЭВ только POST запросы
    final boolean post = "POST".equalsIgnoreCase(req.getMethod());
    final Registry registry = !post ? null : Registry.REGISTRY.get();
    final ServerLogResource serverLogResource = registry == null ? null : registry.getServerLogResource();
    if (serverLogResource != null) {
      processExchange(req, (HttpServletResponse) resp, chain, serverLogResource);
    } else {
      chain.doFilter(req, resp);
    }
  }

  private void processExchange(HttpServletRequest req, HttpServletResponse resp,
                               FilterChain chain,
                               ServerLogResource serverLogResource) throws IOException, ServletException {
    try {
      ServerLog serverLog = serverLogResource.createLog(req);
      if (serverLog == null) {
        chain.doFilter(req, resp);
      } else {
        logExchange(chain, req, resp, serverLog);
      }
    } finally {
      serverLogResource.close();
    }
  }

  private void logExchange(FilterChain chain, HttpServletRequest httpReq,
                           HttpServletResponse httpResp,
                           ServerLog serverLog) throws IOException, ServletException {
    try {
      chain.doFilter(
        new HttpServletRequestLogger(serverLog, httpReq),
        new HttpServletResponseLogger(serverLog, httpResp)
      );
    } finally {
      serverLog.close();
    }
  }

  public void init(FilterConfig config) throws ServletException {
  }

  public void destroy() {
  }

}
