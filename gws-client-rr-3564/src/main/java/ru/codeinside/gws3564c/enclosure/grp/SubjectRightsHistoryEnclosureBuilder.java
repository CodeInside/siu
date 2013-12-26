/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c.enclosure.grp;

import ru.codeinside.gws.api.ExchangeContext;
import ru.grp.*;

import java.util.Date;

import static ru.grp.RequestGRP.Request.RequiredData;
import static ru.grp.RequestGRP.Request.RequiredData.RequiredDataSubject.ExtractSubject;

/**
 * Выписка из реестра о правах отдельного лица на имевшиеся (имеющиеся) у него объекты недвижимого имущества
 */
public class SubjectRightsHistoryEnclosureBuilder extends SubjectRightsEnclosureBuilder {
  public SubjectRightsHistoryEnclosureBuilder(ExchangeContext ctx) {
    super(ctx);
  }

  @Override
  protected RequiredData createRequiredData() {
    RequiredData requiredData = new RequiredData();
    RequiredData.RequiredDataSubject dataSubject = new RequiredData.RequiredDataSubject();
    ExtractSubject extractSubject = new ExtractSubject();

    dataSubject.setExtractSubject(extractSubject);
    requiredData.setRequiredDataSubject(dataSubject);
    extractSubject.getOwners().add(getOwnerFromContext());
    extractSubject.setDataPeriod(getRequestPeriodFromContext());
    extractSubject.setRealtyType(getRealtyTypeFromContext());
    extractSubject.setTerritory(getTerritoryDataFromContext());
    return requiredData;
  }

  private ExtractSubject.Territory getTerritoryDataFromContext() {
    ExtractSubject.Territory territory = new ExtractSubject.Territory();
    long countRegions = ctx.getLongFromContext("territory");
    if (countRegions == 0L) {
      territory.setTerritoryRussia(true);
      return territory;
    }
    final ExtractSubject.Territory.Regions regions = new ExtractSubject.Territory.Regions();
    for (int idx = 1; idx <= countRegions; idx++) {
      regions.getRegion().add(ctx.getStrFromContext("territoryCode_" + idx));
    }
    territory.setRegions(regions);
    return territory;
  }

  private ExtractSubject.RealtyType getRealtyTypeFromContext() {
    ExtractSubject.RealtyType realtyType = new ExtractSubject.RealtyType();
    RealtyKind realtyKind = RealtyKind.valueOf(ctx.getStrFromContext("objectKind"));
    switch (realtyKind) {
      case ALL:
        realtyType.setRealtyTypeAll(true);
        break;
      case PARCEL:
        realtyType.getRealtyTypeText().add(getParcelSubjectData());
        break;
      case ROOM:
        realtyType.getRealtyTypeText().add(getRoomSubjectData());
        break;
      case BUILDING:
        realtyType.getRealtyTypeText().add(getBuildingSubjectData());
        break;
      case OTHER:
        realtyType.getRealtyTypeText().add(getOtherSubjectData());
    }
    return realtyType;
  }

  private TExtractSubjectObjectType getOtherSubjectData() {
    TExtractSubjectObjectType objectType = new TExtractSubjectObjectType();
    objectType.setObjKind(ctx.getStrFromContext("objectOtherKind"));
    return objectType;
  }

  private TExtractSubjectObjectType getBuildingSubjectData() {
    TExtractSubjectObjectType objectType = new TExtractSubjectObjectType();
    TExtractBuilding building = new TExtractBuilding();
    fillBuildingFromContext(building, "");
    objectType.setBuilding(building);
    return objectType;
  }

  private TExtractSubjectObjectType getRoomSubjectData() {
    TExtractSubjectObjectType objectType = new TExtractSubjectObjectType();
    TExtractRoom room = new TExtractRoom();
    fillRoomDataFromContext(room, "");
    objectType.setRoom(room);
    return objectType;
  }

  private TExtractSubjectObjectType getParcelSubjectData() {
    TExtractSubjectObjectType subjectObjectType = new TExtractSubjectObjectType();
    TExtractParcel parcel = new TExtractParcel();
    parcel.setCategory(ctx.getStrFromContext("parcelCategory"));
    subjectObjectType.setParcel(parcel);
    return subjectObjectType;
  }

  enum RealtyKind {
    ALL, PARCEL, ROOM, OTHER, BUILDING
  }

  private ExtractSubject.DataPeriod getRequestPeriodFromContext() {
    ExtractSubject.DataPeriod dataPeriod = new ExtractSubject.DataPeriod();
    Date startDate = ctx.getDateFromContext("request_interval_start");
    Date endDate = ctx.getDateFromContext("request_interval_end");
    if (startDate != null && endDate != null) {
      ExtractSubject.DataPeriod.Interval interval = new ExtractSubject.DataPeriod.Interval();
      interval.setDateEnd(date(endDate));
      interval.setDateStart(date(startDate));
      dataPeriod.setInterval(interval);
    } else if (startDate != null) {
      dataPeriod.setDateStart(date(startDate));
    } else if (endDate != null) {
      dataPeriod.setDateEnd(date(endDate));
    } else {
      dataPeriod.setDate(date(new Date()));
    }
    return dataPeriod;
  }
}
