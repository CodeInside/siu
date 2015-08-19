/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.definitions;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.BpmnParser;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.form.FormTypes;
import org.activiti.engine.impl.persistence.entity.DeploymentEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.util.xml.Element;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.API;
import ru.codeinside.gses.activiti.forms.api.definitions.NullAction;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyTree;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyType;
import ru.codeinside.gses.activiti.forms.api.definitions.SandboxAware;
import ru.codeinside.gses.activiti.forms.api.definitions.VariableType;
import ru.codeinside.gses.activiti.forms.api.definitions.VariableTypes;
import ru.codeinside.gses.activiti.forms.api.duration.DurationPreference;
import ru.codeinside.gses.activiti.forms.api.duration.IllegalDurationExpression;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FormParser {

  final static ImmutableSet<String> EXTRA_ATTRIBUTES = ImmutableSet.of(
    "#underline", "#tip", "#null", "#write", "#read"
  );

  final static ImmutableSet<String> SMEV_TYPES = ImmutableSet.of("smevRequestEnclosure", "smevResponseEnclosure");

  final static ImmutableSet<String> NO_VALUES = ImmutableSet.of("false", "no", "0", "n");


  String deploymentId;
  String formKey;
  Map<String, FormProperty> formProperties;
  boolean startEvent;
  DurationPreference durationPreference = new DurationPreference();

  Map<String, VariableType> variableTypes;
  BpmnParse bpmnParse;
  boolean signatureRequired;
  boolean isDataFlow;
  boolean isResultDataFlow;
  String consumerName;
  String requestType; //TODO сделать enum?
  String responseMessage;
  Map<String, Boolean> dataFlowParameters = new HashMap<String, Boolean>();
  Map<String, Boolean> resultDataFlowParameters = new HashMap<String, Boolean>();
  boolean sandbox;

  private PropertyTree buildTree() {
    if (bpmnParse.hasErrors()) {
      return null;
    }
    FormTypes formTypes = Context.getProcessEngineConfiguration().getFormTypes();
    if (formTypes instanceof VariableTypes) {
      this.variableTypes = ((VariableTypes) formTypes).getTypes();
    } else {
      throw new IllegalStateException("Конфигурация системы не поддерживает типы!");
    }

    try {
      Map<String, PropertyParser> nodes = new LinkedHashMap<String, PropertyParser>(formProperties.size());
      createNodes(nodes);
      for (PropertyParser propertyParser : nodes.values()) {
        propertyParser.process(nodes);
      }
      final List<PropertyParser> rootList = new ArrayList<PropertyParser>();
      processBlocks(nodes, rootList);
      return convertNodes(nodes, rootList, durationPreference);
    } catch (BuildException e) {
      bpmnParse.addError(e.getMessage(), e.element);
      return null;
    }
  }

  PropertyTree convertNodes(Map<String, PropertyParser> nodes, List<PropertyParser> rootList, DurationPreference durationPreference) throws BuildException {
    final Collection<PropertyParser> values = nodes.values();

    final Map<String, PropertyNode> global = new LinkedHashMap<String, PropertyNode>();

    // сначала элементы которые не ссылаются
    for (final PropertyParser propertyParser : values) {
      propertyParser.convert1(global);
    }

    // затем ссылочные элементы
    for (final PropertyParser propertyParser : values) {
      propertyParser.convert2(global);
    }

    // затем индексы
    for (final PropertyParser propertyParser : values) {
      propertyParser.convert3(global);
    }

    final PropertyNode[] array = new PropertyNode[rootList.size()];
    for (int i = 0; i < array.length; i++) {
      array[i] = global.get(rootList.get(i).property.id);
    }
    return new NTree(
        array, global, durationPreference, formKey, signatureRequired,
        isDataFlow, consumerName, dataFlowParameters,
        isResultDataFlow, requestType, responseMessage, resultDataFlowParameters);
  }

  void processBlocks(Map<String, PropertyParser> nodes, List<PropertyParser> rootList) throws BuildException {
    final ArrayList<PropertyParser> allPropertyParsers = new ArrayList<PropertyParser>(nodes.values());
    final LinkedList<BlockStartParser> stack = new LinkedList<BlockStartParser>();
    for (final PropertyParser propertyParser : allPropertyParsers) {
      final BlockStartParser block = stack.peekFirst();
      propertyParser.block = block;
      final boolean end = (propertyParser instanceof EndBlockParser);
      if (!end) {
        if (block == null) {
          if (!(propertyParser instanceof SignatureParser) &&
              !(propertyParser instanceof DataFlowParser) &&
              !(propertyParser instanceof ResultDataFlowParser)) {
            rootList.add(propertyParser);
          }
        } else {
          block.items.add(propertyParser);
        }
        if (propertyParser instanceof BlockStartParser) {
          final BlockStartParser start = (BlockStartParser) propertyParser;
          start.items = new ArrayList<PropertyParser>();
          stack.addFirst(start);
        }
      } else {
        if (block == null || !block.property.id.substring(1).equals(propertyParser.property.id.substring(1))) {
          throw new BuildException("Заверешение блока без начала", propertyParser);
        }
        stack.removeFirst();
      }
    }

    PropertyParser badStart = stack.peekFirst();
    if (badStart != null) {
      throw new BuildException("Начало блока без завершения", badStart);
    }

    for (PropertyParser propertyParser : allPropertyParsers) {
      if (propertyParser instanceof BlockStartParser) {
        final BlockStartParser start = (BlockStartParser) propertyParser;
        if (start.items.isEmpty()) {
          throw new BuildException("Пустой блок", propertyParser);
        }
      }
    }
  }

  void createNodes(Map<String, PropertyParser> nodes) throws BuildException {
    for (FormProperty handler : formProperties.values()) {
      String id = handler.id;
      if (!"!".equals(id)) {
        final PropertyParser propertyParser = createNode(handler);
        nodes.put(id, propertyParser);
      } else {
        try {
          durationPreference.parseWorkedDaysPreference(handler.name);
          if (startEvent) {
            if (handler.defaultExpression != null) {
              String defaultExpression = handler.defaultExpression.getExpressionText();
              if (StringUtils.isNotBlank(defaultExpression)) {
                durationPreference.parseTaskDefaultPreference(defaultExpression);
              }
            }
            if (handler.variableExpression != null) {
              String periodExpression = handler.variableExpression.getExpressionText();
              if (StringUtils.isNotBlank(periodExpression)) {
                durationPreference.parseProcessPreference(periodExpression);
              }
            }
          } else if (handler.variableExpression != null) {
            String expressionText = handler.variableExpression.getExpressionText();
            if (StringUtils.isNotBlank(expressionText)) {
              durationPreference.parseTaskPreference(expressionText);
            }
          }
        } catch (IllegalDurationExpression err) {
          throw new BuildException(err.getMessage(), handler);
        }
      }
    }
  }

  PropertyParser createNode(FormProperty property) throws BuildException {
    final char firstChar = property.id.charAt(0);
    switch (firstChar) {
      case '^':
        return new TogglePropertyParser(property, PropertyType.TOGGLE);

      case '~':
        return new TogglePropertyParser(property, PropertyType.VISIBILITY_TOGGLE);

      case '+':
        return new BlockStartParser(property);

      case '-':
        return new EndBlockParser(property);

    }
    if ("signature".equals(property.type)) {
      signatureRequired = true;
      return new SignatureParser(property);
    } else if ("dataflow".equals(property.type)) {
      isDataFlow = true;
      if (property.values.containsKey("needSp") && property.values.get("needSp").equals("true")) {
        dataFlowParameters.put("needSp", true);
      } else {
        dataFlowParameters.put("needSp", false);
      }
      if (property.values.containsKey("needOv") && property.values.get("needOv").equals("true")) {
        dataFlowParameters.put("needOv", true);
      } else {
        dataFlowParameters.put("needOv", false);
      }
      if (property.values.containsKey("needTep") && property.values.get("needTep").equals("true")) {
        dataFlowParameters.put("needTep", true);
      } else {
        dataFlowParameters.put("needTep", false);
      }
      if (property.values.containsKey("needSend") && property.values.get("needSend").equals("true")) {
        dataFlowParameters.put("needSend", true);
      } else {
        dataFlowParameters.put("needSend", false);
      }
      if (property.values.containsKey("isLazyWriter") && property.values.get("isLazyWriter").equals("true")) {
        dataFlowParameters.put("isLazyWriter", true);
      } else {
        dataFlowParameters.put("isLazyWriter", false);
      }
      if (property.values.containsKey("consumerName")) {
        consumerName = property.values.get("consumerName");
      }
      if (property.values.containsKey("appDataSignatureBlockPosition") && property.values.get("appDataSignatureBlockPosition").equals("last")) {
        dataFlowParameters.put("isAppDataSignatureBlockLast", true);
      } else {
        dataFlowParameters.put("isAppDataSignatureBlockLast", false);
      }

      return new DataFlowParser(property);

    } else if ("resultDataflow".equals(property.type)) {
      isResultDataFlow = true;
      if (property.values.containsKey("needSp") && property.values.get("needSp").equals("true")) {
        resultDataFlowParameters.put("needSp", true);
      } else {
        resultDataFlowParameters.put("needSp", false);
      }
      if (property.values.containsKey("needOv") && property.values.get("needOv").equals("true")) {
        resultDataFlowParameters.put("needOv", true);
      } else {
        resultDataFlowParameters.put("needOv", false);
      }
      if (property.values.containsKey("requestType")) {
        requestType = property.values.get("requestType");
      }
      if (property.values.containsKey("responseMessage")) {
        responseMessage = property.values.get("responseMessage");
      }
      if (property.values.containsKey("appDataSignatureBlockPosition") && property.values.get("appDataSignatureBlockPosition").equals("last")) {
        resultDataFlowParameters.put("isAppDataSignatureBlockLast", true);
      } else {
        resultDataFlowParameters.put("isAppDataSignatureBlockLast", false);
      }

      return new ResultDataFlowParser(property);

    } else if (property.type != null && SMEV_TYPES.contains(property.type)) {
      // вложения в запрос СМЭВ
      return new EnclosurePropertyParser(property);
    } else {
      return new GeneralPropertyParser(property);
    }
  }


  public PropertyTree parseProperties(Element activityElement, ProcessDefinitionEntity processDefinition, DeploymentEntity deployment, BpmnParse bpmnParse) {
    this.deploymentId = deployment.getId();
    this.bpmnParse = bpmnParse;
    sandbox = ((SandboxAware) bpmnParse).isSandbox();
    startEvent = activityElement.getTagName().equals("startEvent");
    formKey = activityElement.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "formKey");
    if (startEvent && formKey != null) {
      processDefinition.setStartFormKey(true);
    }
    Element extensionElement = activityElement.element("extensionElements");
    if (extensionElement == null) {
      formProperties = Collections.emptyMap();
    } else {

      ExpressionManager expressionManager = Context
        .getProcessEngineConfiguration()
        .getExpressionManager();

      List<Element> formPropertyElements = extensionElement.elementsNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "formProperty");
      formProperties = new LinkedHashMap<String, FormProperty>(formPropertyElements.size());
      for (Element formPropertyElement : formPropertyElements) {
        FormProperty formProperty = new FormProperty();
        formProperty.element = formPropertyElement;
        String id = StringUtils.trimToNull(formPropertyElement.attribute("id"));
        if (id == null) {
          bpmnParse.addError("attribute 'id' is required", formPropertyElement);
        }
        if (formProperties.containsKey(id)) {
          FormProperty dup = formProperties.get(id);
          bpmnParse.addError(
            "attribute 'id' is duplicated with (" + dup.element.getLine() + "," + dup.element.getColumn() + ")",
            formPropertyElement);
        }
        formProperties.put(id, formProperty);
        formProperty.id = id;
        formProperty.name = formPropertyElement.attribute("name");
        formProperty.type = formPropertyElement.attribute("type");
        formProperty.pattern = formPropertyElement.attribute("datePattern");

        List<Element> valueElements = formPropertyElement.elementsNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "value");
        if (!valueElements.isEmpty()) {
          Map<String, String> values = new LinkedHashMap<String, String>(valueElements.size());
          for (Element valueElement : valueElements) {
            String valueId = valueElement.attribute("id");
            String valueName = valueElement.attribute("name");
            if (values.containsKey(valueId)) {
              if (sandbox) {
                bpmnParse.addError("Дублирование идентификатора '" + valueId + "'", valueElement);
              }
            } else {
              values.put(valueId, valueName);
            }
          }
          Set<String> valueIds = new HashSet<String>(values.keySet());
          Map<String, String> extra = new LinkedHashMap<String, String>(valueElements.size());
          for (String valueId : valueIds) {
            if (EXTRA_ATTRIBUTES.contains(valueId)) {
              extra.put(valueId, values.remove(valueId));
            }
          }
          if (!values.isEmpty()) {
            formProperty.values = ImmutableMap.copyOf(values);
          }
          if (!extra.isEmpty()) {
            formProperty.extra = ImmutableMap.copyOf(extra);
          }
        }

        String requiredText = formPropertyElement.attribute("required", "false");
        Boolean required = bpmnParse.parseBooleanAttribute(requiredText);
        if (required != null) {
          formProperty.isRequired = required;
        } else {
          bpmnParse.addError("attribute 'required' must be one of {on|yes|true|enabled|active|off|no|false|disabled|inactive}", formPropertyElement);
        }

        String readableText = formPropertyElement.attribute("readable", "true");
        Boolean readable = bpmnParse.parseBooleanAttribute(readableText);
        if (readable != null) {
          formProperty.isReadable = readable;
        } else {
          bpmnParse.addError("attribute 'readable' must be one of {on|yes|true|enabled|active|off|no|false|disabled|inactive}", formPropertyElement);
        }

        String writableText = formPropertyElement.attribute("writable", "true");
        Boolean writable = bpmnParse.parseBooleanAttribute(writableText);
        if (writable != null) {
          formProperty.isWritable = writable;
        } else {
          bpmnParse.addError("attribute 'writable' must be one of {on|yes|true|enabled|active|off|no|false|disabled|inactive}", formPropertyElement);
        }

        formProperty.variableName = formPropertyElement.attribute("variable");

        String expressionText = formPropertyElement.attribute("expression");
        if (expressionText != null) {
          formProperty.variableExpression = expressionManager.createExpression(expressionText);
        }

        String defaultExpressionText = formPropertyElement.attribute("default");
        if (defaultExpressionText != null) {
          formProperty.defaultExpression = expressionManager.createExpression(defaultExpressionText);
        }
      }
    }
    return buildTree();
  }

  // TODO: можно сразу привязывать узел к свойству
  final static class FormProperty {
    String id;
    String name;
    String type;
    boolean isReadable;
    boolean isWritable;
    boolean isRequired;
    String variableName;
    Expression variableExpression;
    Expression defaultExpression;
    Element element;
    String pattern;
    Map<String, String> values;
    Map<String, String> extra;
  }

  final static class BuildException extends Exception {

    final Element element;

    BuildException(String message, Element element) {
      super(message);
      this.element = element;
    }

    BuildException(String message, FormProperty formProperty) {
      super(message + ' ' + formProperty.id);
      this.element = formProperty.element;
    }

    BuildException(String message, PropertyParser parser) {
      this(message, parser.property);
    }

  }

  final class EnclosurePropertyParser extends PropertyParser {
    EnclosurePropertyParser(FormProperty property) {
      super(property);
    }

    @Override
    boolean acceptToggle() {
      return true;
    }

    @Override
    void process(Map<String, PropertyParser> global) throws BuildException {
    }

    @Override
    void convert1(Map<String, PropertyNode> global) throws BuildException {
      VariableType enclosureType = variableTypes.get("enclosure");
      if (enclosureType == null) {
        throw new BuildException("Тип enclosure не поддерживается", this);
      }
      global.put(property.id, new EnclosureItem(
        property.id, property.name, property.variableName, getUnderline(), getTip(), getNullAction(), getType(),
        property.isReadable && property.isWritable, enclosureType
      ));
    }
  }

  final class EndBlockParser extends PropertyParser {

    EndBlockParser(FormProperty property) {
      super(property);
    }

    @Override
    void process(Map<String, PropertyParser> global) throws BuildException {
      if (property.name != null) {
        throw new BuildException("Атрибут 'Name' не применим к завершению блока", this);
      }
      if (property.variableExpression != null) {
        throw new BuildException("Атрибут 'Expression' не применим к завершению блока", this);
      }
      if (property.variableName != null) {
        throw new BuildException("Атрибут 'Variable' не применим к завершению блока", this);
      }
      if (property.defaultExpression != null) {
        throw new BuildException("Атрибут 'Default' не применим к завершению блока", this);
      }
    }

    @Override
    boolean acceptToggle() {
      return false;
    }

  }

  final class SignatureParser extends PropertyParser {

    SignatureParser(FormProperty property) {
      super(property);
    }

    @Override
    void process(Map<String, PropertyParser> global) throws BuildException {
      if (property.variableExpression != null) {
        throw new BuildException("Атрибут 'Expression' не применим к ЭЦП", this);
      }
      if (property.defaultExpression != null) {
        throw new BuildException("Атрибут 'Default' не применим к ЭЦП", this);
      }
      for (PropertyParser p : global.values()) {
        if (p != this && p instanceof SignatureParser) {
          throw new BuildException("Дублирование ЭЦП", this);
        }
      }
    }

    @Override
    boolean acceptToggle() {
      return false;
    }

  }

  final class DataFlowParser extends PropertyParser {
    DataFlowParser(FormProperty property) {
      super(property);
    }

    @Override
    void process(Map<String, PropertyParser> global) throws BuildException {
      if (!property.values.containsKey("consumerName")) {
        throw new BuildException("Не заполнено название потребителя (consumerName)", this);
      }
      for (PropertyParser p : global.values()) {
        if (p != this && p instanceof DataFlowParser) {
          throw new BuildException("Дублирование блока 'DataFlow'", this);
        }
      }
    }

    @Override
    boolean acceptToggle() {
      return false;
    }
  }

  final class ResultDataFlowParser extends PropertyParser {
    ResultDataFlowParser(FormProperty property) {
      super(property);
    }

    @Override
    void process(Map<String, PropertyParser> global) throws BuildException {
      if (!property.values.containsKey("requestType") ||
          ( !"result".equals(property.values.get("requestType")) && !"status".equals(property.values.get("requestType")) )
          ) {
        throw new BuildException("Не заполнен тип запроса (requestType)", this);
      }

      //TODO делать ли поле responseMessage обязательным?
//      if ("status".equals(property.values.get("requestType")) && !property.values.containsKey("responseMessage")) {
//        throw new BuildException("Для типа \"status\" поле \"responseMessage\" является обязательным", this);
//      }

      for (PropertyParser p : global.values()) {
        if (p != this && p instanceof ResultDataFlowParser) {
          throw new BuildException("Дублирование блока 'ResultDataFlow'", this);
        }
      }
    }

    @Override
    boolean acceptToggle() {
      return false;
    }
  }


  abstract class PropertyParser {

    final FormProperty property;

    /**
     * Блок-владелец
     */
    PropertyParser block;

    PropertyParser(FormProperty property) {
      this.property = property;
    }

    boolean acceptToggle() {
      return true;
    }

    abstract void process(Map<String, PropertyParser> global) throws BuildException;

    VariableType getType() throws BuildException {
      String type = property.type == null ? "string" : property.type;
      if (!variableTypes.containsKey(type)) {
        throw new BuildException("Неизвестный тип " + property.type, this);
      }
      VariableType variableType = variableTypes.get(type);
      try {
        variableType.validateParams(property.pattern, property.values, sandbox);
      } catch (Exception e) {
        throw new BuildException(e.getMessage(), this);
      }
      return variableType;
    }

    void convert1(Map<String, PropertyNode> global) throws BuildException {
    }

    void convert2(Map<String, PropertyNode> global) {
    }

    void convert3(Map<String, PropertyNode> global) {

    }

    int getInteger(String key, int defaultValue) throws BuildException {
      if (property.values != null && property.values.containsKey(key)) {
        try {
          return Integer.parseInt(property.values.get(key));
        } catch (NumberFormatException e) {
          throw new BuildException(key + " должно быть числом", this);
        }
      }
      return defaultValue;
    }

    boolean hasExtra(String key) {
      return property.extra != null && property.extra.containsKey(key);
    }

    String getExtra(final String key) {
      if (hasExtra(key)) {
        return StringUtils.trimToNull(property.extra.get(key));
      }
      return null;
    }

    String getUnderline() {
      return getExtra("#underline");
    }

    String getTip() {
      return getExtra("#tip");
    }

    boolean isVarReadable() {
      String read = getExtra("#read");
      return read == null || !NO_VALUES.contains(read.toLowerCase());
    }

    boolean isVarWritable() {
      String read = getExtra("#write");
      return read == null || !NO_VALUES.contains(read.toLowerCase());
    }

    NullAction getNullAction() throws BuildException {
      // пропускать блоки подписи!
      if ("signature".equals(property.type)) {
        return NullAction.skip;
      }
      final String value = getExtra("#null");
      if ("skip".equalsIgnoreCase(value)) {
        return NullAction.skip;
      }
      if ("remove".equalsIgnoreCase(value)) {
        return NullAction.remove;
      }
      if ("set".equalsIgnoreCase(value) || value == null) {
        return NullAction.set;
      }
      throw new BuildException("Неизвестное поведение для null: " + value, this);
    }
  }

  final class GeneralPropertyParser extends PropertyParser {
    Boolean readOverride = null;

    GeneralPropertyParser(FormProperty formProperty) {
      super(formProperty);
    }

    @Override
    boolean acceptToggle() {
      return true;
    }

    @Override
    boolean isVarReadable() {
      if (readOverride != null) {
        return readOverride;
      }
      return super.isVarReadable();
    }

    @Override
    void process(Map<String, PropertyParser> global) throws BuildException {
      if (API.JSON_FORM.equals(property.id)) {
        Expression exp = property.defaultExpression;
        if (exp == null && StringUtils.isEmpty(property.variableName)) {
          throw new BuildException("Не указан url шаблона JSON формы", this);
        }
        if (exp != null && exp.getExpressionText() != null) {
          try {
            new URL(new URL("http://localhost:8080"), exp.getExpressionText());
          } catch (MalformedURLException e) {
            throw new BuildException("Ошибка в url шаблона JSON формы", this);
          }
        }
        Set<String> keys = new HashSet<String>(global.keySet());
        keys.remove(property.id);
        int count = 0;
        for (String key : keys) {
          PropertyParser propertyParser = global.get(key);
          if (
              !(propertyParser instanceof GeneralPropertyParser ||
              propertyParser instanceof SignatureParser ||
              propertyParser instanceof DataFlowParser ||
              propertyParser instanceof ResultDataFlowParser)
              ) {
            throw new BuildException("Недопустимый тип для JSON формы", propertyParser);
          }
          String type = propertyParser.property.type;
          if (type == null) {
            propertyParser.property.type = "json";
            count++;
          } else if ("signature".equals(type)) {
            count += 0;
          } else if ("dataflow".equals(type)) {
            count += 0;
          } else if ("resultDataflow".equals(type)) {
            count += 0;
          } else if ("string".equals(type)) {
            count++;
          } else if ("json".equals(type)) {
            count++;
          } else {
            throw new BuildException("Недопустимый тип для переменной JSON", propertyParser);
          }
          if (count > 1) {
            throw new BuildException("Допустима лишь одна переменная JSON", propertyParser);
          }
        }
        if (count == 0) {
          throw new BuildException("Нет переменной для JSON формы", this);
        }
        if (!property.isReadable) {
          // fix readable
          property.isReadable = true;
        }
        // по умолчанию НЕ читать значение!
        if (!hasExtra("#read")) {
          readOverride = false;
        }
      }
    }

    @Override
    void convert1(Map<String, PropertyNode> global) throws BuildException {
      global.put(property.id, new NItem(
        property.id, getUnderline(), getTip(), getNullAction(), isVarReadable(), isVarWritable(),
        property.name, property.isReadable, property.isRequired,
        property.variableName, property.variableExpression, property.defaultExpression,
        getType(), property.isWritable, // readable & writable
        property.pattern, property.values
      ));
    }
  }

  final class TogglePropertyParser extends PropertyParser {

    PropertyType type;
    PropertyParser toggler;
    String toggleValue;
    boolean toggleTo;
    PropertyParser[] togglePropertyParsers;

    TogglePropertyParser(FormProperty property, PropertyType type) {
      super(property);
      this.type = type;
    }

    private boolean isWrongForToggle(final PropertyParser propertyParser) {
      return propertyParser == null || !propertyParser.acceptToggle();
    }

    @Override
    void process(Map<String, PropertyParser> global) throws BuildException {
      final String name = property.name;
      if (name != null) {
        throw new BuildException("Атрибут 'Name' не применим к переключателю", this);
      }
      String expression = null;
      if (property.variableExpression != null) {
        expression = property.variableExpression.getExpressionText();
      }
      if (expression == null) {
        throw new BuildException("Пропущен aтрибут 'Expression' для переключателя", this);
      }
      final Splitter varSplitter = Splitter.on(',').trimResults().omitEmptyStrings();
      final Set<String> refs = Sets.newLinkedHashSet(varSplitter.split(expression));
      if (refs.isEmpty()) {
        throw new BuildException("Пустой 'Expression' для переключателя", this);
      }
      final List<PropertyParser> togglePropertyParsers = new ArrayList<PropertyParser>();
      for (final String ref : refs) {
        final PropertyParser depended = global.get(ref);
        if (isWrongForToggle(depended)) {
          throw new BuildException("Ошибка идентификтора " + ref + " в 'Expression' для переключателя", this);
        }
        togglePropertyParsers.add(depended);
      }
      this.togglePropertyParsers = togglePropertyParsers.toArray(new PropertyParser[togglePropertyParsers.size()]);
      String ref = property.variableName;
      if (ref == null) {
        ref = property.id.substring(1);
      }
      final PropertyParser var = global.get(ref);
      if (isWrongForToggle(var) || togglePropertyParsers.contains(var)) {
        throw new BuildException("Ошибка идентификтора " + ref + " в 'Variable' для переключателя", this);
      }
      toggler = var;
      String value = null;
      if (property.defaultExpression != null) {
        value = property.defaultExpression.getExpressionText();
      }
      if (value == null) {
        value = "true";
      }
      toggleValue = value;
      toggleTo = property.isRequired;
    }

    @Override
    void convert1(Map<String, PropertyNode> global) {
      // гарантируем порядок ключей
      global.put(property.id, null);
    }

    @Override
    void convert2(Map<String, PropertyNode> global) {
      final PropertyNode[] toggleNodes = new PropertyNode[this.togglePropertyParsers.length];
      for (int i = 0; i < toggleNodes.length; i++) {
        toggleNodes[i] = global.get(this.togglePropertyParsers[i].property.id);
      }
      final NToggle nToggle = new NToggle(property.id, type, global.get(toggler.property.id), toggleValue, toggleTo, toggleNodes);
      global.put(property.id, nToggle);
    }
  }

  final class BlockStartParser extends PropertyParser {
    List<PropertyParser> items;
    int min = 0;
    int max = 999;

    BlockStartParser(FormProperty property) {
      super(property);
    }

    @Override
    void process(Map<String, PropertyParser> global) throws BuildException {
      if (property.type != null) {
        throw new BuildException("Атрибут 'Type' не применим для начала блока", this);
      }
      String variableName = property.variableName;
      if (variableName == null) {
        variableName = property.id.substring(1);
        property.variableName = variableName;
      }
      if (global.containsKey(variableName)) {
        throw new BuildException("Ошибка атрибута 'Variable', переменная уже используется", this);
      }
      if (property.isRequired) {
        min = 1;
      }
      if (property.variableExpression != null) {
        try {
          min = Integer.parseInt(property.variableExpression.getExpressionText());
        } catch (NumberFormatException e) {
          throw new BuildException("'Expression' должо быть числом", this);
        }
        if (min < 0) {
          throw new BuildException("'Expression' не должно быть отрицательным", this);
        }
        property.variableExpression = null;
      }
      if (property.isRequired && min == 0) {
        throw new BuildException("'Expression' должен быть больше нуля при Required=true", this);
      }
      max = getInteger("max", max);
      if (min > max) {
        throw new BuildException("Минимум превышает максимум", this);
      }
      property.type = "long";
      property.values = null;
    }

    @Override
    void convert1(Map<String, PropertyNode> global) throws BuildException {
      global.put(property.id, new NBlock(
        property.id, new PropertyNode[items.size()], min, max, getUnderline(), getTip(), getNullAction(),
        getType(), property.isWritable, property.variableName, property.isRequired, property.isReadable,
        property.name, property.defaultExpression, isVarReadable(), isVarWritable()
      ));
    }

    @Override
    void convert3(final Map<String, PropertyNode> global) {
      final PropertyNode[] array = ((NBlock) global.get(property.id)).getNodes();
      for (int i = 0; i < array.length; i++) {
        array[i] = global.get(items.get(i).property.id);
      }
    }
  }

}

