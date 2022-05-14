package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadPrivilegesException;
import be.larp.mylarpmanager.models.uuid.Skill;
import be.larp.mylarpmanager.models.uuid.SkillTree;
import be.larp.mylarpmanager.requests.ChangeSkillDetailsRequest;
import be.larp.mylarpmanager.requests.CreateSkillRequest;
import be.larp.mylarpmanager.services.SkillService;
import be.larp.mylarpmanager.services.SkillTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/skill")
public class SkillController extends Controller {

    @Autowired
    private SkillService skillService;

    @Autowired
    private SkillTreeService skillTreeService;

    @PostMapping("/changeDetails")
    public ResponseEntity<?> changeSkillDetails(@Valid @RequestBody ChangeSkillDetailsRequest changeSkillDetailsRequest) {
        if (requesterIsAdmin() || requesterIsOrga()) {
            Skill skill = skillService.getSkillByUuid(changeSkillDetailsRequest.getUuid());
            SkillTree skillTree = skillTreeService.getSkillTreeByUuid(changeSkillDetailsRequest.getSkillTreeUuid());
            setValues(skill, changeSkillDetailsRequest, skillTree);
            trace(getRequestUser(), "update skill", skill);
            return ResponseEntity.ok(skill);
        } else {
            throw new BadPrivilegesException();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSkill(@Valid @RequestBody CreateSkillRequest createSkillRequest) {
        if (requesterIsAdmin() || requesterIsOrga()) {
            Skill skill = new Skill();
            SkillTree skillTree = skillTreeService.getSkillTreeByUuid(createSkillRequest.getSkillTreeUuid());
            setValues(skill, createSkillRequest, skillTree);
            trace(getRequestUser(), "create skill", skill);
            return ResponseEntity.ok(skill);
        } else {
            throw new BadPrivilegesException();
        }
    }

    @GetMapping("/getAllSkillTreeSkills/{skillTreeUuid}")
    public ResponseEntity<?> getAllSkillTreeSkills(@PathVariable String skillTreeUuid) {
        if (requesterIsAdmin()) {
            SkillTree skillTree = skillTreeService.getSkillTreeByUuid(skillTreeUuid);
            Collection<Skill> skills = skillTree.getSkills();
            if (!requesterIsAdmin() && !requesterIsOrga()) {
                skills = skills.stream().filter(skill -> !skill.isHidden()).collect(Collectors.toList());
            }
            trace(getRequestUser(), "requested skills", skillTree);
            return ResponseEntity.ok(skills);
        } else {
            throw new BadPrivilegesException();
        }
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteSkill(@PathVariable String uuid) {
        if (requesterIsAdmin()) {
            Skill skill = skillService.getSkillByUuid(uuid);
            //TODO check cascade configuration for characters having that skill
            skillService.delete(skill);
            trace(getRequestUser(), "deleted skill", skill);
            return ResponseEntity.ok().build();
        } else {
            throw new BadPrivilegesException();
        }
    }

    private void setValues(Skill skill, CreateSkillRequest createSkillRequest, SkillTree skillTree) {
        skill.setName(createSkillRequest.getName())
                .setSkillTree(skillTree)
                .setHidden(createSkillRequest.isHidden())
                .setCost(createSkillRequest.getCost())
                .setLevel(createSkillRequest.getLevel())
                .setAllowMultiple(createSkillRequest.isAllowMultiple())
                .setDescription(createSkillRequest.getDescription())
                .setIconURL(createSkillRequest.getIconURL());
        skillService.save(skill);
    }
}

