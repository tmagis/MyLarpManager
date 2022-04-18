package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadRequestException;
import be.larp.mylarpmanager.models.Character;
import be.larp.mylarpmanager.models.Nation;
import be.larp.mylarpmanager.models.Role;
import be.larp.mylarpmanager.models.User;
import be.larp.mylarpmanager.repositories.CharacterRepository;
import be.larp.mylarpmanager.repositories.NationRepository;
import be.larp.mylarpmanager.repositories.UserRepository;
import be.larp.mylarpmanager.requests.ChangeCharacterDetailsRequest;
import be.larp.mylarpmanager.requests.ChangeNationDetailsRequest;
import be.larp.mylarpmanager.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
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
        if( highPrivileges() || (user.getNation() != null && user.getNation().getUuid().equals(nation.getUuid()) && user.getRole().equals(Role.NATION_MANAGER))) {
            nation.setName(changeNationDetailsRequest.getName());
            nation.setIntroText(changeNationDetailsRequest.getIntroText());
            nation.setFullDescription(changeNationDetailsRequest.getFullDescription());
            nationRepository.saveAndFlush(nation);
            return ResponseEntity.ok(nation);
        }else{
            throw new BadCredentialsException("Your account privileges doesn't allow you to do that.");
        }
    }

    @GetMapping("/getmynationplayers")
    public ResponseEntity<?> getMyNationPlayers(){
        User user = getRequestUser();
        Nation nation = user.getNation();
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

