/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.p.router.web.wrap;

import com.sun.xml.ws.transport.Headers;
import com.sun.xml.ws.util.ByteArrayBuffer;
import ru.codeinside.gws.api.LogService;
import ru.codeinside.gws.p.router.registry.ProviderRegistry;
import ru.codeinside.gws.p.router.web.Registry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class LoggerFilter implements Filter {

  public void destroy() {
  }

  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
    if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
      CustomHttpServletRequestWrapper requestWrapper = new CustomHttpServletRequestWrapper((HttpServletRequest) req);
      String marker = null;
      LogService logger = getLogger();
      if (logger != null) {
        marker = logger.generateMarker(false);
      }
      requestWrapper.setServerMarker(marker);

      HttpServletResponseCopier responseCopier = new HttpServletResponseCopier((HttpServletResponse) resp);

      logRequest(requestWrapper, marker);
      chain.doFilter(requestWrapper, responseCopier);
      responseCopier.flushBuffer();
      logResponse(responseCopier, marker);
    } else {
      chain.doFilter(req, resp);
    }
  }

  public void init(FilterConfig config) throws ServletException {
  }

  public static void logResponse(HttpServletResponseCopier response, String marker) throws IOException {
    LogService logger = getLogger();
    if (logger != null && logger.shouldWriteServerLog()) {
      ByteArrayBuffer buf = new ByteArrayBuffer();
      buf.write(response.getCopy());
      dump(buf, false, getResponseHeaders(response), marker);
    }
  }

  public static void logRequest(HttpServletRequest request, String marker) throws IOException {
    LogService logger = getLogger();
    if (logger != null && logger.shouldWriteServerLog()) {
      InputStream in = request.getInputStream();
      ByteArrayBuffer buf = new ByteArrayBuffer();
      buf.write(in);
      in.close();
      dump(buf, true, getRequestHeaders(request), marker);
    }
  }

  public static Map<String, List<String>> getRequestHeaders(HttpServletRequest request) {
    Headers requestHeaders = new Headers();
    Enumeration enums = request.getHeaderNames();
    while (enums.hasMoreElements()) {
      String headerName = (String) enums.nextElement();
      Enumeration e = request.getHeaders(headerName);
      if (e != null) {
        List<String> values = requestHeaders.get(headerName);
        while (e.hasMoreElements()) {
          String headerValue = (String) e.nextElement();
          if (values == null) {
            values = new ArrayList<String>();
            requestHeaders.put(headerName, values);
          }
          values.add(headerValue);
        }
      }
    }
    return requestHeaders;
  }

  public static Map<String, List<String>> getResponseHeaders(HttpServletResponse request) {
    Headers requestHeaders = new Headers();
    Collection<String> enums = request.getHeaderNames();
    for (String headerName : enums) {
      Collection<String> e = request.getHeaders(headerName);
      if (e != null && !e.isEmpty()) {
        List<String> values = requestHeaders.get(headerName);
        for (String headerValue : e) {
          if (values == null) {
            values = new ArrayList<String>();
            requestHeaders.put(headerName, values);
          }
          values.add(headerValue);
        }
      }
    }
    return requestHeaders;
  }

  private static void dump(ByteArrayBuffer buf, boolean isRequest, Map<String, List<String>> headers, String marker) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintWriter pw = new PrintWriter(baos, true);
    if (headers != null) {
      for (Map.Entry<String, List<String>> header : headers.entrySet()) {
        if (header.getValue().isEmpty()) {
          // I don't think this is legal, but let's just dump it,
          // as the point of the dump is to uncover problems.
          pw.println(header.getValue());
        } else {
          for (String value : header.getValue()) {
            pw.println(header.getKey() + ": " + value);
          }
        }
      }
    }
    buf.writeTo(baos);
    String msg = baos.toString();
    LogService logger = getLogger();
    if (logger != null) {
      logger.log(marker, msg, isRequest, false);
    }
  }

  private static LogService getLogger() {
    Registry registry = Registry.REGISTRY.get();
    return registry == null ? null : registry.logService();
  }

}
