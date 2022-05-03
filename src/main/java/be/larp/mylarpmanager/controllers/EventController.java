package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadPrivilegesException;
import be.larp.mylarpmanager.models.uuid.Event;
import be.larp.mylarpmanager.models.uuid.SkillTree;
import be.larp.mylarpmanager.repositories.EventRepository;
import be.larp.mylarpmanager.requests.ChangeEventDetailsRequest;
import be.larp.mylarpmanager.requests.CreateEventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/event")
public class EventController extends Controller {

    @Autowired
    private EventRepository eventRepository;

    @PostMapping("/changedetails")
    public ResponseEntity<?> changeNationDetails(@Valid @RequestBody ChangeEventDetailsRequest changeEventDetailsRequest) {
        if (requesterIsAdmin()) {
            Event event = eventRepository.findByUuid(changeEventDetailsRequest.getUuid())
                    .orElseThrow(() -> new NoSuchElementException("Event with uuid " + changeEventDetailsRequest.getUuid() + " not found."));
            setValues(changeEventDetailsRequest, event);
            trace(getRequestUser(), "update event", event);
            return ResponseEntity.ok(event);
        } else {
            throw new BadPrivilegesException();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEvent(@Valid @RequestBody CreateEventRequest createEventRequest) {
        if (requesterIsAdmin()) {
            Event event = new Event();
            setValues(createEventRequest, event);
            trace(getRequestUser(), "create event", event);
            return ResponseEntity.ok(event);
        } else {
            throw new BadPrivilegesException();
        }
    }

    private void setValues(CreateEventRequest createEventRequest, Event event) {
        event.setConcluded(createEventRequest.isConcluded());
        event.setDateFrom(createEventRequest.getDateFrom());
        event.setDateTo(createEventRequest.getDateTo());
        event.setDeadlineLogisticPack(createEventRequest.getDeadlineLogisticPack());
        event.setYearTI(createEventRequest.getYearTI());
        event.setName(createEventRequest.getName());
        event.setDescription(createEventRequest.getDescription());
        eventRepository.saveAndFlush(event);
    }
}

