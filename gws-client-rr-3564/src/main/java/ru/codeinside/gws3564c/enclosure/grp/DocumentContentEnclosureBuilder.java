/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c.enclosure.grp;

import ru.codeinside.gws.api.ExchangeContext;
import ru.grp.*;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static ru.grp.RequestGRP.Request.RequiredData;
import static ru.grp.RequestGRP.Request.RequiredData.RequiredDataDocument.ContentDocument;
import static ru.grp.TRequiredObject.Parcel.Description;

/**
 * Строит запрос для получения  справки о содержании правоустанавливающего документа
 */
public class DocumentContentEnclosureBuilder extends EnclosureGRPBuilder {
  public DocumentContentEnclosureBuilder(ExchangeContext ctx) {
    super(ctx);
  }

  @Override
  protected RequiredData createRequiredData() {
    final RequiredData requiredData = new RequiredData();
    final RequiredData.RequiredDataDocument requiredDataDocument = new RequiredData.RequiredDataDocument();
    requiredData.setRequiredDataDocument(requiredDataDocument);
    final ContentDocument contentDocument = getContentDocumentFromContext("contentDocument");
    requiredDataDocument.setContentDocument(contentDocument);
    final ContentDocument.Objects objects = new ContentDocument.Objects();
    contentDocument.setObjects(objects);
    if (ctx.getLongFromContext("parcels") > 0L) {
      objects.getObject().addAll(getParcelsObjectsFromContext());
    } else {
      objects.getObject().addAll(getObjectsFromContext());
    }
    return requiredData;
  }

  private ContentDocument getContentDocumentFromContext(String prefix) {
    ContentDocument contentDocument = new ContentDocument();
    contentDocument.setName(ctx.getStrFromContext(prefix + "PDocumentName"));
    contentDocument.setCodeDocument(ctx.getStrFromContext(prefix + "PDocumentCode"));
    contentDocument.setSeries(ctx.getStrFromContext(prefix + "PDocumentSeries"));
    contentDocument.setNumber(ctx.getStrFromContext(prefix + "PDocumentNumber"));
    XMLGregorianCalendar date = date(ctx.getDateFromContext(prefix + "PDocumentDate"));
    contentDocument.setDate(date);
    contentDocument.setIssueOrgan(ctx.getStrFromContext(prefix + "PDocumentIssueOrgan"));
    return contentDocument;
  }

  protected Collection<TRequiredObject> getObjectsFromContext() {
    List<TRequiredObject> list = new LinkedList<TRequiredObject>();
    final Long countObjects = ctx.getLongFromContext("objects");
    for (long idx = 1; idx <= countObjects; idx++) {
      TRequiredObject object = new TRequiredObject();
      object.setObject(getObjectFromContext("_" + idx));
      list.add(object);
    }
    return list;
  }

  private TRequiredObject.Object getObjectFromContext(String suffix) {
    final TRequiredObject.Object object = new TRequiredObject.Object();
    object.setLocation(createLocation("objectLocation", suffix));
    object.setArea(getAreaFromContext("object", suffix));
    object.setCadastralNumbers(getObjectCadastralNumbers(suffix));

    object.setObjKind(getObjectKindFromContext(suffix));
    object.setDopInfo(new TDopInfo());
    return object;
  }

  private TRequiredObject.Object.CadastralNumbers getObjectCadastralNumbers(String suffix) {

    String cadastralNumber = ctx.getStrFromContext("objectCadastralNumber" + suffix);
    if (cadastralNumber == null || "".equals(cadastralNumber)) return null;

    final TRequiredObject.Object.CadastralNumbers cadastralNumbers = new TRequiredObject.Object.CadastralNumbers();
    cadastralNumbers.setCadastralNumber(cadastralNumber);
    return cadastralNumbers;
  }

  private TExtractObjectType getObjectKindFromContext(String suffix) {
    final TExtractObjectType objectType = new TExtractObjectType();
    ObjectKind kind = ObjectKind.valueOf(ctx.getStrFromContext("objectKind" + suffix));
    switch (kind) {
      case ROOM:
        TExtractRoomRequired room = new TExtractRoomRequired();
        fillRoomDataFromContext(room, suffix);
        objectType.setRoom(room);
        break;
      case BUILDING:
        TExtractBuildingRequired building = new TExtractBuildingRequired();
        fillBuildingFromContext(building, suffix);
        objectType.setBuilding(building);
        break;
      case OTHER:
        objectType.setObjKind(ctx.getStrFromContext("objectOtherKind" + suffix));
    }
    return objectType;
  }





  enum ObjectKind {
    ROOM, BUILDING, OTHER
  }

  protected List<TRequiredObject> getParcelsObjectsFromContext() {
    final LinkedList<TRequiredObject> objects = new LinkedList<TRequiredObject>();
    final Long countObjects = ctx.getLongFromContext("parcels");
    for (long idx = 1; idx <= countObjects; idx++) {
      TRequiredObject object = new TRequiredObject();
      object.setParcel(getParcelFromContext("_" + idx));
      objects.add(object);
    }
    return objects;
  }

  public TRequiredObject.Parcel getParcelFromContext(String suffix) {
    final TRequiredObject.Parcel parcel = new TRequiredObject.Parcel();
    final Description description = new Description();
    description.setAddress(getParcelAddressFromContext(suffix));
    parcel.setDescription(description);
    return parcel;
  }

  private Description.Address getParcelAddressFromContext(String suffix) {
    final Description.Address address = new Description.Address();
    address.setAreas(getAreaFromContext("parcel", suffix));
    address.setCadastralNumber(ctx.getStrFromContext("parcelCadastralNumber" + suffix));
    address.setLocation(createLocation("parcelLocation", suffix));
    return address;
  }

  private TArea getAreaFromContext(final String prefix, String suffix) {
    final TArea area = new TArea();
    area.setUnit(ctx.getStrFromContext(prefix + "AreaUnit" + suffix));
    area.setValue(BigDecimal.valueOf(getDoubleFromContext(prefix + "AreaValue" + suffix)));
    return area;
  }

  private double getDoubleFromContext(String varName) {
    String stringValue = ctx.getStrFromContext(varName).replace(',', '.');
    return Double.parseDouble(stringValue);
  }


}
