package com.eop.client.gws3793c;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.xml.namespace.QName;

import com.eop.client.gws3793c.types.AppData;


import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.XmlTypes;
import unisoft.ws.fnszdl.rq1.Документ.ЗапросНП;
import unisoft.ws.fnszdl.rq1.Документ.СвЮЛ;
import unisoft.ws.fnszdl.rs2.Документ.СвЗад;
import unisoft.ws.fnszdl.rs2.Документ.СвЗад.ПерНО;


public class FNSClient implements Client{
	
	  public static final QName GET_RESPONSE_RQ = new QName("http://ws.unisoft/", "getResponse");
	  public static final QName GET_RESPONSE_RS = new QName("http://ws.unisoft/", "GetResponseRs");
	  public static final QName SEND_REQUEST_RQ = new QName("http://ws.unisoft/", "sendRequest");
	  public static final QName SEND_REQUEST_RS = new QName("http://ws.unisoft/", "SendRequestRs");
	  public static final String SMEV_REQUEST_ID = "smevRequestId";
	  public static final String SMEV_ERORR = "smevError";
	  public static final String SMEV_ORIGIN_REQUEST_ID = "smevOriginRequestId";
	  public static final String SMEV_POOL = "smevPool";
	  public static final String KOD_OBR = "codeObr";
	  public static final String SYSTEM_ERROR = "99";
	  public static final String STATUS = "status";
	  public static final String VERS_FORM = "4.02";

	@Override
	public Revision getRevision() {
	   return Revision.rev120315;
	}

	@Override
	public URL getWsdlUrl() {
	   return getClass().getClassLoader().getResource("wsdl/smev/v_2_4/FNSZDLService.wsdl");
	}
	
