/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.liquibase.impl;

import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.database.Database;
import liquibase.exception.LiquibaseException;

import java.util.List;

final class Changes {
    final private DatabaseChangeLog databaseChangeLog;
    final private List<ChangeSet> changes;

    Changes(DatabaseChangeLog databaseChangeLog, List<ChangeSet> changes) {
        this.databaseChangeLog = databaseChangeLog;
        this.changes = changes;
    }

    void apply(Database database) throws LiquibaseException {
        for (ChangeSet item : changes) {
            database.markChangeSetExecStatus(item, item.execute(databaseChangeLog, database));
        }
    }

}
