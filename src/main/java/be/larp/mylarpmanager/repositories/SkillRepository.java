package be.larp.mylarpmanager.repositories;

import be.larp.mylarpmanager.models.uuid.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long>, UuidRepository<Skill> {

}
