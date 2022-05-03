package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadPrivilegesException;
import be.larp.mylarpmanager.models.uuid.Character;
import be.larp.mylarpmanager.models.uuid.EventParticipation;
import be.larp.mylarpmanager.models.uuid.PointHistory;
import be.larp.mylarpmanager.repositories.CharacterRepository;
import be.larp.mylarpmanager.repositories.EventParticipationRepository;
import be.larp.mylarpmanager.repositories.PointHistoryRepository;
import be.larp.mylarpmanager.requests.ChangePointHistoryDetailsRequest;
import be.larp.mylarpmanager.requests.CreatePointHistoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/point")
public class PointHistoryController extends Controller {

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private EventParticipationRepository eventParticipationRepository;

    @PostMapping("/changedetails")
    public ResponseEntity<?> changeNationDetails(@Valid @RequestBody ChangePointHistoryDetailsRequest changePointHistoryDetailsRequest) {
        if (requesterIsAdmin() || requesterIsOrga()) {
            PointHistory pointHistory = pointHistoryRepository.findByUuid(changePointHistoryDetailsRequest.getUuid())
                    .orElseThrow(() -> new NoSuchElementException("PointHistory with uuid " + changePointHistoryDetailsRequest.getUuid() + " not found."));
            setValues(changePointHistoryDetailsRequest, pointHistory);
            trace(getRequestUser(), "update pointhistory", pointHistory);
            return ResponseEntity.ok(pointHistory);
        } else {
            throw new BadPrivilegesException();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEvent(@Valid @RequestBody CreatePointHistoryRequest createPointHistoryRequest) {
        if (requesterIsAdmin()) {
            PointHistory pointHistory = new PointHistory();

            setValues(createPointHistoryRequest, pointHistory);
            trace(getRequestUser(), "create pointhistory", pointHistory);
            return ResponseEntity.ok(pointHistory);
        } else {
            throw new BadPrivilegesException();
        }
    }

    private void setValues(CreatePointHistoryRequest createPointHistoryRequest, PointHistory pointHistory) {
        pointHistory.setPoints(createPointHistoryRequest.getPoints());
        pointHistory.setReason(createPointHistoryRequest.getReason());
        pointHistory.setAwardedBy(getRequestUser());
        EventParticipation eventParticipation = eventParticipationRepository.findByUuid(createPointHistoryRequest.getEventParticipationUuid())
                .orElseThrow(() -> new NoSuchElementException("EventParticipation with uuid " + createPointHistoryRequest.getEventParticipationUuid() + " not found."));
        pointHistory.setEventParticipation(eventParticipation);
        if (createPointHistoryRequest.getAwardedToCharacterUuid() != null && !createPointHistoryRequest.getAwardedToCharacterUuid().isBlank()) {
            Character character = characterRepository.findByUuid(createPointHistoryRequest.getAwardedToCharacterUuid())
                    .orElseThrow(() -> new NoSuchElementException("Character with uuid " + createPointHistoryRequest.getAwardedToCharacterUuid() + " not found."));
            pointHistory.setAwardedTo(character);
        }
        pointHistoryRepository.saveAndFlush(pointHistory);
    }
}

