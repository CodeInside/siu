/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.CryptoPro.JCP.tools.resources;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Его зачем то ищет КриптоПРО JCP
 */
public class logger extends Logger {

  public logger(String name, String resourceBundleName) {
    super(name, resourceBundleName);
  }

  @Override
  public Level getLevel() {
    return Level.OFF;
  }

  @Override
  public void setLevel(Level newLevel) throws SecurityException {
  }

  @Override
  public void log(LogRecord record) {
  }

  @Override
  public void log(Level level, String msg) {
  }

  @Override
  public void log(Level level, String msg, Object param1) {
  }

  @Override
  public void log(Level level, String msg, Object[] params) {
  }

  @Override
  public void log(Level level, String msg, Throwable thrown) {
  }

  @Override
  public void logp(Level level, String sourceClass, String sourceMethod, String msg) {
  }

  @Override
  public void logp(Level level, String sourceClass, String sourceMethod, String msg, Object param1) {
  }

  @Override
  public void logp(Level level, String sourceClass, String sourceMethod, String msg, Object[] params) {
  }

  @Override
  public void logp(Level level, String sourceClass, String sourceMethod, String msg, Throwable thrown) {
  }

  @Override
  public void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg) {
  }

  @Override
  public void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg, Object param1) {
  }

  @Override
  public void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg, Object[] params) {
  }

  @Override
  public void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg, Throwable thrown) {
  }

}
