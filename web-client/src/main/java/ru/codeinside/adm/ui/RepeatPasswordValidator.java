/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.google.common.base.Objects;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.ui.PasswordField;

final public class RepeatPasswordValidator extends AbstractValidator {

    private static final long serialVersionUID = 1L;
    final PasswordField password;

    public RepeatPasswordValidator(final PasswordField pass) {
        super("Повтор пароля не совпадает");
        this.password = pass;
    }

    @Override
    public boolean isValid(Object value) {
        return password.isValid() && Objects.equal(value, password.getValue());
    }
}
