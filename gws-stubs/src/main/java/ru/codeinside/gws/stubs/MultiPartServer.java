/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.stubs;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused") //API
public class MultiPartServer {

  private Server server;

  List<MultiPartItem> responseItems;
  String requestBody;
  int responseStatus = HttpServletResponse.SC_OK;
  Map<String, String[]> parameterMap;
  Map<String, List<String>> headerMap;
  Exception fail;


  public void start(int port) throws Exception {
    server = new Server(new InetSocketAddress("127.0.0.1", port));
    server.setHandler(new AbstractHandler() {
      @Override
      public void handle(
        String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
          headerMap = new LinkedHashMap<String, List<String>>();
          Enumeration<String> headerNames = request.getHeaderNames();
          while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            List<String> values = new ArrayList<String>();
            final Enumeration<String> headers = request.getHeaders(headerName);
            while (headers.hasMoreElements()) {
              values.add(headers.nextElement());
            }
            headerMap.put(headerName, values);
          }
          parameterMap = request.getParameterMap();
          requestBody = Streams.toString(baseRequest.getInputStream(), request.getCharacterEncoding());
          response.setStatus(responseStatus);

          String boundary = "uuid:" + UUID.randomUUID();
          ServletOutputStream out = response.getOutputStream();
          response.setContentType("multipart/related; type=\"text/xml\"; boundary=\"" + boundary + "\"");
          for (MultiPartItem item : responseItems) {
            out.println("\n--" + boundary);
            for (Map.Entry<String, String> e : item.headers.entrySet()) {
              out.println(e.getKey() + ": " + e.getValue());
            }
            out.println();
            out.write(item.content);
          }
          out.println("\n--" + boundary + "--");
          out.flush();
          out.close();

          baseRequest.setHandled(true);
          fail = null;
        } catch (IOException e) {
          e.printStackTrace(System.err);
          fail = e;
          throw e;
        } catch (Exception e) {
          e.printStackTrace(System.err);
          fail = e;
          throw new ServletException(e);
        }
      }
    });
    server.start();
  }

  public void stop() throws Exception {
    server.stop();
  }

  public Map<String, String[]> getParameterMap() {
    return parameterMap;
  }

  public Map<String, List<String>> getHeaderMap() {
    return headerMap;
  }

  public String getRequestBody() {
    return requestBody;
  }

  public void setResponseStatus(int status) {
    responseStatus = status;
  }

  public Exception getFail() {
    return fail;
  }

  public void setResponseItems(List<MultiPartItem> responseItems) {
    this.responseItems = responseItems;
  }
}
