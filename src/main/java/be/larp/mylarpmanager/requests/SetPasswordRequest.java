package be.larp.mylarpmanager.requests;

import javax.validation.constraints.NotBlank;

public class SetPasswordRequest {

    @NotBlank(message = "New password is required")
    private String newPassword;
    @NotBlank(message = "New password confirmation is required")
    private String newPasswordConfirmation;
    @NotBlank(message = "Token is required")
    private String token;

    public SetPasswordRequest() {
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirmation() {
        return newPasswordConfirmation;
    }

    public void setNewPasswordConfirmation(String newPasswordConfirmation) {
        this.newPasswordConfirmation = newPasswordConfirmation;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
