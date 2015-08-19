/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core;

import ru.codeinside.gws.api.ClientProtocol;
import ru.codeinside.gws.api.CryptoProvider;
import ru.codeinside.gws.api.LogService;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.ServerProtocol;
import ru.codeinside.gws.api.ServiceDefinition;
import ru.codeinside.gws.api.ServiceDefinitionParser;
import ru.codeinside.gws.api.XmlNormalizer;
import ru.codeinside.gws.api.XmlSignatureInjector;
import ru.codeinside.gws.core.cproto.ClientRev111111;
import ru.codeinside.gws.core.cproto.ClientRev120315;
import ru.codeinside.gws.core.sproto.R111111;
import ru.codeinside.gws.core.sproto.R120315;

final public class ProtocolFactory implements ru.codeinside.gws.api.ProtocolFactory {

  private ServiceDefinitionParser definitionParser;
  private CryptoProvider cryptoProvider;
  private XmlNormalizer xmlNormalizer;
  private XmlSignatureInjector xmlSignatureInjector;

  private LogService logService;

  public void setDefinitionParser(final ServiceDefinitionParser definitionParser) {
    if (this.definitionParser != null) {
      throw new IllegalStateException("Предыдущий разборщик не убран");
    }
    this.definitionParser = definitionParser;
  }

  public void removeDefinitionParser(final ServiceDefinitionParser definitionParser) {
    if (this.definitionParser != definitionParser) {
      throw new IllegalArgumentException("Предыдущий разборщик отличается");
    }
    this.definitionParser = null;
  }

  public void setCryptoProvider(final CryptoProvider cryptoProvider) {
    if (this.cryptoProvider != null) {
      throw new IllegalStateException("Предыдущий провайдер не убран");
    }
    this.cryptoProvider = cryptoProvider;
  }

  public void removeCryptoProvider(final CryptoProvider cryptoProvider) {
    if (this.cryptoProvider != cryptoProvider) {
      throw new IllegalStateException("Предыдущий провайдер отличается");
    }
    this.cryptoProvider = null;
  }

  public void setXmlNormalizer(final XmlNormalizer xmlNormalizer) {
    if (this.xmlNormalizer != null) {
      throw new IllegalStateException("Предыдущий XmlNormalizer не убран");
    }
    this.xmlNormalizer = xmlNormalizer;
  }

  public void removeXmlNormalizer(final XmlNormalizer xmlNormalizer){
    if (this.xmlNormalizer != xmlNormalizer) {
      throw new IllegalStateException("Предыдущий XmlNormalizer отличается");
    }
    this.xmlNormalizer = null;
  }

  public void setXmlSignatureInjector(final XmlSignatureInjector xmlSignatureInjector) {
    if (this.xmlSignatureInjector != null) {
      throw new IllegalStateException("Предыдущий XmlSignatureInjector не убран");
    }
    this.xmlSignatureInjector = xmlSignatureInjector;
  }

  public void removeXmlSignatureInjector(final XmlSignatureInjector xmlSignatureInjector) {
    if (this.xmlSignatureInjector != xmlSignatureInjector) {
      throw new IllegalStateException("Предыдущий XmlSignatureInjector отличается");
    }
    this.xmlSignatureInjector = null;
  }

  public void addLogService(final LogService log) {
    if (this.logService != null) {
      throw new IllegalStateException("Предыдущий logger не убран");
    }
    if (log != null) {
      logService = log;
    }
  }

  public void removeLogService(final LogService log) {
    if (logService != log) {
      throw new IllegalArgumentException("Предыдущий logger отличается");
    }
    logService = null;
  }


  @Override
  public ClientProtocol createClientProtocol(final Revision revision) {
    if (revision == null || revision == Revision.rev110801) {
      throw new IllegalArgumentException();
    }
    if (revision == Revision.rev111111) {
      return new ClientRev111111(definitionParser, cryptoProvider, xmlNormalizer, xmlSignatureInjector);
    }
    return new ClientRev120315(definitionParser, cryptoProvider, xmlNormalizer, xmlSignatureInjector);
  }

  @Override
  public ServerProtocol createServerProtocol(final ServiceDefinition wsdl) {
    if (wsdl.namespaces.contains(Xml.REV120315)) {
      return new R120315(cryptoProvider, xmlNormalizer, xmlSignatureInjector);
    }
    if (wsdl.namespaces.contains(Xml.REV111111)) {
      return new R111111(cryptoProvider, xmlNormalizer, xmlSignatureInjector);
    }
    throw new IllegalArgumentException("Неизвестный тип протокола");
  }

}
