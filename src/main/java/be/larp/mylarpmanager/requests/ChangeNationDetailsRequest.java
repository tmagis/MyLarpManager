package be.larp.mylarpmanager.requests;

import javax.validation.constraints.NotBlank;

public class ChangeNationDetailsRequest extends GenericChangeRequest{

    @NotBlank(message = "Nation name is required.")
    private String name;

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

    public ChangeNationDetailsRequest() {
    }

}
