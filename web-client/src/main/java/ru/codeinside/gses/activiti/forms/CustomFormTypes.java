/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.BpmnParser;
import org.activiti.engine.impl.form.AbstractFormType;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.EnumFormType;
import org.activiti.engine.impl.form.FormTypes;
import org.activiti.engine.impl.util.xml.Element;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.activiti.DelegateFormType;
import ru.codeinside.gses.vaadin.FieldConstructor;
import ru.codeinside.gses.vaadin.FieldFormType;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

final public class CustomFormTypes extends FormTypes {

  @Override
  public AbstractFormType parseFormPropertyType(Element formPropertyElement, BpmnParse bpmnParse) {
    AbstractFormType formType = null;

    String typeText = formPropertyElement.attribute("type");

    // это ломает логику блоков - там проверяется что тип НЕ ЗАДАН!
    // if (typeText == null) {
    //   typeText = "string"; // явно устанавливаем тип по умолчанию
    // }

    if (typeText != null) {
      formType = formTypes.get(typeText);
      if (formType == null) {
        bpmnParse.addError("unknown type '" + typeText + "'", formPropertyElement);
      }
      if (formType instanceof DelegateFormType) {
        FieldFormType type = ((DelegateFormType) formType).getType();
        FieldConstructor fieldConstructor = ((DelegateFormType) formType).getType().createConstructorOfField();
        String patternText = null;
        if (type.usePattern()) {
          patternText = formPropertyElement.attribute("datePattern");
          if (StringUtils.isNotEmpty(patternText)) {
            fieldConstructor.setPattern(patternText);
          }
        }
        if (type.useMap()) {
          final Map<String, String> values = new LinkedHashMap<String, String>();
          for (final Element element : formPropertyElement.elementsNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "value")) {
            final String key = element.attribute("id");
            if (!Builder.EXTRA_ATTRIBUTES.contains(key)) {
              final String value = element.attribute("name");
              values.put(key, value);
            }
          }
          fieldConstructor.setMap(values);
        }
        return new DelegateFormType(type, fieldConstructor);
      }
    }
    String datePatternText = formPropertyElement.attribute("datePattern");
    if ("date".equals(typeText) && datePatternText != null) {
      formType = new DateFormType(datePatternText);
    } else if ("enum".equals(typeText)) {
      Map<String, String> values = new HashMap<String, String>();
      for (Element valueElement : formPropertyElement.elementsNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "value")) {
        String valueId = valueElement.attribute("id");
        String valueName = valueElement.attribute("name");
        values.put(valueId, valueName);
      }
      formType = new EnumFormType(values);
    }
    return formType;
  }
}
