package ru.codeinside.gws.xml.normalizer.normalize.exceptions;

public class NormalizationException extends RuntimeException {
    public NormalizationException(String message) {
        super(message);
    }

    public NormalizationException(Throwable cause) {
        super(cause);
    }
}