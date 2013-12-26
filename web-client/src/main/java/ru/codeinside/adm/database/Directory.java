/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Map;

@Entity
public class Directory implements Serializable {

    private static final long serialVersionUID = 15348965278936L;

    @Id
    private String name;

    @ElementCollection
    private Map<String, String> values;

    public Directory() {}

    public Directory(String name) {
        this.name = name;
    }

    public Map<String, String> getValues(){
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }
}
