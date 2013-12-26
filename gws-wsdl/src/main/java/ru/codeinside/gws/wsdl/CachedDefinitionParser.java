/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.wsdl;

import ru.codeinside.gws.api.ServiceDefinition;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

final public class CachedDefinitionParser implements ru.codeinside.gws.api.ServiceDefinitionParser {

  final Map<URL, ServiceDefinition> cache = new HashMap<URL, ServiceDefinition>();
  final ru.codeinside.gws.api.ServiceDefinitionParser definitionParser;

  public CachedDefinitionParser() {
    this(new ServiceDefinitionParser());
  }

  public CachedDefinitionParser(ru.codeinside.gws.api.ServiceDefinitionParser definitionParser) {
    this.definitionParser = definitionParser;
  }

  @Override
  public ServiceDefinition parseServiceDefinition(final URL wsdlUrl) {
    synchronized (cache) {
      final ServiceDefinition cached = cache.get(wsdlUrl);
      if (cached != null) {
        return cached;
      }
      final ServiceDefinition def = definitionParser.parseServiceDefinition(wsdlUrl);
      if (def != null) {
        cache.put(wsdlUrl, def);
      }
      return def;
    }
  }
}
