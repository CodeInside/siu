/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import java.security.cert.X509Certificate;

/**
 * Результат локальной проверки ЭЦП в конверте сообщения.
 */
final public class VerifyResult {

    /**
     * Описание ошибки.
     */
    final public String error;

    /**
     * Сертификат ЭП-ОВ (технологическая подпись информационной системы)
     */
    final public X509Certificate actor;

    /**
     * Сертификат ЭП-СМЭВ (подпись шлюза Ростелеком, через который прошло сообщение).
     */
    final public X509Certificate recipient;

    public VerifyResult(final String error, final X509Certificate actor, final X509Certificate recipient) {
        this.error = error;
        this.actor = actor;
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "{" +
                "error='" + error + '\'' +
                ", actor=" + actor +
                ", recipient=" + recipient +
                '}';
    }
}
