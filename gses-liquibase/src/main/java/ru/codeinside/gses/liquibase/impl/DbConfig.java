/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.liquibase.impl;

import javax.sql.DataSource;
import javax.sql.XADataSource;

final public class DbConfig {
    final public XADataSource xds;
    final public DataSource ds;

    public DbConfig(XADataSource ds) {
        if (ds == null) {
            throw new NullPointerException();
        }
        this.xds = ds;
        this.ds = null;
    }

    public DbConfig(DataSource ds) {
        if (ds == null) {
            throw new NullPointerException();
        }
        this.xds = null;
        this.ds = ds;
    }


}
