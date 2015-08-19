/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core.cproto;

import com.sun.xml.ws.developer.SchemaValidationFeature;
import com.sun.xml.ws.dump.MessageDumpingFeature;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import ru.codeinside.gws.api.ClientLog;
import ru.codeinside.gws.api.ClientProtocol;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.CryptoProvider;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.RouterPacket;
import ru.codeinside.gws.api.ServiceDefinition;
import ru.codeinside.gws.api.ServiceDefinitionParser;
import ru.codeinside.gws.api.VerifyResult;
import ru.codeinside.gws.api.XmlNormalizer;
import ru.codeinside.gws.api.XmlSignatureInjector;
import ru.codeinside.gws.core.Xml;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * В тестах можно включить дамп: HttpTransportPipe.dump =  true;
 */
public class ClientProtocolImpl implements ClientProtocol {

  //
  static boolean validate = false;
  static boolean validateBaseSchema = false;
  static boolean dumping = false;

  //
  private final ServiceDefinitionParser definitionParser;
  private final CryptoProvider cryptoProvider;
  private final XmlNormalizer xmlNormalizer;
  private final XmlSignatureInjector injector;
  private final Map<URL, ServiceDefinition> definitionMap = new HashMap<URL, ServiceDefinition>();
  private final Logger logger = Logger.getLogger(getClass().getName());
  private final String REV;
  private final Revision revisionNumber;
  private final String xsdSchema;
  transient private Schema schema;

  public ClientProtocolImpl(Revision revision, String namespace, String xsdSchema,
                            ServiceDefinitionParser definitionParser, CryptoProvider cryptoProvider,
                            XmlNormalizer xmlNormalizer, XmlSignatureInjector injector) {
    this.revisionNumber = revision;
    this.REV = namespace;
    this.xsdSchema = xsdSchema;
    this.definitionParser = definitionParser;
    this.cryptoProvider = cryptoProvider;
    this.xmlNormalizer = xmlNormalizer;
    this.injector = injector;
  }

  @Override
  final public Revision getRevision() {
    return revisionNumber;
  }

