package be.larp.mylarpmanager.requests;

import be.larp.mylarpmanager.models.TranslatedItem;

import javax.validation.constraints.NotNull;

public class CreateNationRequest {

    @NotNull(message = "Nation name is required.")
    private TranslatedItem name;

    @NotNull(message = "Nation familyFriendly attribute is required.")
    private boolean familyFriendly;

    @NotNull(message = "Nation internationalFriendly attribute is required.")
    private boolean internationalFriendly;

    private int contributionInCents;

    @NotNull(message = "Nation contributionMandatory attribute is required.")
    private boolean contributionMandatory;

    private TranslatedItem introText;

    private TranslatedItem fullDescription;

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
