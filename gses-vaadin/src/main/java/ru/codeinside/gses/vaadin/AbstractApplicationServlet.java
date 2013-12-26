/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.vaadin;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

abstract public class AbstractApplicationServlet extends com.vaadin.terminal.gwt.server.AbstractApplicationServlet {

  public ServletContext getServletContext() {
    final ServletContext ctx = super.getServletContext();
    return new ServletContext() {

      @Override
      public void setAttribute(String name, Object object) {
        ctx.setAttribute(name, object);
      }

      @Override
      public void removeAttribute(String name) {
        ctx.removeAttribute(name);
      }

      @Override
      public void log(String message, Throwable throwable) {
        ctx.log(message, throwable);
      }

      @Override
      public void log(Exception exception, String msg) {
        ctx.log(msg, exception);
      }

      @Override
      public void log(String msg) {
        ctx.log(msg);
      }

      @Override
      public Enumeration<Servlet> getServlets() {
        return ctx.getServlets();
      }

      @Override
      public Enumeration<String> getServletNames() {
        return ctx.getServletNames();
      }

      @Override
      public String getServletContextName() {
        return ctx.getServletContextName();
      }

      @Override
      public Servlet getServlet(String name) throws ServletException {
        return ctx.getServlet(name);
      }

      @Override
      public String getServerInfo() {
        return ctx.getServerInfo();
      }

      @Override
      public Set<String> getResourcePaths(String path) {
        return ctx.getResourcePaths(path);
      }

      @Override
      public InputStream getResourceAsStream(String path) {
        return ctx.getResourceAsStream(path);
      }

      @Override
      public URL getResource(String path) throws MalformedURLException {
        if (path != null && path.startsWith("/VAADIN/")) {
          final URL r = AbstractApplicationServlet.class.getClassLoader().getResource(path.substring(1));
          if (r != null) {
            return r;
          }
        }
        return ctx.getResource(path);
      }

      @Override
      public RequestDispatcher getRequestDispatcher(String path) {
        return ctx.getRequestDispatcher(path);
      }

      @Override
      public String getRealPath(String path) {
        return ctx.getRealPath(path);
      }

      @Override
      public RequestDispatcher getNamedDispatcher(String name) {
        return ctx.getNamedDispatcher(name);
      }

      @Override
      public int getMinorVersion() {
        return ctx.getMinorVersion();
      }

      @Override
      public String getMimeType(String file) {
        return ctx.getMimeType(file);
      }

      @Override
      public int getMajorVersion() {
        return ctx.getMajorVersion();
      }

      @Override
      public Enumeration<String> getInitParameterNames() {
        return ctx.getInitParameterNames();
      }

      @Override
      public String getInitParameter(String name) {
        return ctx.getInitParameter(name);
      }


      @Override
      public String getContextPath() {
        return ctx.getContextPath();
      }

      @Override
      public ServletContext getContext(String uripath) {
        return ctx.getContext(uripath);
      }

      @Override
      public Enumeration<String> getAttributeNames() {
        return ctx.getAttributeNames();
      }

      @Override
      public Object getAttribute(String name) {
        return ctx.getAttribute(name);
      }

    };
  }

}
