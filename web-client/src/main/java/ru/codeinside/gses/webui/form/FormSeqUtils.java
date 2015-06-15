package ru.codeinside.gses.webui.form;

import com.vaadin.ui.Form;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.SignatureProtocol;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.webui.osgi.Activator;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientProtocol;
import ru.codeinside.gws.api.ProtocolFactory;
import ru.codeinside.gws.api.Server;
import ru.codeinside.gws.api.ServerProtocol;
import ru.codeinside.gws.api.ServiceDefinition;
import ru.codeinside.gws.api.ServiceDefinitionParser;

public class FormSeqUtils {
  private FormSeqUtils() {
    throw new UnsupportedOperationException("Static methods only");
  }

  static public ClientProtocol getClientProtocol(Client client) {
    ClientProtocol clientProtocol;
    ServiceReference serviceReference = Activator.getContext().getServiceReference(ProtocolFactory.class.getName());

    try {
      ProtocolFactory protocolFactory = (ProtocolFactory) Activator.getContext().getService(serviceReference);
      clientProtocol = protocolFactory.createClientProtocol(client.getRevision());
    } finally {
      Activator.getContext().ungetService(serviceReference);
    }
    return clientProtocol;
  }

  static public ServerProtocol getServerProtocol(Server service) {
    ServerProtocol serverProtocol;
    ServiceReference serviceReference = Activator.getContext().getServiceReference(ProtocolFactory.class.getName());

    try {
      ProtocolFactory protocolFactory = (ProtocolFactory) Activator.getContext().getService(serviceReference);
      final ServiceDefinitionParser serviceDefinitionParser = AdminServiceProvider.get().getServiceDefinitionParser();
      ServiceDefinition serviceDefinition = serviceDefinitionParser.parseServiceDefinition(service.getWsdlUrl());
      serverProtocol = protocolFactory.createServerProtocol(serviceDefinition);
    } finally {
      Activator.getContext().ungetService(serviceReference);
    }
    return  serverProtocol;
  }

  static <T> ServiceReference getServiceReference(String serviceName, Class<T> clazz) {
    ServiceReference[] references;
    String filter = "(&(component.name=" + serviceName + "))";
    try {
      references = Activator.getContext().getAllServiceReferences(clazz.getName(), filter);
    } catch (InvalidSyntaxException e) {
      throw new IllegalStateException("Не удаётся получить ссылку на сервис " + serviceName);
    }

    ServiceReference reference = null;
    if (references == null || references.length < 1) {
      throw new IllegalStateException("Сервис " + serviceName + " не найден");
    } else if (references.length == 1) {
      reference = references[0];
    } else if (references.length > 1) { // Если есть несколько сервисов с таким именем, берём тот, у которого выше версия
      for (ServiceReference comparedReference : references) {
        if (reference == null) {
          reference = comparedReference;
        }

        int comparedReferenceId = Integer.valueOf(comparedReference.getProperty("service.id").toString());
        int referenceId = Integer.valueOf(reference.getProperty("service.id").toString());

        if (comparedReferenceId > referenceId) {
          reference = comparedReference;
        }
      }
    }
    return reference;
  }

  static <T> T getService(ServiceReference reference, Class<T> clazz) {
    return  (T) Activator.getContext().getService(reference);
  }

  static void addSignedDataToForm(Form form, String signData, String propertyId) {
    final ReadOnly txt = new ReadOnly(signData);
    txt.setCaption("Подписываемые данные");
    txt.addStyleName("light");
    form.addField(propertyId, txt);
  }

  static void addSignatureFieldToForm(Form form, FormID formId, String signData, String fieldId, DataAccumulator dataAccumulator) {
    byte[] signDataBytes = signData.getBytes();
    boolean[] files = {false};
    String[] ids = {fieldId};

    FormSignatureField signatureField = new FormSignatureField(
        new SignatureProtocol(formId, FormSignatureSeq.SIGNATURE, FormSignatureSeq.SIGNATURE,
            new byte[][]{signDataBytes}, files, ids, form, dataAccumulator));
    signatureField.setCaption(FormSignatureSeq.SIGNATURE);
    signatureField.setRequired(true);

    form.addField(FormSignatureSeq.SIGNATURE, signatureField);
  }
}
