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

    public CreateSkillRequest setName(TranslatedItem name) {
        this.name = name;
        return this;
    }

    public boolean isHidden() {
        return hidden;
    }

    public CreateSkillRequest setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public TranslatedItem getDescription() {
        return description;
    }

    public CreateSkillRequest setDescription(TranslatedItem description) {
        this.description = description;
        return this;
    }

    public int getCost() {
        return cost;
    }

    public CreateSkillRequest setCost(int cost) {
        this.cost = cost;
        return this;
    }

    public String getIconURL() {
        return iconURL;
    }

    public CreateSkillRequest setIconURL(String iconURL) {
        this.iconURL = iconURL;
        return this;
    }

    public String getSkillTreeUuid() {
        return skillTreeUuid;
    }

    public CreateSkillRequest setSkillTreeUuid(String skillTreeUuid) {
        this.skillTreeUuid = skillTreeUuid;
        return this;
    }

    public boolean isAllowMultiple() {
        return allowMultiple;
    }

    public CreateSkillRequest setAllowMultiple(boolean allowMultiple) {
        this.allowMultiple = allowMultiple;
        return this;
    }

    public int getLevel() {
        return level;
    }

    public CreateSkillRequest setLevel(int level) {
        this.level = level;
        return this;
    }
}
