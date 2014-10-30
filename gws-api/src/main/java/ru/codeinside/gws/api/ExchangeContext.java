/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import java.util.Set;

/**
 * Контекст потребителя для обработки внутри процесса исполнения.
 */
public interface ExchangeContext {

    /**
     * Получить локальный объект.
     */
    Object getLocal();

    /**
     * Установить локальный объект. Существует лишь между запросом и отвветом.
     */
    void setLocal(Object value);

    /**
     * Получить имена переменных из процесса исполнения.
     */
    Set<String> getVariableNames();

    /**
     * Получить значение переменной.
     */
    Object getVariable(String name);

    /**
     * Ассоциирована ли переменная с вложением.
     */
    boolean isEnclosure(String name);

    /**
     * Сохранить значение переменной в процесс исполнения.
     */
    void setVariable(String name, Object value);

    /**
     * Получить вложение по имени ассоциированой переменной.
     */
    Enclosure getEnclosure(String name);

    /**
     * Добавить вложение и содать ассоциацию с именем переменной.
     */
    void addEnclosure(String name, Enclosure enclosure);
}
