package ru.codeinside.gses.activiti.ftarchive.requests;

import java.util.logging.Logger;

import net.mobidom.bp.beans.СсылкаНаДокумент;
import net.mobidom.bp.beans.builder.DocumentRequestBuilder;
import net.mobidom.bp.beans.request.DocumentRequest;
import ru.codeinside.gses.activiti.ftarchive.DocumentsForRequestFFT;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;

public class AddRequestAction implements Button.ClickListener {
  private static final long serialVersionUID = -9006594502921784978L;

  private static Logger log = Logger.getLogger(AddRequestAction.class.getName());

  private DocumentsForRequestFFT documentsForRequestFFT;

  public AddRequestAction(DocumentsForRequestFFT documentsForRequestFFT) {
    this.documentsForRequestFFT = documentsForRequestFFT;
  }

  @Override
  public void buttonClick(ClickEvent event) {

    Integer idx = (Integer) event.getButton().getData();
    Object data = documentsForRequestFFT.requestTemplatesMap.get(idx);

    final DocumentRequest request;
    if (data instanceof СсылкаНаДокумент) {

      event.getButton().setEnabled(false);

      СсылкаНаДокумент documentRef = (СсылкаНаДокумент) data;
      request = DocumentRequestBuilder.createRequestForDocumentReference(documentRef, documentsForRequestFFT.mainRequest);

      addRequestToTable(request);
      documentsForRequestFFT.updateDocumentRequestsInProcessContext();

    } else if (data instanceof DocumentRequest) {

      DocumentRequest baseRequest = (DocumentRequest) data;
      request = DocumentRequestBuilder.fillDocumentRequest(baseRequest, documentsForRequestFFT.mainRequest);

      documentsForRequestFFT.showRequestFormWindow(event.getButton().getWindow(), request, new DocumentsForRequestFFT.RequestFormCompleted() {

        @Override
        public void onSubmit(boolean submit) {
          if (submit) {
            addRequestToTable(request);
            documentsForRequestFFT.updateDocumentRequestsInProcessContext();
          } else {
            log.info("no need to add request");
          }
        }
      });
    }
  }

  public static class ReplaceRequestsTableItem implements DocumentsForRequestFFT.RequestFormCompleted {

    Integer idx;
    DocumentRequest request;

    public ReplaceRequestsTableItem(Integer idx, DocumentRequest request) {
      this.idx = idx;
      this.request = request;
    }

    @Override
    public void onSubmit(boolean submit) {
      if (submit) {

      }
    }
  }

  private Object[] buildItemContent(final Integer idx, DocumentRequest request) {
    Label label = new Label(request.requestParamsToLabel());

    Button showRequestDataButton = new Button("Редактировать");
    showRequestDataButton.setData(request);
    showRequestDataButton.addListener(new Button.ClickListener() {
      private static final long serialVersionUID = 6348767896636362763L;

      @Override
      public void buttonClick(ClickEvent event) {
        DocumentRequest request = (DocumentRequest) event.getButton().getData();
        documentsForRequestFFT.showRequestFormWindow(event.getButton().getWindow(), request, new ReplaceRequestsTableItem(idx, request));
        documentsForRequestFFT.requestsTable.removeItem(idx);
        documentsForRequestFFT.requestsTable.addItem(buildItemContent(idx, request), idx);
      }
    });

    Button cancelButton = new Button("Удалить");
    cancelButton.setData(idx);
    cancelButton.addListener(new RemoveRequestAction(documentsForRequestFFT));

    return new Object[] { label, showRequestDataButton, cancelButton };
  }

  private void addRequestToTable(DocumentRequest request) {
    final Integer nextIdx = documentsForRequestFFT.requestsTable.size() + 1;

    documentsForRequestFFT.requestsTable.addItem(buildItemContent(nextIdx, request), nextIdx);
    documentsForRequestFFT.requestsTable.setPageLength(documentsForRequestFFT.requestsTable.size() + 1);
    documentsForRequestFFT.requestsMap.put(nextIdx, request);
  }
}