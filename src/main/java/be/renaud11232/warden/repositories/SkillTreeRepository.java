package be.renaud11232.warden.repositories;

import be.renaud11232.warden.models.SkillTree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillTreeRepository extends JpaRepository<SkillTree, Long> {

    Optional<SkillTree> findByName(String name);

    Optional<SkillTree> findByUuid(String uuid);

}
