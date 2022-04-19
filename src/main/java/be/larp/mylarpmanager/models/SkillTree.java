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

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "BLESSING", nullable = false)
    private String blessing;

    @OneToMany(mappedBy = "skillTree")
    private Collection<Skill> skills;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBlessing() {
        return blessing;
    }

    public void setBlessing(String blessing) {
        this.blessing = blessing;
    }

    public Collection<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Collection<Skill> skills) {
        this.skills = skills;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
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
