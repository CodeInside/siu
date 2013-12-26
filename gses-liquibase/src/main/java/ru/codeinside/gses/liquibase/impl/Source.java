/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.liquibase.impl;

import liquibase.changelog.ChangeLogParameters;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.changelog.RanChangeSet;
import liquibase.database.Database;
import liquibase.exception.LiquibaseException;
import liquibase.parser.ChangeLogParser;
import liquibase.parser.ChangeLogParserFactory;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

final class Source {

    final DatabaseChangeLog databaseChangeLog;

    private Source(DatabaseChangeLog changeLog) {
        this.databaseChangeLog = changeLog;
    }

    public static Source create(String changeLog, ClassLoader classLoader) throws LiquibaseException {
        final ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(changeLog, classLoader);
        final ChangeLogParser parser = ChangeLogParserFactory.getInstance().getParser(changeLog, resourceAccessor);
        final ChangeLogParameters changeLogParameters = new ChangeLogParameters();
        return new Source(parser.parse(changeLog, changeLogParameters, resourceAccessor));
    }

    private static String asString(RanChangeSet change) {
        return change.getId() + ":" + change.getAuthor() + ":" + change.getChangeLog() + ":(" + change.getLastCheckSum() + ")";
    }

    public Changes getChanges(String from, String to) throws LiquibaseException {
        final Iterator<ChangeSet> i = getFromIterator(from);
        final LinkedList<ChangeSet> changes = new LinkedList<ChangeSet>();
        while (i.hasNext()) {
            final ChangeSet item = i.next();
            changes.add(item);
            if (item.getId().equals(to)) {
                return new Changes(databaseChangeLog, changes);
            }
        }
        if (to != null && !to.equals(from)) {
            throw new LiquibaseException("Can't found TO version " + to);
        }
        return new Changes(databaseChangeLog, changes);
    }

    public String getVersion(final String appVersion) {
        final String fullVersion = trimToNull(appVersion);
        if (fullVersion == null) {
            throw new IllegalStateException("Can't detect App version");
        }
        int delimeter = fullVersion.indexOf('_');
        if (delimeter < 0) {
            return fullVersion;
        }
        if (delimeter == 0) {
            throw new IllegalStateException("App version not in format dbVersion_codeVersion");
        }
        String dbVersion = trimToNull(fullVersion.substring(0, delimeter));
        if (dbVersion == null) {
            throw new IllegalStateException("Invalid db version in '" + fullVersion + "'");
        }
        if (indexOf(dbVersion) < 0) {
            throw new IllegalStateException("Invalid version " + dbVersion);
        }
        return dbVersion;
    }

    private int indexOf(final String id) {
        int index = 0;
        for (ChangeSet changeSet : databaseChangeLog.getChangeSets()) {
            if (changeSet.getId().equals(id)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public String getVersion(Database targetDb, String upperVersion) throws LiquibaseException {
        targetDb.checkDatabaseChangeLogTable(false, databaseChangeLog, null);
        databaseChangeLog.validate(targetDb, null);

        final List<ChangeSet> src = databaseChangeLog.getChangeSets();
        final List<RanChangeSet> dst = targetDb.getRanChangeSetList();
        Iterator<ChangeSet> srcIter = src.iterator();
        Iterator<RanChangeSet> dstIter = dst.iterator();
        boolean hasUpperVersion = false;
        String lastVersion = null;
        while (srcIter.hasNext() && dstIter.hasNext()) {
            ChangeSet required = srcIter.next();
            RanChangeSet actual = dstIter.next();
            if (!actual.getId().equalsIgnoreCase(required.getId())
                    || !actual.getAuthor().equalsIgnoreCase(required.getAuthor())
                    || !actual.getChangeLog().equalsIgnoreCase(required.getFilePath())) {
                throw new LiquibaseException("Required " + required + " != " + asString(actual));
            }
            if (hasUpperVersion) {
                throw new LiquibaseException("To " + upperVersion + " < " + asString(actual));
            }
            if (actual.getId().equalsIgnoreCase(upperVersion)) {
                hasUpperVersion = true;
            }
            lastVersion = required.getId();
        }
        if (dstIter.hasNext()) {
            RanChangeSet actual = dstIter.next();
            throw new LiquibaseException("To " + upperVersion + " < " + asString(actual));
        }
        return lastVersion;
    }

    private Iterator<ChangeSet> getFromIterator(String from) throws LiquibaseException {
        final Iterator<ChangeSet> i = databaseChangeLog.getChangeSets().iterator();
        if (from != null) {
            while (i.hasNext()) {
                ChangeSet changes = i.next();
                if (changes.getId().equals(from)) {
                    return i;
                }
            }
            throw new LiquibaseException("Can't found FROM version " + from);
        }
        return i;
    }

    private String trimToNull(final String str) {
        if (str == null) {
            return null;
        }
        final String trimmed = str.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        return trimmed;
    }

}
