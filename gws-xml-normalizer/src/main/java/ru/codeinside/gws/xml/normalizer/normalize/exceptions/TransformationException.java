package ru.codeinside.gws.xml.normalizer.normalize.exceptions;

public class TransformationException extends XMLSecurityException {
    public TransformationException(String message) {
        super(message);
    }

    public TransformationException(String msgID, Object exArgs[], Exception originalException) {
        super(msgID, exArgs, originalException);
    }
}
