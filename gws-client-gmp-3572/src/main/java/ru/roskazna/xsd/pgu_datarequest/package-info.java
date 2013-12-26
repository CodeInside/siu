/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

//javax.xml.bind.annotation.XmlSchema(namespace = "http://roskazna.ru/xsd/PGU_DataRequest")
@javax.xml.bind.annotation.XmlSchema(

    namespace = "http://roskazna.ru/xsd/PGU_DataRequest",
    //elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED,
    xmlns = {
        @javax.xml.bind.annotation.XmlNs(prefix = "unifo",
            namespaceURI="http://rosrazna.ru/xsd/SmevUnifoService"),
        @javax.xml.bind.annotation.XmlNs(prefix = "pirq",
            namespaceURI="http://roskazna.ru/xsd/PGU_ImportRequest"),
        @javax.xml.bind.annotation.XmlNs(prefix = "pdrq",
            namespaceURI="http://roskazna.ru/xsd/PGU_DataRequest")
    })
package ru.roskazna.xsd.pgu_datarequest;
