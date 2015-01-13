package server.mcsv1002;

import org.junit.Before;
import org.junit.Test;
import ru.codeinside.gws.api.ReceiptContext;
import server.mcsv1002.response.HousebookExtractionResponse;
import server.mcsv1002.response.Owner;
import server.mcsv1002.response.RegisteredPerson;

import javax.xml.datatype.XMLGregorianCalendar;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ResponseExtractorTest {
  private ReceiptContext ctx;
  private ResponseExtractor extractor;
  private Date ownerSince;
  private Date checkInDate;
  private Date checkOutDate;
  private Date docIssueDate;
  private Date personBirthday;

  @Before
  public void setUp() throws Exception {
    ctx = new DummyContext();
    extractor = new ResponseExtractor();

    ownerSince = new SimpleDateFormat("dd.MM.yyyy").parse("15.12.2012");
    checkInDate = new SimpleDateFormat("dd.MM.yyyy").parse("25.12.2011");
    checkOutDate = new SimpleDateFormat("dd.MM.yyyy").parse("25.11.2011");
    docIssueDate = new SimpleDateFormat("dd.MM.yyyy").parse("15.11.2011");
    personBirthday = new SimpleDateFormat("dd.MM.yyyy").parse("15.11.2011");
  }

  @Test
  public void testFillOwners() throws Exception {
    ctx.setVariable("owners", 1l);
    ctx.setVariable("owner_first_name_1", "owner_first_name_0");
    ctx.setVariable("owner_last_name_1", "owner_last_name_0");
    ctx.setVariable("owner_given_name_1", "owner_given_name_0");
    ctx.setVariable("owner_share_1", "owner_share_0");
    ctx.setVariable("owner_type_1", "owner_type_0");
    ctx.setVariable("owner_since_1", ownerSince);
    ctx.setVariable("owner_size_1", "owner_size_0");
    ctx.setVariable("request_is_correct", true);
    ctx.setVariable("is_order_rejected", false);
    final HousebookExtractionResponse response = extractor.convertToResponseObject(ctx);
    assertEquals(1, response.getOwner().size());
    final Owner owner = response.getOwner().get(0);
    assertEquals("owner_first_name_0", owner.getFirstName());
    assertEquals("owner_given_name_0", owner.getGivenName());
    assertEquals("owner_last_name_0", owner.getLastName());
    assertEquals("owner_share_0", owner.getShare());
    assertEquals("owner_type_0", owner.getType());
    assertEquals("owner_size_0", owner.getSize());
    checkXMLCalendar(owner.getSince(), 15, 12, 2012);
  }

  @Test
  public void testFillPerson() throws Exception {
    ctx.setVariable("is_order_rejected", false);
    ctx.setVariable("person", 1l);
    ctx.setVariable("person_last_name_1", "person_last_name_0");
    ctx.setVariable("person_first_name_1", "person_first_name_0");
    ctx.setVariable("person_given_name_1", "person_given_name_0");
    ctx.setVariable("person_birth_place_1", "person_birth_place_0");
    ctx.setVariable("person_birthday_1", personBirthday);
    ctx.setVariable("person_doc_type_1", "person_doc_type_0");
    ctx.setVariable("person_doc_series_1", "person_doc_series_0");
    ctx.setVariable("person_doc_number_1", "person_doc_number_0");
    ctx.setVariable("person_doc_issue_date_1", docIssueDate);
    ctx.setVariable("person_doc_issuer_1", "person_doc_issuer_0");
    ctx.setVariable("person_checkin_date_1", checkInDate);
    ctx.setVariable("person_checkout_date_1", checkOutDate);
    ctx.setVariable("request_is_correct", true);
    final HousebookExtractionResponse response = extractor.convertToResponseObject(ctx);
    assertEquals(1, response.getRegisteredPerson().size());
    final RegisteredPerson person = response.getRegisteredPerson().get(0);

    assertEquals("person_last_name_0", person.getLastName());
    assertEquals("person_first_name_0", person.getFirstName());
    assertEquals("person_given_name_0", person.getGivenName());
    assertEquals("person_birth_place_0", person.getBirthPlace());
    checkXMLCalendar(person.getBirthday(), 15, 11, 2011);
    assertEquals("person_doc_type_0", person.getDocType());
    assertEquals("person_doc_series_0", person.getDocSerie());
    assertEquals("person_doc_number_0", person.getDocNumber());
    checkXMLCalendar(person.getDocIssueDate(), 15, 11, 2011);
    assertEquals("person_doc_issuer_0", person.getDocIssuer());
    checkXMLCalendar(person.getCheckinDate(), 25, 12, 2011);
    checkXMLCalendar(person.getCheckoutDate(), 25, 11, 2011);
  }

  private void checkXMLCalendar(XMLGregorianCalendar since, int expectedDay, int expectedMonth, int expectedYear) {
    assertEquals(expectedMonth, since.getMonth());
    assertEquals(expectedDay, since.getDay());
    assertEquals(expectedYear, since.getYear());
  }

  @Test
  public void testComment() throws Exception {
    ctx.setVariable("is_order_rejected", false);
    final String commentText = "comment_text";
    ctx.setVariable("request_is_correct", true);
    ctx.setVariable("comment", commentText);
    final HousebookExtractionResponse response = extractor.convertToResponseObject(ctx);
    assertEquals(commentText, response.getComment());
  }

  @Test(expected = DeclarerException.class)
  public void testThrowExceptionWhenResponseMarkedAsWrong() throws Exception {
    ctx.setVariable("is_order_rejected", false);
    ctx.setVariable("request_is_correct", false);
    extractor.convertToResponseObject(ctx);
  }

  @Test(expected = DeclarerException.class)
  public void testThrowExceptionWhenOrderIsRejected() throws Exception {
    ctx.setVariable("is_order_rejected", null);
    ctx.setVariable("request_is_correct", true);
    extractor.convertToResponseObject(ctx);
  }

  @Test
  public void testFillRequestParams() throws Exception {
    ctx.setVariable("is_order_rejected", false);
    ctx.setVariable("request_is_correct", true);
    ctx.setVariable("regionCode", "regionCode");
    ctx.setVariable("objectDistrict", "objectDistrict");
    ctx.setVariable("objectCity", "objectCity");
    ctx.setVariable("objectTown", "objectTown");
    ctx.setVariable("objectStreet", "objectStreet");
    ctx.setVariable("objectHouse", "objectHouse");
    ctx.setVariable("objectCorpus", "objectCorpus");
    ctx.setVariable("objectBuilding", "objectBuilding");
    ctx.setVariable("objectFlat", "objectFlat");
    ctx.setVariable("objectCadastrNumber", "objectCadastrNumber");
    ctx.setVariable("objectConditNumber", "objectConditNumber");

    final HousebookExtractionResponse response = extractor.convertToResponseObject(ctx);

    assertEquals("regionCode", response.getRegionCode());
    assertEquals("objectDistrict", response.getObjectDistrict());
    assertEquals("objectCity", response.getObjectCity());
    assertEquals("objectTown", response.getObjectTown());
    assertEquals("objectStreet", response.getObjectStreet());
    assertEquals("objectHouse", response.getObjectHouse());
    assertEquals("objectCorpus", response.getObjectCorpus());
    assertEquals("objectBuilding", response.getObjectBuilding());
    assertEquals("objectFlat", response.getObjectFlat());
    assertEquals("objectCadastrNumber", response.getObjectCadastrNumber());
    assertEquals("objectConditNumber", response.getObjectConditNumber());
  }
}
