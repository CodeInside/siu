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
import org.activiti.engine.impl.form.FormTypes;
import org.activiti.engine.impl.util.xml.Element;
import ru.codeinside.gses.activiti.DelegateFormType;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final public class CustomFormTypes extends FormTypes {

  @Override
  public AbstractFormType parseFormPropertyType(Element formPropertyElement, BpmnParse bpmnParse) {
    AbstractFormType formType = null;
    String typeText = formPropertyElement.attribute("type");
    if (typeText != null) {
      formType = formTypes.get(typeText);
      if (formType == null) {
        bpmnParse.addError("unknown type '" + typeText + "'", formPropertyElement);
      }
      if (formType instanceof DelegateFormType) {
        String patternText = formPropertyElement.attribute("datePattern");
        Map<String, String> params;
        List<Element> elements = formPropertyElement.elementsNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "value");
        if (elements.isEmpty()) {
          params = Collections.emptyMap();
        } else {
          params = new LinkedHashMap<String, String>(elements.size());
          for (Element element : elements) {
            String key = element.attribute("id");
            if (!Builder.EXTRA_ATTRIBUTES.contains(key)) {
              final String value = element.attribute("name");
              params.put(key, value);
            }
          }
          if (params.isEmpty()) {
            params = Collections.emptyMap();
          }
        }
        formType = ((DelegateFormType) formType).withParams(patternText, params);
      }
    }
    return formType;
  }
}
