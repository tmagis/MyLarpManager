package be.larp.mylarpmanager.requests;


import javax.validation.constraints.NotBlank;

public class ChangeEventDetailsRequest extends CreateEventRequest implements GenericUuidBasedRequest{

    public ChangeEventDetailsRequest() {

    }

    @NotBlank(message = "Event uuid is required.")
    private String uuid;

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
