/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.vaadin;

import java.util.List;

public interface FieldFormTypeService {

	public void registerFieldTypes(Iterable<FieldFormType> fieldTypes);

	public void unregisterFieldTypes(Iterable<FieldFormType> fieldTypes);

	public List<FieldFormType> getFormTypes();

	public void addListener(FieldFormTypeListener listener);

	public void removeListener(FieldFormTypeListener listener);

}
