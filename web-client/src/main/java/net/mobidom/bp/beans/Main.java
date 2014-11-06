package net.mobidom.bp.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class Main {

  static Logger log = Logger.getLogger(Main.class.getCanonicalName());

  public static void main(String[] args) throws Throwable {

    // log.info("temp");

    Request request = new Request();

//    List<DocumentRef> documentRefs = new ArrayList<DocumentRef>();
//
//    InnRef innDocumentRef = new InnRef();
//    innDocumentRef.setNumber("123-123-123");
//    documentRefs.add(innDocumentRef);
//
//    PassportRef passportRef = new PassportRef();
//    passportRef.issueDate = new Date();
//    passportRef.number = "1234";
//    passportRef.serial = "2345";
//    documentRefs.add(passportRef);
//
//    SnilsRef snilsRef = new SnilsRef();
//    snilsRef.setNumber("321-321-321");
//    documentRefs.add(snilsRef);
//
//    request.setDocumentRefs(documentRefs);
//
//    JAXBContext jaxbContext = JAXBContext.newInstance("net.mobidom.bp.beans");
//    Marshaller marshaller = jaxbContext.createMarshaller();
//    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//    marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
//    marshaller.marshal(request, System.out);
  }
}
