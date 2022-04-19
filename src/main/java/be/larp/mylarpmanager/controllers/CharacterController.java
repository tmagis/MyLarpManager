package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadPrivilegesException;
import be.larp.mylarpmanager.models.Character;
import be.larp.mylarpmanager.models.Role;
import be.larp.mylarpmanager.repositories.CharacterRepository;
import be.larp.mylarpmanager.requests.ChangeCharacterDetailsRequest;
import be.larp.mylarpmanager.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/character")
public class CharacterController extends Controller {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CharacterRepository characterRepository;

    @PostMapping("/changedetails")
    public ResponseEntity<?> reset(@Valid @RequestBody ChangeCharacterDetailsRequest changeCharacterDetailsRequest) {
        Character character = characterRepository.findByUuid(changeCharacterDetailsRequest.getUuid())
                .orElseThrow(() -> new NoSuchElementException("Character with uuid " + changeCharacterDetailsRequest.getUuid() + " not found."));
        if(highPrivileges() || character.getPlayer().getUuid().equals(getRequestUser().getUuid()) ){
            character.setAge(changeCharacterDetailsRequest.getAge());
            character.setBackground(changeCharacterDetailsRequest.getBackground());
            character.setName(changeCharacterDetailsRequest.getName());
            character.setReasonOfDeath(changeCharacterDetailsRequest.getReasonOfDeath());
            character.setPictureURL(changeCharacterDetailsRequest.getPictureURL());
            character.setLastModificationTime(LocalDateTime.now());
            characterRepository.saveAndFlush(character);
            trace(getRequestUser(), "updated character :"+character);
            return ResponseEntity.ok(character);
        }else{
            throw new BadPrivilegesException("Your account privileges doesn't allow you to do that.");
        }
    }


}

