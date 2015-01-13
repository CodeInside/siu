package server.mcsv1002;

import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ReceiptContext;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class DummyContext implements ReceiptContext {

  final public Map<String, Object> vars = new LinkedHashMap<String, Object>();
  final public Map<String, Enclosure> enclosures = new LinkedHashMap<String, Enclosure>();

  @Override
  public Set<String> getPropertyNames() {
    return vars.keySet();
  }

  @Override
  public Set<String> getAllPropertyNames() {
    return getPropertyNames();
  }

  @Override
  public Set<String> getEnclosureNames() {
    return enclosures.keySet();
  }

  @Override
  public Set<String> getAllEnclosureNames() {
    return getEnclosureNames();
  }

  @Override
  public Object getVariable(String name) {
    return vars.get(name);
  }

  @Override
  public Object getVariableByFullName(String name) {
    return getVariable(name);
  }

  @Override
  public Enclosure getEnclosure(String name) {
    return enclosures.get(name);
  }

  @Override
  public Enclosure getEnclosureByFullName(String name) {
    return getEnclosure(name);
  }

  @Override
  public void setEnclosure(String name, Enclosure enclosure) {
    enclosures.put(name, enclosure);
  }

  @Override
  public void setVariable(String name, Object value) {
    vars.put(name, value);
  }

  @Override
  public String getBid() {
    return "1";
  }

}
