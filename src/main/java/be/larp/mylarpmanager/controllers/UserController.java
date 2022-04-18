package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.models.Nation;
import be.larp.mylarpmanager.models.User;
import be.larp.mylarpmanager.repositories.NationRepository;
import be.larp.mylarpmanager.repositories.UserRepository;
import be.larp.mylarpmanager.requests.ChangeUserDetailsRequest;
import be.larp.mylarpmanager.requests.JoinNationRequest;
import be.larp.mylarpmanager.security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/user")
public class UserController extends Controller {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NationRepository nationRepository;

    @Autowired
    JwtUtils jwtUtils;

    //TODO Allow to change someone else with roles ?
    @PostMapping("/changedetails")
    public ResponseEntity<?> changeDetails(@Valid @RequestBody ChangeUserDetailsRequest changeUserDetailsRequest) {
        User user = getRequestUser();
        user.setUsername(changeUserDetailsRequest.getUsername());
        user.setEmail(changeUserDetailsRequest.getEmail());
        user.setFirstName(changeUserDetailsRequest.getFirstName());
        user.setLastName(changeUserDetailsRequest.getLastName());
        userRepository.saveAndFlush(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/getmycharacters")
    public ResponseEntity<?> getMyCharacters(){
        User user = getRequestUser();
        return ResponseEntity.ok(user.getCharacters());
    }

    @PostMapping("/joinnation")
    public ResponseEntity<?> joinNation(@Valid @RequestBody JoinNationRequest joinNationRequest) {
        User user = getRequestUser();
        Nation nation = nationRepository.findByUuid(joinNationRequest.getUuid())
                .orElseThrow(() -> new NoSuchElementException("Nation with uuid " + joinNationRequest.getUuid() + " not found."));
        user.setNation(nation);
        userRepository.saveAndFlush(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/leavenation")
    public ResponseEntity<?> leaveNation(){
        User user = getRequestUser();
        user.setNation(null);
        userRepository.saveAndFlush(user);
        return ResponseEntity.ok(user.getCharacters());
    }

}
