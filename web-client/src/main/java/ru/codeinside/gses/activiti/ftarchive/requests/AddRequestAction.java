package ru.codeinside.gses.activiti.ftarchive.requests;

import java.util.Date;
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

  @SuppressWarnings("static-access")
  @Override
  public void buttonClick(ClickEvent event) {

    Integer idx = (Integer) event.getButton().getData();
    Object data = documentsForRequestFFT.requestTemplatesMap.get(idx);

    final DocumentRequest request;
    if (data instanceof СсылкаНаДокумент) {

      event.getButton().setEnabled(false);

      СсылкаНаДокумент documentRef = (СсылкаНаДокумент) data;
      request = DocumentRequestBuilder.createRequestForDocumentReference(documentRef, documentsForRequestFFT.mainRequest);
      request.setCreateDate(new Date());

      addRequestToTable(request);
      documentsForRequestFFT.updateDocumentRequestsInProcessContext();

    } else if (data instanceof DocumentRequest) {

      DocumentRequest baseRequest = (DocumentRequest) data;
      request = DocumentRequestBuilder.fillDocumentRequest(baseRequest, documentsForRequestFFT.mainRequest);

      documentsForRequestFFT.showRequestFormWindow(event.getButton().getWindow(), request, new DocumentsForRequestFFT.RequestFormCompleted() {

        @Override
        public void onSubmit(boolean submit) {
          if (submit) {
            request.setCreateDate(new Date());
            addRequestToTable(request);
            documentsForRequestFFT.updateDocumentRequestsInProcessContext();
          } else {
            log.info("no need to add request");
          }
        }
      }, false);
    }
  }

  public class ReplaceRequestsTableItem implements DocumentsForRequestFFT.RequestFormCompleted {

    Integer idx;
    DocumentRequest request;

    public ReplaceRequestsTableItem(Integer idx, DocumentRequest request) {
      this.idx = idx;
      this.request = request;
    }

    @Override
    public void onSubmit(boolean submit) {
      if (submit) {
        documentsForRequestFFT.requestsTable.removeItem(idx);
        Object[] comps = buildItemContent(idx, request);
        documentsForRequestFFT.requestsTable.addItem(comps, idx);
      }
    }
  }

  public Object[] buildItemContent(final Integer idx, DocumentRequest request) {
    Label label = new Label(request.requestParamsToLabel());

    Button showRequestDataButton = new Button("Редактировать");
    showRequestDataButton.setData(request);
    showRequestDataButton.addListener(new Button.ClickListener() {
      private static final long serialVersionUID = 6348767896636362763L;

      @SuppressWarnings("static-access")
      @Override
      public void buttonClick(ClickEvent event) {
        DocumentRequest request = (DocumentRequest) event.getButton().getData();
        documentsForRequestFFT.showRequestFormWindow(event.getButton().getWindow(), request, AddRequestAction.this.new ReplaceRequestsTableItem(idx, request), false);
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
    if (documentsForRequestFFT.requestsTable.size() >= 5) {
      documentsForRequestFFT.requestsTable.setPageLength(documentsForRequestFFT.requestsTable.size() + 1);
    }
    documentsForRequestFFT.requestsMap.put(nextIdx, request);
  }
}