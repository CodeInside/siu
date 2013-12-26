/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package xmltype;

import org.junit.Assert;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

final public class R {


    public static SOAPMessage getSoapResource(final String resource) throws IOException, SOAPException {
        final InputStream stream = getRequiredResourceStream(resource);
        return MessageFactory.newInstance().createMessage(null, stream);
    }

    public static String getTextResource(final String resource) throws IOException {
        final InputStream stream = getRequiredResourceStream(resource);
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        int read;
        while ((read = stream.read(buffer)) != -1) {
            bos.write(buffer, 0, read);
        }
        return new String(bos.toByteArray(), "UTF-8");
    }

    public static InputStream getRequiredResourceStream(final String resource) {
        final InputStream message = R.class.getClassLoader().getResourceAsStream(resource);
        Assert.assertNotNull("Не найден ресурс " + resource, message);
        return message;
    }


}
