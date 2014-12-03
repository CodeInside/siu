package net.mobidom.oep.pfrf3622;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.junit.Test;

import ru.codeinside.gws.api.XmlTypes;

import com.rstyle.skmv.pfr.FIO;
import com.rstyle.skmv.pfr.GENDER;
import com.rstyle.skmv.snils_by_data.SnilsByDataIn;

public class MessagesTest {

  public void test_1() throws Exception {

    SnilsByDataIn in = new SnilsByDataIn();

    in.setBirthDate("");

    FIO fio = new FIO();
    fio.setFirstName("");
    fio.setLastName("");
    fio.setPatronymic("");
    in.setFio(fio);

    in.setGender(GENDER.F);

    JAXBContext ctx = JAXBContext.newInstance(SnilsByDataIn.class);
    Marshaller marshaller = ctx.createMarshaller();

    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
    marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        
    StringWriter sw = new StringWriter();

    marshaller.marshal(in, sw);

    System.out.println(sw.toString());

  }

}
