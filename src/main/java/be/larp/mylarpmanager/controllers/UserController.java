package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadPrivilegesException;
import be.larp.mylarpmanager.exceptions.BadRequestException;
import be.larp.mylarpmanager.models.*;
import be.larp.mylarpmanager.repositories.JoinNationDemandRepository;
import be.larp.mylarpmanager.repositories.NationRepository;
import be.larp.mylarpmanager.repositories.UserRepository;
import be.larp.mylarpmanager.requests.*;
import be.larp.mylarpmanager.security.events.OnRegistrationCompleteEvent;
import be.larp.mylarpmanager.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user")
public class UserController extends Controller {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @PostMapping("/changedetails")
    public ResponseEntity<?> changeDetails(@Valid @RequestBody ChangeUserDetailsRequest changeUserDetailsRequest) {
        User requester = getRequestUser();
        if (requester.isOrga() || requester.isAdmin() || requester.getUuid().equals(changeUserDetailsRequest.getUuid())) {
            User userToChange = userRepository.findByUuid(changeUserDetailsRequest.getUuid())
                    .orElseThrow(() -> new NoSuchElementException("User with uuid " + changeUserDetailsRequest.getUuid() + " not found."));
            setValues(userToChange, changeUserDetailsRequest);
            trace(requester, "update user", userToChange);
            return ResponseEntity.ok(userToChange);
        } else {
            throw new BadPrivilegesException();
        }
    }

    /**
     * Creates a user, bypass the email verification step.
     */
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody CreateUserRequest createUserRequest) {
        User requester = getRequestUser();
        if (requester.isAdmin() || requester.isOrga()) {
            validateUser(createUserRequest);
            User userToCreate = new User();
            userToCreate.setEnabled(true);
            userToCreate.setUuid(getRandomUuid());
            userToCreate.setPassword(encoder.encode(getRandomUuid()));
            setValues(userToCreate, createUserRequest);
            trace(requester, "created user", userToCreate);
            return ResponseEntity.ok(userToCreate);
        } else {
            throw new BadPrivilegesException();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CreateUserRequest createUserRequest) {
        validateUser(createUserRequest);
        User userToCreate = new User();
        userToCreate.setUuid(getRandomUuid());
        userToCreate.setPassword(encoder.encode(getRandomUuid()));
        setValues(userToCreate, createUserRequest);
        //
        RequestContextHolder.currentRequestAttributes();
        //
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(userToCreate, "pouet", Locale.FRENCH));
        trace(userToCreate, "register", null);
        return ResponseEntity.ok().build();
    }

    private void validateUser(CreateUserRequest createUserRequest) {
        if(userRepository.findByUsername(createUserRequest.getUsername()).isPresent()){
            throw new BadRequestException("This username is already in use.");
        }
        if(userRepository.findByEmail(createUserRequest.getEmail()).isPresent()){
            throw new BadRequestException("This email address is already in use.");
        }
    }

    private void setValues(User userToChange, CreateUserRequest createUserRequest) {
        userToChange.setUsername(createUserRequest.getUsername());
        userToChange.setEmail(createUserRequest.getEmail());
        userToChange.setFirstName(createUserRequest.getFirstName());
        userToChange.setLastName(createUserRequest.getLastName());
        userRepository.saveAndFlush(userToChange);
    }

    @GetMapping("/getmycharacters")
    public ResponseEntity<?> getMyCharacters() {
        User user = getRequestUser();
        return ResponseEntity.ok(user.getCharacters());
    }

}
