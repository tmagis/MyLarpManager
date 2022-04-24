package be.larp.mylarpmanager.requests;

import be.larp.mylarpmanager.models.TranslatedItem;

import javax.validation.constraints.NotNull;

public class CreateSkillTreeRequest {


    @NotNull(message = "SkillTree name is required.")
    private TranslatedItem name;

    @NotNull(message = "SkillTree description is required.")
    private TranslatedItem description;

    @NotNull(message = "SkillTree blessing is required.")
    private TranslatedItem blessing;

    public CreateSkillTreeRequest() {
    }

    public TranslatedItem getName() {
        return name;
    }

    public void setName(TranslatedItem name) {
        this.name = name;
    }

    public TranslatedItem getDescription() {
        return description;
    }

    public void setDescription(TranslatedItem description) {
        this.description = description;
    }

    public TranslatedItem getBlessing() {
        return blessing;
    }

    public void setBlessing(TranslatedItem blessing) {
        this.blessing = blessing;
    }
}
