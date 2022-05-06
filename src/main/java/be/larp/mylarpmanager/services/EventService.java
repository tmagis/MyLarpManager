package be.larp.mylarpmanager.services;

import be.larp.mylarpmanager.models.uuid.Event;
import be.larp.mylarpmanager.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
@Transactional
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event getEventByUuid(String uuid) {
        return eventRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("Event with uuid " + uuid + " not found."));
    }

    public void delete(Event event) {
        eventRepository.delete(event);
    }

    public void save(Event event) {
        eventRepository.saveAndFlush(event);
    }
}
