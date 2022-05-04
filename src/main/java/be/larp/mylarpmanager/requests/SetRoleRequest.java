package be.larp.mylarpmanager.requests;

import javax.validation.constraints.NotBlank;

public class SetRoleRequest {

    @NotBlank(message = "User uuid is required")
    private String userUuid;

    @NotBlank(message = "Role is required")
    private String role;

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public SetRoleRequest() {
    }

}
