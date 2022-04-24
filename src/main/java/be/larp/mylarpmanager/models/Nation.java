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

    @OneToOne(optional = false, cascade = CascadeType.PERSIST)
    private TranslatedItem name;

    @OneToOne(cascade = CascadeType.PERSIST)
    private TranslatedItem introText;

    @OneToOne(cascade = CascadeType.PERSIST)
    private TranslatedItem fullDescription;

    @OneToMany(mappedBy = "nation")
    private List<User> players;

    @Column(name = "FAMILY_FRIENDLY", nullable = false)
    private boolean familyFriendly;

    @Column(name = "INTERNATIONAL_FRIENDLY", nullable = false)
    private boolean internationalFriendly;

    @Column(name = "CONTRIBUTION_IN_CENTS")
    private int contributionInCents;

    @Column(name = "CONTRIBUTION_MANDATORY", nullable = false)
    private boolean contributionMandatory;

    @OneToMany(mappedBy = "nation")
    private List<JoinNationDemand> joinNationDemands;

    @JsonIgnore
    public List<JoinNationDemand> getJoinNationDemands() {
        return joinNationDemands;
    }

    public void setJoinNationDemands(List<JoinNationDemand> joinNationDemands) {
        this.joinNationDemands = joinNationDemands;
    }

    public TranslatedItem getName() {
        return name;
    }

    public void setName(TranslatedItem name) {
        this.name = name;
    }

    public TranslatedItem getIntroText() {
        return introText;
    }

    public void setIntroText(TranslatedItem introText) {
        this.introText = introText;
    }

    public TranslatedItem getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(TranslatedItem fullDescription) {
        this.fullDescription = fullDescription;
    }

    @JsonIgnore
    public List<User> getPlayers() {
        return players;
    }

    public void setPlayers(List<User> players) {
        this.players = players;
    }

    public boolean isFamilyFriendly() {
        return familyFriendly;
    }

    public void setFamilyFriendly(boolean familyFriendly) {
        this.familyFriendly = familyFriendly;
    }

    public boolean isInternationalFriendly() {
        return internationalFriendly;
    }

    public void setInternationalFriendly(boolean internationalFriendly) {
        this.internationalFriendly = internationalFriendly;
    }

    public int getContributionInCents() {
        return contributionInCents;
    }

    public void setContributionInCents(int contributionInCents) {
        this.contributionInCents = contributionInCents;
    }

    public boolean isContributionMandatory() {
        return contributionMandatory;
    }

    public void setContributionMandatory(boolean contributionMandatory) {
        this.contributionMandatory = contributionMandatory;
    }

    @Override
    public String toString() {
        return "Nation{" +
                "name='" + name + '\'' +
                ", introText='" + introText + '\'' +
                ", fullDescription='" + fullDescription + '\'' +
                ", familyFriendly=" + familyFriendly +
                ", internationalFriendly=" + internationalFriendly +
                ", contributionInCents=" + contributionInCents +
                ", contributionMandatory=" + contributionMandatory +
                '}';
    }
}
