/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core.enclosure;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


@XmlRegistry
public class ObjectFactory {

  private final static QName _CodeDocument_QNAME = new QName("http://smev.gosuslugi.ru/request/rev111111", "CodeDocument");
  private final static QName _AppliedDocument_QNAME = new QName("http://smev.gosuslugi.ru/request/rev111111", "AppliedDocument");
  private final static QName _DigestValue_QNAME = new QName("http://smev.gosuslugi.ru/request/rev111111", "DigestValue");
  private final static QName _Name_QNAME = new QName("http://smev.gosuslugi.ru/request/rev111111", "Name");
  private final static QName _Number_QNAME = new QName("http://smev.gosuslugi.ru/request/rev111111", "Number");
  private final static QName _Type_QNAME = new QName("http://smev.gosuslugi.ru/request/rev111111", "Type");
  private final static QName _AppliedDocuments_QNAME = new QName("http://smev.gosuslugi.ru/request/rev111111", "AppliedDocuments");
  private final static QName _URL_QNAME = new QName("http://smev.gosuslugi.ru/request/rev111111", "URL");

  /**
   * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ru.gosuslugi.smev.request.rev111111
   */
  public ObjectFactory() {
  }

  /**
   * Create an instance of {@link AppliedDocumentType }
   */
  public AppliedDocumentType createAppliedDocumentType() {
    return new AppliedDocumentType();
  }

  /**
   * Create an instance of {@link ru.codeinside.gws.core.enclosure.AppliedDocumentsType }
   */
  public AppliedDocumentsType createAppliedDocumentsType() {
    return new AppliedDocumentsType();
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
   */
  @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/request/rev111111", name = "CodeDocument")
  public JAXBElement<String> createCodeDocument(String value) {
    return new JAXBElement<String>(_CodeDocument_QNAME, String.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link AppliedDocumentType }{@code >}}
   */
  @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/request/rev111111", name = "AppliedDocument")
  public JAXBElement<AppliedDocumentType> createAppliedDocument(AppliedDocumentType value) {
    return new JAXBElement<AppliedDocumentType>(_AppliedDocument_QNAME, AppliedDocumentType.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
   */
  @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/request/rev111111", name = "DigestValue")
  public JAXBElement<byte[]> createDigestValue(byte[] value) {
    return new JAXBElement<byte[]>(_DigestValue_QNAME, byte[].class, null, ((byte[]) value));
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
   */
  @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/request/rev111111", name = "Name")
  public JAXBElement<String> createName(String value) {
    return new JAXBElement<String>(_Name_QNAME, String.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
   */
  @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/request/rev111111", name = "Number")
  public JAXBElement<String> createNumber(String value) {
    return new JAXBElement<String>(_Number_QNAME, String.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
   */
  @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/request/rev111111", name = "Type")
  public JAXBElement<String> createType(String value) {
    return new JAXBElement<String>(_Type_QNAME, String.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link AppliedDocumentsType }{@code >}}
   */
  @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/request/rev111111", name = "AppliedDocuments")
  public JAXBElement<AppliedDocumentsType> createAppliedDocuments(AppliedDocumentsType value) {
    return new JAXBElement<AppliedDocumentsType>(_AppliedDocuments_QNAME, AppliedDocumentsType.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
   */
  @XmlElementDecl(namespace = "http://smev.gosuslugi.ru/request/rev111111", name = "URL")
  public JAXBElement<String> createURL(String value) {
    return new JAXBElement<String>(_URL_QNAME, String.class, null, value);
  }

}
