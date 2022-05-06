package be.larp.mylarpmanager.requests;


import javax.validation.constraints.NotBlank;

public class SetSummaryRequest {

    @NotBlank(message = "EventParticipation uuid is required.")
    private String eventParticipationUuid;

    @NotBlank(message = "Summary is required.")
    private String summary;

    public SetSummaryRequest() {
    }

    public String getEventParticipationUuid() {
        return eventParticipationUuid;
    }

    public SetSummaryRequest setEventParticipationUuid(String eventParticipationUuid) {
        this.eventParticipationUuid = eventParticipationUuid;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public SetSummaryRequest setSummary(String summary) {
        this.summary = summary;
        return this;
    }
}
