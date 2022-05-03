package be.larp.mylarpmanager.requests;


import javax.validation.constraints.NotBlank;

public class ChangePointHistoryDetailsRequest extends CreatePointHistoryRequest implements GenericUuidBasedRequest {

    @NotBlank(message = "PointHistory uuid is required.")
    private String uuid;

    @Override
    public String getUuid() {
        return this.uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public ChangePointHistoryDetailsRequest() {
    }
}
