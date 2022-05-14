package be.larp.mylarpmanager.controllers;

import be.larp.mylarpmanager.exceptions.BadPrivilegesException;
import be.larp.mylarpmanager.models.uuid.Character;
import be.larp.mylarpmanager.models.uuid.EventParticipation;
import be.larp.mylarpmanager.models.uuid.PointHistory;
import be.larp.mylarpmanager.requests.ChangePointHistoryDetailsRequest;
import be.larp.mylarpmanager.requests.CreatePointHistoryRequest;
import be.larp.mylarpmanager.services.CharacterService;
import be.larp.mylarpmanager.services.EventParticipationService;
import be.larp.mylarpmanager.services.PointHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@RestController
@RequestMapping("/api/v1/point")
public class PointHistoryController extends Controller {

    @Autowired
    private PointHistoryService pointHistoryService;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private EventParticipationService eventParticipationService;

    @PostMapping("/changeDetails")
    public ResponseEntity<?> changePointHistory(@Valid @RequestBody ChangePointHistoryDetailsRequest changePointHistoryDetailsRequest) {
        if (requesterIsAdmin() || requesterIsOrga()) {
            PointHistory pointHistory = pointHistoryService.getPointHistoryByUuid(changePointHistoryDetailsRequest.getUuid());
            setValues(changePointHistoryDetailsRequest, pointHistory);
            trace(getRequestUser(), "update pointhistory", pointHistory);
            return ResponseEntity.ok(pointHistory);
        } else {
            throw new BadPrivilegesException();
        }
    }


    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deletePointHistory(@PathVariable String uuid) {
        if (requesterIsAdmin()) {
            PointHistory pointHistory = pointHistoryService.getPointHistoryByUuid(uuid);
            pointHistoryService.delete(pointHistory);
            return ResponseEntity.ok().build();
        } else {
            throw new BadPrivilegesException();
        }
    }


    @PostMapping("/create")
    public ResponseEntity<?> createPointHistory(@Valid @RequestBody CreatePointHistoryRequest createPointHistoryRequest) {
        if (requesterIsAdmin() || requesterIsOrga()) {
            PointHistory pointHistory = new PointHistory();
            setValues(createPointHistoryRequest, pointHistory);
            trace(getRequestUser(), "create pointhistory", pointHistory);
            return ResponseEntity.ok(pointHistory);
        } else {
            throw new BadPrivilegesException();
        }
    }

    private void setValues(CreatePointHistoryRequest createPointHistoryRequest, PointHistory pointHistory) {
        EventParticipation eventParticipation = eventParticipationService.getEventParticipationByUuid(createPointHistoryRequest.getEventParticipationUuid());
        pointHistory.setPoints(createPointHistoryRequest.getPoints())
                .setReason(createPointHistoryRequest.getReason())
                .setAwardedBy(getRequestUser())
                .setEventParticipation(eventParticipation);
        if (createPointHistoryRequest.getAwardedToCharacterUuid() != null && !createPointHistoryRequest.getAwardedToCharacterUuid().isBlank()) {
            Character character = characterService.getCharacterByUuid(createPointHistoryRequest.getAwardedToCharacterUuid());
            pointHistory.setAwardedTo(character);
        }
        pointHistoryService.save(pointHistory);
    }
}

