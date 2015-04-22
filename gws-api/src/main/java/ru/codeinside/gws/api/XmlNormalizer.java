/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2015, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Апи сервиса каноникализации xml
 */
public interface XmlNormalizer {
    /**
     * @param source       данные для нормализации
     * @param outputStream исходящий поток результата
     */
    void normalize(InputStream source, OutputStream outputStream);
}
