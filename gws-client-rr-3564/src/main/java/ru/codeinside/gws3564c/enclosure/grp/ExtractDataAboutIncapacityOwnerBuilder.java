/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c.enclosure.grp;

import ru.codeinside.gws.api.ExchangeContext;

import static ru.grp.RequestGRP.Request.RequiredData;
import static ru.grp.RequestGRP.Request.RequiredData.RequiredDataIncapacity.IncapacityOwner;

/**
 * Строит запрос выписки выписки из Единого государственного реестра прав на недвижимое имущество
 * и сделок с ним о признании правообладателя недееспособным
 * или ограничено дееспособным
 */
public class ExtractDataAboutIncapacityOwnerBuilder extends EnclosureGRPBuilder {
  public ExtractDataAboutIncapacityOwnerBuilder(ExchangeContext ctx) {
    super(ctx);
  }

  @Override
  protected RequiredData createRequiredData() {
    final RequiredData requiredData = new RequiredData();
    final RequiredData.RequiredDataIncapacity dataIncapacity = new RequiredData.RequiredDataIncapacity();
    requiredData.setRequiredDataIncapacity(dataIncapacity);
    long countOwners = ctx.getLongFromContext("incapacityOwner");
    for (long ownerIdx = 1; ownerIdx <= countOwners; ownerIdx++){
      IncapacityOwner owner = new IncapacityOwner();
      fillPersonOwner(owner, "ownerPerson", "_" + ownerIdx);
      dataIncapacity.getIncapacityOwner().add(owner);
    }
    return requiredData;
  }
}
