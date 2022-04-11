package be.larp.mylarpmanager.repositories;

import be.larp.mylarpmanager.models.SkillTree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillTreeRepository extends JpaRepository<SkillTree, Long> {

    Optional<SkillTree> findByName(String name);

    Optional<SkillTree> findByUuid(String uuid);

}
