package be.larp.mylarpmanager.models.uuid;

import be.larp.mylarpmanager.models.TranslatedItem;
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

    @Column(name="MAIN_DEITY")
    private String mainDeity;

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

    public Nation setJoinNationDemands(List<JoinNationDemand> joinNationDemands) {
        this.joinNationDemands = joinNationDemands;
        return this;
    }

    public TranslatedItem getName() {
        return name;
    }

    public Nation setName(TranslatedItem name) {
        this.name = name;
        return this;
    }

    public TranslatedItem getIntroText() {
        return introText;
    }

    public Nation setIntroText(TranslatedItem introText) {
        this.introText = introText;
        return this;
    }

    public TranslatedItem getFullDescription() {
        return fullDescription;
    }

    public Nation setFullDescription(TranslatedItem fullDescription) {
        this.fullDescription = fullDescription;
        return this;
    }

    @JsonIgnore
    public List<User> getPlayers() {
        return players;
    }

    public Nation setPlayers(List<User> players) {
        this.players = players;
        return this;
    }

    public boolean isFamilyFriendly() {
        return familyFriendly;
    }

    public Nation setFamilyFriendly(boolean familyFriendly) {
        this.familyFriendly = familyFriendly;
        return this;
    }

    public boolean isInternationalFriendly() {
        return internationalFriendly;
    }

    public Nation setInternationalFriendly(boolean internationalFriendly) {
        this.internationalFriendly = internationalFriendly;
        return this;
    }

    public int getContributionInCents() {
        return contributionInCents;
    }

    public Nation setContributionInCents(int contributionInCents) {
        this.contributionInCents = contributionInCents;
        return this;
    }

    public boolean isContributionMandatory() {
        return contributionMandatory;
    }

    public Nation setContributionMandatory(boolean contributionMandatory) {
        this.contributionMandatory = contributionMandatory;
        return this;
    }

    public String getMainDeity() {
        return mainDeity;
    }

    public Nation setMainDeity(String mainDeity) {
        this.mainDeity = mainDeity;
        return this;
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

    public Nation() {
        setUuid();
    }
}
