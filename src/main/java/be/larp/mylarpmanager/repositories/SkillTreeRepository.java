package be.larp.mylarpmanager.repositories;

import be.larp.mylarpmanager.models.uuid.SkillTree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillTreeRepository extends JpaRepository<SkillTree, Long>, UuidRepository<SkillTree> {

}
