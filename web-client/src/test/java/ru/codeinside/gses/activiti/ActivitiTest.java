/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import org.junit.Test;
import ru.codeinside.gws.api.Enclosure;

import static org.junit.Assert.assertEquals;
import static ru.codeinside.gses.activiti.Activiti.getAttachmentName;

public class ActivitiTest {

  @Test
  public void fileName() throws Exception {
    assertEquals(null, Activiti.getFileName(null));
    assertEquals(null, Activiti.getFileName(""));
    assertEquals(null, Activiti.getFileName("//"));
    assertEquals("a", Activiti.getFileName("//a"));
    assertEquals("a", Activiti.getFileName("a"));
  }

  @Test
  public void attachmentName() throws Exception {
    byte[] content = new byte[0];
    assertEquals("x", getAttachmentName(new Enclosure("x", content), "?"));
    assertEquals("x", getAttachmentName(new Enclosure("/a/x", content), "?"));
    assertEquals("y", getAttachmentName(new Enclosure("x", "y", content), "?"));
    assertEquals("y", getAttachmentName(new Enclosure("", "y", content), "?"));
    assertEquals("?", getAttachmentName(new Enclosure("/a/", content), "?"));
    assertEquals("?", getAttachmentName(new Enclosure("", "", content), "?"));
  }
}
