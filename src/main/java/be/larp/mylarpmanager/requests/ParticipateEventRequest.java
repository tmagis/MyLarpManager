package be.larp.mylarpmanager.requests;

import javax.validation.constraints.NotBlank;

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

    public ParticipateEventRequest setEventUuid(String eventUuid) {
        this.eventUuid = eventUuid;
        return this;
    }

    public String getNationUuid() {
        return nationUuid;
    }

    public ParticipateEventRequest setNationUuid(String nationUuid) {
        this.nationUuid = nationUuid;
        return this;
    }
}
