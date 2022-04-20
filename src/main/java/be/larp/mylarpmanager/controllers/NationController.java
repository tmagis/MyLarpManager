package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadPrivilegesException;
import be.larp.mylarpmanager.exceptions.BadRequestException;
import be.larp.mylarpmanager.models.Nation;
import be.larp.mylarpmanager.models.Role;
import be.larp.mylarpmanager.models.User;
import be.larp.mylarpmanager.repositories.NationRepository;
import be.larp.mylarpmanager.requests.ChangeNationDetailsRequest;
import be.larp.mylarpmanager.requests.CreateNationRequest;
import be.larp.mylarpmanager.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/nation")
public class NationController extends Controller {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private NationRepository nationRepository;

    @PostMapping("/changedetails")
    public ResponseEntity<?> changeNationDetails(@Valid @RequestBody ChangeNationDetailsRequest changeNationDetailsRequest) {
      User user = getRequestUser();
        Nation nation = nationRepository.findByUuid(changeNationDetailsRequest.getUuid())
                .orElseThrow(() -> new NoSuchElementException("Nation with uuid " + changeNationDetailsRequest.getUuid() + " not found."));
        if( admin() || (user.getNation() != null && user.getNation().getUuid().equals(nation.getUuid()) && user.getRole().equals(Role.NATION_MANAGER))) {
            setNationValues(nation, changeNationDetailsRequest);
            trace(user, "has updated nation: "+nation);
            return ResponseEntity.ok(nation);
        }else{
            throw new BadPrivilegesException("Your account privileges doesn't allow you to do that.");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNation(@Valid @RequestBody CreateNationRequest createNationRequest) {
        User user = getRequestUser();
        if( admin() ) {
            Nation nation = new Nation();
            nation.setUuid(getRandomUuid());
            setNationValues(nation, createNationRequest);
            trace(user, "has created nation: "+nation);
            return ResponseEntity.ok(nation);
        }else{
            throw new BadPrivilegesException("Your account privileges doesn't allow you to do that.");
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
    public ResponseEntity<?> getMyNationPlayers(){
        User user = getRequestUser();
        Nation nation = user.getNation();
        trace(user, "has loaded the list of his nation players.");
        if(nation!=null) {
            return ResponseEntity.ok(nation.getPlayers());
        }else{
            throw new BadRequestException("You don't belong to a nation.");
        }
    }

    @GetMapping("/getallnations")
    public ResponseEntity<?> getAllNations(){
        return ResponseEntity.ok(nationRepository.findAll());
    }
}

