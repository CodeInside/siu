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
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.activiti.forms.Signatures;
import ru.codeinside.gses.webui.form.api.FieldSignatureSource;
import ru.codeinside.gses.webui.wizard.TransitionAction;

import java.util.List;

public class FormOvSignatureSeq extends AbstractFormSeq {

  public static final String OV_SIGN = "_SoapBodySignatureField";
  public static final String SIGNED_DATA_ID = "_SignedSoapBody";
  public static final String REQUEST_ID = "_ClientRequestEntityId";

  private Form form;

  public FormOvSignatureSeq(DataAccumulator dataAccumulator) {
    super(dataAccumulator);
  }

  @Override
  public String getCaption() {
    return "Подписание тела запроса подписью ОВ";
  }

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
    SignData signData = (SignData) resultTransition.getData();

    form = new OvSignatureForm(dataAccumulator.getServiceName(), signData);
    FormUtils.addSignedDataToForm(form, signData, SIGNED_DATA_ID);
    FormUtils.addSignatureFieldToForm(form, formId, signData, previous.getFormFields(), OV_SIGN, dataAccumulator);

    return form;
  }

  @Override
  public void backwardAction() {
    if (dataAccumulator != null) {
      dataAccumulator.setSoapMessage(null);
    }
  }

  @Override
  public TransitionAction getTransitionAction() {
    if (dataAccumulator.getServiceName() != null) {
      // срабатывает для потребителя
      return new CreateSoapMessageAction(dataAccumulator);
    } else if (dataAccumulator.getRequestType() != null) {
      // срабатывет для поставщика
      return new CreateResultSoapMessageAction(dataAccumulator);
    } else {
      throw new IllegalStateException("Ошибка в маршруте");
    }
  }

  final public static class OvSignatureForm extends Form implements FieldSignatureSource {

    private String serviceName;
    private SignData signData;

    public OvSignatureForm(String serviceName, SignData signData) {
      this.setDescription("Электронная подпись предназначена для идентификации лица, " +
          "подписавшего электронный документ и является полноценной заменой (аналогом) " +
          "собственноручной подписи в случаях, предусмотренных Гражданским кодексом Российской Федерации " +
          "(часть 1, глава 9, статья 160)");
      this.serviceName = serviceName;
      this.signData = signData;
    }

    public String getEntityFieldId() {
      return serviceName + FormOvSignatureSeq.REQUEST_ID;
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
    public SignData getSignData() {
      return signData;
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
