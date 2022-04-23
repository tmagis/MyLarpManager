package be.larp.mylarpmanager.requests;

import be.larp.mylarpmanager.models.TranslatedLabel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateSkillTreeRequest {


    @NotNull(message = "SkillTree name is required.")
    private TranslatedLabel name;

    @NotNull(message = "SkillTree description is required.")
    private TranslatedLabel description;

    @NotNull(message = "SkillTree blessing is required.")
    private TranslatedLabel blessing;

    public CreateSkillTreeRequest() {
    }

    public TranslatedLabel getName() {
        return name;
    }

    public void setName(TranslatedLabel name) {
        this.name = name;
    }

    public TranslatedLabel getDescription() {
        return description;
    }

    public void setDescription(TranslatedLabel description) {
        this.description = description;
    }

    public TranslatedLabel getBlessing() {
        return blessing;
    }

    public void setBlessing(TranslatedLabel blessing) {
        this.blessing = blessing;
    }
}
