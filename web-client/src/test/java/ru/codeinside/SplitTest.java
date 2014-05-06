/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class SplitTest {
  @Test
  public void testRegex() {
    assertEquals(Arrays.asList("Жил", "был", "пёс.", "Барбос", "127.0.0.1"),
      Arrays.asList("Жил был пёс.\n Барбос; 127.0.0.1 ".split("[,;\\s]+"))
    );
  }
}
