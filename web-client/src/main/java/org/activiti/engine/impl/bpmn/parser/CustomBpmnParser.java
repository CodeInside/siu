package org.activiti.engine.impl.bpmn.parser;

import org.activiti.engine.impl.el.ExpressionManager;

/**
 * Класс нужен лишь для того чтобы заменить BpmnParse, так как в API Activiti это не предусмотрено.
 */
final public class CustomBpmnParser extends BpmnParser {
    public CustomBpmnParser(final ExpressionManager expressionManager) {
        super(expressionManager);
    }

    public BpmnParse createParse() {
        return new CustomBpmnParse(this);
    }

}
