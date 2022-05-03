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

    public void setEventParticipationUuid(String eventParticipationUuid) {
        this.eventParticipationUuid = eventParticipationUuid;
    }

    public String getAwardedToCharacterUuid() {
        return awardedToCharacterUuid;
    }

    public void setAwardedToCharacterUuid(String awardedToCharacterUuid) {
        this.awardedToCharacterUuid = awardedToCharacterUuid;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
