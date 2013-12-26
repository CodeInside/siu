/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.osgi;

import com.google.common.base.Objects;
import org.osgi.framework.BundleException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.codeinside.gses.liquibase.api.MigrationService;
import ru.codeinside.gses.migrations.Databases;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.XADataSource;
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

final public class Renovation {

    final Logger logger = Logger.getLogger(getClass().getName());

    public void validateResources() throws BundleException {
        try {
            final Set<String> catalogs = new HashSet<String>();
            String tmpCatalog = getCatalog(Databases.TMP);

            for (final String dbName : Databases.ALL) {
                String catalog = getCatalog(dbName);
                catalogs.add(catalog);
                if (catalogs.size() > 1) {
                    final String msg = dbName + " use other catalog!";
                    throw new BundleException(msg, BundleException.ACTIVATOR_ERROR);
                }
                if(Objects.equal(tmpCatalog, catalog)){
                    final String msg = dbName + " and "+ Databases.TMP + " use one catalog!";
                    throw new BundleException(msg, BundleException.ACTIVATOR_ERROR);
                }
            }
        } catch (NamingException e) {
            final Throwable cause = e.getCause();
            final String msg = cause == null ? e.getMessage() : cause.getMessage();
            throw new BundleException(msg, BundleException.ACTIVATOR_ERROR, e);
        } catch (SQLException e) {
            throw new BundleException("Database fail", BundleException.ACTIVATOR_ERROR, e);
        }
    }

    private String getCatalog(String dbName) throws NamingException, SQLException, BundleException {
        final DataSource dataSource = InitialContext.doLookup(dbName);
        try {
            if (!dataSource.isWrapperFor(XADataSource.class)) {
                throw new BundleException(dbName + " is not XADataSource!", BundleException.ACTIVATOR_ERROR);
            }
        } catch (SQLFeatureNotSupportedException e) {
            throw new BundleException(dbName + " is not XADataSource!", BundleException.ACTIVATOR_ERROR);
        }
        final Connection connection = dataSource.getConnection();
        try {
            return connection.getCatalog();
        } finally {
            connection.close();
        }
    }

    public void validatePersistence() throws BundleException {
        final InputStream is = getClass().getClassLoader().getResourceAsStream("META-INF/persistence.xml");
        if (is == null) {
            throw new BundleException("Missed META-INF/persistence.xml", BundleException.ACTIVATOR_ERROR);
        }
        try {
            final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            final Document doc = builder.parse(is);
            final NodeList units = doc.getElementsByTagName("persistence-unit");
            for (int i = 0; i < units.getLength(); i++) {
                final Element unit = (Element) units.item(i);
                if (!"JTA".equals(unit.getAttribute("transaction-type"))) {
                    final String msg = "JPA unit " + unit.getAttribute("name") + " without JTA";
                    throw new BundleException(msg, BundleException.ACTIVATOR_ERROR);
                }
                final NodeList propertiesList = unit.getElementsByTagName("properties");
                for (int j = 0; j < propertiesList.getLength(); j++) {
                    final Element properties = (Element) propertiesList.item(j);
                    final NodeList propertyList = properties.getElementsByTagName("property");
                    for (int k = 0; k < propertyList.getLength(); k++) {
                        final Element property = (Element) propertyList.item(k);
                        if ("eclipselink.ddl-generation".equals(property.getAttribute("name"))) {
                            if (!"none".equals(property.getAttribute("value"))) {
                                final String msg = "JPA unit " + unit.getAttribute("name") + " with DDL generation!";
                                throw new BundleException(msg, BundleException.ACTIVATOR_ERROR);
                            }
                        }
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            throw new BundleException("DOM Parser error", BundleException.ACTIVATOR_ERROR, e);
        } catch (SAXException e) {
            throw new BundleException("SAX error", BundleException.ACTIVATOR_ERROR, e);
        } catch (IOException e) {
            throw new BundleException("IO error", BundleException.ACTIVATOR_ERROR, e);
        }
    }

    //TODO: переместить в тест
    public void renovate(final MigrationService migrationService) throws Exception {
        final UserTransaction tx = InitialContext.doLookup("UserTransaction");
        logger.info("tx status : " + tx.getStatus());
        if (tx.getStatus() != Status.STATUS_NO_TRANSACTION) {
            logger.info("Rollback transaction " + tx);
            tx.rollback();
        }
        tx.begin();
        try {
            renovateUnderTx(migrationService);
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
        tx.commit();
    }

    public void renovateUnderTx(MigrationService migrationService) throws NamingException {
        final DataSource tmp = InitialContext.doLookup(Databases.TMP);
        for (final Map.Entry<String, String> entry : Databases.VERSIONS.entrySet()) {
            final String jndiName = entry.getKey();
            final String version = entry.getValue();
            final String simpleName = jndiName.substring(jndiName.indexOf('/') + 1);
            logger.info("Migrate " + simpleName + " to " + version);
            final DataSource ds = InitialContext.doLookup(jndiName);
            final String changeLog = "migrations/" + simpleName + "/migration.xml";
            migrationService.migrate(changeLog, getClass().getClassLoader(), version, tmp, ds);
        }
    }
}
