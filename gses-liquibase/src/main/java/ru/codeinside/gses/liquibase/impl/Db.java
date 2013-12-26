/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.liquibase.impl;

import liquibase.database.core.PostgresDatabase;
import liquibase.database.typeconversion.TypeConverter;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.exception.LockException;
import liquibase.lockservice.LockService;
import liquibase.servicelocator.ServiceLocator;
import ru.codeinside.gses.liquibase.impl.DbConfig;
import ru.codeinside.gses.liquibase.types.JPAPostgresTypeConverter;

import javax.sql.XAConnection;
import javax.sql.XADataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

final class Db extends PostgresDatabase {

    private DeferredTxJdbcConnection deferredTxJdbcConnection;
    private Connection connection;
    private XAConnection xaConnection;

    private Db(Connection connection, XAConnection xaConnection, boolean xa) throws DatabaseException {
        this.connection = connection;
        this.xaConnection = xaConnection;
        deferredTxJdbcConnection = new DeferredTxJdbcConnection(this.connection, xa);
        setConnection(deferredTxJdbcConnection);
    }

    static {
        final ServiceLocator serviceLocator = ServiceLocator.getInstance();
        final Class<JPAPostgresTypeConverter> jpaTypeConverter = JPAPostgresTypeConverter.class;
        serviceLocator.addPackageToScan(jpaTypeConverter.getPackage().getName());
        if (jpaTypeConverter != serviceLocator.findClass(TypeConverter.class)) {
            throw new IllegalStateException("ServiceLocator already cached type converters! Can not patch PostgreSQL types!");
        }
    }

    private static Db create(DbConfig config) throws DatabaseException {
        if (config.ds != null) {
            try {
                return new Db(config.ds.getConnection(), null, config.ds.isWrapperFor(XADataSource.class));
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }
        }
        XAConnection xaConnection = null;
        try {
            xaConnection = config.xds.getXAConnection();
            return new Db(xaConnection.getConnection(), xaConnection, true);
        } catch (SQLException e) {
            if (xaConnection != null) {
                try {
                    xaConnection.close();
                } catch (SQLException e2) {
                    Logger.getLogger(Db.class.getName()).log(Level.WARNING, "close", e2);
                }
            }
            throw new DatabaseException(e);
        }
    }

    static <T> T execute(DbConfig config, Query<T> query) throws LiquibaseException {
        Db db = create(config);
        try {
            return query.execute(db);
        } finally {
            db.close();
        }
    }

    void createTo(Changes changes) throws LiquibaseException {
        deferredTxJdbcConnection.setDeferredCommit(true);
        try {
            dropThenLoad(null);
            checkChangeLogTable();
            changes.apply(this);
        } finally {
            deferredTxJdbcConnection.setDeferredCommit(false);
        }
        deferredTxJdbcConnection.commit();
    }

    void migrateTo(Changes changes) throws LiquibaseException {
        deferredTxJdbcConnection.setDeferredCommit(true);
        try {
            changes.apply(this);
        } finally {
            deferredTxJdbcConnection.setDeferredCommit(false);
        }
        deferredTxJdbcConnection.commit();
    }

    void dropAndLoad(String sqlScriptFileName) throws DatabaseException {
        deferredTxJdbcConnection.setDeferredCommit(true);
        try {
            dropThenLoad(sqlScriptFileName);
        } finally {
            deferredTxJdbcConnection.setDeferredCommit(false);
        }
        //deferredTxJdbcConnection.commit();
    }

    private void dropThenLoad(String sqlScriptFileName) throws DatabaseException {
        try {
            List<String> lst = new ArrayList<String>(6);
            lst.add("pg_toast");
            lst.add("pg_temp_1");
            lst.add("pg_toast_temp_1");
            lst.add("pg_catalog");
            lst.add("information_schema");
            lst.add("public");

            final DatabaseMetaData metaData = connection.getMetaData();

            ResultSet resultSet = metaData.getSchemas();//connection.prepareStatement("SELECT schema_name FROM information_schema.schemata").executeQuery();
            while (resultSet.next()) {
                String schemaName = resultSet.getString(1);
                if(!lst.contains(schemaName)){
                    dropDatabaseObjects(schemaName);
                    connection.createStatement().execute("DROP SCHEMA IF EXISTS " + schemaName + " CASCADE");
                }
            }
            resultSet.close();
            dropDatabaseObjects("public");
            dropSpecialTables();
            if (sqlScriptFileName != null) {
                load(sqlScriptFileName);
            }
        } catch (DatabaseException e) {
            throw e;
        } catch (SQLException e) {
            SQLException n = e;
            while (n != null) {
                System.err.println(e.getMessage());
                n = n.getNextException();
            }
            System.err.println();
            throw new DatabaseException(e);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public void checkChangeLogTable() throws DatabaseException {
        checkDatabaseChangeLogTable(false, null, null);
        if (!LockService.getInstance(this).hasChangeLogLock()) {
            checkDatabaseChangeLogLockTable();
        }
    }

    private void dropSpecialTables() throws DatabaseException, SQLException {
        Statement statement = connection.createStatement();
        statement.execute("DROP TABLE IF EXISTS databasechangelog, databasechangeloglock");
    }

    private void load(String sqlScriptFileName) throws IOException, DatabaseException, SQLException {
        String script = readFileToString(new File(sqlScriptFileName), "UTF8");
        Statement statement = connection.createStatement();
        statement.execute(script);
        statement.close();
    }

    void releaseLock() throws LockException {
        begin();
        LockService.getInstance(this).releaseLock();
    }

    void waitForLock() throws LockException {
        begin();
        LockService.getInstance(this).waitForLock();
    }

    private void begin() throws LockException {
        deferredTxJdbcConnection.setDeferredCommit(false);
        try {
            deferredTxJdbcConnection.rollback();
        } catch (DatabaseException e) {
            throw new LockException(e);
        }
    }

    @Override
    public void close() throws DatabaseException {
        super.close();
        if (xaConnection != null) {
            try {
                xaConnection.close();
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }
        }

    }

    private String readFileToString(File file, String encoding) throws IOException {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            StringWriter sw = new StringWriter();
            InputStreamReader in2 = new InputStreamReader(in, encoding);
            char[] buffer = new char[1024];
            int n = 0;
            while (-1 != (n = in2.read(buffer))) {
                sw.write(buffer, 0, n);
            }
            return sw.toString();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ioe) {
                // ignore
            }
        }
    }

}
