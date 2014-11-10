package ru.codeinside.gses.activiti.forms.types;

import java.util.List;
import java.util.Map;

import net.mobidom.bp.beans.Document;
import ru.codeinside.adm.database.FieldBuffer;
import ru.codeinside.gses.activiti.forms.api.definitions.VariableType;

public class DocumentsType implements VariableType<List>{

  @Override
  public List convertFormValueToModelValue(Object propertyValue, String pattern, Map<String, String> values) {
    return (List<Document>) propertyValue;
  }

  @Override
  public void validateParams(String pattern, Map<String, String> values, boolean sandbox) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Class<List> getJavaType() {
    return List.class;
  }

  @Override
  public List convertBufferToModelValue(FieldBuffer fieldBuffer) {
    return null;
  }

  @Override
  public String getName() {
    return "documents";
  }

}
