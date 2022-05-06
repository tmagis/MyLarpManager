package be.larp.mylarpmanager.repositories;

import be.larp.mylarpmanager.models.uuid.Nation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NationRepository extends JpaRepository<Nation, Long>, UuidRepository<Nation> {

}
