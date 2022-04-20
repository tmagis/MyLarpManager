package be.larp.mylarpmanager.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateNationRequest {

    @NotBlank(message = "Nation name is required.")
    private String name;

    @NotNull(message = "Nation familyFriendly attribute is required.")
    private boolean familyFriendly;

    @NotNull(message = "Nation internationalFriendly attribute is required.")
    private boolean internationalFriendly;

    private int contributionInCents;

    @NotNull(message = "Nation contributionMandatory attribute is required.")
    private boolean contributionMandatory;

    private String introText;

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

    public CreateNationRequest() {
    }

}
