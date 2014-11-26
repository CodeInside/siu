package net.mobidom.oep.pfrf3814;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.junit.Test;

import ru.codeinside.gws.api.XmlTypes;
import unisoft.ws.fns2ndflws.sendquery.Документ;
import unisoft.ws.fns2ndflws.sendquery.ФИОТип;
import unisoft.ws.fns2ndflws.sendquery.Документ.СвНА;
import unisoft.ws.fns2ndflws.sendquery.Документ.СвНА.СвНАФЛ;

public class MessagesTest {

  @Test
  public void test_1() throws Exception {

    Документ in = new Документ();
    in.setВерсФорм("4.01");
    in.setИдЗапросП("2012");
    in.setОтчетГод(XmlTypes.date("01.01.2012"));
    in.setТипЗапросП("1");

    ФИОТип фиоТип = new ФИОТип();
    фиоТип.setИмя("НАТАЛЬЯ");
    фиоТип.setОтчество("ВЛАДИМИРОВНА");
    фиоТип.setФамилия("ЕВСЕЕВА");

    СвНАФЛ свНАФЛ = new СвНАФЛ();
    свНАФЛ.setСНИЛС("12345678901234");
    свНАФЛ.setФИО(фиоТип);

    СвНА свНА = new СвНА();
    свНА.setСвНАФЛ(свНАФЛ);

    in.setСвНА(свНА);

    JAXBContext ctx = JAXBContext.newInstance(Документ.class);
    Marshaller marshaller = ctx.createMarshaller();

    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
    marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

    StringWriter sw = new StringWriter();

    marshaller.marshal(in, sw);

    System.out.println(sw.toString());

  }

}
