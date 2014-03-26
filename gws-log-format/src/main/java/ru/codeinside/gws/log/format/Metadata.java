/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.log.format;

import java.util.Date;

/**
 * Created by alexey on 12.03.14.
 */
public class Metadata {
  public String error;
  public Date date;
  public String processInstanceId;
  public boolean client;
  public Pack clientRequest;
  public Pack clientResponse;
  public Pack serverRequest;
  public Pack serverResponse;
  public String componentName;

}
