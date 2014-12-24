package server.mcsv1002.parser;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import ru.codeinside.gws.api.XmlTypes;
import server.mcsv1002.request.HouseBookExtractionRequest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestParser {
    final private Logger logger = Logger.getLogger(getClass().getName());
     public HouseBookExtractionRequest parseRequest(byte[] data){
         try {
             DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
             dbf.setNamespaceAware(true);
             DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
             InputSource is = new InputSource(new StringReader(new String(data, "UTF-8")));
             Document doc = documentBuilder.parse(is);
             return XmlTypes.elementToBean(doc.getDocumentElement(), HouseBookExtractionRequest.class);
         } catch (Exception e) {
             logger.log(Level.SEVERE, e.getMessage(), e);
             return null;
         }
     }
}
