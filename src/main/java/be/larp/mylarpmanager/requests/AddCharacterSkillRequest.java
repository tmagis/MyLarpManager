package be.larp.mylarpmanager.requests;

import javax.validation.constraints.NotBlank;

public class AddCharacterSkillRequest {

    @NotBlank(message = "Character uuid is required.")
    private String characterUuid;

    @NotBlank(message = "Skill uuid is required.")
    private String skillUuid;

    public AddCharacterSkillRequest(){}

    public String getCharacterUuid() {
        return characterUuid;
    }

    public void setCharacterUuid(String characterUuid) {
        this.characterUuid = characterUuid;
    }

    public String getSkillUuid() {
        return skillUuid;
    }

    public void setSkillUuid(String skillUuid) {
        this.skillUuid = skillUuid;
    }
}
