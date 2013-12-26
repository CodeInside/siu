/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.wsdl;

import org.ow2.easywsdl.schema.api.Element;
import org.ow2.easywsdl.schema.api.Import;
import org.ow2.easywsdl.schema.api.Include;
import org.ow2.easywsdl.schema.api.Schema;
import org.ow2.easywsdl.schema.api.Type;
import org.ow2.easywsdl.schema.api.extensions.NamespaceMapperImpl;
import org.ow2.easywsdl.wsdl.api.Binding;
import org.ow2.easywsdl.wsdl.api.BindingOperation;
import org.ow2.easywsdl.wsdl.api.Description;
import org.ow2.easywsdl.wsdl.api.Endpoint;
import org.ow2.easywsdl.wsdl.api.Operation;
import org.ow2.easywsdl.wsdl.api.Part;
import org.ow2.easywsdl.wsdl.api.Service;
import org.ow2.easywsdl.wsdl.api.Types;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfBinding;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfParam;
import org.ow2.easywsdl.wsdl.api.binding.BindingProtocol;
import org.ow2.easywsdl.wsdl.impl.generic.WSDLReaderImpl;
import ru.codeinside.gws.api.ServiceDefinition;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

final public class ServiceDefinitionParser implements ru.codeinside.gws.api.ServiceDefinitionParser {

  final private Logger log = Logger.getLogger(getClass().getName());

  @Override
  public ServiceDefinition parseServiceDefinition(URL wsdlUrl) {
    final Description description = parse(wsdlUrl);
    final ServiceDefinition definition = new ServiceDefinition();
    definition.resources = getImportsAndIncludes(description);
    definition.namespaces = getAllNamespaces(description);
    definition.services = new LinkedHashMap<QName, ServiceDefinition.Service>();
    for (Service service : description.getServices()) {
      final QName serviceQName = service.getQName();
      ServiceDefinition.Service tService = new ServiceDefinition.Service();
      tService.ports = new LinkedHashMap<QName, ServiceDefinition.Port>();
      for (Endpoint endpoint : service.getEndpoints()) {
        if (endpoint.getHttpAuthenticationRealm() != null
          || endpoint.getHttpAuthenticationScheme() != null) {
          log.warning("HttpAuthentication not supported!");
          continue;
        }


        ServiceDefinition.Port port = new ServiceDefinition.Port();
        port.operations = new LinkedHashMap<QName, ServiceDefinition.Operation>();
        port.soapAddress = endpoint.getAddress();
        Binding binding = endpoint.getBinding();

        if (AbsItfBinding.BindingConstants.SOAP11_BINDING4WSDL11 != binding.getTypeOfBinding()) {
          log.warning("binding type:" + binding.getTypeOfBinding());
          continue;
        }

        final AbsItfBinding.StyleConstant bindingStyle = binding.getStyle();
        if (bindingStyle != null) {
          if (AbsItfBinding.StyleConstant.DOCUMENT != bindingStyle) {
            log.warning("binding style:" + bindingStyle);
            continue;
          }
        }

        if (!"1.1".equals(binding.getVersion())) {
          log.warning("binding version:" + binding.getVersion());
          continue;
        }

        if (!"http://schemas.xmlsoap.org/soap/http".equals(binding.getTransportProtocol())) {
          log.warning("binding transport:" + binding.getTransportProtocol());
          continue;
        }

        port.binding = binding.getQName();
        port.port = binding.getInterface().getQName();
        final List<BindingOperation> operations = binding.getBindingOperations();
        for (BindingOperation bop : operations) {
          if (bop.getMEP() != BindingProtocol.SOAPMEPConstants.REQUEST_RESPONSE) {
            log.warning("soap MEP: " + bop.getMEP());
            continue;
          }
          if (bop.getStyle() != AbsItfBinding.StyleConstant.DOCUMENT) {
            log.warning("style: " + bop.getStyle());
            continue;
          }
          ServiceDefinition.Operation sOp = new ServiceDefinition.Operation();
          sOp.in = new ServiceDefinition.Arg();
          sOp.out = new ServiceDefinition.Arg();
          port.operations.put(bop.getQName(), sOp);

          final Operation op = bop.getOperation();
          sOp.in = parseParam(op.getInput());
          sOp.out = parseParam(op.getOutput());
        }
        tService.ports.put(new QName(port.port.getNamespaceURI(), endpoint.getName()), port);
      }
      definition.services.put(serviceQName, tService);
    }
    return definition;
  }

  private Set<URI> getImportsAndIncludes(Description description) {
    final Set<URI> uris = new LinkedHashSet<URI>();
    for (final Schema schema : description.getTypes().getSchemas()) {
      collectImportsAndIncludes(uris, schema);
    }
    return uris;
  }

  private void collectImportsAndIncludes(Set<URI> uris, Schema schema) {
    // Схемы может вообще не быть!
    if (schema != null) {
      for (final Include i : schema.getIncludes()) {
        if (uris.add(i.getLocationURI())) {
          collectImportsAndIncludes(uris, i.getSchema());
        }
      }
      for (final Import i : schema.getImports()) {
        if (uris.add(i.getLocationURI())) {
          collectImportsAndIncludes(uris, i.getSchema());
        }
      }
    }
  }

  private ServiceDefinition.Arg parseParam(AbsItfParam input) {
    ServiceDefinition.Arg arg = new ServiceDefinition.Arg();
    arg.message = input.getMessageName();
    arg.name = input.getName();
    try {
      Map<QName, String> attributes = input.getOtherAttributes();
      arg.soapAction = attributes.get(new QName("http://www.w3.org/2007/05/addressing/metadata", "Action"));
    } catch (Exception e) {
      log.log(Level.WARNING, "check action fail", e);
    }
    arg.parts = new LinkedHashMap<String, QName>();
    for (Part part : input.getParts()) {
      final String partName = part.getPartQName().getLocalPart();
      final Element el = part.getElement();
      if (el != null) {
        arg.parts.put(partName, el.getQName());
      } else {
        final Type type = part.getType();
        if (type != null) {
          arg.parts.put(partName, type.getQName());
        }
      }
    }
    return arg;
  }

  private Set<String> getAllNamespaces(final Description description) {
    final Set<String> allNamespaces = new LinkedHashSet<String>();
    final NamespaceMapperImpl namespaces = description.getNamespaces();
    for (final Map.Entry<String, String> e : namespaces.getNamespaces().entrySet()) {
      final String ns = e.getValue();
      if (log.isLoggable(Level.FINE)) {
        String p = e.getKey();
        log.fine("NS: " + p + ":" + ns);
      }
      allNamespaces.add(ns);
    }
    Types types = description.getTypes();
    for (final Schema schema : types.getSchemas()) {
      if (log.isLoggable(Level.FINE) && !schema.getElements().isEmpty()) {
        log.fine("elements: " + schema.getElements());
      }
      for (final Type type : schema.getTypes()) {
        boolean isNew = allNamespaces.add(type.getQName().getNamespaceURI());
        if (isNew && log.isLoggable(Level.FINE)) {
          log.fine("T: " + type.getQName());
        }
      }
    }
    return allNamespaces;
  }

  private Description parse(URL wsdlUrl) {
    try {
      return new WSDLReaderImpl().read(wsdlUrl);
    } catch (WSDLException e) {
      throw new RuntimeException(e);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
