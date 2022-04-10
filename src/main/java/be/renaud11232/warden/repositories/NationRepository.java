package be.renaud11232.warden.repositories;

import be.renaud11232.warden.models.Nation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NationRepository extends JpaRepository<Nation, Long> {

    Optional<Nation> findByName(String name);

    Optional<Nation> findByUuid(String uuid);

}
