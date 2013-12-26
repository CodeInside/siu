/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3572c;


import junit.framework.Assert;
import org.junit.Test;

public class UINGeneratorTest {
  @Test
  public void testChargeGenerateUIN() throws Exception {
    Assert.assertEquals("Ъ20091D0134000000115", UINGenerator.generateChargeUIN("20091d", "013400000011"));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testRegistryCodeTooShort() throws Exception {
    UINGenerator.generateChargeUIN("1", "013400000011");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testRegistryCodeTooLong() throws Exception {
    UINGenerator.generateChargeUIN("123456789", "013400000011");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testNullCode() throws Exception {
    UINGenerator.generateChargeUIN(null, "013400000011");
  }


  @Test (expected = IllegalArgumentException.class)
  public void testNullOrdinalNumber() throws Exception {
    UINGenerator.generateChargeUIN("12356", null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testNullOrdinalNumberTooLong() throws Exception {
    UINGenerator.generateChargeUIN("12356", "0134000000111");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testNullOrdinalNumberTooShort() throws Exception {
    UINGenerator.generateChargeUIN("12356", "01");
  }
}
