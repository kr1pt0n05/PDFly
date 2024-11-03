package de.lind3.PDFly.exception;

public class InvalidFileTypeException extends RuntimeException{
    public InvalidFileTypeException(String message) {
        super(message);
    }
}
