/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.themes.BaseTheme;

final public class UserInfoPanel extends HorizontalLayout implements TabSheet.CloseHandler {

  public UserInfoPanel(final String userLogin) {
    setMargin(true);
    setWidth("100%");
    Button logoutLink = new Button("Выход");
    logoutLink.setStyleName(BaseTheme.BUTTON_LINK);
    logoutLink.addListener(new Logout());

    addComponent(new EmployeeInfo(userLogin, logoutLink));
  }

  public static UserInfoPanel addClosableToTabSheet(TabSheet tabSheet, String login) {
    UserInfoPanel uip = new UserInfoPanel(login);
    Tab tab = tabSheet.addTab(uip, login, new ThemeResource("../runo/icons/16/user.png"));
    tab.setDescription("Закройте вкладку чтобы выйти из системы");
    tab.setClosable(true);
    return uip;
  }

  @Override
  public void onTabClose(TabSheet tabsheet, Component tabContent) {
    getApplication().getMainWindow().getApplication().close();
  }

}
