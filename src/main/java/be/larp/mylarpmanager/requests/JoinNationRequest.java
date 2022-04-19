package be.larp.mylarpmanager.requests;

import javax.validation.constraints.NotBlank;

public class JoinNationRequest {

    @NotBlank(message = "Nation uuid is required.")
    private String uuid;

    public JoinNationRequest(){}

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
