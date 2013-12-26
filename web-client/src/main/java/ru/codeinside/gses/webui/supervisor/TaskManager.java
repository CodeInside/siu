/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.supervisor;

import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;

import java.io.Serializable;

final public class TaskManager extends VerticalLayout {

  interface BlockProducer extends Serializable {
    Component createBlock();
  }

  static class ExecutionsBlockProducer implements BlockProducer {
    @Override
    public Component createBlock() {
      return new ExecutionsPanel();
    }
  }

  static class ExceptionsBlockProducer implements BlockProducer {
    @Override
    public Component createBlock() {
      return new ExceptionsPanel();
    }
  }

  Component currentBlock;
  final MenuBar menu = new MenuBar();

  public TaskManager() {
    setSizeFull();
    setMargin(false);
    setSpacing(false);
    menu.setWidth(100, UNITS_PERCENTAGE);
    menu.addStyleName("submenu");
    addComponent(menu);
    setExpandRatio(menu, 0.001f);

    addBlock("Исполняемые процессы", new ExecutionsBlockProducer());
    addBlock("Ошибки исполнения", new ExceptionsBlockProducer());
  }

  void createBlock(final BlockProducer blockProducer) {
    currentBlock = blockProducer.createBlock();
    addComponent(currentBlock);
    setExpandRatio(currentBlock, 0.999f);
  }

  private void addBlock(final String title, final BlockProducer blockProducer) {
    final MenuBar.MenuItem item = menu.addItem(title, new BlockSelector(blockProducer));
    if (currentBlock == null) {
      item.setEnabled(false);
      createBlock(blockProducer);
    }
  }


  class BlockSelector implements MenuBar.Command {
    final BlockProducer blockProducer;

    BlockSelector(BlockProducer blockProducer) {
      this.blockProducer = blockProducer;
    }

    @Override
    public void menuSelected(final MenuBar.MenuItem selectedItem) {
      for (MenuBar.MenuItem i : menu.getItems()) {
        i.setEnabled(i != selectedItem);
      }
      if (currentBlock != null) {
        removeComponent(currentBlock);
      }
      createBlock(blockProducer);
    }
  }

}
