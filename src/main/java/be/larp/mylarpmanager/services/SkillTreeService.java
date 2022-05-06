package be.larp.mylarpmanager.services;

import be.larp.mylarpmanager.models.uuid.SkillTree;
import be.larp.mylarpmanager.repositories.SkillTreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class SkillTreeService {

    @Autowired
    private SkillTreeRepository skillTreeRepository;

    public SkillTree getSkillTreeByUuid(String uuid){
        return skillTreeRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("SkillTree with uuid " + uuid + " not found."));
    }

    public void save(SkillTree skillTree){
        skillTreeRepository.saveAndFlush(skillTree);
    }

    public void delete(SkillTree skillTree){
        skillTreeRepository.delete(skillTree);
    }

    public List<SkillTree> getAll() {
        return skillTreeRepository.findAll();
    }
}
