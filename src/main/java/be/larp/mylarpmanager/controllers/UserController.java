package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadPrivilegesException;
import be.larp.mylarpmanager.models.Nation;
import be.larp.mylarpmanager.models.User;
import be.larp.mylarpmanager.repositories.NationRepository;
import be.larp.mylarpmanager.repositories.UserRepository;
import be.larp.mylarpmanager.requests.ChangeUserDetailsRequest;
import be.larp.mylarpmanager.requests.JoinNationRequest;
import be.larp.mylarpmanager.requests.LeaveNationRequest;
import be.larp.mylarpmanager.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/user")
public class UserController extends Controller {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NationRepository nationRepository;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/changedetails")
    public ResponseEntity<?> changeDetails(@Valid @RequestBody ChangeUserDetailsRequest changeUserDetailsRequest) {
        User user = getRequestUser();
        if (highPrivileges() || user.getUuid().equals(changeUserDetailsRequest.getUuid())) {
            User userToChange = userRepository.findByUuid(changeUserDetailsRequest.getUuid())
                    .orElseThrow(() -> new NoSuchElementException("User with uuid " + changeUserDetailsRequest.getUuid() + " not found."));
            userToChange.setUsername(changeUserDetailsRequest.getUsername());
            userToChange.setEmail(changeUserDetailsRequest.getEmail());
            userToChange.setFirstName(changeUserDetailsRequest.getFirstName());
            userToChange.setLastName(changeUserDetailsRequest.getLastName());
            userRepository.saveAndFlush(userToChange);
            trace(user, "has updated user " + userToChange);
            return ResponseEntity.ok(userToChange);
        } else {
            throw new BadPrivilegesException("Your account privileges doesn't allow you to do that.");
        }
    }

    @GetMapping("/getmycharacters")
    public ResponseEntity<?> getMyCharacters() {
        User user = getRequestUser();
        return ResponseEntity.ok(user.getCharacters());
    }

    @PostMapping("/joinnation")
    public ResponseEntity<?> joinNation(@Valid @RequestBody JoinNationRequest joinNationRequest) {
        User user = getRequestUser();
        if (highPrivileges() || user.getUuid().equals(joinNationRequest.getPlayerUuid())) {
            Nation nation = nationRepository.findByUuid(joinNationRequest.getNationUuid())
                    .orElseThrow(() -> new NoSuchElementException("Nation with uuid " + joinNationRequest.getNationUuid() + " not found."));
            User userToChange = userRepository.findByUuid(joinNationRequest.getPlayerUuid())
                    .orElseThrow(() -> new NoSuchElementException("Player with uuid " + joinNationRequest.getPlayerUuid() + " not found."));
            userToChange.setNation(nation);
            userRepository.saveAndFlush(userToChange);
            trace(user, "has changed " + userToChange + " making him join nation " + nation);
            return ResponseEntity.ok(user);
        } else {
            throw new BadPrivilegesException("Your account privileges doesn't allow you to do that.");
        }
    }

    @PostMapping("/leavenation")
    public ResponseEntity<?> leaveNation(@Valid @RequestBody LeaveNationRequest leaveNationRequest) {
        User user = getRequestUser();
        if (highPrivileges() || user.getUuid().equals(leaveNationRequest.getUuid())) {
            User userToChange = userRepository.findByUuid(leaveNationRequest.getUuid())
                    .orElseThrow(() -> new NoSuchElementException("Player with uuid " + leaveNationRequest.getUuid() + " not found."));
            userToChange.setNation(null);
            userRepository.saveAndFlush(userToChange);
            trace(user, " has made user " + user + " leave his nation.");
            return ResponseEntity.ok(user.getCharacters());
        } else {
            throw new BadPrivilegesException("Your account privileges doesn't allow you to do that.");
        }
    }
}
