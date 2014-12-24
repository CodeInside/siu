package server.mcsv1002;

import ru.codeinside.gws.api.DeclarerContext;
import ru.codeinside.gws.api.Enclosure;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class DeclarerContextStub implements DeclarerContext {

  public String id;
  public Set<String> vars = new HashSet<String>();
  public Set<String> enclosures = new HashSet<String>();
  public Map<String, Object> values = new HashMap<String, Object>();

  @Override
  public Set<String> getPropertyNames() {
    return vars;
  }

  @Override
  public boolean isRequired(String propertyName) {
    return false;
  }

  @Override
  public boolean isEnclosure(String propertyName) {
    return enclosures.contains(propertyName);
  }

  @Override
  public String getType(String propertyName) {
    return "string";
  }

  @Override
  public void setValue(String propertyName, Object value) {
    values.put(propertyName, value);
  }

  @Override
  public void addEnclosure(String propertyName, Enclosure enclosure) {
    values.put(propertyName, enclosure);
  }

  @Override
  public String declare() {
    return id;
  }

  @Override
  public String declare(String tag, String declarant) {
    return id;
  }

  public Object getVariable(String name) {
    return values.get(name);
  }
}
