/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.liquibase.impl;

import liquibase.exception.LiquibaseException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.PrintStream;

public interface Migration {

    void drop(DbConfig config) throws LiquibaseException, IOException;

    void dropAndLoad(DbConfig config, String sqlScript) throws LiquibaseException, IOException;

    void createExperimental(String appVersion, DbConfig config) throws LiquibaseException;

    boolean validate(DbConfig a, DbConfig b, PrintStream out) throws LiquibaseException, IOException, ParserConfigurationException;

    boolean validate(String appVersion, DbConfig config, DbConfig expCfg, PrintStream out) throws LiquibaseException, IOException, ParserConfigurationException;
}
