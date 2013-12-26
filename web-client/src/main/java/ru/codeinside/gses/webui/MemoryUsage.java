/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import ru.codeinside.gses.webui.eventbus.SizeEvent;

final public class MemoryUsage extends CustomComponent {

  final TabSheet tabs;
  final TextArea sizeErr;

  public MemoryUsage(TabSheet tabs) {
    this.tabs = tabs;
    sizeErr = new TextArea();
    sizeErr.setStyleName("debug-panel");
    sizeErr.setSizeFull();
    sizeErr.setReadOnly(true);
    setCompositionRoot(sizeErr);
    setSizeFull();

    Flash.router().addListener(SizeEvent.class, this, "onSizeEvent");
    tabs.addTab(this, "Память").setDescription("Отладочная информация");
  }

  @SuppressWarnings("unused") // EventBus API
  public void onSizeEvent(SizeEvent event) {
    sizeErr.setReadOnly(false);
    sizeErr.setValue(event.error == null ? "Нет ошибок сериализации" : event.error);
    sizeErr.setReadOnly(true);
    tabs.getTab(this).setCaption((event.error == null ? "" : "!") + event.size);
  }

}
