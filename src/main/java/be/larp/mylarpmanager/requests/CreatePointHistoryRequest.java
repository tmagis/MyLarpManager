package be.larp.mylarpmanager.requests;


import be.larp.mylarpmanager.models.TranslatedItem;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CreatePointHistoryRequest {

    @NotBlank(message = "EventParticipation uuid is required.")
    private String eventParticipationUuid;

    private String awardedToCharacterUuid;

    private int points;

    private String reason;
    public CreatePointHistoryRequest() {
    }

    public String getEventParticipationUuid() {
        return eventParticipationUuid;
    }

    public CreatePointHistoryRequest setEventParticipationUuid(String eventParticipationUuid) {
        this.eventParticipationUuid = eventParticipationUuid;
        return this;
    }

    public String getAwardedToCharacterUuid() {
        return awardedToCharacterUuid;
    }

    public CreatePointHistoryRequest setAwardedToCharacterUuid(String awardedToCharacterUuid) {
        this.awardedToCharacterUuid = awardedToCharacterUuid;
        return this;
    }

    public int getPoints() {
        return points;
    }

    public CreatePointHistoryRequest setPoints(int points) {
        this.points = points;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public CreatePointHistoryRequest setReason(String reason) {
        this.reason = reason;
        return this;
    }
}
