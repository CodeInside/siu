/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.BpmnParser;
import org.activiti.engine.impl.form.AbstractFormType;
import org.activiti.engine.impl.form.FormPropertyHandler;
import org.activiti.engine.impl.util.xml.Element;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.API;
import ru.codeinside.gses.activiti.DelegateFormType;
import ru.codeinside.gses.activiti.forms.duration.DurationPreference;
import ru.codeinside.gses.activiti.forms.duration.IllegalDurationExpression;
import ru.codeinside.gses.activiti.ftarchive.JsonFFT;
import ru.codeinside.gses.activiti.ftarchive.LongFFT;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

final class Builder {

  final static ImmutableSet<String> EXTRA_ATTRIBUTES = ImmutableSet.of(
    "#underline", "#tip", "#null", "#write", "#read"
  );
  final static ImmutableSet<String> NO_VALUES = ImmutableSet.of("false", "no", "0", "n");
  final static DelegateFormType LONG_TYPE = new DelegateFormType("long", new LongFFT());
  final static DelegateFormType JSON_TYPE = new DelegateFormType("json", new JsonFFT());
  private static final Logger LOGGER = Logger.getLogger(Builder.class.getName());

  public static PropertyTree buildTree(final Element owner, final List<FormPropertyHandler> formPropertyHandlers, final BpmnParse bpmnParse) {
    if (bpmnParse.hasErrors()) {
      return null;
    }
    final Element extensionElement = owner.element("extensionElements");
    if (extensionElement != null) {
      final List<Element> formProperties = extensionElement.elementsNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "formProperty");
      try {
        if (owner.getTagName().equals("startEvent")) {
          return build(formProperties, formPropertyHandlers, true);
        } else {
          return build(formProperties, formPropertyHandlers, false);
        }
      } catch (BuildException e) {
        bpmnParse.addError(e.getMessage(), e.element);
      }
    }
    return new EmptyTree();
  }

  private static PropertyTree build(final List<Element> elements, final List<FormPropertyHandler> handlerList, boolean isStartEvent) throws BuildException {
    final Map<String, Node> nodes = new LinkedHashMap<String, Node>();
    DurationPreference durationPreference = new DurationPreference();
    createNodes(nodes, elements, handlerList, durationPreference, isStartEvent);
    processNodes(nodes);
    final List<Node> rootList = new ArrayList<Node>();
    processBlocks(nodes, rootList);
    return convertNodes(nodes, rootList, handlerList, durationPreference);
  }

  /**
   * Базовые проверки и заполнение параметров.
   */
  private static void processNodes(Map<String, Node> nodes) throws BuildException {
    for (final Node node : nodes.values()) {
      node.process(nodes);
    }
  }

  private static void processBlocks(Map<String, Node> nodes, List<Node> rootList) throws BuildException {
    final ArrayList<Node> allNodes = new ArrayList<Node>(nodes.values());
    final LinkedList<StartNode> stack = new LinkedList<StartNode>();
    for (final Node node : allNodes) {
      final StartNode block = stack.peekFirst();
      node.block = block;
      final boolean end = (node instanceof EndNode);
      if (!end) {
        if (block == null) {
          rootList.add(node);
        } else {
          block.items.add(node);
        }
        if (node instanceof StartNode) {
          final StartNode start = (StartNode) node;
          start.items = new ArrayList<Node>();
          stack.addFirst(start);
        }
      } else {
        if (block == null || !block.handler.getId().substring(1).equals(node.id.substring(1))) {
          throw new BuildException("Заверешение блока без начала", node);
        }
        stack.removeFirst();
      }
    }

    final Node badStart = stack.peekFirst();
    if (badStart != null) {
      throw new BuildException("Начало блока без завершения", badStart);
    }

    for (final Node node : allNodes) {
      if (node instanceof StartNode) {
        final StartNode start = (StartNode) node;
        if (start.items.isEmpty()) {
          throw new BuildException("Пустой блок", node);
        }
      }
    }
  }

  private static void createNodes(Map<String, Node> nodes, List<Element> elements, List<FormPropertyHandler> handlerList,
                                  DurationPreference durationPreference, boolean isStartEvent) throws BuildException {
    // создание индекса элементов
    final Map<String, Element> elementMap = createElementMap(elements);
    // создание индекса узлов
    for (final FormPropertyHandler handler : handlerList) {
      final String id = handler.getId();
      if (!"!".equals(id)) {
        final Node node = createNode(handler, id, elementMap.get(id));
        nodes.put(id, node);
      } else {
        try {
          durationPreference.parseWorkedDaysPreference(handler.getName());
          if (isStartEvent) {
            String defaultExpression = handler.getDefaultExpression().getExpressionText();
            if (StringUtils.isNotBlank(defaultExpression)) {
              durationPreference.parseTaskDefaultPreference(defaultExpression);
            }
            String periodExpression = handler.getVariableExpression().getExpressionText();
            if (StringUtils.isNotBlank(periodExpression)) {
              durationPreference.parseProcessPreference(periodExpression);
            }
          } else {
            String expressionText = handler.getVariableExpression().getExpressionText();
            if (StringUtils.isNotBlank(expressionText)) {
              durationPreference.parseTaskPreference(expressionText);
            }
          }
        } catch (IllegalDurationExpression err) {
          LOGGER.log(Level.SEVERE, String.format("Ошибка при вычислении сроков выполнения %s", elementMap.get(handler.getId()).getText()), err);
        }
      }
    }
  }

  private static Node createNode(final FormPropertyHandler handler, final String id, final Element element) throws BuildException {
    final char firstChar = id.charAt(0);
    switch (firstChar) {
      case '^':
        return new ToggleNode(id, element, handler, PropertyType.TOGGLE);

      case '~':
        return new ToggleNode(id, element, handler, PropertyType.VISIBILITY_TOGGLE);

      case '+':
        return new StartNode(id, element, handler);

      case '-':
        return new EndNode(id, element, handler);

    }
    if (handler.getType() != null && Arrays.asList("smevRequestEnclosure", "smevResponseEnclosure").contains(handler.getType().getName())) {
      // вложения в запрос SMEV
      return new EnclosureNode(id, element, handler);
    } else {
      return new GeneralNode(id, element, handler);
    }
  }

  private static PropertyTree convertNodes(Map<String, Node> nodes, List<Node> rootList, final List<FormPropertyHandler> handlerList,
                                           DurationPreference durationPreference) throws BuildException {
    final Collection<Node> values = nodes.values();

    // убрать завершения и переключатели из обработчиков
    for (final Node node : values) {
      if (node.hasHandler()) {
        continue;
      }
      final Iterator<FormPropertyHandler> i = handlerList.iterator();
      while (i.hasNext()) {
        final FormPropertyHandler h = i.next();
        if (node.id.equals(h.getId())) {
          i.remove();
          break;
        }
      }
    }

    final Map<String, PropertyNode> global = new LinkedHashMap<String, PropertyNode>();

    // сначала элементы которые не ссылаются
    for (final Node node : values) {
      node.convert1(global);
    }

    // затем ссылочные элементы
    for (final Node node : values) {
      node.convert2(global);
    }

    // затем индексы
    for (final Node node : values) {
      node.convert3(global);
    }

    final PropertyNode[] array = new PropertyNode[rootList.size()];
    for (int i = 0; i < array.length; i++) {
      array[i] = global.get(rootList.get(i).id);
    }
    return new NTree(array, global, durationPreference);
  }

  private static Map<String, Element> createElementMap(final List<Element> elements) throws BuildException {
    final Map<String, Element> index = new LinkedHashMap<String, Element>();
    for (final Element element : elements) {
      final String id = element.attribute("id");
      final String trimmed = id.trim();
      if (!id.equals(trimmed)) {
        throw new BuildException("Id с пробелами: '" + id + "'", element);
      }
      if (id.isEmpty()) {
        throw new BuildException("Пустой Id", element);
      }
      if (index.put(id, element) != null) {
        throw new BuildException("Дублирование Id " + id, element);
      }
    }
    return index;
  }

  abstract static class Node {

    final String id;
    final Element element;
    final FormPropertyHandler handler;
    final Map<String, String> extra;
    /**
     * Блок-владелец
     */
    Node block;

    Node(final String id, final Element element, final FormPropertyHandler handler) {
      this.id = id;
      this.element = element;
      this.handler = handler;
      extra = createExtra(element);
    }

    static Map<String, String> createExtra(final Element element) {
      Map<String, String> map = null;
      for (final Element valueElement : element.elementsNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "value")) {
        final String valueId = valueElement.attribute("id");
        final String valueName = valueElement.attribute("name");
        if (map == null) {
          map = new LinkedHashMap<String, String>();
        }
        map.put(valueId, valueName);
      }
      return map;
    }

    boolean acceptToggle() {
      return true;
    }

    boolean hasHandler() {
      return false;
    }

    abstract void process(Map<String, Node> global) throws BuildException;

    void convert1(Map<String, PropertyNode> global) throws BuildException {
    }

    void convert2(Map<String, PropertyNode> global) {
    }

    void convert3(Map<String, PropertyNode> global) {

    }

    int getExtraInteger(String key, int defaultValue) throws BuildException {
      if (extra != null && extra.containsKey(key)) {
        try {
          return Integer.parseInt(extra.get("max"));
        } catch (NumberFormatException e) {
          throw new BuildException(key + " должно быть числом", this);
        }
      }
      return defaultValue;
    }

    boolean hasExtra(String key) {
      return extra != null && extra.containsKey(key);
    }

    String getExtra(final String key) {
      if (hasExtra(key)) {
        return StringUtils.trimToNull(extra.get(key));
      }
      return null;
    }

    String getUnderline() {
      return getExtra("#underline");
    }

    String getTip() {
      return getExtra("#tip");
    }

    boolean isReadable() {
      String read = getExtra("#read");
      return read == null || !NO_VALUES.contains(read.toLowerCase());
    }

    boolean isWritable() {
      String read = getExtra("#write");
      return read == null || !NO_VALUES.contains(read.toLowerCase());
    }

    NullAction getNullAction() throws BuildException {
      // пропускать блоки подписи!
      final AbstractFormType type = handler.getType();
      if (type != null && "signature".equals(type.getName())) {
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

  final static class GeneralNode extends Node {
    Boolean readOverride = null;

    GeneralNode(String id, Element element, FormPropertyHandler handler) {
      super(id, element, handler);
    }

    @Override
    boolean hasHandler() {
      return true;
    }

    @Override
    boolean acceptToggle() {
      return true;
    }

    @Override
    boolean isReadable() {
      if (readOverride != null) {
        return readOverride;
      }
      return super.isReadable();
    }

    @Override
    void process(Map<String, Node> global) throws BuildException {
      if (API.JSON_FORM.equals(id)) {
        Expression exp = handler.getDefaultExpression();
        if (exp == null && StringUtils.isEmpty(handler.getVariableName())) {
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
        keys.remove(id);
        int count = 0;
        for (String key : keys) {
          Node node = global.get(key);
          if (!(node instanceof GeneralNode)) {
            throw new BuildException("Не допустимый тип для JSON формы", node);
          }
          AbstractFormType type = node.handler.getType();
          if (type == null) {
            node.handler.setType(JSON_TYPE);
            count++;
          } else if ("signature".equals(type.getName())) {
            count += 0;
          } else if ("string".equals(type.getName())) {
            count++;
          } else if ("json".equals(type.getName())) {
            count++;
          } else {
            throw new BuildException("Не допустимый тип для переменной JSON", node);
          }
          if (count > 1) {
            throw new BuildException("Допустима лишь одна переменная JSON", node);
          }
        }
        if (count == 0) {
          throw new BuildException("Нет переменной для JSON формы", this);
        }
        boolean isPropertyReadable = handler.isReadable();
        if (!isPropertyReadable) {
          // fix readable
          handler.setReadable(true);
        }
        // по умолчанию НЕ читать значение!
        if (!hasExtra("#read")) {
          readOverride = false;
        }
      }
    }

    @Override
    void convert1(Map<String, PropertyNode> global) throws BuildException {
      global.put(id, new NItem(id, getUnderline(), getTip(), getNullAction(), isReadable(), isWritable()));
    }
  }

  final static class EnclosureNode extends Node {
    EnclosureNode(String id, Element element, FormPropertyHandler handler) {
      super(id, element, handler);
    }

    @Override
    boolean hasHandler() {
      return true;
    }

    @Override
    boolean acceptToggle() {
      return true;
    }

    @Override
    void process(Map<String, Node> global) throws BuildException {
    }

    @Override
    void convert1(Map<String, PropertyNode> global) throws BuildException {
      global.put(id, new EnclosureItem(id, getUnderline(), getTip(), getNullAction()));
    }
  }

  final static class ToggleNode extends Node {

    PropertyType type;
    Node toggler;
    String toggleValue;
    boolean toggleTo;
    Node[] toggleNodes;

    ToggleNode(String id, Element element, FormPropertyHandler handler, PropertyType type) {
      super(id, element, handler);
      this.type = type;
    }

    private boolean isWrongForToggle(final Node node) {
      return node == null || !node.acceptToggle();
    }

    @Override
    void process(Map<String, Node> global) throws BuildException {
      final String name = handler.getName();
      if (name != null) {
        throw new BuildException("Атрибут 'Name' не применим к переключателю", this);
      }
      String expression = null;
      if (handler.getVariableExpression() != null) {
        expression = handler.getVariableExpression().getExpressionText();
      }
      if (expression == null) {
        throw new BuildException("Пропущен aтрибут 'Expression' для переключателя", this);
      }
      final Splitter varSplitter = Splitter.on(',').trimResults().omitEmptyStrings();
      final Set<String> refs = Sets.newLinkedHashSet(varSplitter.split(expression));
      if (refs.isEmpty()) {
        throw new BuildException("Пустой 'Expression' для переключателя", this);
      }
      final List<Node> toggleNodes = new ArrayList<Node>();
      for (final String ref : refs) {
        final Node depended = global.get(ref);
        if (isWrongForToggle(depended)) {
          throw new BuildException("Ошибка идентификтора " + ref + " в 'Expression' для переключателя", this);
        }
        toggleNodes.add(depended);
      }
      this.toggleNodes = toggleNodes.toArray(new Node[toggleNodes.size()]);
      String ref = handler.getVariableName();
      if (ref == null) {
        ref = id.substring(1);
      }
      final Node var = global.get(ref);
      if (isWrongForToggle(var) || toggleNodes.contains(var)) {
        throw new BuildException("Ошибка идентификтора " + ref + " в 'Variable' для переключателя", this);
      }
      toggler = var;
      String value = null;
      if (handler.getDefaultExpression() != null) {
        value = handler.getDefaultExpression().getExpressionText();
      }
      if (value == null) {
        value = "true";
      }
      toggleValue = value;
      toggleTo = handler.isRequired();
    }

    @Override
    void convert1(Map<String, PropertyNode> global) {
      // гарантируем порядок ключей
      global.put(id, null);
    }

    @Override
    void convert2(Map<String, PropertyNode> global) {
      final PropertyNode[] toggleNodes = new PropertyNode[this.toggleNodes.length];
      for (int i = 0; i < toggleNodes.length; i++) {
        toggleNodes[i] = global.get(this.toggleNodes[i].id);
      }
      final NToggle nToggle = new NToggle(id, type, global.get(toggler.id), toggleValue, toggleTo, toggleNodes);
      global.put(id, nToggle);
    }
  }

  final static class StartNode extends Node {
    List<Node> items;
    int min = 0;
    int max = 999;

    StartNode(String id, Element element, FormPropertyHandler handler) {
      super(id, element, handler);
    }

    @Override
    boolean hasHandler() {
      return true;
    }

    @Override
    void process(Map<String, Node> global) throws BuildException {

      if (handler.getType() != null) {
        throw new BuildException("Атрибут 'Type' не применим для начала блока", this);
      }
      String variableName = handler.getVariableName();
      if (variableName == null) {
        variableName = id.substring(1);
        handler.setVariableName(variableName);
      }
      if (global.containsKey(variableName)) {
        throw new BuildException("Ошибка атрибута 'Variable', переменная уже используется", this);
      }
      if (handler.isRequired()) {
        min = 1;
      }
      if (handler.getVariableExpression() != null) {
        try {
          min = Integer.parseInt(handler.getVariableExpression().getExpressionText());
        } catch (NumberFormatException e) {
          throw new BuildException("'Expression' должо быть числом", this);
        }
        if (min < 0) {
          throw new BuildException("'Expression' не должно быть отрицательным", this);
        }
        handler.setVariableExpression(null);
      }
      if (handler.isRequired() && min == 0) {
        throw new BuildException("'Expression' должен быть больше нуля при Required=true", this);
      }
      max = getExtraInteger("max", max);
      if (min > max) {
        throw new BuildException("Минимум превышает максимум", this);
      }
      handler.setType(LONG_TYPE);
    }

    @Override
    void convert1(Map<String, PropertyNode> global) throws BuildException {
      global.put(id, new NBlock(id, new PropertyNode[items.size()], min, max, getUnderline(), getTip(), getNullAction()));
    }

    @Override
    void convert3(final Map<String, PropertyNode> global) {
      final PropertyNode[] array = ((NBlock) global.get(id)).getNodes();
      for (int i = 0; i < array.length; i++) {
        array[i] = global.get(items.get(i).id);
      }
    }
  }

  final static class EndNode extends Node {

    EndNode(String id, Element element, FormPropertyHandler handler) {
      super(id, element, handler);
    }

    @Override
    void process(Map<String, Node> global) throws BuildException {
      final String name = handler.getName();
      if (name != null) {
        throw new BuildException("Атрибут 'Name' не применим к завершению блока", this);
      }
      if (handler.getVariableExpression() != null) {
        throw new BuildException("Атрибут 'Expression' не применим к завершению блока", this);
      }
      if (handler.getVariableName() != null) {
        throw new BuildException("Атрибут 'Variable' не применим к завершению блока", this);
      }
      if (handler.getDefaultExpression() != null) {
        throw new BuildException("Атрибут 'Default' не применим к завершению блока", this);
      }
    }

    @Override
    boolean acceptToggle() {
      return false;
    }


  }

}
