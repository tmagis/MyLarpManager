package be.larp.mylarpmanager.requests;


import javax.validation.constraints.NotBlank;

public class ChangeSkillDetailsRequest {

    @NotBlank(message = "Skill uuid is required.")
    private String uuid;

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

    public ChangeSkillDetailsRequest() {
    }

    public ChangeSkillDetailsRequest(String uuid, String name, String description, int cost, String iconURL, String skillTreeUuid, boolean allowMultiple, int level, boolean hidden) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.iconURL = iconURL;
        this.skillTreeUuid = skillTreeUuid;
        this.allowMultiple = allowMultiple;
        this.level = level;
        this.hidden = hidden;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
