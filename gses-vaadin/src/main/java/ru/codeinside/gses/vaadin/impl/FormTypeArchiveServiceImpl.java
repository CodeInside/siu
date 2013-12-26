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

import ru.codeinside.gses.vaadin.FieldFormType;
import ru.codeinside.gses.vaadin.FieldFormTypeListener;
import ru.codeinside.gses.vaadin.FieldFormTypeService;

public class FormTypeArchiveServiceImpl implements FieldFormTypeService {

	private List<FieldFormType> listFieldFormType = new ArrayList<FieldFormType>();

	private ArrayList<FieldFormTypeListener> listeners = new ArrayList<FieldFormTypeListener>();

	@SuppressWarnings("unchecked")
	public void registerFieldTypes(Iterable<FieldFormType> fieldTypes) {
		for (FieldFormType fieldFormType : fieldTypes) {
			listFieldFormType.add(fieldFormType);
		}
		for (FieldFormTypeListener listener : (ArrayList<FieldFormTypeListener>) listeners.clone()) {
			listener.fieldTypesRegistered(this, fieldTypes);
		}
	}

	@SuppressWarnings("unchecked")
	public void unregisterFieldTypes(Iterable<FieldFormType> fieldTypes) {
		for (FieldFormType fieldFormType : fieldTypes) {
			listFieldFormType.remove(fieldFormType);
		}
		for (FieldFormTypeListener listener : (ArrayList<FieldFormTypeListener>) listeners.clone()) {
			listener.fieldTypesUnregistered(this, fieldTypes);
		}
	}

	public List<FieldFormType> getFormTypes() {
		return Collections.unmodifiableList(listFieldFormType);
	}

	public void addListener(FieldFormTypeListener listener) {
		listeners.add(listener);
	}

	public void removeListener(FieldFormTypeListener listener) {
		listeners.remove(listener);
	}

}
