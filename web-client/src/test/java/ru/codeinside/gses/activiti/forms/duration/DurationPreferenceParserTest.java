/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.duration;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DurationPreferenceParserTest {
  DurationPreferenceParser parser;

  @Before
  public void setUp() throws Exception {
    parser = new DurationPreferenceParser();

  }

  @Test
  public void testParseTaskPeriods() throws Exception {
    DurationPreference preference = parser.parseTaskPreference("1/25/30");
    assertEquals(1, preference.notificationPeriod);
    assertEquals(25, preference.executionPeriod);
    assertEquals(30, preference.inactivePeriod);

    DurationPreference processPreference = parser.parseProcessPreference("1/25");
    assertEquals(1, processPreference.notificationPeriod);
    assertEquals(25, processPreference.executionPeriod);
    assertEquals(0, processPreference.inactivePeriod); // в поле используется примити
  }

  @Test(expected = IllegalDurationExpression.class)
  public void testParseEmptyExpression() throws Exception {
    parser.parseTaskPreference("");
  }

  @Test(expected = IllegalDurationExpression.class)
  public void testParseWithAlphaExpression() throws Exception {
    parser.parseTaskPreference("1/qwe/12");
  }

  @Test(expected = IllegalDurationExpression.class)
  public void testParseEmptyExpressionForProcess() throws Exception {
    parser.parseProcessPreference("");
  }

  @Test(expected = IllegalDurationExpression.class)
  public void testParseWithAlphaExpressionForProcess() throws Exception {
    parser.parseProcessPreference("1/qwe/12");
  }
}