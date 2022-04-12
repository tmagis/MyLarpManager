package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadRequestException;
import be.larp.mylarpmanager.responses.Errors;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

public class Controller {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Errors handleValidationException(MethodArgumentNotValidException ex) {
        Errors errors = new Errors();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            if (error instanceof FieldError) {
                String field = ((FieldError) error).getField();
                errors.addValidationError(field, error.getDefaultMessage());
            } else {
                errors.addGlobalError(error.getDefaultMessage());
            }
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public Errors handle(BadRequestException ex) {
        Errors errors = new Errors();
        errors.addGlobalError(ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public Errors handle(NoSuchElementException ex) {
        Errors errors = new Errors();
        errors.addGlobalError(ex.getMessage());
        return errors;
    }
}
