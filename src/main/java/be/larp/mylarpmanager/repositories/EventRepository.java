package be.larp.mylarpmanager.repositories;

import be.larp.mylarpmanager.models.uuid.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, UuidRepository<Event> {

}