  @Override
  final public ClientResponse send(URL wsdlUrl, ClientRequest request, ClientLog clientLog) {
    try {
      NormalizedRequest normalizedRequest = createNormalizedRequest(wsdlUrl, request, clientLog);
      try {
        SOAPMessage soapRequest;
        if (request.requestMessage != null && request.requestMessage.length > 0) {
          MessageFactory factory = MessageFactory.newInstance();
          soapRequest = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(request.requestMessage));

          if (soapRequest.getSOAPHeader().getFirstChild() == null) {
            signSoapMessage(soapRequest);
          }

        } else {
          soapRequest = buildSoapMessage(normalizedRequest);
          signSoapMessage(soapRequest);

        }
        return getClientResponse(wsdlUrl, clientLog, normalizedRequest, soapRequest);
      } catch (RuntimeException e) {
        throw e;
      }

    } catch (RuntimeException e) {
      logException(clientLog, e);
      throw e;
    } catch (Exception e) {
      logException(clientLog, e);
      throw new RuntimeException(e);
    }
  }

  private void logException(ClientLog clientLog, Exception e) {
    if (clientLog != null) {
      clientLog.log(e);
    }
  }

  private ClientResponse getClientResponse(URL wsdlUrl, ClientLog clientLog, NormalizedRequest normalizedRequest, SOAPMessage soapRequest) {
    Dispatch<SOAPMessage> dispatch = createSoapMessageDispatch(wsdlUrl, normalizedRequest);
    final Map<String, Object> ctx = dispatch.getRequestContext();

    logger.finest("Use address '" + normalizedRequest.portSoapAddress + "' for " + normalizedRequest.action);
    if (clientLog != null) {
      ctx.put(ClientLog.class.getName(), clientLog);
    }
    ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, normalizedRequest.portSoapAddress);
    final String soapAction = normalizedRequest.operation.soapAction;
    if (soapAction != null) {
      ctx.put(BindingProvider.SOAPACTION_USE_PROPERTY, true);
      ctx.put(BindingProvider.SOAPACTION_URI_PROPERTY, soapAction);
    }
    ClientResponse clientResponse;
    try {
      clientResponse = processResult(dispatch.invoke(soapRequest));
    } catch (SOAPException e) {
      throw new RuntimeException(e);
    } catch (WebServiceException e) {
      logger.log(Level.WARNING, "GWS fail " + e.getLocalizedMessage());
      Throwable cause = e.getCause();
      while (cause instanceof RuntimeException) {
        Throwable root = cause.getCause();
        if (root == null) {
          break;
        }
        cause = root;
      }
      if (cause instanceof IOException) {
        throw new RuntimeException(cause);
      }
      if (cause instanceof RuntimeException) {
        throw (RuntimeException) cause;
      }
      throw e;
    }
    logResponse(clientLog, clientResponse);
    return clientResponse;
  }

  private void logResponse(ClientLog clientLog, ClientResponse clientResponse) {
    if (clientLog != null) {
      clientLog.logResponse(clientResponse);
    }
  }

  private SOAPMessage buildSoapMessage(NormalizedRequest normalizedRequest) throws Exception {
    SOAPMessage soapRequest = createSoapMessage(normalizedRequest);
    soapRequest.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");
    return soapRequest;
  }

  /**
   * Подготовить SOAP-сообщение перед отправкой
   *
   * @param wsdlUrl        ссылка на описание сервиса в формате WSDL.
   * @param request        запрос от клиента к поствщику.
   * @param log            журнал клиента.
   * @param normalizedSignedInfo нормализованный блок Body для получения подписи ОВ
   * @return предварительное сообщение для отправки
   */
  @Override
  public SOAPMessage createMessage(URL wsdlUrl, ClientRequest request, ClientLog log, OutputStream normalizedSignedInfo) {
    NormalizedRequest normalizedRequest = createNormalizedRequest(wsdlUrl, request, log);
    try {
      SOAPMessage soapMessage = buildSoapMessage(normalizedRequest);

      //на случай, если в маршруте нет этапа подписи ЭП-ОВ
      if (normalizedSignedInfo == null) {
        return soapMessage;
      }

      ByteArrayOutputStream normalizedBody = new ByteArrayOutputStream();
      xmlNormalizer.normalize(soapMessage.getSOAPBody(), normalizedBody);
      ByteArrayInputStream normalizedBodyIS = new ByteArrayInputStream(normalizedBody.toByteArray());
      byte[] normalizedBodyDigest = cryptoProvider.digest(normalizedBodyIS);

            // TODO: захват тела ответа зависит от провайдера!
            // Для Metro нужно переделывать "трубы" http://metro.java.net/guide/ch02.html#logging
            // пример1 - http://musingsofaprogrammingaddict.blogspot.ru/2010/03/runtime-configuration-of-schema.html
            // пример2 - http://marek.potociar.net/2009/10/19/custom-metro-tube-interceptor/
      injector.prepareSoapMessage(soapMessage, normalizedBodyDigest);

      Element signedInfo = (Element) soapMessage.getSOAPHeader().getFirstChild().getFirstChild().getFirstChild();

      xmlNormalizer.normalize(signedInfo, normalizedSignedInfo);

      return soapMessage;
    } catch (Exception e) {
      logException(log, e);
      throw new RuntimeException(e);
    }
  }

  private Dispatch<SOAPMessage> createSoapMessageDispatch(URL wsdlUrl, NormalizedRequest normalizedRequest) {
    //TODO: кешиировать сервис по wsdl и имени?
    Service service = Service.create(wsdlUrl, normalizedRequest.service);

    // TODO: захват тела ответа зависит от провайдера!
    // Для Metro нужно переделывать "трубы" http://metro.java.net/guide/ch02.html#logging
    // пример1 - http://musingsofaprogrammingaddict.blogspot.ru/2010/03/runtime-configuration-of-schema.html
    // пример2 -  http://marek.potociar.net/2009/10/19/custom-metro-tube-interceptor/

    final List<WebServiceFeature> features = new ArrayList<WebServiceFeature>();
    if (validate) {
      features.add(new SchemaValidationFeature());
    }
    if (dumping) {
      features.add(new MessageDumpingFeature(ClientProtocolImpl.class.getName(), Level.INFO, false));
    }

    return service.createDispatch(
        normalizedRequest.port,
        SOAPMessage.class,
        Service.Mode.MESSAGE,
        features.toArray(new WebServiceFeature[features.size()])
    );
  }

  private NormalizedRequest createNormalizedRequest(URL wsdlUrl, ClientRequest request, ClientLog clientLog) {
    validateWsdlUrl(wsdlUrl);
    validateClientRequest(request);
    logRequest(request, clientLog);
    final ServiceDefinition wsdl = parseAndCacheDefinition(wsdlUrl);
    return normalize(wsdl, wsdlUrl, request);
  }

  private void logRequest(ClientRequest request, ClientLog clientLog) {
    if (clientLog != null) {
      clientLog.logRequest(request);
    }
  }

  private void validateClientRequest(ClientRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("request is null");
    }
  }

  private void validateWsdlUrl(URL wsdlUrl) {
    if (wsdlUrl == null) {
      throw new IllegalArgumentException("wsdlUrl is null");
    }
  }

  private ServiceDefinition parseAndCacheDefinition(URL wsdlUrl) {
    ServiceDefinition definition;
    synchronized (definitionMap) {
      definition = definitionMap.get(wsdlUrl);
      if (definition == null) {
        definition = definitionParser.parseServiceDefinition(wsdlUrl);
        if (definition == null || definition.services == null) {
          throw new IllegalStateException(wsdlUrl.toString());
        }
        definitionMap.put(wsdlUrl, definition);
      }
    }
    return definition;
  }

  private SOAPMessage createSoapMessage(final NormalizedRequest request) throws Exception {
    final MessageFactory factory = MessageFactory.newInstance();
    final SOAPMessage message = factory.createMessage();

    final SOAPPart part = message.getSOAPPart();
    final SOAPEnvelope envelope = part.getEnvelope();

    // Стандартные пространства
    envelope.addNamespaceDeclaration("smev", REV)//
        .addNamespaceDeclaration("wsu",
            "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");

    final SOAPBody body = envelope.getBody();
    body.addAttribute(envelope.createQName("Id", "wsu"), "body");// только для подписи системы

    final QName inArg = request.operation.in.parts.values().iterator().next();
    // TODO: может ли отличаться пространство вызова?
    SOAPBodyElement action = body.addBodyElement(envelope.createName(inArg.getLocalPart(), "SOAP-WS", inArg.getNamespaceURI()));
    Xml.fillSmevMessageByPacket(action, request.packet, revisionNumber);
    Xml.addMessageData(request.appData, request.enclosureDescriptor, request.enclosures, action, part, cryptoProvider, revisionNumber);
    validateBySchema(part);
    return message;
  }

  private void signSoapMessage(SOAPMessage message) {
    cryptoProvider.sign(message);
    validateBySchema(message.getSOAPPart());
  }

  private ClientResponse processResult(final SOAPMessage message) throws SOAPException {

    ClientResponse response = new ClientResponse();

    // проверка на пакет СМЭВ
    validateBySchema(message.getSOAPPart());

    VerifyResult result = cryptoProvider.verify(message);
    response.verifyResult = result;

    if (result.error != null) {
      // даже не пытаться разбирать сбойный пакет!
      return response;
    }

    if (result.recipient == null) {
      logger.fine("Сертификата посредника нет либо тестовый провайдер безопасности!");
    }
    if (result.actor == null) {
      logger.fine("Сертификата нет либо тестовый провайдер безопасности!");
    }

    RouterPacket routerPacket = Xml.parseRouterPacket(message.getSOAPHeader(), revisionNumber);
    if (routerPacket != null) {
      if (routerPacket.direction != RouterPacket.Direction.RESPONSE) {
        throw new IllegalStateException("Ошибка роутера СМЭВ: вернул запрос");
      }
    }
    response.routerPacket = routerPacket;

    final SOAPBody soapBody = message.getSOAPBody();
    if ("Fault".equals(soapBody.getNodeName())
        && "http://www.w3.org/2003/05/soap-envelope".equals(soapBody.getNamespaceURI())) {
      logger.warning("Не обработанная ошбка SOAP " + soapBody);
    } else {
      final Element action = Xml.parseAction(soapBody);
      if (action == null) {
        throw new IllegalStateException("Пустое тело пакета");
      }
      response.action = new QName(action.getNamespaceURI(), action.getLocalName());
      response.packet = Xml.parseSmevMessage(action, revisionNumber);
      final Xml.MessageDataContent mdc = Xml.processMessageData(message, action, revisionNumber, cryptoProvider);
      response.enclosureDescriptor = mdc.requestCode;
      response.appData = mdc.appData;
      response.enclosures = mdc.attachmens != null ? mdc.attachmens.toArray(new Enclosure[mdc.attachmens.size()]) : null;
    }

    return response;
  }

  private void validateBySchema(Document document) {
    if (validateBaseSchema) {
      final Schema schema = getOrLoadSchema();
      final long startMs = System.currentTimeMillis();
      try {
        schema.newValidator().validate(new DOMSource(document));
      } catch (SAXException e) {
        e.printStackTrace(System.out);
        throw new RuntimeException(e);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      System.out.println("VALIDATE SCHEMA: " + (System.currentTimeMillis() - startMs) + "ms");
    }
  }

  private Schema getOrLoadSchema() {
    if (schema == null) {
      synchronized (this) {
        if (schema == null) {
          final long startMs = System.currentTimeMillis();
          final SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
          factory.setResourceResolver(new W3cResourceResolver("schema/"));
          InputStream is = getClass().getClassLoader().getResourceAsStream(xsdSchema);
          if (is == null) {
            throw new IllegalStateException();
          }
          try {
            schema = factory.newSchema(new StreamSource(is));
          } catch (SAXException e) {
            throw new RuntimeException(e);
          }
          System.out.println("LOAD SCHEMA: " + (System.currentTimeMillis() - startMs) + "ms");
        }
      }
    }
    return schema;
  }

  private NormalizedRequest normalize(ServiceDefinition wsdl, URL wsdlUrl, ClientRequest request) {
    if (wsdl.services == null) {
      throw new IllegalArgumentException("Invalid wsdl " + wsdlUrl);
    }
    if (!wsdl.namespaces.contains(REV)) {
      throw new IllegalArgumentException("WSDL " + wsdlUrl + " not use " + REV);
    }
    QName serviceName = request.service;
    if (request.service == null) {
      if (wsdl.services.size() != 1) {
        throw new IllegalArgumentException("Ambiguous service in " + wsdlUrl);
      }
      serviceName = wsdl.services.keySet().iterator().next();
    }
    ServiceDefinition.Service serviceDef = wsdl.services.get(serviceName);
    if (serviceDef == null || serviceDef.ports == null) {
      throw new IllegalArgumentException("Invalid service " + serviceName);
    }
    QName portName = request.port;
    if (portName == null) {
      if (serviceDef.ports.size() != 1) {
        throw new IllegalArgumentException("Ambiguous port for service " + serviceName);
      }
      portName = serviceDef.ports.keySet().iterator().next();
    }
    ServiceDefinition.Port port = serviceDef.ports.get(portName);
    if (port == null || port.operations == null) {
      throw new IllegalArgumentException("Invalid port " + portName + " in service " + serviceName);
    }
    String portSoapAddress = request.portAddress;
    if (portSoapAddress == null) {
      portSoapAddress = port.soapAddress;
    }
    if (portSoapAddress == null) {
      throw new IllegalArgumentException("Missed soapAddress for port " + portName + " in service " + serviceName);
    }

    QName action = request.action;
    if (action == null) {
      if (port.operations.size() != 1) {
        throw new IllegalArgumentException("Ambiguous operation for port " + portName + " in service " + serviceName);
      }
      action = port.operations.keySet().iterator().next();
    }
    ServiceDefinition.Operation operation = port.operations.get(action);
    if (operation == null || operation.in == null || operation.out == null) {
      throw new IllegalArgumentException("Invalid operation " + action + " for port " + portName + " in service " + serviceName);
    }
    if (operation.in.parts == null || operation.in.parts.size() != 1) {
      throw new IllegalArgumentException("Invalid parts operation " + action + " for port " + portName + " in service " + serviceName);
    }
    if (operation.out.parts == null || operation.out.parts.size() != 1) {
      throw new IllegalArgumentException("Invalid parts operation " + action + " for port " + portName + " in service " + serviceName);
    }

    final NormalizedRequest normalized = new NormalizedRequest();
    normalized.packet = request.packet;
    normalized.action = action;
    normalized.service = serviceName;
    normalized.port = portName;
    normalized.portSoapAddress = portSoapAddress;
    normalized.appData = request.appData;
    normalized.enclosureDescriptor = request.enclosureDescriptor;
    normalized.enclosures = request.enclosures;
    normalized.applicantSign = request.applicantSign;
    normalized.operation = operation;
    return normalized;
  }

  final static class NormalizedRequest {
    public Packet packet;
    public QName action;
    public QName service;
    public QName port;
    public String portSoapAddress;
    public String appData;
    public String enclosureDescriptor;
    public Enclosure[] enclosures;
    public boolean applicantSign;
    ServiceDefinition.Operation operation;
  }
}
