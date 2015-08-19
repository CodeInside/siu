/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2015, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.gses.webui.form;

import ru.codeinside.gws.api.Enclosure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SignData {
    private final byte[] data;  // данные для получения СП в бинарном виде
    private final List<Enclosure> enclosures;  // вложения сформированные сервисом потребителя или поставщика

    public SignData(final byte[] data, final List<Enclosure> enclosures) {
        this.data = data;
        this.enclosures = enclosures;
    }

    public SignData(final byte[] data, final Enclosure[] enclosures) {
        this(data, enclosures != null ? Arrays.asList(enclosures) : null);
    }

    public byte[] getData() {
        return data;
    }

    public List<Enclosure> getEnclosures() {
        if (enclosures == null) {
            return Collections.emptyList();
        }
        return enclosures;
    }
}
