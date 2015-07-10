package ru.codeinside.gses.webui.form;

import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.VerticalLayout;
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
    List<Long> entityId = null;
    if (dataAccumulator.getServiceName() != null) {
      entityId = dataAccumulator.getRequestId();
    } else if (dataAccumulator.getRequestType() != null) {
      entityId = dataAccumulator.getResponseId();
    }

    SignData signData = (SignData) resultTransition.getData();

    final Form form = new SpSignatureForm(
            entityId,
            dataAccumulator.getServiceName(),
            dataAccumulator.isNeedOv(),
            signData
    );

    FormUtils.addSignedDataToForm(form, signData, SIGNED_DATA_ID);
    FormUtils.addSignatureFieldToForm(form, formId, signData, SP_SIGN, dataAccumulator);

    return form;
  }

  @Override
  public TransitionAction getTransitionAction(List<FormField> formFields) {
    if (formFields != null && formFields.size() > 0) {
      dataAccumulator.setFormFields(formFields);
    }

    if (dataAccumulator.getServiceName() != null) {
      return new GetAppDataAction(dataAccumulator);
    } else if (dataAccumulator.getRequestType() != null){
      return new GetRequestAppDataAction(dataAccumulator);
    } else {
      throw new IllegalStateException("Ошибка в маршруте");
    }
  }

  final public static class SpSignatureForm extends Form implements FieldSignatureSource {

    private List<Long> entityId;// List нужен для того, что бы entityId был mutable. Там всегда один элемент
    private String serviceName;
    private boolean needOv;
    private SignData signData;

    public SpSignatureForm(List<Long> entityId, String serviceName, boolean needOv, SignData signData) {
      this.setDescription("Электронная подпись предназначена для идентификации лица, " +
          "подписавшего электронный документ и является полноценной заменой (аналогом) " +
          "собственноручной подписи в случаях, предусмотренных Гражданским кодексом Российской Федерации " +
          "(часть 1, глава 9, статья 160)");
      this.entityId = entityId;
      this.serviceName = serviceName;
      this.needOv = needOv;
      this.signData = signData;
    }

    public boolean needOv() {
      return needOv;
    }

    public Long getEntityId() {
      return entityId.get(0);
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
