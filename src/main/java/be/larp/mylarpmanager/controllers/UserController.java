package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadPrivilegesException;
import be.larp.mylarpmanager.exceptions.BadRequestException;
import be.larp.mylarpmanager.models.*;
import be.larp.mylarpmanager.repositories.JoinNationDemandRepository;
import be.larp.mylarpmanager.repositories.NationRepository;
import be.larp.mylarpmanager.repositories.UserRepository;
import be.larp.mylarpmanager.requests.*;
import be.larp.mylarpmanager.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user")
public class UserController extends Controller {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JoinNationDemandRepository joinNationDemandRepository;

    @Autowired
    private NationRepository nationRepository;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/changedetails")
    public ResponseEntity<?> changeDetails(@Valid @RequestBody ChangeUserDetailsRequest changeUserDetailsRequest) {
        User requester = getRequestUser();
        if (requester.isOrga() || requester.isAdmin() || requester.getUuid().equals(changeUserDetailsRequest.getUuid())) {
            User userToChange = userRepository.findByUuid(changeUserDetailsRequest.getUuid())
                    .orElseThrow(() -> new NoSuchElementException("User with uuid " + changeUserDetailsRequest.getUuid() + " not found."));
            setValues(userToChange, changeUserDetailsRequest);
            trace(requester, "has updated user " + userToChange);
            return ResponseEntity.ok(userToChange);
        } else {
            throw new BadPrivilegesException("Your account privileges doesn't allow you to do that.");
        }
    }
    @PostMapping("/create")
    public ResponseEntity<?> changeDetails(@Valid @RequestBody CreateUserRequest createUserRequest) {
        User requester = getRequestUser();
        if (requester.isAdmin() || requester.isOrga()) {
            User userToChange =  new User();
            userToChange.setUuid(getRandomUuid());
            userToChange.setPassword(encoder.encode(getRandomUuid()));
            setValues(userToChange, createUserRequest);
            trace(requester, "has created user " + userToChange);
            return ResponseEntity.ok(userToChange);
        } else {
            throw new BadPrivilegesException("Your account privileges doesn't allow you to do that.");
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

    @PostMapping("/forcejoinnation")
    public ResponseEntity<?> forceJoinNation(@Valid @RequestBody ForceJoinNationRequest joinNationRequest) {
        User requester = getRequestUser();
        if (requester.isAdmin() || requester.isOrga()) {
            Nation nation = nationRepository.findByUuid(joinNationRequest.getNationUuid())
                    .orElseThrow(() -> new NoSuchElementException("Nation with uuid " + joinNationRequest.getNationUuid() + " not found."));
            User userToChange = userRepository.findByUuid(joinNationRequest.getPlayerUuid())
                    .orElseThrow(() -> new NoSuchElementException("Player with uuid " + joinNationRequest.getPlayerUuid() + " not found."));
            cancelPendingRequests(userToChange);
            userToChange.setNation(nation);
            userRepository.saveAndFlush(userToChange);
            trace(requester, "has changed " + userToChange + " making him join nation " + nation);
            return ResponseEntity.ok(requester);
        } else {
            throw new BadPrivilegesException("Your account privileges doesn't allow you to do that.");
        }
    }

    @PostMapping("/processdemand")
    public ResponseEntity<?> forceJoinNation(@Valid @RequestBody ProcessDemandRequest processDemandRequest) {
        User requester = getRequestUser();
        if (requester.isAdmin() || requester.isOrga() || requester.isNationSheriff() || requester.isNationAdmin()) {
            JoinNationDemand joinNationDemand = joinNationDemandRepository.findByUuid(processDemandRequest.getUuid())
                    .orElseThrow(() -> new NoSuchElementException("Demand with uuid " + processDemandRequest.getUuid() + " not found."));
            switch (Status.valueOf(processDemandRequest.getStatus())) {
                case APPROVED:
                    joinNationDemand.setStatus(Status.APPROVED);
                    joinNationDemand.setApprover(requester);
                    joinNationDemand.getCandidate().setNation(joinNationDemand.getNation());
                    joinNationDemand.setProcessingTime(LocalDateTime.now());
                    userRepository.saveAndFlush(joinNationDemand.getCandidate());
                    joinNationDemandRepository.saveAndFlush(joinNationDemand);
                    trace(requester, "has approved " + joinNationDemand.getCandidate() + " making him join nation " + joinNationDemand.getNation());
                    return ResponseEntity.ok(joinNationDemand);
                case REFUSED:
                    if (processDemandRequest.getApproverMotivation() == null || processDemandRequest.getApproverMotivation().isBlank()) {
                        throw new BadRequestException("A motivation is required when refusing.");
                    } else {
                        joinNationDemand.setStatus(Status.REFUSED);
                        joinNationDemand.setApproverMotivation(processDemandRequest.getApproverMotivation());
                        joinNationDemand.setApprover(requester);
                        joinNationDemand.setProcessingTime(LocalDateTime.now());
                        joinNationDemandRepository.saveAndFlush(joinNationDemand);
                        return ResponseEntity.ok(joinNationDemand);
                    }
                default:
                    throw new BadRequestException("The status can only be \""+Status.APPROVED+"\" or \""+Status.REFUSED+"\".");
            }
        } else {
            throw new BadPrivilegesException("Your account privileges doesn't allow you to do that.");
        }
    }

    @PostMapping("/joinnation")
    public ResponseEntity<?> joinNation(@Valid @RequestBody JoinNationRequest joinNationRequest) {
        User requester = getRequestUser();
        if (requester.isAdmin() || requester.isOrga() || requester.getUuid().equals(joinNationRequest.getPlayerUuid())) {
            Nation nation = nationRepository.findByUuid(joinNationRequest.getNationUuid())
                    .orElseThrow(() -> new NoSuchElementException("Nation with uuid " + joinNationRequest.getNationUuid() + " not found."));
            User candidate = userRepository.findByUuid(joinNationRequest.getPlayerUuid())
                    .orElseThrow(() -> new NoSuchElementException("Player with uuid " + joinNationRequest.getPlayerUuid() + " not found."));

            if(candidate.getNation() != null){
                trace(requester, " has made " + candidate + " left nation " + nation);
                candidate.setNation(null);
                userRepository.saveAndFlush(candidate);
            }
            cancelPendingRequests(candidate);
            JoinNationDemand joinNationDemand = new JoinNationDemand();
            joinNationDemand.setUuid(getRandomUuid());
            joinNationDemand.setRequestTime(LocalDateTime.now());
            joinNationDemand.setMotivation(joinNationRequest.getMotivation());
            joinNationDemand.setCandidate(candidate);
            joinNationDemand.setNation(nation);
            joinNationDemand.setStatus(Status.PENDING);
            joinNationDemandRepository.saveAndFlush(joinNationDemand);
            trace(requester, " has made candidate " + candidate + " apply for joining nation " + nation);
            return ResponseEntity.ok(joinNationDemand);
        } else {
            throw new BadPrivilegesException("Your account privileges doesn't allow you to do that.");
        }
    }

    private void cancelPendingRequests(User candidate) {
        candidate.getJoinNationDemands()
                .stream()
                .filter(j -> j
                        .getStatus()
                        .equals(Status.PENDING))
                .collect(Collectors.toList())
                .forEach(k -> {
                    k.setStatus(Status.CANCELLED);
                    k.setProcessingTime(LocalDateTime.now());
                    joinNationDemandRepository.saveAndFlush(k);
                });
    }

    @PostMapping("/leavenation")
    public ResponseEntity<?> leaveNation(@Valid @RequestBody LeaveNationRequest leaveNationRequest) {
        User requester = getRequestUser();
        if (requesterIsAdmin() || requesterIsOrga() || requester.getUuid().equals(leaveNationRequest.getUuid())) {
            User userToChange = userRepository.findByUuid(leaveNationRequest.getUuid())
                    .orElseThrow(() -> new NoSuchElementException("Player with uuid " + leaveNationRequest.getUuid() + " not found."));
            userToChange.setNation(null);
            if(userToChange.isNationAdmin() || userToChange.isNationSheriff()){
                userToChange.setRole(Role.PLAYER);
            }
            userRepository.saveAndFlush(userToChange);
            trace(requester, " has made user " + requester + " leave his nation.");
            return ResponseEntity.ok(ResponseEntity.ok());
        } else {
            throw new BadPrivilegesException("Your account privileges doesn't allow you to do that.");
        }
    }
}
