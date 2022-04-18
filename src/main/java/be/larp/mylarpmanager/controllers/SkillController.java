package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.models.*;
import be.larp.mylarpmanager.repositories.SkillRepository;
import be.larp.mylarpmanager.repositories.SkillTreeRepository;
import be.larp.mylarpmanager.requests.ChangeSkillDetailsRequest;
import be.larp.mylarpmanager.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
      if(highPrivileges()){
        Skill skill = skillRepository.findByUuid(changeSkillDetailsRequest.getUuid())
                .orElseThrow(() -> new NoSuchElementException("Skill with uuid " + changeSkillDetailsRequest.getUuid() + " not found."));
        SkillTree skillTree = skillTreeRepository.findByUuid(changeSkillDetailsRequest.getSkillTreeUuid())
                .orElseThrow(() -> new NoSuchElementException("SkillTree with uuid " + changeSkillDetailsRequest.getSkillTreeUuid() + " not found."));
            skill.setName(changeSkillDetailsRequest.getName());
            skill.setSkillTree(skillTree);
            skill.setHidden(changeSkillDetailsRequest.isHidden());
            skill.setCost(changeSkillDetailsRequest.getCost());
            skill.setLevel(changeSkillDetailsRequest.getLevel());
            skill.setAllowMultiple(changeSkillDetailsRequest.isAllowMultiple());
            skill.setDescription(changeSkillDetailsRequest.getDescription());
            skill.setIconURL(changeSkillDetailsRequest.getIconURL());
            skillRepository.saveAndFlush(skill);
            return ResponseEntity.ok(skill);
        }else{
            throw new BadCredentialsException("Your account privileges doesn't allow you to do that.");
        }
    }
}

