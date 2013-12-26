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
import ru.gkn.TObjectLot;

public class ParcelValueRequest extends RequestEnclosureBuilder implements EnclosureRequestBuilder {


  public ParcelValueRequest(ExchangeContext ctx) {
    super(ctx);
  }

  @Override
  protected RequestGKN.Request.RequiredData createRequiredData() {
    RequestGKN.Request.RequiredData requiredData = new RequestGKN.Request.RequiredData();
    RequestGKN.Request.RequiredData.KSZUKS kszuks = new RequestGKN.Request.RequiredData.KSZUKS();
    TObjectLot objectLot = new TObjectLot();
    objectLot.setObjKind("002001001000");
    objectLot.setLocation(setLocation("cadastralPassport"));
    kszuks.setObjectLot(objectLot);
    requiredData.setKSZUKS(kszuks);
    return requiredData;
  }

}
