package be.larp.mylarpmanager.requests;

import javax.validation.constraints.NotBlank;

public class ChangeNationDetailsRequest extends CreateNationRequest implements GenericUuidBasedRequest {

    @NotBlank(message = "Nation uuid is required.")
    private String uuid;

    @Override
    public String getUuid() {
        return this.uuid;
    }

    @Override
    public ChangeNationDetailsRequest setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public ChangeNationDetailsRequest() {
    }

}
