package server.mcsv1002;

import ru.codeinside.gws.api.*;
import server.mcsv1002.parser.RequestParser;
import server.mcsv1002.request.HouseBookExtractionRequest;
import server.mcsv1002.response.HousebookExtractionResponse;
import server.mcsv1002.response.message.ProcessCanonicalServiceResponseMessageType;

import javax.xml.namespace.QName;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Arrays.asList;

public class Declarer implements Server {

  private static final long PROCEDURE_CODE = 5000000000000001002L;
  final private Logger logger = Logger.getLogger(getClass().getName());

  @Override
  public Revision getRevision() {
    return Revision.rev111111;
  }

  @Override
  public URL getWsdlUrl() {
    return getClass().getClassLoader().getResource("mvvact/mvvact.wsdl");
  }

  @Override
  public ServerResponse processRequest(final RequestContext ctx) {
    if (ctx.isFirst()) {
      return processFirstRequest(ctx);
    } else {
      return processContinueRequest(ctx);
    }
  }

  private ServerResponse processContinueRequest(final RequestContext ctx) {
    final ServerRequest request = ctx.getRequest();
    if ("processCanonicalService".equals(request.action.getLocalPart())) {
      if (request.packet.status != Packet.Status.PING) {
        throw new IllegalStateException("Illegal status "
            + request.packet.status);
      }
      final ServerResponse result = ctx.getResult();
      if (result != null) {
        if (result.packet != null) {
        result.packet.oktmo = request.packet.oktmo;
        if (request != null && request.packet != null){
          result.packet.serviceCode = request.packet.serviceCode;
          result.packet.caseNumber = request.packet.caseNumber;
          result.packet.testMsg = request.packet.testMsg;
        }
        // если нет ничего
        if (request != null && request.routerPacket == null && request.packet != null && request.packet.requestIdRef==null) {
          result.packet.requestIdRef = request.packet.originRequestIdRef; //UUID.randomUUID().toString();
        }
        }
        return result;
      }
      return createResponse(request, Packet.Status.PROCESS, ctx.getBid(), 0, null);
    }
    switch (request.packet.status) {
      case REQUEST:
      case PING:
        // проверка состояние это либо запрос либо опрос.
        break;

      default:
        throw new IllegalStateException("Illegal status " + request.packet.status);
    }
    final ServerResponse result = ctx.getResult();
    if (result != null) {
      result.packet.oktmo = request.packet.oktmo;
      if (request != null && request.packet != null){
        result.packet.serviceCode = request.packet.serviceCode;
        result.packet.caseNumber = request.packet.caseNumber;
        result.packet.testMsg = request.packet.testMsg;
      }
      // если нет ничего
      if (request != null && request.routerPacket == null && request.packet != null && request.packet.requestIdRef==null) {
        result.packet.requestIdRef = request.packet.originRequestIdRef; //UUID.randomUUID().toString();
      }
      return result;
    }
    final ServerResponse state = ctx.getState();
    if (state != null) {
      state.packet.oktmo = request.packet.oktmo;
      if (request != null && request.packet != null){
        state.packet.serviceCode = request.packet.serviceCode;
        state.packet.caseNumber = request.packet.caseNumber;
        state.packet.testMsg = request.packet.testMsg;
      }
      // если нет ничего
      if (request != null && request.routerPacket == null && request.packet != null && request.packet.requestIdRef==null) {
        state.packet.requestIdRef = request.packet.originRequestIdRef; //UUID.randomUUID().toString();
      }
      return state;
    }
    return createResponse(request, Packet.Status.PROCESS, ctx.getBid(), 0, null);
  }

  private ServerResponse processFirstRequest(final RequestContext ctx) {
    final ServerRequest request = ctx.getRequest();
    if (request.packet.status != Packet.Status.REQUEST) {
      throw new IllegalStateException("Illegal status "
          + request.packet.status);
    }
    try {
      Map<String, Object> values = processInitialRequestAttachment(request);
      final DeclarerContext declarerContext = ctx.getDeclarerContext(PROCEDURE_CODE);
      addRequestParamsToContext(declarerContext, values);
      String bid = declarerContext.declare();
      return createResponse(request, Packet.Status.ACCEPT, bid, 0, null);
    } catch (DeclarerException err) {
      return createResponse(request, Packet.Status.FAILURE, null, err.getCode(), err.getMessage());
    }
  }

