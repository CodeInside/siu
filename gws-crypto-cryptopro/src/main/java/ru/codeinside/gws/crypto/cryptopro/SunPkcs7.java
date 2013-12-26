/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.crypto.cryptopro;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.codeinside.gws.api.Signature;
import sun.security.pkcs.ContentInfo;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.ParsingException;
import sun.security.pkcs.SignerInfo;
import sun.security.util.DerOutputStream;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.AlgorithmId;
import sun.security.x509.X500Name;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.cert.X509Certificate;

final public class SunPkcs7 {

    final static ObjectIdentifier GOST3410;
    final static ObjectIdentifier GOST3411;

    static {
        try {
            GOST3410 = new ObjectIdentifier("1.2.643.2.2.19");
            GOST3411 = new ObjectIdentifier("1.2.643.2.2.9");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    final private static Log log = LogFactory.getLog(CryptoProvider.class);

    public static byte[] toPkcs7(final Signature signature) {
        final X509Certificate certificate = signature.certificate;
        final byte[] sign = signature.sign;
        X500Name issuer = X500Name.asX500Name(certificate.getIssuerX500Principal());
        final AlgorithmId digestAlgorithmId = new AlgorithmId(GOST3411);
        final AlgorithmId signAlgorithmId = new AlgorithmId(GOST3410);
        SignerInfo sInfo = new SignerInfo(issuer, certificate.getSerialNumber(), digestAlgorithmId, signAlgorithmId, sign);
        ContentInfo cInfo = new ContentInfo(ContentInfo.DATA_OID, null);
        PKCS7 pkcs7 = new PKCS7(
                new AlgorithmId[]{digestAlgorithmId},
                cInfo,
                new X509Certificate[]{certificate},
                new SignerInfo[]{sInfo});
        final ByteArrayOutputStream bOut = new DerOutputStream();
        try {
            pkcs7.encodeSignedData(bOut);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bOut.toByteArray();
    }

    public static Signature fromPkcs7(final byte[] bytes) {
        final PKCS7 pkcs7;
        try {
            pkcs7 = new PKCS7(bytes);
        } catch (ParsingException e) {
            log.info("fail parse pkcs7: ", e);
            return new Signature(null, null, null, false);
        }

        final AlgorithmId digestAlgorithmId = new AlgorithmId(GOST3411);
        final AlgorithmId signAlgorithmId = new AlgorithmId(GOST3410);
        final AlgorithmId[] digestAlgorithmIds = pkcs7.getDigestAlgorithmIds();

        if (digestAlgorithmIds == null || digestAlgorithmIds.length == 0) {
            log.info("no digestAlgorithm in pkcs7");
        } else if (!digestAlgorithmIds[0].equals(digestAlgorithmId)) {
            log.info("no GOST3411 in pkcs7");
        } else {
            final X509Certificate[] certificates = pkcs7.getCertificates();
            if (certificates == null || certificates.length == 0) {
                log.info("no certificate in pkcs7");
            } else {
                final X509Certificate certificate = certificates[0];
                final SignerInfo[] signerInfos = pkcs7.getSignerInfos();
                if (signerInfos == null || signerInfos.length == 0) {
                    log.info("no signerInfos in pkcs7");
                } else {
                    final SignerInfo signerInfo = signerInfos[0];
                    if (!signerInfo.getIssuerName().equals(X500Name.asX500Name(certificate.getIssuerX500Principal()))) {
                        log.info("invalid issuerX500Principal in pkcs7");
                    } else if (!signerInfo.getDigestAlgorithmId().equals(digestAlgorithmId)) {
                        log.info("no GOST3411 in pkcs7");
                    } else if (!signerInfo.getDigestEncryptionAlgorithmId().equals(signAlgorithmId)) {
                        log.info("no GOST3410 in pkcs7");
                    } else if (!signerInfo.getCertificateSerialNumber().equals(certificate.getSerialNumber())) {
                        log.info("invalid certificate serial number in pkcs7");
                    } else {
                        return new Signature(certificate, null, signerInfo.getEncryptedDigest(), true);
                    }
                }
            }
        }
        return new Signature(null, null, null, false);
    }

}
