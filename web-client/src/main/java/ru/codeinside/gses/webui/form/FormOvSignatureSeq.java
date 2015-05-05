/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2015, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.vaadin.ui.Form;
import com.vaadin.ui.TextField;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.webui.gws.TRef;
import ru.codeinside.gses.webui.osgi.TRefRegistryImpl;
import ru.codeinside.gses.webui.wizard.TransitionAction;
import ru.codeinside.gws.api.Client;

import java.util.List;

public class FormOvSignatureSeq extends AbstractFormSeq {

    private final String consumerName;
    private Client consumer;

    public FormOvSignatureSeq(String consumerName) {
        this.consumerName = consumerName;
    }

    @Override
    public String getCaption() {
        return "Подписание данных подписью ОВ";
    }

    /**
     * Заполненные поля в порядке заполнения.
     */
    @Override
    public List<FormField> getFormFields() {
        return null;
    }

    /**
     * Создание формы на основе предыдущей.
     *
     * @param formId
     * @param previous
     */
    @Override
    public Form getForm(FormID formId, FormSeq previous) {
        Form form = new FormSignatureSeq.SignatureForm();
        TextField field = new TextField(getClientBody());
        form.addField("Hello", field);
        return form;
    }

    /**
     * Получить действие перехода
     */
    @Override
    public TransitionAction getTransitionAction() {
        // TODO: реализовать новый екшин
        throw new IllegalStateException("Какая-то ошибочка.");
    }

    private String getClientBody() {
        TRef<Client> clientTRef = new TRefRegistryImpl().getClientByNameAndVersion(consumerName, "");
        if (clientTRef == null) {
            return null;
        }
        return clientTRef.getName();
    }
}
