package net.mobidom.oep.fss3415;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.XmlTypes;
import ru.fss.common.ТипОтвета;
import ru.fss.svednoregdobrstrahnotrudormat.Запрос;
import ru.fss.svednoregdobrstrahnotrudormat.Ответ;
import ru.gosuslugi.smev.rev111111.AppData;

public class FSS3415Client implements Client {

	static Logger log = Logger.getLogger(FSS3415Client.class.getName());

	@Override
	public Revision getRevision() {
		return Revision.rev111111;
	}

	@Override
	public URL getWsdlUrl() {
		return getClass().getClassLoader().getResource("fss3415/SvedNoRegDobrStrahNoTrudOrMat_1.wsdl");
	}

	@Override
	public ClientRequest createClientRequest(ExchangeContext ctx) {
		final String originRequestId = (String) ctx.getVariable("smevOriginRequestId");
		final String requestId = (String) ctx.getVariable("smevRequestId");
		final Boolean smevPool = (Boolean) ctx.getVariable("smevPool");

		final Packet packet = new Packet();
		packet.recipient = new InfoSystem("FSSR01001", "ФСС России");
		packet.typeCode = Packet.Type.SERVICE;
		packet.date = new Date();
		packet.exchangeType = "1"; // 1 - Запрос на оказание услуги
		packet.originRequestIdRef = originRequestId;
		packet.testMsg = (String) ctx.getVariable("smevTest");

		final ClientRequest request = new ClientRequest();
		request.packet = packet;
		request.action = new QName("http://fss.ru/SvedNoRegDobrStrahNoTrudOrMat/request", "request");

		if (Boolean.TRUE == smevPool) {
			packet.status = Packet.Status.PING;
			packet.requestIdRef = requestId;
		} else {
			packet.status = Packet.Status.REQUEST;
			request.appData = createAppData(ctx);
		}
		return request;
	}

	private String createAppData(ExchangeContext ctx) {
		final Запрос request = new Запрос();

		request.setINameCiv((String) ctx.getVariable("iNameCiv"));
		request.setFNameCiv((String) ctx.getVariable("fNameCiv"));
		request.setMNameCiv((String) ctx.getVariable("mNameCiv"));
		request.setCodeKind((String) ctx.getVariable("codeKind"));
		request.setSeriesNumber((String) ctx.getVariable("seriesNumber"));
		request.setDocDataCiv(XmlTypes.date((String) ctx.getVariable("docDatCiv")));
		request.setInn((String) ctx.getVariable("inn"));
		request.setSnils((String) ctx.getVariable("snils"));
		request.setRegionFrom((String) ctx.getVariable("regionFrom"));
		request.setNameOrganizationFrom((String) ctx.getVariable("nameOrganizationFrom"));

		return new XmlTypes(Запрос.class).toXml(request);
	}

	@Override
	public void processClientResponse(ClientResponse response, ExchangeContext context) {
		Boolean pooled = (Boolean) context.getVariable("smevPool");
		if (response.verifyResult.error != null) {
			context.setVariable("smevPool", false);
			context.setVariable("smevError", response.verifyResult.error);
		} else if (!new QName("http://fss.ru/SvedNoRegDobrStrahNoTrudOrMat/request", "requestResponse").equals(response.action)) {
			context.setVariable("smevPool", false);
			context.setVariable("smevError", "Неизвестный ответ " + response.action);
		} else {
			if (response.packet.status == Packet.Status.ACCEPT) {
				context.setVariable("smevPool", true);
				if (Boolean.TRUE != pooled) {
					context.setVariable("smevRequestId", response.packet.requestIdRef);
					context.setVariable("smevOriginRequestId", response.packet.originRequestIdRef);
				}
			} else {
				context.setVariable("smevPool", false);
				AppData appData = (AppData) XmlTypes.elementToBean(response.appData, AppData.class);
				ТипОтвета responseType = appData.getТипОтвета();
				context.setVariable("hint", appData.getПримечание());
				if (responseType == ТипОтвета.ОШИБКА_В_ЗАПРОСЕ) {
					context.setVariable("smevError", "Ошибка в запросе: " + appData.getПримечание());
					context.setVariable("status", "Ошибка");
				} else if (responseType == ТипОтвета.ОТВЕТ) {
					context.setVariable("status", "Данные найдены");

					final Ответ r = appData.getОтвет();

					context.setVariable("regionTo_response", r.getRegionTo());
					context.setVariable("nameOrganizationTo_response", r.getNameOrganizationTo());
					context.setVariable("regionToPay_response", r.getRegionTo());
					context.setVariable("iNameCiv_response", r.getINameCiv());
					context.setVariable("fNameCiv_response", r.getFNameCiv());
					context.setVariable("mNameCiv_response", r.getMNameCiv());
					context.setVariable("docDatCiv_response", date(r.getDocDataCiv()));
					context.setVariable("codeKind_response", r.getCodeKind());
					context.setVariable("seriesNumber_response", r.getSeriesNumber());
					context.setVariable("inn_response", r.getInn());
					context.setVariable("snils_response", r.getSnils());
					context.setVariable("obtainingGrants2_response", r.isObtainingGrants2());
					context.setVariable("removalAccountDate_response", date(r.getRemovalAccountDate()));
					context.setVariable("registrationDate_response", date(r.getRegistrationDate()));

				} else {
					context.setVariable("status", "Данные не найдены");
				}
			}
		}
	}

	private String date(final XMLGregorianCalendar calendar) {
		if (calendar == null) {
			return null;
		}
		return new SimpleDateFormat("dd.MM.yyyy").format(calendar.toGregorianCalendar().getTime());
	}
}