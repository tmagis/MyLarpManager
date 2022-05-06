package be.larp.mylarpmanager.requests;


import javax.validation.constraints.NotBlank;

public class SetChosenOneRequest {

    @NotBlank(message = "Character uuid is required.")
    private String characterUuid;

    @NotBlank(message = "EventParticipation uuid is required.")
    private String eventParticipationUuid;

    public SetChosenOneRequest() {
    }

    public String getCharacterUuid() {
        return characterUuid;
    }

    public SetChosenOneRequest setCharacterUuid(String characterUuid) {
        this.characterUuid = characterUuid;
        return this;
    }

    public String getEventParticipationUuid() {
        return eventParticipationUuid;
    }

    public SetChosenOneRequest setEventParticipationUuid(String eventParticipationUuid) {
        this.eventParticipationUuid = eventParticipationUuid;
        return this;
    }
}
