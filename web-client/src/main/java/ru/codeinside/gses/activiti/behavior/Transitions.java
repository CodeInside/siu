/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.behavior;

import com.google.common.base.Predicate;
import org.activiti.engine.impl.pvm.PvmTransition;

final public class Transitions {

  public static Predicate<PvmTransition> withPrefix(final String prefix) {
    return new Predicate<PvmTransition>() {
      @Override
      public boolean apply(PvmTransition transition) {
        return transition.getId().toLowerCase().startsWith(prefix);
      }
    };
  }

  private Transitions() {
  }
}
