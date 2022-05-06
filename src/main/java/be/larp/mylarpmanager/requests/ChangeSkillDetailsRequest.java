package be.larp.mylarpmanager.requests;


import javax.validation.constraints.NotBlank;

public class ChangeSkillDetailsRequest extends CreateSkillRequest implements GenericUuidBasedRequest {

    @NotBlank(message = "Skill uuid is required.")
    private String uuid;

    @Override
    public String getUuid() {
        return this.uuid;
    }

    @Override
    public ChangeSkillDetailsRequest setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public ChangeSkillDetailsRequest() {
    }
}
