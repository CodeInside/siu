/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.liquibase.api;

import javax.sql.DataSource;

public interface MigrationService {

    /**
     * Выполнение миграции.
     * Вызывающий обязан запустить JTA транзакцию и при возникновении исключени откатить изменения.
     *
     * @param resource    Ресурс с описателем миграций.
     * @param classLoader Загрузчик относительно которого загружаются ресурсы.
     * @param appVersion  версия приложения, ДО которой исполяется миграция.
     * @param tmp         база для тестов миграции.
     * @param target      Целевая база данных.
     */
    void migrate(String resource, ClassLoader classLoader, String appVersion, DataSource tmp, DataSource target);
}
