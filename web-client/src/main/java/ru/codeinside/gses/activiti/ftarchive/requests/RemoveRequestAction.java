package ru.codeinside.gses.activiti.ftarchive.requests;

import java.util.Map.Entry;
import java.util.logging.Logger;

import net.mobidom.bp.beans.СсылкаНаДокумент;
import net.mobidom.bp.beans.request.DocumentRequest;
import ru.codeinside.gses.activiti.ftarchive.DocumentsForRequestFFT;

import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class RemoveRequestAction implements Button.ClickListener {
  private static final long serialVersionUID = -6567624423008258723L;

//  private static Logger log = Logger.getLogger(RemoveRequestAction.class.getName());

  private DocumentsForRequestFFT documentsForRequestFFT;

  public RemoveRequestAction(DocumentsForRequestFFT documentsForRequestFFT) {
    this.documentsForRequestFFT = documentsForRequestFFT;
  }

  @Override
  public void buttonClick(ClickEvent event) {

    Integer idx = (Integer) event.getButton().getData();
    documentsForRequestFFT.requestsTable.removeItem(idx);
    DocumentRequest request = (DocumentRequest) documentsForRequestFFT.requestsMap.remove(idx);

    if (request.getDocRef() != null) {
      СсылкаНаДокумент documentRef = request.getDocRef();
      for (Entry<Integer, Object> en : documentsForRequestFFT.requestTemplatesMap.entrySet()) {
        if (en.getValue() == documentRef) {
          Property prop = documentsForRequestFFT.requestTemplatesTable.getItem(en.getKey()).getItemProperty("Запросить");
          if (prop.getValue() instanceof Button) {
            ((Button) prop.getValue()).setEnabled(true);
          }
          break;
        }
      }
    }

    documentsForRequestFFT.updateDocumentRequestsInProcessContext();
  }
}