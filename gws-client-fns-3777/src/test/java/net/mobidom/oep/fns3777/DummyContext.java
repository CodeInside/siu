/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package net.mobidom.oep.fns3777;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ExchangeContext;

public class DummyContext implements ExchangeContext {

    final Map<String, Object> vars = new LinkedHashMap<String, Object>();
    private Object local;
    final Map<String, Enclosure> enclosures = new LinkedHashMap<String, Enclosure>();


    @Override
    public Object getLocal() {
        return local;
    }

    @Override
    public void setLocal(Object o) {
        local = o;
    }

    @Override
    public Object getVariable(String name) {
        return vars.get(name);
    }

    @Override
    public void setVariable(String name, Object value) {
        vars.put(name, value);
    }

    // @Override
    // public String createDocument(String name, InputStream inputStream) {
    // throw new UnsupportedOperationException();
    // }

    public boolean isEnclosure(String name) {
        // TODO Auto-generated method stub
        return false;
    }

    public Enclosure getEnclosure(String name) {
        // TODO Auto-generated method stub
        return enclosures.get(name);
    }

    public void addEnclosure(String name, Enclosure enclosure) {
        // TODO Auto-generated method stub
        enclosures.put(name, enclosure);

    }

    public Set<String> getVariableNames() {
        return vars.keySet();
    }

}
