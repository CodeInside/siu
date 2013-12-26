/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.liquibase.impl;

import liquibase.database.Database;
import liquibase.diff.Diff;
import liquibase.diff.DiffResult;
import liquibase.exception.LiquibaseException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.PrintStream;

final class Assistance implements Migration {

    private final String changeLog;
    private final ClassLoader classLoader;
    private Source lazySource;

    Assistance(String changeLog, ClassLoader classLoader) {
        this.changeLog = changeLog;
        this.classLoader = classLoader;
    }

    private boolean isEquals(Database from, Database to, PrintStream out) throws LiquibaseException {
        return isEquals(from, to, "Changes for migrate from " + from.getConnection().getCatalog() + " to " + to.getConnection().getCatalog(), out);
    }

    static boolean isEquals(Database from, Database to, String message, PrintStream out) throws LiquibaseException {
        DiffResult diffResult = new Diff(to, from).compare();
        try {
            if (diffResult.differencesFound()) {
                diffResult.printResult(out);
                out.println();
                out.println(message);
                diffResult.printChangeLog(out, to);
                return false;
            }
            return true;
        } catch (IOException e) {
            throw new LiquibaseException(e);
        } catch (ParserConfigurationException e) {
            throw new LiquibaseException(e);
        }
    }

    private Source getSource() throws LiquibaseException {
        if (lazySource == null) {
            if (changeLog == null) {
                throw new IllegalStateException("no changeLog");
            }
            lazySource = Source.create(changeLog, classLoader);
        }
        return lazySource;
    }

    @Override
    public void drop(DbConfig config) throws LiquibaseException, IOException {
        dropAndLoad(config, null);
    }

    @Override
    public void dropAndLoad(DbConfig config, final String sqlScript) throws LiquibaseException, IOException {
        Db.execute(config, new Query<Void>() {
            public Void execute(Db db) throws LiquibaseException {
                db.dropAndLoad(sqlScript);
                return null;
            }
        });
    }

    @Override
    public void createExperimental(String appVersion, DbConfig config) throws LiquibaseException {
        Source source = getSource();
        final Changes changes = source.getChanges(null, source.getVersion(appVersion));
        Db.execute(config, new Query<Void>() {
            public Void execute(Db db) throws LiquibaseException {
                db.createTo(changes);
                return null;
            }
        });
    }

    @Override
    public boolean validate(DbConfig a, final DbConfig b, final PrintStream out) throws LiquibaseException, IOException, ParserConfigurationException {
        return Db.execute(a, new Query<Boolean>() {
            public Boolean execute(final Db db1) throws LiquibaseException {
                return Db.execute(b, new Query<Boolean>() {
                    public Boolean execute(Db db2) throws LiquibaseException {
                        return isEquals(db1, db2, out);
                    }
                });
            }
        });
    }

    @Override
    public boolean validate(String appVersion, final DbConfig config, DbConfig expCfg, final PrintStream out) throws LiquibaseException, IOException, ParserConfigurationException {
        final Source source = getSource();
        final Changes changes = source.getChanges(null, source.getVersion(appVersion));
        return Db.execute(expCfg, new Query<Boolean>() {
            public Boolean execute(final Db experimental) throws LiquibaseException {
                experimental.createTo(changes);
                return Db.execute(config, new Query<Boolean>() {
                    public Boolean execute(Db hibernate) throws LiquibaseException {
                        return isEquals(experimental, hibernate, out);
                    }
                });
            }
        });
    }
}

