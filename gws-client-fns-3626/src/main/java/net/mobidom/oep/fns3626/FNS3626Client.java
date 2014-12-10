package net.mobidom.oep.fns3626;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.stream.StreamSource;

import net.mobidom.bp.beans.XmlContentWrapper;
import net.mobidom.bp.beans.Документ;
import net.mobidom.bp.beans.request.DocumentRequest;
import net.mobidom.bp.beans.request.DocumentRequestType;
import net.mobidom.bp.beans.request.ResponseType;
import net.mobidom.bp.beans.types.ТипДокумента;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.ServiceDefinition;
import ru.codeinside.gws.core.Xml;
import ru.codeinside.gws.core.cproto.ClientRev111111;
import ru.codeinside.gws.crypto.cryptopro.CryptoProvider;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;

public class FNS3626Client implements Client {

	static Logger log = Logger.getLogger(FNS3626Client.class.getName());

	@Override
	public Revision getRevision() {
		return Revision.rev120315;
	}

	@Override
	public URL getWsdlUrl() {
		return getClass().getClassLoader().getResource("fns3626/FNSINNFIODRSvc_1.wsdl");
	}

	@Override
	public ClientRequest createClientRequest(ExchangeContext ctx) {
		DocumentRequest documentRequest = (DocumentRequest) ctx.getVariable("REQUEST_OBJECT");
		if (documentRequest == null) {
			throw new IllegalStateException("Context have no parameter 'REQUEST_OBJECT'");
		}
		if (documentRequest.getRequestType() == null) {
			throw new IllegalStateException("REQUEST_OBJECT have null requestType");
		}

		Map<String, Serializable> params = documentRequest.getRequestParams();
		if (params == null) {
			throw new IllegalStateException("REQUEST_OBJECT have no parameters");
		}

		InfoSystem pnzr01581 = new InfoSystem("PNZR01581", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");

		// create packet
		Packet packet = new Packet();
		packet.typeCode = Packet.Type.SERVICE;
		packet.date = new Date();
		packet.exchangeType = "2";
		packet.recipient = new InfoSystem("PFRF01001", "Пенсионный фонд РФ");
		packet.sender = packet.originator = pnzr01581;
		// packet.testMsg = "Test query";

		if (documentRequest.getRequestType() == DocumentRequestType.ЗАПРОС_ДОКУМЕНТА) {
			packet.status = Packet.Status.REQUEST;
		}
		if (documentRequest.getRequestType() == DocumentRequestType.ПРОВЕРКА_ВЫПОЛНЕНИЯ) {
			packet.status = Packet.Status.PING;
		}

		// setup request
		ClientRequest clientRequest = new ClientRequest();
		clientRequest.packet = packet;

		if (documentRequest.getRequestType() == DocumentRequestType.ЗАПРОС_ДОКУМЕНТА) {
			clientRequest.action = new QName("http://ws.unisoft/", "queryINNFLFIODR");
		}
		if (documentRequest.getRequestType() == DocumentRequestType.ПРОВЕРКА_ВЫПОЛНЕНИЯ) {
			clientRequest.action = new QName("http://ws.unisoft/", "getINNFLFIODR");
		}

		// create appdata
		clientRequest.appData = createAppData(ctx, documentRequest);

		return clientRequest;
	}

	static DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

	private String createAppData(ExchangeContext ctx, DocumentRequest documentRequest) {
		Map<String, Serializable> params = documentRequest.getRequestParams();

		String result = "";

		try {
			StringWriter sw = new StringWriter();

			if (documentRequest.getRequestType() == DocumentRequestType.ЗАПРОС_ДОКУМЕНТА) {
				unisoft.ws.innfiodr.query.rq.Документ doc = new unisoft.ws.innfiodr.query.rq.Документ();
				doc.setВерсФорм("4.01");

				unisoft.ws.innfiodr.query.rq.Документ.СвФЛ свФЛ = new unisoft.ws.innfiodr.query.rq.Документ.СвФЛ();
				doc.setСвФЛ(свФЛ);

				if (params.get("ДатаРожд") == null) {
					throw new IllegalStateException("Not defined 'ДатаРожд'");
				}
				свФЛ.setДатаРожд(df.format(params.get("ДатаРожд")));

				свФЛ.setИмя((String) params.get("Имя"));
				if (свФЛ.getИмя() == null) {
					throw new IllegalStateException("Not defined 'Имя'");
				}

				свФЛ.setФамилия((String) params.get("Фамилия"));
				if (свФЛ.getФамилия() == null) {
					throw new IllegalStateException("Not defined 'Фамилия'");
				}

				свФЛ.setОтчество((String) params.get("Отчество"));
				if (свФЛ.getОтчество() == null) {
					throw new IllegalStateException("Not defined 'Отчество'");
				}

				свФЛ.getПризОтч(); // ???? В документации написано, что это обязательный параметр, но в примерах его нет и какие значения должны быть не указано
				
				JAXBContext jaxbContext = JAXBContext.newInstance(unisoft.ws.innfiodr.query.rq.Документ.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
				jaxbMarshaller.marshal(doc, sw);

				result = sw.toString();
			}

			if (documentRequest.getRequestType() == DocumentRequestType.ПРОВЕРКА_ВЫПОЛНЕНИЯ) {
				unisoft.ws.innfiodr.get.rq.Документ doc = new unisoft.ws.innfiodr.get.rq.Документ();
				doc.setВерсФорм("4.01");

				doc.setИдЗапросФ(new BigInteger(documentRequest.getRequestId()));

				JAXBContext jaxbContext = JAXBContext.newInstance(unisoft.ws.innfiodr.get.rq.Документ.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
				jaxbMarshaller.marshal(doc, sw);

				result = sw.toString();
			}

			return result;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void processClientResponse(ClientResponse clientResponse, ExchangeContext ctx) {

		DocumentRequest documentRequest = (DocumentRequest) ctx.getVariable("REQUEST_OBJECT");
		if (documentRequest == null) {
			throw new IllegalStateException("Context have no parameter 'REQUEST_OBJECT'");
		}

		Element appData = clientResponse.appData;
		if (appData == null) {
			throw new RuntimeException("AppData not found in response");
		}

		if (documentRequest.getRequestType() == DocumentRequestType.ЗАПРОС_ДОКУМЕНТА) {
			try {
				NodeList nl = appData.getElementsByTagName("Документ");
				Element docEl = (Element) nl.item(0);

				JAXBContext jaxbContext = JAXBContext.newInstance(unisoft.ws.innfiodr.query.rs.Документ.class);
				JAXBElement<unisoft.ws.innfiodr.query.rs.Документ> appDataElement = jaxbContext.createUnmarshaller().unmarshal(docEl, unisoft.ws.innfiodr.query.rs.Документ.class);
				unisoft.ws.innfiodr.query.rs.Документ appDataDoc = appDataElement.getValue();

				if ("99".equals(appDataDoc.getКодОбр())) {
					documentRequest.setResponseType(ResponseType.SYSTEM_ERROR);
				} else {
					documentRequest.setRequestId(appDataDoc.getИдЗапросФ().toString());
				}

			} catch (Exception e) {
				throw new RuntimeException("Unmarshall error", e);
			}
		}

		if (documentRequest.getRequestType() == DocumentRequestType.ПРОВЕРКА_ВЫПОЛНЕНИЯ) {
			try {
				NodeList nl = appData.getElementsByTagName("Документ");
				Element docEl = (Element) nl.item(0);

				JAXBContext jaxbContext = JAXBContext.newInstance(unisoft.ws.innfiodr.get.rs.Документ.class);
				JAXBElement<unisoft.ws.innfiodr.get.rs.Документ> appDataElement = jaxbContext.createUnmarshaller().unmarshal(docEl, unisoft.ws.innfiodr.get.rs.Документ.class);
				unisoft.ws.innfiodr.get.rs.Документ appDataDoc = appDataElement.getValue();

				if ("01".equals(appDataDoc.getКодОбр())) {
					documentRequest.setResponseType(ResponseType.DATA_NOT_FOUND);
				} else if ("52".equals(appDataDoc.getКодОбр())) {
					documentRequest.setResponseType(ResponseType.RESULT_NOT_READY);
				} else if ("83".equals(appDataDoc.getКодОбр())) {
					documentRequest.setResponseType(ResponseType.DATA_ERROR);
				} else if ("99".equals(appDataDoc.getКодОбр())) {
					documentRequest.setResponseType(ResponseType.SYSTEM_ERROR);
				} else {
					if (appDataDoc.getИННФЛ() != null) {
						documentRequest.setResponseType(ResponseType.RESULT);
						ctx.setVariable("ИННФЛ", appDataDoc.getИННФЛ());

						Документ doc = new Документ();
						XmlContentWrapper xmlContentWrapper = new XmlContentWrapper();

						xmlContentWrapper.setXmlContent(docEl);
						doc.setXmlContent(xmlContentWrapper);
						doc.setDocumentType(ТипДокумента.ИНН);
						documentRequest.setДокумент(doc);
						documentRequest.setReady(true);

					} else {
						throw new IllegalStateException("Unknown state exception");
					}
				}
			} catch (Exception e) {
				throw new RuntimeException("Unmarshall error", e);
			}
		}

	}

	private static ClientResponse processResult(final SOAPMessage message) throws Exception {

		ClientResponse response = new ClientResponse();

		final SOAPBody soapBody = message.getSOAPBody();
		if ("Fault".equals(soapBody.getNodeName()) && "http://www.w3.org/2003/05/soap-envelope".equals(soapBody.getNamespaceURI())) {
			log.warning("Не обработанная ошбка SOAP " + soapBody);
		} else {
			final Element action = Xml.parseAction(soapBody);
			if (action == null) {
				throw new IllegalStateException("Пустое тело пакета");
			}
			response.action = new QName(action.getNamespaceURI(), action.getLocalName());
			response.packet = Xml.parseSmevMessage(action, Revision.rev111111);

			// ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// message.writeTo(baos);
			// String xml = new String(baos.toByteArray(), "UTF-8");
			// System.out.println("===>" + xml);
			//
			final Xml.MessageDataContent mdc = Xml.processMessageData(message, action, Revision.rev111111, null);
			response.enclosureDescriptor = mdc.requestCode;
			response.appData = mdc.appData;

			response.enclosures = mdc.attachmens != null ? mdc.attachmens.toArray(new Enclosure[mdc.attachmens.size()]) : null;
		}

		return response;
	}

	public static SOAPMessage readSoapMessage(String resourceName) throws Exception {
		SOAPMessage message = MessageFactory.newInstance().createMessage();
		SOAPPart soapPart = message.getSOAPPart();

		InputStream is = FNS3626Client.class.getClassLoader().getResourceAsStream(resourceName);
		InputStreamReader isr = new InputStreamReader(is, "UTF-8");

		soapPart.setContent(new StreamSource(isr));
		message.saveChanges();

		return message;
	}

	public static void main(String[] args) throws Exception {
		FNS3626Client client = new FNS3626Client();

		DummyContext ctx = new DummyContext();

		DocumentRequest dr = new DocumentRequest();
		dr.setRequestType(DocumentRequestType.ЗАПРОС_ДОКУМЕНТА);

		dr.setRequestParameter("Имя", "ПЕТР");
		dr.setRequestParameter("Фамилия", "ЧАХЛОВ");
		dr.setRequestParameter("Отчество", "АЛЕКСЕЕВИЧ");
		dr.setRequestParameter("ДатаРожд", df.parse("12.07.1954"));

		ctx.setVariable("REQUEST_OBJECT", dr);

		ClientRequest queryRequest = client.createClientRequest(ctx);
		queryRequest.portAddress = "http://94.125.90.50:6336/FNSINNFIODRSvc?wsdl";

		ServiceDefinitionParser definitionParser = new ServiceDefinitionParser();
		ServiceDefinition serviceDefinition = definitionParser.parseServiceDefinition(client.getWsdlUrl());
		CryptoProvider cryptoProvider = new CryptoProvider();
		ClientRev111111 rev111111 = new ClientRev111111(definitionParser, cryptoProvider);
		ClientResponse queryResponse = rev111111.send(client.getWsdlUrl(), queryRequest, null);
		client.processClientResponse(queryResponse, ctx);

		ResponseType responseType;
		do {
			dr.setRequestType(DocumentRequestType.ПРОВЕРКА_ВЫПОЛНЕНИЯ);
			ClientRequest getRequest = client.createClientRequest(ctx);
			getRequest.portAddress = "http://94.125.90.50:6336/FNSINNFIODRSvc?wsdl";
			ClientResponse getResponse = rev111111.send(client.getWsdlUrl(), getRequest, null);
			client.processClientResponse(getResponse, ctx);
			responseType = dr.getResponseType();
			log.info("responseType=" + responseType);
			if (responseType == ResponseType.RESULT_NOT_READY) {
				Thread.sleep(1000L);
			}
		} while (responseType == ResponseType.RESULT_NOT_READY);

		log.info("RESULT IS " + ctx.getVariable("ИННФЛ"));
	}

}