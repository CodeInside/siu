/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import java.security.cert.X509Certificate;

/**
 * ЭЦП.
 */
final public class Signature {

    /**
     * Сертификат.
     */
    final public X509Certificate certificate;

    /**
     * Подписанное содержимое.
     */
    final public byte[] content;

    /**
     * Подпись.
     */
    final public byte[] sign;

    /**
     * Соответсвует ли содержимое подписи.
     */
    final public boolean valid;

    public Signature(final X509Certificate certificate, final byte[] content, final byte[] sign, final boolean valid) {
        this.certificate = certificate;
        this.content = content;
        this.sign = sign;
        this.valid = valid;
    }

    public Signature toInvalid() {
        return new Signature(this.certificate, this.content, this.sign, false);
    }

    @Override
    public String toString() {
        return "Signature{" +
                "certificate=" + certificate +
                ", content=" + content.length +
                ", sign=" + sign +
                ", valid=" + valid +
                '}';
    }
}
