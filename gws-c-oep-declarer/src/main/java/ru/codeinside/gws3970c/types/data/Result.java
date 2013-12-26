
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3970c.types.data;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Result", propOrder = {
  "dataRow",
  "params"
})
public class Result {

  @XmlElement(nillable = true)
  private List<DataRow> dataRow;
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

  public void setDataRow(List<DataRow> dataRow) {
    this.dataRow = dataRow;
  }
}
