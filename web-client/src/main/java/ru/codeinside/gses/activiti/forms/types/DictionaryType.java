/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.types;

import ru.codeinside.adm.database.FieldBuffer;
import ru.codeinside.gses.activiti.forms.api.definitions.VariableType;
import ru.codeinside.gses.beans.DirectoryBeanProvider;

import java.util.Map;

class DictionaryType implements VariableType<String> {

  @Override
  public String convertFormValueToModelValue(Object propertyValue, String pattern, Map<String, String> values) {
    String directoryId = values.get("directory_id");
    String key = propertyValue == null ? null : propertyValue.toString();
    String value = DirectoryBeanProvider.get().getValue(directoryId, key);
    if (value != null) {
      return key;
    }
    return null;
  }

  @Override
  public void validateParams(String pattern, Map<String, String> values, boolean sandbox) {
    if (pattern != null) {
      throw VariableTypes.badPattern(GsesTypes.DICTIONARY);
    }
    if (values == null || !values.containsKey("directory_id")) {
      throw new IllegalStateException("Параметер directory_id обязателен для типа " + GsesTypes.DICTIONARY.name);
    }
    if (values.size() > 1) {
      throw new IllegalStateException("Для типа " + GsesTypes.DICTIONARY.name + " поддерживается лишь параметр directory_id");
    }
  }

  @Override
  public Class<String> getJavaType() {
    return String.class;
  }

  @Override
  public String convertBufferToModelValue(FieldBuffer fieldBuffer) {
    return fieldBuffer.getTextValue();
  }

  @Override
  public String getName() {
    return "directory";
  }
}
