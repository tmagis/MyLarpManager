package be.larp.mylarpmanager.requests;

import javax.validation.constraints.NotBlank;

public class ChangeSkillTreeDetailsRequest extends CreateSkillTreeRequest implements GenericUuidBasedRequest {

    @NotBlank(message = "SkillTree uuid is required.")
    private String uuid;

    @Override
    public String getUuid() {
        return this.uuid;
    }

    @Override
    public ChangeSkillTreeDetailsRequest setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }
    public ChangeSkillTreeDetailsRequest() {
    }
}
