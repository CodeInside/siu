package ru.codeinside.gws.xml.normalizer.normalize.exceptions;

public class CanonicalizationException extends XMLSecurityException {

    public CanonicalizationException(String message) {
        super(message);
    }

    public CanonicalizationException(String msgID, Object[] args) {
        super(msgID, args);
    }
    public CanonicalizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
