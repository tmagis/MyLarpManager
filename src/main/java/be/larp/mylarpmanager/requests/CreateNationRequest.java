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

    public CreateNationRequest setName(TranslatedItem name) {
        this.name = name;
        return this;
    }

    public boolean isFamilyFriendly() {
        return familyFriendly;
    }

    public CreateNationRequest setFamilyFriendly(boolean familyFriendly) {
        this.familyFriendly = familyFriendly;
        return this;
    }

    public boolean isInternationalFriendly() {
        return internationalFriendly;
    }

    public CreateNationRequest setInternationalFriendly(boolean internationalFriendly) {
        this.internationalFriendly = internationalFriendly;
        return this;
    }

    public int getContributionInCents() {
        return contributionInCents;
    }

    public CreateNationRequest setContributionInCents(int contributionInCents) {
        this.contributionInCents = contributionInCents;
        return this;
    }

    public boolean isContributionMandatory() {
        return contributionMandatory;
    }

    public CreateNationRequest setContributionMandatory(boolean contributionMandatory) {
        this.contributionMandatory = contributionMandatory;
        return this;
    }

    public TranslatedItem getIntroText() {
        return introText;
    }

    public CreateNationRequest setIntroText(TranslatedItem introText) {
        this.introText = introText;
        return this;
    }

    public TranslatedItem getFullDescription() {
        return fullDescription;
    }

    public CreateNationRequest setFullDescription(TranslatedItem fullDescription) {
        this.fullDescription = fullDescription;
        return this;
    }

    public CreateNationRequest() {
    }

}
