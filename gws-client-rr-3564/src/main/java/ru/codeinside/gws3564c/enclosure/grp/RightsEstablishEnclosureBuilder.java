/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c.enclosure.grp;


import ru.codeinside.gws.api.ExchangeContext;
import ru.grp.TRequiredObject;

import java.util.List;

import static ru.grp.RequestGRP.Request.RequiredData;

/**
 * Выписка из Единого государственного реестра прав на недвижимое имущество и сделок с ним о переходе
 * прав на объект недвижимого имущества
 */
public class RightsEstablishEnclosureBuilder extends DocumentContentEnclosureBuilder {
  public RightsEstablishEnclosureBuilder(ExchangeContext ctx) {
    super(ctx);
  }

  @Override
  protected RequiredData createRequiredData() {
    RequiredData requiredData = new RequiredData();
    RequiredData.RequiredDataRealty dataRealty = new RequiredData.RequiredDataRealty();
    requiredData.setRequiredDataRealty(dataRealty);
    RequiredData.RequiredDataRealty.ExtractRealtyList extractRealtyList = new RequiredData.RequiredDataRealty.ExtractRealtyList();
    dataRealty.setExtractRealtyList(extractRealtyList);
    List<TRequiredObject> objects = extractRealtyList.getObjects();
    if (ctx.getLongFromContext("parcels") > 0L) {
      objects.addAll(getParcelsObjectsFromContext());
    } else {
      objects.addAll(getObjectsFromContext());
    }
    return requiredData;
  }
}
