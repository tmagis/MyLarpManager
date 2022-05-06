package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadPrivilegesException;
import be.larp.mylarpmanager.exceptions.BadRequestException;
import be.larp.mylarpmanager.models.uuid.Character;
import be.larp.mylarpmanager.models.uuid.Skill;
import be.larp.mylarpmanager.requests.*;
import be.larp.mylarpmanager.services.CharacterService;
import be.larp.mylarpmanager.services.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequestMapping("/api/v1/character")
public class CharacterController extends Controller {

    @Value("${warden.app.game.skill.points:10}")
    private int skillPoints;

    @Autowired
    private SkillService skillService;

    @Autowired
    private CharacterService characterService;

    @PostMapping("/changedetails")
    public ResponseEntity<?> updateCharacter(@Valid @RequestBody ChangeCharacterDetailsRequest changeCharacterDetailsRequest) {
        Character character = characterService.getCharacterByUuid(changeCharacterDetailsRequest.getUuid());
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
        Character character = characterService.getCharacterByUuid(killCharacterRequest.getUuid());
        if (requesterIsAdmin() || requesterIsOrga()) {
            character.setAlive(false);
            character.setReasonOfDeath(killCharacterRequest.getReasonOfDeath());
            characterService.save(character);
            trace(getRequestUser(), "killed character", character);
            return ResponseEntity.ok(character);
        } else {
            throw new BadPrivilegesException();
        }
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteCharacter(@PathVariable String uuid) {
        Character character = characterService.getCharacterByUuid(uuid);
        if (requesterIsAdmin() || requesterIsOrga() || getRequestUser().equals(character.getPlayer())) {
            //TODO avoid delete a dead character ? Disallow delete active character ?
            characterService.delete(character);
            return ResponseEntity.ok().build();
        } else {
            throw new BadPrivilegesException();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCharacter(@Valid @RequestBody CreateCharacterRequest createCharacterRequest) {
        Character character = new Character().setPlayer(getRequestUser());
        setDetails(createCharacterRequest, character);
        trace(getRequestUser(), "create character", character);
        return ResponseEntity.ok(character);
    }

    @GetMapping("/getpointsavailable/{uuid}")
    public ResponseEntity<?> addSkill(@PathVariable String uuid) {
        Character character = characterService.getCharacterByUuid(uuid);
        return ResponseEntity.ok(skillPoints - getPointsUsed(character.getSkills()));
    }

    @PostMapping("/addskill")
    public ResponseEntity<?> addSkill(@Valid @RequestBody AddCharacterSkillRequest addCharacterSkillRequest) {
        Character character = characterService.getCharacterByUuid(addCharacterSkillRequest.getCharacterUuid());
        Skill skill = skillService.getSkillByUuid(addCharacterSkillRequest.getSkillUuid());
        checkConstraints(character, skill);
        addSkill(character, skill);
        trace(getRequestUser(), "add skill", character);
        return ResponseEntity.ok(character);
    }

    @PostMapping("/removeskill")
    public ResponseEntity<?> removeSkill(@Valid @RequestBody AddCharacterSkillRequest removeCharacterSkillRequest) {
        Character character = characterService.getCharacterByUuid(removeCharacterSkillRequest.getCharacterUuid());
        Skill skill = skillService.getSkillByUuid(removeCharacterSkillRequest.getSkillUuid());
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
        characterService.save(character);
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
        characterService.save(character);
    }

    private int getPointsUsed(Collection<Skill> skills) {
        return skills.stream().mapToInt(Skill::getCost).sum();
    }

    private void setDetails(CreateCharacterRequest createCharacterRequest, Character character) {
        character.setAge(createCharacterRequest.getAge())
                .setBackground(createCharacterRequest.getBackground())
                .setName(createCharacterRequest.getName())
                .setPictureURL(createCharacterRequest.getPictureURL())
                .setRace(createCharacterRequest.getRace())
                .setLastModificationTime(LocalDateTime.now());
        characterService.save(character);
    }


}

