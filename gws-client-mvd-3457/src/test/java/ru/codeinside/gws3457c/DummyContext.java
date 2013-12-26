/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3457c;

import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ExchangeContext;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

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
