package org.activiti.engine.impl.bpmn.parser;

import org.activiti.engine.impl.el.ExpressionManager;
import ru.codeinside.gses.activiti.forms.api.definitions.SandboxAware;

/**
 * Фабрика механизма анализа BPMN.
 */
final public class CustomBpmnParser extends BpmnParser implements SandboxAware {

  final boolean sandbox;

  public CustomBpmnParser(final ExpressionManager expressionManager, boolean sandbox) {
    super(expressionManager);
    this.sandbox = sandbox;
  }

  public BpmnParse createParse() {
    return new CustomBpmnParse(this, sandbox);
  }

  @Override
  public boolean isSandbox() {
    return sandbox;
  }
}
