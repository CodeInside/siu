package net.mobidom.bp.beans;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Ignore;
import org.junit.Test;

public class RequestFromFileTest {

  @Ignore
  @Test
  @SuppressWarnings("unchecked")
  public void marshalToString() throws Exception {

    //
    Обращение request = null;
    JAXBContext jaxbContext = JAXBContext.newInstance("net.mobidom.bp.beans");

    // unmarshal
    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
    JAXBElement<Обращение> element = (JAXBElement<Обращение>) unmarshaller.unmarshal(getClass().getResourceAsStream("request123.xml"));
    request = element.getValue();

    // marshal
    StringWriter sw = new StringWriter();
    Marshaller marshaller = jaxbContext.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
    marshaller.marshal(request, sw);

    System.out.println(sw);

  }

}
