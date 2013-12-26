/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c.enclosure.grp;

import ru.codeinside.gws.api.ExchangeContext;

import static ru.grp.RequestGRP.Request.RequiredData;
import static ru.grp.RequestGRP.Request.RequiredData.RequiredDataRealty;

/**
 * Запрос выписки из Единого государственного реестра прав на недвижимое имущество и сделок с ним
 */
public class ObjectRightEnclosureBuilder  extends DocumentContentEnclosureBuilder{

  public ObjectRightEnclosureBuilder(ExchangeContext ctx) {
    super(ctx);
  }

  @Override
  protected RequiredData createRequiredData() {
    final RequiredData requiredData = new RequiredData();
    RequiredDataRealty dataRealty = new RequiredDataRealty();
    requiredData.setRequiredDataRealty(dataRealty);
    RequiredDataRealty.ExtractRealty extractRealty = new RequiredDataRealty.ExtractRealty();
    dataRealty.setExtractRealty(extractRealty);
    if (ctx.getLongFromContext("parcels") > 0L) {
      extractRealty.getObjects().addAll(getParcelsObjectsFromContext());
    } else {
      extractRealty.getObjects().addAll(getObjectsFromContext());
    }
    return requiredData;
  }
}
