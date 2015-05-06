/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2015, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.SignatureProtocol;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.webui.wizard.TransitionAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FormOvSignatureSeq extends AbstractFormSeq {
    private Form form;

    public FormOvSignatureSeq(DataAccumulator dataAccumulator) {
        super(dataAccumulator);
    }

    @Override
    public String getCaption() {
        return "Подписание тела запроса подписью ОВ";
    }

    /**
     * Заполненные поля в порядке заполнения.
     */
    @Override
    public List<FormField> getFormFields() {
        List<FormField> fields = new ArrayList<FormField>();
        if (form != null) {
            Collection<?> propertyIds = form.getItemPropertyIds();
            for (Object propertyId: propertyIds) {
                Field field = form.getField(propertyId);
                fields.add(new BasicFormField(propertyId, field.getCaption(), field.getValue()));
            }
        }
        return Collections.unmodifiableList(fields);
    }

    /**
     * Создание формы на основе предыдущей.
     *
     * @param formId
     * @param previous
     */
    @Override
    public Form getForm(FormID formId, FormSeq previous) {
        byte[] signData = (byte[]) resultTransition.getData();

        form = new FormSignatureSeq.SignatureForm();

        String signDataFieldId = "SignDataField";
        ReadOnly field = new ReadOnly("Подписываемые данные", new String(signData), true);
        field.addStyleName("light");

        FormSignatureField signatureField = new FormSignatureField(
                new SignatureProtocol(formId, FormSignatureSeq.SIGNATURE, FormSignatureSeq.SIGNATURE,
                        new byte[][]{signData}, new boolean[]{false}, new String[]{signDataFieldId}, form));
        signatureField.setCaption(FormSignatureSeq.SIGNATURE);
        signatureField.setRequired(true);

        form.addField(signDataFieldId, field);
        form.addField(FormSignatureSeq.SIGNATURE, signatureField);
        return form;
    }

    /**
     * Получить действие перехода
     */
    @Override
    public TransitionAction getTransitionAction() {
        return new CreateSoapMessageAction(dataAccumulator);
    }
}
