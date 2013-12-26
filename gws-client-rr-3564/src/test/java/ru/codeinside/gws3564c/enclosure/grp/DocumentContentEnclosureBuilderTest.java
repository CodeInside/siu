/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c.enclosure.grp;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws3564c.DummyContext;
import ru.grp.RequestGRP;
import ru.grp.TArea;
import ru.grp.TRequiredObject;

import java.math.BigDecimal;
import java.util.List;


import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Тестируем построение вложения для запроса содержимого правоустанавливающего документа
 */
public class DocumentContentEnclosureBuilderTest {
  private ExchangeContext context;
  private DocumentContentEnclosureBuilder enclosureBuilder;

  @Before
  public void setUp() throws Exception {
    context = new DummyContext();
    enclosureBuilder = new DocumentContentEnclosureBuilder(context);
  }

  @Test
  public void testFillRequiredData() throws Exception {
    fillContext();
    RequestGRP request = enclosureBuilder.createRequest("id");
    final RequestGRP.Request.RequiredData.RequiredDataDocument requiredDataDocument = request.getRequest()
                                                                                             .getRequiredData()
                                                                                             .getRequiredDataDocument();
    assertThat(requiredDataDocument, notNullValue());
    assertThat(requiredDataDocument.getContentDocument(), notNullValue());
  }

  @Test
  public void testFillRequiredDataWhenObjectIsParcel() throws Exception {
    fillContext();
    // fill parcel data
    context.setVariable("parcels", 2l);
    context.setVariable("parcelCadastralNumber_1", "cadastralNumber");
    context.setVariable("parcelAreaValue_1", "23.09");
    context.setVariable("parcelAreaUnit_1", "unit");
    context.setVariable("parcelLocationOKATO_1", "okato_1");
    context.setVariable("parcelLocationOKATO_2", "okato_2");

    context.setVariable("parcelCadastralNumber_2", "222cadastralNumber");
    context.setVariable("parcelAreaValue_2", "25.09");
    context.setVariable("parcelAreaUnit_2", "unit222");
    RequestGRP request = enclosureBuilder.createRequest("id");
    final List<TRequiredObject> objects = request.getRequest()
                                                 .getRequiredData()
                                                 .getRequiredDataDocument()
                                                 .getContentDocument()
                                                 .getObjects()
                                                 .getObject();
    assertThat(objects.size(), equalTo(2));
    // verify first object
    TRequiredObject.Parcel parcel = objects.get(0).getParcel();
    final TRequiredObject.Parcel.Description.Address address = parcel.getDescription().getAddress();
    assertThat(address.getLocation().getCodeOKATO(), equalTo("okato_1"));
    assertThat(address.getCadastralNumber(), Matchers.equalTo("cadastralNumber"));
    TArea areas = parcel.getDescription().getAddress().getAreas();
    assertThat(areas.getValue(), closeTo(BigDecimal.valueOf(23.09), BigDecimal.valueOf(.001)));
    assertThat(areas.getUnit(), equalTo("unit"));
    // verify second object
    parcel = objects.get(1).getParcel();
    String cadastralNumber = parcel.getDescription().getAddress().getCadastralNumber();
    assertThat(cadastralNumber, equalTo("222cadastralNumber"));
    areas = parcel.getDescription().getAddress().getAreas();
    assertThat(areas.getValue(), closeTo(BigDecimal.valueOf(25.09), BigDecimal.valueOf(0.001)));
    assertThat(areas.getUnit(), equalTo("unit222"));
  }

