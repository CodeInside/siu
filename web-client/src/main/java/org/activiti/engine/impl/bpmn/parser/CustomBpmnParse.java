package org.activiti.engine.impl.bpmn.parser;

import org.activiti.engine.impl.form.StartFormHandler;
import org.activiti.engine.impl.form.TaskFormHandler;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.impl.util.ReflectUtil;
import org.activiti.engine.impl.util.xml.Element;
import ru.codeinside.gses.activiti.forms.CustomStartFormHandler;
import ru.codeinside.gses.activiti.forms.CustomTaskFormHandler;

import java.util.List;

/**
 * Класс нужен лишь для того чтобы заменить StartFormHandler и TaskFormHandler,
 * так как в API Activiti это не предусмотрено.
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


}
