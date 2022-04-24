package be.larp.mylarpmanager.requests;

import javax.validation.constraints.NotBlank;

public class ForceJoinNationRequest {

    @NotBlank(message = "Nation uuid is required.")
    private String nationUuid;

    @NotBlank(message = "Player uuid is required.")
    private String playerUuid;

    public ForceJoinNationRequest(){}

    public String getNationUuid() {
        return nationUuid;
    }

    public void setNationUuid(String nationUuid) {
        this.nationUuid = nationUuid;
    }

    public String getPlayerUuid() {
        return playerUuid;
    }

    public void setPlayerUuid(String playerUuid) {
        this.playerUuid = playerUuid;
    }
}
