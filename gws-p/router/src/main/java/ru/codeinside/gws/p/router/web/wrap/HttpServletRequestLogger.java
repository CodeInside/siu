/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.p.router.web.wrap;


import ru.codeinside.gws.api.ServerLog;
import ru.codeinside.gws.p.router.web.Servlet;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.Enumeration;

final class HttpServletRequestLogger extends HttpServletRequestWrapper {

  final HttpServletRequest request;
  final ServerLog serverLog;

  ServletInputStream inputStream;

  HttpServletRequestLogger(ServerLog serverLog, HttpServletRequest request) {
    super(request);
    this.request = request;
    this.serverLog = serverLog;

    PrintWriter pw = new PrintWriter(serverLog.getHttpInStream(), true); //TODO: what about charset?
    pw.print(request.getMethod());
    pw.print(" ");
    pw.print(request.getRequestURI());
    pw.println("  HTTP/1.1");
    Enumeration<String> headers = request.getHeaderNames();
    while (headers.hasMoreElements()) {
      String header = headers.nextElement();
      Enumeration<String> values = request.getHeaders(header);
      while (values.hasMoreElements()) {
        pw.println(header + ": " + values.nextElement());
      }
    }
    pw.println();
    request.setAttribute(ServerLog.class.getName(), serverLog);
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {
    if (inputStream == null) {
      inputStream = new ServletInputStreamLogger(request.getInputStream(), serverLog.getHttpInStream());
    }
    return inputStream;
  }

}
