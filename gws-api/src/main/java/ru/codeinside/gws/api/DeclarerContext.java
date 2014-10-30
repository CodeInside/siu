/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import java.util.Set;

/**
 * Контест заявителя для поставщика СМЭВ.
 */
public interface DeclarerContext {

    /**
     * Набор имён свойств.
     */
    Set<String> getPropertyNames();

    /**
     * Обязательно ли свойство.
     */
    boolean isRequired(String propertyName);

    /**
     * Является ли свойство вложением.
     */
    boolean isEnclosure(String propertyName);

    /**
     * Имя типа свойства.
     */
    String getType(String propertyName);

    /**
     * Задать значение свойства.
     */
    void setValue(String propertyName, Object value);

    /**
     * Добавление вложения.
     */
    void addEnclosure(String propertyName, Enclosure enclosure);

    /**
     * Создание заявки из текущего контекста.
     *
     * @return идентификатор заявки.
     */
    String declare();

    /**
     * Создание заявки из текущего контекста.
     *
     * @param tag       метка службы.
     * @param declarant имя заявителя.
     * @return идентификатор заявки.
     */
    String declare(String tag, String declarant);

    /**
     * Получить значение переменной (свойства или вложения).
     */
    Object getVariable(String name);
}
