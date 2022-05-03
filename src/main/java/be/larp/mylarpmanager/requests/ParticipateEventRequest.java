package be.larp.mylarpmanager.requests;


import be.larp.mylarpmanager.models.TranslatedItem;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ParticipateEventRequest {

    @NotBlank(message = "Event uuid is required.")
    private String eventUuid;

    @NotBlank(message = "Nation uuid is required.")
    private String nationUuid;

    public ParticipateEventRequest() {
    }

    public String getEventUuid() {
        return eventUuid;
    }

    public void setEventUuid(String eventUuid) {
        this.eventUuid = eventUuid;
    }

    public String getNationUuid() {
        return nationUuid;
    }

    public void setNationUuid(String nationUuid) {
        this.nationUuid = nationUuid;
    }
}
