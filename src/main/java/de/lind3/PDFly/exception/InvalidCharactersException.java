package de.lind3.PDFly.exception;

public class InvalidCharactersException extends RuntimeException{
    public InvalidCharactersException(String message) {
        super(message);
    }
}
