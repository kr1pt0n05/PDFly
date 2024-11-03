package de.lind3.PDFly.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ModelAndView createModelAndViewObject(Exception ex, HttpStatus status){
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.setStatus(status);
        modelAndView.addObject("errorMessage", ex.getMessage());
        modelAndView.addObject("statusCode", status.value());
        return modelAndView;
    }

    @ExceptionHandler(NoFileUploadedException.class)
    public ModelAndView handleNoFileUploadedException(NoFileUploadedException ex){
        return createModelAndViewObject(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex){
        return createModelAndViewObject(ex, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(InvalidPageException.class)
    public ModelAndView handleInvalidPageException(InvalidPageException ex){
        return createModelAndViewObject(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFileTypeException.class)
    public ModelAndView handleInvalidFileTypeException(InvalidFileTypeException ex){
        return createModelAndViewObject(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyDocumentException.class)
    public ModelAndView handleEmptyDocumentException(EmptyDocumentException ex){
        return createModelAndViewObject(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCharactersException.class)
    public ModelAndView handleInvalidCharactersException(InvalidCharactersException ex){
        return createModelAndViewObject(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ModelAndView handleIOException(IOException ex){
        return createModelAndViewObject(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex){
        return createModelAndViewObject(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }




}
