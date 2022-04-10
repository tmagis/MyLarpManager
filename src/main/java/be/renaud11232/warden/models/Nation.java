package be.renaud11232.warden.models;

import javax.persistence.*;

@Entity
@Table(name = "NATION")
public class Nation extends UuidModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NATION_ID")
    private Long id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "INTRO_TEXT", nullable = false)
    private String introText;

    @Column(name = "FULL_DESCRIPTION", nullable = false)
    private String fullDescription;

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
}
