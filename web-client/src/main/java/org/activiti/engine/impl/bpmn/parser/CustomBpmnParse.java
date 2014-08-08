package org.activiti.engine.impl.bpmn.parser;

import org.activiti.engine.impl.form.StartFormHandler;
import org.activiti.engine.impl.form.TaskFormHandler;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ScopeImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.impl.util.ReflectUtil;
import org.activiti.engine.impl.util.xml.Element;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.activiti.behavior.SmevTaskBehavior;
import ru.codeinside.gses.activiti.behavior.SmevTaskConfig;
import ru.codeinside.gses.activiti.forms.CustomStartFormHandler;
import ru.codeinside.gses.activiti.forms.CustomTaskFormHandler;

import java.util.List;

/**
 * Расширения стуктурного анализа BPMN.
 */
final public class CustomBpmnParse extends BpmnParse {

  CustomBpmnParse(BpmnParser parser) {
    super(parser);
  }

  @Override
  protected void parseStartFormHandlers(List<Element> startEventElements, ProcessDefinitionEntity processDefinition) {
    if (processDefinition.getInitial() != null) {
      for (Element startEventElement : startEventElements) {
        if (startEventElement.attribute("id").equals(processDefinition.getInitial().getId())) {
          StartFormHandler startFormHandler;
          String startFormHandlerClassName = startEventElement.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "formHandlerClass");
          if (startFormHandlerClassName != null) {
            startFormHandler = (StartFormHandler) ReflectUtil.instantiate(startFormHandlerClassName);
          } else {
            // замена обработчика:
            startFormHandler = new CustomStartFormHandler();
          }
          startFormHandler.parseConfiguration(startEventElement, deployment, processDefinition, this);
          processDefinition.setStartFormHandler(startFormHandler);
        }
      }
    }
  }

  public TaskDefinition parseTaskDefinition(Element taskElement, String taskDefinitionKey, ProcessDefinitionEntity processDefinition) {
    TaskFormHandler taskFormHandler;
    String taskFormHandlerClassName = taskElement.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "formHandlerClass");
    if (taskFormHandlerClassName != null) {
      taskFormHandler = (TaskFormHandler) ReflectUtil.instantiate(taskFormHandlerClassName);
    } else {
      // замена обработчика:
      taskFormHandler = new CustomTaskFormHandler();
    }
    taskFormHandler.parseConfiguration(taskElement, deployment, processDefinition, this);
    TaskDefinition taskDefinition = new TaskDefinition(taskFormHandler);
    taskDefinition.setKey(taskDefinitionKey);
    processDefinition.getTaskDefinitions().put(taskDefinitionKey, taskDefinition);
    String name = taskElement.attribute("name");
    if (name != null) {
      taskDefinition.setNameExpression(expressionManager.createExpression(name));
    }
    String descriptionStr = parseDocumentation(taskElement);
    if (descriptionStr != null) {
      taskDefinition.setDescriptionExpression(expressionManager.createExpression(descriptionStr));
    }
    parseHumanPerformer(taskElement, taskDefinition);
    parsePotentialOwner(taskElement, taskDefinition);

    // Activiti custom extension
    parseUserTaskCustomExtensions(taskElement, taskDefinition);
    return taskDefinition;
  }

  @Override
  public ActivityImpl parseServiceTask(Element serviceTaskElement, ScopeImpl scope) {
    String delegateExpression = StringUtils.trimToNull(serviceTaskElement.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "delegateExpression"));
    if (!"СМЭВ".equalsIgnoreCase(delegateExpression)) {
      return super.parseServiceTask(serviceTaskElement, scope);
    }
    ActivityImpl activity = createActivityOnScope(serviceTaskElement, scope);
    try {
      SmevTaskConfig config = new SmevTaskConfig(parseFieldDeclarations(serviceTaskElement));
      activity.setActivityBehavior(new SmevTaskBehavior(config));
    } catch (IllegalArgumentException e) {
      addError(e.getMessage(), serviceTaskElement);
    }
    parseExecutionListenersOnScope(serviceTaskElement, activity);
    for (BpmnParseListener parseListener : parseListeners) {
      parseListener.parseServiceTask(serviceTaskElement, scope, activity);
    }
    return activity;
  }

  @Override
  protected void parseRootElement() {
    super.parseRootElement();
    for (ProcessDefinitionEntity processDefinition : getProcessDefinitions()) {
      for (ActivityImpl activity : processDefinition.getActivities()) {
        if (activity.getActivityBehavior() instanceof SmevTaskBehavior) {
          SmevTaskBehavior behavior = (SmevTaskBehavior) activity.getActivityBehavior();
          try {
            behavior.validateTransitions(activity);
          } catch (IllegalArgumentException e) {
            addError(e.getMessage(), null);
          }
        }
      }
    }
  }

}
