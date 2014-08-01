/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.types;

import ru.codeinside.gses.activiti.forms.api.definitions.VariableType;

import java.util.HashMap;
import java.util.Map;

final public class VariableTypes extends ru.codeinside.gses.activiti.forms.api.definitions.VariableTypes {

  public Map<String, VariableType> types = new HashMap<String, VariableType>();

  public VariableTypes() {
    types.put(GsesTypes.STRING.name, new StringType());
    types.put(GsesTypes.BOOLEAN.name, new BooleanType());
    types.put(GsesTypes.LONG.name, new LongType());
    types.put(GsesTypes.ENUM.name, new EnumType());
    types.put(GsesTypes.DATE.name, new DateType());
    types.put(GsesTypes.MASKED.name, new MaskedType());
    types.put(GsesTypes.DICTIONARY.name, new DictionaryType());
    types.put(GsesTypes.ATTACHMENT.name, new AttachmentType());
    types.put(GsesTypes.ENCLOSURE.name, new EnclosureType());
    types.put(GsesTypes.SMEV_REQUEST_ENCLOSURE.name, new StringType());
    types.put(GsesTypes.SMEV_RESPONSE_ENCLOSURE.name, new StringType());
    types.put(GsesTypes.JSON.name, new JsonType());
  }

  public static IllegalStateException badPattern(GsesTypes type) {
    return new IllegalStateException("Атрибут pattern не применим к типу " + type.name);
  }

  public static IllegalStateException badValues(GsesTypes type) {
    return new IllegalStateException("Элемент values не применим к типу " + type.name);
  }

  @Override
  public Map<String, VariableType> getTypes() {
    return types;
  }

}
