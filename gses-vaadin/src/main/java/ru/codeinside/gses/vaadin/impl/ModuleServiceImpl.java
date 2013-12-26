/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.vaadin.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.codeinside.gses.vaadin.Module;
import ru.codeinside.gses.vaadin.ModuleService;
import ru.codeinside.gses.vaadin.ModuleServiceListener;

public class ModuleServiceImpl implements ModuleService {

    private ArrayList<Module> modules = new ArrayList<Module>();
    
    private ArrayList<ModuleServiceListener> listeners = new ArrayList<ModuleServiceListener>();

    @SuppressWarnings("unchecked")
    public synchronized void registerModule(Module module) {
        modules.add(module);
        for (ModuleServiceListener listener : (ArrayList<ModuleServiceListener>) listeners.clone()) {
            listener.moduleRegistered(this, module);
        }
    }
    
    @SuppressWarnings("unchecked")
    public synchronized void unregisterModule(Module module) {
        modules.remove(module);
        for (ModuleServiceListener listener : (ArrayList<ModuleServiceListener>) listeners.clone()) {
            listener.moduleUnregistered(this, module);
        }
    }
    
    public List<Module> getModules() {
        return Collections.unmodifiableList(modules);
    }
    
    public synchronized void addListener(ModuleServiceListener listener) {
        listeners.add(listener);
    }
    
    public synchronized void removeListener(ModuleServiceListener listener) {
        listeners.remove(listener);
    }
}

