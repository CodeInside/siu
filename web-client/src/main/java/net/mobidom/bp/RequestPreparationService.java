package net.mobidom.bp;

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

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.DelegateExecution;

import ru.codeinside.adm.AdminService;
import ru.codeinside.gses.beans.ActivitiExchangeContext;
import ru.codeinside.gws.api.Enclosure;

@Named("requestPreparationService")
@Singleton
public class RequestPreparationService {

  static Logger log = Logger.getLogger(RequestPreparationService.class.getName());

  @Inject
  AdminService adminService;

  @Inject
  Instance<ProcessEngine> processEngine;

  public void init() {
  }

  public void prepareAttachmentVariable(DelegateExecution execution) {
    ActivitiExchangeContext aeCtx = new ActivitiExchangeContext(execution);

    String enclosureVarName = null;

    // TODO webdom for testing using _call_target_.bpmn
    if (aeCtx.getVariableNames().contains("appData_attachment")) {
      enclosureVarName = "appData_attachment";
    } else {
      for (String varName : aeCtx.getVariableNames()) {
        if (varName.endsWith("xml") && !varName.contains("metadata.xml")) {
          enclosureVarName = varName;
          break;
        }
      }
    }

    if (enclosureVarName == null || enclosureVarName.isEmpty()) {
      throw new RuntimeException("unable to find file-attachment in process");
    }

    Enclosure enclosure = aeCtx.getEnclosure(enclosureVarName);
    aeCtx.addEnclosure("request_attachment", enclosure);
  }

  @SuppressWarnings("unchecked")
  public void prepareRequest(DelegateExecution execution) {

    try {

      ActivitiExchangeContext aeCtx = new ActivitiExchangeContext(execution);

      Enclosure enclosure = aeCtx.getEnclosure("attachment");

      String requestId = "undefined";
      if (enclosure.fileName.endsWith(".xml")) {
        requestId = enclosure.fileName.split("\\.")[0];
      }

      ByteArrayInputStream in = new ByteArrayInputStream(enclosure.content);

      JAXBContext jaxbContext = JAXBContext.newInstance("net.mobidom.bp.beans");
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      JAXBElement<Обращение> element = (JAXBElement<Обращение>) unmarshaller.unmarshal(in);
      Обращение request = element.getValue();

      execution.createVariableLocal("requestId", requestId);
      execution.createVariableLocal("request", request);

      List<Документ> documents = new ArrayList<Документ>();
      documents.addAll(request.getДокументы());
      execution.createVariableLocal("documents", documents);

    } catch (Throwable t) {
      log.log(Level.SEVERE, "can't prepare request", t);
      throw new RuntimeException(t);
    }

  }
}
