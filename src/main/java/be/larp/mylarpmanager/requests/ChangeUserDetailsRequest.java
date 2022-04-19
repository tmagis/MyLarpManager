package be.larp.mylarpmanager.requests;

import javax.validation.constraints.NotBlank;

public class ChangeUserDetailsRequest extends CreateUserRequest implements GenericUuidBasedRequest {
    @NotBlank(message = "User uuid is required.")
    private String uuid;

    @Override
    public String getUuid() {
        return this.uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public ChangeUserDetailsRequest() {
    }
}
