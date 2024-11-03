package de.lind3.PDFly.exception;

public class NoFileUploadedException extends RuntimeException{
    public NoFileUploadedException(String message) {
        super(message);
    }
}
