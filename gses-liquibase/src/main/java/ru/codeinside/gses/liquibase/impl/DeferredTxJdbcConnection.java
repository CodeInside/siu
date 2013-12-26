/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.liquibase.impl;

import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;

import java.sql.Connection;

final class DeferredTxJdbcConnection extends JdbcConnection {

    private final boolean xa;
    private boolean deferredCommit;

    public DeferredTxJdbcConnection(Connection connection, boolean xa) throws DatabaseException {
        super(connection);
        this.xa = xa;
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws DatabaseException {
        if (!deferredCommit) {
            if (!xa) {
                super.setAutoCommit(autoCommit);
            }
        }
    }

    @Override
    public void commit() throws DatabaseException {
        if (!deferredCommit) {
            if (!xa) {
                super.commit();
            }
        }
    }

    @Override
    public void rollback() throws DatabaseException {
        if (!deferredCommit) {
            if (!xa) {
                super.rollback();
            }
        }
    }

    @Override
    public void close() throws DatabaseException {
        if (xa) {
            final boolean flag = deferredCommit;
            deferredCommit = true;
            super.close();
            deferredCommit = flag;
        } else {
            super.close();
        }
    }

    public void setDeferredCommit(boolean deferredCommit) {
        this.deferredCommit = deferredCommit;
    }
}
