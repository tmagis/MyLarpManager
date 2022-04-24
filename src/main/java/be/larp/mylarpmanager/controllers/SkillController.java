package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadPrivilegesException;
import be.larp.mylarpmanager.models.*;
import be.larp.mylarpmanager.repositories.SkillRepository;
import be.larp.mylarpmanager.repositories.SkillTreeRepository;
import be.larp.mylarpmanager.requests.ChangeSkillDetailsRequest;
import be.larp.mylarpmanager.requests.CreateSkillRequest;
import be.larp.mylarpmanager.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/skill")
public class SkillController extends Controller {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SkillTreeRepository skillTreeRepository;

    @PostMapping("/changedetails")
    public ResponseEntity<?> changeNationDetails(@Valid @RequestBody ChangeSkillDetailsRequest changeSkillDetailsRequest) {
      if(requesterIsAdmin() || requesterIsOrga()){
        Skill skill = skillRepository.findByUuid(changeSkillDetailsRequest.getUuid())
                .orElseThrow(() -> new NoSuchElementException("Skill with uuid " + changeSkillDetailsRequest.getUuid() + " not found."));
        SkillTree skillTree = skillTreeRepository.findByUuid(changeSkillDetailsRequest.getSkillTreeUuid())
                .orElseThrow(() -> new NoSuchElementException("SkillTree with uuid " + changeSkillDetailsRequest.getSkillTreeUuid() + " not found."));
          setValues(skill, changeSkillDetailsRequest, skillTree);
          trace(getRequestUser(), "has updated skill "+skill);
            return ResponseEntity.ok(skill);
        }else{
            throw new BadPrivilegesException("Your account privileges doesn't allow you to do that.");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSkill(@Valid @RequestBody CreateSkillRequest createSkillRequest) {
        if(requesterIsAdmin() || requesterIsOrga()){
            Skill skill = new Skill();
            skill.setUuid(getRandomUuid());
            SkillTree skillTree = skillTreeRepository.findByUuid(createSkillRequest.getSkillTreeUuid())
                    .orElseThrow(() -> new NoSuchElementException("SkillTree with uuid " + createSkillRequest.getSkillTreeUuid() + " not found."));
            setValues(skill, createSkillRequest, skillTree);
            trace(getRequestUser(), "has created skill "+skill);
            return ResponseEntity.ok(skill);
        }else{
            throw new BadPrivilegesException("Your account privileges doesn't allow you to do that.");
        }
    }

    private void setValues(Skill skill, CreateSkillRequest createSkillRequest, SkillTree skillTree) {
        skill.setName(createSkillRequest.getName());
        skill.setSkillTree(skillTree);
        skill.setHidden(createSkillRequest.isHidden());
        skill.setCost(createSkillRequest.getCost());
        skill.setLevel(createSkillRequest.getLevel());
        skill.setAllowMultiple(createSkillRequest.isAllowMultiple());
        skill.setDescription(createSkillRequest.getDescription());
        skill.setIconURL(createSkillRequest.getIconURL());
        skillRepository.saveAndFlush(skill);
    }
}

