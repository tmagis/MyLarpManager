package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadPrivilegesException;
import be.larp.mylarpmanager.exceptions.BadRequestException;
import be.larp.mylarpmanager.models.uuid.Character;
import be.larp.mylarpmanager.models.uuid.Event;
import be.larp.mylarpmanager.models.uuid.EventParticipation;
import be.larp.mylarpmanager.models.uuid.Nation;
import be.larp.mylarpmanager.repositories.CharacterRepository;
import be.larp.mylarpmanager.repositories.EventParticipationRepository;
import be.larp.mylarpmanager.repositories.EventRepository;
import be.larp.mylarpmanager.repositories.NationRepository;
import be.larp.mylarpmanager.requests.*;
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

    @Autowired
    private EventParticipationRepository eventParticipationRepository;

    @Autowired
    private NationRepository nationRepository;

    @Autowired
    private CharacterRepository characterRepository;

    @PostMapping("/changedetails")
    public ResponseEntity<?> changeEventDetails(@Valid @RequestBody ChangeEventDetailsRequest changeEventDetailsRequest) {
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

    @PostMapping("/participate")
    public ResponseEntity<?> participate(@Valid @RequestBody ParticipateEventRequest participateEventRequest) {
        if (requesterIsAdmin() || requesterIsNationAdmin() || requesterIsOrga()) {
            Event event = eventRepository.findByUuid(participateEventRequest.getEventUuid())
                    .orElseThrow(() -> new NoSuchElementException("Event with uuid " + participateEventRequest.getEventUuid() + " not found."));

            Nation nation = nationRepository.findByUuid(participateEventRequest.getNationUuid())
                    .orElseThrow(() -> new NoSuchElementException("Nation with uuid " + participateEventRequest.getNationUuid() + " not found."));
            EventParticipation eventParticipation = new EventParticipation();
            eventParticipation.setEvent(event);
            eventParticipation.setNation(nation);
            trace(getRequestUser(), "nation join event", eventParticipation);
            return ResponseEntity.ok(eventParticipation);
        } else {
            throw new BadPrivilegesException();
        }
    }

    @PostMapping("/setchosenone")
    public ResponseEntity<?> setChosenOne(@Valid @RequestBody SetChosenOneRequest setChosenOneRequest) {
        if (requesterIsAdmin() || requesterIsNationAdmin() || requesterIsOrga()) {
            EventParticipation eventParticipation = eventParticipationRepository.findByUuid(setChosenOneRequest.getEventParticipationUuid())
                    .orElseThrow(() -> new NoSuchElementException("EventParticipation with uuid " + setChosenOneRequest.getEventParticipationUuid() + " not found."));
            Character chosenOne = characterRepository.findByUuid(setChosenOneRequest.getCharacterUuid())
                    .orElseThrow(() -> new NoSuchElementException("Character with uuid " + setChosenOneRequest.getCharacterUuid() + " not found."));
            if(!chosenOne.getPlayer().getNation().equals(eventParticipation.getNation())){
                throw new BadRequestException("Cannot set chosen one for a character not inside the nation.");
            }
            eventParticipation.setChosenOne(chosenOne);
            eventParticipationRepository.saveAndFlush(eventParticipation);
            trace(getRequestUser(), "set chosen one", eventParticipation);
            return ResponseEntity.ok(eventParticipation);
        } else {
            throw new BadPrivilegesException();
        }
    }

    @PostMapping("/setsummary")
    public ResponseEntity<?> setConclusion(@Valid @RequestBody SetSummaryRequest setSummaryRequest) {
        if (requesterIsAdmin() || requesterIsNationAdmin() || requesterIsOrga()) {
            EventParticipation eventParticipation = eventParticipationRepository.findByUuid(setSummaryRequest.getEventParticipationUuid())
                    .orElseThrow(() -> new NoSuchElementException("EventParticipation with uuid " + setSummaryRequest.getEventParticipationUuid() + " not found."));
            eventParticipation.setSummary(setSummaryRequest.getSummary());
            eventParticipationRepository.saveAndFlush(eventParticipation);
            trace(getRequestUser(), "set summary", eventParticipation);
            return ResponseEntity.ok(eventParticipation);
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

