package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadPrivilegesException;
import be.larp.mylarpmanager.exceptions.BadRequestException;
import be.larp.mylarpmanager.models.Role;
import be.larp.mylarpmanager.models.uuid.User;
import be.larp.mylarpmanager.requests.*;
import be.larp.mylarpmanager.security.events.OnRegistrationCompleteEvent;
import be.larp.mylarpmanager.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static be.larp.mylarpmanager.models.Role.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController extends Controller {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @PostMapping("/changeDetails")
    public ResponseEntity<?> changeDetails(@Valid @RequestBody ChangeUserDetailsRequest changeUserDetailsRequest) {
        User requester = getRequestUser();
        if (requester.isOrga() || requester.isAdmin() || requester.getUuid().equals(changeUserDetailsRequest.getUuid())) {
            User userToChange = userService.getUserByUuid(changeUserDetailsRequest.getUuid());
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
            setValues(userToCreate, createUserRequest);
            trace(requester, "created user", userToCreate);
            return ResponseEntity.ok(userToCreate);
        } else {
            throw new BadPrivilegesException();
        }
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteUser(@PathVariable String uuid) {
        if (requesterIsAdmin()) {
            User user = userService.getUserByUuid(uuid);
            userService.delete(user);
            return ResponseEntity.ok().build();
        } else {
            throw new BadPrivilegesException();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(HttpServletRequest request, @Valid @RequestBody CreateUserRequest createUserRequest) {
        validateUser(createUserRequest);
        User userToCreate = new User();
        setValues(userToCreate, createUserRequest);
        String host = String.valueOf(request.getRequestURL().delete(request.getRequestURL().indexOf(request.getRequestURI()), request.getRequestURL().length()));
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(userToCreate, host, request.getLocale()));
        trace(userToCreate, "register", null);
        return ResponseEntity.ok().build();
    }

    //TODO improve readability
    @PostMapping("/setRole")
    public ResponseEntity<?> setRole(@Valid @RequestBody SetRoleRequest setRoleRequest) {
        User requester = getRequestUser();
        User userToChange = userService.getUserByUuid(setRoleRequest.getUserUuid());
        Role role;
        try {
            role = Role.valueOf(setRoleRequest.getRole());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("This Role does not exist.");
        }
        switch (role) {
            case ADMIN:
                if (requester.isAdmin()) {
                    setRole(userToChange, ADMIN);
                } else {
                    throw new BadPrivilegesException();
                }
                break;
            case ORGA:
                if ((requester.isAdmin()) || (requester.isOrga() && !userToChange.isAdmin())) {
                    setRole(userToChange, ORGA);
                } else {
                    throw new BadPrivilegesException();
                }
                break;
            case NATION_ADMIN:
                setNationRole(userToChange, requester, NATION_ADMIN);
                break;
            case NATION_SHERIFF:
                setNationRole(userToChange, requester, NATION_SHERIFF);
                break;
            case PLAYER:
                if (requester.isAdmin()) {
                    setRole(userToChange, PLAYER);
                } else if (requester.isOrga() && !userToChange.isAdmin()) {
                    setRole(userToChange, PLAYER);
                } else if (requester.isNationAdmin() && (userToChange.isNationAdmin() || userToChange.isNationSheriff())) {
                    setNationRole(userToChange, requester, PLAYER);
                } else {
                    throw new BadPrivilegesException();
                }
                break;
            default:
                throw new BadRequestException("This role does not exist.");
        }
        trace(requester, "change role", userToChange);
        return ResponseEntity.ok().build();
    }

    private void setRole(User userToChange, Role role) {
        userToChange.setRole(role);
        userService.save(userToChange);
    }

    private void setNationRole(User userToChange, User requester, Role nationRole) {
        checkMemberOfNation(userToChange);
        if (requester.isAdmin() || requester.isOrga()) {
            setRole(userToChange, nationRole);
        } else if (requester.isNationAdmin() && requester.getNation().equals(userToChange.getNation())) {
            setRole(userToChange, nationRole);
        } else {
            throw new BadPrivilegesException();
        }
    }


    private void validateUser(CreateUserRequest createUserRequest) {
        if (userService.userWithUsername(createUserRequest.getUsername())) {
            throw new BadRequestException("This username is already in use.");
        }
        if (userService.userWithEmail(createUserRequest.getEmail())) {
            throw new BadRequestException("This email address is already in use.");
        }
    }

    private void setValues(User userToChange, CreateUserRequest createUserRequest) {
        checkSamePassword(createUserRequest.getPassword(), createUserRequest.getPasswordConfirmation());
        userToChange.setPassword(encoder.encode(createUserRequest.getPassword()))
                .setUsername(createUserRequest.getUsername())
                .setEmail(createUserRequest.getEmail())
                .setFirstName(createUserRequest.getFirstName())
                .setLastName(createUserRequest.getLastName());
        userService.save(userToChange);
    }

    @GetMapping("/getMyCharacters")
    public ResponseEntity<?> getMyCharacters() {
        User user = getRequestUser();
        return ResponseEntity.ok(user.getCharacters());
    }

}
