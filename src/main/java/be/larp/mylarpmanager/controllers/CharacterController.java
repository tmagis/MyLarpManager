package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadPrivilegesException;
import be.larp.mylarpmanager.models.uuid.Character;
import be.larp.mylarpmanager.repositories.CharacterRepository;
import be.larp.mylarpmanager.requests.ChangeCharacterDetailsRequest;
import be.larp.mylarpmanager.requests.CreateCharacterRequest;
import be.larp.mylarpmanager.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> updateCharacter(@Valid @RequestBody ChangeCharacterDetailsRequest changeCharacterDetailsRequest) {
        Character character = characterRepository.findByUuid(changeCharacterDetailsRequest.getUuid())
                .orElseThrow(() -> new NoSuchElementException("Character with uuid " + changeCharacterDetailsRequest.getUuid() + " not found."));
        if (requesterIsAdmin() || requesterIsOrga() || character.getPlayer().getUuid().equals(getRequestUser().getUuid())) {
            setDetails(changeCharacterDetailsRequest, character);
            trace(getRequestUser(), "update character ", character);
            return ResponseEntity.ok(character);
        } else {
            throw new BadPrivilegesException();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCharacter(@Valid @RequestBody CreateCharacterRequest createCharacterRequest) {
        Character character = new Character();
        character.setCreationTime(LocalDateTime.now());
        character.setLastModificationTime(LocalDateTime.now());
        character.setAlive(true);
        setDetails(createCharacterRequest, character);
        trace(getRequestUser(), "create character :", character);
        return ResponseEntity.ok(character);
    }

    private void setDetails(CreateCharacterRequest createCharacterRequest, Character character) {
        character.setAge(createCharacterRequest.getAge());
        character.setBackground(createCharacterRequest.getBackground());
        character.setName(createCharacterRequest.getName());
        character.setReasonOfDeath(createCharacterRequest.getReasonOfDeath());
        character.setPictureURL(createCharacterRequest.getPictureURL());
        character.setLastModificationTime(LocalDateTime.now());
        characterRepository.saveAndFlush(character);
    }


}

