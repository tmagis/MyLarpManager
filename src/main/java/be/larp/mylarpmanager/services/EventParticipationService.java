package be.larp.mylarpmanager.services;

import be.larp.mylarpmanager.models.uuid.Event;
import be.larp.mylarpmanager.models.uuid.EventParticipation;
import be.larp.mylarpmanager.repositories.EventParticipationRepository;
import be.larp.mylarpmanager.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
@Transactional
public class EventParticipationService {

    @Autowired
    private EventParticipationRepository eventParticipationRepository;

    public EventParticipation getEventParticipationByUuid(String uuid) {
        return eventParticipationRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("EventParticipation with uuid " + uuid + " not found."));
    }

    public void delete(EventParticipation eventParticipation) {
        eventParticipationRepository.delete(eventParticipation);
    }

    public void save(EventParticipation eventParticipation) {
        eventParticipationRepository.saveAndFlush(eventParticipation);
    }
}
