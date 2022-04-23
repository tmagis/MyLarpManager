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
    private TranslatedLabel name;

    @OneToOne(optional = false, cascade = CascadeType.PERSIST)
    private TranslatedLabel description;

    @OneToOne(optional = false, cascade = CascadeType.PERSIST)
    private TranslatedLabel blessing;

    @OneToMany(mappedBy = "skillTree")
    private Collection<Skill> skills;

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

    public Collection<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Collection<Skill> skills) {
        this.skills = skills;
    }

    public TranslatedLabel getName() {
        return name;
    }

    public void setName(TranslatedLabel name) {
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
