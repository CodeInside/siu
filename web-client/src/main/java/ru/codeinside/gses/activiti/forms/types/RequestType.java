package ru.codeinside.gses.activiti.forms.types;

import java.util.Map;
import java.util.logging.Logger;

import net.mobidom.bp.beans.Обращение;
import ru.codeinside.adm.database.FieldBuffer;
import ru.codeinside.gses.activiti.forms.api.definitions.VariableType;

public class RequestType implements VariableType<Обращение> {

  private static final long serialVersionUID = -7202659332728536669L;

  static Logger logger = Logger.getLogger(RequestType.class.getName());

  @Override
  public Обращение convertFormValueToModelValue(Object propertyValue, String pattern, Map<String, String> values) {

    logger.info("convertFormValueToModelValue = " + propertyValue);
   
    return (Обращение) propertyValue;
  }

  @Override
  public void validateParams(String pattern, Map<String, String> values, boolean sandbox) {
    logger.info(String.format("validateParams pattern = %s, values = %s, sandbox = %s", pattern, values, sandbox));
  }

  @Override
  public Class<Обращение> getJavaType() {
    return Обращение.class;
  }

  @Override
  public Обращение convertBufferToModelValue(FieldBuffer fieldBuffer) {
    logger.info("convertBufferToModelValue = " + fieldBuffer);
    return null;
  }

  @Override
  public String getName() {
    return "request";
  }

}
