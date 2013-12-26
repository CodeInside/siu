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

/**
 * Подготавливает вложение для запроса
 * О кадастровом плане территории
 */
public class ParcelCadastralPlanRequest extends RequestEnclosureBuilder implements EnclosureRequestBuilder {
  public ParcelCadastralPlanRequest(ExchangeContext ctx) {
    super(ctx);
  }

  @Override
  protected RequestGKN.Request.RequiredData createRequiredData() {
    final RequestGKN.Request.RequiredData requiredData = new RequestGKN.Request.RequiredData();
    RequestGKN.Request.RequiredData.KPT kpt = new RequestGKN.Request.RequiredData.KPT();
    kpt.setCadastralNumber( ctx.getStrFromContext("kptCadastralNumber"));
    kpt.setOrient(ctx.getStrFromContext("kptOrient"));

    requiredData.setKPT(kpt);
    return requiredData;
  }


}
