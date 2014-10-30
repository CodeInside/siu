/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside;

import javax.ejb.EJBException;
import java.util.concurrent.Callable;

public class EJBMonkey {

  public static void npe() {
    Exception cause = new NullPointerException();
    throw new EJBException(cause);
  }

  public static void npe(Callable<Exception> callable) {
    Exception cause;
    try {
      cause = callable.call();
    } catch (Exception e) {
      cause = e;
    }
    throw new EJBException(cause);
  }

}
