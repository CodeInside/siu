/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.VerticalLayout;
import ru.codeinside.gses.activiti.FileValue;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.SignatureProtocol;
import ru.codeinside.gses.activiti.VariableToBytes;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.activiti.forms.Signatures;
import ru.codeinside.gses.webui.form.api.FieldSignatureSource;
import ru.codeinside.gses.webui.wizard.TransitionAction;

import java.util.List;

final public class FormSignatureSeq extends AbstractFormSeq {

  public static final String SIGNATURE = "ЭЦП";
  private static final long serialVersionUID = 1L;

  public FormSignatureSeq(DataAccumulator dataAccumulator) {
    super(dataAccumulator);
  }

  @Override
  public String getCaption() {
    return "Подписание данных личной ЭЦП";
  }

  @Override
  public List<FormField> getFormFields() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Form getForm(final FormID formId, final FormSeq previous) {

    final List<FormField> formFields = previous.getFormFields();

    final Form form = new SignatureForm();

    int n = 1;
    for (final FormField formField : formFields) {
      Object valueObject = formField.getValue();
      if (valueObject == null) {
        valueObject = "";
      } else if (valueObject instanceof Boolean) {
        valueObject = ((Boolean) valueObject) ? "Да" : "Нет";
      }
      String name = formField.getName();
      if (name == null) {
        name = formField.getPropId();
      }
      final ReadOnly txt = new ReadOnly(name + " = " + valueObject);
      if (n == 1) {
        txt.setCaption("Подписываемые данные");
      }
      txt.addStyleName("light");
      form.addField("*" + n + "*", txt);
      n++;
    }

    if (!formFields.isEmpty()) {
      final int size = formFields.size();
      byte[][] blocks = new byte[size][];
      String[] ids = new String[size];
      boolean[] files = new boolean[size];
      int i = 0;
      for (final FormField formField : formFields) {
        Object modelValue = formField.getValue();
        blocks[i] = VariableToBytes.toBytes(modelValue);
        files[i] = (modelValue instanceof FileValue);
        ids[i] = formField.getPropId();
        i++;
      }
      FormSignatureField sign = new FormSignatureField(new SignatureProtocol(formId, SIGNATURE, SIGNATURE, blocks, files, ids, form, dataAccumulator));
      sign.setCaption(SIGNATURE);
      sign.setRequired(true);
      form.addField(SIGNATURE, sign);
    } else {
      //TODO: понять ранее на уровне FFT.needStep()
      final ReadOnly txt = new ReadOnly("Нет данных для подписания");
      txt.setRequired(false);
      txt.setCaption(SIGNATURE);
      txt.setRequired(true);
      form.addField(SIGNATURE, txt);
    }
    return form;
  }

  @Override
  public TransitionAction getTransitionAction() {
    return new EmptyAction();
  }

  final public static class SignatureForm extends Form implements FieldSignatureSource {

    public SignatureForm() {
      this.setDescription("Электронная подпись предназначена для идентификации лица, " +
              "подписавшего электронный документ и является полноценной заменой (аналогом) " +
              "собственноручной подписи в случаях, предусмотренных Гражданским кодексом Российской Федерации " +
              "(часть 1, глава 9, статья 160)");
    }

    @Override
    public String getSignedData() {
      return null;
    }

    @Override
    public Signatures getSignatures() {
      Field field = getField(SIGNATURE);
      Object value = field.getValue();
      return value instanceof Signatures ? (Signatures) value : null;
    }

    @Override
    public SignData getSignData() {
      throw new UnsupportedOperationException("Method does not allowed");
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
