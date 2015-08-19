package ru.codeinside.gws.xml.normalizer.normalize.exceptions;

import ru.codeinside.gws.xml.normalizer.normalize.utils.I18n;

import java.text.MessageFormat;

public class XMLSecurityException extends Exception {
    protected String msgID;

    public XMLSecurityException() {
        super();
    }

    public XMLSecurityException(String message) {
        super(message);
    }

    public XMLSecurityException(String msgID, Object exArgs[]) {

        super(MessageFormat.format(I18n.getExceptionMessage(msgID), exArgs));

        this.msgID = msgID;
    }

    public XMLSecurityException(String msgID, Object exArgs[], Exception originalException) {
        super(MessageFormat.format(I18n.getExceptionMessage(msgID), exArgs), originalException);

        this.msgID = msgID;
    }

    public XMLSecurityException(String message, Throwable cause) {
        super(message, cause);
    }
}
