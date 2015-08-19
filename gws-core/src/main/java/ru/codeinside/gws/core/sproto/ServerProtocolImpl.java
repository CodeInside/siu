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
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.ServerLog;
import ru.codeinside.gws.api.ServerProtocol;
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;
import ru.codeinside.gws.api.ServiceDefinition;
import ru.codeinside.gws.api.XmlNormalizer;
import ru.codeinside.gws.api.XmlSignatureInjector;
import ru.codeinside.gws.core.Xml;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

/**
 * В тестах можно включить дамп: HttpAdapter.dump = false;
 */
public class ServerProtocolImpl implements ServerProtocol {
  private final Logger logger = Logger.getLogger(ServerProtocolImpl.class.getName());
  private final String REV;
  private final CryptoProvider cryptoProvider;
  private final Revision revision;
  private final XmlNormalizer xmlNormalizer;
  private final XmlSignatureInjector injector;

  public ServerProtocolImpl(final CryptoProvider cryptoProvider, String REV, Revision revision, XmlNormalizer xmlNormalizer, XmlSignatureInjector injector) {
    this.cryptoProvider = cryptoProvider;
    this.REV = REV;
    this.revision = revision;
    this.xmlNormalizer = xmlNormalizer;
    this.injector = injector;
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
      findOperation:
      for (final Map.Entry<QName, ServiceDefinition.Operation> op : port.operations.entrySet()) {
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
  public SOAPMessage processResponse(
      ServerRequest request, ServerResponse response, QName service, ServiceDefinition.Port port, ServerLog serverLog) {
    if (response.responseMessage != null && response.responseMessage.length > 0) {
      try {
        MessageFactory messageFactory = MessageFactory.newInstance();
        return messageFactory.createMessage(new MimeHeaders(), new ByteArrayInputStream(response.responseMessage));
      } catch (SOAPException e) {
        logger.severe("Unable deserialize SOAPMessage from bytes (SOAPException): " + e.getMessage());
        throw new RuntimeException(e);
      } catch (IOException e) {
        logger.severe("Unable deserialize SOAPMessage from bytes (IOException): " + e.getMessage());
        throw new RuntimeException(e);
      }
    }
    return getLegacySoapMessage(request, response, service, port, serverLog);
  }

  private SOAPMessage getLegacySoapMessage(
      ServerRequest request, ServerResponse response, QName service, ServiceDefinition.Port port,
      ServerLog serverLog) {
    if (request != null) {
      processChain(request, response);
    }
    if (serverLog != null) {
      serverLog.logResponse(response);
    }
    final SOAPMessage out = buildSoapMessage(response, service, port);
    cryptoProvider.sign(out);
    return out;
  }

  @Override
  public SOAPMessage createMessage(
      ServerResponse response,
      QName service, ServiceDefinition.Port port,
      ServerLog serverLog,
      OutputStream normalizedSignedInfo) {

    if (serverLog != null) {
      serverLog.logResponse(response);
    }

    try {
      SOAPMessage soapMessage = buildSoapMessage(response, service, port);

      //на случай, если в маршруте нет этапа подписи ЭП-ОВ
      if (normalizedSignedInfo == null) {
        return soapMessage;
      }

      ByteArrayOutputStream normalizedBody = new ByteArrayOutputStream();
      xmlNormalizer.normalize(soapMessage.getSOAPBody(), normalizedBody);
      ByteArrayInputStream normalizedBodyIS = new ByteArrayInputStream(normalizedBody.toByteArray());
      byte[] normalizedBodyDigest = cryptoProvider.digest(normalizedBodyIS);

      injector.prepareSoapMessage(soapMessage, normalizedBodyDigest);

      Element signedInfo = (Element) soapMessage.getSOAPHeader().getFirstChild().getFirstChild().getFirstChild();

      xmlNormalizer.normalize(signedInfo, normalizedSignedInfo);

      return soapMessage;
    } catch (SOAPException e) {
      e.printStackTrace();
      throw new IllegalStateException("Ошибка нормализации тела SOAP-сообщения", e);
    }
  }

  private void processChain(final ServerRequest request, final ServerResponse response) {
    final Packet requestPacket = request.packet;
    final Packet responsePacket = response.packet;

    if (response.action == null) {
      response.action = request.action;
    }

    // перевернём отправителя и получателя
    responsePacket.sender = requestPacket.recipient;
    responsePacket.recipient = requestPacket.sender;
    responsePacket.originator = requestPacket.originator;

    // дата обработки
    if (responsePacket.date == null) {
      responsePacket.date = new Date();
    }

    // начало цепочки запросов (обычно поставщик должен обеспечить!)
    if (responsePacket.originRequestIdRef == null) {
      if (requestPacket.originRequestIdRef != null) {
        // связываем с запросом
        responsePacket.originRequestIdRef = requestPacket.originRequestIdRef;
      } else if (request.routerPacket != null) {
        // связываем с ID присвоенным роутером
        responsePacket.originRequestIdRef = request.routerPacket.messageId;
      } else {
        // без роутера используем ID запроса.
        responsePacket.originRequestIdRef = requestPacket.requestIdRef;
      }
    }

    // цепочка запросов
    if (responsePacket.requestIdRef == null) {
      if (request.routerPacket != null) {
        // связываем с ID присвоенным роутером
        responsePacket.requestIdRef = request.routerPacket.messageId;
      } else {
        // без роутера используем ID запроса.
        responsePacket.requestIdRef = requestPacket.requestIdRef;
      }
    }

    // тип ответа
    if (responsePacket.exchangeType == null) {
      responsePacket.exchangeType = requestPacket.exchangeType;
    }

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

  private SOAPMessage buildSoapMessage(ServerResponse response, final QName service, final ServiceDefinition.Port port) {

    final ServiceDefinition.Operation operation = getOperation(service, port, response.action);
    SOAPMessage soapMessage;

    try {
      soapMessage = MessageFactory.newInstance().createMessage();

      final SOAPEnvelope envelope = soapMessage.getSOAPPart().getEnvelope();
      envelope.addNamespaceDeclaration("smev", REV);
      envelope.addNamespaceDeclaration("wsu", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
      final SOAPBody body = envelope.getBody();
      // TODO: перенести это в провайдер криптографии?
      body.addAttribute(envelope.createQName("Id", "wsu"), "body");
      final QName outArg = operation.out.parts.values().iterator().next();
      SOAPBodyElement action = body.addBodyElement(envelope.createName(outArg.getLocalPart(), "SOAP-WS", outArg.getNamespaceURI()));
      Xml.fillSmevMessageByPacket(action, response.packet, revision);
      final Enclosure[] enclosures = response.attachmens == null ? null : response.attachmens.toArray(new Enclosure[response.attachmens.size()]);
      Xml.addMessageData(response.appData, response.docRequestCode, enclosures, action, soapMessage.getSOAPPart(), cryptoProvider, revision);
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
    return soapMessage;
  }
}
