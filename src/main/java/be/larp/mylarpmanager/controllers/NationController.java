package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.models.Character;
import be.larp.mylarpmanager.models.Nation;
import be.larp.mylarpmanager.models.User;
import be.larp.mylarpmanager.repositories.CharacterRepository;
import be.larp.mylarpmanager.repositories.NationRepository;
import be.larp.mylarpmanager.repositories.UserRepository;
import be.larp.mylarpmanager.requests.ChangeCharacterDetailsRequest;
import be.larp.mylarpmanager.requests.ChangeNationDetailsRequest;
import be.larp.mylarpmanager.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/changedetails")
    public ResponseEntity<?> reset(@Valid @RequestBody ChangeNationDetailsRequest changeNationDetailsRequest) {
        Nation nation = nationRepository.findByUuid(changeNationDetailsRequest.getUuid())
                .orElseThrow(() -> new NoSuchElementException("Nation with uuid " + changeNationDetailsRequest.getUuid() + " not found."));
        nation.setName(changeNationDetailsRequest.getName());
        nation.setIntroText(changeNationDetailsRequest.getIntroText());
        nation.setFullDescription(changeNationDetailsRequest.getFullDescription());
        nationRepository.saveAndFlush(nation);
        return ResponseEntity.ok(nation);
    }

    @GetMapping("/getmynationplayers")
    public ResponseEntity<?> get(){
        User user = userRepository.findByUuid(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUuid())
                .orElseThrow(() -> new NoSuchElementException("User with uuid " + ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUuid() + " not found."));
        Nation nation = user.getNation();
        return ResponseEntity.ok(nation.getPlayers());
    }
}

