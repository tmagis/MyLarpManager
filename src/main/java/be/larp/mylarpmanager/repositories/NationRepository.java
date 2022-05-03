package be.larp.mylarpmanager.repositories;

import be.larp.mylarpmanager.models.uuid.Nation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NationRepository extends JpaRepository<Nation, Long> {

    Optional<Nation> findByName(String name);

    Optional<Nation> findByUuid(String uuid);

}
