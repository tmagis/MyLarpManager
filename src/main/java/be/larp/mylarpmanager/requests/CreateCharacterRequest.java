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

    public CreateCharacterRequest setRace(String race) {
        this.race = race;
        return this;
    }

    public String getName() {
        return name;
    }


    public CreateCharacterRequest setName(String name) {
        this.name = name;
        return this;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public CreateCharacterRequest setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
        return this;
    }

    public String getBackground() {
        return background;
    }

    public CreateCharacterRequest setBackground(String background) {
        this.background = background;
        return this;
    }

    public int getAge() {
        return age;
    }

    public CreateCharacterRequest setAge(int age) {
        this.age = age;
        return this;
    }

    public CreateCharacterRequest() {
    }

}
