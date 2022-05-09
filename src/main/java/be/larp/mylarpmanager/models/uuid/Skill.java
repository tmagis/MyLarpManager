package be.larp.mylarpmanager.models.uuid;

import be.larp.mylarpmanager.models.TranslatedItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

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

    @Column(name = "ICON_URL", columnDefinition = "LONGTEXT")
    private String iconURL;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private SkillTree skillTree;

    @Column(name = "ALLOW_MULTIPLE", nullable = false)
    private boolean allowMultiple;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade =
                    {
                            CascadeType.DETACH,
                            CascadeType.MERGE,
                            CascadeType.REFRESH,
                            CascadeType.PERSIST
                    },
            targetEntity = Character.class)
    @JoinTable(name = "SKILL_CHARACTER",
            joinColumns = @JoinColumn(name = "SKILL_ID",
                    nullable = false,
                    updatable = false),
            inverseJoinColumns = @JoinColumn(name = "CHARACTER_ID",
                    nullable = false,
                    updatable = false),
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Collection<Character> characters;

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

    public Skill setName(TranslatedItem name) {
        this.name = name;
        return this;
    }

    public TranslatedItem getDescription() {
        return description;
    }

    public Skill setDescription(TranslatedItem description) {
        this.description = description;
        return this;
    }

    public int getCost() {
        return cost;
    }

    public Skill setCost(int cost) {
        this.cost = cost;
        return this;
    }

    public String getIconURL() {
        return iconURL;
    }

    public Skill setIconURL(String iconURL) {
        this.iconURL = iconURL;
        return this;
    }

    public SkillTree getSkillTree() {
        return skillTree;
    }

    public Skill setSkillTree(SkillTree skillTree) {
        this.skillTree = skillTree;
        return this;
    }

    public boolean isAllowMultiple() {
        return allowMultiple;
    }

    public Skill setAllowMultiple(boolean allowMultiple) {
        this.allowMultiple = allowMultiple;
        return this;
    }

    public int getLevel() {
        return level;
    }

    public Skill setLevel(int level) {
        this.level = level;
        return this;
    }

    public boolean isHidden() {
        return hidden;
    }

    public Skill setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    @JsonIgnore
    public Collection<Character> getCharacters() {
        return characters;
    }

    public Skill setCharacters(Collection<Character> characters) {
        this.characters = characters;
        return this;
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
