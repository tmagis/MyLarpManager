package be.larp.mylarpmanager.requests;


import be.larp.mylarpmanager.models.TranslatedItem;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateSkillRequest {

    @NotNull(message = "Skill name is required.")
    private TranslatedItem name;

    @NotNull(message = "Skill hidden is required.")
    private boolean hidden;

    @NotNull(message = "Skill description is required.")
    private TranslatedItem description;

    @NotNull(message = "Skill cost is required.")
    private int cost;

    private String iconURL;

    @NotBlank(message = "SkillTree uuid is required.")
    private String skillTreeUuid;

    @NotNull(message = "Skill duplication authorized is required.")
    private boolean allowMultiple;

    @NotNull(message = "Skill level is required.")
    private int level;

    public CreateSkillRequest() {
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

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public String getSkillTreeUuid() {
        return skillTreeUuid;
    }

    public void setSkillTreeUuid(String skillTreeUuid) {
        this.skillTreeUuid = skillTreeUuid;
    }

    public boolean isAllowMultiple() {
        return allowMultiple;
    }

    public void setAllowMultiple(boolean allowMultiple) {
        this.allowMultiple = allowMultiple;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
