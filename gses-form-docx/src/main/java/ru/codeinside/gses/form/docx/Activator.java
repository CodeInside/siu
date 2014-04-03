/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.form.docx;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import ru.codeinside.gses.form.FormConverter;

final public class Activator implements BundleActivator {

  ServiceRegistration registration;
  ConverterProxy converter;

  @Override
  public void start(final BundleContext context) throws Exception {
    converter = new ConverterProxy();
    registration = context.registerService(FormConverter.class.getName(), converter, null);
  }

  @Override
  public void stop(final BundleContext context) throws Exception {
    if (converter != null) {
      converter.close();
    }

    if (registration != null) {
      registration.unregister();
    }
  }
}
