/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.types;

import com.google.common.collect.ImmutableMap;
import ru.codeinside.gses.activiti.forms.api.definitions.VariableType;

import java.util.Map;

final public class VariableTypes extends ru.codeinside.gses.activiti.forms.api.definitions.VariableTypes {

  final private ImmutableMap<String, VariableType> types;

  public VariableTypes() {
    ImmutableMap.Builder<String, VariableType> builder = ImmutableMap.builder();
    builder.put(GsesTypes.STRING.name, new StringType());
    builder.put(GsesTypes.BOOLEAN.name, new BooleanType());
    builder.put(GsesTypes.LONG.name, new LongType());
    builder.put(GsesTypes.ENUM.name, new EnumType());
    builder.put(GsesTypes.DATE.name, new DateType());
    builder.put(GsesTypes.MASKED.name, new MaskedType());
    builder.put(GsesTypes.DICTIONARY.name, new DictionaryType());
    builder.put(GsesTypes.ATTACHMENT.name, new AttachmentType());
    builder.put(GsesTypes.ENCLOSURE.name, new EnclosureType());
    builder.put(GsesTypes.SMEV_REQUEST_ENCLOSURE.name, new StringType());
    builder.put(GsesTypes.SMEV_RESPONSE_ENCLOSURE.name, new StringType());
    builder.put(GsesTypes.JSON.name, new JsonType());
    builder.put(GsesTypes.MULTILINE.name, new MultilineType());
    types = builder.build();
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
