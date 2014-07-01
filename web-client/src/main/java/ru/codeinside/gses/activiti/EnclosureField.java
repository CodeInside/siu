/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import ru.codeinside.gses.vaadin.customfield.CustomField;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.utils.Components;

import java.io.Serializable;


public class EnclosureField extends CustomField implements Field, Serializable {

  public EnclosureField(FileValue attachment) {
    Component attachShowButton = Components.createAttachShowButton(attachment, Flash.app());
    setCompositionRoot(attachShowButton);
    setValue(attachment, true);
  }

  @Override
  public Class<?> getType() {
    return FileValue.class;
  }


}
