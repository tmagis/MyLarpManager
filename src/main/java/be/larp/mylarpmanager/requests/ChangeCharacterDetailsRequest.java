package be.larp.mylarpmanager.requests;

import javax.validation.constraints.NotBlank;

public class ChangeCharacterDetailsRequest extends CreateCharacterRequest implements GenericUuidBasedRequest {

    @NotBlank(message = "Character uuid is required.")
    private String uuid;

    public ChangeCharacterDetailsRequest() {
    }

    @Override
    public String getUuid() {
        return this.uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
