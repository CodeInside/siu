package server.mcsv1002;

import org.junit.Test;
import server.mcsv1002.request.HouseBookExtractionRequest;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RequestDataFillerTest {
  @Test
  public void testFillRequest() throws Exception {
    HouseBookExtractionRequest request = new HouseBookExtractionRequest();
    request.setRegionCode("regionCode");
    request.setObjectDistrict("objectDistrict");
    request.setObjectCity("objectCity");
    request.setObjectTown("objectTown");
    request.setObjectStreet("objectStreet");
    request.setObjectHouse("objectHouse");
    request.setObjectCorpus("objectCorpus");
    request.setObjectBuilding("objectBuilding");
    request.setObjectFlat("objectFlat");
    request.setObjectCadastrNumber("objectCadastrNumber");
    request.setObjectConditNumber("objectConditNumber");

    Map<String, Object> context = request.makeMapValue();

    assertEquals("regionCode", context.get("result_regionCode"));
    assertEquals("objectDistrict", context.get("result_objectDistrict"));
    assertEquals("objectCity", context.get("result_objectCity"));
    assertEquals("objectTown", context.get("result_objectTown"));
    assertEquals("objectStreet", context.get("result_objectStreet"));
    assertEquals("objectHouse", context.get("result_objectHouse"));
    assertEquals("objectCorpus", context.get("result_objectCorpus"));
    assertEquals("objectBuilding", context.get("result_objectBuilding"));
    assertEquals("objectFlat", context.get("result_objectFlat"));
    assertEquals("objectCadastrNumber", context.get("result_objectCadastrNumber"));
    assertEquals("objectConditNumber", context.get("result_objectConditNumber"));

  }
}
