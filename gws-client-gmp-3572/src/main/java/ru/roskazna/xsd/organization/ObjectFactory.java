
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.roskazna.xsd.organization;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ru.roskazna.xsd.organization package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ru.roskazna.xsd.organization
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Organization }
     * 
     */
    public Organization createOrganizationType() {
        return new Organization();
    }

    /**
     * Create an instance of {@link Bank }
     * 
     */
    public Bank createBankType() {
        return new Bank();
    }

    /**
     * Create an instance of {@link KFO }
     * 
     */
    public KFO createKFOType() {
        return new KFO();
    }

    /**
     * Create an instance of {@link Account }
     * 
     */
    public Account createAccountType() {
        return new Account();
    }

    /**
     * Create an instance of {@link Organization.Addresses }
     * 
     */
    public Organization.Addresses createOrganizationTypeAddresses() {
        return new Organization.Addresses();
    }

    /**
     * Create an instance of {@link Organization.Contacts }
     * 
     */
    public Organization.Contacts createOrganizationTypeContacts() {
        return new Organization.Contacts();
    }

}