  @Test
  public void testFillRequiredDataNonDomesticRoomObjectData() throws Exception {
    fillContext();
    // fill parcel data
    context.setVariable("objects", 1l);
    context.setVariable("objectCadastralNumber_1", "cadastralNumber");
    context.setVariable("objectAreaValue_1", "23.09");
    context.setVariable("objectAreaUnit_1", "unit");
    context.setVariable("objectLocationOKATO_1", "okato_1");
    context.setVariable("objectKind_1", "ROOM");
    context.setVariable("objectRoomKind_1", "NONDOMESTIC");

    RequestGRP request = enclosureBuilder.createRequest("id");
    final List<TRequiredObject> objects = request.getRequest()
                                                 .getRequiredData()
                                                 .getRequiredDataDocument()
                                                 .getContentDocument()
                                                 .getObjects()
                                                 .getObject();
    assertThat(objects.size(), equalTo(1));
    final TRequiredObject.Object object = objects.get(0).getObject();
    assertThat(object.getLocation().getCodeOKATO(), equalTo("okato_1"));
    assertThat(object.getArea().getUnit(), equalTo("unit"));
    assertThat(object.getArea().getValue(), closeTo(BigDecimal.valueOf(23.09), BigDecimal.valueOf(0.001)));
    assertThat(object.getObjKind().getRoom().isIsNondomestic(), equalTo(true));
    assertThat(object.getObjKind().getRoom().isIsFlat(), nullValue());
    assertThat(object.getObjKind().getRoom().isIsRoom(), nullValue());
  }

  @Test
  public void testFillRequiredDataFlatRoomObjectData() throws Exception {
    fillContext();
    // fill parcel data
    context.setVariable("objects", 1l);
    context.setVariable("objectCadastralNumber_1", "cadastralNumber");
    context.setVariable("objectAreaValue_1", "23.09");
    context.setVariable("objectAreaUnit_1", "unit");
    context.setVariable("objectLocationOKATO_1", "okato_1");
    context.setVariable("objectKind_1", "ROOM");
    context.setVariable("objectRoomKind_1", "FLAT");

    RequestGRP request = enclosureBuilder.createRequest("id");
    final List<TRequiredObject> objects = request.getRequest()
                                                 .getRequiredData()
                                                 .getRequiredDataDocument()
                                                 .getContentDocument()
                                                 .getObjects()
                                                 .getObject();
    assertThat(objects.size(), equalTo(1));
    final TRequiredObject.Object object = objects.get(0).getObject();

    assertThat(object.getObjKind().getRoom().isIsNondomestic(), nullValue());
    assertThat(object.getObjKind().getRoom().isIsFlat(), equalTo(true));
    assertThat(object.getObjKind().getRoom().isIsRoom(), nullValue());
  }

  @Test
  public void testFillRequiredDataRoomRoomObjectData() throws Exception {
    fillContext();
    // fill parcel data
    context.setVariable("objects", 1l);
    context.setVariable("objectCadastralNumber_1", "cadastralNumber");
    context.setVariable("objectAreaValue_1", "23.09");
    context.setVariable("objectAreaUnit_1", "unit");
    context.setVariable("objectLocationOKATO_1", "okato_1");
    context.setVariable("objectKind_1", "ROOM");
    context.setVariable("objectRoomKind_1", "ROOM");

    RequestGRP request = enclosureBuilder.createRequest("id");
    final List<TRequiredObject> objects = request.getRequest()
                                                 .getRequiredData()
                                                 .getRequiredDataDocument()
                                                 .getContentDocument()
                                                 .getObjects()
                                                 .getObject();
    assertThat(objects.size(), equalTo(1));
    final TRequiredObject.Object object = objects.get(0).getObject();

    assertThat(object.getObjKind().getRoom().isIsNondomestic(), nullValue());
    assertThat(object.getObjKind().getRoom().isIsFlat(), nullValue());
    assertThat(object.getObjKind().getRoom().isIsRoom(), equalTo(true));
  }

  @Test
  public void testFillRequiredDataBuildingNonDomesticObject() throws Exception {
    fillContext();
    // fill parcel data
    context.setVariable("objects", 1l);
    context.setVariable("objectCadastralNumber_1", "cadastralNumber");
    context.setVariable("objectAreaValue_1", "23.09");
    context.setVariable("objectAreaUnit_1", "unit");
    context.setVariable("objectLocationOKATO_1", "okato_1");
    context.setVariable("objectKind_1", "BUILDING");
    context.setVariable("buildingKind_1", "NONDOMESTIC");


    RequestGRP request = enclosureBuilder.createRequest("id");
    final List<TRequiredObject> objects = request.getRequest()
                                                 .getRequiredData()
                                                 .getRequiredDataDocument()
                                                 .getContentDocument()
                                                 .getObjects()
                                                 .getObject();
    assertThat(objects.size(), equalTo(1));
    final TRequiredObject.Object object = objects.get(0).getObject();

    assertThat(object.getObjKind().getBuilding().isIsNondomestic(), equalTo(true));
    assertThat(object.getObjKind().getBuilding().isIsApartments(), nullValue());
    assertThat(object.getObjKind().getBuilding().isIsLiving(), nullValue());
  }

