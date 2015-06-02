/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2015, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.VerticalLayout;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.SignatureProtocol;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.activiti.forms.Signatures;
import ru.codeinside.gses.webui.form.api.FieldSignatureSource;
import ru.codeinside.gses.webui.wizard.TransitionAction;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FormOvSignatureSeq extends AbstractFormSeq {

  public static final String OV_SIGN = "SoapBodySignatureField";
  public static final String SIGNED_DATA_ID = "SignedSoapBody";
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
      for (Object propertyId : propertyIds) {
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
    form = new OvSignatureForm();

    byte[] signDataBytes = (byte[]) resultTransition.getData();
    String signData = new String(signDataBytes, Charset.forName("UTF-8"));

    addSignedDataToForm(form, signData, SIGNED_DATA_ID);
    addSignatureFieldToForm(form, formId, signData, OV_SIGN);

    return form;
  }

  /**
   * Получить действие перехода
   */
  @Override
  public TransitionAction getTransitionAction(List<FormField> formFields) {
    return new CreateSoapMessageAction(dataAccumulator);
  }

  //TODO дублирование кода из FormSpSignatureSeq
  private void addSignedDataToForm(Form form, String signData, String propertyId) {
    final ReadOnly txt = new ReadOnly(signData);
    txt.setCaption("Подписываемые данные");
    txt.addStyleName("light");
    form.addField(propertyId, txt);
  }

  private void addSignatureFieldToForm(Form form, FormID formId, String signData, String fieldId) {
    byte[] signDataBytes = signData.getBytes();
    boolean[] files = {false};
    String[] ids = {fieldId};

    FormSignatureField signatureField = new FormSignatureField(
        new SignatureProtocol(formId, FormSignatureSeq.SIGNATURE, FormSignatureSeq.SIGNATURE,
            new byte[][] {signDataBytes}, files, ids, form, dataAccumulator));
    signatureField.setCaption(FormSignatureSeq.SIGNATURE);
    signatureField.setRequired(true);

    form.addField(FormSignatureSeq.SIGNATURE, signatureField);
  }

  final public static class OvSignatureForm extends Form implements FieldSignatureSource {

    public OvSignatureForm() {
      this.setDescription("Электронная подпись предназначена для идентификации лица, " +
          "подписавшего электронный документ и является полноценной заменой (аналогом) " +
          "собственноручной подписи в случаях, предусмотренных Гражданским кодексом Российской Федерации " +
          "(часть 1, глава 9, статья 160)");
    }

    @Override
    public String getSignedData() {
      Field f = getField(SIGNED_DATA_ID);
      return (String) f.getValue();
    }

    @Override
    public Signatures getSignatures() {
      Field field = getField(FormSignatureSeq.SIGNATURE);
      Object value = field.getValue();
      return value instanceof Signatures ? (Signatures) value : null;
    }

    @Override
    public void attach() {
      super.attach();
      VerticalLayout vl = (VerticalLayout) getParent();
      vl.setWidth(100, UNITS_PERCENTAGE);
      vl.setHeight(-1, UNITS_PIXELS);
    }
  }
}
