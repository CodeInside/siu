/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import ru.codeinside.gses.webui.components.api.Changer;

public final class TabChanger implements Changer {

  private static final long serialVersionUID = 1L;
  private final TabSheet owner;

  private Component current;
  private Component previous;

  public TabChanger(final TabSheet owner) {
    this.owner = owner;
  }

  @Override
  public void set(Component newComponent, String name) {
    clear();
    owner.addTab(newComponent, name);
    owner.setSelectedTab(newComponent);
    current = newComponent;
  }

  @Override
  public void clear() {
    if (current != null) {
      final Tab tab = owner.getTab(current);
      if (tab != null) {
        owner.removeTab(tab);
      }
      current = null;
      previous = null;
    }
  }

  @Override
  public void change(final Component newComponent) {
    final Component oldComponent = current;
    owner.replaceComponent(oldComponent, newComponent);
    previous = oldComponent;
    current = newComponent;
  }

  @Override
  public void back() {
    final Component oldComponent = current;
    final Component newComponent = previous;
    owner.replaceComponent(oldComponent, newComponent);
    previous = null;
    current = newComponent;
  }
}