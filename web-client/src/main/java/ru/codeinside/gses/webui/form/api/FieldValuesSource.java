/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form.api;

import java.util.Map;

/**
 * Источник значений формы
 */
public interface FieldValuesSource {
  Map<String, Object> getFieldValues();
}
