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

    public SetPasswordRequest setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }

    public String getNewPasswordConfirmation() {
        return newPasswordConfirmation;
    }

    public SetPasswordRequest setNewPasswordConfirmation(String newPasswordConfirmation) {
        this.newPasswordConfirmation = newPasswordConfirmation;
        return this;
    }

    public String getToken() {
        return token;
    }

    public SetPasswordRequest setToken(String token) {
        this.token = token;
        return this;
    }
}
