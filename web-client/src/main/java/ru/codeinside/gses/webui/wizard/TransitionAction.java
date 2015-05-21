/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2015, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.wizard;

/**
 * Дейтсвие перехода между этапами в {@link Wizard}
 */
public interface TransitionAction {

    /**
     * Выполнить действие перехода.
     */
    ResultTransition doIt() throws IllegalStateException;
}