  @Test
  public void testFillRequiredDataBuildingLivingObject() throws Exception {
    fillContext();
    // fill parcel data
    context.setVariable("objects", 1l);
    context.setVariable("objectCadastralNumber_1", "cadastralNumber");
    context.setVariable("objectAreaValue_1", "23.09");
    context.setVariable("objectAreaUnit_1", "unit");
    context.setVariable("objectLocationOKATO_1", "okato_1");
    context.setVariable("objectKind_1", "BUILDING");
    context.setVariable("buildingKind_1", "LIVING");


    RequestGRP request = enclosureBuilder.createRequest("id");
    final List<TRequiredObject> objects = request.getRequest()
                                                 .getRequiredData()
                                                 .getRequiredDataDocument()
                                                 .getContentDocument()
                                                 .getObjects()
                                                 .getObject();
    assertThat(objects.size(), equalTo(1));
    final TRequiredObject.Object object = objects.get(0).getObject();

    assertThat(object.getObjKind().getBuilding().isIsNondomestic(), nullValue());
    assertThat(object.getObjKind().getBuilding().isIsApartments(), nullValue());
    assertThat(object.getObjKind().getBuilding().isIsLiving(), equalTo(true));
  }

  @Test
  public void testFillRequiredDataOtherObject() throws Exception {
    fillContext();
    // fill parcel data
    context.setVariable("objects", 1l);
    context.setVariable("objectCadastralNumber_1", "cadastralNumber");
    context.setVariable("objectAreaValue_1", "23.09");
    context.setVariable("objectAreaUnit_1", "unit");
    context.setVariable("objectLocationOKATO_1", "okato_1");
    context.setVariable("objectKind_1", "OTHER");
    context.setVariable("objectOtherKind_1", "123131313");


    RequestGRP request = enclosureBuilder.createRequest("id");
    final List<TRequiredObject> objects = request.getRequest()
                                                 .getRequiredData()
                                                 .getRequiredDataDocument()
                                                 .getContentDocument()
                                                 .getObjects()
                                                 .getObject();
    assertThat(objects.size(), equalTo(1));
    final TRequiredObject.Object object = objects.get(0).getObject();
    assertThat(object.getObjKind().getObjKind(), equalTo("123131313"));
  }

  @Test
  public void testFillRequiredDataBuildingApartmentsObject() throws Exception {
    fillContext();
    // fill parcel data
    context.setVariable("objects", 1l);
    context.setVariable("objectCadastralNumber_1", "cadastralNumber");
    context.setVariable("objectAreaValue_1", "23.09");
    context.setVariable("objectAreaUnit_1", "unit");
    context.setVariable("objectLocationOKATO_1", "okato_1");
    context.setVariable("objectKind_1", "BUILDING");
    context.setVariable("buildingKind_1", "APARTMENTS");


    RequestGRP request = enclosureBuilder.createRequest("id");
    final List<TRequiredObject> objects = request.getRequest()
                                                 .getRequiredData()
                                                 .getRequiredDataDocument()
                                                 .getContentDocument()
                                                 .getObjects()
                                                 .getObject();
    assertThat(objects.size(), equalTo(1));
    final TRequiredObject.Object object = objects.get(0).getObject();

    assertThat(object.getObjKind().getBuilding().isIsNondomestic(), nullValue());
    assertThat(object.getObjKind().getBuilding().isIsApartments(), equalTo(true));
    assertThat(object.getObjKind().getBuilding().isIsLiving(), nullValue());
  }

