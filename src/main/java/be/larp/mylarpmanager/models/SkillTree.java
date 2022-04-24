package be.larp.mylarpmanager.models;

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

    public Collection<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Collection<Skill> skills) {
        this.skills = skills;
    }

    public TranslatedItem getName() {
        return name;
    }

    public void setName(TranslatedItem name) {
        this.name = name;
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
