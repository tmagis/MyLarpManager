package be.larp.mylarpmanager.models.uuid;

import be.larp.mylarpmanager.models.TranslatedItem;

import javax.persistence.*;

@Entity
@Table(name = "SKILL")
public class Skill extends UuidModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SKILL_ID")
    private Long id;

    @OneToOne(optional = false, cascade = CascadeType.PERSIST)
    private TranslatedItem name;

    @OneToOne(optional = false, cascade = CascadeType.PERSIST)
    private TranslatedItem description;

    @Column(name = "COST", nullable = false)
    private int cost;

    @Column(name = "ICON_URL")
    private String iconURL;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private SkillTree skillTree;

    @Column(name = "ALLOW_MULTIPLE", nullable = false)
    private boolean allowMultiple;

    @Column(name = "LEVEL", nullable = false)
    private int level;

    @Column(name = "HIDDEN", nullable = false)
    private boolean hidden;

    public Skill() {
        setUuid();
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

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String icon) {
        this.iconURL = icon;
    }

    public SkillTree getSkillTree() {
        return skillTree;
    }

    public void setSkillTree(SkillTree skillTree) {
        this.skillTree = skillTree;
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

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cost=" + cost +
                ", iconURL='" + iconURL + '\'' +
                ", allowMultiple=" + allowMultiple +
                ", level=" + level +
                ", hidden=" + hidden +
                '}';
    }
}
