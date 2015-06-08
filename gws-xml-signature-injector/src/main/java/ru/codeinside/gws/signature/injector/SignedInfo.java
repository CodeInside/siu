/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.signature.injector;

import org.apache.commons.codec.binary.Base64;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class SignedInfo {
  @XmlElement(name = "CanonicalizationMethod", namespace = XMLDSign.XMLNS)
  public CanonicalizationMethod canonicalizationMethod = new CanonicalizationMethod();

  @XmlElement(name = "SignatureMethod", namespace = XMLDSign.XMLNS)
  public SignatureMethod signatureMethod = new SignatureMethod();

  @XmlElement(name = "Reference", namespace = XMLDSign.XMLNS)
  public Reference reference = new Reference();

  public SignedInfo() {

  }

  public SignedInfo(byte[] digest, String id) {
    canonicalizationMethod.algorithm = XMLDSign.CANONICALIZATION_METHOD;
    signatureMethod.algorithm = XMLDSign.SIGNATURE_METHOD;
    reference.digestMethod.algorithm = XMLDSign.DIGEST_METHOD;
    reference.uri = "#" + id;
    reference.setDigestValue(digest);
    reference.transforms.add(new Transform(XMLDSign.CANONICALIZATION_METHOD));
  }

  public void addEnvelopedTransform() {
    reference.transforms.add(0, new Transform(XMLDSign.ENVELOPED_SIGNATURE));
  }

  public void removeEnvelopedTransform() {
    Iterator<Transform> iterator = reference.transforms.iterator();
    while (iterator.hasNext()) {
      Transform transform = iterator.next();
      if (XMLDSign.ENVELOPED_SIGNATURE.equals(transform.algorithm)) {
        iterator.remove();
      }
    }
  }

  /**
   * INNER CLASSES
   **/

  static class CanonicalizationMethod {
    @XmlAttribute(name = "Algorithm")
    public String algorithm;
  }

  static class SignatureMethod {
    @XmlAttribute(name = "Algorithm")
    public String algorithm;
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  static class Reference {
    @XmlAttribute(name = "URI")
    public String uri;

    @XmlElementWrapper(name = "Transforms", namespace = XMLDSign.XMLNS)
    @XmlElement(name = "Transform", namespace = XMLDSign.XMLNS)
    public List<Transform> transforms = new ArrayList<Transform>();

    @XmlElement(name = "DigestMethod", namespace = XMLDSign.XMLNS)
    public DigestMethod digestMethod = new DigestMethod();

    @XmlElement(name = "DigestValue", namespace = XMLDSign.XMLNS)
    private String digestValue = "";

    public byte[] getDigestValue() {
      return Base64.decodeBase64(digestValue);
    }

    public void setDigestValue(byte[] digestValue) {
      this.digestValue = Base64.encodeBase64String(digestValue);
    }
  }

  static class Transform {
    @XmlAttribute(name = "Algorithm")
    public String algorithm;

    public Transform() {
    }

    public Transform(String algorithm) {
      this.algorithm = algorithm;
    }
  }

  static class DigestMethod {
    @XmlAttribute(name = "Algorithm")
    public String algorithm;
  }
}
