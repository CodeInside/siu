/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.p.router.web;

import com.sun.xml.ws.api.server.BoundEndpoint;
import com.sun.xml.ws.api.server.Container;
import com.sun.xml.ws.transport.http.servlet.ServletModule;

import javax.servlet.ServletContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

final class ServletContainer extends Container {
  final ServletContext servletContext;

  final ServletModule module = new ServletModule() {
    final List<BoundEndpoint> endpoints = new ArrayList<BoundEndpoint>();

    public List<BoundEndpoint> getBoundEndpoints() {
      return endpoints;
    }

    public String getContextPath() {
      return servletContext.getContextPath();
    }
  };

  final com.sun.xml.ws.api.ResourceLoader loader = new com.sun.xml.ws.api.ResourceLoader() {
    public URL getResource(String resource) throws MalformedURLException {
      return getClass().getClassLoader().getResource(resource);
    }
  };

  ServletContainer(ServletContext servletContext) {
    this.servletContext = servletContext;
  }

  public <T> T getSPI(Class<T> spiType) {
    if (spiType == ServletContext.class) {
      return spiType.cast(servletContext);
    }
    if (spiType.isAssignableFrom(ServletModule.class)) {
      return spiType.cast(module);
    }
    if (spiType == com.sun.xml.ws.api.ResourceLoader.class) {
      return spiType.cast(loader);
    }
    return null;
  }
}
