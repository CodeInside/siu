/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.liquibase.types;

import liquibase.database.structure.type.DateTimeType;
import liquibase.database.typeconversion.core.PostgresTypeConverter;

public class JPAPostgresTypeConverter extends PostgresTypeConverter {

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public DateTimeType getDateTimeType() {
        return new DateTimeType("TIMESTAMP");
    }

}
