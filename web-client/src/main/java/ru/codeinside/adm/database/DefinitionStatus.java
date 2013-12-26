/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import java.util.ArrayList;
import java.util.List;


public enum DefinitionStatus {
  Work("Работает"),
  Done("Готово"),
  Debugging("Отладка"),
  Created("Создан"),
  PathToArchive("На пути в архив"),
  Archive("Архив");
  private String name;

  DefinitionStatus(String name) {
    this.name = name;
  }

  public String getLabelName() {
    return name;
  }

  public List<DefinitionStatus> getAvailableStatus() {
    List<DefinitionStatus> result = new ArrayList<DefinitionStatus>();
    switch (this) {
      case Created:
        result.add(Archive);
        result.add(Debugging);
        break;
      case Debugging:
        result.add(Archive);
        result.add(Done);
        break;
      case Done:
        result.add(Archive);
        result.add(Work);
        result.add(Debugging);
        break;
      case Work:
        //result.add(PathToArchive);
        result.add(Archive);
        break;
      case PathToArchive:
        result.add(Archive);
        break;
      default:
        break;
    }
    return result;
  }

  public static DefinitionStatus getStatusByLabelName(String labelName) {
    for (DefinitionStatus s : DefinitionStatus.values()) {
      if (s.getLabelName().equals(labelName)) {
        return s;
      }
    }
    return DefinitionStatus.Created;
  }

}