/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c;

import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws3564c.enclosure.EnclosureRequestBuilder;
import ru.codeinside.gws3564c.enclosure.gkn.EncRealtyCadastralPassport;
import ru.codeinside.gws3564c.enclosure.gkn.EnclosureCadastralExtract;
import ru.codeinside.gws3564c.enclosure.gkn.ParcelCadastralPlanRequest;
import ru.codeinside.gws3564c.enclosure.gkn.ParcelValueRequest;
import ru.codeinside.gws3564c.enclosure.grp.*;

public class EnclosureBuilderFactory {

  public static EnclosureRequestBuilder createEnclosureBuilder(ExchangeContext ctx) {
    RequestType reqType = RequestType.valueOf((String) ctx.getVariable("enclosure_request_type"));
    switch (reqType) {
      case OBJECT_RIGHTS: {
        return new ObjectRightEnclosureBuilder(ctx);
      }
      case SUBJECT_RIGHTS: {
        return new SubjectRightsEnclosureBuilder(ctx);
      }
      case INCAPACITY_OWNER_RIGHTS: {
        return new ExtractDataAboutIncapacityOwnerBuilder(ctx);
      }
      case SUBJECT_RIGHTS_HISTORY: {
        return new SubjectRightsHistoryEnclosureBuilder(ctx);
      }
      case CONTENT_DOCUMENT_EXTRACT: {
        return new DocumentContentEnclosureBuilder(ctx);
      }
      case RIGHTS_ESTABLISH: {
        return new RightsEstablishEnclosureBuilder(ctx);
      }
      case CADASTRAL_PASSPORT:
      case PARCEL_CADASTRAL_PASSPORT: { // запрос кадастрового паспорта
        return new EncRealtyCadastralPassport(ctx);
      }
      case PARCEL_VALUE: { // О запросе кадастровой справки о кадастровой стоимости земельного участка
        return new ParcelValueRequest(ctx);
      }
      case PARCEL_CADASTRAL_PLAN: { // кадастровый план участка
        return new ParcelCadastralPlanRequest(ctx);
      }
      case CADASTRAL_EXTRACT: { // выписка кадастрового плана
        return new  EnclosureCadastralExtract(ctx);
      }
      default:
        throw new UnsupportedOperationException();
    }
  }

}
