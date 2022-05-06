package be.larp.mylarpmanager.services;

import be.larp.mylarpmanager.models.uuid.Skill;
import be.larp.mylarpmanager.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
@Transactional
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    public Skill getSkillByUuid(String uuid){
        return skillRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("Skill with uuid " + uuid + " not found."));
    }

    public void delete(Skill skill){
        skillRepository.delete(skill);
    }

    public void save(Skill skill){
        skillRepository.saveAndFlush(skill);
    }
}
