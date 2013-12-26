/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import java.io.Serializable;

public class BidWorkersId implements Serializable {

  private static final long serialVersionUID = 1L;

  Long bid;

  String employee;

  public Long getBid() {
    return bid;
  }

  public void setBid(Long bid) {
    this.bid = bid;
  }

  public String getEmployee() {
    return employee;
  }

  public void setEmployee(String employee) {
    this.employee = employee;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BidWorkersId that = (BidWorkersId) o;
    if (bid != null ? !bid.equals(that.bid) : that.bid != null) return false;
    if (employee != null ? !employee.equals(that.employee) : that.employee != null) return false;
    return true;
  }

  @Override
  public int hashCode() {
    int result = bid != null ? bid.hashCode() : 0;
    result = 31 * result + (employee != null ? employee.hashCode() : 0);
    return result;
  }
}
