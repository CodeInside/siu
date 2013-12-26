/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import org.activiti.engine.form.FormProperty;
import ru.codeinside.gses.webui.form.FormSeq;

public interface FormPostProcessor {

  boolean needStep(FormProperty property);

  FormSeq createFormSeq(FormProperty property);
}
