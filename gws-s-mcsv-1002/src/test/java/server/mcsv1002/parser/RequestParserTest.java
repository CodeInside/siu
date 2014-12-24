package server.mcsv1002.parser;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import server.mcsv1002.request.HouseBookExtractionRequest;

import java.io.InputStream;

import static junit.framework.Assert.assertEquals;

public class RequestParserTest {

    @Test
    public void testParseSampleRequest() throws Exception {
        final InputStream inputStream = this.getClass().getResourceAsStream("/attachmentRequest.xml");
        final byte[] data = IOUtils.toByteArray(inputStream);
        RequestParser parser = new RequestParser();
        final HouseBookExtractionRequest request = parser.parseRequest(data);
        assertEquals("4000", request.getRegionCode());
        assertEquals("Санкт-Петербург", request.getObjectCity());
        assertEquals("Ленинский пр.", request.getObjectStreet());
        assertEquals("140", request.getObjectHouse());
        assertEquals("2", request.getObjectCorpus());
        assertEquals("А", request.getObjectBuilding());
        assertEquals("16", request.getObjectFlat());
        assertEquals("09:06:0120221:50", request.getObjectCadastrNumber());
    }
}
