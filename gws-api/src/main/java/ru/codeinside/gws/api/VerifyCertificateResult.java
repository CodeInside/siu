/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;


/**
 * Результат проверки сертификата.
 */
final public class VerifyCertificateResult {

    private final int code;
    private final String description;

    public VerifyCertificateResult(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Код результата.
     */
    public int getCode() {
        return code;
    }

    /**
     * Описание деталей.
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "{" +
                "code=" + code +
                ", description='" + description + '\'' +
                '}';
    }
}
