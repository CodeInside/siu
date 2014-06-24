/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses;

final public class API {

  final public static String ENABLE_CLIENT_LOG = "HttpTransportPipe.dump";
  final public static String PRODUCTION_MODE = "productionMode";
  final public static String LOG_DEPTH = "logDepth";
  final public static int DEFAULT_LOG_DEPTH = 14;
  final public static String LOG_ERRORS = "logErrors";
  final public static String LOG_STATUS = "logStatus";
  final public static String SKIP_LOG_IPS = "skipLogIps";
  final public static String EMAIL_TO = "emailTo";
  final public static String RECEIVER_NAME = "receiverName";
  final public static String EMAIL_FROM = "emailFrom";
  final public static String SENDER_LOGIN = "senderLogin";
  final public static String SENDER_NAME = "senderName";
  final public static String PASSWORD = "password";
  final public static String HOST = "host";
  final public static String PORT = "port";
  final public static String TLS = "tls";

  final public static String JSON_FORM = "json-form";

  private API() {

  }
}
