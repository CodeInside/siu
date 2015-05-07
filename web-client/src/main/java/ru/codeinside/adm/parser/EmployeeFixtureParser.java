/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.parser;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.adm.database.Role;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang.StringUtils.trimToNull;

/**
 * Разбор файла фикстур с данными о сотрудниках и организициях
 */
public class EmployeeFixtureParser {
  private final Splitter groupSplitter;
  private final LinkedList<Row> stack;
  private final Pattern snilsPattern = Pattern.compile("\\d{11}");

  public EmployeeFixtureParser() {
    groupSplitter = Splitter.on(',').trimResults().omitEmptyStrings();
    stack = new LinkedList<Row>();
  }

  public void loadFixtures(InputStream is, EmployeeFixtureParser.PersistenceCallback callback) throws IOException {
    final Splitter propertySplitter = Splitter.on(';');
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
      final String name = StringUtils.trimToNull(props.get(0));
      if (name == null) {
        if (props.size() == 1) {
          continue;
        }
        throw new IllegalStateException("Пропущено имя( строка:" + lineNumber + ")");
      } else if (name.length() >= 255) {
        throw new IllegalStateException("Длина имени не должна превышать 255 символов (строка: "+lineNumber+")");
      }
      final boolean isOrg = props.size() <= 2;
      Row parent = getParentRow(level);
      final Set<String> groups = parseGroups(props, getGroupPropertyIndex(isOrg));
      if (isOrg) {
        long orgId = callback.onOrganizationComplete(name, groups, parent != null ? parent.id : null);
        stack.addLast(new Row(level, orgId));
      }
      if (!isOrg && parent != null) {
        String pwd = defaultIfEmpty(trimToNull(props.get(2)), null);
        Set<Role> roles = parseRoles(groupSplitter, lineNumber, props.get(3));
        String snils = "";
        if (props.size() == 6) {
          String snilsValue = defaultIfEmpty(trimToNull(props.get(5)), null);
          Matcher snilsMatcher = snilsPattern.matcher(snilsValue);
          if (snilsMatcher.matches()) {
            snils = snilsValue.replaceAll("\\D+", "");
          }
        }
        callback.onUserComplete(StringUtils.trim(props.get(1)), pwd, name, snils, parent.id, roles, groups);
      }
      if (!isOrg && parent == null) {
        throw new IllegalStateException("Пользователь без организации(строка:" + lineNumber + ").");
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

  private int getGroupPropertyIndex(boolean org) {
    return org ? 1 : 4;
  }

  private Set<String> parseGroups(ArrayList<String> props, int propertyGroupIndex) {
    final Set<String> groups;
    if (props.size() > propertyGroupIndex) {
      groups = Sets.newTreeSet(groupSplitter.split(props.get(propertyGroupIndex)));
    } else {
      groups = Collections.emptySet();
    }
    return groups;
  }

  private Set<Role> parseRoles(Splitter groupSplitter, int lineNumber, String rolesString) {
    Set<String> roleNames = Sets.newTreeSet(groupSplitter.split(rolesString));
    Set<Role> roles = EnumSet.noneOf(Role.class);
    for (String roleName : roleNames) {
      try {
        roles.add(Role.valueOf(roleName));
      } catch (Exception e) {
        throw new IllegalStateException("Неизвестная роль ( строка:" + lineNumber + "):" + roleName);
      }
    }

    return roles;
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
    Long onOrganizationComplete(String orgName, Set<String> groups, Long ownerId);

    void onUserComplete(String login, String pwd, String name, String snils, long orgId, Set<Role> roles, Set<String> groups);
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

