/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.behavior;

import com.google.common.collect.ImmutableList;
import org.activiti.engine.impl.bpmn.parser.FieldDeclaration;
import org.activiti.engine.impl.el.FixedValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public class SmevTaskConfigTest {

  @Test
  public void required() {
    try {
      new SmevTaskConfig(ImmutableList.of(
        new FieldDeclaration("модуль ", "", null)
      ));
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Пропущено обязательное поле {потребитель | модуль | компонент}", e.getMessage());
    }
  }

  @Test
  public void value() {
    FixedValue pingInterval = new FixedValue(10);
    FixedValue module = new FixedValue("fss");
    FixedValue strategy = new FixedValue("опрос");

    SmevTaskConfig config = new SmevTaskConfig(ImmutableList.of(
      new FieldDeclaration("модуль ", "", module),
      new FieldDeclaration("страте ГиЯ", "", strategy),
      new FieldDeclaration("задержкаОпроса", "", pingInterval)
    ));

    assertSame(module, config.consumer);
    assertSame(strategy, config.strategy);
    assertSame(pingInterval, config.pingInterval);
  }

  @Test
  public void verify() {
    try {
      new SmevTaskConfig(ImmutableList.of(
        new FieldDeclaration("модуль ", "", new FixedValue(1)),
        new FieldDeclaration("стратегия", "", new FixedValue(2)),
        new FieldDeclaration("xyz", "", null)
      ));
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Не известное поле {xyz}", e.getMessage());
    }
  }

}