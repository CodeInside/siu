package net.mobidom.bp.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import net.mobidom.bp.beans.request.DocumentRequest;
import net.mobidom.bp.beans.types.ТипДокумента;

@XmlRootElement(name = "документ", namespace = "http://www.mobidom.net/")
@XmlType(namespace = "http://www.mobidom.net/", name = "Документ")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Документ implements Serializable {
  private static final long serialVersionUID = -2951102600558480199L;

  static SimpleDateFormat LABEL_SDF = new SimpleDateFormat("dd.MM.yy");

  String тип;
  ВидДокумента вид;
  СостояниеДокумента состояние;

  String серия;
  String номер;
  Date датаВыдачи;
  Date датаОкончанияСрокаДействия;
  String местоВыдачи;

  Integer количествоЛистов;
  Integer количествоЭкземпляров;

  String огвЗапроса;

  XmlContentWrapper служебныеДанные;

  BinaryContent binaryContent;
  XmlContentWrapper xmlContent;

  DocumentRequest documentRequest;

  public СостояниеДокумента getСостояние() {
    return состояние;
  }

  public void setСостояние(СостояниеДокумента состояние) {
    this.состояние = состояние;
  }

  public String getСерия() {
    return серия;
  }

  public void setСерия(String серия) {
    this.серия = серия;
  }

  public String getНомер() {
    return номер;
  }

  public void setНомер(String номер) {
    this.номер = номер;
  }

  public Date getДатаВыдачи() {
    return датаВыдачи;
  }

  public void setДатаВыдачи(Date датаВыдачи) {
    this.датаВыдачи = датаВыдачи;
  }

  public Date getДатаОкончанияСрокаДействия() {
    return датаОкончанияСрокаДействия;
  }

  public void setДатаОкончанияСрокаДействия(Date датаОкончанияСрокаДействия) {
    this.датаОкончанияСрокаДействия = датаОкончанияСрокаДействия;
  }

  public String getМестоВыдачи() {
    return местоВыдачи;
  }

  public void setМестоВыдачи(String местоВыдачи) {
    this.местоВыдачи = местоВыдачи;
  }

  public Integer getКоличествоЛистов() {
    return количествоЛистов;
  }

  public void setКоличествоЛистов(Integer количествоЛистов) {
    this.количествоЛистов = количествоЛистов;
  }

  public Integer getКоличествоЭкземпляров() {
    return количествоЭкземпляров;
  }

  public void setКоличествоЭкземпляров(Integer количествоЭкземпляров) {
    this.количествоЭкземпляров = количествоЭкземпляров;
  }

  public String getОгвЗапроса() {
    return огвЗапроса;
  }

  public void setОгвЗапроса(String огвЗапроса) {
    this.огвЗапроса = огвЗапроса;
  }

  public XmlContentWrapper getСлужебныеДанные() {
    return служебныеДанные;
  }

  public void setСлужебныеДанные(XmlContentWrapper служебныеДанные) {
    this.служебныеДанные = служебныеДанные;
  }

  @XmlTransient
  public ТипДокумента getDocumentType() {
    if (тип == null || тип.isEmpty()) {
      return ТипДокумента.UNKNOWN;
    }

    for (ТипДокумента type : ТипДокумента.values()) {
      if (тип.equals(type.getMfcId())) {
        return type;
      }
    }

    return ТипДокумента.UNKNOWN;
  }

  public void setDocumentType(ТипДокумента type) {
    this.тип = String.valueOf(type);
  }

  public String getТип() {
    return тип;
  }

  public void setТип(String type) {
    this.тип = type;
  }

  public BinaryContent getBinaryContent() {
    return binaryContent;
  }

  public void setBinaryContent(BinaryContent binaryContent) {
    this.binaryContent = binaryContent;
  }

  public XmlContentWrapper getXmlContent() {
    return xmlContent;
  }

  public void setXmlContent(XmlContentWrapper xmlContent) {
    this.xmlContent = xmlContent;
  }

  public ВидДокумента getВид() {
    return вид;
  }

  public void setВид(ВидДокумента вид) {
    this.вид = вид;
  }

  @XmlTransient
  public DocumentRequest getDocumentRequest() {
    return documentRequest;
  }

  public void setDocumentRequest(DocumentRequest documentRequest) {
    this.documentRequest = documentRequest;
  }

  public Map<String, String> getDocumentReferencePropertiesForLabels() {
    Map<String, String> props = new LinkedHashMap<String, String>();

    String val = null;
    if (getDocumentType() == ТипДокумента.UNKNOWN) {
      val = getDocumentType().getLabel() + "(" + getТип() + ")";
    } else {
      val = getDocumentType().getLabel();
    }
    props.put("Тип", val);

    if (getСерия() != null) {
      val = getСерия();
    } else {
      val = "-";
    }
    props.put("Серия", val);

    if (getНомер() != null) {
      val = getНомер();
    } else {
      val = "-";
    }
    props.put("Номер", val);

    if (getМестоВыдачи() != null) {
      val = getМестоВыдачи();
    } else {
      val = "-";
    }
    props.put("Место выдачи", val);

    if (getДатаВыдачи() != null) {
      val = LABEL_SDF.format(getДатаВыдачи());
    } else {
      val = "-";
    }
    props.put("Дата выдачи", val);

    if (getДатаОкончанияСрокаДействия() != null) {
      val = LABEL_SDF.format(getДатаОкончанияСрокаДействия());
    } else {
      val = "-";
    }
    props.put("Дата окончания", val);

    if (getМестоВыдачи() != null) {
      val = getМестоВыдачи();
    } else {
      val = "-";
    }
    props.put("Место выдачи", val);

    if (getОгвЗапроса() != null) {
      val = getОгвЗапроса();
    } else {
      val = "-";
    }
    props.put("ОГВ запроса", val);

    if (getСостояние() != null) {
      val = getСостояние().getLabel();
    } else {
      val = "-";
    }
    props.put("Состояние", val);

    if (getВид() != null) {
      val = getВид().getLabel();
    } else {
      val = "-";
    }
    props.put("Вид", val);

    if (getКоличествоЛистов() != null) {
      val = String.valueOf(getКоличествоЛистов());
    } else {
      val = "-";
    }
    props.put("Кол-во листов", val);

    if (getКоличествоЭкземпляров() != null) {
      val = String.valueOf(getКоличествоЭкземпляров());
    } else {
      val = "-";
    }
    props.put("Кол-во экземпляров", val);

    return props;
  }

}
