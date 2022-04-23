package be.larp.mylarpmanager.requests;

import be.larp.mylarpmanager.models.TranslatedLabel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateNationRequest {

    @NotNull(message = "Nation name is required.")
    private TranslatedLabel name;

    @NotNull(message = "Nation familyFriendly attribute is required.")
    private boolean familyFriendly;

    @NotNull(message = "Nation internationalFriendly attribute is required.")
    private boolean internationalFriendly;

    private int contributionInCents;

    @NotNull(message = "Nation contributionMandatory attribute is required.")
    private boolean contributionMandatory;

    private TranslatedLabel introText;

    private TranslatedLabel fullDescription;

    public TranslatedLabel getName() {
        return name;
    }

    public void setName(TranslatedLabel name) {
        this.name = name;
    }

    public TranslatedLabel getIntroText() {
        return introText;
    }

    public void setIntroText(TranslatedLabel introText) {
        this.introText = introText;
    }

    public TranslatedLabel getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(TranslatedLabel fullDescription) {
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
