package de.lind3.PDFly.exception;

public class EmptyDocumentException extends RuntimeException{
    public EmptyDocumentException(String message) {
        super(message);
    }
}
