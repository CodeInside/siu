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
import ru.codeinside.gws.core.Xml;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.LogicalMessage;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.LogicalHandler;
import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
  private final Map<URL, ServiceDefinition> definitionMap = new HashMap<URL, ServiceDefinition>();
  private final Logger logger = Logger.getLogger(getClass().getName());
  private final String REV;
  private final Revision revisionNumber;
  private final String xsdSchema;
  transient private Schema schema;

  public ClientProtocolImpl(Revision revision, String namespace, String xsdSchema, ServiceDefinitionParser definitionParser, CryptoProvider cryptoProvider) {
    this.revisionNumber = revision;
    this.REV = namespace;
    this.xsdSchema = xsdSchema;
    this.definitionParser = definitionParser;
    this.cryptoProvider = cryptoProvider;
  }

  @Override
  final public Revision getRevision() {
    return revisionNumber;
  }


  @Override
  final public ClientResponse send(URL wsdlUrl, ClientRequest request, ClientLog clientLog) {
    try {

      if (wsdlUrl == null) {
        throw new IllegalArgumentException("wsdlUrl is null");
      }

      if (request == null) {
        throw new IllegalArgumentException("request is null");
      }

      if (clientLog != null) {
        clientLog.logRequest(request);
      }

      final ServiceDefinition wsdl = parseAndCacheDefinition(wsdlUrl);
      NormalizedRequest normalizedRequest = normalize(wsdl, wsdlUrl, request);

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

      Dispatch<SOAPMessage> dispatch = service.createDispatch(
        normalizedRequest.port,
        SOAPMessage.class,
        Service.Mode.MESSAGE,
        features.toArray(new WebServiceFeature[features.size()])
      );

      try {
        SOAPMessage soapRequest = createMessage(normalizedRequest);
        soapRequest.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");
        
        final Map<String, Object> ctx = dispatch.getRequestContext();
        if (false) {
          //
          // TODO: журнал на уровне SOAP сообщения.
          //
          List<Handler> handlerChain = dispatch.getBinding().getHandlerChain();
          handlerChain.add(new LogicalHandler<LogicalMessageContext>() {
            @Override
            public boolean handleMessage(LogicalMessageContext context) {
              boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
              if (false) {
                logger.info("LOGICAL: " + outbound);
                for (String key : context.keySet()) {
                  logger.info(key + "=" + context.get(key));
                }
              }
              if (false) {
                // Работает лишь при успешном разборе,
                // так что полезность для отладки малая.
                if (!outbound) {
                  LogicalMessage message = context.getMessage();
                  try {
                    StringWriter writer = new StringWriter();
                    final Transformer transformer = TransformerFactory.newInstance().newTransformer();
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    transformer.transform(message.getPayload(), new StreamResult(writer));
                    logger.info((outbound ? "OUT" : "IN") + ": " + writer);
                  } catch (TransformerException e) {
                    //
                  }
                }
              }
              return true;
            }

            @Override
            public boolean handleFault(LogicalMessageContext context) {
              boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
              logger.info("LOGICAL FAULT ON " + (outbound ? "OUT" : "IN"));
              return true;
            }

            @Override
            public void close(MessageContext context) {
            }
          });
          dispatch.getBinding().setHandlerChain(handlerChain);
        }
        logger.finest("Use address '" + normalizedRequest.portSoapAddress + "' for " + normalizedRequest.action);
        if (clientLog != null) {
          ctx.put(ClientLog.class.getName(), clientLog);
        }
        ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, normalizedRequest.portSoapAddress);
        final String soapAction = normalizedRequest.operation.in.soapAction;
        if (soapAction != null) {
          ctx.put(BindingProvider.SOAPACTION_USE_PROPERTY, true);
          ctx.put(BindingProvider.SOAPACTION_URI_PROPERTY, soapAction);
        }
        ClientResponse clientResponse = processResult(dispatch.invoke(soapRequest));
        if (clientLog != null) {
          clientLog.logResponse(clientResponse);
        }
        return clientResponse;
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
      } catch (SOAPException e) {
        throw new RuntimeException(e);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } catch (RuntimeException e) {
      if (clientLog != null) {
        clientLog.log(e);
      }
      throw e;
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

  private SOAPMessage createMessage(final NormalizedRequest request) throws Exception {
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

    QName inArg = null;
    if (request.operation.in.parts.values().size() == 1) {
      inArg = request.operation.in.parts.values().iterator().next();
    } else {
      Iterator<QName> inPartIt = request.operation.in.parts.values().iterator();
      while (inPartIt.hasNext()) {
        inArg = inPartIt.next();
        if (inArg.equals(request.action)) {
          break;
        } else {
          inArg = null;
        }
      }

    }
    if (inArg == null) {
      throw new IllegalArgumentException("can't find suitable in part for " + request.action);
    }
    

    // TODO: может ли отличаться пространство вызова?
    SOAPBodyElement action = body.addBodyElement(envelope.createName(inArg.getLocalPart(), "SOAP-WS", inArg.getNamespaceURI()));
    Xml.fillSmevMessageByPacket(action, request.packet, revisionNumber);
    Xml.addMessageData(request.appData, request.enclosureDescriptor, request.enclosures, action, part, cryptoProvider, revisionNumber);
    validateBySchema(part);
    cryptoProvider.sign(message);
    validateBySchema(part);
    return message;
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
    
    
    // TODO webdom как пришли к такому ограничению?
    
//    if (operation.in.parts == null || operation.in.parts.size() != 1) {
//      throw new IllegalArgumentException("Invalid parts operation " + action + " for port " + portName + " in service " + serviceName);
//    }
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
