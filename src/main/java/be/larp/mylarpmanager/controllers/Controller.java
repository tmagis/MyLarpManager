package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadRequestException;
import be.larp.mylarpmanager.models.Role;
import be.larp.mylarpmanager.models.User;
import be.larp.mylarpmanager.repositories.UserRepository;
import be.larp.mylarpmanager.responses.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

public class Controller {


    @Autowired
    UserRepository userRepository;

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

    public User getRequestUser(){
        return userRepository.findByUuid(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUuid())
                .orElseThrow(() -> new NoSuchElementException("User with uuid " + ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUuid() + " not found."));
    }

    public boolean highPrivileges(){
        return (getRequestUser().getRole().equals(Role.ORGA) || getRequestUser().getRole().equals(Role.ADMIN));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public Errors handle(BadRequestException ex) {
        Errors errors = new Errors();
        errors.addGlobalError(ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public Errors handleLoginException(BadCredentialsException ex) {
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
