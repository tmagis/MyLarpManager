package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadPrivilegesException;
import be.larp.mylarpmanager.models.uuid.SkillTree;
import be.larp.mylarpmanager.requests.ChangeSkillTreeDetailsRequest;
import be.larp.mylarpmanager.requests.CreateSkillTreeRequest;
import be.larp.mylarpmanager.services.SkillTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/skilltree")
public class SkillTreeController extends Controller {

    @Autowired
    private SkillTreeService skillTreeService;

    @PostMapping("/changedetails")
    public ResponseEntity<?> changeSkillTreeDetails(@Valid @RequestBody ChangeSkillTreeDetailsRequest changeSkillTreeDetailsRequest) {
        if (requesterIsAdmin() || requesterIsOrga()) {
            SkillTree skillTree = skillTreeService.getSkillTreeByUuid(changeSkillTreeDetailsRequest.getUuid());
            setValues(skillTree, changeSkillTreeDetailsRequest);
            trace(getRequestUser(), "update SkillTree", skillTree);
            return ResponseEntity.ok(skillTree);
        } else {
            throw new BadPrivilegesException();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSkillTree(@Valid @RequestBody CreateSkillTreeRequest createSkillTreeRequest) {
        if (requesterIsAdmin() || requesterIsOrga()) {
            SkillTree skillTree = new SkillTree();
            setValues(skillTree, createSkillTreeRequest);
            trace(getRequestUser(), "create SkillTree", skillTree);
            return ResponseEntity.ok(skillTree);
        } else {
            throw new BadPrivilegesException();
        }
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteSkillTree(@PathVariable String uuid) {
        if (requesterIsAdmin()) {
            SkillTree skillTree = skillTreeService.getSkillTreeByUuid(uuid);
            skillTreeService.delete(skillTree);
            return ResponseEntity.ok().build();
        } else {
            throw new BadPrivilegesException();
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(skillTreeService.getAll());
    }

    private void setValues(SkillTree skillTree, CreateSkillTreeRequest createSkillTreeRequest) {
        skillTree.setDescription(createSkillTreeRequest.getDescription())
                .setName(createSkillTreeRequest.getName())
                .setBlessing(createSkillTreeRequest.getBlessing());
        skillTreeService.save(skillTree);
    }
}

