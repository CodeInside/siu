/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.activiti.forms.Signatures;
import ru.codeinside.gses.vaadin.customfield.CustomField;
import ru.codeinside.gses.webui.CertificateReader;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.components.sign.SignApplet;
import ru.codeinside.gses.webui.components.sign.SignAppletListener;

final public class FormSignatureField extends CustomField {

  private static final long serialVersionUID = 1L;

  public FormSignatureField(final SignAppletListener appletListener) {
    SignApplet applet = new SignApplet(appletListener);
    byte[] x509 = AdminServiceProvider.get().withEmployee(Flash.login(), new CertificateReader());
    if (x509 != null){
      applet.setSignMode(x509);
    } else {
      applet.setUnboundSignMode();
    }
    setCompositionRoot(applet);
  }

  @Override
  public Class<?> getType() {
    return Signatures.class;
  }

}
