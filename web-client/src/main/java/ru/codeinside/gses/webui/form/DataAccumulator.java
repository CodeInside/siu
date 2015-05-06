package ru.codeinside.gses.webui.form;

import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientRequest;

import javax.xml.soap.SOAPMessage;
import java.io.Serializable;

/**
 * Накапливает данные в результат выполнения шагов мастера. Используется пока как черновое решение.
 */
public class DataAccumulator implements Serializable {
    private Client client;
    private ClientRequest clientRequest;
    private SOAPMessage soapMessage;
    private String processDefinitionId;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ClientRequest getClientRequest() {
        return clientRequest;
    }

    public void setClientRequest(ClientRequest clientRequest) {
        this.clientRequest = clientRequest;
    }

    public SOAPMessage getSoapMessage() {
        return soapMessage;
    }

    public void setSoapMessage(SOAPMessage soapMessage) {
        this.soapMessage = soapMessage;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
}
