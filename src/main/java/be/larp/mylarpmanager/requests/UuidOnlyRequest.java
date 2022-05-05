package be.larp.mylarpmanager.requests;

import javax.validation.constraints.NotBlank;

public class UuidOnlyRequest implements GenericUuidBasedRequest{

    @NotBlank(message = "Uuid is required.")
    private String uuid;

    public UuidOnlyRequest(){}

    @Override
    public String getUuid() {
        return this.uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
