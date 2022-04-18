package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.models.SkillTree;
import be.larp.mylarpmanager.repositories.SkillRepository;
import be.larp.mylarpmanager.repositories.SkillTreeRepository;
import be.larp.mylarpmanager.requests.ChangeSkillTreeDetailsRequest;
import be.larp.mylarpmanager.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
      if(highPrivileges()){
        SkillTree skillTree = skillTreeRepository.findByUuid(changeSkillTreeDetailsRequest.getUuid())
                .orElseThrow(() -> new NoSuchElementException("SkillTree with uuid " + changeSkillTreeDetailsRequest.getUuid() + " not found."));
            skillTree.setDescription(changeSkillTreeDetailsRequest.getDescription());
            skillTree.setName(changeSkillTreeDetailsRequest.getName());
            skillTree.setBlessing(changeSkillTreeDetailsRequest.getBlessing());
            skillTreeRepository.saveAndFlush(skillTree);
            return ResponseEntity.ok(skillTree);
        }else{
            throw new BadCredentialsException("Your account privileges doesn't allow you to do that.");
        }
    }

    @GetMapping("/getallskilltrees")
    public ResponseEntity<?> getAllSkillTrees(){
        return ResponseEntity.ok(skillTreeRepository.findAll());
    }
}

