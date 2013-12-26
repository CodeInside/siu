/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.p.router.web;

import com.sun.xml.ws.transport.http.HttpAdapter;
import ru.codeinside.gws.p.adapter.ProviderEntry;
import ru.codeinside.gws.p.adapter.Registry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.TreeSet;
import java.util.logging.Logger;

final public class Chain {

  final HttpServletRequest req;
  final HttpServletResponse resp;
  final ServletAdapterRegistrar registrar;
  final long startAt = System.currentTimeMillis();
  final Registry registry = Registry.REGISTRY.get();

  public Chain(final HttpServletRequest req, final HttpServletResponse resp, final ServletAdapterRegistrar registrar) {
    this.req = req;
    this.resp = resp;
    this.registrar = registrar;
  }

  final static class Result {
    final Cmd cmd;
    final boolean sleep;

    Result(final Cmd cmd, final boolean sleep) {
      this.cmd = cmd;
      this.sleep = sleep;
    }
  }

  interface Cmd {
    Result process() throws IOException;
  }

  public void process() throws IOException {
    Cmd cmd = new GetInfoCmd();
    while (cmd != null) {
      final Result result = cmd.process();
      if (result == null) {
        cmd = null;
      } else {
        cmd = result.cmd;
        if (result.sleep) {
          try {
            Thread.sleep(100);
          } catch (InterruptedException e) {
            timeOut();
            cmd = null;
          }
          if (cmd != null && System.currentTimeMillis() - startAt > 20000L) {
            timeOut();
            cmd = null;
          }
        }
      }
    }
  }

  private void timeOut() throws IOException {
    //TODO: если SOAP запрос, вернуть SOAP ошибку!
    resp.sendError(HttpServletResponse.SC_GATEWAY_TIMEOUT);
  }

  private void notFound(String path, HttpServletResponse resp) throws IOException {
    //TODO: если SOAP запрос, вернуть SOAP ошибку!
    resp.sendError(HttpServletResponse.SC_NOT_FOUND, path);
  }

  final class RegistrarCmd implements Cmd {

    final String service;

    public RegistrarCmd(String service) {
      this.service = service;
    }

    @Override
    public Result process() throws IOException {
      final ProviderEntry entry = registry.getProviderEntry(service);
      if (entry == null || entry.wsService == null) {
        return new Result(this, true);
      }
      registrar.createEndpoint(service, entry);
      return new Result(new ProcessCmd(service), false);
    }
  }

  final class ProcessCmd implements Cmd {

    final String service;

    ProcessCmd(String service) {
      this.service = service;
    }

    @Override
    public Result process() throws IOException {
      final ProviderEntry entry = registry.getProviderEntry(service);
      if (entry == null || entry.servletAdapter == null) {
        return new Result(new RegistrarCmd(service), true);
      }
      if (!"get".equalsIgnoreCase(req.getMethod())) {
        // Ждём только SOAP
        if (entry.protocol == null || entry.declarant == null) {
          return new Result(this, true);
        }
      }
      entry.servletAdapter.invokeAsync(registrar.context, req, resp, new HttpAdapter.CompletionCallback() {
        @Override
        public void onCompletion() {
        }
      });
      return null;
    }
  }

  final class CheckProviderCmd implements Cmd {
    @Override
    public Result process() throws IOException {
      final String pathInfo = req.getPathInfo();
      int slash = pathInfo.indexOf('/', 1);
      if (slash < 0) {
        slash = pathInfo.length();
      }
      final String service = pathInfo.substring(1, slash);
      if (registry == null || null == registry.getProviderEntry(service)) {
        notFound(pathInfo, resp);
        return null;
      }
      return new Result(new WsdlCmd(service), false);
    }
  }

  final class WsdlCmd implements Cmd {

    final String service;

    public WsdlCmd(String service) {
      this.service = service;
    }

    @Override
    public Result process() throws IOException {
      final ProviderEntry entry = registry.getProviderEntry(service);
      if (entry == null || entry.wsService == null) {
        return new Result(this, true);
      }
      if ("get".equalsIgnoreCase(req.getMethod())) {
        final String pathInfo = req.getPathInfo();
        if (pathInfo.endsWith(".xsd")) {
          int slash = pathInfo.indexOf('/', 1);
          final String xsd = pathInfo.substring(slash);
          try {
            String baseUri = entry.wsdl.toURI().toString();
            baseUri = baseUri.substring(0, baseUri.lastIndexOf('/'));
            resp.setContentType("text/xml;charset=utf-8");
            InputStream input = new URL(baseUri + xsd).openStream();
            ServletOutputStream output = resp.getOutputStream();
            byte[] buffer = new byte[1024];
            int n;
            while (-1 != (n = input.read(buffer))) {
              output.write(buffer, 0, n);
            }
            input.close();
            output.close();
          } catch (URISyntaxException e) {
            e.printStackTrace();
          }
        }
      }
      return new Result(new RegistrarCmd(service), false);
    }
  }

  final class GetInfoCmd implements Cmd {
    @Override
    public Result process() throws IOException {
      final String pathInfo = req.getPathInfo();
      if (pathInfo == null || pathInfo.equals("/")) {
        if ("get".equalsIgnoreCase(req.getMethod())) {
          resp.setContentType("text/html; charset=utf8");
          final PrintWriter writer = resp.getWriter();
          writer.println("<html><body><h1>Web Service Registry</h1><ul>");
          TreeSet<String> names = new TreeSet<String>(registry.names());
          for (String name : names) {
            writer.println("<li><a href=\"" + name + "/\">" + name + "</a></li>");
          }
          writer.println("</ul></body></html>");
          writer.close();
        } else {
          Logger.getLogger(getClass().getName()).warning("Не найден '" + pathInfo + "' /" + req.getMethod());
          notFound(pathInfo, resp);
        }
        return null;
      }
      return new Result(new CheckProviderCmd(), false);
    }
  }
}
