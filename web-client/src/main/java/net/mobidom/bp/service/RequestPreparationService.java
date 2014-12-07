package net.mobidom.bp.service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import net.mobidom.bp.beans.Документ;
import net.mobidom.bp.beans.Обращение;
import net.mobidom.bp.beans.ПодписьОбращения;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.DelegateExecution;

import ru.codeinside.adm.AdminService;
import ru.codeinside.gses.beans.ActivitiExchangeContext;
import ru.codeinside.gws.api.Enclosure;

@Named("requestPreparationService")
@Singleton
public class RequestPreparationService {

  static Logger log = Logger.getLogger(RequestPreparationService.class.getName());

  private static String REQUEST_FILE_NAME = "request_attachment";
  private static String REQUEST_SIGNATURE_FILE_NAME = "request_signature_attachment";
  private static String METADATA_XML = "metadata.xml";

  @Inject
  AdminService adminService;

  @Inject
  Instance<ProcessEngine> processEngine;

  public void init() {
  }

  public void prepareAttachmentVariable(DelegateExecution execution) {

    ActivitiExchangeContext aeCtx = new ActivitiExchangeContext(execution);

    for (String varName : aeCtx.getVariableNames()) {

      // 1 attachment
      if (varName.endsWith("appData_attachment_signature") && !varName.contains(METADATA_XML)) {
        createTargetVariable(aeCtx, varName, REQUEST_SIGNATURE_FILE_NAME);
      }

      // 2 signature
      if (varName.equals("appData_attachment") && !varName.contains(METADATA_XML)) {
        createTargetVariable(aeCtx, varName, REQUEST_FILE_NAME);
      }

    }

  }

  private void createTargetVariable(ActivitiExchangeContext aeCtx, String enclosureVarName, String newEnclosureVarName) {
    Enclosure enclosure = aeCtx.getEnclosure(enclosureVarName);
    aeCtx.addEnclosure(newEnclosureVarName, enclosure);
  }

  @SuppressWarnings("unchecked")
  public void prepareRequest(DelegateExecution execution) {

    try {

      ActivitiExchangeContext aeCtx = new ActivitiExchangeContext(execution);

      // read request_arrachment
      Enclosure attachmentEnclosure = aeCtx.getEnclosure(REQUEST_FILE_NAME);

      String requestId = "undefined";
      if (attachmentEnclosure.fileName.endsWith(".xml")) {
        requestId = attachmentEnclosure.fileName.split("\\.")[0];
      }

      ByteArrayInputStream in = new ByteArrayInputStream(attachmentEnclosure.content);

      JAXBContext jaxbContext = JAXBContext.newInstance(Обращение.class);
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      JAXBElement<Обращение> element = (JAXBElement<Обращение>) unmarshaller.unmarshal(in);
      Обращение request = element.getValue();

      execution.createVariableLocal("requestId", requestId);
      execution.createVariableLocal("request", request);
      log.info("обращение извелечено");

      List<Документ> documents = new ArrayList<Документ>();
      documents.addAll(request.getДокументы());
      execution.createVariableLocal("documents", documents);

      // read request_signature_attachment
      if (aeCtx.getVariableNames().contains(REQUEST_SIGNATURE_FILE_NAME)) {

        Enclosure signatureEnclosure = aeCtx.getEnclosure(REQUEST_SIGNATURE_FILE_NAME);
        in = new ByteArrayInputStream(signatureEnclosure.content);

        jaxbContext = JAXBContext.newInstance(ПодписьОбращения.class);
        unmarshaller = jaxbContext.createUnmarshaller();

        ПодписьОбращения sign = (ПодписьОбращения) unmarshaller.unmarshal(in);
        request.setПодписьОбращения(sign);

        log.info("подпись извлечена");

        // check signature
        request.setSignatureValid(false);

      }

    } catch (Throwable t) {
      log.log(Level.SEVERE, "can't prepare request", t);
      throw new RuntimeException(t);
    }

  }
}
