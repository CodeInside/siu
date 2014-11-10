package ru.codeinside.gses.activiti.forms.types;

import java.util.Map;

import ru.codeinside.adm.database.FieldBuffer;
import ru.codeinside.gses.activiti.forms.api.definitions.VariableType;

public class DocumentsForRequestType implements VariableType<String> {

  private static final long serialVersionUID = -3553853668879662995L;

  @Override
  public String convertFormValueToModelValue(Object propertyValue, String pattern, Map<String, String> values) {
    return (String) propertyValue;
  }

  @Override
  public void validateParams(String pattern, Map<String, String> values, boolean sandbox) {
  }

  @Override
  public Class<String> getJavaType() {
    return String.class;
  }

  @Override
  public String convertBufferToModelValue(FieldBuffer fieldBuffer) {
    return null;
  }

  @Override
  public String getName() {
    return "documentsForRequest";
  }

}
