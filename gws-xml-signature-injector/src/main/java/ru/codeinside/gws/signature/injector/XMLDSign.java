/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.signature.injector;

import org.apache.commons.codec.binary.Base64;
import ru.codeinside.gws.api.Signature;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Signature", namespace = XMLDSign.XMLNS)
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLDSign {
    public static final String XMLNS = "http://www.w3.org/2000/09/xmldsig#";
    public static final String DIGEST_METHOD = "http://www.w3.org/2001/04/xmldsig-more#gostr3411";
    public static final String SIGNATURE_METHOD = "http://www.w3.org/2001/04/xmldsig-more#gostr34102001-gostr3411";
    public static final String CANONICALIZATION_METHOD = "http://www.w3.org/2001/10/xml-exc-c14n#";
    public static final String ENVELOPED_SIGNATURE = "http://www.w3.org/2000/09/xmldsig#enveloped-signature";

    @XmlElement(name = "SignedInfo", namespace = XMLDSign.XMLNS)
    private SignedInfo signedInfo;

    @XmlElement(name = "SignatureValue", namespace = XMLNS)
    private String signatureValue;

    private byte[] getSignatureValue() {
        return Base64.decodeBase64(signatureValue);
    }
    private void setSignatureValue(byte[] signatureValue) {
        this.signatureValue = Base64.encodeBase64String(signatureValue);
    }

    public XMLDSign() {

    }

    public XMLDSign(Signature signature, String id) {
        signedInfo = new SignedInfo(signature.getDigest(), id);
        setSignatureValue(signature.sign);
    }

    public void setEnveloped(boolean enveloped) {
        if (enveloped) {
            signedInfo.addEnvelopedTransform();
        } else {
            signedInfo.removeEnvelopedTransform();
        }
    }

//    public XmlSignature getXmlSignature() {
//        if (!SIGNATURE_METHOD.equals(signedInfo.signatureMethod.algorithm)) {
//            throw new RuntimeException("Unexpected signature method: " + signedInfo.signatureMethod.algorithm);
//        }
//        if (!DIGEST_METHOD.equals(signedInfo.reference.digestMethod.algorithm)) {
//            throw new RuntimeException("Unexpected digest method: " + signedInfo.reference.digestMethod.algorithm);
//        }
//        XmlSignature xmlSignature = new XmlSignature();
//        xmlSignature.elementReference = signedInfo.reference.uri;
//        xmlSignature.certificate = keyInfo.x509Data.getCertificate();
//        xmlSignature.digest = signedInfo.reference.getDigestValue();
//        xmlSignature.signature = getSignatureValue();
//        return xmlSignature;
//    }


//    /** INNER CLASSES **/
//
//    @XmlAccessorType(XmlAccessType.FIELD)
//    static class X509Data {
//        @XmlElement(name = "X509Certificate", namespace = XMLNS)
//        private String x509Certificate = "";
//        @XmlElement(name = "X509SubjectName", namespace = XMLNS)
//        private String x509SubjectName = "";
//
//        public void setSubjectName(String subjectName) {
//            this.x509SubjectName = subjectName;
//        }
//
//        public void setCertificate(X509Certificate certificate) {
//            DerOutputStream derEncoder;
//            try {
//                derEncoder = new DerOutputStream();
//                derEncoder.write(certificate.getEncoded());
//            } catch (CertificateEncodingException e) {
//                throw new RuntimeException(e);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            this.x509Certificate = Base64.encodeBase64String(derEncoder.toByteArray());
//        }
//
//        public X509Certificate getCertificate() {
//            try {
//                byte[] derBytes = Base64.decodeBase64(x509Certificate);
//                ByteArrayInputStream is = new ByteArrayInputStream(derBytes);
//                return (X509Certificate) CertificateFactory.getInstance("X509").generateCertificate(is);
//            } catch (CertificateException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
}
