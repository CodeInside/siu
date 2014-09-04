/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.c.oep.dict.app;

import ru.codeinside.gws.c.oep.dict.data.Result;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "AppData")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AppData", propOrder = {"result"})
public class AppData {

  @XmlElement(namespace = "http://oep-penza.ru/com/oep", required = true)
  protected List<Result> result;

  public List<Result> getResult() {
    if (result == null) {
      result = new ArrayList<Result>();
    }
    return this.result;
  }

}
