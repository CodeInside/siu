package ru.codeinside.gses.activiti.forms.types;

import java.util.Map;
import java.util.logging.Logger;

import net.mobidom.bp.beans.Request;
import ru.codeinside.adm.database.FieldBuffer;
import ru.codeinside.gses.activiti.forms.api.definitions.VariableType;

public class RequestType implements VariableType<Request> {

  private static final long serialVersionUID = -7202659332728536669L;

  static Logger logger = Logger.getLogger(RequestType.class.getName());

  @Override
  public Request convertFormValueToModelValue(Object propertyValue, String pattern, Map<String, String> values) {

    logger.info("convertFormValueToModelValue = " + propertyValue);
   
    return (Request) propertyValue;
  }

  @Override
  public void validateParams(String pattern, Map<String, String> values, boolean sandbox) {
    logger.info(String.format("validateParams pattern = %s, values = %s, sandbox = %s", pattern, values, sandbox));
  }

  @Override
  public Class<Request> getJavaType() {
    return Request.class;
  }

  @Override
  public Request convertBufferToModelValue(FieldBuffer fieldBuffer) {
    logger.info("convertBufferToModelValue = " + fieldBuffer);
    return null;
  }

  @Override
  public String getName() {
    return "request";
  }

}
