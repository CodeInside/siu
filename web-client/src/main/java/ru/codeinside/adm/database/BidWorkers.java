/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Связка исполнителя с заявкой для упрощения запросов выборки.
 * Повторяет путь от UserTask к ProcessIntance и далее к Bid.
 */
@Entity
@IdClass(BidWorkersId.class)
public class BidWorkers implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "bid_id", referencedColumnName = "id")
    private Bid bid;

    @Id
    @ManyToOne
    @JoinColumn(name = "employee_login", referencedColumnName = "login")
    private Employee employee;


    public BidWorkers() {

    }

    public BidWorkers(Bid bid, Employee employee) {
        this.bid = bid;
        this.employee = employee;
    }

    public Long getBid() {
        return bid.getId();
    }

    public String getEmployee() {
        return employee.getLogin();
    }
}