  private void fillContext() {
    //declarant
    context.setVariable("declarantType", "GOVERNANCE");
    context.setVariable("declKind", "357007000000");      //Required
    context.setVariable("declGovernanceName", "Пенсионный фонд");
    context.setVariable("declGovernanceCode", "007001001001");
    context.setVariable("declGovernanceEmail", "test@test.ru");

    //AppliedDocument
    context.setVariable("applied", 1l);
    context.setVariable("appliedADocumentCode_1", "558102100000");
    context.setVariable("appliedADocumentName_1",
                        "Запрос о предоставлении сведений, содержащихся в Едином государственном реестре прав на недвижимое имущество и сделок с ним");
    context.setVariable("appliedADocumentNumber_1", "26-0-1-21/4001/2011-166");
    context.setVariable("appliedADocumentDate_1", TestUtils.getDateValue("2012-07-24"));
    context.setVariable("appliedADocumentOriginalQuantity_1", 1L);
    context.setVariable("appliedADocumentOriginalQuantitySheet_1", 1L);
    context.setVariable("appliedADocumentCopyQuantity_1", 1L);
    context.setVariable("appliedADocumentCopyQuantitySheet_1", 1L);
    //ReasonFreeDocument
    context.setVariable("freeACodeDocument", "555005000000");
    context.setVariable("freeADocumentName",
                        "Запрос о предоставлении сведений, содержащихся в Едином государственном реестре прав на недвижимое имущество и сделок с ним");
    context.setVariable("freeADocumentNumber", "");
    context.setVariable("freeADocumentDate", TestUtils.getDateValue("2012-07-24"));
    context.setVariable("freeADocumentOriginalQuantity", "1");
    context.setVariable("freeADocumentOriginalQuantitySheet", "1");
    context.setVariable("isPaymentFree", false);
    context.setVariable("freePayment", 1L);
    context.setVariable("payment", 0L);
    context.setVariable("territory", 0L);
    context.setVariable("smevTest", "Первичный запрос");

    // object location
    context.setVariable("objectLocationOKATO_1", "objectLocationOKATO"); //тип string
    context.setVariable("objectLocationCLADR_1", "objectLocationCLADR"); //тип string
    context.setVariable("objectLocationPostalCode_1", "objectLocationPostalCode"); //тип string
    context.setVariable("objectLocationRegion_1", "objectLocationRegion"); //тип string
    context.setVariable("objectLocationDistrictName_1", "objectLocationDistrictName"); //тип string
    context.setVariable("objectLocationDistrictType_1", "р-н"); //тип enum

    context.setVariable("objectLocationCityName_1", "objectLocationCityName"); //тип string
    context.setVariable("objectLocationDCity_1", "г"); //тип string
    context.setVariable("objectLocationUrbanDistictName_1", "objectLocationUrbanDistictName"); //тип string
    context.setVariable("objectLocationUrbanDistictType_1", "р-н"); //тип string

    context.setVariable("objectLocationSovietVillageName_1", "objectLocationSovietVillageName"); //тип string
    context.setVariable("objectLocationSovietVillageType_1", "волость"); //тип string

    context.setVariable("objectLocationLocalityName_1", "objectLocationLocalityName"); //тип string
    context.setVariable("objectLocationLocalityType_1", "аал"); //тип string

    context.setVariable("objectLocationStreetName_1", "objectLocationStreetName"); //тип string
    context.setVariable("objectLocationDStreets_1", "аллея"); //тип enum

    context.setVariable("objectLocationLocationLevel1Type_1", "д"); //тип enum

    context.setVariable("objectLocationLocationLevel1Value_1", "objectLocationLocationLevel1Value"); //тип string
    context.setVariable("objectLocationLocationLevel2Type_1", "корп"); //тип enum

    context.setVariable("objectLocationLocationLevel2Value_1", "objectLocationLocationLevel2Value"); //тип string
    context.setVariable("objectLocationLocationLevel3Type_1", "блок"); //тип enum

    context.setVariable("objectLocationLocationLevel3Value_1", "objectLocationLocationLevel3Value"); //тип string
    context.setVariable("objectLocationLocationApartmentType_1", "кв"); //тип enum

    context.setVariable("objectLocationLocationApartmentValue_1", "objectLocationLocationApartmentValue"); //тип string
    context.setVariable("objectLocationLocationOther_1", "objectLocationLocationOther"); //тип string
    context.setVariable("objectLocationLocationNote_1", "objectLocationLocationNote"); //тип string
  }
}
