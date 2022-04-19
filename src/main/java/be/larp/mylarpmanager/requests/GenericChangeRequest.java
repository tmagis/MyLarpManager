package be.larp.mylarpmanager.requests;

import javax.validation.constraints.NotBlank;

public class GenericChangeRequest {

    @NotBlank(message = "Uuid is required.")
    private String uuid;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
