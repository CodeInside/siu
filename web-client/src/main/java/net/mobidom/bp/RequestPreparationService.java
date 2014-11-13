package net.mobidom.bp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import net.mobidom.bp.beans.Document;
import net.mobidom.bp.beans.Request;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.DelegateExecution;

import ru.codeinside.adm.AdminService;
import ru.codeinside.gses.beans.ActivitiExchangeContext;
import ru.codeinside.gses.webui.executor.ArchiveFactory;
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
    Enclosure enclosure = aeCtx.getEnclosure("attachment_raw");
    byte[] zipData = enclosure.content;
    String base64Attachment = ArchiveFactory.toBase64HumanString(zipData);
    execution.createVariableLocal("attachment", base64Attachment);
  }

  @SuppressWarnings("unchecked")
  public void prepareRequest(DelegateExecution execution) {

    try {

      ActivitiExchangeContext aeCtx = new ActivitiExchangeContext(execution);

      Enclosure enclosure = aeCtx.getEnclosure("attachment");

      byte[] zipData = enclosure.content;

      File tmpFile = File.createTempFile("tmp", "zip");
      FileOutputStream tmpFileOutputStream = new FileOutputStream(tmpFile);
      tmpFileOutputStream.write(zipData);
      tmpFileOutputStream.close();

      ZipFile zipFile = new ZipFile(tmpFile);

      ZipEntry entry = null;
      Enumeration<? extends ZipEntry> entries = zipFile.entries();
      while (entries.hasMoreElements()) {
        entry = entries.nextElement();

        String fileName = entry.getName();
        if (fileName.endsWith(".xml")) {
          String requestId = fileName.split("\\.")[0];

          InputStream in = zipFile.getInputStream(entry);

          JAXBContext jaxbContext = JAXBContext.newInstance("net.mobidom.bp.beans");
          Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
          JAXBElement<Request> element = (JAXBElement<Request>) unmarshaller.unmarshal(in);
          Request request = element.getValue();

          execution.createVariableLocal("requestId", requestId);
          execution.createVariableLocal("request", request);
          execution.createVariableLocal("declarer", request.getDeclarer());

          List<Document> documents = new ArrayList<Document>();
          documents.addAll(request.getDocuments());
          execution.createVariableLocal("documents", documents);

          // execution.createVariableLocal("pid",
          // execution.getProcessInstanceId());

          in.close();

        } else {
          log.info(fileName + " - not file request");
        }
      }

      tmpFile.delete();

    } catch (Throwable t) {
      log.log(Level.SEVERE, "can't prepare request", t);
    }

  }
}
