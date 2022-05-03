package be.larp.mylarpmanager.repositories;

import be.larp.mylarpmanager.models.uuid.EventParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventParticipationRepository extends JpaRepository<EventParticipation, Long> {

    Optional<EventParticipation> findByUuid(String uuid);

}
