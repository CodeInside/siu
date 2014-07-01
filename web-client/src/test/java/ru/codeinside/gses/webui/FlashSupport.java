/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlashSupport {

  public static void setLogin(String login) {
    Flasher flasher = mock(Flasher.class);
    Flash.set(flasher);
    when(flasher.getLogin()).thenReturn(login);
  }
}
