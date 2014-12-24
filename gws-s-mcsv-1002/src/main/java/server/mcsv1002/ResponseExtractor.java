package server.mcsv1002;

import ru.codeinside.gws.api.ReceiptContext;
import server.mcsv1002.context.TypedContext;
import server.mcsv1002.response.HousebookExtractionResponse;
import server.mcsv1002.response.Owner;
import server.mcsv1002.response.RegisteredPerson;

import java.util.LinkedList;
import java.util.List;

public class ResponseExtractor {
  public HousebookExtractionResponse convertToResponseObject(ReceiptContext ctx) throws DeclarerException {
    TypedContext context = new TypedContext(ctx, "");
    if (!context.hasVariableInContext("is_order_rejected")) {
    	throw new DeclarerException(4, "Заявка отклонена");
    }
    if (!context.getBoolean("request_is_correct")) {
      throw new DeclarerException(4, context.getString("comment"));
    }
   
    HousebookExtractionResponse response = new HousebookExtractionResponse();
    fillRequestObjectData(response, context);
    response.getOwner().addAll(fillOwnerData(context));
    response.getRegisteredPerson().addAll(fillRegisteredPerson(context));
    response.setComment(context.getString("comment"));
    return response;
  }

  private void fillRequestObjectData(HousebookExtractionResponse response, TypedContext ctx) {
    response.setRegionCode(ctx.getString("regionCode"));
    response.setObjectDistrict(ctx.getString("objectDistrict"));
    response.setObjectCity(ctx.getString("objectCity"));
    response.setObjectTown(ctx.getString("objectTown"));
    response.setObjectStreet(ctx.getString("objectStreet"));
    response.setObjectHouse(ctx.getString("objectHouse"));
    response.setObjectCorpus(ctx.getString("objectCorpus"));
    response.setObjectBuilding(ctx.getString("objectBuilding"));
    response.setObjectFlat(ctx.getString("objectFlat"));
    response.setObjectCadastrNumber(ctx.getString("objectCadastrNumber"));
    response.setObjectConditNumber(ctx.getString("objectConditNumber"));
  }

  private List<RegisteredPerson> fillRegisteredPerson(TypedContext context) {
    final LinkedList<RegisteredPerson> persons = new LinkedList<RegisteredPerson>();
    long countRegisteredPerson = context.getLong("person");
    for (long idx = 0; idx < countRegisteredPerson; idx++) {
      persons.add(createPerson(context, idx + 1));
    }
    return persons;
  }

  private RegisteredPerson createPerson(TypedContext context, long suffix) {
    final RegisteredPerson person = new RegisteredPerson();
    person.setLastName(context.getString("person_last_name_" + suffix));
    person.setFirstName(context.getString("person_first_name_" + suffix));
    person.setGivenName(context.getString("person_given_name_" + suffix));
    person.setBirthPlace(context.getString("person_birth_place_" + suffix));
    person.setBirthday(context.getCalendar("person_birthday_" + suffix));
    person.setDocType(context.getString("person_doc_type_" + suffix));
    person.setDocSerie(context.getString("person_doc_series_" + suffix));
    person.setDocNumber(context.getString("person_doc_number_" + suffix));
    person.setDocIssueDate(context.getCalendar("person_doc_issue_date_" + suffix));
    person.setDocIssuer(context.getString("person_doc_issuer_" + suffix));
    person.setCheckinDate(context.getCalendar("person_checkin_date_" + suffix));
    person.setCheckoutDate(context.getCalendar("person_checkout_date_" + suffix));
    return person;
  }

  private List<Owner> fillOwnerData(TypedContext context) {
    List<Owner> result = new LinkedList<Owner>();
    long countOwners = context.getLong("owners");
    for (long idx = 0; idx < countOwners; idx++) {
      result.add(createOwner(context, idx + 1));
    }
    return result;
  }

  private Owner createOwner(TypedContext context, long suffix) {
    final Owner owner = new Owner();
    owner.setFirstName(context.getString("owner_first_name_" + suffix));
    owner.setLastName(context.getString("owner_last_name_" + suffix));
    owner.setGivenName(context.getString("owner_given_name_" + suffix));
    owner.setShare(context.getString("owner_share_" + suffix));
    owner.setSince(context.getCalendar("owner_since_" + suffix));
    owner.setType(context.getString("owner_type_" + suffix));
    owner.setSize(context.getString("owner_size_" + suffix));
    return owner;
  }
}
