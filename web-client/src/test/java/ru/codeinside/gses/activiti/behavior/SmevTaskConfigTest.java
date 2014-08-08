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
import ru.codeinside.adm.database.SmevTaskStrategy;

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
      assertEquals("Пропущено обязательное поле {потребитель}, альтернативные названия {модуль | компонент}", e.getMessage());
    }
  }

  @Test
  public void value() {
    SmevTaskConfig config = new SmevTaskConfig(ImmutableList.of(
      new FieldDeclaration("модуль ", "", new FixedValue("fss")),
      new FieldDeclaration("страте ГиЯ", "", new FixedValue("опрос")),
      new FieldDeclaration("задержкаОпроса", "", new FixedValue(10))
    ));

    assertSame("fss", config.consumer.getValue(null));
    assertSame(SmevTaskStrategy.PING, config.strategy.getValue(null));
    assertSame(10, config.pingInterval.getValue(null));
  }

  @Test
  public void verify() {
    try {
      new SmevTaskConfig(ImmutableList.of(
        new FieldDeclaration("модуль ", "", new FixedValue(123)),
        new FieldDeclaration("стратегия", "", new FixedValue("опрос")),
        new FieldDeclaration("xyz", "", null)
      ));
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Неизвестные поля {xyz}", e.getMessage());
    }
  }

}