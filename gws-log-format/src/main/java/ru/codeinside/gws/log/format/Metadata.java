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

  final public static String METADATA = "metadata";
  final public static String HTTP_SEND = "http-send";
  final public static String HTTP_RECEIVE = "http-receive";

  /**
   * Номер заявки.
   */
  public Long bid;
  public String error;
  public Date date;
  public String processInstanceId;
  public boolean client;
  public Pack send;
  public Pack receive;
  public String componentName;

}
