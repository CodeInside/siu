/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core.enclosure;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "AppliedDocuments")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AppliedDocumentsType", propOrder = {
        "appliedDocument"
})
public class AppliedDocumentsType {

    @XmlElement(name = "AppliedDocument")
    protected List<AppliedDocumentType> appliedDocument;

    public List<AppliedDocumentType> getAppliedDocument() {
        if (appliedDocument == null) {
            appliedDocument = new ArrayList<AppliedDocumentType>();
        }
        return this.appliedDocument;
    }

}
