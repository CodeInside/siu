package net.mobidom.oep.fssp3924;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

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
import ru.gosuslugi.smev.rev111111.AppDataType;

public class FSSP3924Client implements Client {

	static Logger log = Logger.getLogger(FSSP3924Client.class.getName());

	@Override
	public Revision getRevision() {
		return Revision.rev120315;
	}

	@Override
	public URL getWsdlUrl() {
		return getClass().getClassLoader().getResource("fssp3924/wsdl_1.wsdl");
	}

	/**
	 * В контексте ожидается один параметр 'REQUEST_OBJECT' типа DocumentRequest
	 * Он содержит параметры запроса. Вырианты параметра
	 * DocumentRequest.requestType: Если DocumentRequest.requestType =
	 * ЗАПРОС_ДОКУМЕНТА, то параметры запроса следующие: SECOND_NAME -
	 * Обязательный FIRST_NAME - Обязательный PATRONYMIC - Обязательный при
	 * наличии у лица SNILS - Обязательный YEARS - Обязательный
	 * 
	 * Если DocumentRequest.requestType = ПРОВЕРКА_ВЫПОЛНЕНИЯ, то дополнительные
	 * параметры не нужны
	 */

	@Override
	public ClientRequest createClientRequest(ExchangeContext ctx) {

		InfoSystem pnzr01581 = new InfoSystem("PNZR01581", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");

		// create packet
		Packet packet = new Packet();
		packet.typeCode = Packet.Type.SERVICE;
		packet.date = new Date();
		packet.exchangeType = "2";
		packet.recipient = new InfoSystem("PFRF01001", "Пенсионный фонд РФ");
		packet.status = Packet.Status.REQUEST;
		packet.sender = packet.originator = pnzr01581;
		// packet.testMsg = "Test query";

		// setup request
		ClientRequest clientRequest = new ClientRequest();
		clientRequest.packet = packet;
		clientRequest.action = new QName("http://service.pfr.socit.ru", "Process");

		// create appdata
		clientRequest.appData = createAppData(ctx);

		return clientRequest;
	}

	private String createAppData(ExchangeContext ctx) {

		DocumentRequest documentRequest = (DocumentRequest) ctx.getVariable("REQUEST_OBJECT");
		if (documentRequest == null) {
			throw new IllegalStateException("Context have no parameter 'REQUEST_OBJECT'");
		}

		if (documentRequest.getRequestType() == null) {
			throw new IllegalStateException("REQUEST_OBJECT have null requestType");
		}

		Map<String, Object> params = documentRequest.getRequestParams();
		if (params == null) {
			throw new IllegalStateException("REQUEST_OBJECT have no parameters");
		}

		try {
			StringWriter sw = new StringWriter();


			String result = sw.toString();

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

		AppDataType appData = null;
		try {
			JAXBContext jctx = JAXBContext.newInstance(AppDataType.class);
			JAXBElement<AppDataType> el = jctx.createUnmarshaller().unmarshal(clientResponse.appData, AppDataType.class);
			appData = el.getValue();
		} catch (Exception e) {
			throw new RuntimeException("Unmarshall error", e);
		}

		if (appData == null) {
			throw new RuntimeException("AppData not found in response");
		}

//		Type responseType = appData.getType();
//		if (responseType == null) {
//			throw new RuntimeException("AppData.Type not found in response");
//		}
//
//		if (responseType == Type.RESPONSE) {
//			if (appData.getFilePFR() == null) {
//				throw new RuntimeException("AppData.FilePTR not found in response");
//			}
//
//			try {
//				String result = new String(appData.getFilePFR(), "UTF-8").trim();
//				
//				try {
//					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//			        DocumentBuilder builder = factory.newDocumentBuilder();
//			        Document document = builder.parse(new InputSource(new StringReader(result)));
//			        Element root = document.getDocumentElement();				
//					
//					Документ документ = new Документ();
//					XmlContentWrapper xmlContent = new XmlContentWrapper();
//					xmlContent.setXmlContent(root);
//					документ.setXmlContent(xmlContent);
//					
//					documentRequest.setДокумент(документ);
//					
//					documentRequest.setResponseType(ResponseType.RESULT);
//				} catch (Exception e) {
//					throw new RuntimeException("Extract data error", e);
//				}
//			} catch (UnsupportedEncodingException e) {
//				throw new RuntimeException(e);
//			}
//		} else {
//			if (responseType == Type.ERROR) {
//				Properties properties = appData.getProperties();
//				if (properties == null) {
//					throw new RuntimeException("AppData.Properties not found in response");
//				}
//
//				String errorCode = getPropertyValue(properties, "ERROR_CODE");
//				String errorMessage = getPropertyValue(properties, "ERROR_NAME");
//
//				documentRequest.setFault("ErrorCode:" + errorCode + "; ErrorMessage:" + errorMessage);
//
//				documentRequest.setResponseType(ResponseType.DATA_ERROR);
//			} else {
//	
//				if (responseType == Type.STATUSRESPONSE) {
//					Properties properties = appData.getProperties();
//					if (properties == null) {
//						throw new RuntimeException("AppData.Properties not found in response");
//					}
//					String statusCode = getPropertyValue(properties, "STATUSCODE");
//					String statusMessage = getPropertyValue(properties, "STATUSDESC");
//					
//					String requestId = getPropertyValue(properties, "QRYNMB");
//					documentRequest.setRequestId(requestId);
//					documentRequest.setResponseType(ResponseType.RESULT_NOT_READY);
//				}
//			}
//		}

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

//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			message.writeTo(baos);
//			String xml = new String(baos.toByteArray(), "UTF-8");
//			System.out.println("===>" + xml);
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

		InputStream is = FSSP3924Client.class.getClassLoader().getResourceAsStream(resourceName);
		InputStreamReader isr = new InputStreamReader(is, "UTF-8");

		soapPart.setContent(new StreamSource(isr));
		message.saveChanges();

		return message;
	}

	public static void main(String[] args) throws Exception {
		FSSP3924Client client = new FSSP3924Client();

		DummyContext ctx = new DummyContext();

		DocumentRequest dr = new DocumentRequest();
		dr.setRequestType(DocumentRequestType.ЗАПРОС_ДОКУМЕНТА);

		dr.setRequestParameter("FIRST_NAME", "ЕЛЕНА");
		dr.setRequestParameter("SECOND_NAME", "ПЕТИНА");
		dr.setRequestParameter("PATRONYMIC", "ВЛАДИМИРОВНА");
		dr.setRequestParameter("SNILS", "027-733-198 62");
		dr.setRequestParameter("YEARS", "2010;2011");

		ctx.setVariable("REQUEST_OBJECT", dr);

		ClientRequest soapRequest = client.createClientRequest(ctx);
		soapRequest.portAddress = "http://smev-mvf.test.gosuslugi.ru:7777/gateway/services/SID0003423/1.00";

		ServiceDefinitionParser definitionParser = new ServiceDefinitionParser();
		ServiceDefinition serviceDefinition = definitionParser.parseServiceDefinition(client.getWsdlUrl());
		CryptoProvider cryptoProvider = new CryptoProvider();
		ClientRev111111 rev111111 = new ClientRev111111(definitionParser, cryptoProvider);
		ClientResponse response = rev111111.send(client.getWsdlUrl(), soapRequest, null);

//		 SOAPMessage responseMessage = readSoapMessage("data-response.xml");
//		 ClientResponse response = processResult(responseMessage);
//		
//		 client.processClientResponse(response, ctx);
	}
}