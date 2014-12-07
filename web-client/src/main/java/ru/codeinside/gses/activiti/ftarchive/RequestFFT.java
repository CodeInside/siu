package ru.codeinside.gses.activiti.ftarchive;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

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
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class RequestFFT implements FieldType<Обращение> {

  private static String _300PX = "300px";
  private static String DATE_PATTERN = "dd.MM.yyyy";
  private static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);

  private static final long serialVersionUID = 8309095853078973327L;

  private static Logger log = Logger.getLogger(RequestFFT.class.getName());

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

    Layout layout = form.getLayout();

    if (value.getПодписьОбращения() == null) {
      layout.addComponent(createLabel("ЭЦП не обнаружена", "v-label-orange"));
    } else {
      if (value.isSignatureValid()) {
        layout.addComponent(createLabel("ЭЦП валидна", "v-label-green"));
      } else {
        layout.addComponent(createLabel("ЭЦП не валидна", "v-label-red"));
      }
    }

    layout.addComponent(createDateLabel("Дата подачи", value.getДатаПриема()));

    layout.addComponent(createTextField("Услуга", value.getУслуга()));
    layout.addComponent(createTextField("ОГВ", value.getОГВ()));

    if (value.getЮридическоеЛицо() != null) {
      ЮридическоеЛицо юридическоеЛицо = value.getЮридическоеЛицо();

      layout.addComponent(createTextField("Полное название", юридическоеЛицо.getПолноеНазвание()));

      if (юридическоеЛицо.getРуководитель() != null) {
        Руководитель руководитель = юридическоеЛицо.getРуководитель();
        layout.addComponent(createTextField("Руководитель", руководитель.toGeneralString()));
      }

      if (юридическоеЛицо.getГлавныйБухгалтер() != null) {
        ГлавныиБухгалтер главныиБухгалтер = юридическоеЛицо.getГлавныйБухгалтер();
        layout.addComponent(createTextField("Главный Бухгалтер", главныиБухгалтер.toGeneralString()));
      }

      layout.addComponent(createTextArea("Почтовый Адрес", юридическоеЛицо.getПочтовыйАдрес().toGeneralString()));
      layout.addComponent(createTextArea("Юридический Адрес", юридическоеЛицо.getЮридическийАдрес().toGeneralString()));
      layout.addComponent(createTextArea("Номер телефона", юридическоеЛицо.getТелефон().toGeneralString()));

    } else if (value.getФизическоеЛицо() != null) {
      // ФизическоеЛицо
      ФизическоеЛицо declarerValue = value.getФизическоеЛицо();
      layout.addComponent(createTextField("Фамилия", declarerValue.getФио().getФамилия()));
      layout.addComponent(createTextField("Имя", declarerValue.getФио().getИмя()));
      layout.addComponent(createTextField("Отчество", declarerValue.getФио().getОтчество()));

      layout.addComponent(createDateLabel("Дата рождения", declarerValue.getДатаРождения()));

      layout.addComponent(createTextArea("Адрес регистрации", declarerValue.getАдрес().toGeneralString()));
      layout.addComponent(createTextField("Номер телефона", declarerValue.getТелефон().toGeneralString()));
    }

    form.setValue(value);

    return form;
  }

  TextField createTextField(String caption, String value) {
    TextField textField = new TextField(caption, value);
    textField.setWidth(_300PX);
    textField.setReadOnly(true);
    return textField;
  }

  Label createLabel(String value, String styleName) {
    Label label = new Label(value);
    label.setWidth(_300PX);
    if (styleName != null) {
      label.setStyleName(styleName);
    }
    return label;
  }

  TextArea createTextArea(String caption, String value) {
    TextArea textArea = new TextArea(caption, value);
    textArea.setWordwrap(true);
    textArea.setWidth(_300PX);
    textArea.setRows(4);
    textArea.setReadOnly(true);
    return textArea;
  }

  TextField createDateLabel(String caption, Date value) {
    String valueStr = SIMPLE_DATE_FORMAT.format(value);
    return createTextField(caption, valueStr);
  }

}
