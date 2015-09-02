/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.codeinside.gws.api.CryptoProvider;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.RouterPacket;
import ru.codeinside.gws.core.enclosure.AppliedDocumentType;
import ru.codeinside.gws.core.enclosure.AppliedDocumentsType;
import ru.codeinside.gws.core.enclosure.ObjectFactory;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Общие методы разбора.
 */
final public class Xml {

  final public static String REV111111 = "http://smev.gosuslugi.ru/rev111111";
  final public static String REV120315 = "http://smev.gosuslugi.ru/rev120315";

  public static DocumentFragment parseXml(Document doc, String xml) throws SAXException, IOException,
    ParserConfigurationException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    Document d = factory.newDocumentBuilder().parse(
      new InputSource(new StringReader("<fragment>" + xml + "</fragment>")));
    // Импорт узлов для совместимости с документом
    final Node node = doc.importNode(d.getDocumentElement(), true); //doc.adoptNode(d.getDocumentElement())
    DocumentFragment docfrag = doc.createDocumentFragment();
    while (node.hasChildNodes()) {
      docfrag.appendChild(node.removeChild(node.getFirstChild()));
    }
    return docfrag;
  }

  public static TextContent getTextContent(final Element element, final String ns, final String name) {
    NodeList nodes = element.getElementsByTagNameNS(ns, name);
    if (nodes.getLength() > 0) {
      final Node node = nodes.item(0);
      if (node instanceof Element) {
        final Element child = (Element) node;
        return new TextContent(child.getTextContent());
      }
    }
    return null;
  }

  /**
   * Получить текст для "правильного" элемента.
   */
  public static String getRequiredText(Element element, String ns, String name) {
    NodeList nodes = element.getElementsByTagNameNS(ns, name);
    final Element text = (Element) nodes.item(0);
    return text.getTextContent();
  }

  public static String parseOptionalTextContent(Element element, String ns, String name) {
    TextContent textContent = getTextContent(element, ns, name);
    if (textContent != null) {
      return textContent.text;
    }
    return null;
  }

  public static byte[] parseOptionalBase64Content(Element element, String ns, String name) {
    final TextContent textContent = getTextContent(element, ns, name);
    if (textContent != null && textContent.text != null) {
      return DatatypeConverter.parseBase64Binary(textContent.text.trim());
    }
    return null;
  }

  public static Element parseAction(Element body) {
    NodeList childs = body.getChildNodes();
    if (childs.getLength() > 0) {
      for (int i = 0; i < childs.getLength(); i++) {
        final Node item = childs.item(i);
        if (item instanceof Element) {
          return (Element) item;
        }
      }
    }
    return null;
  }

  public static Element getFirstElement(Element parent, String ns, String name) {
    final NodeList nodes = parent.getElementsByTagNameNS(ns, name);
    if (nodes.getLength() != 1) {
      return null;
    }
    final Node node = nodes.item(0);
    if (node instanceof Element) {
      return (Element) node;
    }
    return null;
  }

  public static void addSystem(SOAPElement smevSender, String code, String name) throws SOAPException {
    smevSender.addChildElement("Code", "smev").setValue(code);
    smevSender.addChildElement("Name", "smev").setValue(name);
  }

  public static void fillSmevMessageByPacket(final SOAPElement action, Packet packet, Revision revision) throws SOAPException, DatatypeConfigurationException {
    final SOAPElement smevMessage = action.addChildElement("Message", "smev");
    addSystem(smevMessage.addChildElement("Sender", "smev"), packet.sender.code, packet.sender.name);
    addSystem(smevMessage.addChildElement("Recipient", "smev"), packet.recipient.code, packet.recipient.name);
    if (packet.originator != null) {
      addSystem(smevMessage.addChildElement("Originator", "smev"), packet.originator.code, packet.originator.name);
    }
    if (revision == Revision.rev120315) {
      if (packet.serviceName != null) {
        smevMessage.addChildElement("ServiceName", "smev").setValue(packet.serviceName);
      }
    }

    smevMessage.addChildElement("TypeCode", "smev").setValue(packet.typeCode.getName());
    smevMessage.addChildElement("Status", "smev").setValue(packet.status.name());

    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(packet.date);
    smevMessage.addChildElement("Date", "smev").setValue(
      DatatypeFactory.newInstance().newXMLGregorianCalendar(gc).toXMLFormat());

    smevMessage.addChildElement("ExchangeType", "smev").setValue(packet.exchangeType);
    if (packet.requestIdRef != null) {
      smevMessage.addChildElement("RequestIdRef", "smev").setValue(packet.requestIdRef);
    }
    if (packet.originRequestIdRef != null) {
      smevMessage.addChildElement("OriginRequestIdRef", "smev").setValue(packet.originRequestIdRef);
    }
    if (packet.serviceCode != null) {
      smevMessage.addChildElement("ServiceCode", "smev").setValue(packet.serviceCode);
    }
    if (packet.caseNumber != null) {
      smevMessage.addChildElement("CaseNumber", "smev").setValue(packet.caseNumber);
    }
    // для ревизии rev120315 не используем SubMessages:smev
    if (packet.testMsg != null) {
      smevMessage.addChildElement("TestMsg", "smev").setValue(packet.testMsg);
    }
    if (packet.oktmo != null) {
      smevMessage.addChildElement("OKTMO", "smev").setValue(packet.oktmo);
    }
  }

  public static Packet parseSmevMessage(final Element action, final Revision revision) {
    final String smevNs = revisionToNs(revision);

    Packet packet = new Packet();
    Element message = getFirstElement(action, smevNs, "Message");
    if (message == null) {
      throw new IllegalStateException("Ошибка в <smev:Message>");
    }
    Element sender = getFirstElement(message, smevNs, "Sender");
    Element recipient = getFirstElement(message, smevNs, "Recipient");
    if (sender == null) {
      throw new IllegalStateException("Ошибка в <smev:Sender>");
    }
    if (recipient == null) {
      throw new IllegalStateException("Ошибка в <smev:Recipient>");
    }
    packet.sender = parseInfoSystem(sender, smevNs);
    packet.recipient = parseInfoSystem(recipient, smevNs);
    {
      Element originator = getFirstElement(message, smevNs, "Originator");
      if (originator != null) {
        packet.originator = parseInfoSystem(originator, smevNs);
      }
    }
    TextContent typeCode = getTextContent(message, smevNs, "TypeCode");

    for (final Packet.Type type : Packet.Type.values()) {
      if (type.getName().equals(typeCode.text)) {
        packet.typeCode = type;
        break;
      }
    }
    TextContent status = getTextContent(message, smevNs, "Status");
    packet.status = Packet.Status.valueOf(status.text);
    TextContent date = getTextContent(message, smevNs, "Date");
    packet.date = DatatypeConverter.parseDateTime(date.text).getTime();
    packet.exchangeType = getTextContent(message, smevNs, "ExchangeType").text;
    packet.requestIdRef = trimToNull(parseOptionalTextContent(message, smevNs, "RequestIdRef"));
    packet.originRequestIdRef = trimToNull(parseOptionalTextContent(message, smevNs, "OriginRequestIdRef"));
    if (revision == Revision.rev120315) {
      packet.serviceName = parseOptionalTextContent(message, smevNs, "ServiceName");
    }
    packet.serviceCode = parseOptionalTextContent(message, smevNs, "ServiceCode");
    packet.caseNumber = parseOptionalTextContent(message, smevNs, "CaseNumber");
    packet.testMsg = parseOptionalTextContent(message, smevNs, "TestMsg");
    packet.oktmo = parseOptionalTextContent(message, smevNs, "OKTMO");

    return packet;
  }

  private static String revisionToNs(Revision revision) {
    final String smevNs;
    if (revision == Revision.rev111111) {
      smevNs = REV111111;
    } else {
      smevNs = REV120315;
    }
    return smevNs;
  }

  private static InfoSystem parseInfoSystem(final Element element, String ns) {
    final TextContent code = getTextContent(element, ns, "Code");
    final TextContent name = getTextContent(element, ns, "Name");
    if (code == null || name == null) {
      return null;
    }
    return new InfoSystem(code.text, name.text);
  }

  static public RouterPacket parseRouterPacket(final SOAPHeader soapHeader, final Revision revision) {
    if (soapHeader != null) {
      final String smevNs = revisionToNs(revision);
      final NodeList headers = soapHeader.getElementsByTagNameNS(smevNs, "Header");
      if (headers.getLength() > 0) {
        final Element header = (Element) headers.item(0);
        final TextContent messageId = getTextContent(header, smevNs, "MessageId");
        final TextContent timeStamp = getTextContent(header, smevNs, "TimeStamp");
        final TextContent nodeId = getTextContent(header, smevNs, "NodeId");
        final TextContent messageClass = getTextContent(header, smevNs, "MessageClass");
        if (messageId == null || timeStamp == null || nodeId == null || messageClass == null) {
          throw new IllegalStateException("Ошибка роутера СМЭВ: пропущен блок");
        }
        // TODO: проверить wsu:Id на достоверность подписи, нужа поддержка криптопровайдера
        RouterPacket routerPacket = new RouterPacket();
        routerPacket.messageId = messageId.text;
        routerPacket.nodeId = nodeId.text;
        routerPacket.timeStamp = DatatypeConverter.parseDateTime(timeStamp.text).getTime();
        routerPacket.direction = RouterPacket.parseDirection(messageClass.text);
        return routerPacket;
      }
    }
    return null;
  }

  public static Element getAppData(final Element messageData, final Revision revision) {
    final String smevNs = revisionToNs(revision);
    Element appData = getFirstElement(messageData, smevNs, "AppData");
    if (appData != null && appData.getChildNodes().getLength() > 0) {
      try {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        final Document doc = dbf.newDocumentBuilder().newDocument();
        appData = (Element) doc.importNode(appData, true);
        //if (appData.getPrefix() != null) {
        //  appData = (Element) doc.renameNode(appData, "", "AppData");
        //}
        doc.appendChild(appData);
        return appData;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return null;
  }

  public static MessageDataContent processMessageData(SOAPMessage message, Element action, Revision revision, CryptoProvider cryptoProvider) throws SOAPException {
    final String smevNs = revisionToNs(revision);
    final MessageDataContent mdc = new MessageDataContent();
    final Element messageData = getFirstElement(action, smevNs, "MessageData");
    if (messageData != null) {
      final Element appDocument = getFirstElement(messageData, smevNs, "AppDocument");
      if (appDocument != null) {
        Map<String, Enclosure> attachments = Collections.emptyMap();
        mdc.requestCode = parseOptionalTextContent(appDocument, smevNs, "RequestCode");
        final byte[] binaryData = parseOptionalBase64Content(appDocument, smevNs, "BinaryData");
        if (binaryData != null) {
          attachments = Zip.collectAttachments(new ByteArrayInputStream(binaryData));
        } else {
          Element reference = getFirstElement(appDocument, smevNs, "Reference");
          if (reference != null) {
            attachments = parseAttachments(message, smevNs, appDocument, reference);
          }
        }
        final Set<String> files = new LinkedHashSet<String>();
        for (final String file : attachments.keySet()) {
          if (!file.endsWith(".sig")) {
            files.add(file);
          }
        }
        final ArrayList<Enclosure> enclosures = new ArrayList<Enclosure>(files.size());
        // это можно вызывать уже ПОСЛЕ так как не зависит напрямую от протокола!
        if (mdc.requestCode != null) {
          final String metaName = mdc.requestCode + ".xml";
          if (attachments.containsKey(metaName)) {
            final AppliedDocumentsType types = unmarshallMetadata(attachments.get(metaName).content);
            if (types != null) {
              for (final AppliedDocumentType doc : types.getAppliedDocument()) {
                final String zipName = doc.getURL();
                final Enclosure enclosure;
                if (!attachments.containsKey(zipName)) {
                  enclosure = new Enclosure(zipName, new byte[0]);
                  attachments.put(zipName, enclosure);
                  files.add(zipName);
                } else {
                  enclosure = attachments.get(zipName);
                }
                enclosure.id = doc.getID();
                enclosure.digest = doc.getDigestValue();
                enclosure.mimeType = doc.getType();
                enclosure.number = doc.getNumber();
                enclosure.fileName = doc.getName();
                enclosure.code = doc.getCodeDocument();
              }
            }
          }
        }
        for (final String file : files) {
          final Enclosure enclosure = attachments.get(file);
          enclosures.add(enclosure);
          final String pkcs7File = file + ".sig";
          if (attachments.containsKey(pkcs7File)) {
            Enclosure sig = attachments.get(pkcs7File);
            enclosure.signature = cryptoProvider.fromPkcs7(sig.content);
            if (!cryptoProvider.validate(enclosure.signature, enclosure.digest, enclosure.content)) {
              if (enclosure.signature != null)
            	  enclosure.signature = enclosure.signature.toInvalid();
            }
          }
        }
        mdc.attachmens = enclosures;
      }
      mdc.appData = getAppData(messageData, revision);
    }
    return mdc;
  }

  private static Map<String, Enclosure> parseAttachments(SOAPMessage message, String smevNs, Element appDocument, Element reference) throws SOAPException {
    Map<String, Enclosure> attachments = Collections.emptyMap();
    Element include = getFirstElement(reference, "http://www.w3.org/2004/08/xop/include", "Include");
    byte[] digestValue = parseOptionalBase64Content(appDocument, smevNs, "DigestValue");
    String href = trimToNull(include.getAttribute("href"));
    if (href != null && href.startsWith("cid:")) {
      String cid = href.substring(4); // skip "cid:"
      String quotedCid = "<" + cid + ">";
      Iterator<AttachmentPart> soapAttachments = message.getAttachments();
      while (soapAttachments.hasNext()) {
        AttachmentPart attachmentPart = soapAttachments.next();
        String contentId = attachmentPart.getContentId();
        if (cid.equals(contentId) || quotedCid.equals(contentId)) {
          String contentType = attachmentPart.getContentType();
          if ("application/zip".equals(contentType)) {
            boolean digestValid = true;
            if (digestValue != null) {
              try {
                MessageDigest digest = MessageDigest.getInstance("GOST3411");
                digest.update(DatatypeConverter.printBase64Binary(attachmentPart.getRawContentBytes()).getBytes());
                digestValid = Arrays.equals(digestValue, digest.digest());
              } catch (NoSuchAlgorithmException e) {
                Logger.getLogger(Enclosure.class.getName()).warning("Отсутствует GOST3411, нельзя проверить целостность" + cid);
              }
            }
            if (digestValid) {
              attachments = Zip.collectAttachments(attachmentPart.getRawContent());
            } else {
              Logger.getLogger(Enclosure.class.getName()).warning("Нарушение целостности " + cid);
            }
          } else {
            Logger.getLogger(Enclosure.class.getName()).info("invalid contentType " + contentType + " for " + cid);
          }
          break;
        }
      }
    } else {
      Logger.getLogger(Enclosure.class.getName()).info("invalid href in Include: " + href);
    }
    return attachments;
  }

  public static void addMessageData(
    String appData,
    String enclosureDescriptor,
    Enclosure[] enclosures,
    SOAPBodyElement action,
    SOAPPart part,
    CryptoProvider cryptoProvider,
    Revision revision) throws SOAPException, ParserConfigurationException, SAXException, IOException {
    final boolean hasAppData = appData != null;
    final boolean hasEnclosures = enclosures != null && enclosures.length > 0;

    if (hasAppData || hasEnclosures) {
      final SOAPElement messageData = action.addChildElement("MessageData", "smev");
      if (hasAppData) {
        if (appData.contains("AppData>")) {
          appData = appData.replaceAll("<AppData Id=\"AppData\">", "").replaceAll("</AppData>", "");
          messageData
              .addChildElement("AppData", "smev").addAttribute(new QName("Id"), "AppData")
              .appendChild(Xml.parseXml(part, appData));
        } else {
          messageData
              .addChildElement("AppData", "smev")
              .appendChild(Xml.parseXml(part, appData));
        }
      }
      if (hasEnclosures) {
        final SOAPElement appDocument = messageData.addChildElement("AppDocument", "smev");

        // RequestCode ввели после rev110801
        final boolean requestCodeRequired = revision != Revision.rev110801;

        String requestCode = null;
        boolean needDescriptor = false;

        if (requestCodeRequired) {
          requestCode = enclosureDescriptor == null ? "metadata" : enclosureDescriptor;
          for (final Enclosure enclosure : enclosures) {
            if (enclosure.id != null || enclosure.code != null || enclosure.fileName != null || enclosure.digest != null || enclosure.number != null) {
              needDescriptor = true;
            }
          }
          if (needDescriptor) {
            final String zipPath = requestCode + ".xml";
            for (final Enclosure enclosure : enclosures) {
              if (zipPath.equals(enclosure.zipPath)) {
                needDescriptor = false;
                break;
              }
            }
          }
          appDocument
            .addChildElement("RequestCode", "smev")
            .setValue(requestCode);
        }
        final Enclosure[] enclosures2;
        if (!needDescriptor) {
          enclosures2 = enclosures;
        } else {
          enclosures2 = new Enclosure[enclosures.length + 1];
          for (int i = 0; i < enclosures.length; i++) {
            enclosures2[1 + i] = enclosures[i];
          }
          final AppliedDocumentsType docs = new AppliedDocumentsType();
          for (final Enclosure enclosure : enclosures) {
            final AppliedDocumentType doc = new AppliedDocumentType();
            doc.setURL(enclosure.zipPath);
            doc.setID(enclosure.id);
            doc.setDigestValue(enclosure.digest);
            doc.setType(enclosure.mimeType);
            doc.setNumber(enclosure.number);
            doc.setName(enclosure.fileName);
            doc.setCodeDocument(enclosure.code);
            docs.getAppliedDocument().add(doc);
          }
          enclosures2[0] = new Enclosure(requestCode + ".xml", marshallMetadata(docs));
        }
        appDocument
          .addChildElement("BinaryData", "smev")
          .setValue(Zip.toBinaryData(enclosures2, cryptoProvider));

      }
    }
  }

  private static AppliedDocumentsType unmarshallMetadata(final byte[] content) {
    try {
      final JAXBContext ctx = JAXBContext.newInstance(ObjectFactory.class);
      Unmarshaller unmarshaller = ctx.createUnmarshaller();
      Object object = unmarshaller.unmarshal(new ByteArrayInputStream(content));
      if (object instanceof JAXBElement) {
        object = ((JAXBElement<?>) object).getValue();
      }
      return (AppliedDocumentsType) object;
    } catch (JAXBException e) {
//      Logger.getLogger(Xml.class.getName()).log(Level.WARNING, "parse meta fail", e);
      System.err.println("Ошибка в процессе преобразования(unmarshalling) данных.");
      return null;
    }
  }

  private static byte[] marshallMetadata(final AppliedDocumentsType metadata) {
    try {
      final JAXBContext ctx = JAXBContext.newInstance(ObjectFactory.class);
      final Marshaller marshaller = ctx.createMarshaller();
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      marshaller.marshal(metadata, baos);
      return baos.toByteArray();
    } catch (JAXBException e) {
      Logger.getLogger(Xml.class.getName()).log(Level.WARNING, "marshall meta fail", e);
      return null;
    }
  }

  private static String trimToNull(final String text) {
    if (text == null) {
      return null;
    }
    final String trimmed = text.trim();
    if (trimmed.isEmpty()) {
      return null;
    }
    return trimmed;
  }

  /**
   * "Носитель" текста
   */
  final public static class TextContent {
    final public String text;

    TextContent(String text) {
      this.text = text;
    }

    @Override
    public String toString() {
      return text;
    }
  }

  /**
   * "Носитель" данных
   */
  final public static class MessageDataContent {
    public String requestCode;
    public List<Enclosure> attachmens;
    public Element appData;
  }


}
