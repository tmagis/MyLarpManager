package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadPrivilegesException;
import be.larp.mylarpmanager.exceptions.BadRequestException;
import be.larp.mylarpmanager.models.uuid.Character;
import be.larp.mylarpmanager.models.uuid.Skill;
import be.larp.mylarpmanager.repositories.CharacterRepository;
import be.larp.mylarpmanager.repositories.SkillRepository;
import be.larp.mylarpmanager.requests.*;
import be.larp.mylarpmanager.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/character")
public class CharacterController extends Controller {

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${warden.app.game.skill.points:10}")
    private int skillPoints;

    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private CharacterRepository characterRepository;

    @PostMapping("/changedetails")
    public ResponseEntity<?> updateCharacter(@Valid @RequestBody ChangeCharacterDetailsRequest changeCharacterDetailsRequest) {
        Character character = characterRepository.findByUuid(changeCharacterDetailsRequest.getUuid())
                .orElseThrow(() -> new NoSuchElementException("Character with uuid " + changeCharacterDetailsRequest.getUuid() + " not found."));
        if (requesterIsAdmin() || requesterIsOrga() || character.getPlayer().getUuid().equals(getRequestUser().getUuid())) {
            setDetails(changeCharacterDetailsRequest, character);
            trace(getRequestUser(), "update character", character);
            return ResponseEntity.ok(character);
        } else {
            throw new BadPrivilegesException();
        }
    }

    @PostMapping("/kill")
    public ResponseEntity<?> kill(@Valid @RequestBody KillCharacterRequest killCharacterRequest) {
        Character character = characterRepository.findByUuid(killCharacterRequest.getUuid())
                .orElseThrow(() -> new NoSuchElementException("Character with uuid " + killCharacterRequest.getUuid() + " not found."));
        if (requesterIsAdmin() || requesterIsOrga()) {
            character.setAlive(false);
            character.setReasonOfDeath(killCharacterRequest.getReasonOfDeath());
            characterRepository.saveAndFlush(character);
            trace(getRequestUser(), "killed character", character);
            return ResponseEntity.ok(character);
        } else {
            throw new BadPrivilegesException();
        }
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteCharacter(@PathVariable String uuid) {
        Character character = characterRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("Character with uuid " + uuid + " not found."));
        if (requesterIsAdmin() || requesterIsOrga() || getRequestUser().equals(character.getPlayer())) {
            //TODO avoid delete a dead character ? Disallow delete active character ?
            characterRepository.delete(character);
            return ResponseEntity.ok().build();
        } else {
            throw new BadPrivilegesException();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCharacter(@Valid @RequestBody CreateCharacterRequest createCharacterRequest) {
        Character character = new Character();
        character.setCreationTime(LocalDateTime.now());
        character.setLastModificationTime(LocalDateTime.now());
        character.setPlayer(getRequestUser());
        setDetails(createCharacterRequest, character);
        trace(getRequestUser(), "create character", character);
        return ResponseEntity.ok(character);
    }

    @GetMapping("/getpointsavailable/{uuid}")
    public ResponseEntity<?> addSkill(@PathVariable String uuid) {
        Character character = characterRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("Character with uuid " + uuid + " not found."));
        return ResponseEntity.ok(skillPoints - getPointsUsed(character.getSkills()));
    }

    @PostMapping("/addskill")
    public ResponseEntity<?> addSkill(@Valid @RequestBody AddCharacterSkillRequest addCharacterSkillRequest) {
        Character character = characterRepository.findByUuid(addCharacterSkillRequest.getCharacterUuid())
                .orElseThrow(() -> new NoSuchElementException("Character with uuid " + addCharacterSkillRequest.getCharacterUuid() + " not found."));
        Skill skill = skillRepository.findByUuid(addCharacterSkillRequest.getSkillUuid())
                .orElseThrow(() -> new NoSuchElementException("Skill with uuid " + addCharacterSkillRequest.getSkillUuid() + " not found."));
        checkConstraints(character, skill);
        addSkill(character, skill);
        trace(getRequestUser(), "add skill", character);
        return ResponseEntity.ok(character);
    }

    @PostMapping("/removeskill")
    public ResponseEntity<?> removeSkill(@Valid @RequestBody AddCharacterSkillRequest removeCharacterSkillRequest) {
        Character character = characterRepository.findByUuid(removeCharacterSkillRequest.getCharacterUuid())
                .orElseThrow(() -> new NoSuchElementException("Character with uuid " + removeCharacterSkillRequest.getCharacterUuid() + " not found."));
        Skill skill = skillRepository.findByUuid(removeCharacterSkillRequest.getSkillUuid())
                .orElseThrow(() -> new NoSuchElementException("Skill with uuid " + removeCharacterSkillRequest.getSkillUuid() + " not found."));
        checkConstraints(character, skill);
        removeSkill(character, skill);
        trace(getRequestUser(), "remove skill", character);
        return ResponseEntity.ok(character);
    }

    private void checkConstraints(Character character, Skill skill) {
        if (skill.isHidden() && !(requesterIsAdmin() || requesterIsOrga())) {
            throw new BadPrivilegesException();
        }
        if (!getRequestUser().equals(character.getPlayer()) && !(requesterIsAdmin() || requesterIsOrga())) {
            throw new BadPrivilegesException();
        }
    }

    private void removeSkill(Character character, Skill skill) {
        if (!character.getSkills().remove(skill)) {
            throw new BadRequestException("The character don't have that skill.");
        }
        characterRepository.saveAndFlush(character);
    }

    private void addSkill(Character character, Skill skill) {
        Collection<Skill> skills = character.getSkills();
        int credit = skillPoints - getPointsUsed(skills);
        if (!character.isNPC() && skill.getCost() > credit) {
            throw new BadRequestException("You cannot afford this skill. (cost: " + skill.getCost() + " while only " + credit + "is available).");
        }
        if (!skill.isAllowMultiple() && skills.contains(skill)) {
            throw new BadRequestException("This skill is allowed only once.");
        }
        skills.add(skill);
        characterRepository.saveAndFlush(character);
    }

    private int getPointsUsed(Collection<Skill> skills) {
        return skills.stream().mapToInt(Skill::getCost).sum();
    }

    private void setDetails(CreateCharacterRequest createCharacterRequest, Character character) {
        character.setAge(createCharacterRequest.getAge());
        character.setBackground(createCharacterRequest.getBackground());
        character.setName(createCharacterRequest.getName());
        character.setPictureURL(createCharacterRequest.getPictureURL());
        character.setRace(createCharacterRequest.getRace());
        character.setLastModificationTime(LocalDateTime.now());
        characterRepository.saveAndFlush(character);
    }


}

