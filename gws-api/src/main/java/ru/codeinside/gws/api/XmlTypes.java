/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.bind.*;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import static javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT;
import static javax.xml.bind.Marshaller.JAXB_FRAGMENT;

final public class XmlTypes {
  private final JAXBContext ctx;
  private Marshaller marshaller;
  private Unmarshaller unmarshaller;

  public XmlTypes(final Class<?>... classes) {
    try {
      ctx = JAXBContext.newInstance(classes);
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }

  public XmlTypes(final String packageName) {
    try {
      ctx = JAXBContext.newInstance(packageName);
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }

  private Unmarshaller getUnmarshaller() {
    if (unmarshaller == null) {
      try {
        final Unmarshaller u = ctx.createUnmarshaller();
        unmarshaller = u;
      } catch (JAXBException e) {
        throw new RuntimeException(e);
      }
    }
    return unmarshaller;
  }

  private Marshaller getMarshaller() {
    if (marshaller == null) {
      try {
        final Marshaller m = ctx.createMarshaller();
        m.setProperty(JAXB_FORMATTED_OUTPUT, false);
        m.setProperty(JAXB_FRAGMENT, true);
        marshaller = m;
      } catch (JAXBException e) {
        throw new RuntimeException(e);
      }
    }
    return marshaller;
  }

  public String toXml(final Object... objects) {
    final StringWriter writer = new StringWriter();
    try {
      for (Object object : objects) {
        getMarshaller().marshal(object, writer);
      }
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
    return writer.toString();
  }

  public <T> String toXml(final T object, Class<T> type, QName qName) {
    final StringWriter writer = new StringWriter();
    try {
      getMarshaller().marshal(new JAXBElement<T>(qName, type, object), writer);
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
    return writer.toString();
  }

  public <T> T fromXml(Class<T> type, final String text) {
    try {
      return type.cast(getUnmarshaller().unmarshal(new StringReader(text)));
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }

  public Object toBean(Node node) {
    try {
      Object object = getUnmarshaller().unmarshal(node);
      if (object instanceof JAXBElement) {
        return ((JAXBElement) object).getValue();
      }
      return object;
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }

  public Element toElement(Object jaxb, boolean namespaceAware) {
    try {
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setNamespaceAware(namespaceAware);
      Document document = documentBuilderFactory.newDocumentBuilder().newDocument();
      getMarshaller().marshal(jaxb, document);
      return document.getDocumentElement();
    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }

  public static XMLGregorianCalendar date(String text) {
    if (text == null) {
      return null;
    }
    try {
      final GregorianCalendar calendar = new GregorianCalendar();
      calendar.setTime(new SimpleDateFormat("dd.MM.yyyy").parse(text));
      final XMLGregorianCalendar xml = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
      xml.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
      xml.setSecond(DatatypeConstants.FIELD_UNDEFINED);
      xml.setMinute(DatatypeConstants.FIELD_UNDEFINED);
      xml.setHour(DatatypeConstants.FIELD_UNDEFINED);
      xml.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
      return xml;
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String beanToXml(final Object object) {
    return new XmlTypes(object.getClass()).toXml(object);
  }

  public static Object elementToBean(final Element element, final Class<?>... classes) {
    return new XmlTypes(classes).toBean(element);
  }

  public static <T> T elementToBean(final Element element, final Class<T> expected) {
    return expected.cast(new XmlTypes(expected).toBean(element));
  }

  public static Element beanToElement(final Object object, final boolean namespaceAware) {
    return new XmlTypes(object.getClass()).toElement(object, namespaceAware);
  }

  public static XMLGregorianCalendar dateTimeAndZeroMilliseconds(String text) {
    if (text == null) {
      return null;
    }
    try {
      final GregorianCalendar calendar = new GregorianCalendar();
      calendar.setTime(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(text));
      final XMLGregorianCalendar xml = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
      xml.setMillisecond(0);
      xml.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
      return xml;
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }
}
