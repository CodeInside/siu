/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.p.router.web;

import com.sun.xml.ws.api.BindingID;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.server.InstanceResolver;
import com.sun.xml.ws.api.server.SDDocument;
import com.sun.xml.ws.api.server.SDDocumentSource;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import com.sun.xml.ws.developer.SchemaValidationFeature;
import com.sun.xml.ws.transport.http.DeploymentDescriptorParser;
import com.sun.xml.ws.transport.http.servlet.ServletAdapter;
import com.sun.xml.ws.transport.http.servlet.ServletAdapterList;
import ru.codeinside.gws.p.adapter.Adapter;
import ru.codeinside.gws.p.adapter.ProviderEntry;

import javax.servlet.ServletContext;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ServletAdapterRegistrar {

  final Logger logger = Logger.getLogger(getClass().getName());
  ServletContext context;
  private DeploymentDescriptorParser.AdapterFactory<ServletAdapter> servletAdapters;

  public void initialize(ServletContext context) {
    this.context = context;
    servletAdapters = new ServletAdapterList(context);
  }

  public void createEndpoint(final String name, final ProviderEntry entry) {
    synchronized (entry) {
      if (entry.servletAdapter != null) {
        return;
      }
      final Adapter adapter = new Adapter(entry);
      final WSEndpoint<?> endpoint = WSEndpoint.create(
        adapter.getClass(),
        false,
        InstanceResolver.createSingleton(adapter).createInvoker(),
        entry.wsService,
        entry.wsPort,
        new ServletContainer(context), // один контейнер на сервис
        createBinding(adapter.getClass(), false), //TODO: включать валидацию
        SDDocumentSource.create(entry.wsdl),
        createMetadata(entry),
        null,
        false,
        true
      );
      for (SDDocument doc : endpoint.getServiceDefinition()) {
        logger.info(doc.getURL() + " includes " + doc.getImports());
      }
      entry.servletAdapter = servletAdapters.createAdapter(name, "/" + name + "/", endpoint);
    }
  }

  private List<SDDocumentSource> createMetadata(ProviderEntry entry) {
    final List<SDDocumentSource> metadata = new ArrayList<SDDocumentSource>(entry.wsDef.resources.size());
    for (URI uri : entry.wsDef.resources) {
      try {
        metadata.add(SDDocumentSource.create(new URL(entry.wsdl, uri.toString())));
      } catch (MalformedURLException e) {
        e.printStackTrace();
      }
    }
    return metadata;
  }

  private WSBinding createBinding(Class<?> implementorClass, boolean validate) {
    final BindingID bindingID = BindingID.parse(implementorClass);
    final WebServiceFeatureList features = new WebServiceFeatureList();
    if (validate) {
      features.add(new SchemaValidationFeature());
    }
    features.addAll(bindingID.createBuiltinFeatureList());
    return bindingID.createBinding(features.toArray());
  }

}
