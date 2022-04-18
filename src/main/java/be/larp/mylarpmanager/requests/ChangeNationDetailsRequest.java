package be.larp.mylarpmanager.requests;

import javax.validation.constraints.NotBlank;

public class ChangeNationDetailsRequest {

    @NotBlank(message = "Nation uuid is required.")
    private String uuid;

    @NotBlank(message = "Nation name is required.")
    private String name;

    private String introText;

    private String fullDescription;

    public ChangeNationDetailsRequest(String uuid, String name, String introText, String fullDescription) {
        this.uuid = uuid;
        this.name = name;
        this.introText = introText;
        this.fullDescription = fullDescription;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

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
