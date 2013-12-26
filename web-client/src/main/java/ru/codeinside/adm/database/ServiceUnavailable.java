/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class ServiceUnavailable implements Serializable {

    private static final long serialVersionUID = 452387671L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private InfoSystemService infoSystemService;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();

    private String name; //  Похоже на дублирование

    private String address;// Похоже на дублирование

    public ServiceUnavailable() {
    }

    public ServiceUnavailable(InfoSystemService service) {
        infoSystemService = service;
        name = service.getName();
        address = service.getAddress();
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public InfoSystemService getInfoSystemService() {
        return infoSystemService;
    }

    public void setInfoSystemService(InfoSystemService infoSystemService) {
        this.infoSystemService = infoSystemService;
    }
}
