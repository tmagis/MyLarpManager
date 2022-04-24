package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadPrivilegesException;
import be.larp.mylarpmanager.models.Skill;
import be.larp.mylarpmanager.models.SkillTree;
import be.larp.mylarpmanager.repositories.SkillRepository;
import be.larp.mylarpmanager.repositories.SkillTreeRepository;
import be.larp.mylarpmanager.requests.ChangeSkillTreeDetailsRequest;
import be.larp.mylarpmanager.requests.CreateSkillTreeRequest;
import be.larp.mylarpmanager.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/skilltree")
public class SkillTreeController extends Controller {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SkillTreeRepository skillTreeRepository;

    @PostMapping("/changedetails")
    public ResponseEntity<?> changeNationDetails(@Valid @RequestBody ChangeSkillTreeDetailsRequest changeSkillTreeDetailsRequest) {
      if(requesterIsAdmin() || requesterIsOrga()){
        SkillTree skillTree = skillTreeRepository.findByUuid(changeSkillTreeDetailsRequest.getUuid())
                .orElseThrow(() -> new NoSuchElementException("SkillTree with uuid " + changeSkillTreeDetailsRequest.getUuid() + " not found."));
          setValues(skillTree, changeSkillTreeDetailsRequest);
          trace(getRequestUser(), "has updated SkillTree "+skillTree);
            return ResponseEntity.ok(skillTree);
        }else{
            throw new BadPrivilegesException("Your account privileges doesn't allow you to do that.");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> changeNationDetails(@Valid @RequestBody CreateSkillTreeRequest createSkillTreeRequest) {
        if(requesterIsAdmin() || requesterIsOrga()){
            SkillTree skillTree = new SkillTree();
            skillTree.setUuid(getRandomUuid());
            setValues(skillTree, createSkillTreeRequest);
            trace(getRequestUser(), "has updated SkillTree "+skillTree);
            return ResponseEntity.ok(skillTree);
        }else{
            throw new BadPrivilegesException("Your account privileges doesn't allow you to do that.");
        }
    }

    private void setValues(SkillTree skillTree, CreateSkillTreeRequest createSkillTreeRequest) {
        skillTree.setDescription(createSkillTreeRequest.getDescription());
        skillTree.setName(createSkillTreeRequest.getName());
        skillTree.setBlessing(createSkillTreeRequest.getBlessing());
        skillTreeRepository.saveAndFlush(skillTree);
    }

    @GetMapping("/getallskilltrees")
    public ResponseEntity<?> getAllSkillTrees(){
        return ResponseEntity.ok(skillTreeRepository.findAll());
    }
}