  private void addRequestParamsToContext(DeclarerContext declarerContext, Map<String, Object> values) {
    for (String key : values.keySet()) {
      declarerContext.setValue(key, values.get(key));
    }
  }

  private Map<String, Object> processInitialRequestAttachment(final ServerRequest request) throws DeclarerException {
    if (request.attachmens == null) {
      throw new DeclarerException(4, "В запросе должно быть вложение");
    } else if (request.attachmens.size() != 1) {
      throw new DeclarerException(4, "В запросе должно быть только одно вложение");
    }
    try {
      Enclosure enclosure = request.attachmens.get(0);
      return parseEnclosureData(enclosure.content);
    } catch (Exception err) {
      logger.log(Level.SEVERE, err.getMessage(), err);
      throw new DeclarerException(4, "При анализе вложения произошла ошибка " + err.getMessage());
    }
  }

  private Map<String, Object> parseEnclosureData(byte[] content) {
    RequestParser parser = new RequestParser();
    final HouseBookExtractionRequest request = parser.parseRequest(content);
    request.checkRequiredFields();
    return request.makeMapValue();
  }

  private ServerResponse createResponse(ServerRequest request,
                                        Packet.Status status, String taskId, int code, String message) {
    ServerResponse response = new ServerResponse();
    response.packet = new Packet();
    response.packet.status = status;
    response.packet.exchangeType = "1";
    response.packet.typeCode = Packet.Type.SERVICE;
    response.packet.serviceName = "10000001022";
    response.action = request.action;
    if(request.packet.oktmo != null){
      response.packet.oktmo = request.packet.oktmo;
    }
    if (request != null && request.packet != null){
      response.packet.serviceCode = request.packet.serviceCode;
      response.packet.caseNumber = request.packet.caseNumber;
      response.packet.testMsg = request.packet.testMsg;
    }
    // если нет ничего
    if (request != null && request.routerPacket == null && request.packet != null && request.packet.requestIdRef==null) {
      response.packet.requestIdRef = request.packet.originRequestIdRef; //UUID.randomUUID().toString();
    }
    response.appData = createAppData(taskId, code, message);
    return response;
  }

  private String createAppData(String taskId, int code, String message) {
    ProcessCanonicalServiceResponseMessageType obj = new ProcessCanonicalServiceResponseMessageType();
    obj.setTaskId(taskId);
    obj.setErrorCode(code);
    obj.setErrorDescription(message);
    return XmlTypes.beanToXml(obj);
  }

  @Override
  public ServerResponse processStatus(String statusMessage,
                                      ReceiptContext receiptContext) {

    ServerResponse response = new ServerResponse();
    response.packet = new Packet();
    response.packet.status = Packet.Status.PROCESS;
    response.packet.exchangeType = "1";
    response.packet.typeCode = Packet.Type.SERVICE;
    response.appData = null;
    response.action = new QName("http://canonicalRequests.gov.ru",
        "processCanonicalService");
    return response;
  }

  @Override
  public ServerResponse processResult(String resultMessage,
                                      ReceiptContext receiptContext) {

    ServerResponse response = new ServerResponse();
    response.packet = new Packet();
    response.packet.status = Packet.Status.RESULT;
    response.packet.exchangeType = "1";
    response.packet.typeCode = Packet.Type.SERVICE;
    response.packet.oktmo = "56701000";
    response.action = new QName("http://canonicalRequests.gov.ru", "processCanonicalService");
    try {
      ResponseExtractor extractor = new ResponseExtractor();
      HousebookExtractionResponse housebookExtraction = extractor.convertToResponseObject(receiptContext);
      String xml = XmlTypes.beanToXml(housebookExtraction);
      String enclosureName = UUID.randomUUID().toString();
      Enclosure enclosure = new Enclosure(enclosureName.concat(".xml"), enclosureName, xml.getBytes());
      receiptContext.setEnclosure(enclosureName, enclosure);
      response.docRequestCode = enclosureName;
      response.attachmens = asList(enclosure);
      response.appData = createAppData(null, 0, null);
    } catch (Exception err) {
      logger.log(Level.SEVERE, err.getMessage());
      response.packet.status = Packet.Status.REJECT;
      response.appData = createAppData(null, 4, err.getMessage());
    }
    return response;
  }
}
