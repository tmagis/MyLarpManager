package be.larp.mylarpmanager.requests;

import javax.validation.constraints.NotBlank;

public class CreateCharacterRequest {

    @NotBlank(message = "Character name is required.")
    private String name;

    @NotBlank(message = "Character race is required.")
    private String race;

    private String pictureURL;

    private String background;

    private int age;

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

    public CreateCharacterRequest() {
    }

}
