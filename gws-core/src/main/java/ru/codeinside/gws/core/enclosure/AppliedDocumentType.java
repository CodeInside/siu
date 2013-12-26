/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core.enclosure;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AppliedDocumentType", propOrder = {
  "codeDocument",
  "name",
  "number",
  "url",
  "type",
  "digestValue"
})
public class AppliedDocumentType {

  @XmlElement(name = "CodeDocument")
  protected String codeDocument;
  @XmlElement(name = "Name", required = true)
  protected String name;
  @XmlElement(name = "Number")
  protected String number;
  @XmlElement(name = "URL", required = true)
  protected String url;
  @XmlElement(name = "Type", required = true)
  protected String type;
  @XmlElement(name = "DigestValue")
  protected byte[] digestValue;
  @XmlAttribute(name = "ID", namespace = "http://smev.gosuslugi.ru/request/rev111111")
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlID
  protected String id;

  public String getCodeDocument() {
    return codeDocument;
  }

  public void setCodeDocument(String value) {
    this.codeDocument = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String value) {
    this.name = value;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String value) {
    this.number = value;
  }

  public String getURL() {
    return url;
  }

  public void setURL(String value) {
    this.url = value;
  }

  public String getType() {
    return type;
  }

  public void setType(String value) {
    this.type = value;
  }

  public byte[] getDigestValue() {
    return digestValue;
  }

  public void setDigestValue(byte[] value) {
    this.digestValue = ((byte[]) value);
  }

  public String getID() {
    return id;
  }

  public void setID(String value) {
    this.id = value;
  }

}
