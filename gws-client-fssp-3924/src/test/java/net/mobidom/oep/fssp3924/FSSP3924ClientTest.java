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

public class FSSP3924ClientTest {

	static Logger log = Logger.getLogger(FSSP3924ClientTest.class.getName());

}