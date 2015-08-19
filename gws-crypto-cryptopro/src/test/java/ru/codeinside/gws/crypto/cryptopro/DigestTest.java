/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2015, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.crypto.cryptopro;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestTest extends Assert {
    @Test
    public void test_digetst() throws NoSuchAlgorithmException {
        CryptoProvider cp = new CryptoProvider();
        byte[] data = new byte[] {1,2,3,4,5,6,7,8};
        MessageDigest md = MessageDigest.getInstance("GOST3411");
        md.digest(data);
        assertArrayEquals(md.digest(data), cp.digest(new ByteArrayInputStream(data)));
    }
}
