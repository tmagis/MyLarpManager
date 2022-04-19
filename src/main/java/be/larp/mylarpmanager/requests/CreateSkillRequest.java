package be.larp.mylarpmanager.requests;


import javax.validation.constraints.NotBlank;

public class CreateSkillRequest {

    @NotBlank(message = "Skill name is required.")
    private String name;

    @NotBlank(message = "Skill hidden is required.")
    private boolean hidden;

    @NotBlank(message = "Skill description is required.")
    private String description;

    @NotBlank(message = "Skill cost is required.")
    private int cost;

    private String iconURL;

    @NotBlank(message = "SkillTree uuid is required.")
    private String skillTreeUuid;

    @NotBlank(message = "Skill duplication authorized is required.")
    private boolean allowMultiple;

    @NotBlank(message = "Skill level is required.")
    private int level;

    public CreateSkillRequest() {
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
