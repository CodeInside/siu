/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.vaadin;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.AbstractComponent;
import ru.codeinside.gses.vaadin.client.VJsonFormIntegration;

import java.util.Map;

@com.vaadin.ui.ClientWidget(VJsonFormIntegration.class)
@SuppressWarnings("unused") // API
final public class JsonFormIntegration extends AbstractComponent {

  public interface Receiver {
    void onReceive(String value);
  }

  private static final long serialVersionUID = 1L;

  private Resource source = null;
  private boolean go;
  private boolean fixArchiveSupport;
  private Receiver valueReceiver;
  private Receiver errorReceiver;
  private boolean validationMode;

  @Override
  public void paintContent(PaintTarget target) throws PaintException {
    if (source != null) {
      target.addAttribute("src", source);
      if (fixArchiveSupport) {
        target.addAttribute("fixArchiveSupport", true);
      }
    }
    if (validationMode) {
      target.addAttribute("validationMode", true);
    }
    if (go) {
      target.addAttribute("go", true);
      go = false;
    }
  }

  @Override
  public void changeVariables(Object source, Map<String, Object> variables) {
    receive(variables, "jsonError", errorReceiver);
    receive(variables, "jsonValue", valueReceiver);
  }

  private void receive(Map<String, Object> variables, String value, Receiver receiver) {
    if (variables.containsKey(value) && receiver != null) {
      receiver.onReceive(variables.get(value).toString());
    }
  }

  public void fireJson() {
    go = true;
    requestRepaint();
  }

  public void setFixArchiveSupport(boolean _) {
    fixArchiveSupport = _;
    requestRepaint();
  }

  public void setSource(Resource _) {
    source = _;
    requestRepaint();
  }

  public void setValidationMode(boolean _) {
    validationMode = _;
    requestRepaint();
  }

  public void setValueReceiver(Receiver _) {
    valueReceiver = _;
  }

  public void setErrorReceiver(Receiver _) {
    errorReceiver = _;
  }

}
