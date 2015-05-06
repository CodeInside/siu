package ru.codeinside.gses.webui.form;

import com.vaadin.ui.Form;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.SignatureProtocol;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.webui.wizard.TransitionAction;

import java.util.List;

public class FormSpSignatureSeq extends AbstractFormSeq {

  public static final String SIGNATURE = "ЭЦП";
  private static final long serialVersionUID = 1L;
  private final String consumerName;
  private final DataAccumulator dataAccumulator;

  FormSpSignatureSeq(String consumerName, DataAccumulator dataAccumulator) {
    this.consumerName = consumerName;
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

    final Form form = new FormSignatureSeq.SignatureForm();
    form.setDescription("Электронная подпись предназначена для идентификации лица, " +
        "подписавшего электронный документ и является полноценной заменой (аналогом) " +
        "собственноручной подписи в случаях, предусмотренных Гражданским кодексом Российской Федерации " +
        "(часть 1, глава 9, статья 160)");

    String appData = (String) resultTransition.getData();
    addSignedDataToForm(form, appData);
    addSignatureFieldToForm(form, formId, appData);

    return form;
  }

  /**
   * Получить действие перехода
   */
  @Override
  public TransitionAction getTransitionAction() {
    return new GetAppDataAction(consumerName, dataAccumulator);
  }

  private void addSignedDataToForm(Form form, String appData) {
    final ReadOnly txt = new ReadOnly(appData);
    txt.setCaption("Подписываемые данные");
    txt.addStyleName("light");
    form.addField(0, txt);
  }

  private void addSignatureFieldToForm(Form form, FormID formId, String appData) {
    byte[][] blocks = new byte[1][];
    blocks[0] = appData.getBytes();
    boolean[] files = {false};
    String[] ids = {"0"};

    FormSignatureField sign = new FormSignatureField(new SignatureProtocol(formId, SIGNATURE, SIGNATURE, blocks, files, ids, form));
    sign.setCaption(SIGNATURE);
    sign.setRequired(true);
    form.addField(SIGNATURE, sign);
  }
}