	@Override
	public ClientRequest createClientRequest(ExchangeContext ctx) {
	    final String originRequestId = (String) ctx.getVariable(SMEV_ORIGIN_REQUEST_ID);
	    final String requestId = (String) ctx.getVariable(SMEV_REQUEST_ID);
	    final Boolean smevPool = (Boolean) ctx.getVariable(SMEV_POOL);

	    final Packet packet = new Packet();
	    packet.recipient = new InfoSystem("FNS001001", "ФНС России");
	    packet.typeCode = Packet.Type.SERVICE;
	    packet.serviceName = "FNSZDLWS";	//Сервис предоставления Сведений о наличии (отсутствии) задолженности по уплате налогов, сборов, пеней, штрафов
	    packet.date = new Date();
	    packet.exchangeType = "2"; 
	    packet.originRequestIdRef = originRequestId;
	    packet.testMsg = (String) ctx.getVariable("smevTest");
	    packet.sender = new InfoSystem("008201", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
	    packet.originator = new InfoSystem("008201", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");

	    final ClientRequest request = new ClientRequest();
	    request.packet = packet;
	    
	    if (Boolean.TRUE == smevPool) {
	      packet.status = Packet.Status.PING;
	      packet.requestIdRef = requestId;
	      request.action = GET_RESPONSE_RQ;
	      request.appData = createAppData(ctx, true);
	    } else {
	      packet.status = Packet.Status.REQUEST;
	      request.action = SEND_REQUEST_RQ;
	      request.appData = createAppData(ctx, false);
	    }
	    return request;
	}

	  private String createAppData(ExchangeContext ctx, boolean ping) {
		  	if (!ping){
		  		unisoft.ws.fnszdl.rq1.Документ документ = new unisoft.ws.fnszdl.rq1.Документ();
		  		СвЮЛ свЮЛ = new СвЮЛ();
		  		свЮЛ.setИННЮЛ((String) ctx.getVariable("INNUL"));
		  		свЮЛ.setНаимЮЛ((String) ctx.getVariable("NaimUL"));
		  		свЮЛ.setОГРН((String) ctx.getVariable("OGRN"));
		  		ЗапросНП запросНП = new ЗапросНП();
		  		Date date = (Date) ctx.getVariable("dataNa");
		  		String dataNa = new SimpleDateFormat("dd.MM.yyyy").format(date);
		  		запросНП.setДатаНа(dataNa);
		  		if (((String) ctx.getVariable("INNFL_INNUL")).equals("INNFL"))
		  			запросНП.setИННФЛ((String) ctx.getVariable("INNFL"));
		  		else
		  			запросНП.setИННЮЛ((String) ctx.getVariable("INNUL"));
		  		String идЗапрос = UUID.randomUUID().toString();
		  		документ.setВерсФорм(VERS_FORM);
		  		документ.setЗапросНП(запросНП);
		  		документ.setСвЮЛ(свЮЛ);
		  		документ.setИдЗапросП(идЗапрос);
		  		return new XmlTypes(unisoft.ws.fnszdl.rq1.Документ.class).toXml(документ);
		  	}
		  	else {
		  		unisoft.ws.fnszdl.rq2.Документ документ = new unisoft.ws.fnszdl.rq2.Документ();
		  		String идЗапрос = (String) ctx.getVariable(SMEV_ORIGIN_REQUEST_ID);
		  		if ((идЗапрос == null)||(идЗапрос.equals("")))
		  			ctx.setVariable(SMEV_POOL, false);
		  		документ.setВерсФорм(VERS_FORM);
		  		документ.setИдЗапросФ(идЗапрос);
		  		return new XmlTypes(unisoft.ws.fnszdl.rq2.Документ.class).toXml(документ);
			}
		  	
	  }

	@Override
	public void processClientResponse(ClientResponse response, ExchangeContext context) {
	    Boolean pooled = (Boolean) context.getVariable(SMEV_POOL);
	    if (response.verifyResult.error != null) {  //ошибка?
	      context.setVariable(SMEV_POOL, false);
	      context.setVariable(SMEV_ERORR, response.verifyResult.error);
	    } else if (!(GET_RESPONSE_RS.equals(response.action)||(SEND_REQUEST_RS.equals(response.action)))) { //правильный метод? 
	      context.setVariable(SMEV_POOL, false);
	      context.setVariable(SMEV_ERORR, "Неизвестный ответ " + response.action);
	    } else {
	      if ((response.packet.status == Packet.Status.ACCEPT)&&(Boolean.TRUE != pooled)) {  //Обработка первичного запроса
	    	context.setVariable(SMEV_POOL, false);
	        AppData appData = (AppData) XmlTypes.elementToBean(response.appData, AppData.class);
	        unisoft.ws.fnszdl.rs1.Документ документ = appData.getДокументПервичныйОтвет();
		    if (документ != null){
		    	String кодОбр = документ.getКодОбр();
		        if ((кодОбр != null)&&(!кодОбр.toString().equals(""))) {  //Если есть код обработки, то сервер нас не принял
			        if (кодОбр.equals("82")) {
			          context.setVariable(SMEV_ERORR, "Ошибка форматно-логического контроля");
			          context.setVariable(STATUS, "Ошибка");
			        }else if (кодОбр.equals(SYSTEM_ERROR)) {
				          context.setVariable(SMEV_ERORR, "Системная ошибка");
				          context.setVariable(STATUS, "Ошибка");	        	
			        }
			        context.setVariable(KOD_OBR, кодОбр);
		        }
		        else { // противном случае наш запрос зарегистрирован, сохраняем идентификатор запроса
		        	  context.setVariable(SMEV_REQUEST_ID, response.packet.requestIdRef);
			          context.setVariable(SMEV_ORIGIN_REQUEST_ID, документ.getИдЗапросФ());
			          context.setVariable(SMEV_POOL, true);
				}
		    }else
		    	context.setVariable(KOD_OBR, SYSTEM_ERROR);
	      } else {  //обработка вторичных запросов
		        context.setVariable(SMEV_POOL, false);
		        AppData appData = (AppData) XmlTypes.elementToBean(response.appData, AppData.class);
		        unisoft.ws.fnszdl.rs2.Документ документ = appData.getДокументВторичныйОтвет();
		        if (документ != null){
			        String кодОбр = документ.getКодОбр();
			        if ((кодОбр != null)&&(кодОбр.equals("52"))) {  //Ответ еще не готов? - продолжаем опрос
			        	context.setVariable(SMEV_POOL, true);
			        }
			        else { //Ответ готов
						if ((кодОбр != null)&&(!кодОбр.toString().equals(""))) { //Записываем код обработки, если он имеется
							context.setVariable(KOD_OBR, кодОбр);
						}
						else { //получен результат, пихаем его в форму
							СвЗад свЗад = документ.getСвЗад();
							if (свЗад != null){
								if (свЗад.getДатаНа() != null)
									context.setVariable("dataNa_response", свЗад.getДатаНа());
								if (свЗад.getИННФЛ() != null)
									context.setVariable("INNFL_response", свЗад.getИННФЛ());
								if (свЗад.getИННЮЛ() != null)
									context.setVariable("INNUL_response", свЗад.getИННЮЛ());
								ПерНО перНО = свЗад.getПерНО();
								if (перНО != null){
									if (перНО.getКодИФНС() != null && перНО.getКодИФНС().size() > 0){
										context.setVariable("perNO_response", перНО.getКодИФНС().size());
										for (int i = 0; i < перНО.getКодИФНС().size(); i++) {
											String codeIFNS = перНО.getКодИФНС().get(i);
											context.setVariable("perNO_codeIFNS_response_" + String.valueOf(i+1), codeIFNS);			
										}							
									}								
								}
								if (свЗад.getПрЗад() != null)
									context.setVariable("prZad_response", свЗад.getПрЗад());
							}					
						}
					}	
		        }else{
			          context.setVariable(SMEV_ERORR, "Системная ошибка");
			          context.setVariable(STATUS, "Ошибка");	 
			          context.setVariable(KOD_OBR, SYSTEM_ERROR);
		        }
	      }
	    }
	}
}
