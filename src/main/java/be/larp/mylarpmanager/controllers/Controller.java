package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadPrivilegesException;
import be.larp.mylarpmanager.exceptions.BadRequestException;
import be.larp.mylarpmanager.models.Role;
import be.larp.mylarpmanager.models.User;
import be.larp.mylarpmanager.models.UserActionHistory;
import be.larp.mylarpmanager.repositories.UserActionHistoryRepository;
import be.larp.mylarpmanager.repositories.UserRepository;
import be.larp.mylarpmanager.responses.Errors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

public class Controller {

    Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserActionHistoryRepository userActionHistoryRepository;

    @Autowired
    PasswordEncoder encoder;


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

    public void trace( User user, String text){
        UserActionHistory userActionHistory = new UserActionHistory();
        userActionHistory.setUser(user);
        userActionHistory.setAction(text);
        userActionHistory.setActionTime(LocalDateTime.now());
        userActionHistoryRepository.saveAndFlush(userActionHistory);
        logger.info("User: "+user.toString()+" has done action: "+text);
    }

    public User getRequestUser(){
        return userRepository.findByUuid(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUuid())
                .orElseThrow(() -> new NoSuchElementException("User with uuid " + ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUuid() + " not found."));
    }

    public boolean requesterIsOrga(){
        return (getRequestUser().getRole().equals(Role.ORGA));
    }
    public boolean requesterIsNationAdmin(){
        return (getRequestUser().getRole().equals(Role.NATION_ADMIN));
    }
    public boolean requesterIsNationSheriff(){
        return (getRequestUser().getRole().equals(Role.NATION_SHERIFF));
    }
    public boolean requesterIsAdmin(){
        return (getRequestUser().getRole().equals(Role.ADMIN));
    }
    public String getRandomUuid(){
        return UUID.randomUUID().toString();
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

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadPrivilegesException.class)
    public Errors handleLoginException(BadPrivilegesException ex) {
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
