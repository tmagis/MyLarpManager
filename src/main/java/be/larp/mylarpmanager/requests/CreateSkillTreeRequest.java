package be.larp.mylarpmanager.requests;

import javax.validation.constraints.NotBlank;

public class CreateSkillTreeRequest {


    @NotBlank(message = "SkillTree name is required.")
    private String name;

    @NotBlank(message = "SkillTree description is required.")
    private String description;

    @NotBlank(message = "SkillTree blessing is required.")
    private String blessing;

    public CreateSkillTreeRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBlessing() {
        return blessing;
    }

    public void setBlessing(String blessing) {
        this.blessing = blessing;
    }
}
