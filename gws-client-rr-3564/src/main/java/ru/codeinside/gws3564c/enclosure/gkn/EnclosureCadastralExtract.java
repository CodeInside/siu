/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c.enclosure.gkn;

import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws3564c.context.TypedContext;
import ru.codeinside.gws3564c.enclosure.EnclosureRequestBuilder;
import ru.gkn.*;

/**
 * О кадастровой выписки об объекте недвижимости
 */
public class EnclosureCadastralExtract extends RequestEnclosureBuilder implements EnclosureRequestBuilder {

  public EnclosureCadastralExtract(ExchangeContext ctx) {
    super(ctx);
  }

  @Override
  protected RequestGKN.Request.RequiredData createRequiredData() {
    final RequestGKN.Request.RequiredData requiredData = new RequestGKN.Request.RequiredData();
    //KV
    final RequestGKN.Request.RequiredData.KV kv = new RequestGKN.Request.RequiredData.KV();

    TObjectLot objectLot = new TObjectLot();
    objectLot.setObjKind("002001001000");
    objectLot.setLocation(setLocation("cadastralPassport"));
    kv.setObjectLot(objectLot);
    kv.setKV1(true);
    if (ctx.getBooleanValue("KV2"))
      kv.setKV2(true);
    if (ctx.getBooleanValue("KV3"))
      kv.setKV3(true);
    if (ctx.getBooleanValue("KV4"))
      kv.setKV4(true);
    if (ctx.getBooleanValue("KV5"))
      kv.setKV5(true);
    if (ctx.getBooleanValue("KV6"))
      kv.setKV6(true);

    requiredData.setKV(kv);
    return requiredData;
  }


}
