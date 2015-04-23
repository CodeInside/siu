/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2015, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

public class WrappedAppData {
    /**
     * Данные обернутые в тег AppData
     */
    private final String wrappedAppData;

    /**
     * Подпись содержимого
     */
    private final Signature signature;

    public WrappedAppData(final String wrappedAppData, final Signature signature) {
        this.wrappedAppData = wrappedAppData;
        this.signature = signature;
    }

    public String getWrappedAppData() {
        return wrappedAppData;
    }

    public Signature getSignature() {
        return signature;
    }
}
