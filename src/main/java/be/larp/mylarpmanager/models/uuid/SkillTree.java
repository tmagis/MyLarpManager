package be.larp.mylarpmanager.models.uuid;

import be.larp.mylarpmanager.models.TranslatedItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "SKILL_TREE")
public class SkillTree extends UuidModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SKILL_TREE_ID")
    private Long id;

    @OneToOne(optional = false, cascade = CascadeType.PERSIST)
    private TranslatedItem name;

    @OneToOne(optional = false, cascade = CascadeType.PERSIST)
    private TranslatedItem description;

    @OneToOne(optional = false, cascade = CascadeType.PERSIST)
    private TranslatedItem blessing;

    @OneToMany(mappedBy = "skillTree")
    private Collection<Skill> skills;

    public SkillTree() {
        setUuid();
    }

    public TranslatedItem getDescription() {
        return description;
    }

    public SkillTree setDescription(TranslatedItem description) {
        this.description = description;
        return this;
    }

    public TranslatedItem getBlessing() {
        return blessing;
    }

    public SkillTree setBlessing(TranslatedItem blessing) {
        this.blessing = blessing;
        return this;
    }

    @JsonIgnore
    public Collection<Skill> getSkills() {
        return skills;
    }

    public SkillTree setSkills(Collection<Skill> skills) {
        this.skills = skills;
        return this;
    }

    public TranslatedItem getName() {
        return name;
    }

    public SkillTree setName(TranslatedItem name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "SkillTree{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", blessing='" + blessing + '\'' +
                '}';
    }
}
