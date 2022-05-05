package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadPrivilegesException;
import be.larp.mylarpmanager.exceptions.BadRequestException;
import be.larp.mylarpmanager.models.*;
import be.larp.mylarpmanager.models.uuid.JoinNationDemand;
import be.larp.mylarpmanager.models.uuid.Nation;
import be.larp.mylarpmanager.models.uuid.User;
import be.larp.mylarpmanager.repositories.JoinNationDemandRepository;
import be.larp.mylarpmanager.repositories.NationRepository;
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
@RequestMapping("/api/v1/nation")
public class NationController extends Controller {

    @Autowired
    private JoinNationDemandRepository joinNationDemandRepository;

    @Autowired
    private NationRepository nationRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/changedetails")
    public ResponseEntity<?> changeNationDetails(@Valid @RequestBody ChangeNationDetailsRequest changeNationDetailsRequest) {
        User user = getRequestUser();
        Nation nation = nationRepository.findByUuid(changeNationDetailsRequest.getUuid())
                .orElseThrow(() -> new NoSuchElementException("Nation with uuid " + changeNationDetailsRequest.getUuid() + " not found."));
        if (requesterIsAdmin() || (user.getNation() != null && user.getNation().getUuid().equals(nation.getUuid()) && user.getRole().equals(Role.NATION_ADMIN))) {
            setNationValues(nation, changeNationDetailsRequest);
            trace(user, "update nation", nation);
            return ResponseEntity.ok(nation);
        } else {
            throw new BadPrivilegesException();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNation(@Valid @RequestBody CreateNationRequest createNationRequest) {
        User requester = getRequestUser();
        if (requesterIsAdmin()) {
            Nation nation = new Nation();
            setNationValues(nation, createNationRequest);
            trace(requester, "create nation", nation);
            return ResponseEntity.ok(nation);
        } else {
            throw new BadPrivilegesException();
        }
    }

    private void setNationValues(Nation nation, CreateNationRequest createNationRequest) {
        nation.setName(createNationRequest.getName());
        nation.setIntroText(createNationRequest.getIntroText());
        nation.setFullDescription(createNationRequest.getFullDescription());
        nation.setFamilyFriendly(createNationRequest.isFamilyFriendly());
        nation.setContributionInCents(createNationRequest.getContributionInCents());
        nation.setInternationalFriendly(createNationRequest.isInternationalFriendly());
        nation.setContributionMandatory(createNationRequest.isContributionMandatory());
        nationRepository.saveAndFlush(nation);
    }

    @GetMapping("/getmynationplayers")
    public ResponseEntity<?> getMyNationPlayers() {
        User requester = getRequestUser();
        checkMemberOfNation(requester);
        trace(requester, "has loaded the list of his nation players.", null);
        return ResponseEntity.ok(requester.getNation().getPlayers());
    }


    @GetMapping("/getmynationrequests")
    public ResponseEntity<?> getMyNationRequest() {
        User requester = getRequestUser();
        Nation nation = requester.getNation();
        trace(requester, "load the list of his nation requests.", null);
        checkMemberOfNation(requester);
        if (requester.isAdmin() || requester.isOrga() || requester.isNationAdmin() || requester.isNationSheriff()) {
            return ResponseEntity.ok(nation.getJoinNationDemands());
        } else {
            throw new BadPrivilegesException();
        }
    }

    @GetMapping("/getallnations")
    public ResponseEntity<?> getAllNations() {
        return ResponseEntity.ok(nationRepository.findAll());
    }


    @GetMapping("/getmynation")
    public ResponseEntity<?> getMyNation() {
        User requester = getRequestUser();
        checkMemberOfNation(requester);
        return ResponseEntity.ok(requester.getNation());
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
            trace(requester, "force join nation", userToChange);
            return ResponseEntity.ok(requester);
        } else {
            throw new BadPrivilegesException();
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
                    trace(requester, "approve request", joinNationDemand);
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
                    throw new BadRequestException("The status can only be \"" + Status.APPROVED + "\" or \"" + Status.REFUSED + "\".");
            }
        } else {
            throw new BadPrivilegesException();
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

            if (candidate.getNation() != null) {
                trace(requester, "autoleave nation", candidate);
                candidate.setNation(null);
                userRepository.saveAndFlush(candidate);
            }
            cancelPendingRequests(candidate);
            JoinNationDemand joinNationDemand = new JoinNationDemand();
            joinNationDemand.setRequestTime(LocalDateTime.now());
            joinNationDemand.setMotivation(joinNationRequest.getMotivation());
            joinNationDemand.setCandidate(candidate);
            joinNationDemand.setNation(nation);
            joinNationDemand.setStatus(Status.PENDING);
            joinNationDemandRepository.saveAndFlush(joinNationDemand);
            trace(requester, "create joining request", joinNationDemand);
            return ResponseEntity.ok(joinNationDemand);
        } else {
            throw new BadPrivilegesException();
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
                    trace(candidate, "got his demand cancelled", k);
                    k.setStatus(Status.CANCELLED);
                    k.setProcessingTime(LocalDateTime.now());
                    joinNationDemandRepository.saveAndFlush(k);
                });
    }

    @GetMapping("/leavenation/{playerUuid}")
    public ResponseEntity<?> leaveNation(@PathVariable String playerUuid) {
        User requester = getRequestUser();
        if (requesterIsAdmin() || requesterIsOrga() || requester.getUuid().equals(playerUuid)) {
            User userToChange = userRepository.findByUuid(playerUuid)
                    .orElseThrow(() -> new NoSuchElementException("Player with uuid " + playerUuid + " not found."));
            userToChange.setNation(null);
            if (userToChange.isNationAdmin() || userToChange.isNationSheriff()) {
                userToChange.setRole(Role.PLAYER);
            }
            userRepository.saveAndFlush(userToChange);
            trace(requester, "leave nation", userToChange);
            return ResponseEntity.ok().build();
        } else {
            throw new BadPrivilegesException();
        }
    }
}

