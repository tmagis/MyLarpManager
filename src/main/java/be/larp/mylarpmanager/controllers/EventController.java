package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadPrivilegesException;
import be.larp.mylarpmanager.exceptions.BadRequestException;
import be.larp.mylarpmanager.models.uuid.Character;
import be.larp.mylarpmanager.models.uuid.Event;
import be.larp.mylarpmanager.models.uuid.EventParticipation;
import be.larp.mylarpmanager.models.uuid.Nation;
import be.larp.mylarpmanager.requests.*;
import be.larp.mylarpmanager.services.CharacterService;
import be.larp.mylarpmanager.services.EventParticipationService;
import be.larp.mylarpmanager.services.EventService;
import be.larp.mylarpmanager.services.NationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/event")
public class EventController extends Controller {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventParticipationService eventParticipationService;

    @Autowired
    private NationService nationService;

    @Autowired
    private CharacterService characterService;

    @PostMapping("/changedetails")
    public ResponseEntity<?> changeEventDetails(@Valid @RequestBody ChangeEventDetailsRequest changeEventDetailsRequest) {
        if (requesterIsAdmin()) {
            Event event = eventService.getEventByUuid(changeEventDetailsRequest.getUuid());
            setValues(changeEventDetailsRequest, event);
            trace(getRequestUser(), "update event", event);
            return ResponseEntity.ok(event);
        } else {
            throw new BadPrivilegesException();
        }
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteEvent(@PathVariable String uuid) {
        if (requesterIsAdmin()) {
            Event event = eventService.getEventByUuid(uuid);
            eventService.delete(event);
            return ResponseEntity.ok().build();
        } else {
            throw new BadPrivilegesException();
        }
    }

    @PostMapping("/participate")
    public ResponseEntity<?> participate(@Valid @RequestBody ParticipateEventRequest participateEventRequest) {
        if (requesterIsAdmin() || requesterIsNationAdmin() || requesterIsOrga()) {
            Event event = eventService.getEventByUuid(participateEventRequest.getEventUuid());
            Nation nation = nationService.getSkillByUuid(participateEventRequest.getNationUuid());
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
            EventParticipation eventParticipation = eventParticipationService.getEventParticipationByUuid(setChosenOneRequest.getEventParticipationUuid());
            Character chosenOne = characterService.getCharacterByUuid(setChosenOneRequest.getCharacterUuid());
            if (!chosenOne.getPlayer().getNation().equals(eventParticipation.getNation())) {
                throw new BadRequestException("Cannot set chosen one for a character not inside the nation.");
            }
            eventParticipation.setChosenOne(chosenOne);
            eventParticipationService.save(eventParticipation);
            trace(getRequestUser(), "set chosen one", eventParticipation);
            return ResponseEntity.ok(eventParticipation);
        } else {
            throw new BadPrivilegesException();
        }
    }

    @PostMapping("/setsummary")
    public ResponseEntity<?> setConclusion(@Valid @RequestBody SetSummaryRequest setSummaryRequest) {
        if (requesterIsAdmin() || requesterIsNationAdmin() || requesterIsOrga()) {
            EventParticipation eventParticipation = eventParticipationService.getEventParticipationByUuid(setSummaryRequest.getEventParticipationUuid());
            eventParticipation.setSummary(setSummaryRequest.getSummary());
            eventParticipationService.save(eventParticipation);
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
        event.setConcluded(createEventRequest.isConcluded())
                .setDateFrom(createEventRequest.getDateFrom())
                .setDateTo(createEventRequest.getDateTo())
                .setDeadlineLogisticPack(createEventRequest.getDeadlineLogisticPack())
                .setYearTI(createEventRequest.getYearTI())
                .setName(createEventRequest.getName())
                .setDescription(createEventRequest.getDescription());
        eventService.save(event);
    }
}

