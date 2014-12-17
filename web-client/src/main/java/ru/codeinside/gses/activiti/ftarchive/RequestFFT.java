package ru.codeinside.gses.activiti.ftarchive;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.mobidom.bp.beans.ГлавныиБухгалтер;
import net.mobidom.bp.beans.Обращение;
import net.mobidom.bp.beans.Руководитель;
import net.mobidom.bp.beans.ФизическоеЛицо;
import net.mobidom.bp.beans.ЮридическоеЛицо;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.types.FieldType;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class RequestFFT implements FieldType<Обращение> {

  private static String _100p = "100%";
  private static String DATE_PATTERN = "dd.MM.yyyy";
  private static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);

  private static final long serialVersionUID = 8309095853078973327L;

  @SuppressWarnings("serial")
  @Override
  public Field createField(String taskId, String fieldId, String name, Обращение value, PropertyNode node, boolean archive) {

    if (value == null) {
      throw new RuntimeException("request value is null");
    }

    Form form = new Form() {

      Обращение value;

      @Override
      public void commit() throws SourceException, InvalidValueException {
        // nothing to do
      }

      @Override
      public void setValue(Object newValue) throws com.vaadin.data.Property.ReadOnlyException, com.vaadin.data.Property.ConversionException {
        if (newValue == null) {
          throw new RuntimeException("request value is null");
        }

        if (!(newValue instanceof Обращение)) {
          throw new RuntimeException("request value is not Request instance. it's = " + newValue.getClass());
        }

        this.value = (Обращение) newValue;
      };

      @Override
      public Object getValue() {
        return this.value;
      }

    };

    form.setCaption("Заявление");

    HorizontalLayout rootLayout = new HorizontalLayout();
    rootLayout.setWidth("1500px");

    FormLayout leftLayout = new FormLayout();
    leftLayout.setWidth("750px");

    leftLayout.addComponent(createTextField("Идентификатор", value.getИдентификатор()));
    leftLayout.addComponent(createTextField("Номер", value.getНомер()));

    leftLayout.addComponent(createDateLabel("Дата приема", value.getДатаПриема()));
    leftLayout.addComponent(createDateLabel("Планируемая дата выдачи результата", value.getПланируемойВыдачиРезультата()));

    leftLayout.addComponent(createTextField("Услуга", value.getУслуга()));
    leftLayout.addComponent(createTextField("ОГВ", value.getОГВ()));

    if (value.getЮридическоеЛицо() != null) {
      ЮридическоеЛицо юридическоеЛицо = value.getЮридическоеЛицо();

      leftLayout.addComponent(createTextArea("Полное название", юридическоеЛицо.getПолноеНазвание(), 3));

      if (юридическоеЛицо.getРуководитель() != null) {
        Руководитель руководитель = юридическоеЛицо.getРуководитель();
        leftLayout.addComponent(createTextField("Руководитель", руководитель.toGeneralString()));
      }

      if (юридическоеЛицо.getГлавныйБухгалтер() != null) {
        ГлавныиБухгалтер главныиБухгалтер = юридическоеЛицо.getГлавныйБухгалтер();
        leftLayout.addComponent(createTextField("Главный Бухгалтер", главныиБухгалтер.toGeneralString()));
      }

      if (value.getПредставитель() != null) {
        leftLayout.addComponent(createTextField("Представитель ИД", value.getПредставитель().getИдентификатор()));
      }

      leftLayout.addComponent(createTextArea("Почтовый Адрес", юридическоеЛицо.getПочтовыйАдрес().toGeneralString()));
      leftLayout.addComponent(createTextArea("Юридический Адрес", юридическоеЛицо.getЮридическийАдрес().toGeneralString()));
      leftLayout.addComponent(createTextField("Номер телефона", юридическоеЛицо.getТелефон().toGeneralString()));

    } else if (value.getФизическоеЛицо() != null) {
      // ФизическоеЛицо
      ФизическоеЛицо declarerValue = value.getФизическоеЛицо();

      leftLayout.addComponent(createTextField("Идентификатор ФЛ", declarerValue.getИдентификатор()));

      leftLayout.addComponent(createTextField("Фамилия", declarerValue.getФио().getФамилия()));
      leftLayout.addComponent(createTextField("Имя", declarerValue.getФио().getИмя()));
      leftLayout.addComponent(createTextField("Отчество", declarerValue.getФио().getОтчество()));

      leftLayout.addComponent(createDateLabel("Дата рождения", declarerValue.getДатаРождения()));

      leftLayout.addComponent(createTextField("Пол", declarerValue.getПол()));

      leftLayout.addComponent(createTextArea("Место рождения", declarerValue.getМестоРождения()));

      leftLayout.addComponent(createTextArea("Адрес регистрации", declarerValue.getАдрес().toGeneralString()));
      leftLayout.addComponent(createTextField("Номер телефона", declarerValue.getТелефон().toGeneralString()));
    }

    rootLayout.addComponent(leftLayout);

    VerticalLayout rightLayout = new VerticalLayout();

    if (value.getПодписьОбращения() == null) {
      Label label = createLabel("ЭЦП не обнаружена", "v-label-orange");
      rightLayout.addComponent(label);

    } else {
      Label label = null;
      if (value.getПодписьОбращения().isSignatureValid()) {
        label = createLabel("ЭЦП валидна", "v-label-green");
      } else {
        label = createLabel("ЭЦП не валидна", "v-label-red");
      }
      rightLayout.addComponent(label);

      Label signOwnerInfo = new Label();
      signOwnerInfo.setContentMode(Label.CONTENT_PREFORMATTED);
      signOwnerInfo.setValue(value.getПодписьОбращения().getOwnerInfo());
      signOwnerInfo.setReadOnly(true);
      signOwnerInfo.setSizeFull();

      rightLayout.addComponent(signOwnerInfo);
    }

    rootLayout.addComponent(rightLayout);
    form.setLayout(rootLayout);
    form.setValue(value);

    return form;
  }

  TextField createTextField(String caption, Object value) {
    TextField textField = new TextField();
    textField.setCaption(caption);
    if (value == null) {
      textField.setValue("не указано");
    } else {
      textField.setValue(String.valueOf(value));
    }
    textField.setWidth(_100p);
    textField.setReadOnly(true);
    return textField;
  }

  Label createLabel(String value, String styleName) {
    Label label = new Label(value);
    label.setWidth(_100p);
    if (styleName != null) {
      label.addStyleName(styleName);
    }
    return label;
  }

  TextArea createTextArea(String caption, String value, int rows) {
    TextArea textArea = new TextArea(caption);
    if (value == null) {
      textArea.setValue("не указано");
    } else {
      textArea.setValue(String.valueOf(value));
    }
    textArea.setWordwrap(true);
    textArea.setWidth("100%");
    textArea.setRows(rows);
    textArea.setReadOnly(true);
    return textArea;
  }

  TextArea createTextArea(String caption, String value) {
    return createTextArea(caption, value, 2);
  }

  TextField createDateLabel(String caption, Date value) {
    String valueStr = SIMPLE_DATE_FORMAT.format(value);
    return createTextField(caption, valueStr);
  }

}
