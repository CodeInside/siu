package net.mobidom.bp.beans.types;

import java.util.ArrayList;
import java.util.List;

public enum ТипДокумента {

  UNKNOWN("Не определен документ", null), 
  СТРАХОВОЕ_СВИДЕТЕЛЬСТВО_ОБЯЗАТЕЛЬНОГО_ПЕНСИОННОГО_СТРАХОВАНИЯ,
  СТРАХОВОЕ_СВИДЕТЕЛЬСТВО_ГОСУДАРСТВЕННОГО_ПЕНСИОННОГО_СТРАХОВАНИЯ,
  СВИДЕТЕЛЬСТВО_О_ПОСТАНОВКЕ_НА_УЧЁТ_ФИЗИЧЕСКОГО_ЛИЦА_В_НАЛОГОВОМ_ОРГАНЕ,
  ПАСПОРТ_ГРАЖДАНИНА_РФ("Паспорт гражданина РФ", null), 
  СВИДЕТЕЛЬСТВО_О_РОЖДЕНИИ, 
  ВОЕННЫЙ_БИЛЕТ, 
  СВИДЕТЕЛЬСТВО_О_БРАКЕ, 
  КВИТАНЦИЯ_ОБ_ОПЛАТЕ,
  // FNS 3777
  НДФЛ_2("2-НДФЛ", "fns3777"),
  // FNS 3521
  НДФЛ_3("3-НДФЛ", "fns3521"),
  // MIDRF 3894
  ТРАНСКРИБИРОВАНИЕ_ФИГ("Транскрибирование ФИГ", "midrf3894"), 
  // PFRF 3815
  ДАННЫЕ_ЛИЦЕВОГО_СЧЕТА_ЗАСТРАХОВАННОГО_ЛИЦА(null, "pfrf3815"), 
  // PFRF 3814
  СНИЛС("СНИЛС", "pfrf3814"), 
  // RR 3564
  ВЫПИСКА_ИЗ_ЕГРП_НА_НЕДВИЖИМОЕ_ИМУЩЕСТВО_И_СДЕЛОК_С_НИМ_ОБЩЕДОСТУПНЫЕ_СВЕДЕНИЯ("Выписка из ЕГРП на недвижимое имущество и сделок с ним (общедоступные сведения о зарегистрированных правах на объект недвижимости)", "rr3564"),
  ВЫПИСКА_ИЗ_ЕГРП_НА_НЕДВИЖИМОЕ_ИМУЩЕСТВО_И_СДЕЛОК_С_НИМ_О_ПЕРЕХОДЕ_ПРАВ_НА_ОБЪЕКТ_НЕДВИЖИМОГО_ИМУЩЕСТВА("Выписка из ЕГРП на недвижимое имущество и сделок с ним о переходе прав на объект недвижимого имущества", "rr3564"),
  СПРАВКА_О_СОДЕРЖАНИИ_ПРАВОУСТАНАВЛИВАЮЩЕГО_ДОКУМЕНТА(null, "rr3564"),
  ВЫПИСКА_ИЗ_ЕГРП_НА_НЕДВИЖИМОЕ_ИМУЩЕСТВО_И_СДЕЛОК_С_НИМ_О_ПРАВАХ_ОТДЕЛЬНОГО_ЛИЦА_НА_ИМЕЮЩИЕСЯ_У_НЕГО_ОБЪЕКТЫ_НЕДВИЖИМОСТИ("Выписка из ЕГРП на недвижимое имущество и сделок с ним о правах отдельного лица на имеющиеся у него объекты недвижимости", "rr3564"),
  ВЫПИСКА_ИЗ_ЕГРП_НА_НЕДВИЖИМОЕ_ИМУЩЕСТВО_И_СДЕЛОК_С_НИМ_О_ПРАВАХ_ОТДЕЛЬНОГО_ЛИЦА_НА_ИМЕВШИЕСЯ_ИМЕЮЩИЕСЯ_У_НЕГО_ОБЪЕКТЫ_НЕДВИЖИМОСТИ("Выписка из ЕГРП на недвижимое имущество и сделок с ним о правах отдельного лица на имевшиеся имеющиеся у него объекты недвижимости", "rr3564"),
  ВЫПИСКА_ИЗ_ЕГРП_НА_НЕДВИЖИМОЕ_ИМУЩЕСТВО_И_СДЕЛОК_С_НИМ_О_ПРИЗНАНИИ_ПРАВООБЛАДАТЕЛЯ_НЕДЕЕСПОСОБНЫМ_ИЛИ_ОГРАНИЧЕННО_ДЕЕСПОСОБНЫМ("Выписка из ЕГРП на недвижимое имущество и сделок с ним о признании правообладателя недееспособным или ограниченно дееспособным", "rr3564"),
  КАДАСТРОВЫЙ_ПАСПОРТ_ОБЪЕКТА_НЕДВИЖИМОСТИ(null, "rr3564"),
  КАДАСТРОВАЯ_ВЫПИСКИ_ОБ_ОБЪЕКТЕ_НЕДВИЖИМОСТИ(null, "rr3564"),
  КАДАСТРОВЫЙ_ПЛАН_ТЕРРИТОРИИ(null, "rr3564"),
  КАДАСТРОВАЯ_СПРАВКА_О_КАДАСТРОВОЙ_СТОИМОСТИ_ЗЕМЕЛЬНОГО_УЧАСТКА(null, "rr3564"),
  КАДАСТРОВЫЙ_ПАСПОРТ_ЗДАНИЯ_СТРОЕНИЯ_СООРУЖЕНИЯ("Кадастрового паспорт здания, строения, сооружения", "rr3564"),
  // FNS 3793
  СВЕДЕНИЯ_О_НАЛИЧИИ_ОТСУТСТВИИ_ЗАДОЛЖЕННОСТИ_ПО_УПЛАТЕ_НАЛОГОВ_СБОРОВ_ПЕНЕЙ_ШТРАФОВ("Сведения о наличии (отсутствии) задолженности по уплате налогов, сборов, пеней, штрафов", "fns3793"),
  // MVD 3456
  СВЕДЕНИЯ_О_НАЛИЧИИ_СУДИМОСТИ("Сведения о наличии судимости", "mvd3456"),
  СВЕДЕНИЯ_О_НАЛИЧИИ_НЕПОГАШЕННОЙ_ИЛИ_НЕСНЯТОЙ_СУДИМОСТИ_ЗА_СОВЕРШЕНИЕ_УМЫШЛЕННОГО_ПРЕСТУПЛЕНИЯ("Сведения о наличии непогашенной или неснятой судимости за совершение умышленного преступления", "mvd3456"),
  СВЕДЕНИЯ_О_НАХОЖДЕНИИ_В_РОЗЫСКЕ("Сведения о нахождении в розыске", "mvd3456"),
  // RR 3907
  ЗАЯВЛЕНИЕ_В_РОСИМУЩЕСТВО("Заявление в Росимущество", "rr3907"),
  // ROSVODRES 3618
  СВЕДЕНИЯ_О_ВОДНЫХ_ОБЪЕКТАХ_ИЗ_ГОСУДАРСТВЕННОГО_ВОДНОГО_РЕЕСТРА("Сведения о водных объектах из Государственного водного реестра", "rosvodres3618"),
  ЗАПРОС_КОПИЙ_ДОКУМЕНТОВ_ИЗ_ГОСУДАРСТВЕННОГО_ВОДНОГО_РЕЕСТРА("Запрос копий документов из Государственного водного реестра", "rosvodres3618"),
  // PRTR 3641
  СПРАВКА_О_СООТВЕТСТВИИ_НЕСООТВЕТСТВИ_ЖИЛЫХ_ПОМЕЩЕНИЙ_ЗДАНИЙ_ТРЕБОВАНИЯМ_САНИТАРНОГО_ЗАКОНОДАТЕЛЬСТВА_ПРИ_ОФОРМЛЕНИИ_ОПЕКИ_ИЛИ_ПОПЕЧИТЕЛЬСТВА("Справка о соответствии (несоответствии) жилых помещений (зданий) требованиям санитарного законодательства при оформлении опеки или попечительства", "prtr3641"),
  // FNS 3626
  ИНН("ИНН", "fns3626"),
  // FNS 3554
  ВЫПИСКА_ИЗ_ЕГРЮЛ_ПОЛНАЯ("Выписка из ЕГРЮЛ полная", "fns3554"),
  ВЫПИСКА_ИЗ_ЕГРИП_ПОЛНАЯ("Выписка из ЕГРИП полная", "fns3554"),
  // PFRF 3622
  СВЕДЕНИЯ_О_ЗАРАБОТНОЙ_ПЛАТЕ_ИНЫХ_ВЫПЛАТАХ_И_ВОЗНАГРАЖДЕНИЯХ_ЗАСТРАХОВАННОГО_ЛИЦА(null, "pfrf3622"),
  // ENSI 3407
  ПОИСК_КОМПОНЕНТА_НСИ("Поиск компонента НСИ", "ensi3407"),
  ПОИСК_И_ПОЛУЧЕНИЕ_РЕВИЗИИ_МЕТАДАННЫХ_КОМПОНЕНТА_НСИ("Поиск и получение ревизии (метаданных) компонента НСИ", "ensi3407"),
  ПОИСК_И_ПОЛУЧЕНИЕ_РЕВИЗИИ_ЭЛЕМЕНТА_ДАННЫХ_КОМПОНЕНТА_НСИ("Поиск и получение ревизии элемента (данных) компонента НСИ", "ensi3407"),
  ПОЛУЧЕНИЕ_ОБНОВЛЕНИЙ_И_РЕПЛИКИ_КОМПОНЕНТА_НСИ("Получение обновлений и реплики компонента НСИ", "ensi3407"),
  // MVD 3879
  ПОИСК_СВЕДЕНИЙ_О_ТС_И_ЕГО_ВЛАДЕЛЬЦЕ("Поиск сведений о ТС и его владельце", "mvd3879"),
  // MCOMM 1019 
  СВЕДЕНИЯ_О_НЕПОЛУЧЕНИИ_ЕЖЕМЕСЯЧНОГО_ПОСОБИЯ_ПО_УХОДУ_ЗА_РЕБЕНКОМ_В_ОРГАНАХ_СОЦИАЛЬНОЙ_ЗАЩИТЫ_НАСЕЛЕНИЯ("Сведения о неполучении ежемесячного пособия по уходу за ребенком в органах социальной защиты населения", "mcomm1019"),
  // FSSP 3924
  СВЕДЕНИЯ_О_НАЛОЖЕННЫХ_НА_ДОЛЖНИКА_СПИ_ОГРАНИЧЕНИЙ_АРЕСТОВ("Сведения о наложенных на должника СПИ ограничений (арестов)", "fssp3924"),
  ПЕРЕДАЧА_ПОДТВЕРЖДЕНИЯ_ИСПОЛНЕНИЯ_НАЛОЖЕННЫХ_ОГРАНИЧЕНИЙ_АРЕСТОВ("Передача подтверждения исполнения наложенных ограничений (арестов)", "fssp3924"),
  ПЕРЕДАЧА_ИСПОЛНИТЕЛЬНЫХ_ДОКУМЕНТОВ_ДОКУМЕНТОВ_ИЗМЕНЯЮЩИХ_СУММЫ_ЗАДОЛЖЕННОСТИ_ПО_ИСПОЛНИТЕЛЬНЫМ_ДОКУМЕНТАМ_ЗАЯВЛЕНИЙ_ОБ_ОТЗЫВЕ_ИСПОЛНИТЕЛЬНЫХ_ДОКУМЕНТОВ_УВЕДОМЛЕНИЯ_ОБ_ИНИЦИИРОВАНИИ_ПРОЦЕДУРЫ_БАНКРОТСТВА_ДОЛЖНИКА("Передача исполнительных документов, документов, изменяющих суммы задолженности по исполнительным документам, заявлений об отзыве исполнительных документов, уведомления об инициировании процедуры банкротства должника", "fssp3924"),
  СВЕДЕНИЯ_РЕЕСТР_О_ЗАДОЛЖЕННОСТИ_ПО_ИСПОЛНИТЕЛЬНЫМ_ПРОИЗВОДСТВАМ("Сведения(реестр) о задолженности по исполнительным производствам", "fssp3924"),
  ПЕРЕДАЧА_РЕЕСТРОВ_ОБ_ОПЛАТЕ_ЗАДОЛЖЕННОСТИ("Передача(реестров) об оплате задолженности", "fssp3924"),
  ПЕРЕДАЧА_СВЕДЕНИЙ_О_ХОДЕ_ИСПОЛНИТЕЛЬНОГО_ПРОИЗВОДСТВА("Передача сведений о ходе исполнительного производства", "fssp3924"),
  СВЕДЕНИЯ_О_ХОДЕ_ИСПОЛНИТЕЛЬНОГО_ПРОИЗВОДСТВА("Сведения о ходе исполнительного производства", "fssp3924"),
  ПЕРЕДАЧА_СВЕДЕНИЙ_О_НАЛИЧИИ_ИСПОЛНИТЕЛЬНЫХ_ПРОИЗВОДСТВ_ПО_ФИЗИЧЕСКИМ_И_ЮРИДИЧЕСКИМ_ЛИЦАМ_ИНДИВИДУАЛЬНЫМ_ПРЕДПРИНИМАТЕЛЯМ("Передача сведений о наличии исполнительных производств по физическим и юридическим лицам (индивидуальным предпринимателям)", "fssp3924"),
  СВЕДЕНИЯ_О_НАЛИЧИИ_ИСПОЛНИТЕЛЬНЫХ_ПРОИЗВОДСТВ_ПО_ФИЗИЧЕСКИМ_И_ЮРИДИЧЕСКИМ_ЛИЦАМ_ИНДИВИДУАЛЬНЫМ_ПРЕДПРИНИМАТЕЛЯМ("Сведения о наличии исполнительных производств по физическим и юридическим лицам (индивидуальным предпринимателям)", "fssp3924"),
  // FSS 3415
  СВЕДЕНИЯ_ОБ_ОТСУТСТВИИ_РЕГИСТРАЦИИ_ГРАЖДАНИНА_В_КАЧЕСТВЕ_ЛИЦА_ДОБРОВОЛЬНО_ВСТУПИВШЕГО_В_ПРАВООТНОШЕНИЯ_ПО_ОБЯЗАТЕЛЬНОМУ_СОЦИАЛЬНОМУ_СТРАХОВАНИЮ_НА_СЛУЧАЙ_ВРЕМЕННОЙ_НЕТРУДОСПОСОБНОСТИ_И_В_СВЯЗИ_С_МАТЕРИНСТВОМ("Сведения об отсутствии регистрации гражданина в качестве лица, добровольно вступившего в правоотношения по обязательному социальному страхованию на случай временной нетрудоспособности и в связи с материнством", "fss3415");
  
  private String label;
  private String serviceId;
  private String mfcId;
  
  private ТипДокумента() {
    String label = name().replace('_', ' ').toLowerCase();
    label = label.substring(0, 1).toUpperCase() + label.substring(1);
    this.label = label;
  }

  private ТипДокумента(String label, String serviceId) {
    if (label == null) {
      String name = name().replace('_', ' ').toLowerCase();
      name = name.substring(0, 1).toUpperCase() + name.substring(1);
      this.label = name;
    } else {
      this.label = label;
    }

    this.serviceId = serviceId;
  }
  
  public String getLabel() {
    return label;
  }
  
  public String getServiceId() {
    return serviceId;
  }
  
  public String getMfcId() {
    return mfcId;
  }
  
  public static List<ТипДокумента> requestableTypes() {
    List<ТипДокумента> list = new ArrayList<ТипДокумента>();
    for (ТипДокумента type : values()) {
      if (type.serviceId != null)
        list.add(type);
    }

    return list;
  }

}
