package net.mobidom.bp.beans;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Element;

import ru.codeinside.gws.api.XmlTypes;

public class RequestBeanTest {

  @Test
  @SuppressWarnings("unchecked")
  public void marshalToString() throws Exception {
    Request request = createTestRequest();

    JAXBContext jaxbContext = JAXBContext.newInstance("net.mobidom.bp.beans");
    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
    JAXBElement<Request> element = (JAXBElement<Request>) unmarshaller.unmarshal(getClass().getResourceAsStream("request.xml"));
    Request requestFromFile = element.getValue();

    Assert.assertEquals(request, requestFromFile);
  }

  @Test
  public void unmarshallFromString() throws Exception {
    Request request = createTestRequest();
    StringWriter sw = new StringWriter();

    JAXBContext jaxbContext = JAXBContext.newInstance("net.mobidom.bp.beans");
    Marshaller marshaller = jaxbContext.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
    marshaller.marshal(request, sw);

    String requestStrFromFile = IOUtils.toString(getClass().getResourceAsStream("request.xml"));

    Assert.assertEquals(requestStrFromFile, sw.toString());
  }

  private Request createTestRequest() throws Exception {

    Request request = new Request();

    // SIMPLE
    request.setCreateTime(new Date(0L));
    request.setGovernmentAgencyName("FSS");
    request.setServiceName("SERVICE_NAME");

    // DECLARER
    Declarer declarer = new Declarer();
    declarer.setName("asd");
    declarer.setSurname("dsa");
    declarer.setPatronymic("fas");
    declarer.setBirthDate(new Date(0L));
    declarer.setRegistrationAddress("asdadads");
    declarer.setEmail("asdqdas");
    declarer.setPhoneNumber("asdadsasd");
    request.setDeclarer(declarer);

    // DOCUMENT-REFS
//    DocumentRefs documentRefs = new DocumentRefs();
//    documentRefs.setSnils("123441234");
//    documentRefs.setInn("2131242431");
//    PassportRef passport = new PassportRef();
//    passport.setIssueDate(new Date(0L));
//    passport.setNumber("asdadadas");
//    passport.setSerial("asdadada");
//    documentRefs.setPassport(passport);
//    request.setDocumentRefs(documentRefs);

    // DOCUMENTS
    Document document = new Document();
    document.setType("SNILS");
    XmlContentWrapper xmlContent = new XmlContentWrapper();
    xmlContent.setXmlContent(buildHelloWorldElement());
    document.setXmlContent(xmlContent);
    BinaryContent binaryContent = new BinaryContent();
    binaryContent.setMimeType("text/plain");
    binaryContent.setBinaryData("Hello World!".getBytes("UTF-8"));
    document.setBinaryContent(binaryContent);
    request.setDocuments(Arrays.asList(document, document));

    return request;
  }

  @Ignore
  @Test
  public void test_DocumentRefs() throws Exception {

//    DocumentRefs documentRefs = new DocumentRefs();
//
//    documentRefs.setSnils("123441234");
//    documentRefs.setInn("2131242431");
//
//    PassportRef passport = new PassportRef();
//    passport.setIssueDate(new Date(0L));
//    passport.setNumber("asdadadas");
//    passport.setSerial("asdadada");
//
//    documentRefs.setPassport(passport);
//
//    String xml = XmlTypes.beanToXml(documentRefs);
//
//    System.out.println(xml);
  }

  @Ignore
  @Test
  public void test_Declarer() throws Exception {

    Declarer declarer = new Declarer();

    declarer.setName("asd");
    declarer.setSurname("dsa");
    declarer.setPatronymic("fas");
    declarer.setBirthDate(new Date(0L));
    declarer.setRegistrationAddress("asdadads");
    declarer.setEmail("asdqdas");
    declarer.setPhoneNumber("asdadsasd");

    String xml = XmlTypes.beanToXml(declarer);

    System.out.println(xml);
  }

  @Ignore
  @Test
  public void test_Document() throws Exception {

    Document document = new Document();

    document.setType("SNILS");
    XmlContentWrapper xmlContent = new XmlContentWrapper();
    xmlContent.setXmlContent(buildHelloWorldElement());
    document.setXmlContent(xmlContent);

    BinaryContent binaryContent = new BinaryContent();
    binaryContent.setMimeType("text/plain");
    binaryContent.setBinaryData("Hello World!".getBytes("UTF-8"));
    document.setBinaryContent(binaryContent);

    String xml = XmlTypes.beanToXml(document);

    System.out.println(xml);
  }

  Element buildHelloWorldElement() throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    org.w3c.dom.Document document = builder.newDocument();
    Element root = (Element) document.createElement("root");
    document.appendChild(root);
    root.appendChild(document.createTextNode("hello"));
    return document.getDocumentElement();
  }

  @Ignore
  @Test
  public void test_BinaryContent() throws Exception {
    BinaryContent binaryContent = new BinaryContent();

    binaryContent.setMimeType("text/plain");
    binaryContent.setBinaryData("Hello World!".getBytes("UTF-8"));

    String xml = XmlTypes.beanToXml(binaryContent);

    System.out.println(xml);
  }

}