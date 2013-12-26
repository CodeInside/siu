/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import org.activiti.engine.impl.util.xml.Element;

final class BuildException extends Exception {

  final Element element;

  BuildException(final String message, final Element element) {
    super(message);
    this.element = element;
  }

  BuildException(final String message, final Builder.Node node) {
    super(message + ' ' + node.id);
    this.element = node.element;
  }

}
