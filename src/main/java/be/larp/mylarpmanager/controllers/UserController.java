package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.models.User;
import be.larp.mylarpmanager.repositories.UserRepository;
import be.larp.mylarpmanager.requests.ChangeUserDetailsRequest;
import be.larp.mylarpmanager.security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
    JwtUtils jwtUtils;

    @PostMapping("/changedetails")
    public ResponseEntity<?> reset(@Valid @RequestBody ChangeUserDetailsRequest changeUserDetailsRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setUsername(changeUserDetailsRequest.getUsername());
        user.setEmail(changeUserDetailsRequest.getEmail());
        user.setFirstName(changeUserDetailsRequest.getFirstName());
        user.setLastName(changeUserDetailsRequest.getLastName());
        userRepository.saveAndFlush(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/getmycharacters")
    public ResponseEntity<?> get(){
        User user = userRepository.findByUuid(((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUuid())
                .orElseThrow(() -> new NoSuchElementException("User with uuid " + ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUuid() + " not found."));
        return ResponseEntity.ok(user.getCharacters());
    }


}
