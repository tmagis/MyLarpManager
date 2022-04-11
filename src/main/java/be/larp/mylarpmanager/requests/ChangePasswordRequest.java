package be.larp.mylarpmanager.requests;

import javax.validation.constraints.NotBlank;

public class ChangePasswordRequest {

    @NotBlank(message = "Current password is required")
    private String currentPassword;
    @NotBlank(message = "New password is required")
    private String newPassword;
    @NotBlank(message = "New password confirmation is required")
    private String newPasswordConfirmation;

    public ChangePasswordRequest() {
    }

    public ChangePasswordRequest(String password, String newPassword, String newPasswordConfirmation) {
        this.currentPassword = password;
        this.newPassword = newPassword;
        this.newPasswordConfirmation = newPasswordConfirmation;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
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
}
