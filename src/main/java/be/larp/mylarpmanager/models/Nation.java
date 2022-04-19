package be.larp.mylarpmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "NATION")
public class Nation extends UuidModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NATION_ID")
    private Long id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "INTRO_TEXT")
    private String introText;

    @Column(name = "FULL_DESCRIPTION")
    private String fullDescription;

    @OneToMany(mappedBy = "nation")
    private List<User> players;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroText() {
        return introText;
    }

    public void setIntroText(String introText) {
        this.introText = introText;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    @JsonIgnore
    public List<User> getPlayers() {
        return players;
    }

    public void setPlayers(List<User> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        return "Nation{" +
                "name='" + name + '\'' +
                ", introText='" + introText + '\'' +
                ", fullDescription='" + fullDescription + '\'' +
                '}';
    }
}
