package be.larp.mylarpmanager.requests;

import javax.validation.constraints.NotBlank;

public class KillCharacterRequest implements GenericUuidBasedRequest{

    @NotBlank(message = "Character uuid is required.")
    private String uuid;

    private String reasonOfDeath;

    public KillCharacterRequest(){}

    public String getReasonOfDeath() {
        return reasonOfDeath;
    }

    public void setReasonOfDeath(String reasonOfDeath) {
        this.reasonOfDeath = reasonOfDeath;
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
