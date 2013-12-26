/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.liquibase.impl;

import liquibase.servicelocator.ServiceLocator;

final public class LegacyMigrationService {

    public Migration create(String resource, ClassLoader classLoader) {
        if (ServiceLocator.getInstance() == null) {
            ServiceLocator.initialize(null);
        }
        return new Assistance(resource, classLoader);
    }

    public Migration create() {
        return create(null, null);
    }

}
