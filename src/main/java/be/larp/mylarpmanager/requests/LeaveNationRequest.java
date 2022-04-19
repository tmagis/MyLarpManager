package be.larp.mylarpmanager.requests;

import javax.validation.constraints.NotBlank;

public class LeaveNationRequest implements GenericUuidBasedRequest{

    @NotBlank(message = "Player uuid is required.")
    private String uuid;

    public LeaveNationRequest(){}

    @Override
    public String getUuid() {
        return this.uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
