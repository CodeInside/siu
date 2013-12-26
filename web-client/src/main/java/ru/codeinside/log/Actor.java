/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.log;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class Actor implements Serializable {

  private static final long serialVersionUID = 1L;

  private String name;
  private String ip;
  private String os;
  private String browser;


  protected Actor() {
  }

  public Actor(final String name, final String ip, final String os, final String browser) {
    this.name = name;
    this.ip = ip;
    this.os = os;
    this.browser = browser;
  }

  public String getName() {
    return name;
  }

  public String getIp() {
    return ip;
  }

  public String getOs() {
    return os;
  }


  public String getBrowser() {
    return browser;
  }

  @Override
  public String toString() {
    return name == null ? ip : name;
  }
}
