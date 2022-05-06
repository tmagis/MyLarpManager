package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadPrivilegesException;
import be.larp.mylarpmanager.exceptions.BadRequestException;
import be.larp.mylarpmanager.models.*;
import be.larp.mylarpmanager.models.uuid.JoinNationDemand;
import be.larp.mylarpmanager.models.uuid.Nation;
import be.larp.mylarpmanager.models.uuid.User;
import be.larp.mylarpmanager.requests.*;
import be.larp.mylarpmanager.services.JoinNationDemandService;
import be.larp.mylarpmanager.services.NationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/nation")
public class NationController extends Controller {

    @Autowired
    private JoinNationDemandService joinNationDemandService;

    @Autowired
    private NationService nationService;

    @PostMapping("/changedetails")
    public ResponseEntity<?> changeNationDetails(@Valid @RequestBody ChangeNationDetailsRequest changeNationDetailsRequest) {
        User user = getRequestUser();
        Nation nation = nationService.getNationByUuid(changeNationDetailsRequest.getUuid());
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
        nation.setName(createNationRequest.getName())
                .setIntroText(createNationRequest.getIntroText())
                .setFullDescription(createNationRequest.getFullDescription())
                .setFamilyFriendly(createNationRequest.isFamilyFriendly())
                .setContributionInCents(createNationRequest.getContributionInCents())
                .setInternationalFriendly(createNationRequest.isInternationalFriendly())
                .setContributionMandatory(createNationRequest.isContributionMandatory());
        nationService.save(nation);
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
        return ResponseEntity.ok(nationService.getAll());
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
            Nation nation = nationService.getNationByUuid(joinNationRequest.getNationUuid());
            User userToChange = userService.getUserByUuid(joinNationRequest.getPlayerUuid());
            cancelPendingRequests(userToChange);
            userToChange.setNation(nation);
            userService.save(userToChange);
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
            JoinNationDemand joinNationDemand = joinNationDemandService.getJoinNationDemandByUuid(processDemandRequest.getUuid());
            switch (Status.valueOf(processDemandRequest.getStatus())) {
                case APPROVED:
                    joinNationDemand.setStatus(Status.APPROVED)
                            .setApprover(requester)
                            .setProcessingTime(LocalDateTime.now());
                    joinNationDemand.getCandidate().setNation(joinNationDemand.getNation());
                    userService.save(joinNationDemand.getCandidate());
                    joinNationDemandService.save(joinNationDemand);
                    trace(requester, "approve request", joinNationDemand);
                    return ResponseEntity.ok(joinNationDemand);
                case REFUSED:
                    if (processDemandRequest.getApproverMotivation() == null || processDemandRequest.getApproverMotivation().isBlank()) {
                        throw new BadRequestException("A motivation is required when refusing.");
                    } else {
                        joinNationDemand.setStatus(Status.REFUSED)
                                .setApproverMotivation(processDemandRequest.getApproverMotivation())
                                .setApprover(requester)
                                .setProcessingTime(LocalDateTime.now());
                        joinNationDemandService.save(joinNationDemand);
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
            Nation nation = nationService.getNationByUuid(joinNationRequest.getNationUuid());
            User candidate = userService.getUserByUuid(joinNationRequest.getPlayerUuid());
            if (candidate.getNation() != null) {
                trace(requester, "autoleave nation", candidate);
                candidate.setNation(null);
                userService.save(candidate);
            }
            cancelPendingRequests(candidate);
            JoinNationDemand joinNationDemand = new JoinNationDemand()
                    .setRequestTime(LocalDateTime.now())
                    .setMotivation(joinNationRequest.getMotivation())
                    .setCandidate(candidate)
                    .setNation(nation)
                    .setStatus(Status.PENDING);
            joinNationDemandService.save(joinNationDemand);
            trace(requester, "create joining request", joinNationDemand);
            return ResponseEntity.ok(joinNationDemand);
        } else {
            throw new BadPrivilegesException();
        }
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteNation(@PathVariable String uuid) {
        if (requesterIsAdmin()) {
            Nation nation = nationService.getNationByUuid(uuid);
            nationService.delete(nation);
            return ResponseEntity.ok().build();
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
                    joinNationDemandService.save(k);
                });
    }

    @GetMapping("/leavenation/{playerUuid}")
    public ResponseEntity<?> leaveNation(@PathVariable String playerUuid) {
        User requester = getRequestUser();
        if (requesterIsAdmin() || requesterIsOrga() || requester.getUuid().equals(playerUuid)) {
            User userToChange = userService.getUserByUuid(playerUuid);
            userToChange.setNation(null);
            if (userToChange.isNationAdmin() || userToChange.isNationSheriff()) {
                userToChange.setRole(Role.PLAYER);
            }
            userService.save(userToChange);
            trace(requester, "leave nation", userToChange);
            return ResponseEntity.ok().build();
        } else {
            throw new BadPrivilegesException();
        }
    }
}

