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

import java.util.List;

public class FormSpSignatureSeq extends AbstractFormSeq {

  public static final String SP_SIGN = "AppDataSignatureField";
  public static final String SIGNED_DATA_ID = "SignedAppData";
  private static final long serialVersionUID = 1L;
  private final DataAccumulator dataAccumulator;

  FormSpSignatureSeq(DataAccumulator dataAccumulator) {
    this.dataAccumulator = dataAccumulator;
  }

  @Override
  public String getCaption() {
    return "Подписание блока AppData личной ЭЦП";
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
    final Form form = new SpSignatureForm();

    String appData = (String) resultTransition.getData();
    addSignedDataToForm(form, appData, SIGNED_DATA_ID);
    addSignatureFieldToForm(form, formId, appData, SP_SIGN);

    return form;
  }

  /**
   * Получить действие перехода
   */
  @Override
  public TransitionAction getTransitionAction(List<FormField> formFields) {
    dataAccumulator.setFormFields(formFields);
    return new GetAppDataAction(dataAccumulator);
  }

  private void addSignedDataToForm(Form form, String signData, String propertyId) {
    final ReadOnly txt = new ReadOnly(signData);
    txt.setCaption("Подписываемые данные");
    txt.addStyleName("light");
    form.addField(propertyId, txt);
  }

  private void addSignatureFieldToForm(Form form, FormID formId, String appData, String fieldId) {
    byte[] appDataBytes = appData.getBytes();
    boolean[] files = {false};
    String[] ids = {fieldId};

    FormSignatureField sign = new FormSignatureField(
        new SignatureProtocol(formId, FormSignatureSeq.SIGNATURE, FormSignatureSeq.SIGNATURE,
            new byte[][] {appDataBytes}, files, ids, form, dataAccumulator));
    sign.setCaption(FormSignatureSeq.SIGNATURE);
    sign.setRequired(true);
    form.addField(FormSignatureSeq.SIGNATURE, sign);
  }

  final public static class SpSignatureForm extends Form implements FieldSignatureSource {

    public SpSignatureForm() {
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
