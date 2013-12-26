/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c.enclosure.gkn;

import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws3564c.enclosure.EnclosureRequestBuilder;
import ru.gkn.RequestGKN;
import ru.gkn.TObject;


public class EncRealtyCadastralPassport extends RequestEnclosureBuilder implements EnclosureRequestBuilder {
  public EncRealtyCadastralPassport(ExchangeContext ctx) {
    super(ctx);
  }


  @Override
  protected RequestGKN.Request.RequiredData createRequiredData() {
    final RequestGKN.Request.RequiredData requiredData = new RequestGKN.Request.RequiredData();
    requiredData.setCadastralPassport(createCadastralPassport());
    return requiredData;
  }

  RequestGKN.Request.RequiredData.CadastralPassport createCadastralPassport() {
    final RequestGKN.Request.RequiredData.CadastralPassport cadastralPassport = new RequestGKN.Request.RequiredData.CadastralPassport();
    final TObject object = new TObject();
    object.setObjKind(ctx.getStrFromContext("cadastralPassportObjKind"));  //required
    if (ctx.isStringVariableHasValue("cadastralPassportNumber")) {
      object.setCadastralNumber(ctx.getStrFromContext("cadastralPassportNumber"));
    }
    object.setLocation(setLocation("cadastralPassport"));
    cadastralPassport.setObject(object);
    return cadastralPassport;
  }

}
