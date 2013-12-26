/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.parser;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Разбор файла фикстур с данными о услугах и процедурах
 */
public class ServiceFixtureParser {
  private final LinkedList<Row> stack;

  public ServiceFixtureParser() {
    stack = new LinkedList<Row>();
  }

  public void loadFixtures(InputStream is, ServiceFixtureParser.PersistenceCallback callback) throws IOException {
    final Splitter propertySplitter = Splitter.on(':');
    final BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
    String line;
    int lineNumber = 0;
    while ((line = reader.readLine()) != null) {
      lineNumber++;
      int level = startIndex(line);
      if (line.startsWith("#") || level < 0) {
        continue;
      }
      final ArrayList<String> props = Lists.newArrayList(propertySplitter.split(line.substring(level)));
      final String name = StringUtils.trimToNull(props.get(0)).replace("<br/>", "\n");
      if (name == null) {
        throw new IllegalStateException("Пропущено имя( строка:" + lineNumber + ")");
      }
      final Long regCode;
      try {
        regCode = Long.parseLong(props.get(1));
      } catch (NumberFormatException e) {
        throw new IllegalStateException("Пропущен код ( строка:" + lineNumber + "): " + name);
      }
      final boolean isProc = level > 0;
      Row parent = getParentRow(level);
      if (!isProc) {
        long servId = callback.onServiceComplete(name, regCode);
        stack.addLast(new Row(level, servId));
      }
      if (isProc && parent != null) {
        callback.onProcedureComplete(name, regCode, parent.id);
      }
      if (isProc && parent == null) {
        throw new IllegalStateException("Процедура без услуги( строка:" + lineNumber + "): " + name);
      }
    }
  }

  private Row getParentRow(int level) {
    Row parent = stack.isEmpty() ? null : stack.getLast();
    while (!stack.isEmpty()) {
      if (parent != null && (parent.level < level)) {
        break;
      }
      stack.removeLast();
      parent = stack.isEmpty() ? null : stack.getLast();
    }
    return parent;
  }

  private int startIndex(String line) {
    int N = line.length();
    for (int i = 0; i < N; i++) {
      char c = line.charAt(i);
      if (c != ' ') {
        return i;
      }
    }
    return -1;
  }

  public interface PersistenceCallback {
    Long onServiceComplete(String orgName, Long regCode);

    void onProcedureComplete(String name, Long regCode, long orgId);
  }

  final static class Row {
    final int level;
    final long id;

    Row(int level, long id) {
      this.level = level;
      this.id = id;
    }
  }


}

