/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.migrations;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.gses.liquibase.impl.DbConfig;
import ru.codeinside.gses.liquibase.impl.LegacyMigrationService;
import ru.codeinside.gses.liquibase.impl.Migration;

import javax.naming.InitialContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@RunWith(Arquillian.class)
public class SchemaSandbox extends Assert {

    @Deployment
    public static JavaArchive createDeployment() {
        // можно притащить зависимость:
        //.addAsLibraries(DependencyResolvers
        //.use(MavenDependencyResolver.class)
        //        .artifact("com.example:utils:1.0.0-SNAPSHOT")
        //        .resolveAsFiles());

        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(Employee.class.getPackage())
                .addAsResource("META-INF/persistence.xml");
    }

    static void log(String txt) {
        System.err.println(txt);
    }

    static void dumpMetaModel(final Metamodel metamodel) {
        Set<String> entities = new TreeSet<String>();
        for (final EntityType<?> entity : metamodel.getEntities()) {
            entities.add(entity.getName());
        }
        log("entities " + entities);
        Set<String> types = new TreeSet<String>();
        for (ManagedType<?> type : metamodel.getManagedTypes()) {
            types.add(type.getJavaType().getName());
        }
        for (final EmbeddableType<?> type : metamodel.getEmbeddables()) {
            types.add(type.getJavaType().getName());
        }
        log("types " + types);
    }

    // можно запускать внутри одной сюиты
    private static void dropContent(DataSource ds) throws Exception {
        final UserTransaction tx = (UserTransaction) InitialContext.doLookup("UserTransaction");
        assertNotNull(tx);
        tx.begin();
        Migration assistance = new LegacyMigrationService().create();
        assistance.drop(new DbConfig(ds));
        tx.commit();
    }

    @Test
    public void autoGenerationToFiles() throws Exception {
        String appDir = "target";
        for (final String entry : Databases.UNITS) {
            String createName = "createDDL_" + entry + ".jdbc";
            String dropName = "dropDDL_" + entry + ".jdbc";
            final Map<String, String> props = new LinkedHashMap<String, String>();
            props.put("eclipselink.ddl-generation", "create-tables");
            props.put("eclipselink.ddl-generation.output-mode", "sql-script");
            props.put("eclipselink.logging.level", "SEVERE");
            props.put("eclipselink.application-location", appDir);
            props.put("eclipselink.create-ddl-jdbc-file-name", createName);
            props.put("eclipselink.drop-ddl-jdbc-file-name", dropName);
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(entry, props);
            emf.createEntityManager().close();
            emf.close();
        }

        //props.put("eclipselink.jdbc.native-sql", "true");
        //props.put("eclipselink.jdbc.exclusive-connection.mode", "Transactional");
        //props.put("eclipselink.jdbc.exclusive-connection.is-lazy", "true");
        //props.put("eclipselink.jdbc.connections.initial", "0");
        //props.put("eclipselink.jdbc.connections.min", "0");
        //props.put("eclipselink.jdbc.connections.max", "1");

        // До создания EMF можно изменить соединение:
        //props.put("javax.persistence.jdbc.url", "jdbc:h2:mem:adminka2;MODE=PostgreSQL;MVCC=true");
        //props.put("javax.persistence.jdbc.user", "sa");
        //props.put("javax.persistence.jdbc.password", "");
    }

    @Test
    public void autoGenerarionToDb() throws Exception {
        DataSource ds = InitialContext.doLookup("jdbc/adminka");

        if (null != System.getProperty("arquillian.launch")) {
            // заточено только под postgresql
            dropContent(ds);
        } else {
            assertTrue(introspectTables(ds, false).isEmpty());
        }

        final Map<String, String> props = new LinkedHashMap<String, String>();
        props.put("eclipselink.ddl-generation", "create-tables");
        props.put("eclipselink.ddl-generation.output-mode", "database");
        props.put("eclipselink.logging.level", "SEVERE");

        props.put("eclipselink.jdbc.native-sql", "true");
        //props.put("eclipselink.jdbc.exclusive-connection.mode", "Transactional");
        //props.put("eclipselink.jdbc.exclusive-connection.is-lazy", "true");
        //props.put("eclipselink.jdbc.connections.initial", "0");
        //props.put("eclipselink.jdbc.connections.min", "0");
        //props.put("eclipselink.jdbc.connections.max", "1");

        // До создания EMF можно изменить соединение:
        //props.put("javax.persistence.jdbc.url", "jdbc:h2:mem:adminka3;MODE=PostgreSQL;MVCC=true");
        //props.put("javax.persistence.jdbc.user", "sa");
        //props.put("javax.persistence.jdbc.password", "");


        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU", props);
        emf.createEntityManager().close();
        emf.close();

        Set<String> strings = introspectTables(ds, false);
        assertTrue(
                strings.contains("EMPLOYEE") ||
                        strings.contains("employee")
        );
    }

    private Set<String> introspectTables(DataSource ds, boolean dump) throws SQLException {
        final LinkedHashSet<String> result = new LinkedHashSet<String>();
        final Connection c = ds.getConnection();
        final DatabaseMetaData metaData = c.getMetaData();
        if (dump) {
            ResultSet tableTypes = metaData.getTableTypes();
            while (tableTypes.next()) {
                log("has type: " + tableTypes.getObject(1));
            }
            tableTypes.close();
        }
        if (dump) {
            ResultSet catalogs = metaData.getCatalogs();
            while (catalogs.next()) {
                log("catalog: " + catalogs.getObject(1));
            }
            catalogs.close();
        }
        ResultSet tables = metaData.getTables("", "", null, new String[]{"TABLE", "SEQUENCE"});
        final StringBuilder sb = new StringBuilder();
        while (tables.next()) {
            if (dump) {
                sb.delete(0, sb.length());
                sb.append("table ");
                for (int i = 1; i < tables.getMetaData().getColumnCount(); i++) {
                    if (i > 1) {
                        sb.append(',');
                    }
                    final Object o = tables.getObject(i);
                    if (o != null) {
                        sb.append(o);
                    }
                }
                log(sb.toString());
            }
            result.add(tables.getString(3));
        }
        tables.close();
        c.close();
        return result;
    }

}
