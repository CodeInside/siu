/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.liquibase.impl;

import liquibase.exception.LiquibaseException;
import ru.codeinside.gses.liquibase.api.MigrationService;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Logger;

final public class MigrationServiceImpl implements MigrationService {

    final Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public void migrate(final String changeLog, final ClassLoader classLoader,
                        final String toVersion, final DataSource experimentalDs, final DataSource targetDs) {
        final DbConfig experimentalConfig = new DbConfig(experimentalDs);
        final DbConfig targetConfig = new DbConfig(targetDs);
        try {
            final Source source = Source.create(changeLog, classLoader);
            Db.execute(experimentalConfig, new Query<Void>() {
                public Void execute(final Db experimental) throws LiquibaseException {
                    Db.execute(targetConfig, new Query<Void>() {
                        public Void execute(Db targetDb) throws LiquibaseException {
                            targetDb.waitForLock();
                            try {
                                final String fromVersion = source.getVersion(targetDb, toVersion);
                                if (fromVersion != null) {
                                    logger.fine("From version " + fromVersion);
                                    experimental.createTo(source.getChanges(null, fromVersion));
                                } else {
                                    experimental.dropAndLoad(null);
                                }
                                final File baseTmpDir = new File(System.getProperty("java.io.tmpdir"));
                                final File file = File.createTempFile("migration", ".diff", baseTmpDir);
                                final PrintStream printStream = new PrintStream(file, "UTF8");
                                try {
                                    final String message = "Changes in changelog, that missed in version " + fromVersion;
                                    if (!Assistance.isEquals(targetDb, experimental, message, printStream)) {
                                        throw new IllegalStateException(
                                                "Detected invalid version " + fromVersion +
                                                        " for " + changeLog +
                                                        ", details in " + file);
                                    }
                                } finally {
                                    printStream.close();
                                }
                                file.delete();
                                logger.fine("To version " + toVersion);
                                targetDb.migrateTo(source.getChanges(fromVersion, toVersion));
                                return null;
                            } catch (IOException e) {
                                throw new LiquibaseException("io error", e);
                            } finally {
                                targetDb.releaseLock();
                            }
                        }
                    });
                    return null;
                }
            });
        } catch (LiquibaseException e) {
            throw new IllegalStateException(e);
        }
    }
}
