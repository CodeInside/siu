/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core.sproto;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import ru.codeinside.gws.api.CryptoProvider;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.ServerProtocol;
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;
import ru.codeinside.gws.api.ServiceDefinition;
import ru.codeinside.gws.core.Xml;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.util.Map;

/**
 * В тестах можно включить дамп: HttpAdapter.dump = false;
 */
public class ServerProtocolImpl implements ServerProtocol {

  private final String REV;
  private final CryptoProvider cryptoProvider;
  private final Revision revision;

  public ServerProtocolImpl(final CryptoProvider cryptoProvider, String REV, Revision revision) {
    this.cryptoProvider = cryptoProvider;
    this.REV = REV;
    this.revision = revision;
  }

  @Override
  public Revision getRevision() {
    return revision;
  }

  //TODO: как осуществлять возврат ошибок?
  @Override
  public ServerRequest processRequest(final SOAPMessage message, final QName service, final ServiceDefinition.Port port) {
    try {
      final SOAPBody soapBody = message.getSOAPBody();
      if ("Fault".equals(soapBody.getNodeName())
        && "http://www.w3.org/2003/05/soap-envelope".equals(soapBody.getNamespaceURI())) {
        // TODO: разобрать и вернуть ошибку!
        throw new UnsupportedOperationException();
      }
      final ServerRequest serverRequest = new ServerRequest();
      Element action = Xml.parseAction(soapBody);
      QName inPart = new QName(action.getNamespaceURI(), action.getLocalName());
      ServiceDefinition.Operation operation = null;
      findOperation: for (final Map.Entry<QName, ServiceDefinition.Operation> op : port.operations.entrySet()) {
        final ServiceDefinition.Operation value = op.getValue();
        for (QName name : value.in.parts.values()) {
          if (name.equals(inPart)) {
            operation = value;
            serverRequest.action = op.getKey();
            break findOperation;
          }
        }
      }
      if (operation == null) {
        // TODO: с WSDL схемой это невозможно
        throw new IllegalArgumentException("Invalid operation " + inPart + " for port " + port.port + " in service " + service);
      }
      serverRequest.verifyResult = cryptoProvider.verify(message);
      serverRequest.routerPacket = Xml.parseRouterPacket(message.getSOAPHeader(), revision);
      serverRequest.packet = Xml.parseSmevMessage(action, revision);
      final Xml.MessageDataContent mdc = Xml.processMessageData(message, action, revision, cryptoProvider);
      serverRequest.docRequestCode = mdc.requestCode;
      serverRequest.appData = mdc.appData;
      serverRequest.attachmens = mdc.attachmens;
      return serverRequest;
    } catch (SOAPException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public SOAPMessage processResponse(final ServerResponse response, final QName service, final ServiceDefinition.Port port) {
    final ServiceDefinition.Operation operation = getOperation(service, port, response.action);
    final SOAPMessage out;
    try {
      out = MessageFactory.newInstance().createMessage();

      final SOAPEnvelope envelope = out.getSOAPPart().getEnvelope();
      envelope.addNamespaceDeclaration("smev", REV);
      envelope.addNamespaceDeclaration("wsu", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
      final SOAPBody body = envelope.getBody();
      // TODO: перенести это в провайдер!!!
      body.addAttribute(envelope.createQName("Id", "wsu"), "body");
      final QName outArg = operation.out.parts.values().iterator().next();
      SOAPBodyElement action = body.addBodyElement(envelope.createName(outArg.getLocalPart(), "SOAP-WS", outArg.getNamespaceURI()));
      Xml.fillSmevMessageByPacket(action, response.packet, revision);
      final Enclosure[] enclosures = response.attachmens == null ? null : response.attachmens.toArray(new Enclosure[response.attachmens.size()]);
      Xml.addMessageData(response.appData, response.docRequestCode, enclosures, action, out.getSOAPPart(), cryptoProvider, revision);
    } catch (SOAPException e) {
      throw new IllegalStateException(e);
    } catch (DatatypeConfigurationException e) {
      throw new IllegalStateException(e);
    } catch (SAXException e) {
      throw new IllegalStateException(e);
    } catch (ParserConfigurationException e) {
      throw new IllegalStateException(e);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
    cryptoProvider.sign(out);
    return out;
  }

  // TODO: проверки ЗА протокол в тесты
  private ServiceDefinition.Operation getOperation(final QName service, final ServiceDefinition.Port port, final QName action) {
    final ServiceDefinition.Operation operation = port.operations.get(action);
    if (operation == null || operation.in == null || operation.out == null) {
      throw new IllegalArgumentException("Invalid operation " + action + " for port " + port.port + " in service " + service);
    }
    if (operation.in.parts == null || operation.in.parts.size() != 1) {
      throw new IllegalArgumentException("Invalid parts operation " + action + " for port " + port.port + " in service " + service);
    }
    if (operation.out.parts == null || operation.out.parts.size() != 1) {
      throw new IllegalArgumentException("Invalid parts operation " + action + " for port " + port.port + " in service " + service);
    }
    return operation;
  }

}
