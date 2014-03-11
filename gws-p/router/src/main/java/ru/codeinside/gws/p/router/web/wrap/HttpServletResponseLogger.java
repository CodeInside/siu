/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.p.router.web.wrap;

import ru.codeinside.gws.api.ServerLog;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;


final public class HttpServletResponseLogger extends HttpServletResponseWrapper {

  final ServerLog serverLog;
  final HttpServletResponse response;

  String statusMessage = "OK";
  boolean hasHeaders;
  int length = -1;
  ServletOutputStream os;
  PrintWriter pw;

  public HttpServletResponseLogger(ServerLog serverLog, HttpServletResponse response) throws IOException {
    super(response);
    this.serverLog = serverLog;
    this.response = response;
  }

  @Override
  public void setStatus(int sc, String sm) {
    statusMessage = sm;
    super.setStatus(sc, sm);
  }

  @Override
  public void sendError(int sc) throws IOException {
    statusMessage = "Error" + sc;
    super.sendError(sc);
    logHeaders();
  }

  @Override
  public void sendError(int sc, String msg) throws IOException {
    statusMessage = msg;
    super.sendError(sc, msg);
    logHeaders();
  }

  @Override
  public void setContentLength(int len) {
    length = len;
    super.setContentLength(len);
  }

  @Override
  public ServletOutputStream getOutputStream() throws IOException {
    if (os == null) {
      logHeaders();
      os = new ServletOutputStreamLogger(response.getOutputStream(), serverLog.getHttpOutStream());
    }
    return os;
  }

  @Override
  public PrintWriter getWriter() throws IOException {
    if (pw == null) {
      pw = new PrintWriter(new OutputStreamWriter(getOutputStream(), response.getCharacterEncoding()), true);
    }
    return pw;
  }

  @Override
  public void flushBuffer() throws IOException {
    logHeaders();
    if (pw != null) {
      pw.flush();
    }
    if (os != null) {
      os.flush();
    }
  }

  private void logHeaders() {
    if (!hasHeaders) {
      hasHeaders = true;
      PrintWriter pw = new PrintWriter(serverLog.getHttpOutStream(), true);

      pw.print("HTTP/1.1 ");
      pw.print(response.getStatus());
      pw.print(" ");
      pw.println(statusMessage);

      String contentType = response.getContentType();
      if (contentType != null) {
        pw.println("Content-Type: " + contentType);
      }

      if (length >= 0) {
        pw.println("Content-Length: " + length);
      }

      for (String header : response.getHeaderNames()) {
        for (String value : response.getHeaders(header)) {
          pw.println(header + ": " + value);
        }
      }

      pw.println();
    }
  }
}