package be.larp.mylarpmanager.requests;

public class JoinNationRequest extends ForceJoinNationRequest{

    private String motivation;

    public JoinNationRequest() {
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }
}
