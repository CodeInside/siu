package net.mobidom.oep.pfrf3622;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ExchangeContext;

public class DummyContext implements ExchangeContext {

    final Map<String, Object> vars = new LinkedHashMap<String, Object>();
    private Object local;

    @Override
    public Object getLocal() {
        return local;
    }

    @Override
    public void setLocal(Object o) {
        local = o;
    }

    @Override
    public Set<String> getVariableNames() {
        return vars.keySet();
    }

    @Override
    public Object getVariable(String name) {
        return vars.get(name);
    }

    @Override
    public void setVariable(String name, Object value) {
        vars.put(name, value);
    }

    @Override
    public boolean isEnclosure(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Enclosure getEnclosure(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addEnclosure(String name, Enclosure enclosure) {
        throw new UnsupportedOperationException();
    }
}