/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.types;


import ru.codeinside.gses.activiti.forms.api.definitions.VariableType;
import ru.codeinside.gses.activiti.ftarchive.AttachmentFFT;
import ru.codeinside.gses.activiti.ftarchive.BooleanFFT;
import ru.codeinside.gses.activiti.ftarchive.DateFFT;
import ru.codeinside.gses.activiti.ftarchive.DirectoryFFT;
import ru.codeinside.gses.activiti.ftarchive.EnclosureFFT;
import ru.codeinside.gses.activiti.ftarchive.EnumFFT;
import ru.codeinside.gses.activiti.ftarchive.JsonFFT;
import ru.codeinside.gses.activiti.ftarchive.LongFFT;
import ru.codeinside.gses.activiti.ftarchive.MaskedFFT;
import ru.codeinside.gses.activiti.ftarchive.MultilineFFT;
import ru.codeinside.gses.activiti.ftarchive.RequestFFT;
import ru.codeinside.gses.activiti.ftarchive.StringFFT;

public enum FieldTypes {
  STRING(StringType.class, new StringFFT()),
  BOOLEAN(BooleanType.class, new BooleanFFT()),
  DATE(DateType.class, new DateFFT()),
  LONG(LongType.class, new LongFFT()),
  ENUM(EnumType.class, new EnumFFT()),
  MASKED(MaskedType.class, new MaskedFFT()),
  DICTIONARY(DictionaryType.class, new DirectoryFFT()),
  ATTACHMENT(AttachmentType.class, new AttachmentFFT()),
  ENCLOSURE(EnclosureType.class, new EnclosureFFT()),
  JSON(JsonType.class, new JsonFFT()),
  MULTILINE(MultilineType.class, new MultilineFFT()),
  REQUEST(RequestType.class, new RequestFFT());

  public final Class<? extends VariableType> varType;
  public final FieldType fieldType;

  <T> FieldTypes(Class<? extends VariableType<T>> varType, FieldType<T> type) {
    this.varType = varType;
    this.fieldType = type;
  }

  public static FieldType getType(VariableType varType) {
    for (FieldTypes ft : values()) {
      if (ft.varType == varType.getClass()) {
        return ft.fieldType;
      }
    }
    throw new IllegalStateException("Тип " + varType + " не поддерживается");
  }
}
