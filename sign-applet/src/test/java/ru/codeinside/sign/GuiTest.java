/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.sign;

import junit.framework.Assert;
import org.fest.swing.applet.AppletViewer;
import org.fest.swing.core.Robot;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.launcher.AppletLauncher;
import org.fest.swing.launcher.AppletParameter;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.Dimension;
import java.awt.Label;


@Ignore
public class GuiTest {

  private AppletViewer viewer;
  private FrameFixture applet;

  @Before
  public void before() {
    viewer = AppletLauncher.applet(new SignApplet())
      .withParameters(
        AppletParameter.name("width").value("300px"),
        AppletParameter.name("height").value("300px")
      ).start();
    applet = new FrameFixture(viewer);
    applet.resizeTo(new Dimension(300, 300));
  }

  @After
  public void after() throws Exception {
    viewer.unloadApplet();
    applet.cleanUp();
  }


  @Test
  public void test() throws Exception {
    Robot robot = applet.robot;
    Label label = robot.finder().findByName("label0", Label.class);
    robot.waitForIdle();
    Assert.assertEquals("Поиск сертификатов...", label.getText());
  }

}
