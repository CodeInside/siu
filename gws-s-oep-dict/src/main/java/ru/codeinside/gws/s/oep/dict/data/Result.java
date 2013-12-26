
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.s.oep.dict.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


@XmlRootElement(name = "result")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Result", propOrder = {
  "dataRow",
  "params"
})
public class Result {

  @XmlElement(nillable = true)
  protected List<DataRow> dataRow;
  protected SystemParams params;

  public List<DataRow> getDataRow() {
    if (dataRow == null) {
      dataRow = new ArrayList<DataRow>();
    }
    return this.dataRow;
  }

  public SystemParams getParams() {
    return params;
  }

  public void setParams(SystemParams value) {
    this.params = value;
  }

  public String getRowValue(String code) {
    if (dataRow != null && code != null) {
      for (final DataRow raw : dataRow) {
        if (code.equals(raw.getName())) {
          return raw.getValue();
        }
      }
    }
    return null;
  }

}
