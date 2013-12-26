/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.crypto.cryptopro;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.security.Signature;

public class Pkcs7Test extends Assert {

    @Test
    public void testDencoding() throws Exception {
        byte[] sig = IOUtils.toByteArray(R.getRequiredResourceStream("rr/req_cb8cff92-c788-46cf-a97f-f5c6f0170be5.xml.sig"));
        ru.codeinside.gws.api.Signature sig2 = SunPkcs7.fromPkcs7(sig);
        assertEquals("T=Генеральный директор, CN=Семенкин Максим Викторович, O=ООО КодИнсайд, " +
                "L=Пенза, ST=58 Пензенская область, C=RU, EMAILADDRESS=maxim.semenkin@gmail.ru, " +
                "OID.1.2.643.3.131.1.1=5837040135, OID.1.2.643.100.1=1095837000929", sig2.certificate.getSubjectDN().toString());

        byte[] content = IOUtils.toByteArray(R.getRequiredResourceStream("rr/req_cb8cff92-c788-46cf-a97f-f5c6f0170be5.xml"));

        assertTrue(R.provider.validate(sig2, null, content));

    }

    @Test
    public void testEncoding() throws Exception {
        CryptoProvider.loadCertificate();
        byte[] content = IOUtils.toByteArray(R.getRequiredResourceStream("rr/req_cb8cff92-c788-46cf-a97f-f5c6f0170be5.xml"));
        final Signature signature = Signature.getInstance("GOST3411withGOST3410EL");
        signature.initSign(CryptoProvider.privateKey);
        signature.update(content);
        byte[] sign = signature.sign();
        byte[] pkcs7 = SunPkcs7.toPkcs7(new ru.codeinside.gws.api.Signature(CryptoProvider.cert, null, sign, false));
        ru.codeinside.gws.api.Signature sig2 = SunPkcs7.fromPkcs7(pkcs7);
        assertEquals(CryptoProvider.cert, sig2.certificate);
        assertArrayEquals(sign, sig2.sign);

        assertTrue(R.provider.validate(sig2, null, content));

    }
}
