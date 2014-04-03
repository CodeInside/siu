/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.osgi;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import ru.codeinside.gses.form.FormConverter;
import ru.codeinside.gses.form.FormData;

import java.util.logging.Logger;

public class FormConverterCustomicer implements ServiceTrackerCustomizer {

  static BundleContext BUNDLE;
  static volatile ServiceReference REF;

  final Logger logger = Logger.getLogger(getClass().getName());

  FormConverterCustomicer(BundleContext bundleContext) {
    BUNDLE = bundleContext;
  }

  @Override
  public Object addingService(ServiceReference serviceReference) {
    REF = serviceReference;
    return null;
  }

  @Override
  public void modifiedService(ServiceReference serviceReference, Object o) {
  }

  @Override
  public void removedService(ServiceReference serviceReference, Object o) {
    REF = null;
  }

  public static boolean convert(FormData formData) {
    ServiceReference ref = REF;
    if (ref != null) {
      FormConverter converter = (FormConverter) BUNDLE.getService(ref);
      try {
        converter.createForm(formData);
        return true;
      } finally {
        BUNDLE.ungetService(ref);
      }
    }
    return false;
  }
}
