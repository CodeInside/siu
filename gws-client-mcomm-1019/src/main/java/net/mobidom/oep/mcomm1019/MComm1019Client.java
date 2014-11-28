package net.mobidom.oep.mcomm1019;

import java.net.URL;
import java.util.Date;
import java.util.logging.Logger;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.XmlTypes;

public class MComm1019Client implements Client {

	static Logger log = Logger.getLogger(MComm1019Client.class.getName());

	@Override
	public Revision getRevision() {
		return Revision.rev120315;
	}

	@Override
	public URL getWsdlUrl() {
		return getClass().getClassLoader().getResource("mcomm1019/?????.wsdl");
	}

	@Override
	public ClientRequest createClientRequest(ExchangeContext ctx) {
	}

	private String createAppData(ExchangeContext ctx) {
	}

	@Override
	public void processClientResponse(ClientResponse clientResponse,
			ExchangeContext ctx) {
	}
}