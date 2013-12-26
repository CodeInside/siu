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
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.codeinside.adm.database.Employee;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceProperty;
import javax.transaction.UserTransaction;

/**
 * "Не будет проходить так EMF уже создан, и для генерации схемы уже поздно")
 */
@RunWith(Arquillian.class)
public class OverrideNotWork extends Assert {

    @PersistenceContext(
            unitName = "myPU",
            properties = {
                    // свойства именно для EM а не EMF
                    @PersistenceProperty(name = "eclipselink.ddl-generation", value = "create-tables"),
                    @PersistenceProperty(name = "eclipselink.ddl-generation.output-mode", value = "database")

            })
    EntityManager em;
    @Inject
    UserTransaction tx;

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)//
                .addPackage(Employee.class.getPackage())//
                .addAsResource("META-INF/persistence.xml")//
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void model() throws Exception {
        tx.begin();
        Employee x = new Employee();
        x.setLogin("x");
        em.persist(x);
        x = em.find(Employee.class, "x");
        assertNotNull(x);
        tx.commit();
    }

}
