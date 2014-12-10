package net.mobidom.bp.beans.types;

import java.util.HashMap;
import java.util.Map;

import net.mobidom.bp.beans.СсылкаНаДокумент;
import net.mobidom.bp.beans.СсылкаНаУдостоверениеЛичности;

public class DocumentTypesHelper {

  static Map<ТипСсылкиНаДокумент, ТипДокумента> DOCUMENT_REFERENCE_TYPES_TO_DOCUMENT_TYPES = new HashMap<ТипСсылкиНаДокумент, ТипДокумента>() {
    private static final long serialVersionUID = -5343689600862070814L;

    {
      put(ТипСсылкиНаДокумент.ИНН, ТипДокумента.СВИДЕТЕЛЬСТВО_О_ПОСТАНОВКЕ_НА_УЧЁТ_ФИЗИЧЕСКОГО_ЛИЦА_В_НАЛОГОВОМ_ОРГАНЕ);
      put(ТипСсылкиНаДокумент.СНИЛС, ТипДокумента.ДАННЫЕ_ЛИЦЕВОГО_СЧЕТА_ЗАСТРАХОВАННОГО_ЛИЦА);
    }
  };

  static Map<ТипУдостоверенияЛичности, ТипДокумента> ID_REFERENCE_TYPES_TO_DOCUMENT_TYPES = new HashMap<ТипУдостоверенияЛичности, ТипДокумента>() {
    private static final long serialVersionUID = 8103641367049497941L;

    {
      put(ТипУдостоверенияЛичности.ВОЕННЫЙ_БИЛЕТ, ТипДокумента.ВОЕННЫЙ_БИЛЕТ);
      put(ТипУдостоверенияЛичности.ПАСПОРТ_ГРАЖДАНИНА_РФ, ТипДокумента.ПАСПОРТ_ГРАЖДАНИНА_РФ);
      put(ТипУдостоверенияЛичности.СВИДЕТЕЛЬСТВО_О_РОЖДЕНИИ, ТипДокумента.СВИДЕТЕЛЬСТВО_О_РОЖДЕНИИ);
    }
  };

  public static ТипДокумента defineDocumentTypeByReferenceType(СсылкаНаДокумент documentReference) {
    ТипСсылкиНаДокумент documentReferenceType = documentReference.getТипСсылкиНаДокумент();
    if (documentReferenceType == ТипСсылкиНаДокумент.УДОСТОВЕРЕНИЕ_ЛИЧНОСТИ) {
      return ID_REFERENCE_TYPES_TO_DOCUMENT_TYPES.get(((СсылкаНаУдостоверениеЛичности) documentReference).getТип());
    }

    return DOCUMENT_REFERENCE_TYPES_TO_DOCUMENT_TYPES.get(documentReferenceType);
  }

}
