package be.larp.mylarpmanager.requests;

import javax.validation.constraints.NotBlank;

public class ChangeCharacterDetailsRequest {

    @NotBlank(message = "Character uuid is required.")
    private String uuid;

    @NotBlank(message = "Character name is required.")
    private String name;

    @NotBlank(message = "Character race is required.")
    private String race;

    private String pictureURL;

    private String background;


    private int age;

    private String reasonOfDeath;

    public ChangeCharacterDetailsRequest(String uuid, String name, String pictureURL, String background, int age, String reasonOfDeath, String race) {
        this.uuid = uuid;
        this.name = name;
        this.pictureURL = pictureURL;
        this.background = background;
        this.age = age;
        this.reasonOfDeath = reasonOfDeath;
        this.race = race;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getReasonOfDeath() {
        return reasonOfDeath;
    }

    public void setReasonOfDeath(String reasonOfDeath) {
        this.reasonOfDeath = reasonOfDeath;
    }

    public ChangeCharacterDetailsRequest() {
    }

}
