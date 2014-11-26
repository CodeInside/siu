package net.mobidom.bp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import net.mobidom.bp.beans.Адрес;
import net.mobidom.bp.beans.ГлавныиБухгалтер;
import net.mobidom.bp.beans.Документ;
import net.mobidom.bp.beans.Обращение;
import net.mobidom.bp.beans.Пол;
import net.mobidom.bp.beans.Руководитель;
import net.mobidom.bp.beans.СсылкаНаДокумент;
import net.mobidom.bp.beans.Телефон;
import net.mobidom.bp.beans.ФИО;
import net.mobidom.bp.beans.ФизическоеЛицо;
import net.mobidom.bp.beans.ЮридическоеЛицо;

public class Test {
  public static void main(String[] args) throws Exception {

    Обращение обращение = new Обращение();
    обращение.setДатаПриема(new Date());
    обращение.setПланируемойВыдачиРезультата(new Date());
    обращение.setИдентификатор("ид-1");
    обращение.setНомер("номер-1");
    обращение.setОГВ("Управление Мочалок");
    обращение.setУслуга("Уборка помещения");

    ФизическоеЛицо физическоеЛицо = new ФизическоеЛицо();
    ФИО фио = new ФИО();
    фио.setИмя("Ифан");
    фио.setОтчество("Ифанович");
    фио.setФамилия("Ифашка");

    физическоеЛицо.setФио(фио);
    физическоеЛицо.setДатаРождения(new Date());
    физическоеЛицо.setМестоРождения("Плутон");
    физическоеЛицо.setПол(Пол.ЖЕНСКИЙ);
    физическоеЛицо.setИдентификатор("7989459");

    Адрес адрес = new Адрес();
    адрес.setСтрана("РФ");
    адрес.setРегион("Дотационный");
    адрес.setРайон("Опасный");
    адрес.setНаселенныйПункт("Желтизна");
    адрес.setПочтовыйИндекс("123654");
    адрес.setУлица("Нелучшая");
    адрес.setДом("хрущевка");
    адрес.setКвартира("однокомнатная");
    физическоеЛицо.setАдрес(адрес);

    Телефон телефон = new Телефон();
    телефон.setПрефикс("555");
    телефон.setНомер("5555555");
    физическоеЛицо.setТелефон(телефон);

    обращение.setФизическоеЛицо(физическоеЛицо);

    ЮридическоеЛицо юридическоеЛицо = new ЮридическоеЛицо();
    юридическоеЛицо.setИдентификатор("ид-1");
    юридическоеЛицо.setНазвание("нейм");
    юридическоеЛицо.setПолноеНазвание("фулл нейм");
    юридическоеЛицо.setЮридическийАдрес(адрес);
    юридическоеЛицо.setПочтовыйАдрес(адрес);
    юридическоеЛицо.setТелефон(телефон);

    ГлавныиБухгалтер главныиБухгалтер = new ГлавныиБухгалтер();
    главныиБухгалтер.setФио(фио);
    юридическоеЛицо.setГлавныйБухгалтер(главныиБухгалтер);

    Руководитель руководитель = new Руководитель();
    руководитель.setДолжность("дъякон");
    руководитель.setФио(фио);
    юридическоеЛицо.setРуководитель(руководитель);

    обращение.setЮридическоеЛицо(юридическоеЛицо);

    List<Документ> документs = new ArrayList<Документ>();
    Документ документ = new Документ();
    документs.add(документ);

    обращение.setДокументы(документs);

    List<СсылкаНаДокумент> ссылкаНаДокументs = new ArrayList<СсылкаНаДокумент>();
    обращение.setСсылкиНаДокументы(ссылкаНаДокументs);

    //
    JAXBContext jaxbContext = JAXBContext.newInstance("net.mobidom.bp.beans");
    Marshaller marshaller = jaxbContext.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
    marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
    
    marshaller.marshal(обращение, System.out);
    
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    marshaller.marshal(обращение, out);

    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
    JAXBElement<Обращение> element = (JAXBElement<Обращение>) unmarshaller.unmarshal(new ByteArrayInputStream(out.toByteArray()));

    обращение = element.getValue();

    System.out.println(обращение);

  }
}
