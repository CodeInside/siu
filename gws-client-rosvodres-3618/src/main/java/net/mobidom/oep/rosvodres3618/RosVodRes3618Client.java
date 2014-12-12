package net.mobidom.oep.rosvodres3618;

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

public class RosVodRes3618Client implements Client {

	static Logger log = Logger.getLogger(RosVodRes3618Client.class.getName());

	@Override
	public Revision getRevision() {
		return Revision.rev120315;
	}

	@Override
	public URL getWsdlUrl() {
		// TODO
		return getClass().getClassLoader().getResource("rosvodres3618/?????.wsdl");
	}

	@Override
	public ClientRequest createClientRequest(ExchangeContext ctx) {
		return null;
	}

	private String createAppData(ExchangeContext ctx) {
		return null;
	}

	@Override
	public void processClientResponse(ClientResponse clientResponse,
			ExchangeContext ctx) {
	}
}