/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c.enclosure.grp;

import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws3564c.DeclarantType;
import ru.grp.*;

import static ru.grp.RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubjectRegion;
import static ru.grp.RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubjectRegion.Territory;
import static ru.grp.TOrganizationOwner.Names;

/**
 * О правах отдельного лица на имеющиеся у него объекты недвижимости
 */
public class SubjectRightsEnclosureBuilder extends EnclosureGRPBuilder {
  public SubjectRightsEnclosureBuilder(ExchangeContext ctx) {
    super(ctx);
  }

  @Override
  protected RequestGRP.Request.RequiredData createRequiredData() {
    final RequestGRP.Request.RequiredData requiredData = new RequestGRP.Request.RequiredData();
    final RequestGRP.Request.RequiredData.RequiredDataSubject requiredDataSubject = new RequestGRP.Request.RequiredData.RequiredDataSubject();
    final ExtractSubjectRegion extractSubjectRegion = new ExtractSubjectRegion();

    extractSubjectRegion.getOwner().add(getOwnerFromContext());
    extractSubjectRegion.setTerritory(fillTerritory());
    requiredDataSubject.setExtractSubjectRegion(extractSubjectRegion);
    requiredData.setRequiredDataSubject(requiredDataSubject);
    return requiredData;
  }

  protected TOwner getOwnerFromContext() {
    DeclarantType declarantType = DeclarantType.valueOf(ctx.getStrFromContext("ownerType"));
    final TOwner tOwner = new TOwner();
    switch (declarantType) {
      case GOVERNANCE: {
        TGovernanceOwner governance = new TGovernanceOwner();
        governance.setEMail(ctx.getStrFromContext("ownerGovernanceEmail"));
        governance.setName(ctx.getStrFromContext("ownerGovernanceName"));
        governance.setGovernanceCode(ctx.getStrFromContext("ownerGovernanceCode"));
        governance.setPhone(ctx.getStrFromContext("ownerGovernancePhone"));
        governance.setContactInfo(ctx.getStrFromContext("ownerGovernanceContactInfo"));
        governance.setLocation(createLocation("ownerLocation", ""));
        governance.setNames(getGovernanceNames());
        tOwner.setGovernanceO(governance);
        break;
      }

      case PERSON: {
        final TPersonOwner tPersonOwner = new TPersonOwner();
        fillPersonOwner(tPersonOwner, "ownerPerson", "");
        tPersonOwner.setFIOList(getFioList());
        tOwner.setPersonO(tPersonOwner);
        // todo обеспечить заполненние предыдущих документов
        break;
      }
      case ORGANISATION:
        TOrganizationOwner organization = new TOrganizationOwner();
        tOwner.setOrganizationO(organization);
        formOrganisation("ownerLegalPerson", organization);
        organization.setDocument(null);
        organization.setNames(getOrganizationNamesFromContext("ownerLegalPerson"));
        organization.setLocation(createLocation("ownerLocation", ""));
        //todo обеспечить заполнение предыдущих наименований
        break;
      default:
        throw new IllegalStateException("Owner type is not defined");
    }
    return tOwner;
  }

  private Names getOrganizationNamesFromContext(String type) {
    Names names = new Names();
    names.getName().add(ctx.getStrFromContext(type + "Name"));
    return names;
  }

  private TGovernanceOwner.Names getGovernanceNames() {
    TGovernanceOwner.Names names = new TGovernanceOwner.Names();
    names.getName().add(ctx.getStrFromContext("ownerGovernanceName"));
    return names;
  }

  TPersonOwner.FIOList getFioList() {
    long countFioList = ctx.getLongFromContext("fioList");
    if (countFioList == 0) return null; // todo реализовать заполнение
    return new TPersonOwner.FIOList();
  }

  Territory fillTerritory() {
    long countRegions = ctx.getLongFromContext("territory");
    final Territory territory = new Territory();
    if (countRegions == 0) {
      territory.setTerritoryRussia(true);
      return territory;
    }

    final Territory.Regions regions = new Territory.Regions();
    for (int idx = 1; idx <= countRegions; idx++) {
      regions.getRegion().add(ctx.getStrFromContext("territoryCode_" + idx));
    }
    territory.setRegions(regions);
    return territory;
  }


}
